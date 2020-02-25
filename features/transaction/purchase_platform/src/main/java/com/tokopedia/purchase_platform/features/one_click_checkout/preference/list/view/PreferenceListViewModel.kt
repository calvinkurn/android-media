package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import javax.inject.Inject

class PreferenceListViewModel @Inject constructor(val useCase: GetPreferenceListUseCase) : ViewModel() {

    private val _preferenceList: MutableLiveData<List<Preference>> = MutableLiveData(listOf(Preference(), Preference(), Preference(), Preference(), Preference()))
    val preferenceList: LiveData<List<Preference>>
        get() = _preferenceList

    fun changeDefaultPreference(preference: Preference) {

    }

    fun getAllPreference() {

    }
}