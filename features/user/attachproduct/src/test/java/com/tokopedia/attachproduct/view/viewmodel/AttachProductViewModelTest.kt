package com.tokopedia.attachproduct.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.attachproduct.FileUtil
import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.data.model.mapper.mapToListProduct
import com.tokopedia.attachproduct.domain.model.mapper.toDomainModelMapper
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AttachProductViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockShopId = ""
    private val mockWarehouseId = ""
    private val mockPage = 1

    @RelaxedMockK
    lateinit var useCase: AttachProductUseCase

    @RelaxedMockK
    lateinit var productsObserver: Observer<Result<List<AttachProductItemUiModel>>>

    @RelaxedMockK
    lateinit var checkedListObserver: Observer<List<AttachProductItemUiModel>>

    lateinit var vm: AttachProductViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this)
        vm = AttachProductViewModel(useCase, CoroutineTestDispatchersProvider)
        vm.products.observeForever(productsObserver)
        vm.checkedList.observeForever(checkedListObserver)
    }

    @Test
    fun `success load data and cache data`() {
        //GIVEN
        val aceSearchProductResponse: AceSearchProductResponse =
                FileUtil.parse("/success_ace_search_product.json", AceSearchProductResponse::class.java)
        val expectedValue = aceSearchProductResponse.mapToListProduct().toDomainModelMapper()
        val expectedCacheValue = expectedValue.subList(0, expectedValue.size - 1)

        coEvery {
            useCase(any())
        } returns aceSearchProductResponse

        //WHEN
        vm.loadProductData("", mockShopId, mockPage, mockWarehouseId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        verifyOrder {
            productsObserver.onChanged(Success(expectedValue))
            assertThat(expectedCacheValue, equalTo(vm.cacheList))
            assertThat(vm.cacheHasNext, equalTo(true))
        }
    }

    @Test
    fun `success load data and doesn't cache data` () {
        //GIVEN
        val aceSearchProductResponse = AceSearchProductResponse()
        val expectedValue = aceSearchProductResponse.mapToListProduct().toDomainModelMapper()
        val query = "query is not empty"
        coEvery {
            useCase(any())
        } returns  aceSearchProductResponse

        //WHEN
        vm.loadProductData(query, mockShopId, mockPage, mockWarehouseId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        verifyOrder {
            productsObserver.onChanged(Success(expectedValue))
            assertThat(expectedValue.size, equalTo(0))
            assertThat(vm.cacheHasNext, equalTo(false))
        }
    }

    @Test
    fun `fail load data` () {
        //GIVEN

        val expectedError = Throwable("")
        coEvery {
            useCase(any())
        } throws expectedError

        //WHEN
        vm.loadProductData("", mockShopId, mockPage, mockWarehouseId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        verify {
            productsObserver.onChanged(Fail(expectedError))
        }
    }

    @Test
    fun `update checked list`() {
        //GIVEN
        val aceSearchProductResponse: AceSearchProductResponse =
                FileUtil.parse("/success_ace_search_product.json", AceSearchProductResponse::class.java)
        val expectedValue = aceSearchProductResponse.mapToListProduct().toDomainModelMapper()

        //WHEN
        vm.updateCheckedList(expectedValue)

        //THEN
        verify(exactly = 1) {
            checkedListObserver.onChanged(expectedValue)
        }
    }

    @Test
    fun `clear cache after filling up the cache list` () {
        //GIVEN
        val aceSearchProductResponse: AceSearchProductResponse =
                FileUtil.parse("/success_ace_search_product.json", AceSearchProductResponse::class.java)
        val expectedValue = aceSearchProductResponse.mapToListProduct().toDomainModelMapper()

        coEvery {
            useCase(any())
        } returns  aceSearchProductResponse

        //WHEN
        vm.loadProductData("", mockShopId, mockPage, mockWarehouseId)
        vm.clearCache()

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        verifyOrder {
            productsObserver.onChanged(Success(expectedValue))
            assertThat(vm.cacheList.size, equalTo(0))
        }
    }

    @Test
    fun `success load data and has next`() {
        //GIVEN
        val aceSearchProductResponse: AceSearchProductResponse =
                FileUtil.parse("/success_ace_search_product.json", AceSearchProductResponse::class.java)

        coEvery {
            useCase(any())
        } returns aceSearchProductResponse

        //WHEN
        vm.loadProductData("", mockShopId, mockPage, mockWarehouseId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        assertThat(vm.cacheHasNext, equalTo(true))
    }

    @Test
    fun `success load data and hasn't next`() {
        //GIVEN
        val aceSearchProductResponse: AceSearchProductResponse =
            FileUtil.parse("/success_ace_search_product.json", AceSearchProductResponse::class.java)

        val responseWithThreeData = aceSearchProductResponse.apply {
            this.aceSearchProductResponse.data.products = this.aceSearchProductResponse.data.products.subList(0, 3)
        }
        coEvery {
            useCase(any())
        } returns responseWithThreeData

        //WHEN
        vm.loadProductData("", mockShopId, mockPage, mockWarehouseId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        assertThat(vm.cacheHasNext, equalTo(false))
    }
}