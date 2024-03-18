package com.tokopedia.mvcwidget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvcwidget.multishopmvc.data.CatalogMVCWithProductsListItem
import com.tokopedia.mvcwidget.multishopmvc.data.MerchantCouponResponse
import com.tokopedia.mvcwidget.multishopmvc.data.ProductCategoriesFilterItem
import com.tokopedia.mvcwidget.multishopmvc.data.Productlist
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponData
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponUsecase
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantCouponViewmodelTest {

    lateinit var viewModel: MerchantCouponViewModel
    val merchantCouponUsecase: MerchantCouponUsecase = mockk(relaxed = true)

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
        runTest {
            val dummyData = MerchantCouponResponse(Productlist())
            val couponObserver = mockk<Observer<LiveDataResult<MerchantCouponData>>>(relaxed = true)
            coEvery { merchantCouponUsecase.getResponse(any()) } returns MerchantCouponResponse(
                Productlist()
            )
            viewModel.couponData.observeForever(couponObserver)

            viewModel.merchantCouponData(1)
            verify(ordering = Ordering.ORDERED) {
                couponObserver.onChanged(
                    match {
                        it.status == LiveDataResult.STATUS.SUCCESS
                            && it.data?.merchantCouponResponse == dummyData
                    })
            }
        }
    }

    @Test
    fun `getMerchantCouponData for error data`() {
        val couponObserver = mockk<Observer<LiveDataResult<MerchantCouponData>>>() {
            every { onChanged(any()) } just Runs
        }
        coEvery { merchantCouponUsecase.getResponse(any()) } throws Exception()
        viewModel.couponData.observeForever(couponObserver)
        viewModel.merchantCouponData(0)

        verify(ordering = Ordering.ORDERED) {
            couponObserver.onChanged(match { it.status == LiveDataResult.STATUS.ERROR })
        }
    }
}
