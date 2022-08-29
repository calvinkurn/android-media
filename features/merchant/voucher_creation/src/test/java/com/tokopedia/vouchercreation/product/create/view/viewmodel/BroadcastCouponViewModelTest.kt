package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.ShopWithTopProducts
import com.tokopedia.vouchercreation.product.share.domain.usecase.GetShopAndTopProductsUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.remote.ChatBlastSellerMetadata
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BroadcastCouponViewModelTest {
    companion object {
        private const val COUPON_ID: Long = 212
    }

    @RelaxedMockK
    lateinit var getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase

    @RelaxedMockK
    lateinit var getShopAndTopProductsUseCase: GetShopAndTopProductsUseCase

    @RelaxedMockK
    lateinit var  getCouponDetailUseCase: GetCouponDetailUseCase

    @RelaxedMockK
    lateinit var couponDetailObserver: Observer<in Result<CouponUiModel>>

    @RelaxedMockK
    lateinit var shopAndTopProductsObserver: Observer<in Result<ShopWithTopProducts>>

    @RelaxedMockK
    lateinit var broadcastMetadataObserver: Observer<in Result<ChatBlastSellerMetadata>>


    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        BroadcastCouponViewModel(
            CoroutineTestDispatchersProvider,
            getBroadCastMetaDataUseCase,
            getShopAndTopProductsUseCase,
            getCouponDetailUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.couponDetail.observeForever(couponDetailObserver)
        viewModel.shopWithTopProducts.observeForever(shopAndTopProductsObserver)
        viewModel.broadcastMetadata.observeForever(broadcastMetadataObserver)
    }

    @After
    fun tearDown() {
        viewModel.couponDetail.removeObserver(couponDetailObserver)
        viewModel.shopWithTopProducts.removeObserver(shopAndTopProductsObserver)
        viewModel.broadcastMetadata.removeObserver(broadcastMetadataObserver)
    }


    @Test
    fun `When get broadcast meta data, observer should receive success response`() = runBlocking {
        //Given
        val response = ChatBlastSellerMetadata()


        coEvery { getBroadCastMetaDataUseCase.executeOnBackground() } returns response

        //When
        viewModel.getBroadcastMetaData()

        //Then
        coVerify { broadcastMetadataObserver.onChanged(Success(response)) }
    }

    @Test
    fun `When get broadcast meta data, observer should receive error response`() = runBlocking {
        //Given
        val error = MessageErrorException()

        coEvery { getBroadCastMetaDataUseCase.executeOnBackground() } throws error

        //When
        viewModel.getBroadcastMetaData()

        //Then
        coVerify { broadcastMetadataObserver.onChanged(Fail(error)) }
    }

    @Test
    fun `When get coupon shop and top products data success, observer should receive success response`() = runBlocking {
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
    fun `When get coupon shop and top products data error, observer should receive error response`() = runBlocking {
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
    fun `When get coupon detail success, observer should receive success response`() = runBlocking {
        //Given
        val coupon = mockk<CouponUiModel>()

        coEvery { getCouponDetailUseCase.executeOnBackground()} returns coupon

        //When
        viewModel.getCouponDetail(COUPON_ID)

        //Then
        coVerify { couponDetailObserver.onChanged(Success(coupon)) }
    }

    @Test
    fun `When get coupon detail error, observer should receive error response`() = runBlocking {
        //Given
        val error = MessageErrorException()
        coEvery { getCouponDetailUseCase.executeOnBackground() } throws error

        //When
        viewModel.getCouponDetail(COUPON_ID)

        //Then
        coVerify { couponDetailObserver.onChanged(Fail(error)) }
    }

    @Test
    fun `When get coupon, should return correct data`() {
        //Given
        val coupon = mockk<CouponUiModel>()

        //When
        viewModel.setCoupon(coupon)

        //Then
        val actual = viewModel.getCoupon()
        assertEquals(coupon, actual)
    }

}