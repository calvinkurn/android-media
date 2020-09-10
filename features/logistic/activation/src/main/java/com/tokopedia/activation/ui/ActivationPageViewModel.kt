package com.tokopedia.activation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.activation.domain.GetShopFeatureUseCase
import com.tokopedia.activation.domain.UpdateShopFeatureUseCase
import com.tokopedia.activation.model.ActivationPageState
import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.model.UpdateFeatureModel
import javax.inject.Inject

class ActivationPageViewModel @Inject constructor(
        private val getShopFeatureUseCase: GetShopFeatureUseCase,
        private val updateShopFeatureUseCase: UpdateShopFeatureUseCase) : ViewModel() {

    private val _shopFeature = MutableLiveData<ActivationPageState<ShopFeatureModel>>()
    val shopFeature: LiveData<ActivationPageState<ShopFeatureModel>>
        get() = _shopFeature

    private val _updateShopFeature = MutableLiveData<ActivationPageState<UpdateFeatureModel>>()
    val updateShopFeature: LiveData<ActivationPageState<UpdateFeatureModel>>
        get() = _updateShopFeature

    fun getShopFeature(shopId: String) {
        getShopFeatureUseCase.execute(
                { shopFeatureModel: ShopFeatureModel ->
                    _shopFeature.value = ActivationPageState.Success(shopFeatureModel)

                },
                {
                    throwable: Throwable ->
                    _shopFeature.value = ActivationPageState.Fail(throwable, "")
                }
        )
    }

    fun updateShopFeature(value: Boolean) {
        updateShopFeatureUseCase.execute(
                { updateFeatureModel: UpdateFeatureModel ->
                    _updateShopFeature.value = ActivationPageState.Success(updateFeatureModel)
                },
                {
                    throwable: Throwable ->
                    _updateShopFeature.value = ActivationPageState.Fail(throwable, "")
                }
        )
    }
}