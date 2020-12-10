package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AutoCompleteBottomSheetViewModel @Inject constructor(private val repo: KeroRepository,
                                                           private val mapper: AutoCompleteMapper) : ViewModel(), CoroutineScope {

    private val _autoCompleteList = MutableLiveData<Result<Place>>()
    val autoCompleteList: LiveData<Result<Place>>
        get() = _autoCompleteList

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + onError

    fun getAutoCompleteList(keyword: String) {
        viewModelScope.launch(onError) {
            val autoComplete = repo.getAutoComplete(keyword)
            _autoCompleteList.value = Success(mapper.mapAutoComplete(autoComplete))
        }
    }

    private val onError = CoroutineExceptionHandler { _, e ->
        _autoCompleteList.value = Fail(e)
    }
}