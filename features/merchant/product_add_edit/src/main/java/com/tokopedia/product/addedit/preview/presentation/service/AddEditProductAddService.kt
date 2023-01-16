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
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.common.util.AddEditProductUploadErrorHandler
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TITLE_ERROR_SAVING_DRAFT
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductAddUploadTracking
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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
            // re download product images + update new file path
            downloadProductImages(productInputModel)
//            // re download product variant images + update new file path
//            val productVariantData = productInputModel.variantInputModel.products
//            productInputModel.variantInputModel.products = reDownloadProductVariantImages(productVariantData)
//            // re download product variant size chart + update new file path
//            val variantSizeChart = productInputModel.variantInputModel.sizecharts
//            reDownloadVariantSizeChart(variantSizeChart)
        } else {
            // (1)
            saveProductToDraftAsync()
        }
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

//    private fun downloadFile(url: String, path: String) {
//        launchCatchError(block = {
//            URL(url).openStream().use { input ->
//                FileOutputStream(File(path), false).use { output ->
//                    input.copyTo(output)
//                }
//            }
//        }, onError = { throwable ->
//                AddEditProductErrorHandler.logMessage(path)
//                AddEditProductErrorHandler.logExceptionToCrashlytics(throwable)
//                // TODO notify user about the error
//            })
//    }

//    private fun downloadFile(url: String, outputFileName: String) {
//        launchCatchError(block = {
//            val downloadsDir =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
//            val file = File(downloadsDir, outputFileName)
//            val downloadUrl = URL(URLEncoder.encode(url, "UTF-8"))
//            val ucon: URLConnection = downloadUrl.openConnection()
//            ucon.connect()
//            val inputStream: InputStream = ucon.getInputStream()
//            val fos = FileOutputStream(file)
//            val data = ByteArray(1024)
//            var current = 0
//            while (inputStream.read(data).also { current = it } != -1) {
//                fos.write(data, 0, current)
//            }
//            inputStream.close()
//            fos.flush()
//            fos.close()
//        }, onError = { throwable ->
//                AddEditProductErrorHandler.logMessage(outputFileName)
//                AddEditProductErrorHandler.logExceptionToCrashlytics(throwable)
//                // TODO notify user about the error
//            })
//    }

    private fun downloadProductImages(productInputModel: ProductInputModel) {
        val productImageData = productInputModel.detailInputModel.pictureList
        if (productImageData.isNullOrEmpty()) return
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val mutableData = productImageData.toMutableList()
        var index = 0

        val downloadCompleteListener = object : DownloadHelper.DownloadHelperListener {
            override fun onDownloadComplete() {
                // update file path
                var data = mutableData.getOrNull(index)
                data?.run {
                    var path = downloadsDir + "/" + this.fileName
                    this.filePath = path
                }

                if (mutableData.lastIndex == index) {
                    productInputModel.detailInputModel.pictureList = mutableData.toList()

                    // download variant images
                    downloadProductVariantImages(productInputModel)
                } else {
                    // download next image
                    try {
                        index += Int.ONE
                        var nextImage = mutableData.getOrNull(index)
                        nextImage?.let {
                            val helper = DownloadHelper(
                                context = this@AddEditProductAddService,
                                uri = it.urlOriginal,
                                filename = it.fileName,
                                listener = this
                            )
                            helper.downloadFile { true }
                        }
                    } catch (se: SecurityException) {
                        AddEditProductErrorHandler.logMessage(mutableData.first().fileName)
                        AddEditProductErrorHandler.logExceptionToCrashlytics(se)
                    } catch (iae: IllegalArgumentException) {
                        AddEditProductErrorHandler.logMessage(mutableData.first().fileName)
                        AddEditProductErrorHandler.logExceptionToCrashlytics(iae)
                    } catch (ex: Exception) {
                        AddEditProductErrorHandler.logMessage(mutableData.first().fileName)
                        AddEditProductErrorHandler.logExceptionToCrashlytics(ex)
                    }
                }
            }
        }

        // Find the start index
        for (data in productImageData) {
            var loopIndex = productImageData.indexOf(data)

            // check if file is already downloaded into downloads directory
            val imageFile = File(downloadsDir + data.fileName)

            // all the images already exist in downloads dir
            if (loopIndex == productImageData.lastIndex && imageFile.isFile) {
                downloadProductVariantImages(productInputModel)
                break
            }
            if (!imageFile.isFile) {
                index = productImageData.indexOf(data)
                break
            }
        }

        try {
            // initial download with the start index
            mutableData.getOrNull(index)?.let {
                val helper = DownloadHelper(
                    context = this@AddEditProductAddService,
                    uri = it.urlOriginal,
                    filename = it.fileName,
                    listener = downloadCompleteListener
                )
                helper.downloadFile { true }
            }
        } catch (se: SecurityException) {
            val fileName = mutableData.getOrNull(index)?.fileName.orEmpty()
            AddEditProductErrorHandler.logMessage(fileName)
            AddEditProductErrorHandler.logExceptionToCrashlytics(se)
        } catch (iae: IllegalArgumentException) {
            val fileName = mutableData.getOrNull(index)?.fileName.orEmpty()
            AddEditProductErrorHandler.logMessage(fileName)
            AddEditProductErrorHandler.logExceptionToCrashlytics(iae)
        } catch (ex: Exception) {
            val fileName = mutableData.getOrNull(index)?.fileName.orEmpty()
            AddEditProductErrorHandler.logMessage(fileName)
            AddEditProductErrorHandler.logExceptionToCrashlytics(ex)
        }
    }

    private fun downloadProductVariantImages(productInputModel: ProductInputModel) {
        val productVariantData = productInputModel.variantInputModel.products
        if (productVariantData.isNullOrEmpty()) downloadVariantSizeChart(productInputModel)
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val mutableData = productVariantData.toMutableList()
        var index = 0

        downloadProductVariantImages(productInputModel)

//        // Find the start index
//        for (data in productVariantData) {
//
//            var loopIndex = productImageData.indexOf(data)
//
//            // check if file is already downloaded into downloads directory
//            val imageFile = File(downloadsDir + data.fileName)
//
//            // all the images already exist in downloads dir
//            if (loopIndex == productImageData.lastIndex && imageFile.isFile) {
//                downloadProductVariantImages(productInputModel)
//                break
//            }
//            if (!imageFile.isFile) {
//                index = productImageData.indexOf(data)
//                break
//            }
//        }
    }

    private fun downloadVariantSizeChart(productInputModel: ProductInputModel) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val variantSizeChart = productInputModel.variantInputModel.sizecharts

        val downloadCompleteListener = object : DownloadHelper.DownloadHelperListener {
            override fun onDownloadComplete() {
                saveProductToDraftAsync()
            }
        }

        if (variantSizeChart.urlOriginal.isNotBlank()) {
            // check if file is already downloaded into downloads directory
            val imageFile = File(downloadsDir + variantSizeChart.fileName)
            if (!imageFile.isFile) {
                try {
                    val helper = DownloadHelper(
                        context = this@AddEditProductAddService,
                        uri = variantSizeChart.urlOriginal,
                        filename = variantSizeChart.fileName,
                        listener = downloadCompleteListener
                    )
                    helper.downloadFile { true }
                } catch (se: SecurityException) {
                    val fileName = variantSizeChart.fileName
                    AddEditProductErrorHandler.logMessage(fileName)
                    AddEditProductErrorHandler.logExceptionToCrashlytics(se)
                } catch (iae: IllegalArgumentException) {
                    val fileName = variantSizeChart.fileName
                    AddEditProductErrorHandler.logMessage(fileName)
                    AddEditProductErrorHandler.logExceptionToCrashlytics(iae)
                } catch (ex: Exception) {
                    val fileName = variantSizeChart.fileName
                    AddEditProductErrorHandler.logMessage(fileName)
                    AddEditProductErrorHandler.logExceptionToCrashlytics(ex)
                }
            }
        }
    }

//    private fun reDownloadProductImages(productImageData: List<PictureInputModel>): List<PictureInputModel> {
//        val mutableData = productImageData.toMutableList()
//        val downloadsDir =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
//        mutableData.forEach { data ->
//            var path = downloadsDir + "/" + data.fileName
//            downloadFile(url = data.urlOriginal, outputFileName = data.fileName)
//            data.filePath = path
//        }
//        return mutableData.toList()
//    }

//    private fun reDownloadProductVariantImages(productVariantData: List<ProductVariantInputModel>): List<ProductVariantInputModel> {
//        val mutableData = productVariantData.toMutableList()
//        val downloadsDir =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
//        mutableData.forEach { data ->
//            data.pictures.forEach { picture ->
//                var path = downloadsDir + "/" + picture.fileName
//                downloadFile(url = picture.urlOriginal, outputFileName = picture.fileName)
//                picture.filePath = path
//            }
//        }
//        return mutableData.toList()
//    }

//    private fun reDownloadVariantSizeChart(variantSizeChart: PictureVariantInputModel) {
//        val downloadsDir =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
//        var path = downloadsDir + "/" + variantSizeChart.fileName
//        downloadFile(
//            url = variantSizeChart.urlOriginal,
//            outputFileName = variantSizeChart.fileName
//        )
//        variantSizeChart.filePath = path
//    }
}
