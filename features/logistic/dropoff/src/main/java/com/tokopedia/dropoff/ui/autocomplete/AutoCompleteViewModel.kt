package com.tokopedia.dropoff.ui.autocomplete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.dropoff.domain.mapper.AutoCompleteMapper
import com.tokopedia.logisticCommon.domain.model.SavedAddress
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticCommon.domain.param.GetAddressParam
import com.tokopedia.logisticCommon.domain.param.GetAutoCompleteParam
import com.tokopedia.logisticCommon.domain.usecase.GetAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetAutoCompleteUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class AutoCompleteViewModel @Inject constructor(
    private val getAutoComplete: GetAutoCompleteUseCase,
    private val getAddress: GetAddressUseCase,
    private val mapper: AutoCompleteMapper
) : ViewModel() {

    private val mAutoCompleteList = MutableLiveData<Result<List<SuggestedPlace>>>()
    val autoCompleteList: LiveData<Result<List<SuggestedPlace>>>
        get() = mAutoCompleteList
    private val mSavedAddress = MutableLiveData<Result<List<SavedAddress>>>()
    val savedAddress: LiveData<Result<List<SavedAddress>>>
        get() = mSavedAddress

    fun getAutoCompleteList(keyword: String) {
        viewModelScope.launch(onErrorAutoComplete) {
            val autoComplete = getAutoComplete(GetAutoCompleteParam(keyword = keyword))
            mAutoCompleteList.value = Success(mapper.mapAutoComplete(autoComplete))
        }
    }

    fun getSavedAddress() {
        viewModelScope.launch(onErrorGetAddress) {
            val address = getAddress(GetAddressParam())
            mSavedAddress.value = Success(mapper.mapAddress(address))
        }
    }

    private val onErrorAutoComplete = CoroutineExceptionHandler { _, e ->
        mAutoCompleteList.value = Fail(e)
    }

    private val onErrorGetAddress = CoroutineExceptionHandler { _, e ->
        mSavedAddress.value = Fail(e)
    }
}
