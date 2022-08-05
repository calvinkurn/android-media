package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticRepository
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.IMSResourceProvider
import com.tokopedia.product.addedit.common.util.getValueOrDefault
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductShipmentViewModel @Inject constructor(
    private val saveProductDraftUseCase: SaveProductDraftUseCase,
    private val customProductLogisticRepository: CustomProductLogisticRepository,
    private val customProductLogisticMapper: CustomProductLogisticMapper,
    private val resourceProvider: IMSResourceProvider,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    var isEditMode: Boolean = false
    var isAddMode: Boolean = false
    var isDraftMode: Boolean = false
    var isFirstMoved: Boolean = false

    private val _productInputModel = MutableLiveData<ProductInputModel>()
    val productInputModel get() = _productInputModel.value
    val shipmentInputModel: LiveData<ShipmentInputModel> = Transformations.map(_productInputModel) {
        it.shipmentInputModel
    }
    val hasVariant: LiveData<Boolean> = Transformations.map(_productInputModel) {
        it.variantInputModel.products.isNotEmpty()
    }

    private val _cplList = MutableLiveData<Result<CustomProductLogisticModel>>()
    val cplList: LiveData<Result<CustomProductLogisticModel>>
        get() = _cplList

    private fun getWeight(weight: String) = weight.replace(".", "").toIntOrZero()

    fun setProductInputModel(productInputModel: ProductInputModel) {
        _productInputModel.value = productInputModel
    }

    fun validateWeightInput(weightInput: String): String {
        if (hasVariant.getValueOrDefault(false)) return ""
        return when {
            weightInput.isEmpty() -> resourceProvider.getEmptyProductWeightErrorMessage()
            getWeight(weightInput) < AddEditProductVariantConstants.MIN_PRODUCT_WEIGHT_LIMIT -> {
                resourceProvider.getMinLimitProductWeightErrorMessage(
                    AddEditProductVariantConstants.MIN_PRODUCT_WEIGHT_LIMIT
                )
            }
            getWeight(weightInput) > AddEditProductVariantConstants.MAX_PRODUCT_WEIGHT_LIMIT -> {
                resourceProvider.getMaxLimitProductWeightErrorMessage(
                    AddEditProductVariantConstants.MAX_PRODUCT_WEIGHT_LIMIT
                )
            }
            else -> ""
        }
    }

    fun saveProductDraft(productInputModel: ProductInputModel) {
        val productId = productInputModel.productId
        val productDraft = AddEditProductMapper.mapProductInputModelDetailToDraft(productInputModel)
        launchCatchError(block = {
            saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(productDraft, productId, false)
            withContext(dispatcher.io) {
                saveProductDraftUseCase.executeOnBackground()
            }
        }, onError = {
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        })
    }

    fun getCPLList(shopId: Long, productId: String, shipmentServicesIds: List<Long>?) {
        viewModelScope.launch {
            try {
                val cplList = customProductLogisticRepository.getCPLList(shopId, productId)
                _cplList.value = Success(customProductLogisticMapper.mapCPLData(cplList.response.data, productId, shipmentServicesIds))
            } catch (e: Throwable) {
                _cplList.value = Fail(e)
            }
        }
    }
}