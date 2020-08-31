package com.tokopedia.dropoff.ui.autocomplete

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dropoff.data.response.getAddress.AddressResponse
import com.tokopedia.dropoff.data.response.getDistrict.GetDistrictResponse
import com.tokopedia.dropoff.domain.mapper.AutoCompleteMapper
import com.tokopedia.dropoff.ui.autocomplete.model.ValidatedDistrict
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.dropoff.data.response.autoComplete.AutocompleteResponse
import com.tokopedia.logisticdata.data.autocomplete.SavedAddress
import com.tokopedia.logisticdata.data.autocomplete.SuggestedPlace
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AutoCompleteViewModelTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher: CoroutineDispatcher = mockk()
    private val autoCompleteUseCase: GraphqlUseCase<AutocompleteResponse> = mockk(relaxUnitFun = true)
    private val getDistrictUseCase: GraphqlUseCase<GetDistrictResponse> = mockk(relaxUnitFun = true)
    private val getSavedAddressUseCase: GraphqlUseCase<AddressResponse> = mockk(relaxUnitFun = true)
    private val mapper: AutoCompleteMapper = mockk()
    lateinit var viewModel: AutoCompleteViewModel

    private val autoCompleteObserver: Observer<Result<List<SuggestedPlace>>> = mockk(relaxed = true)
    private val validateObserver: Observer<Result<ValidatedDistrict>> = mockk(relaxed = true)
    private val savedObserver: Observer<Result<List<SavedAddress>>> = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = AutoCompleteViewModel(dispatcher, autoCompleteUseCase,
                getDistrictUseCase, getSavedAddressUseCase, mapper)
        viewModel.autoCompleteList.observeForever(autoCompleteObserver)
        viewModel.validatedDistrict.observeForever(validateObserver)
        viewModel.savedAddress.observeForever(savedObserver)
    }

    @Test
    fun `When autocomplete Given success response Then livedata is changed to success`() {
        val successResponse = AutocompleteResponse()
        val successList = listOf(
                SuggestedPlace("Jakarta Pusat")
        )
        every { autoCompleteUseCase.execute(any(), any()) } answers {
            firstArg<(AutocompleteResponse) -> Unit>().invoke(successResponse)
        }
        every { mapper.mapAutoComplete(successResponse) } returns successList

        viewModel.getAutoCompleteList("")

        verify { autoCompleteObserver.onChanged(Success(successList)) }
    }

    @Test
    fun `When autocomplete Given error response Then livedata is changed to fail`() {
        val testError = Throwable("test error")
        every { autoCompleteUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(testError)
        }

        viewModel.getAutoCompleteList("")

        verify { autoCompleteObserver.onChanged(Fail(testError)) }
    }

    @Test
    fun `When get district Given success callback Then livedata is changed to success`() {
        val successResponse = GetDistrictResponse()
        val successDistrict = ValidatedDistrict()
        every { getDistrictUseCase.execute(any(), any()) } answers {
            firstArg<(GetDistrictResponse) -> Unit>().invoke(successResponse)
        }
        every { mapper.mapValidate(successResponse) } returns successDistrict

        viewModel.getLatLng("")

        verify { validateObserver.onChanged(Success(successDistrict)) }
    }

    @Test
    fun `When get district Given error callback Then livedata is changed to fail`() {
        val testError = Throwable("test error")
        every { getDistrictUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(testError)
        }

        viewModel.getLatLng("")

        verify { validateObserver.onChanged(Fail(testError)) }
    }

    @Test
    fun `When get saved address Given success callback Then livedata is changed to success`() {
        val successResponse = AddressResponse()
        val successDistrict = listOf(
                SavedAddress(addrId = 99)
        )
        every { getSavedAddressUseCase.execute(any(), any()) } answers {
            firstArg<(AddressResponse) -> Unit>().invoke(successResponse)
        }
        every { mapper.mapAddress(successResponse) } returns successDistrict

        viewModel.getSavedAddress()

        verify { savedObserver.onChanged(Success(successDistrict)) }
    }

    @Test
    fun `When get saved address Given error callback Then livedata is changed to fail`() {
        val testError = Throwable("test error")
        every { getSavedAddressUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(testError)
        }

        viewModel.getSavedAddress()

        verify { savedObserver.onChanged(Fail(testError)) }
    }

}