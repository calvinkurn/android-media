package com.tokopedia.shop.campaign.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherListUseCase
import com.tokopedia.shop.campaign.domain.usecase.RedeemPromoVoucherUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExclusiveLaunchVoucherListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getPromoVoucherListUseCase: GetPromoVoucherListUseCase

    @RelaxedMockK
    lateinit var redeemPromoVoucherUseCase: RedeemPromoVoucherUseCase

    private lateinit var viewModel : ExclusiveLaunchVoucherListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ExclusiveLaunchVoucherListViewModel(
            CoroutineTestDispatchersProvider,
            getPromoVoucherListUseCase,
            redeemPromoVoucherUseCase
        )
    }

    //region getPromoVouchers
    @Test
    fun `When get promo voucher from remote success, observer should successfully receive vouchers data`() {
        //Given
        val voucherSlugs = listOf("JUNCASHBACKSUPER")
        val voucher = buildExclusiveLaunchVoucher()
        val expected = Success(listOf(voucher))

        val mockedResponse = listOf(voucher)

        coEvery { getPromoVoucherListUseCase.execute(voucherSlugs) } returns mockedResponse

        //When
        viewModel.getPromoVouchers(voucherSlugs)

        //Then
        val actual = viewModel.vouchers.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When get promo voucher from remote failed, observer should receive error`() {
        //Given
        val voucherSlugs = listOf("JUNCASHBACKSUPER")
        val error = MessageErrorException("Server error")

        coEvery { getPromoVoucherListUseCase.execute(voucherSlugs) } throws error

        //When
        viewModel.getPromoVouchers(voucherSlugs)

        //Then
        val actual = viewModel.vouchers.getOrAwaitValue()
        assertEquals(Fail(error), actual)
    }
    //endregion


    private fun buildExclusiveLaunchVoucher(): ExclusiveLaunchVoucher {
        return ExclusiveLaunchVoucher(
            id = 1,
            voucherName = "Cashback 40% s.d 100rb",
            minimumPurchase = 40_000,
            remainingQuota = 100,
            slug = "JUNCASHBACKSUPER",
            isDisabledButton = false,
            couponCode = "coupon-code",
            buttonStr = "Klaim"
        )
    }
}
