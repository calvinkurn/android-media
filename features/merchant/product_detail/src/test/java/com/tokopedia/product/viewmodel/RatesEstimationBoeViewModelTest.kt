package com.tokopedia.product.viewmodel

import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceProduct
import com.tokopedia.product.estimasiongkir.usecase.GetRatesEstimateUseCase
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationBoeViewModel
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 03/03/21
 */
class RatesEstimationBoeViewModelTest : BaseProductViewModelTest() {

    @RelaxedMockK
    private lateinit var ratesUseCase: GetRatesEstimateUseCase

    @RelaxedMockK
    private lateinit var userSessionInterface: UserSessionInterface

    private val viewModel: RatesEstimationBoeViewModel by lazy {
        RatesEstimationBoeViewModel(ratesUseCase, userSessionInterface, CoroutineTestDispatchersProvider)
    }

    private val service = listOf(ServiceModel(
            status = 200,
            products = listOf(ServiceProduct(status = 200))
    ))

    private val ratesResponse = RatesEstimationModel(rates = RatesModel(services = service))

    @Test
    fun `on success get rates data`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        viewModel.setRatesRequest(RatesEstimateRequest())

        coVerify {
            ratesUseCase.executeOnBackground(any(), any())
        }

        Assert.assertNotNull(viewModel.ratesVisitableResult.value)
        Assert.assertTrue(viewModel.ratesVisitableResult.value is Success)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.first() is ProductShippingHeaderDataModel)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data[1] is ProductShippingServiceDataModel)
        print((viewModel.ratesVisitableResult.value as Success).data.size)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.size == 2)
    }

    @Test
    fun `on fail get rates data`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } throws Throwable()

        viewModel.setRatesRequest(RatesEstimateRequest())

        coVerify {
            ratesUseCase.executeOnBackground(any(), any())
        }

        Assert.assertNotNull(viewModel.ratesVisitableResult.value is Fail)
    }
}