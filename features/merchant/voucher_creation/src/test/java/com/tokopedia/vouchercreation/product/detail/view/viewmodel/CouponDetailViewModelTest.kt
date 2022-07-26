package com.tokopedia.vouchercreation.product.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
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
import org.junit.Assert.assertEquals
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
    fun `When get coupon detail error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        coEvery { getCouponDetailFacadeUseCase.execute(COUPON_ID) } throws error

        //When
        viewModel.getCouponDetail(COUPON_ID)

        //Then
        coVerify { couponDetailObserver.onChanged(Fail(error)) }
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

    @Test
    fun `When get coupon shop and top products data error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val couponUiModel = mockk<CouponUiModel>()

        coEvery { getShopAndTopProductsUseCase.execute(couponUiModel) } throws error

        //When
        viewModel.getShopAndTopProducts(couponUiModel)

        //Then
        coVerify { shopAndTopProductsObserver.onChanged(Fail(error)) }
    }

    @Test
    fun `When set coupon, should set data correctly`() = runBlocking {
        //Given
        val expected = mockk<CouponUiModel>()
        viewModel.setCoupon(expected)

        //When
        val actual = viewModel.getCoupon()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When set max product limit, should set data correctly`() = runBlocking {
        //Given
        val expected = 100
        viewModel.setMaxProductLimit(expected)

        //When
        val actual = viewModel.getMaxProductLimit()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When get coupon setting function is invoked, should call mapper function`() = runBlocking {
        //Given
        val coupon = mockk<CouponUiModel>()

        //When
        viewModel.getCouponSettings(coupon)

        //Then
        coVerify { mapper.map(coupon) }
    }


    @Test
    fun `When coupon status id is 2, should return true`() = runBlocking {
        //Given
        val expected = true
        val ongoingCouponStatusId = 2

        //When
        val actual = viewModel.isOngoingCoupon(ongoingCouponStatusId)

        //Then
        assertEquals(expected, actual)
    }


    @Test
    fun `When coupon status id is not 2, should return as non ongoing coupon`() = runBlocking {
        //Given
        val expected = false
        val endedCouponStatusId = 3

        //When
        val actual = viewModel.isOngoingCoupon(endedCouponStatusId)

        //Then
        assertEquals(expected, actual)
    }

}