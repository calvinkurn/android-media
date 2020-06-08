package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.PreferenceListResponseModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.domain.SetDefaultPreferenceUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.domain.model.SetDefaultPreferenceData
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.domain.model.SetDefaultPreferenceGqlResponse
import javax.inject.Inject

class PreferenceListViewModel @Inject constructor(private val getPreferenceListUseCase: GetPreferenceListUseCase, private val setDefaultPreferenceUseCase: SetDefaultPreferenceUseCase) : ViewModel() {

    private val _preferenceList: MutableLiveData<OccState<PreferenceListResponseModel>> = MutableLiveData()
    val preferenceList: LiveData<OccState<PreferenceListResponseModel>>
        get() = _preferenceList

    private val _setDefaultPreference: MutableLiveData<OccState<SetDefaultPreferenceData>> = MutableLiveData()
    val setDefaultPreference: LiveData<OccState<SetDefaultPreferenceData>>
        get() = _setDefaultPreference

    fun changeDefaultPreference(preference: ProfilesItemModel) {
        _setDefaultPreference.value = OccState.Loading
        setDefaultPreferenceUseCase.execute(preference.profileId!!, { response: SetDefaultPreferenceGqlResponse ->
            _setDefaultPreference.value = OccState.Success(response.response.data)
            getAllPreference()
        }, { throwable: Throwable ->
            _setDefaultPreference.value = OccState.Fail(false, throwable, "")
        })
    }

    fun getAllPreference() {
        _preferenceList.value = OccState.Loading
        getPreferenceListUseCase.execute({ preferenceListResponseModel: PreferenceListResponseModel ->
            _preferenceList.value = OccState.Success(preferenceListResponseModel)
        }, { throwable: Throwable ->
            _preferenceList.value = OccState.Fail(false, throwable, "")
        })
    }

    fun consumePreferenceListFail() {
        val value = _preferenceList.value
        if (value is OccState.Fail) {
            _preferenceList.value = value.copy(isConsumed = true)
        }
    }

}