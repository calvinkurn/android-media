package com.tokopedia.vouchercreation.product.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.mapper.CouponMapper
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailFacadeUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.ShopWithTopProducts
import com.tokopedia.vouchercreation.product.share.domain.usecase.GetShopAndTopProductsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CouponDetailViewModelTest {

    companion object {
        private const val COUPON_ID: Long = 212
    }

    @RelaxedMockK
    lateinit var getCouponDetailFacadeUseCase: GetCouponDetailFacadeUseCase

    @RelaxedMockK
    lateinit var getShopAndTopProductsUseCase: GetShopAndTopProductsUseCase

    @RelaxedMockK
    lateinit var mapper: CouponMapper

    @RelaxedMockK
    lateinit var couponDetailObserver: Observer<in Result<CouponDetailWithMetadata>>

    @RelaxedMockK
    lateinit var shopAndTopProductsObserver: Observer<in Result<ShopWithTopProducts>>

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        CouponDetailViewModel(
            CoroutineTestDispatchersProvider,
            getCouponDetailFacadeUseCase,
            getShopAndTopProductsUseCase,
            mapper
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.couponDetail.observeForever(couponDetailObserver)
        viewModel.shopWithTopProducts.observeForever(shopAndTopProductsObserver)
    }

    @Test
    fun `When get coupon detail success, should emit success to observer`() = runBlocking {
        //Given
        val couponMetadata = mockk<CouponDetailWithMetadata>()

        coEvery { getCouponDetailFacadeUseCase.execute(COUPON_ID) } returns couponMetadata

        //When
        viewModel.getCouponDetail(COUPON_ID)

        //Then
        coVerify { couponDetailObserver.onChanged(Success(couponMetadata)) }
    }



    @Test
    fun `When get coupon shop and top products data success, should emit success to observer`() = runBlocking {
        //Given
        val shopMetadata = mockk<ShopWithTopProducts>()
        val couponUiModel = mockk<CouponUiModel>()

        coEvery { getShopAndTopProductsUseCase.execute(couponUiModel) } returns shopMetadata

        //When
        viewModel.getShopAndTopProducts(couponUiModel)

        //Then
        coVerify { shopAndTopProductsObserver.onChanged(Success(shopMetadata)) }
    }

}