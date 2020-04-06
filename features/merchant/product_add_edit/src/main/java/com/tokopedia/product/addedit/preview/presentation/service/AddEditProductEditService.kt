package com.tokopedia.product.addedit.preview.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tracking.ProductEditStepperTracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

/**
 * Created by faisalramd on 2020-04-05.
 */

class AddEditProductEditService : AddEditProductBaseService() {
    private var productId = ""
    private var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    private var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    private var detailInputModel: DetailInputModel = DetailInputModel()
    private var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()

    companion object {
        private const val JOB_ID = 13131314

        fun startService(context: Context,
                         productId: String,
                         detailInputModel: DetailInputModel,
                         descriptionInputModel: DescriptionInputModel,
                         shipmentInputModel: ShipmentInputModel,
                         variantInputModel: ProductVariantInputModel) {
            val work = Intent(context, AddEditProductBaseService::class.java).apply {
                putExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_ID_INPUT, productId)
                putExtra(AddEditProductUploadConstant.EXTRA_DETAIL_INPUT, detailInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_SHIPMENT_INPUT, shipmentInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_VARIANT_INPUT, variantInputModel)
            }
            enqueueWork(context, AddEditProductEditService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        productId = intent.getStringExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_ID_INPUT)
        shipmentInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_SHIPMENT_INPUT)
        descriptionInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_DESCRIPTION_INPUT)
        detailInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_DETAIL_INPUT)
        variantInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_VARIANT_INPUT)
        uploadProductImages(detailInputModel.imageUrlOrPathList,
                variantInputModel.productSizeChart?.filePath ?: "")
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
                return@withContext productEditUseCase.executeOnBackground()
            }
        }, onError = {
            it.message?.let { errorMessage -> setUploadProductDataSuccess(errorMessage) }
        })
    }
}