package com.tokopedia.attachproduct.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.data.model.mapper.mapToListProduct
import com.tokopedia.attachproduct.domain.model.mapper.toDomainModelMapper
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel
import com.tokopedia.attachproduct.view.viewmodel.AttachProductViewModel
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
    lateinit var productsObserver: Observer<Result<List<NewAttachProductItemUiModel>>>

    @RelaxedMockK
    lateinit var checkedListObserver: Observer<List<NewAttachProductItemUiModel>>

    lateinit var vm: AttachProductViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this)
        vm = AttachProductViewModel(useCase, CoroutineTestDispatchersProvider)
        vm.products.observeForever(productsObserver)
        vm.checkedList.observeForever(checkedListObserver)
    }

    @Test
    fun `success load data and cache data` () {
        //GIVEN
        val aceSearchProductResponse = AceSearchProductResponse()
        val expectedValue = aceSearchProductResponse.mapToListProduct().toDomainModelMapper()
        coEvery {
            useCase(any())
        } returns  aceSearchProductResponse

        //WHEN
        vm.loadProductData("", mockShopId, mockPage, mockWarehouseId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        verifyOrder {
            productsObserver.onChanged(Success(expectedValue))
        }
        assertThat(expectedValue, equalTo(vm.cacheList))
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
    fun `update checked list and previous checked list is empty` () {
        //GIVEN
        val aceSearchProductResponse = AceSearchProductResponse()
        val expectedValue = aceSearchProductResponse.mapToListProduct().toDomainModelMapper()

        //WHEN
        vm.updateCheckedList(expectedValue)

        //THEN
        verify (exactly = 1) {
            checkedListObserver.onChanged(expectedValue)
        }
    }

    @Test
    fun `update checked list and previous checked list is not empty` () {
        //GIVEN
        val aceSearchProductResponseOne = AceSearchProductResponse()
        val aceSearchProductResponseTwo = AceSearchProductResponse()
        val expectedValueOne = aceSearchProductResponseOne.mapToListProduct().toDomainModelMapper()
        val expectedValueTwo = aceSearchProductResponseTwo.mapToListProduct().toDomainModelMapper()

        //WHEN
        vm.updateCheckedList(expectedValueOne)
        vm.updateCheckedList(expectedValueTwo)

        //THEN
        verify (exactly = 2) {
            checkedListObserver.onChanged(expectedValueTwo)
        }
    }

    @Test
    fun `clear cache after filling up the cache list` () {
        //GIVEN
        val aceSearchProductResponse = AceSearchProductResponse()
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
    fun `complete selection after check products` () {
        //GIVEN
        val resultProduct = arrayListOf<ResultProduct>()
        val mockLambda: (ArrayList<ResultProduct>) -> Unit = { data ->
            resultProduct.addAll(data)
        }
        val aceSearchProductResponse = AceSearchProductResponse()
        val expectedValue = aceSearchProductResponse.mapToListProduct().toDomainModelMapper()

        //WHEN
        vm.updateCheckedList(expectedValue)
        vm.completeSelection(mockLambda)

        //THEN
        assertThat(expectedValue.size, equalTo(resultProduct.size))
    }
}