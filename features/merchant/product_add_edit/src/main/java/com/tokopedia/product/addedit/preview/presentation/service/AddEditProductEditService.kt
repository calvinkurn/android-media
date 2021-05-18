package com.tokopedia.product.addedit.preview.presentation.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.common.util.AddEditProductUploadErrorHandler
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.data.model.responses.ProductAddEditV3Response
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TITLE_ERROR_SAVING_DRAFT
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductEditUploadTracking
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.shop.common.data.model.ProductStock
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ProductStockWarehouse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
        saveProductToDraftAsync()
    }

    private fun saveProductToDraftAsync() {
        launchCatchError(block = {
            asyncCatchError( coroutineContext,
                    block = {
                        saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(
                                mapProductInputModelDetailToDraft(productInputModel),
                                productInputModel.draftId, false)
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

    override fun onUploadProductImagesSuccess(uploadIdList: ArrayList<String>, variantInputModel: VariantInputModel) {
        // (3)
        editProduct(uploadIdList, variantInputModel)
    }

    override fun onUploadProductImagesFailed(errorMessage: String) {
        ProductEditUploadTracking.uploadImageFailed(
                userSession.shopId,
                AddEditProductUploadErrorHandler.getUploadImageErrorName(errorMessage))
    }

    override fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager {
        return object : AddEditProductNotificationManager(urlImageCount, applicationContext) {
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
        // This value determine whether we should update stock in separate use case.
        // If true we do not need use separate use case
        val shouldEditStockDirectly =
                !(userSession.isMultiLocationShop && (userSession.isShopOwner || userSession.isShopAdmin))
        val param = editProductInputMapper.mapInputToParam(
                shopId,
                productInputModel.productId.toString(),
                uploadIdList,
                productInputModel.detailInputModel,
                productInputModel.descriptionInputModel,
                productInputModel.shipmentInputModel,
                variantInputModel,
                shouldEditStockDirectly)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                productEditUseCase.params = ProductEditUseCase.createRequestParams(param)
                if (!shouldEditStockDirectly) {
                    productInputModel.run {
                        updateHqStockThroughIms(shopId, productId.toString(), detailInputModel.stock, variantInputModel)
                    }
                }
                productEditUseCase.executeOnBackground()
            }
            // (4)
            clearProductDraft()
            setUploadProductDataSuccess()
            ProductEditUploadTracking.uploadProductFinish(shopId, true)
        }, onError = { throwable ->
            setUploadProductDataError(getErrorMessage(throwable))

            logError(productEditUseCase.params, throwable)
            if (AddEditProductUploadErrorHandler.isServerTimeout(throwable)) {
                ProductEditUploadTracking.uploadGqlTimeout(
                        ProductEditUseCase.QUERY_NAME,
                        shopId,
                        AddEditProductUploadErrorHandler.getErrorName(throwable))
            } else {
                ProductEditUploadTracking.uploadProductFinish(
                        shopId,
                        false,
                        AddEditProductUploadErrorHandler.isValidationError(throwable),
                        AddEditProductUploadErrorHandler.getErrorName(throwable))
            }
        })
    }

    private suspend fun getHeadquartersLocationId(shopId: String): String? {
        getAdminInfoShopLocationUseCase.execute(shopId.toIntOrZero()).let { locationList ->
            return locationList.find { it.isMainLocation() }?.locationId
        }
    }

    private suspend fun updateHqStockThroughIms(shopId: String,
                                                productId: String,
                                                stock: Int,
                                                variantInputModel: VariantInputModel) {
        getHeadquartersLocationId(shopId)?.let { warehouseId ->
            variantInputModel.products.map { variantProduct ->
                ProductStock(variantProduct.id, variantProduct.stock.toString()) }.let { variants ->
                    mutableListOf(ProductStock(productId, stock.toString())).apply {
                        addAll(variants)
                    }
            }.let { productsParam ->
                UpdateProductStockWarehouseUseCase
                        .createRequestParams(shopId, warehouseId, productsParam).let { requestParam ->
                            updateProductStockWarehouseUseCase.execute(requestParam)
                        }
            }
        }
    }

    private suspend fun clearProductDraft() {
        if(productDraftId > 0) {
            deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(productDraftId)
            deleteProductDraftUseCase.executeOnBackground()
        }
    }
}