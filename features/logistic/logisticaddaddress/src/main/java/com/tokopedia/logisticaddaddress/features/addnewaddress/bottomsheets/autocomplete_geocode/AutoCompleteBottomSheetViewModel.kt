package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteGeocodeMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeResponseUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class AutoCompleteBottomSheetViewModel @Inject constructor(private val repo: KeroRepository,
                                                           private val mapper: AutoCompleteMapper,
                                                           private val geocodeMapper: AutocompleteGeocodeMapper) : ViewModel() {

    private val _autoCompleteList = MutableLiveData<Result<Place>>()
    val autoCompleteList: LiveData<Result<Place>>
        get() = _autoCompleteList

    private val _autoCompleteGeocodeList = MutableLiveData<Result<AutocompleteGeocodeResponseUiModel>>()
    val autoCompleteGeocodeList: LiveData<Result<AutocompleteGeocodeResponseUiModel>>
        get() = _autoCompleteGeocodeList


    fun getAutoCompleteList(keyword: String) {
        viewModelScope.launch(onErrorAutoComplete) {
            val autoComplete = repo.getAutoComplete(keyword)
            _autoCompleteList.value = Success(mapper.mapAutoComplete(autoComplete))

        }
    }

    fun getAutoCompleteGeocodeList(lat: Double, long: Double) {
        viewModelScope.launch(onErrorAutoCompleteGeocode) {
            val autoCompleteGeocode = repo.getAutoCompleteGeocode(lat, long)
            _autoCompleteGeocodeList.value = Success(geocodeMapper.map(autoCompleteGeocode))
        }
    }

    private val onErrorAutoComplete = CoroutineExceptionHandler { _, e ->
        _autoCompleteList.value = Fail(e)
    }

    private val onErrorAutoCompleteGeocode = CoroutineExceptionHandler { _, e ->
        _autoCompleteGeocodeList.value = Fail(e)
    }
}