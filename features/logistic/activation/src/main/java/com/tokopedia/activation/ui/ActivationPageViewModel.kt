package com.tokopedia.activation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.activation.domain.GetShippingEditorUseCase
import com.tokopedia.activation.domain.GetShopFeatureUseCase
import com.tokopedia.activation.domain.UpdateShopFeatureUseCase
import com.tokopedia.activation.model.ActivationPageState
import com.tokopedia.activation.model.ShippingEditorModel
import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.model.UpdateFeatureModel
import javax.inject.Inject

class ActivationPageViewModel @Inject constructor(
        private val getShopFeatureUseCase: GetShopFeatureUseCase,
        private val updateShopFeatureUseCase: UpdateShopFeatureUseCase,
        private val getShippingEditorUseCase: GetShippingEditorUseCase) : ViewModel() {

    private val _shopFeature = MutableLiveData<ActivationPageState<ShopFeatureModel>>()
    val shopFeature: LiveData<ActivationPageState<ShopFeatureModel>>
        get() = _shopFeature

    private val _updateShopFeature = MutableLiveData<ActivationPageState<UpdateFeatureModel>>()
    val updateShopFeature: LiveData<ActivationPageState<UpdateFeatureModel>>
        get() = _updateShopFeature

    private val _activatedShipping = MutableLiveData<ActivationPageState<ShippingEditorModel>>()
    val activatedShipping: LiveData<ActivationPageState<ShippingEditorModel>>
        get() = _activatedShipping

    fun getShopFeature(shopId: String) {
        _shopFeature.value = ActivationPageState.Loading
        getShopFeatureUseCase.execute(
                { shopFeatureModel: ShopFeatureModel ->
                    _shopFeature.value = ActivationPageState.Success(shopFeatureModel)

                },
                {
                    throwable: Throwable ->
                    _shopFeature.value = ActivationPageState.Fail(throwable, "")
                },
                getShopFeatureUseCase.generateRequestParams(shopId)
        )
    }

    fun updateShopFeature(value: Boolean) {
        _updateShopFeature.value = ActivationPageState.Loading
        updateShopFeatureUseCase.execute(
                { updateFeatureModel: UpdateFeatureModel ->
                    _updateShopFeature.value = ActivationPageState.Success(updateFeatureModel)
                },
                {
                    throwable: Throwable ->
                    _updateShopFeature.value = ActivationPageState.Fail(throwable, "")
                },
                updateShopFeatureUseCase.generateRequestParams(value)
        )
    }

    fun validateActivatedShipping(userId: Int) {
        _activatedShipping.value= ActivationPageState.Loading
        getShippingEditorUseCase.execute(
                { shippingEditorModel: ShippingEditorModel ->
                    _activatedShipping.value = ActivationPageState.Success(shippingEditorModel)
                },
                {
                    throwable: Throwable ->
                    _activatedShipping.value = ActivationPageState.Fail(throwable, "")
                },
                getShippingEditorUseCase.generateRequestParams(userId)
        )
    }
}