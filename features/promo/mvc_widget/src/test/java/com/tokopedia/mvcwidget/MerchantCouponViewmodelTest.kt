package com.tokopedia.mvcwidget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvcwidget.multishopmvc.data.CatalogMVCWithProductsListItem
import com.tokopedia.mvcwidget.multishopmvc.data.MerchantCouponResponse
import com.tokopedia.mvcwidget.multishopmvc.data.ProductCategoriesFilterItem
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponData
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponUsecase
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponViewModel
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

class MerchantCouponViewmodelTest {

    lateinit var viewModel: MerchantCouponViewModel
    val merchantCouponUsecase: MerchantCouponUsecase= mockk()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = MerchantCouponViewModel(merchantCouponUsecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getMerchantCouponData for success data`() {
        runBlockingTest {
            val couponObserver = mockk<Observer<LiveDataResult<MerchantCouponData>>>() {
                every { onChanged(any()) } just Runs
            }
            val dataCatalog = mockk<List<CatalogMVCWithProductsListItem>>()
            val dataFilter = mockk<List<ProductCategoriesFilterItem?>>()
            val merchantCouponResponseData = mockk<MerchantCouponResponse> {
                every { productlist } returns mockk {
                    every { productCategoriesFilter } returns dataFilter
                    every { tokopointsPaging } returns mockk {
                        every { hasNext } returns true
                    }
                    every { catalogMVCWithProductsList } returns dataCatalog
                }
            }
            val merchantCouponData = MerchantCouponData(merchantCouponResponseData)
            coEvery { merchantCouponUsecase.getResponse(any()) } returns merchantCouponResponseData

            viewModel.couponData.observeForever(couponObserver)
            viewModel.merchantCouponData(1)
            verify(ordering = Ordering.ORDERED) {
                viewModel.couponData.postValue(LiveDataResult.loading())
                viewModel.couponData.postValue(LiveDataResult.success(merchantCouponData))
            }
            val result = viewModel.couponData.value
            assert(result?.data?.merchantCouponResponse == merchantCouponResponseData)
        }
    }

    @Test
    fun `getMerchantCouponData for error data`() {
        val couponObserver = mockk<Observer<LiveDataResult<MerchantCouponData>>>() {
            every { onChanged(any()) } just Runs
        }
        coEvery { merchantCouponUsecase.getResponse(any()) } returns MerchantCouponResponse()
        viewModel.couponData.observeForever(couponObserver)
        viewModel.merchantCouponData(0)

        verify(ordering = Ordering.ORDERED) {
            viewModel.couponData.postValue(LiveDataResult.error(Throwable()))
        }
    }
}