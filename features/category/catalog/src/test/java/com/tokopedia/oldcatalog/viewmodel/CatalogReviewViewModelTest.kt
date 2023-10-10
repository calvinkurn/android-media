package com.tokopedia.oldcatalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.oldcatalog.CatalogTestUtils
import com.tokopedia.oldcatalog.viewmodel.CatalogViewModelTest
import com.tokopedia.oldcatalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.oldcatalog.usecase.detail.CatalogAllReviewUseCase
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.oldcatalog.repository.CatalogAllReviewRepository
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    private val catalogAllReviewRepository : CatalogAllReviewRepository = mockk(relaxed = true)
    private var catalogReviewUseCase = spyk(CatalogAllReviewUseCase(catalogAllReviewRepository))

    private lateinit var viewModel : CatalogAllReviewsViewModel
    private var catalogDetailObserver = mockk<Observer<Result<CatalogProductReviewResponse>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = CatalogAllReviewsViewModel(catalogReviewUseCase)
        viewModel.getCatalogAllReviewsModel().observeForever(catalogDetailObserver)
    }

    @Test
    fun `Get Catalog Review Response Success`() {
        val mockGqlResponse: GraphqlResponse  = createMockGraphqlResponse(getJsonObject("catalog_review_dummy_response.json"))
        runBlocking {
            coEvery { catalogAllReviewRepository.getAllReviews(any(),any(), any()) } returns mockGqlResponse
            viewModel.getAllReviews(CatalogTestUtils.CATALOG_ID,"star","5")
            assert(viewModel.getCatalogShimmerLiveData().value == true)
            assert(viewModel.getCatalogAllReviewsModel().value is Success)
        }
    }

    @Test
    fun `Get Catalog Review Response Exception`() {
        runBlocking {
            coEvery { catalogAllReviewRepository.getAllReviews(any(),any(), any()) } throws Exception()
            viewModel.getAllReviews(CatalogTestUtils.CATALOG_ID,"star","5")
            assert(viewModel.getCatalogAllReviewsModel().value is Fail)
        }
    }

    @Test
    fun `Get Catalog Review Response Fail`() {
        val mockGqlResponse: GraphqlResponse  = CatalogViewModelTest.createMockGraphqlResponse(
            CatalogViewModelTest.getJsonObject("catalog_empty_dummy_response.json"))
        runBlocking {
            coEvery { catalogAllReviewRepository.getAllReviews(any(),any(), any()) } returns mockGqlResponse
            viewModel.getAllReviews(CatalogTestUtils.CATALOG_ID,"star","5")
            assert(viewModel.getCatalogAllReviewsModel().value is Fail)
        }
    }

   companion object {
       fun createMockGraphqlResponse(response : JsonObject): GraphqlResponse {
           val result = HashMap<Type, Any>()
           val errors = HashMap<Type, List<GraphqlError>>()
           val jsonObject: JsonObject = response
           val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
           val objectType = CatalogProductReviewResponse::class.java
           val obj: Any = CommonUtils.fromJson(data, objectType)
           result[objectType] = obj
           return GraphqlResponse(result, errors, false)
       }

       fun getJsonObject(pathString : String) : JsonObject {
           return CommonUtils.fromJson(
               CatalogTestUtils.getJsonFromFile(pathString),
               JsonObject::class.java
           )
       }
   }
}
