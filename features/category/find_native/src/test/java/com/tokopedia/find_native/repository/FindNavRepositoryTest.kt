package com.tokopedia.find_native.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.find_native.data.model.RelatedLinkResponse
import com.tokopedia.find_native.data.repository.FindNavRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FindNavRepositoryTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var testDispatcher: TestCoroutineDispatcher

    private lateinit var findNavRepo: FindNavRepository

    private lateinit var reqParams: Map<String, String>

    @Before
    @Throws(Exception::class)
    fun setUp() {
        testDispatcher = TestCoroutineDispatcher()
        findNavRepo = spyk(FindNavRepository())
        reqParams = mockk()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `check getProductList() invocation once`() {
        val productListResponse: ProductListResponse = mockk()
        testDispatcher.runBlockingTest {
            coEvery { findNavRepo.productListQuery } returns ""
            coEvery { findNavRepo.getProductList(reqParams) } returns productListResponse
            findNavRepo.getProductList(reqParams)
            coVerify { findNavRepo.getProductList(reqParams) }
        }
    }

    @Test
    fun `check getQuickFilterList() invocation once`() {
        val quickFilterResponse: MutableList<Filter> = mockk()
        testDispatcher.runBlockingTest {
            coEvery { findNavRepo.quickFilterListQuery } returns ""
            coEvery { findNavRepo.getQuickFilterList(reqParams) } returns quickFilterResponse
            findNavRepo.getQuickFilterList(reqParams)
            coVerify { findNavRepo.getQuickFilterList(reqParams) }
        }
    }

    @Test
    fun `check getDynamicFilterList() invocation once`() {
        val dynamicFilterResponse: DynamicFilterModel = mockk()
        testDispatcher.runBlockingTest {
            coEvery { findNavRepo.dynamicFilterListQuery } returns ""
            coEvery { findNavRepo.getDynamicFilterList(reqParams) } returns dynamicFilterResponse
            findNavRepo.getDynamicFilterList(reqParams)
            coVerify { findNavRepo.getDynamicFilterList(reqParams) }
        }
    }

    @Test
    fun `check getRelatedLinkList() invocation once`() {
        val relatedLinkResponse: RelatedLinkResponse = mockk()
        testDispatcher.runBlockingTest {
            coEvery { findNavRepo.relatedLinkListQuery } returns ""
            coEvery { findNavRepo.getRelatedLinkList(reqParams) } returns relatedLinkResponse
            findNavRepo.getRelatedLinkList(reqParams)
            coVerify { findNavRepo.getRelatedLinkList(reqParams) }
        }
    }

}