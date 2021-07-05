package com.tokopedia.travel.country_code.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travel.country_code.domain.TravelCountryCodeUseCase
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.country_code.util.TravelCountryCodeGqlQuery
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class PhoneCodePickerViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var phoneCodePickerViewModel: PhoneCodePickerViewModel

    @MockK
    lateinit var travelCountryCodeUseCase: TravelCountryCodeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        phoneCodePickerViewModel = PhoneCodePickerViewModel(travelCountryCodeUseCase, Dispatchers.Unconfined)
    }

    @Test
    fun getCountryList_shouldReturnSuccess(){
        //given
        val rawQuery = TravelCountryCodeGqlQuery.ALL_COUNTRY
        val countryList = listOf<TravelCountryPhoneCode>(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62))
        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(countryList)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)

        //then
        val data = (phoneCodePickerViewModel.countryList.value as Success<List<TravelCountryPhoneCode>>).data
        assert(phoneCodePickerViewModel.countryList.value is Success)
        assertEquals(data.size, countryList.size)
        assertEquals(data, countryList)
        assertEquals(data[0].countryId, countryList[0].countryId)
        assertEquals(data[0].countryName, countryList[0].countryName)
        assertEquals(data[0].countryPhoneCode, countryList[0].countryPhoneCode)

        val dataForFiltered = (phoneCodePickerViewModel.filteredCountryList.value as Success<List<TravelCountryPhoneCode>>).data
        assert(phoneCodePickerViewModel.filteredCountryList.value is Success)
        assertEquals(dataForFiltered.size, countryList.size)
        assertEquals(dataForFiltered, countryList)
        assertEquals(dataForFiltered[0].countryId, countryList[0].countryId)
        assertEquals(dataForFiltered[0].countryName, countryList[0].countryName)
        assertEquals(dataForFiltered[0].countryPhoneCode, countryList[0].countryPhoneCode)
    }

    @Test
    fun filterCountryList_isFiltered(){
        //given
        val rawQuery = TravelCountryCodeGqlQuery.ALL_COUNTRY
        val keyword = "Indo"
        val countryList = listOf<TravelCountryPhoneCode>(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62),
                TravelCountryPhoneCode(countryId = "KR", countryName = "Korea Selatan", countryPhoneCode = 82),
                TravelCountryPhoneCode(countryId = "JP", countryName = "Jepang", countryPhoneCode = 81))
        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(countryList)
        phoneCodePickerViewModel.getCountryList(rawQuery)

        //when
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val data = (phoneCodePickerViewModel.countryList.value as Success<List<TravelCountryPhoneCode>>).data
        assert(phoneCodePickerViewModel.countryList.value is Success)
        assertEquals(data.size, countryList.size)
        assertEquals(data, countryList)

        val dataForFiltered = (phoneCodePickerViewModel.filteredCountryList.value as Success<List<TravelCountryPhoneCode>>).data
        assert(phoneCodePickerViewModel.filteredCountryList.value is Success)
        assertEquals(dataForFiltered[0].countryId, countryList[0].countryId)
        assertEquals(dataForFiltered[0].countryName, countryList[0].countryName)
        assertEquals(dataForFiltered[0].countryPhoneCode, countryList[0].countryPhoneCode)
    }

    @Test
    fun filterCountryList_isReset(){
        //given
        val rawQuery = TravelCountryCodeGqlQuery.ALL_COUNTRY
        val keyword = ""
        val countryList = listOf<TravelCountryPhoneCode>(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62),
                TravelCountryPhoneCode(countryId = "KR", countryName = "Korea Selatan", countryPhoneCode = 82),
                TravelCountryPhoneCode(countryId = "JP", countryName = "Jepang", countryPhoneCode = 81))
        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(countryList)
        phoneCodePickerViewModel.getCountryList(rawQuery)

        //when
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val data = (phoneCodePickerViewModel.countryList.value as Success<List<TravelCountryPhoneCode>>).data
        assert(phoneCodePickerViewModel.countryList.value is Success)
        assertEquals(data.size, countryList.size)
        assertEquals(data, countryList)
        assertEquals(data[0].countryId, countryList[0].countryId)
        assertEquals(data[0].countryName, countryList[0].countryName)
        assertEquals(data[0].countryPhoneCode, countryList[0].countryPhoneCode)

        val dataForFiltered = (phoneCodePickerViewModel.filteredCountryList.value as Success<List<TravelCountryPhoneCode>>).data
        assert(phoneCodePickerViewModel.filteredCountryList.value is Success)
        assertEquals(dataForFiltered.size, countryList.size)
        assertEquals(dataForFiltered, countryList)
        assertEquals(dataForFiltered[0].countryId, countryList[0].countryId)
        assertEquals(dataForFiltered[0].countryName, countryList[0].countryName)
        assertEquals(dataForFiltered[0].countryPhoneCode, countryList[0].countryPhoneCode)
    }

    @Test
    fun convertKeywordToInt_isSuccess(){
        //given
        val keyword = "62"
        val expected = keyword.toInt()
        val method = phoneCodePickerViewModel.javaClass.getDeclaredMethod("convertKeywordToInt", String::class.java)
        method.isAccessible = true
        val param = arrayOfNulls<String>(1)
        param[0] = keyword

        //when
        val result: Int = method.invoke(phoneCodePickerViewModel, *param) as Int

        //then
        assertEquals(result, expected)
    }

    @Test
    fun convertKeywordToInt_isFailed(){
        //given
        val keyword = "*(&"
        val expected = 0
        val method = phoneCodePickerViewModel.javaClass.getDeclaredMethod("convertKeywordToInt", String::class.java)
        method.isAccessible = true
        val param = arrayOfNulls<String>(1)
        param[0] = keyword

        //when
        val result: Int = method.invoke(phoneCodePickerViewModel, *param) as Int

        //then
        assertEquals(result, expected)
    }
}