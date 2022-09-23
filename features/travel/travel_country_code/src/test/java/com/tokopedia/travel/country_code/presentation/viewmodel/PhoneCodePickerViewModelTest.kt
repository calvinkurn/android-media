package com.tokopedia.travel.country_code.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.travel.country_code.DummyGqlQueryInterface
import com.tokopedia.travel.country_code.domain.TravelCountryCodeUseCase
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PhoneCodePickerViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val coroutineDispatcher = CoroutineTestDispatchers
    private val fakeGqlQueryInterface = DummyGqlQueryInterface()

    private lateinit var phoneCodePickerViewModel: PhoneCodePickerViewModel

    @MockK
    lateinit var travelCountryCodeUseCase: TravelCountryCodeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        phoneCodePickerViewModel = PhoneCodePickerViewModel(travelCountryCodeUseCase, coroutineDispatcher.main)
    }

    @Test
    fun `fetch country list should be success`(){
        //given
        val expected = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62))
        coEvery { travelCountryCodeUseCase.execute(any()) } returns Success(expected)

        //when
        phoneCodePickerViewModel.getCountryList(fakeGqlQueryInterface)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Success)
        assertFalse(result is Fail)

        val data = result as Success
        assertEquals(expected, data.data)
        assertEquals(expected.size, data.data.size)
        val firstElementData = data.data[0]
        assertEquals(expected[0].countryId, firstElementData.countryId)
        assertEquals(expected[0].countryName, firstElementData.countryName)
        assertEquals(expected[0].countryPhoneCode, firstElementData.countryPhoneCode)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `fetch country list data filtered should be success`(){
        //given
        val expected = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62))

        coEvery { travelCountryCodeUseCase.execute(any()) } returns Success(expected)

        //when
        phoneCodePickerViewModel.getCountryList(fakeGqlQueryInterface)

        //then
        val result = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(result is Success)
        assertFalse(result is Fail)

        val data = result as Success
        assertEquals(expected, data.data)
        assertEquals(expected.size, data.data.size)
        val firstElementData = data.data[0]
        assertEquals(expected[0].countryId, firstElementData.countryId)
        assertEquals(expected[0].countryName, firstElementData.countryName)
        assertEquals(expected[0].countryPhoneCode, firstElementData.countryPhoneCode)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `fetch country list should be failed`(){
        //given
        val expected = Throwable(message = "failed to fetch")
        coEvery { travelCountryCodeUseCase.execute(any()) } returns Fail(expected)

        //when
        phoneCodePickerViewModel.getCountryList(fakeGqlQueryInterface)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Fail)
        assertFalse(result is Success)

        val data = result as Fail
        assertEquals(expected.message, data.throwable.message)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `fetch country list filtered should be failed`(){
        //given
        val expected = Throwable(message = "failed to fetch")
        coEvery { travelCountryCodeUseCase.execute(any()) } returns Fail(expected)

        //when
        phoneCodePickerViewModel.getCountryList(fakeGqlQueryInterface)

        //then
        val result = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(result is Fail)
        assertFalse(result is Success)

        val data = result as Fail
        assertEquals(expected.message, data.throwable.message)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with valid countryName keyword when country list success fetched should be success`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "Korea"
        val expected = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62),
            TravelCountryPhoneCode(countryId = "KR", countryName = "Korea Selatan", countryPhoneCode = 82),
            TravelCountryPhoneCode(countryId = "JP", countryName = "Jepang", countryPhoneCode = 81))
        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        coroutineDispatcher.coroutineDispatcher.advanceUntilIdle()

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Success)
        assertFalse(result is Fail)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Success)
        assertFalse(filteredDataResult is Fail)

        val data = filteredDataResult as Success
        assertFalse(expected == data.data)
        assertFalse(expected.size == data.data.size)
        assertEquals(1, data.data.size)
        assertEquals(expected[1].countryPhoneCode, data.data[0].countryPhoneCode)
        assertEquals(expected[1].countryName, data.data[0].countryName)
        assertEquals(expected[1].countryId, data.data[0].countryId)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with valid countryId keyword when country list success fetched should be success`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "JP"
        val expected = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62),
            TravelCountryPhoneCode(countryId = "KR", countryName = "Korea Selatan", countryPhoneCode = 82),
            TravelCountryPhoneCode(countryId = "JP", countryName = "Jepang", countryPhoneCode = 81))
        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        coroutineDispatcher.coroutineDispatcher.advanceUntilIdle()

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Success)
        assertFalse(result is Fail)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Success)
        assertFalse(filteredDataResult is Fail)

        val data = filteredDataResult as Success
        assertFalse(expected == data.data)
        assertFalse(expected.size == data.data.size)
        assertEquals(1, data.data.size)
        assertEquals(expected[2].countryPhoneCode, data.data[0].countryPhoneCode)
        assertEquals(expected[2].countryName, data.data[0].countryName)
        assertEquals(expected[2].countryId, data.data[0].countryId)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with valid countryPhoneCode keyword when country list success fetched should be success`() {
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "81"
        val expected = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62),
            TravelCountryPhoneCode(countryId = "KR", countryName = "Korea Selatan", countryPhoneCode = 82),
            TravelCountryPhoneCode(countryId = "JP", countryName = "Jepang", countryPhoneCode = 81))
        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        coroutineDispatcher.coroutineDispatcher.advanceUntilIdle()

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Success)
        assertFalse(result is Fail)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Success)
        assertFalse(filteredDataResult is Fail)

        val data = filteredDataResult as Success
        assertFalse(expected == data.data)
        assertFalse(expected.size == data.data.size)
        assertEquals(1, data.data.size)
        assertEquals(expected[2].countryPhoneCode, data.data[0].countryPhoneCode)
        assertEquals(expected[2].countryName, data.data[0].countryName)
        assertEquals(expected[2].countryId, data.data[0].countryId)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with empty keyword when country list success fetched should be success`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = ""
        val expected = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62),
            TravelCountryPhoneCode(countryId = "KR", countryName = "Korea Selatan", countryPhoneCode = 82),
            TravelCountryPhoneCode(countryId = "JP", countryName = "Jepang", countryPhoneCode = 81))

        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Success)
        assertFalse(result is Fail)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Success)
        assertFalse(filteredDataResult is Fail)

        val data = filteredDataResult as Success
        assertEquals(expected, data.data)
        assertEquals(expected.size, data.data.size)
        assertEquals(expected[0].countryPhoneCode, data.data[0].countryPhoneCode)
        assertEquals(expected[0].countryName, data.data[0].countryName)
        assertEquals(expected[0].countryId, data.data[0].countryId)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with invalid keyword when country list success fetched should be success`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "*()&"
        val expected = listOf(TravelCountryPhoneCode(countryId = "ID", countryName = "Indonesia", countryPhoneCode = 62),
            TravelCountryPhoneCode(countryId = "KR", countryName = "Korea Selatan", countryPhoneCode = 82),
            TravelCountryPhoneCode(countryId = "JP", countryName = "Jepang", countryPhoneCode = 81))

        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Success(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Success)
        assertFalse(result is Fail)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Success)
        assertFalse(filteredDataResult is Fail)

        val data = filteredDataResult as Success
        assertEquals(expected, data.data)
        assertEquals(expected.size, data.data.size)
        assertEquals(expected[0].countryPhoneCode, data.data[0].countryPhoneCode)
        assertEquals(expected[0].countryName, data.data[0].countryName)
        assertEquals(expected[0].countryId, data.data[0].countryId)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with valid countryName keyword when country list failed fetched should be failed`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "Korea"
        val expected = Throwable(message = "fetch failed")

        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Fail(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Fail)
        assertFalse(result is Success)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Fail)
        assertFalse(filteredDataResult is Success)

        val data = filteredDataResult as Fail
        assertEquals(expected.message, data.throwable.message)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with valid countryCode keyword when country list failed fetched should be failed`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "KR"
        val expected = Throwable(message = "fetch failed")

        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Fail(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Fail)
        assertFalse(result is Success)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Fail)
        assertFalse(filteredDataResult is Success)

        val data = filteredDataResult as Fail
        assertEquals(expected.message, data.throwable.message)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with valid number keyword when country list failed fetched should be failed`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "82"
        val expected = Throwable(message = "fetch failed")

        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Fail(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Fail)
        assertFalse(result is Success)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Fail)
        assertFalse(filteredDataResult is Success)

        val data = filteredDataResult as Fail
        assertEquals(expected.message, data.throwable.message)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with invalid keyword when country list failed fetched should be failed`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = "&*("
        val expected = Throwable(message = "fetch failed")

        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Fail(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Fail)
        assertFalse(result is Success)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Fail)
        assertFalse(filteredDataResult is Success)

        val data = filteredDataResult as Fail
        assertEquals(expected.message, data.throwable.message)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `filter country list with empty keyword when country list failed fetched should be failed`(){
        //given
        val rawQuery = fakeGqlQueryInterface
        val keyword = ""
        val expected = Throwable(message = "fetch failed")

        coEvery { travelCountryCodeUseCase.execute(rawQuery) } returns Fail(expected)

        //when
        phoneCodePickerViewModel.getCountryList(rawQuery)
        phoneCodePickerViewModel.filterCountryList(keyword)

        //then
        val result = phoneCodePickerViewModel.countryList.value
        assertTrue(result is Fail)
        assertFalse(result is Success)

        val filteredDataResult = phoneCodePickerViewModel.filteredCountryList.value
        assertTrue(filteredDataResult is Fail)
        assertFalse(filteredDataResult is Success)

        val data = filteredDataResult as Fail
        assertEquals(expected.message, data.throwable.message)

        coVerify { travelCountryCodeUseCase.execute(any()) }
    }

    @Test
    fun `convert keyword to int should be success`(){
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
    fun `convert keyword to int should be failed`(){
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