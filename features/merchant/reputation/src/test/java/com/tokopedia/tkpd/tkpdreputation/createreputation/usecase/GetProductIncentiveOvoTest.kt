package com.tokopedia.tkpd.tkpdreputation.createreputation.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.review.feature.createreputation.Query
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevIncentiveOvo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class GetProductIncentiveOvoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    private val getProductIncentiveOvo by lazy {
        GetProductIncentiveOvo(graphqlRepository, Query.GET_PRODUCT_INCENTIVE_OVO)
    }

    private val gson = Gson()
    private val successResponse = "json/get_product_incentive_ovo.json"

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get product incentive ovo`() = runBlocking {
        val json = this.javaClass.classLoader?.getResourceAsStream(successResponse)?.readBytes()?.toString(Charsets.UTF_8)
        val response = gson.fromJson(json, ProductRevIncentiveOvo::class.java)

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = ProductRevIncentiveOvo::class.java
        result[objectType] = response
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns gqlResponseSuccess

        getProductIncentiveOvo.getIncentiveOvo()

        coVerify {
            graphqlRepository.getReseponse(any(), any())
        }
    }
}