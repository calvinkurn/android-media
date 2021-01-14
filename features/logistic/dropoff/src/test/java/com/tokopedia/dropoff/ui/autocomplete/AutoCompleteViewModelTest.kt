package com.tokopedia.dropoff.ui.autocomplete

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dropoff.domain.mapper.AutoCompleteMapper
import com.tokopedia.dropoff.ui.autocomplete.model.ValidatedDistrict
import com.tokopedia.logisticCommon.domain.model.SavedAddress
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.AddressResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AutoCompleteViewModelTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val mapper: AutoCompleteMapper = AutoCompleteMapper() // lets test actual mapper too
    lateinit var viewModel: AutoCompleteViewModel

    private val autoCompleteObserver: Observer<Result<List<SuggestedPlace>>> = mockk(relaxed = true)
    private val validateObserver: Observer<Result<ValidatedDistrict>> = mockk(relaxed = true)
    private val savedObserver: Observer<Result<List<SavedAddress>>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Test
    fun `When autocomplete Given success response Then livedata is changed to success`() {
        coEvery { repo.getAutoComplete(any()) } returns AutoCompleteResponse()
        viewModel.getAutoCompleteList("")
        verify { autoCompleteObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `When autocomplete Given error response Then livedata is changed to fail`() {
        val testError = defaultThrowable
        coEvery { repo.getAutoComplete(any()) } throws testError
        viewModel.getAutoCompleteList("")
        verify { autoCompleteObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `When get district Given success callback Then livedata is changed to success`() {
        coEvery { repo.getDistrict(any()) } returns GetDistrictResponse()
        viewModel.getLatLng("")
        verify { validateObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `When get district Given error callback Then livedata is changed to fail`() {
        coEvery { repo.getDistrict(any()) } throws defaultThrowable
        viewModel.getLatLng("")
        verify { validateObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `When get saved address Given success callback Then livedata is changed to success`() {
        coEvery { repo.getAddress() } returns AddressResponse()
        viewModel.getSavedAddress()
        verify { savedObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `When get saved address Given error callback Then livedata is changed to fail`() {
        coEvery { repo.getAddress() } throws defaultThrowable
        viewModel.getSavedAddress()
        verify { savedObserver.onChanged(match { it is Fail }) }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = AutoCompleteViewModel(repo, mapper)
        viewModel.autoCompleteList.observeForever(autoCompleteObserver)
        viewModel.validatedDistrict.observeForever(validateObserver)
        viewModel.savedAddress.observeForever(savedObserver)
    }

    @After
    fun setDown() {
        Dispatchers.resetMain()
    }

}