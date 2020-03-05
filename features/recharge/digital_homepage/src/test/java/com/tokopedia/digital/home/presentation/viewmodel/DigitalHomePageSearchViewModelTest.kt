package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.digital.home.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalHomePageSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val errorMessage = "unable to retrieve data"

    @RelaxedMockK
    lateinit var searchCategoryHomePageUseCase: SearchCategoryHomePageUseCase

    lateinit var digitalHomePageSearchViewModel: DigitalHomePageSearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        digitalHomePageSearchViewModel =
                DigitalHomePageSearchViewModel(searchCategoryHomePageUseCase, Dispatchers.Unconfined)
    }

    @Test
    fun getSearchCategoryList_Success() {
        coEvery { searchCategoryHomePageUseCase.searchCategoryList(any(), any(), any()) } returns
                listOf(DigitalHomePageSearchCategoryModel(searchQuery = "test"))

        val dataObserver = Observer<Result<List<DigitalHomePageSearchCategoryModel>>> {
            assert(it is Success)
            val response = it as Success
            assert(response.data.isNotEmpty())
            assertEquals(response.data[0].searchQuery, "test")
        }

        try {
            digitalHomePageSearchViewModel.searchCategoryList.observeForever(dataObserver)
            digitalHomePageSearchViewModel.searchCategoryList("", "test")
        } finally {
            digitalHomePageSearchViewModel.searchCategoryList.removeObserver(dataObserver)
        }
    }

    @Test
    fun getSearchCategoryList_Fail() {
        coEvery{ searchCategoryHomePageUseCase.searchCategoryList(any(), any(), any()) } throws
                MessageErrorException(errorMessage)

        val dataObserver = Observer<Result<List<DigitalHomePageSearchCategoryModel>>> {
            assert(it is Fail)
            assertEquals((it as Throwable).message, errorMessage)
        }

        try {
            digitalHomePageSearchViewModel.searchCategoryList.observeForever(dataObserver)
            digitalHomePageSearchViewModel.searchCategoryList("", "test")
        } finally {
            digitalHomePageSearchViewModel.searchCategoryList.removeObserver(dataObserver)
        }
    }
}