package com.tokopedia.editshipping.ui.customproductlogistic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticRepository
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
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
class CustomProductLogisticViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: CustomProductLogisticRepository = mockk(relaxed = true)
    private val mapper = CustomProductLogisticMapper()

    private val cplListObserver: Observer<Result<CustomProductLogisticModel>> =
        mockk(relaxed = true)

    private lateinit var customProductLogisticViewModel: CustomProductLogisticViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        customProductLogisticViewModel = CustomProductLogisticViewModel(repo, mapper)
        customProductLogisticViewModel.cplList.observeForever(cplListObserver)
    }

    @After
    fun setDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Get CPL List success`() {
        coEvery { repo.getCPLList(any(), any()) } returns OngkirGetCPLQGLResponse()
        customProductLogisticViewModel.getCPLList(1234, "9876")
        verify { cplListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get CPL List failed`() {
        coEvery { repo.getCPLList(any(), any()) } throws defaultThrowable
        customProductLogisticViewModel.getCPLList(1234, "9876")
        verify { cplListObserver.onChanged(match { it is Fail }) }
    }
}