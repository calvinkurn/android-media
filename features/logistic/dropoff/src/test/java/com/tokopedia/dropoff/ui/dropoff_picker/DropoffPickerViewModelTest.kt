package com.tokopedia.dropoff.ui.dropoff_picker

import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dropoff.data.response.getStore.GetStoreResponse
import com.tokopedia.dropoff.domain.mapper.GetStoreMapper
import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffUiModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class DropoffPickerViewModelTest {

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mapper: GetStoreMapper = mockk()
    private val usecase: GraphqlUseCase<GetStoreResponse> = mockk(relaxed = true)
    private val dispatcher: CoroutineDispatcher = mockk()
    lateinit var viewModel: DropoffPickerViewModel

    private val storeObserver = mockk<Observer<Result<DropoffUiModel>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = DropoffPickerViewModel(dispatcher, usecase, mapper)
        viewModel.storeData.observeForever(storeObserver)
    }

    @Test
    fun `When get store list executed Then usecase executed sequentially correct`() {
        viewModel.getStores("")

        verifySequence {
            usecase.setTypeClass(any())
            usecase.setRequestParams(any())
            usecase.setGraphqlQuery(any())
            usecase.execute(any(), any())
        }
    }

    @Test
    fun `When get store list Given success callback Then live data changed to success`() {
        val testData = DropoffUiModel(listOf(), 1)
        every { usecase.execute(any(), any()) } answers {
            firstArg<(GetStoreResponse) -> Unit>().invoke(GetStoreResponse())
        }
        every { mapper.map(GetStoreResponse()) } returns testData

        viewModel.getStores("")

        verify { storeObserver.onChanged(Success(testData)) }
    }

    @Test
    fun `When get store list Given error callback Then live data changed to fail`() {
        val testError = Throwable("test error")
        every { usecase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(testError)
        }

        viewModel.getStores("")

        verify { storeObserver.onChanged(Fail(testError)) }
    }
}