package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.home.RechargeHomepageTestDispatchersProvider
import com.tokopedia.digital.home.old.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalHomePageSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var searchCategoryHomePageUseCase: SearchCategoryHomePageUseCase

    lateinit var digitalHomePageSearchViewModel: DigitalHomePageSearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        digitalHomePageSearchViewModel =
                DigitalHomePageSearchViewModel(searchCategoryHomePageUseCase, RechargeHomepageTestDispatchersProvider())
    }

    @Test
    fun getSearchCategoryList_Success() {
        coEvery { searchCategoryHomePageUseCase.searchCategoryList(any(), any(), any()) } returns
                listOf(DigitalHomePageSearchCategoryModel(searchQuery = "test"))

        digitalHomePageSearchViewModel.searchCategoryList("", "test")
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.isNotEmpty())
        assertEquals(response.data[0].searchQuery, "test")
    }

    @Test
    fun getSearchCategoryList_Fail() {
        coEvery{ searchCategoryHomePageUseCase.searchCategoryList(any(), any(), any()) } throws
                MessageErrorException()

        digitalHomePageSearchViewModel.searchCategoryList("", "test")
        val actualData = digitalHomePageSearchViewModel.searchCategoryList.value
        assert(actualData is Fail)
    }
}