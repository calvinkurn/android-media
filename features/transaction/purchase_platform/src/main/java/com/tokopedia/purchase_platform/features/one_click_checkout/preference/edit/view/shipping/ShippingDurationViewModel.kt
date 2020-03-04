package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceEditUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetShippingDurationUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.ShippingDurationModelMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import javax.inject.Inject

class ShippingDurationViewModel @Inject constructor(val useCase: GetShippingDurationUseCase, val mapper: ShippingDurationModelMapper): ViewModel(){

    private val _shippingDuration = MutableLiveData<ShippingListModel>()
    val shippingDuration: LiveData<ShippingListModel>
    get() = _shippingDuration

    fun getShippingDuration(){
        useCase.
                execute(onSuccess = {
            _shippingDuration.value = mapTomodel(it)
        }, onError = {

        })

    }

    private fun mapTomodel(respones: ShippingNoPriceResponse): ShippingListModel{
        return mapper.convertToDomainModel(respones)
    }
}


