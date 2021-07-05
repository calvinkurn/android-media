package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.domain.model.Place
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
class AutoCompleteBottomSheetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val mapper: AutoCompleteMapper = AutoCompleteMapper()

    private val autoCompleteObserver: Observer<Result<Place>> = mockk(relaxed = true)

    private lateinit var autoCompleteBottomSheetViewModel: AutoCompleteBottomSheetViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        autoCompleteBottomSheetViewModel = AutoCompleteBottomSheetViewModel(repo, mapper)
        autoCompleteBottomSheetViewModel.autoCompleteList.observeForever(autoCompleteObserver)
    }

    @Test
    fun `AutoComplete success`() {
        coEvery { repo.getAutoComplete(any()) } returns AutoCompleteResponse()
        autoCompleteBottomSheetViewModel.getAutoCompleteList("")
        verify { autoCompleteObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `AutoComplete failed`() {
        coEvery { repo.getAutoComplete(any()) } throws defaultThrowable
        autoCompleteBottomSheetViewModel.getAutoCompleteList("")
        verify { autoCompleteObserver.onChanged(match { it is Fail }) }
    }

}