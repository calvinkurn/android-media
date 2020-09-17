package com.tokopedia.product.addedit.preview.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductEditShippingTracking
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
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

        // (1)
        saveProductToDraft()
    }

    private fun saveProductToDraft() {
        launch {
            productDraftId = withContext(Dispatchers.IO){
                saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(
                        mapProductInputModelDetailToDraft(productInputModel),
                        productInputModel.draftId, false)
                saveProductDraftUseCase.executeOnBackground()
            }

            val detailInputModel = productInputModel.detailInputModel
            val variantInputModel = productInputModel.variantInputModel
            // (2)
            uploadProductImages(detailInputModel.imageUrlOrPathList, variantInputModel)
        }
    }

    override fun onUploadProductImagesSuccess(uploadIdList: ArrayList<String>, variantInputModel: VariantInputModel) {
        // (3)
        editProduct(uploadIdList, variantInputModel)
    }

    override fun onUploadProductImagesFailed(errorMessage: String) {
        Log.e("edit upload", errorMessage)
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
            variantInputModel: VariantInputModel
    ) {
        val shopId = userSession.shopId
        val param = editProductInputMapper.mapInputToParam(
                shopId,
                productInputModel.productId.toString(),
                uploadIdList,
                productInputModel.detailInputModel,
                productInputModel.descriptionInputModel,
                productInputModel.shipmentInputModel,
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
            ProductEditShippingTracking.clickFinish(shopId, true)
        }, onError = { throwable ->
            delay(NOTIFICATION_CHANGE_DELAY)
            val errorMessage = getErrorMessage(throwable)
            setUploadProductDataError(errorMessage)

            logError(productEditUseCase.params, throwable)
            ProductEditShippingTracking.clickFinish(shopId, false, throwable.message ?: "", errorMessage)
        })
    }

    private suspend fun clearProductDraft() {
        if(productDraftId > 0) {
            deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(productDraftId)
            deleteProductDraftUseCase.executeOnBackground()
        }
    }
}