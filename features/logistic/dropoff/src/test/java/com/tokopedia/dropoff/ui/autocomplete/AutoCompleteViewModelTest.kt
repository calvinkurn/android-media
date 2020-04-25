package com.tokopedia.dropoff.ui.autocomplete

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dropoff.data.response.getAddress.AddressResponse
import com.tokopedia.dropoff.data.response.getDistrict.GetDistrictResponse
import com.tokopedia.dropoff.domain.mapper.AutoCompleteMapper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticdata.data.autocomplete.SuggestedPlace
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

    val dispatcher: CoroutineDispatcher = mockk()
    val autoCompleteUseCase: GraphqlUseCase<AutocompleteResponse> = mockk(relaxUnitFun = true)
    val getDistrictUseCase: GraphqlUseCase<GetDistrictResponse> = mockk(relaxUnitFun = true)
    val getSavedAddressUseCase: GraphqlUseCase<AddressResponse> = mockk(relaxUnitFun = true)
    val mapper: AutoCompleteMapper = mockk()
    lateinit var viewModel: AutoCompleteViewModel

    @Before
    fun setUp() {
        viewModel = AutoCompleteViewModel(dispatcher, autoCompleteUseCase,
                getDistrictUseCase, getSavedAddressUseCase, mapper)
    }

    @Test
    fun `When autocomplete Given success response Then livedata is changed to success`() {
        val autoCompleteObserver: Observer<Result<List<SuggestedPlace>>> = mockk(relaxed = true)
        val successResponse = AutocompleteResponse()
        val successList = listOf(
                SuggestedPlace("Jakarta Pusat")
        )
        every { autoCompleteUseCase.execute(any(), any()) } answers {
            firstArg<(AutocompleteResponse) -> Unit>().invoke(successResponse)
        }
        every { mapper.mapAutoComplete(successResponse) } returns successList

        viewModel.autoCompleteList.observeForever(autoCompleteObserver)
        viewModel.getAutoCompleteList("")

        verify { autoCompleteObserver.onChanged(Success(successList)) }
    }
}