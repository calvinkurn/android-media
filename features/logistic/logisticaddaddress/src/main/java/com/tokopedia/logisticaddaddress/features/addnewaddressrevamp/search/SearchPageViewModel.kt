package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchPageViewModel @Inject constructor(
    private val repo: KeroRepository,
    private val autoCompleteMapper: AutoCompleteMapper
) : ViewModel() {

    companion object {
        private const val DEFAULT_LONG = 0.0
        private const val DEFAULT_LAT = 0.0
    }

    var saveAddressDataModel = SaveAddressDataModel()
    var isGmsAvailable: Boolean = true
    var isPositiveFlow: Boolean = true
    var isFromPinpoint: Boolean = false
    var isPolygon: Boolean = false
    var isEdit: Boolean = false
    var source: String = ""

    var currentLat: Double = DEFAULT_LAT
    var currentLong: Double = DEFAULT_LONG

    private val _autoCompleteList = MutableLiveData<Result<Place>>()
    val autoCompleteList: LiveData<Result<Place>>
        get() = _autoCompleteList

    fun setDataFromArguments(
        isPositiveFlow: Boolean,
        isFromPinpoint: Boolean,
        isPolygon: Boolean,
        isEdit: Boolean,
        source: String,
        addressData: SaveAddressDataModel?
    ) {
        this.isPositiveFlow = isPositiveFlow
        this.isFromPinpoint = isFromPinpoint
        this.isPolygon = isPolygon
        this.isEdit = isEdit
        this.source = source

        addressData?.apply {
            saveAddressDataModel = this
        }
    }

    fun setLatLong(
        latitude: Double,
        longitude: Double
    ) {
        currentLat = latitude
        currentLong = longitude
    }

    fun loadAutoComplete(input: String) {
        if (currentLat != DEFAULT_LAT && currentLong != DEFAULT_LONG) {
            getAutoCompleteList(input, "$currentLat,$currentLong")
        } else {
            getAutoCompleteList(input, "")
        }
    }

    private fun getAutoCompleteList(keyword: String, latlng: String) {
        viewModelScope.launch(onErrorAutoComplete) {
            val autoComplete = repo.getAutoComplete(keyword, latlng)
            _autoCompleteList.value = Success(autoCompleteMapper.mapAutoComplete(autoComplete))
        }
    }

    private val onErrorAutoComplete = CoroutineExceptionHandler { _, e ->
        _autoCompleteList.value = Fail(e)
    }
}
