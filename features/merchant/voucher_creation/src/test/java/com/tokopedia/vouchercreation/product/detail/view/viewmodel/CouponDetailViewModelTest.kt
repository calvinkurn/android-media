package com.tokopedia.vouchercreation.product.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.map
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.mapper.CouponMapper
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.ShopWithTopProducts
import com.tokopedia.vouchercreation.product.share.domain.usecase.GetShopAndTopProductsUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
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
        val couponMetadata = mockk<CouponDetailWithMetadata>()

        coEvery { getCouponDetailFacadeUseCase.execute(COUPON_ID) } returns couponMetadata


        viewModel.getCouponDetail(COUPON_ID)

        coVerify { couponDetailObserver.onChanged(Success(couponMetadata)) }

    }



    @Test
    fun `When get coupon detail success1, should emit success to observer`() = runBlocking {
        val shopMetadata = mockk<ShopWithTopProducts>()
        val fakeCouponUiModel = CouponUiModel(
            1,
            "",
            VoucherTypeConst.CASHBACK,
            "",
            "",
            "",
            "",
            1,
            "",
            0,
            "",
            0,
            0,
            0,
            0,
            0,
            "",
            "",
            "",
            "",
            "",
            false,
            "",
            listOf(),
            emptyList(),
            0
        )

        coEvery { getShopAndTopProductsUseCase.execute(fakeCouponUiModel) } returns shopMetadata


        viewModel.getShopAndTopProducts(fakeCouponUiModel)

        coVerify { shopAndTopProductsObserver.onChanged(Success(shopMetadata)) }

    }
}