package com.tokopedia.travel.country_code.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.travel.country_code.domain.TravelCountryCodeUseCase
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 23/12/2019
 */
class PhoneCodePickerViewModel @Inject constructor(
        private val useCase: TravelCountryCodeUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private companion object{
        const val MIN_LENGTH_KEYWORD = 2
        const val DELAY_TIME = 250L
    }

    private val _countryList = MutableLiveData<Result<List<TravelCountryPhoneCode>>>()
    val countryList: LiveData<Result<List<TravelCountryPhoneCode>>>
        get() = _countryList

    private val _filteredCountryList = MutableLiveData<Result<List<TravelCountryPhoneCode>>>()
    val filteredCountryList: LiveData<Result<List<TravelCountryPhoneCode>>>
        get() = _filteredCountryList

    fun getCountryList(rawQuery: GqlQueryInterface) {
        launch {
            _countryList.value = useCase.execute(rawQuery)
            _filteredCountryList.value = _countryList.value
        }
    }

    fun filterCountryList(keyword: String) {
        if (keyword.length >= MIN_LENGTH_KEYWORD && _countryList.value is Success) {
            launch {
                delay(DELAY_TIME)
                val filteredData = (_countryList.value as Success<List<TravelCountryPhoneCode>>).data.filter {
                    it.countryName.toLowerCase().contains(keyword.toLowerCase()) || it.countryId.toLowerCase().contains(keyword.toLowerCase()) || it.countryPhoneCode == convertKeywordToInt(keyword)
                }
                _filteredCountryList.value = Success(filteredData)
            }
        }else{
            _filteredCountryList.value = _countryList.value
        }
    }

    private fun convertKeywordToInt(keyword: String): Int = try {
        keyword.toInt()
    } catch (e: NumberFormatException) {
        0
    }
}