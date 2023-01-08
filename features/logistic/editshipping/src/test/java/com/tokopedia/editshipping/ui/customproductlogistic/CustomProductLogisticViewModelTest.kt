package com.tokopedia.editshipping.ui.customproductlogistic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticRepository
import com.tokopedia.logisticCommon.data.response.customproductlogistic.CPLProduct
import com.tokopedia.logisticCommon.data.response.customproductlogistic.GetCPLData
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
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
        val shipperServicesId = 1L
        val mockCPLProduct = arrayListOf(spyk(CPLProduct(shipperServices = arrayListOf(shipperServicesId))))
        val mockGetCPLData = spyk(GetCPLData(cplProduct = mockCPLProduct))
        val mockOngkirGetCPLResponse = spyk(OngkirGetCPLResponse(data = mockGetCPLData))
        val mockResponse = spyk(OngkirGetCPLQGLResponse(response = mockOngkirGetCPLResponse))

        coEvery { repo.getCPLList(any(), any()) } returns mockResponse
        customProductLogisticViewModel.getCPLList(1234, "9876", arrayListOf(shipperServicesId))
        verify { cplListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get CPL List failed`() {
        coEvery { repo.getCPLList(any(), any()) } throws defaultThrowable
        customProductLogisticViewModel.getCPLList(1234, "9876", null)
        verify { cplListObserver.onChanged(match { it is Fail }) }
    }
}