package com.tokopedia.tokopoints.view.merchantcoupon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.view.model.merchantcoupon.CatalogMVCWithProductsListItem
import com.tokopedia.tokopoints.view.model.merchantcoupon.MerchantCouponResponse
import com.tokopedia.tokopoints.view.model.merchantcoupon.ProductCategoriesFilterItem
import com.tokopedia.tokopoints.view.util.*
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
import kotlin.reflect.KClass

class MerchantCouponViewmodelTest {

    lateinit var viewModel: MerchantCouponViewModel
    var gqlRepository = mockk<GraphqlRepository>()
    val repository = spyk(MerchantCouponUsecase(gqlRepository))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = MerchantCouponViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getMerchantCouponData for success data`() {
        runBlockingTest {
            val couponObserver = mockk<Observer<Resources<MerchantCouponData>>>() {
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
            val data = mockk<GraphqlResponse>(relaxed = true)
            coEvery { gqlRepository.getReseponse(any()) } returns data
            coEvery { repository.executeOnBackground() } returns mockk {
                every { getData<MerchantCouponResponse>(MerchantCouponResponse::class.java) } returns merchantCouponResponseData
                every { getError(MerchantCouponResponse::class.java) } returns null
            }

            viewModel.couponData.observeForever(couponObserver)
            viewModel.setCategoryRootId("1")
            viewModel.merchantCouponData(1)
            verify(ordering = Ordering.ORDERED) {
                couponObserver.onChanged(ofType(Loading::class as KClass<Loading<MerchantCouponData>>))
                couponObserver.onChanged(ofType(Success::class as KClass<Success<MerchantCouponData>>))
            }
            val result = viewModel.couponData.value as Success
            assert(result.data.merchantCouponResponse == merchantCouponResponseData)
        }
    }

    @Test
    fun `getMerchantCouponData for error data`() {
        val couponObserver = mockk<Observer<Resources<MerchantCouponData>>>() {
            every { onChanged(any()) } just Runs
        }
        val data = mockk<GraphqlResponse>(relaxed = true)
        coEvery { gqlRepository.getReseponse(any()) } returns data
        coEvery { repository.executeOnBackground() } returns mockk {
            every { getData<MerchantCouponResponse>(MerchantCouponResponse::class.java) } returns null
            every { getError(MerchantCouponResponse::class.java) } returns null
        }
        viewModel.couponData.observeForever(couponObserver)
        viewModel.merchantCouponData(0)

        verify(ordering = Ordering.ORDERED) {
            couponObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<MerchantCouponData>>))
        }
    }
}