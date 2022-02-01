package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.catalog.CatalogTestUtils
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.repository.CatalogAllReviewRepository
import com.tokopedia.catalog.usecase.detail.CatalogAllReviewUseCase
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class CatalogReviewViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val catalogAllReviewRepository : CatalogAllReviewRepository = mockk(relaxed = true)
    private var catalogReviewUseCase = spyk(CatalogAllReviewUseCase(catalogAllReviewRepository))

    private lateinit var viewModel : CatalogAllReviewsViewModel
    private var catalogDetailObserver = mockk<Observer<Result<CatalogProductReviewResponse>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogAllReviewsViewModel(catalogReviewUseCase)
        viewModel.getCatalogAllReviewsModel().observeForever(catalogDetailObserver)
    }

//    @Test
//    fun `Get Catalog Review Response Success`() {
//        val catalogReviewData: GraphqlResponse = mockk(relaxed = true)
//        runBlocking {
//            coEvery { catalogAllReviewRepository.getAllReviews(any(),any(), any()) } returns catalogReviewData
//            catalogReviewUseCase.getCatalogReviews(CatalogTestUtils.CATALOG_ID,"star","5",viewModel.getCatalogAllReviewsModel())
//            if(viewModel.getCatalogAllReviewsModel().value is Success){
//                assert(true)
//            }else {
//                assert(false)
//            }
//
//        }
//    }

    @Test
    fun `Get Catalog Review Response Fail`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_empty_dummy_response.json"))
        runBlocking {
            coEvery { catalogAllReviewRepository.getAllReviews(any(),any(), any()) } returns mockGqlResponse
            catalogReviewUseCase.getCatalogReviews(CatalogTestUtils.CATALOG_ID,"star","5",viewModel.getCatalogAllReviewsModel())
            if(viewModel.getCatalogAllReviewsModel().value is Fail){
                assert(true)
            }else {
                assert(false)
            }

        }
    }

    private fun createMockGraphqlResponse(response : JsonObject): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = response
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = CatalogResponseData::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun getJsonObject(pathString : String) : JsonObject {
        return CommonUtils.fromJson(
                CatalogTestUtils.getJsonFromFile(pathString),
                JsonObject::class.java
        )
    }
}