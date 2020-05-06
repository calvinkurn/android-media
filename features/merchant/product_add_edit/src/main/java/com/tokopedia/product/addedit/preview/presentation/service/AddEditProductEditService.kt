package com.tokopedia.product.addedit.preview.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantOptionParent
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by faisalramd on 2020-04-05.
 */

/*
Step to submit edit product:
(1). Save product data to draft and get productDraftId
(2). Upload product images and get uploadIdList
(3). Hit gql for edit product
(4). clear product from draft if edit data success
 */

class AddEditProductEditService : AddEditProductBaseService() {
    private var productDraftId = 0L
    private var productInputModel: ProductInputModel = ProductInputModel()
    private var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    private var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    private var detailInputModel: DetailInputModel = DetailInputModel()
    private var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()

    companion object {
        fun startService(context: Context, cacheManagerId: String?) {
            val work = Intent(context, AddEditProductBaseService::class.java).apply {
                putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            }
            enqueueWork(context, AddEditProductEditService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val cacheManagerId = intent.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID) ?: ""
        SaveInstanceCacheManager(this, cacheManagerId).run {
            productInputModel =  get(AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()
        }
        productInputModel.let {
            shipmentInputModel = it.shipmentInputModel
            descriptionInputModel = it.descriptionInputModel
            detailInputModel = it.detailInputModel
            variantInputModel = it.variantInputModel
        }
        // (1)
        saveProductToDraft()
    }

    private fun saveProductToDraft() {
        launch {
            productDraftId = withContext(Dispatchers.IO){
                saveProductDraftUseCase.params = SaveProductDraftUseCase
                        .createRequestParams(mapProductInputModelDetailToDraft(productInputModel),
                                productInputModel.draftId, false)
                saveProductDraftUseCase.executeOnBackground()
            }
            // (2)
            uploadProductImages(
                    filterPathOnly(detailInputModel.imageUrlOrPathList),
                    getVariantFilePath(variantInputModel.variantOptionParent),
                    variantInputModel.productSizeChart?.filePath ?: "")
        }
    }

    private fun filterPathOnly(imageUrlOrPathList: List<String>): List<String> =
            imageUrlOrPathList.filterNot {
                it.startsWith(HTTP_PREFIX)
            }

    private fun getVariantFilePath(variantOptionParent: ArrayList<ProductVariantOptionParent>): List<String> {
        val imageList: ArrayList<String> = ArrayList()
        val optionParent = variantOptionParent.firstOrNull()
        optionParent?.apply {
            productVariantOptionChild?.forEach { optionChild ->
                val picture = optionChild.productPictureViewModelList?.firstOrNull()
                picture?.apply {
                    imageList.add(filePath)
                }
            }
        }
        return imageList
    }

    override fun onUploadProductImagesDone(
            uploadIdList: ArrayList<String>,
            variantOptionUploadId: List<String>,
            sizeChartId: String
    ) {
        // (3)
        editProduct(uploadIdList, variantOptionUploadId, sizeChartId)
    }

    override fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return object : AddEditProductNotificationManager(urlImageCount, manager,
                this@AddEditProductEditService) {
            override fun getSuccessIntent(): PendingIntent {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }

            override fun getFailedIntent(errorMessage: String): PendingIntent {
                val draftId = productDraftId.toString()
                val intent = AddEditProductPreviewActivity.createInstance(context, draftId,
                        isFromSuccessNotif = false, isFromNotifEditMode = true)
                return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    private fun editProduct(
            uploadIdList: ArrayList<String>,
            variantOptionUploadId: List<String>,
            sizeChartId: String
    ) {
        val shopId = userSession.shopId
        val param = editProductInputMapper.mapInputToParam(
                shopId,
                productInputModel.productId.toString(),
                uploadIdList,
                variantOptionUploadId,
                sizeChartId,
                detailInputModel,
                descriptionInputModel,
                shipmentInputModel,
                variantInputModel)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                productEditUseCase.params = ProductEditUseCase.createRequestParams(param)
                productEditUseCase.executeOnBackground()
            }
            // (4)
            clearProductDraft()
            delay(NOTIFICATION_CHANGE_DELAY)
            setUploadProductDataSuccess()
        }, onError = { throwable ->
            delay(NOTIFICATION_CHANGE_DELAY)
            setUploadProductDataError(getErrorMessage(throwable))

            logError(productEditUseCase.params, throwable)
        })
    }

    private suspend fun clearProductDraft() {
        if(productDraftId > 0) {
            deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(productDraftId)
            deleteProductDraftUseCase.executeOnBackground()
        }
    }
}