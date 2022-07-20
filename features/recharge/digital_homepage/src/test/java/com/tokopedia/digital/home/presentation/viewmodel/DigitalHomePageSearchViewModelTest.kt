package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.home.model.Tracking
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.digital.home.old.domain.DigitalHomepageSearchByDynamicIconUseCase
import com.tokopedia.digital.home.old.domain.SearchAutoCompleteHomePageUseCase
import com.tokopedia.digital.home.old.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchNewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalHomePageSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var searchCategoryHomePageUseCase: SearchCategoryHomePageUseCase

    @RelaxedMockK
    lateinit var searchCategoryByDynamicIconUseCase: DigitalHomepageSearchByDynamicIconUseCase

    @RelaxedMockK
    lateinit var searchAutoCompleteHomePageUseCase: SearchAutoCompleteHomePageUseCase

    lateinit var digitalHomePageSearchViewModel: DigitalHomePageSearchViewModel

    val searchParam = "navsource=tnb&q=paket&source=search&categoryid="
    val searchQuery = "paket"
    val mapSearchParam = mapOf(DigitalHomePageSearchViewModel.PARAM to searchParam)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        digitalHomePageSearchViewModel =
                DigitalHomePageSearchViewModel(searchCategoryHomePageUseCase, searchCategoryByDynamicIconUseCase,
                        searchAutoCompleteHomePageUseCase, CoroutineTestDispatchersProvider)
    }

    @Test
    fun getSearchCategoryList_Success() {
        coEvery { searchCategoryHomePageUseCase.searchCategoryList(any(), any(), any()) } returns
               DigitalHomePageSearchNewModel(false, Tracking(), "test", listOf(DigitalHomePageSearchCategoryModel(searchQuery = "test")))

        digitalHomePageSearchViewModel.searchCategoryList("", "test")
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.listSearchResult.isNotEmpty())
        assertEquals(response.data.listSearchResult[0].searchQuery, "test")
    }

    @Test
    fun getSearchCategoryList_Fail() {
        coEvery{ searchCategoryHomePageUseCase.searchCategoryList(any(), any(), any()) } throws
                MessageErrorException()

        digitalHomePageSearchViewModel.searchCategoryList("", "test")
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Fail)
    }

    @Test
    fun getSearchCategoryByDynamicIcons_Success() {
        //given
        coEvery {
            searchCategoryByDynamicIconUseCase.searchCategoryList(any(), any())
        } returns DigitalHomePageSearchNewModel(false, Tracking(), "test", listOf(DigitalHomePageSearchCategoryModel(searchQuery = "test")))

        //when
        digitalHomePageSearchViewModel.searchByDynamicIconsCategory("", 0, listOf())

        //then
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.listSearchResult.isNotEmpty())
        assertEquals(response.data.listSearchResult[0].searchQuery, "test")
    }

    @Test
    fun getSearchCategoryByDynamicIcons_Fail() {
        //given
        coEvery{ searchCategoryByDynamicIconUseCase.searchCategoryList(any(), any()) } throws
                MessageErrorException()

        //when
        digitalHomePageSearchViewModel.searchByDynamicIconsCategory("", 0, listOf())

        //then
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Fail)
    }

    @Test
    fun getMapSearchAutoComplete(){
        //when
        val searchParamResult = digitalHomePageSearchViewModel.mapAutoCompleteParams(searchQuery)

        //then
        assertEquals(mapSearchParam, searchParamResult)
    }

    @Test
    fun getSearchAutoComplete_Success() {
        //given
        coEvery {
            searchAutoCompleteHomePageUseCase.searchAutoCompleteList(any(), any())
        } returns DigitalHomePageSearchNewModel(true, Tracking(), "test", listOf(DigitalHomePageSearchCategoryModel(searchQuery = "test")))

        //when
        digitalHomePageSearchViewModel.searchAutoComplete(mapSearchParam, "paket")

        //then
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.listSearchResult.isNotEmpty())
        assertEquals(response.data.isFromAutoComplete, true)
        assertEquals(response.data.listSearchResult[0].searchQuery, "test")
    }

    @Test
    fun getSearchAutoComplete_Fail() {
        //given
        coEvery{ searchAutoCompleteHomePageUseCase.searchAutoCompleteList(any(), any()) } throws
                MessageErrorException()

        //when
        digitalHomePageSearchViewModel.searchAutoComplete(mapSearchParam, searchQuery)

        //then
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Fail)
    }

    @Test
    fun cancelSearchAutoComplete_Success() {
        //given
        val job = Job()
        digitalHomePageSearchViewModel.job = job

        //when
        digitalHomePageSearchViewModel.cancelAutoComplete()

        //then
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.listSearchResult.isEmpty())
        assertEquals(response.data.isFromAutoComplete, false)
    }

    @Test
    fun cancelSearchAutoComplete_NotInitialzed() {
        //when
        digitalHomePageSearchViewModel.cancelAutoComplete()

        //then
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.listSearchResult.isEmpty())
        assertEquals(response.data.isFromAutoComplete, false)
    }

    @Test
    fun cancelSearchAutoComplete_NotActive() {
        GlobalScope.launch {
            //given
            val job = Job()
            digitalHomePageSearchViewModel.job = job
            digitalHomePageSearchViewModel.job.cancelAndJoin()

            //when
            digitalHomePageSearchViewModel.cancelAutoComplete()

            //then
            val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
            assert(actualData is Success)
            val response = actualData as Success
            assert(response.data.listSearchResult.isEmpty())
            assertEquals(response.data.isFromAutoComplete, false)
        }
    }

    @Test
    fun getJob(){
        //given
        val job = Job()

        //when
        digitalHomePageSearchViewModel.job = job

        //then
        assertEquals(job, digitalHomePageSearchViewModel.job)
    }
}