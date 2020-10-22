package com.tokopedia.oneclickcheckout.preference.list.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.PreferenceListResponseModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.preference.list.domain.SetDefaultPreferenceUseCase
import javax.inject.Inject

class PreferenceListViewModel @Inject constructor(private val getPreferenceListUseCase: GetPreferenceListUseCase, private val setDefaultPreferenceUseCase: SetDefaultPreferenceUseCase) : ViewModel() {

    private val _preferenceList: MutableLiveData<OccState<PreferenceListResponseModel>> = MutableLiveData()
    val preferenceList: LiveData<OccState<PreferenceListResponseModel>>
        get() = _preferenceList

    private val _setDefaultPreference: MutableLiveData<OccState<OccEvent<String>>> = MutableLiveData()
    val setDefaultPreference: LiveData<OccState<OccEvent<String>>>
        get() = _setDefaultPreference

    fun changeDefaultPreference(preference: ProfilesItemModel) {
        _setDefaultPreference.value = OccState.Loading
        OccIdlingResource.increment()
        setDefaultPreferenceUseCase.execute(preference.profileId, { response: String ->
            _setDefaultPreference.value = OccState.Success(OccEvent(response))
            getAllPreference()
            OccIdlingResource.decrement()
        }, { throwable: Throwable ->
            _setDefaultPreference.value = OccState.Failed(Failure(throwable))
            OccIdlingResource.decrement()
        })
    }

    fun getAllPreference() {
        _preferenceList.value = OccState.Loading
        OccIdlingResource.increment()
        getPreferenceListUseCase.execute({ preferenceListResponseModel: PreferenceListResponseModel ->
            _preferenceList.value = OccState.Success(preferenceListResponseModel)
            OccIdlingResource.decrement()
        }, { throwable: Throwable ->
            _preferenceList.value = OccState.Failed(Failure(throwable))
            OccIdlingResource.decrement()
        }, getPreferenceListUseCase.generateRequestParams())
    }

}