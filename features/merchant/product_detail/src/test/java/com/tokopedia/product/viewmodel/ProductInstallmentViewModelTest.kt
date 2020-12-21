package com.tokopedia.product.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.model.installment.InstallmentResponse
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.detail.view.viewmodel.ProductInstallmentViewModel
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Test
import java.lang.reflect.Type


/**
 * Created by Yehezkiel on 16/11/20
 */
class ProductInstallmentViewModelTest : BaseProductViewModelTest() {

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    private val viewModel by lazy {
        ProductInstallmentViewModel(graphqlRepository, mapOf(RawQueryKeyConstant.QUERY_INSTALLMENT to ""), CoroutineTestDispatchersProvider)
    }

    @Test
    fun `on success get fullfillment`() {
        val data = InstallmentResponse(result = InstallmentResponse.Result(response = "200", bank = listOf(InstallmentBank(
                installmentList = listOf(InstallmentBank.Installment())
        ))))
        val expectedResponse = GraphqlResponse(
                mapOf(InstallmentResponse::class.java to data) as MutableMap<Type, Any>,
                HashMap<Type, List<GraphqlError>>(),
                false)


        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } coAnswers {
            expectedResponse
        }

        viewModel.transformedInstallment.observeForever { }
        viewModel.loadInstallment(1F)

        viewModel.transformedInstallment.verifySuccessEquals(expectedResponse.getSuccessData<InstallmentResponse>().asSuccess())
    }

    private fun LiveData<*>.verifySuccessEquals(expected: Success<*>) {
        val expectedResult = (expected.data as InstallmentResponse).result.bank.flatMap {
            it.installmentList.map { installment ->
                it.copy(
                        installmentList = listOf(installment))
            }
        }.groupBy { it.installmentList.first().term }

        val actualResult = (value as Success<*>).data
        Assert.assertEquals(expectedResult, actualResult)
    }
}