package com.tokopedia.find_native.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.common_category.model.productModel.SearchProduct
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.find_native.data.model.CategoryTkpdFindRelated
import com.tokopedia.find_native.data.model.RelatedLinkData
import com.tokopedia.find_native.data.model.RelatedLinkResponse
import com.tokopedia.find_native.data.repository.FindNavRepository
import com.tokopedia.find_native.util.FindNavParamBuilder
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class FindNavViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var testDispatcher: TestCoroutineDispatcher

    private lateinit var findNavRepository: FindNavRepository

    private lateinit var findNavParamBuilder: FindNavParamBuilder

    private lateinit var findNavViewModel: FindNavViewModel

    private lateinit var requestParams: RequestParams

    @Before
    @Throws(Exception::class)
    fun setUp() {
        testDispatcher = TestCoroutineDispatcher()

        findNavRepository = mockk(relaxed = true)

        findNavParamBuilder = mockk(relaxed = true)

        findNavViewModel = spyk(FindNavViewModel(findNavRepository, findNavParamBuilder))

        requestParams = mockk(relaxed = true)

        Dispatchers.setMain(testDispatcher)

        every { requestParams.paramsAllValueInString } returns HashMap()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `test generateProductFilterParams() & getProductList() invocation once`() {
        every { findNavParamBuilder.generateProductFilterParams(any(), any(), any(), any(), any(), any()) } returns requestParams
        coEvery { findNavRepository.getProductList(requestParams.paramsAllValueInString) } returns ProductListResponse()
        findNavViewModel.fetchProductList(anyInt(), anyString(), anyInt(), anyString(), any()
                ?: HashMap(), any() ?: HashMap())
        verify { findNavParamBuilder.generateProductFilterParams(any(), any(), any(), any(), any(), any()) }
        coVerify(exactly = 1) { findNavRepository.getProductList(requestParams.paramsAllValueInString) }
    }

    @Test
    fun `test fetchProductList() response when product is banned`() {
        val dummyProductListResponse = ProductListResponse(searchProduct = SearchProduct(errorMessage = "Produk yang kamu cari tidak tersedia di Android."))
        every { findNavParamBuilder.generateProductFilterParams(any(), any(), any(), any(), any(), any()) } returns requestParams
        coEvery { findNavRepository.getProductList(requestParams.paramsAllValueInString) } returns dummyProductListResponse
        findNavViewModel.fetchProductList(anyInt(), anyString(), anyInt(), anyString(), any()
                ?: HashMap(), any() ?: HashMap())
        assertEquals((findNavViewModel.getBannedLiveData().value as Success).data[0], dummyProductListResponse.searchProduct?.errorMessage)
    }

    @Test
    fun `test fetchProductList() response when productList is adult`() {
        val dummyProductListResponse = ProductListResponse(searchProduct = SearchProduct(isQuerySafe = false))
        every { findNavParamBuilder.generateProductFilterParams(any(), any(), any(), any(), any(), any()) } returns requestParams
        coEvery { findNavRepository.getProductList(requestParams.paramsAllValueInString) } returns dummyProductListResponse
        findNavViewModel.fetchProductList(anyInt(), anyString(), anyInt(), anyString(), any()
                ?: HashMap(), any() ?: HashMap())
        assertEquals(findNavViewModel.checkForAdultData(), dummyProductListResponse.searchProduct?.isQuerySafe)
    }

    @Test
    fun `test fetchProductList() response when productList is empty`() {
        val dummyProductListResponse = ProductListResponse(searchProduct = SearchProduct(countText = "0", totalData = 0))
        every { findNavParamBuilder.generateProductFilterParams(any(), any(), any(), any(), any(), any()) } returns requestParams
        coEvery { findNavRepository.getProductList(requestParams.paramsAllValueInString) } returns dummyProductListResponse
        findNavViewModel.fetchProductList(anyInt(), anyString(), anyInt(), anyString(), any()
                ?: HashMap(), any() ?: HashMap())
        assertEquals(findNavViewModel.getProductCountLiveData().value?.get(0), dummyProductListResponse.searchProduct?.countText)
        assertEquals(findNavViewModel.getProductCountLiveData().value?.get(1), dummyProductListResponse.searchProduct?.totalData.toString())
    }

    @Test
    fun `test fetchProductList() exception`() {
        val dummyException = Exception("dummyException")
        every { findNavParamBuilder.generateProductFilterParams(any(), any(), any(), any(), any(), any()) } returns requestParams
        coEvery { findNavRepository.getProductList(requestParams.paramsAllValueInString) } throws dummyException
        findNavViewModel.fetchProductList(anyInt(), anyString(), anyInt(), anyString(), any()
                ?: HashMap(), any() ?: HashMap())
        assertEquals(findNavViewModel.getProductListLiveData().value, Fail(dummyException))
    }

    @Test
    fun `test fetchQuickFilterList() invocation once`() {
        every { findNavParamBuilder.generateQuickFilterParams(any()) } returns requestParams
        coEvery { findNavRepository.getQuickFilterList(requestParams.paramsAllValueInString) } returns any()
        findNavViewModel.fetchQuickFilterList(anyString())
        verify { findNavParamBuilder.generateQuickFilterParams(any()) }
        coVerify(exactly = 1) { findNavRepository.getQuickFilterList(requestParams.paramsAllValueInString) }
    }

    @Test
    fun `test fetchQuickFilterList() exception`() {
        val dummyException = Exception("dummy Exception")
        every { findNavParamBuilder.generateQuickFilterParams(any()) } returns requestParams
        coEvery { findNavRepository.getQuickFilterList(requestParams.paramsAllValueInString) } throws dummyException
        findNavViewModel.fetchQuickFilterList(anyString())
        assertEquals(findNavViewModel.getQuickFilterListListLiveData().value, Fail(dummyException))
    }

    @Test
    fun `test fetchQuickFilterList() Success`() {
        val dummyFilter1 = Filter()
        dummyFilter1.title = "1"
        val dummyFilter2 = Filter()
        dummyFilter1.title = "2"
        val dummyQuickFilterListResponse = listOf(dummyFilter1, dummyFilter2)
        every { findNavParamBuilder.generateQuickFilterParams(any()) } returns requestParams
        coEvery { findNavRepository.getQuickFilterList(requestParams.paramsAllValueInString) } returns dummyQuickFilterListResponse
        findNavViewModel.fetchQuickFilterList(anyString())
        assertEquals(findNavViewModel.getQuickFilterListListLiveData().value, Success(dummyQuickFilterListResponse))
    }

    @Test
    fun `test fetchDynamicFilterList() invocation once`() {
        every { findNavParamBuilder.generateDynamicFilterParams(any()) } returns requestParams
        coEvery { findNavRepository.getDynamicFilterList(requestParams.paramsAllValueInString) } returns any()
        findNavViewModel.fetchDynamicFilterList(anyString())
        verify { findNavParamBuilder.generateDynamicFilterParams(any()) }
        coVerify(exactly = 1) { findNavRepository.getDynamicFilterList(requestParams.paramsAllValueInString) }
    }

    @Test
    fun `test fetchDynamicFilterList() exception`() {
        val dummyException = Exception("dummy Exception")
        every { findNavParamBuilder.generateDynamicFilterParams(any()) } returns requestParams
        coEvery { findNavRepository.getDynamicFilterList(requestParams.paramsAllValueInString) } throws dummyException
        findNavViewModel.fetchDynamicFilterList(anyString())
        assertEquals(findNavViewModel.getDynamicFilterListLiveData().value, Fail(dummyException))
    }

    @Test
    fun `test fetchDynamicFilterList() Success`() {
        val dummyDynamicFilterListResponse = DynamicFilterModel()
        every { findNavParamBuilder.generateDynamicFilterParams(any()) } returns requestParams
        coEvery { findNavRepository.getDynamicFilterList(requestParams.paramsAllValueInString) } returns dummyDynamicFilterListResponse
        findNavViewModel.fetchDynamicFilterList(anyString())
        assertEquals(findNavViewModel.getDynamicFilterListLiveData().value, Success(dummyDynamicFilterListResponse))
    }

    @Test
    fun `test fetchRelatedLinkList() invocation once`() {
        every { findNavParamBuilder.generateRelatedLinkParams(any()) } returns requestParams
        coEvery { findNavRepository.getRelatedLinkList(requestParams.paramsAllValueInString) } returns any()
        findNavViewModel.fetchRelatedLinkList(anyString())
        verify { findNavParamBuilder.generateRelatedLinkParams(any()) }
        coVerify(exactly = 1) { findNavRepository.getRelatedLinkList(requestParams.paramsAllValueInString) }
    }

    @Test
    fun `test fetchRelatedLinkList() exception`() {
        val dummyException = Exception("dummy Exception")
        every { findNavParamBuilder.generateRelatedLinkParams(any()) } returns requestParams
        coEvery { findNavRepository.getRelatedLinkList(requestParams.paramsAllValueInString) } throws dummyException
        findNavViewModel.fetchRelatedLinkList(anyString())
        assertEquals(findNavViewModel.getRelatedLinkListLiveData().value, Fail(dummyException))
    }

    @Test
    fun `test fetchRelatedLinkList() Success`() {
        val dummyHotlist = listOf(RelatedLinkData(id = 0, url = "test1", text = "test1"))
        val dummyCategory = listOf(RelatedLinkData(id = 1, url = "test2", text = "test2"))
        val dummyRelatedLinkListResponse = RelatedLinkResponse(categoryTkpdFindRelated = CategoryTkpdFindRelated(dummyHotlist, dummyCategory))
        val liveDataResponse = ArrayList<RelatedLinkData>()
        liveDataResponse.addAll(dummyCategory)
        liveDataResponse.addAll(dummyHotlist)
        every { findNavParamBuilder.generateRelatedLinkParams(any()) } returns requestParams
        coEvery { findNavRepository.getRelatedLinkList(requestParams.paramsAllValueInString) } returns dummyRelatedLinkListResponse
        findNavViewModel.fetchRelatedLinkList(anyString())
        assertEquals(findNavViewModel.getRelatedLinkListLiveData().value, Success(liveDataResponse))
    }
}