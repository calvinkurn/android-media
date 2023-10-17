package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.domain.usecase.GetAutoCompleteUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: GetAutoCompleteUseCase = mockk(relaxed = true)
    private val autoCompleterMapper = AutoCompleteMapper()

    private lateinit var searchPageViewModel: SearchPageViewModel

    private val autoCompleteListObserver: Observer<Result<Place>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        searchPageViewModel = SearchPageViewModel(repo, autoCompleterMapper)
        searchPageViewModel.autoCompleteList.observeForever(autoCompleteListObserver)
    }

    @Test
    fun `verify when get auto complete list success`() {
        // Given
        coEvery { repo(any()) } returns AutoCompleteResponse()

        // When
        searchPageViewModel.getAutoCompleteList("Jakarta")

        // Then
        verify { autoCompleteListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `verify when get auto complete list failed`() {
        // Given
        coEvery { repo(any()) } throws defaultThrowable

        // When
        searchPageViewModel.getAutoCompleteList("Jakarta")

        // Then
        verify { autoCompleteListObserver.onChanged(match { it is Fail }) }
    }
}
