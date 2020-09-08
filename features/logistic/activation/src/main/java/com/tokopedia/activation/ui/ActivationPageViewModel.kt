package com.tokopedia.activation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.activation.domain.GetShopFeatureUseCase
import com.tokopedia.activation.model.ActivationPageState
import com.tokopedia.activation.model.ShopFeatureModel
import javax.inject.Inject

class ActivationPageViewModel @Inject constructor(
        private val getShopFeatureUseCase: GetShopFeatureUseCase) : ViewModel() {

    private val _shopFeature = MutableLiveData<ActivationPageState<ShopFeatureModel>>()
    val shopFeature: LiveData<ActivationPageState<ShopFeatureModel>>
        get() = _shopFeature

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
}