package com.tokopedia.product.addedit.preview.presentation.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.common.util.AddEditProductUploadErrorHandler
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TITLE_ERROR_DOWNLOAD_IMAGE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TITLE_ERROR_SAVING_DRAFT
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductAddUploadTracking
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * Created by faisalramd on 2020-04-05.
 */

/*
Step to submit add product:
(1). Save product data to draft and get productDraftId
(2). Upload product images and get uploadIdList
(3). Hit gql for add product
(4). clear product from draft if add data success
 */

open class AddEditProductAddService : AddEditProductBaseService() {
    protected var productDraftId = 0L
    protected var productInputModel: ProductInputModel = ProductInputModel()

    private var filename = String.EMPTY

    companion object {
        fun startService(context: Context, cacheId: String) {
            val work = Intent(context, AddEditProductBaseService::class.java).apply {
                putExtra(AddEditProductUploadConstant.EXTRA_CACHE_ID, cacheId)
            }
            enqueueWork(context, AddEditProductAddService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val cacheId = intent.getStringExtra(AddEditProductUploadConstant.EXTRA_CACHE_ID) ?: ""

        val saveInstanceCacheManager = SaveInstanceCacheManager(this, cacheId)
        productInputModel = saveInstanceCacheManager.get(AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()

        if (productInputModel.isDuplicate) {
            duplicateProductImages(productInputModel)
        } else {
            // (1)
            saveProductToDraftAsync()
        }
    }

    private fun duplicateProductImages(productInputModel: ProductInputModel) {
        launchCatchError(
            block = {
                asyncCatchError(
                    coroutineContext,
                    block = {
                        // re download product images + update new file path
                        val productDetail = productInputModel.detailInputModel
                        reDownloadProductImages(productDetail)
                        // re download product variant images + update new file path
                        val productVariant = productInputModel.variantInputModel.products
                        reDownloadProductVariantImages(productVariant)
                        // re download product variant size chart + update new file path
                        val variantSizeChart = productInputModel.variantInputModel.sizecharts
                        reDownloadVariantSizeChart(variantSizeChart)
                    },
                    onError = { throwable ->
                        AddEditProductErrorHandler.logMessage("$TITLE_ERROR_DOWNLOAD_IMAGE $filename")
                        AddEditProductErrorHandler.logExceptionToCrashlytics(throwable)
                    }
                ).await().run { saveProductToDraftAsync() } // save to draft and upload
            },
            onError = { throwable ->
                AddEditProductErrorHandler.logExceptionToCrashlytics(throwable)
            }
        )
    }

    private fun saveProductToDraftAsync() {
        launchCatchError(block = {
            asyncCatchError(
                coroutineContext,
                block = {
                    saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(
                        mapProductInputModelDetailToDraft(productInputModel),
                        productInputModel.draftId,
                        false
                    )
                    saveProductDraftUseCase.executeOnBackground()
                },
                onError = { throwable ->
                    logError(TITLE_ERROR_SAVING_DRAFT, throwable)
                    0L
                }
            ).await().let {
                productDraftId = it ?: 0L

                // (2)
                val detailInputModel = productInputModel.detailInputModel
                val variantInputModel = productInputModel.variantInputModel
                uploadProductImages(detailInputModel.imageUrlOrPathList, variantInputModel)
            }
        }, onError = { throwable ->
                logError(TITLE_ERROR_SAVING_DRAFT, throwable)
            })
    }

    override fun onUploadProductImagesSuccess(
        uploadIdList: ArrayList<String>,
        variantInputModel: VariantInputModel
    ) {
        // (3)
        addProduct(uploadIdList, variantInputModel)
    }

    override fun onUploadProductImagesFailed(errorMessage: String) {
        ProductAddUploadTracking.uploadImageFailed(
            userSession.shopId,
            AddEditProductUploadErrorHandler.getUploadImageErrorName(errorMessage)
        )
    }

    override fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager {
        return object : AddEditProductNotificationManager(urlImageCount, applicationContext) {
            override fun getSuccessIntent(): PendingIntent {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
                } else {
                    PendingIntent.getActivity(context, 0, intent, 0)
                }
            }

            override fun getFailedIntent(errorMessage: String): PendingIntent {
                val draftId = productDraftId.toString()
                val intent = AddEditProductPreviewActivity.createInstance(
                    context,
                    draftId,
                    isFromSuccessNotif = false,
                    isFromNotifEditMode = false
                )
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
                } else {
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
            }
        }
    }

    private fun addProduct(uploadIdList: ArrayList<String>, variantInputModel: VariantInputModel) {
        val shopId = userSession.shopId
        val param = addProductInputMapper.mapInputToParam(
            shopId,
            uploadIdList,
            productInputModel.detailInputModel,
            productInputModel.descriptionInputModel,
            productInputModel.shipmentInputModel,
            variantInputModel
        )
        launchCatchError(block = {
            val result = withContext(Dispatchers.IO) {
                productAddUseCase.params = ProductAddUseCase.createRequestParams(param)
                productAddUseCase.executeOnBackground()
            }
            // (4)
            clearProductDraft()
            setUploadProductDataSuccess(result.productAddEditV3Data.productId)
            addFlagOnUploadProductSuccess()
            ProductAddUploadTracking.uploadProductFinish(shopId, true)
        }, onError = { throwable ->
                val errorMessage = getErrorMessage(throwable)
                setUploadProductDataError(errorMessage)

                logError(productAddUseCase.params, throwable)
                if (AddEditProductUploadErrorHandler.isServerTimeout(throwable)) {
                    ProductAddUploadTracking.uploadGqlTimeout(
                        ProductAddUseCase.QUERY_NAME,
                        shopId,
                        AddEditProductUploadErrorHandler.getErrorName(throwable)
                    )
                } else {
                    ProductAddUploadTracking.uploadProductFinish(
                        shopId,
                        false,
                        AddEditProductUploadErrorHandler.isValidationError(throwable),
                        AddEditProductUploadErrorHandler.getErrorName(throwable)
                    )
                }
            })
    }

    private suspend fun clearProductDraft() {
        if (productDraftId > 0) {
            deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(productDraftId)
            deleteProductDraftUseCase.executeOnBackground()
        }
    }

    private suspend fun addFlagOnUploadProductSuccess() {
        withContext(Dispatchers.IO) {
            sellerAppReviewHelper.saveAddProductFrag()
        }
    }

    private fun downloadFile(url: String, filename: String) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val path = "$downloadsDir/$filename"
        URL(url).openStream().use { input ->
            FileOutputStream(File(path), false).use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun reDownloadProductImages(productDetail: DetailInputModel) {
        val productImageData = productDetail.pictureList
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        productImageData.forEach { data ->
            if (data.urlOriginal.isNotBlank() && data.fileName.isNotBlank()) {
                // logging purpose
                filename = data.fileName

                val path = downloadsDir + "/" + data.fileName
                downloadFile(url = data.urlOriginal, filename = data.fileName)
                data.picID = String.EMPTY
                data.filePath = path
            }
        }
        productDetail.imageUrlOrPathList = productImageData.map { it.filePath }
    }

    private fun reDownloadProductVariantImages(productVariant: List<ProductVariantInputModel>) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        productVariant.forEach { data ->
            data.pictures.forEach { picture ->
                if (picture.urlOriginal.isNotBlank() && picture.fileName.isNotBlank()) {
                    // logging purpose
                    filename = picture.fileName

                    val path = downloadsDir + "/" + picture.fileName
                    downloadFile(url = picture.urlOriginal, filename = picture.fileName)
                    picture.picID = String.EMPTY
                    picture.urlOriginal = path
                }
            }
        }
    }

    private fun reDownloadVariantSizeChart(variantSizeChart: PictureVariantInputModel) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val path = downloadsDir + "/" + variantSizeChart.fileName
        if (variantSizeChart.urlOriginal.isNotBlank() && variantSizeChart.fileName.isNotBlank()) {
            // logging purpose
            filename = variantSizeChart.fileName

            downloadFile(url = variantSizeChart.urlOriginal, filename = variantSizeChart.fileName)
            variantSizeChart.picID = String.EMPTY
            variantSizeChart.urlOriginal = path
        }
    }
}
