package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceEditUseCase
import javax.inject.Inject

class AddressListViewModel @Inject constructor(val useCase: GetPreferenceEditUseCase) : ViewModel() {
    private val _addresslist: MutableLiveData<List<Preference>> = MutableLiveData(listOf(Preference(), Preference(), Preference()))
    val addressList: LiveData<List<Preference>>
    get() = _addresslist
}