package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceEditUseCase
import javax.inject.Inject

class ShippingDurationViewModel @Inject constructor(val useCase: GetPreferenceEditUseCase): ViewModel(){
    private val _shippingDuration: MutableLiveData<List<Preference>> = MutableLiveData(listOf(Preference(), Preference(), Preference(), Preference(), Preference(), Preference()))
    val shippingDuration: LiveData<List<Preference>>
    get() = _shippingDuration

}