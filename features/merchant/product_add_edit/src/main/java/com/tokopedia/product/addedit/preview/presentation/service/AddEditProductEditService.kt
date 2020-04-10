package com.tokopedia.product.addedit.preview.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.Companion.HTTP_PREFIX
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.mapper.mapProductInputModelDetailToDraft
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tracking.ProductEditStepperTracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by faisalramd on 2020-04-05.
 */

class AddEditProductEditService : AddEditProductBaseService() {
    private var productId = ""
    private var productInputModel: ProductInputModel = ProductInputModel()
    private var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    private var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    private var detailInputModel: DetailInputModel = DetailInputModel()
    private var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()

    companion object {
        private const val JOB_ID = 13131314
        private const val EXTRA_PRODUCT_ID_INPUT_EDIT = "EXTRA_PRODUCT_ID_INPUT_EDIT"
        private const val EXTRA_PRODUCT_INPUT_MODEL = "EXTRA_PRODUCT_INPUT_MODEL"

        fun startService(context: Context,
                         productId: String,
                         productInputModel: ProductInputModel) {
            val work = Intent(context, AddEditProductBaseService::class.java).apply {
                putExtra(EXTRA_PRODUCT_ID_INPUT_EDIT, productId)
                putExtra(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
            }
            enqueueWork(context, AddEditProductEditService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        productId = intent.getStringExtra(EXTRA_PRODUCT_ID_INPUT_EDIT)
        productInputModel = intent.getParcelableExtra(EXTRA_PRODUCT_INPUT_MODEL)

        productInputModel.let {
            shipmentInputModel = it.shipmentInputModel
            descriptionInputModel = it.descriptionInputModel
            detailInputModel = it.detailInputModel
            variantInputModel = it.variantInputModel
            uploadProductImages(filterPathOnly(detailInputModel.imageUrlOrPathList),
                    variantInputModel.productSizeChart?.filePath ?: "")
        }
    }

    private fun filterPathOnly(imageUrlOrPathList: List<String>): List<String> =
            imageUrlOrPathList.filterNot {
                it.startsWith(HTTP_PREFIX)
            }


    override fun onUploadProductImagesDone(uploadIdList: ArrayList<String>, sizeChartId: String) {
        editProduct(uploadIdList, sizeChartId)
    }

    override fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return object : AddEditProductNotificationManager(urlImageCount, manager,
                this@AddEditProductEditService) {
            override fun getSuccessIntent(): PendingIntent {
                ProductEditStepperTracking.trackFinishService(userSession.shopId, true)
                val intent = AddEditProductPreviewActivity
                        .createInstance(context, isFromSuccessNotif = true, isFromNotifEditMode = true)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }

            override fun getFailedIntent(errorMessage: String): PendingIntent {
                ProductEditStepperTracking.trackFinishService(userSession.shopId, false)
                val intent = AddEditProductPreviewActivity
                        .createInstance(context, isFromSuccessNotif = false, isFromNotifEditMode = true)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }
        }
    }

    private fun editProduct(uploadIdList: ArrayList<String>, sizeChartId: String) {
        val shopId = userSession.shopId
        val param = editProductInputMapper.mapInputToParam(shopId, productId,
                uploadIdList, sizeChartId, detailInputModel, descriptionInputModel,
                shipmentInputModel, variantInputModel)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                productEditUseCase.params = ProductEditUseCase.createRequestParams(param)
                setUploadProductDataSuccess()
                if(productInputModel.draftId > 0) {
                    deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(productInputModel.draftId)
                    deleteProductDraftUseCase.executeOnBackground()
                }
                return@withContext productEditUseCase.executeOnBackground()
            }
        }, onError = {
            it.message?.let { errorMessage ->
                setUploadProductDataSuccess(errorMessage)
                if(productInputModel.draftId > 0) {
                    saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(mapProductInputModelDetailToDraft(productInputModel), productInputModel.draftId, false)
                    withContext(Dispatchers.IO){ saveProductDraftUseCase.executeOnBackground() }
                }
            }
        })
    }
}