package com.tokopedia.logisticaddaddress.features.addeditaddress.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.domain.param.GetAutoCompleteParam
import com.tokopedia.logisticCommon.domain.usecase.GetAutoCompleteUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchPageViewModel @Inject constructor(
    private val getAutoComplete: GetAutoCompleteUseCase,
    private val autoCompleteMapper: AutoCompleteMapper
) : ViewModel() {

    private val _autoCompleteList = MutableLiveData<Result<Place>>()
    val autoCompleteList: LiveData<Result<Place>>
        get() = _autoCompleteList

    fun getAutoCompleteList(keyword: String) {
        viewModelScope.launch(onErrorAutoComplete) {
            val autoComplete = getAutoComplete(
                GetAutoCompleteParam(
                    keyword = keyword,
                    latLng = "",
                    isManageAddressFlow = true
                )
            )
            val result = autoCompleteMapper.mapAutoComplete(autoComplete)
            _autoCompleteList.value = Success(result)
        }
    }

    private val onErrorAutoComplete = CoroutineExceptionHandler { _, e ->
        _autoCompleteList.value = Fail(e)
    }
}
