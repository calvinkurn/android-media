package com.tokopedia.product.addedit.preview.presentation.service

import android.content.Context
import android.content.Intent
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.Companion.HTTP_PREFIX
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddEditProductDuplicateService : AddEditProductAddService() {

    companion object {
        private const val JOB_ID = 13131314

        fun startService(context: Context,
                         detailInputModel: DetailInputModel,
                         descriptionInputModel: DescriptionInputModel,
                         shipmentInputModel: ShipmentInputModel,
                         variantInputModel: ProductVariantInputModel) {
            val work = Intent(context, AddEditProductBaseService::class.java).apply {
                putExtra(AddEditProductUploadConstant.EXTRA_DETAIL_INPUT, detailInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_SHIPMENT_INPUT, shipmentInputModel)
                putExtra(AddEditProductUploadConstant.EXTRA_VARIANT_INPUT, variantInputModel)
            }
            enqueueWork(context, AddEditProductDuplicateService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        shipmentInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_SHIPMENT_INPUT)
        descriptionInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_DESCRIPTION_INPUT)
        detailInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_DETAIL_INPUT)
        variantInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_VARIANT_INPUT)
        uploadProductImages(filterPathOnly(detailInputModel.imageUrlOrPathList),
                variantInputModel.productSizeChart?.filePath ?: "")
    }

    private fun filterPathOnly(imageUrlOrPathList: List<String>): List<String> =
            imageUrlOrPathList.filterNot {
                it.startsWith(HTTP_PREFIX)
            }

    override fun onUploadProductImagesDone(uploadIdList: ArrayList<String>, sizeChartId: String) {
        duplicateProduct(uploadIdList, sizeChartId)
    }

    private fun duplicateProduct(uploadIdList: ArrayList<String>, sizeChartId: String) {
        val shopId = userSession.shopId
        val param = duplicateProductInputMapper.mapInputToParam(shopId, uploadIdList,
                sizeChartId, detailInputModel, descriptionInputModel, shipmentInputModel,
                variantInputModel)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                productAddUseCase.params = ProductAddUseCase.createRequestParams(param)
                setUploadProductDataSuccess()
                return@withContext productAddUseCase.executeOnBackground()
            }
        }, onError = {
            it.message?.let { errorMessage -> setUploadProductDataError(errorMessage) }
        })
    }
}