package com.tokopedia.dropoff.ui.dropoff_picker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dropoff.data.response.getStore.GetStoreResponse
import com.tokopedia.dropoff.domain.mapper.GetStoreMapper
import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class DropoffPickerViewModelTest {

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mapper: GetStoreMapper = mockk()
    private val repo: GraphqlRepository = mockk()
    lateinit var viewModel: DropoffPickerViewModel

    private val storeObserver = mockk<Observer<Result<DropoffUiModel>>>(relaxed = true)


    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = DropoffPickerViewModel(repo, mapper)
        viewModel.storeData.observeForever(storeObserver)
    }

    @After
    fun release() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When get store list Given success callback Then live data changed to success`() {
        val testData = DropoffUiModel(listOf(), 1)
        val successResult = hashMapOf<Type, Any>(GetStoreResponse::class.java to GetStoreResponse())
        coEvery { repo.getReseponse(any(), any()) } returns GraphqlResponse(
                successResult, HashMap<Type, List<GraphqlError>>(), false)
        every { mapper.map(GetStoreResponse()) } returns testData

        viewModel.getStores("")

        verify { storeObserver.onChanged(Success(testData)) }
    }

    @Test
    fun `When get store throws exception Then live data changed to fail`() {
        val testError = Throwable("test error")
        coEvery { repo.getReseponse(any(), any()) } throws testError

        viewModel.getStores("")

        verify {
            storeObserver.onChanged(Fail(testError))
            mapper wasNot Called
        }
    }

    @Test
    fun `When get store throws cancellation Then live data stays as is`() {
        val testError = CancellationException("cancelled")
        coEvery { repo.getReseponse(any(), any()) } throws testError

        viewModel.getStores("")

        verify { storeObserver wasNot Called }
    }
}