package com.tokopedia.product.addedit.preview.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tracking.ProductAddShippingTracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by faisalramd on 2020-04-05.
 */

open class AddEditProductAddService : AddEditProductBaseService() {
    protected var productInputModel: ProductInputModel = ProductInputModel()
    protected var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    protected var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    protected var detailInputModel: DetailInputModel = DetailInputModel()
    protected var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()

    companion object {
        private const val JOB_ID = 13131314

        fun startService(context: Context,
                         detailInputModel: DetailInputModel,
                         descriptionInputModel: DescriptionInputModel,
                         shipmentInputModel: ShipmentInputModel,
                         variantInputModel: ProductVariantInputModel,
                         draftId: Long
        ) {
            val work = Intent(context, AddEditProductBaseService::class.java).apply {
                putExtra(AddEditProductUploadConstant.EXTRA_DETAIL_INPUT, detailInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_SHIPMENT_INPUT, shipmentInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_VARIANT_INPUT, variantInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_DRAFT_ID, draftId)
            }
            enqueueWork(context, AddEditProductAddService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val draftId = intent.getLongExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_DRAFT_ID, 0)
        shipmentInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_SHIPMENT_INPUT)
        descriptionInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_DESCRIPTION_INPUT)
        detailInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_DETAIL_INPUT)
        variantInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_VARIANT_INPUT)
        productInputModel.let {
            it.shipmentInputModel = shipmentInputModel
            it.descriptionInputModel = descriptionInputModel
            it.detailInputModel = detailInputModel
            it.variantInputModel = variantInputModel
            it.draftId = draftId
        }
        uploadProductImages(filterPathOnly(detailInputModel.imageUrlOrPathList),
                variantInputModel.productSizeChart?.filePath ?: "")
    }

    private fun filterPathOnly(imageUrlOrPathList: List<String>): List<String> =
            imageUrlOrPathList.filterNot {
                it.startsWith(AddEditProductConstants.HTTP_PREFIX)
            }

    override fun onUploadProductImagesDone(uploadIdList: ArrayList<String>, sizeChartId: String) {
        addProduct(uploadIdList, sizeChartId)
    }

    override fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return object : AddEditProductNotificationManager(urlImageCount, manager,
                this@AddEditProductAddService) {
            override fun getSuccessIntent(): PendingIntent {
                val intent = AddEditProductPreviewActivity
                        .createInstance(context, isFromSuccessNotif = true, isFromNotifEditMode = false)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }

            override fun getFailedIntent(errorMessage: String): PendingIntent {
                val intent = AddEditProductPreviewActivity
                        .createInstance(context, isFromSuccessNotif = false, isFromNotifEditMode = false)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }
        }
    }

    private fun addProduct(uploadIdList: ArrayList<String>, sizeChartId: String) {
        val shopId = userSession.shopId
        val param = addProductInputMapper.mapInputToParam(shopId, uploadIdList,
                sizeChartId, detailInputModel, descriptionInputModel, shipmentInputModel,
                variantInputModel)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                productAddUseCase.params = ProductAddUseCase.createRequestParams(param)
                setUploadProductDataSuccess()
                if(productInputModel.draftId > 0) {
                    deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(productInputModel.draftId)
                    deleteProductDraftUseCase.executeOnBackground()
                }
                ProductAddShippingTracking.clickFinish(shopId, true)
                return@withContext productAddUseCase.executeOnBackground()
            }
        }, onError = {
            it.message?.let { errorMessage ->
                setUploadProductDataSuccess(errorMessage)
                if(productInputModel.draftId > 0) {
                    saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(mapProductInputModelDetailToDraft(productInputModel), productInputModel.draftId, false)
                    withContext(Dispatchers.IO){ saveProductDraftUseCase.executeOnBackground() }
                }
                ProductAddShippingTracking.clickFinish(shopId, false, errorMessage)
            }
        })
    }
}