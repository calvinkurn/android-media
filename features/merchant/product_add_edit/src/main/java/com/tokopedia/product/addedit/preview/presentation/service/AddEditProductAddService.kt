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
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantOptionParent
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tracking.ProductAddShippingTracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    protected var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    protected var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    protected var detailInputModel: DetailInputModel = DetailInputModel()
    protected var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()

    companion object {
        fun startService(context: Context, cacheManagerId: String?) {
            val work = Intent(context, AddEditProductBaseService::class.java).apply {
                putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            }
            enqueueWork(context, AddEditProductAddService::class.java, JOB_ID, work)
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
            saveProductDraftUseCase.params = SaveProductDraftUseCase
                    .createRequestParams(mapProductInputModelDetailToDraft(productInputModel),
                            productInputModel.draftId, false)
            productDraftId = withContext(Dispatchers.IO){
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
                it.startsWith(AddEditProductConstants.HTTP_PREFIX)
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

    override fun onUploadProductImagesDone(uploadIdList: ArrayList<String>, variantOptionUploadId: List<String>, sizeChartId: String) {
        // (3)
        addProduct(uploadIdList, variantOptionUploadId, sizeChartId)
    }

    override fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return object : AddEditProductNotificationManager(urlImageCount, manager,
                this@AddEditProductAddService) {
            override fun getSuccessIntent(): PendingIntent {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }

            override fun getFailedIntent(errorMessage: String): PendingIntent {
                val draftId = productDraftId.toString()
                val intent = AddEditProductPreviewActivity.createInstance(context, draftId,
                        isFromSuccessNotif = false, isFromNotifEditMode = false)
                return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    private fun addProduct(uploadIdList: ArrayList<String>, variantOptionUploadId: List<String>, sizeChartId: String) {
        val shopId = userSession.shopId
        val param = addProductInputMapper.mapInputToParam(
                shopId,
                uploadIdList,
                variantOptionUploadId,
                sizeChartId,
                detailInputModel,
                descriptionInputModel,
                shipmentInputModel,
                variantInputModel)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                productAddUseCase.params = ProductAddUseCase.createRequestParams(param)
                productAddUseCase.executeOnBackground()
            }
            // (4)
            clearProductDraft()
            delay(NOTIFICATION_CHANGE_DELAY)
            setUploadProductDataSuccess()
            ProductAddShippingTracking.clickFinish(shopId, true)
        }, onError = {
            it.message?.let { errorMessage ->
                delay(NOTIFICATION_CHANGE_DELAY)
                setUploadProductDataError(errorMessage)
                ProductAddShippingTracking.clickFinish(shopId, false, errorMessage)
            }
        })
    }

    private suspend fun clearProductDraft() {
        if(productDraftId > 0) {
            deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(productDraftId)
            deleteProductDraftUseCase.executeOnBackground()
        }
    }
}