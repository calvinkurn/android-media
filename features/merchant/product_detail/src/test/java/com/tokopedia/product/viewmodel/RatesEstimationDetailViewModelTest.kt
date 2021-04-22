package com.tokopedia.product.viewmodel

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceProduct
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationDetailViewModel
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Test
import java.lang.reflect.Type

/**
 * Created by Yehezkiel on 17/11/20
 */
class RatesEstimationDetailViewModelTest : BaseProductViewModelTest() {

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    private val viewModel by lazy {
        RatesEstimationDetailViewModel(graphqlRepository, "", CoroutineTestDispatchersProvider)
    }

    @Test
    fun `success get rate estimate`() {
        val service = listOf(ServiceModel(
                status = 200,
                products = listOf(ServiceProduct(status = 200))
        ))

        val data = RatesEstimationModel.Response(
                RatesEstimationModel.Data(
                        RatesEstimationModel(
                                rates = RatesModel(
                                        services = service
                                )
                        )
                )
        )

        val expectedResponse = GraphqlResponse(
                mapOf(RatesEstimationModel.Response::class.java to data) as MutableMap<Type, Any>,
                HashMap<Type, List<GraphqlError>>(),
                false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } coAnswers {
            expectedResponse
        }

        viewModel.getCostEstimation(productWeight = 1F, shopId = "shopId", productId = "123456", origin = null)

        coVerify {
            graphqlRepository.getReseponse(any(), any())
        }

        Assert.assertTrue(viewModel.rateEstResp.value is Success)
    }
}