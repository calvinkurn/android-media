package com.tokopedia.tkpd.tkpdreputation.createreputation.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tkpd.tkpdreputation.createreputation.Query
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By @ilhamsuaib on 2020-01-03
 */

@ExperimentalCoroutinesApi
class GetProductReputationFormTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    private val getProductReputationForm by lazy {
        GetProductReputationForm(graphqlRepository, Query.GET_REPUTATION_FORM_QUERY)
    }

    private val reputationId = 302801940
    private val productId = 408083893
    private val gson = Gson()
    private val successResponse = "json/get_product_reputation_form_success_response.json"

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get product reputation form`() = runBlocking {
        val requestParams = GetProductReputationForm.createRequestParam(reputationId, productId)

        val json = this.javaClass.classLoader?.getResourceAsStream(successResponse)?.readBytes()?.toString(Charsets.UTF_8)
        val response = gson.fromJson(json, ProductRevGetForm::class.java)
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(ProductRevGetForm::class.java to response),
                mapOf(ProductRevGetForm::class.java to listOf()),
                false
        )

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns gqlResponseSuccess

        getProductReputationForm.getReputationForm(requestParams)

        coVerify {
            graphqlRepository.getReseponse(any(), any())
        }
    }
}