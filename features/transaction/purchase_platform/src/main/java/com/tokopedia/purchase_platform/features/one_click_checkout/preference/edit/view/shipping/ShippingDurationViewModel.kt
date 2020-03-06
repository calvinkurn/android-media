package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceEditUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetShippingDurationUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.ShippingDurationModelMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import javax.inject.Inject

class ShippingDurationViewModel @Inject constructor(val useCase: GetShippingDurationUseCase, val mapper: ShippingDurationModelMapper): ViewModel(){

/*
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
*/

    private val _shippingDuration_ = MutableLiveData<ShippingListModel>()
    private val _shippingDuration = MutableLiveData<OccState<ShippingListModel>>()
    val shippingDuration: LiveData<OccState<ShippingListModel>>
        get() = _shippingDuration

    fun getShippingDuration(){
        _shippingDuration.value = OccState.Loading
        useCase.execute(onSuccess = {
            _shippingDuration.value = OccState.Success(mapTomodel(it))
        }, onError = {
            _shippingDuration.value = OccState.Fail(false, it, "")
        })

    }

    private fun mapTomodel(respones: ShippingNoPriceResponse): ShippingListModel{
        return mapper.convertToDomainModel(respones)
    }

    fun consumeGetShippingDurationFail(){
        val value = _shippingDuration.value
        if(value is OccState.Fail){
            _shippingDuration.value = value.copy(isConsumed = true)
        }
    }
}


