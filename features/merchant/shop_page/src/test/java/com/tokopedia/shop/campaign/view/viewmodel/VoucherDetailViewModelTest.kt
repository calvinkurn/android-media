package com.tokopedia.shop.campaign.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.entity.VoucherDetail
import com.tokopedia.shop.campaign.domain.usecase.GetVoucherDetailUseCase
import com.tokopedia.shop.campaign.domain.usecase.RedeemPromoVoucherUseCase
import com.tokopedia.shop.campaign.domain.usecase.UsePromoVoucherUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VoucherDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getPromoVoucherDetailUseCase: GetVoucherDetailUseCase

    @RelaxedMockK
    lateinit var redeemPromoVoucherUseCase: RedeemPromoVoucherUseCase

    @RelaxedMockK
    lateinit var usePromoVoucherUseCase: UsePromoVoucherUseCase

    private lateinit var viewModel : VoucherDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = VoucherDetailViewModel(
            CoroutineTestDispatchersProvider,
            getPromoVoucherDetailUseCase,
            redeemPromoVoucherUseCase,
            usePromoVoucherUseCase
        )
    }

    //region getVoucherDetail
    @Test
    fun `When get voucher detail from remote success, observer should successfully receive data`() {
        //Given
        val slug = "JUNCASHBACKSUPER"
        val mockedResponse = buildVoucherDetail()

        val expected = Success(mockedResponse)

        coEvery { getPromoVoucherDetailUseCase.execute(slug) } returns mockedResponse

        //When
        viewModel.getVoucherDetail(slug)

        //Then
        val actual = viewModel.voucherDetail.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When get voucher detail from remote failed, observer should receive error`() {
        //Given
        val slug = "JUNCASHBACKSUPER"
        val error = MessageErrorException("Server error")

        coEvery { getPromoVoucherDetailUseCase.execute(slug) } throws error

        //When
        viewModel.getVoucherDetail(slug)

        //Then
        val actual = viewModel.voucherDetail.getOrAwaitValue()
        assertEquals(Fail(error), actual)
    }
    //endregion

    //region claimPromoVoucher
    @Test
    fun `When claim voucher to remote success, observer should successfully receive data`() {
        //Given
        val voucherId : Long = 1
        val isGift = 0

        val voucherDetail = buildVoucherDetail().copy(id = voucherId, isGift = isGift)

        val slug = "JUNCASHBACKSUPER"
        coEvery { getPromoVoucherDetailUseCase.execute(slug) } returns voucherDetail

        val param = RedeemPromoVoucherUseCase.Param(voucherId, isGift)
        val mockedResponse = RedeemPromoVoucherResult(
            promoId = 1,
            voucherCode = "AABBCC",
            redeemMessage = "Sukses di klaim"
        )
        coEvery { redeemPromoVoucherUseCase.execute(param) } returns mockedResponse

        //When
        viewModel.getVoucherDetail(slug)
        viewModel.claimPromoVoucher()

        //Then
        val actual = viewModel.redeemResult.getOrAwaitValue()
        assertEquals(Success(mockedResponse), actual)
    }

    @Test
    fun `When claiming voucher but voucher detail is null, should not invoke remote call to claim voucher`() {
        //Given
        val voucherId : Long = 1
        val isGift = 0
        val error = MessageErrorException("Server error")

        val slug = "JUNCASHBACKSUPER"
        coEvery { getPromoVoucherDetailUseCase.execute(slug) } throws error

        val param = RedeemPromoVoucherUseCase.Param(voucherId, isGift)
        val mockedResponse = RedeemPromoVoucherResult(
            promoId = 1,
            voucherCode = "AABBCC",
            redeemMessage = "Sukses di klaim"
        )
        coEvery { redeemPromoVoucherUseCase.execute(param) } returns mockedResponse

        //When
        viewModel.getVoucherDetail(slug)
        viewModel.claimPromoVoucher()

        //Then
        coVerify(exactly = 0) { redeemPromoVoucherUseCase.execute(param) }
    }

    @Test
    fun `When claim voucher to remote failed, observer should receive error`() {
        //Given
        val voucherId : Long = 1
        val isGift = 0
        val error = MessageErrorException("Server error")

        val voucherDetail = buildVoucherDetail().copy(id = voucherId, isGift = isGift)

        val slug = "JUNCASHBACKSUPER"
        coEvery { getPromoVoucherDetailUseCase.execute(slug) } returns voucherDetail

        val param = RedeemPromoVoucherUseCase.Param(voucherId, isGift)
        coEvery { redeemPromoVoucherUseCase.execute(param) } throws error

        //When
        viewModel.getVoucherDetail(slug)
        viewModel.claimPromoVoucher()

        //Then
        val actual = viewModel.redeemResult.getOrAwaitValue()
        assertEquals(Fail(error), actual)
    }
    //endregion

    //region usePromoVoucher
    @Test
    fun `When use voucher from remote success, observer should successfully receive true as a result`() {
        //Given
        val shopId = "11334455"
        val voucherCode = "voucher-code"
        val param = UsePromoVoucherUseCase.Param(shopId, voucherCode)
        val mockedResponse = true
        val expected = Success(mockedResponse)

        coEvery { usePromoVoucherUseCase.execute(param) } returns mockedResponse

        //When
        viewModel.usePromoVoucher(shopId, voucherCode)

        //Then
        val actual = viewModel.useVoucherResult.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When use voucher from remote failed, observer should successfully receive false as a result`() {
        //Given
        val shopId = "11334455"
        val voucherCode = "voucher-code"
        val param = UsePromoVoucherUseCase.Param(shopId, voucherCode)
        val mockedResponse = false
        val expected = Success(mockedResponse)

        coEvery { usePromoVoucherUseCase.execute(param) } returns mockedResponse

        //When
        viewModel.usePromoVoucher(shopId, voucherCode)

        //Then
        val actual = viewModel.useVoucherResult.getOrAwaitValue()
        assertEquals(expected, actual)
    }


    @Test
    fun `When use voucher from remote failed, observer should receive error`() {
        //Given
        val shopId = "11334455"
        val voucherCode = "voucher-code"
        val param = UsePromoVoucherUseCase.Param(shopId, voucherCode)
        val error = MessageErrorException("Server error")

        coEvery { usePromoVoucherUseCase.execute(param) } throws error

        //When
        viewModel.usePromoVoucher(shopId, voucherCode)

        //Then
        val actual = viewModel.useVoucherResult.getOrAwaitValue()
        assertEquals(Fail(error), actual)
    }
    //endregion

    private fun buildVoucherDetail(): VoucherDetail {
        return VoucherDetail(
            activePeriodDate = "1 June 2030",
            buttonLabel = "Klaim",
            howToUse = "",
            id = 1,
            imageUrlMobile = "https://images.tokopedia.net/fsfksdkf.png",
            isDisabledButton = false,
            minimumUsage = "Rp. 50.000",
            quota = 100,
            title = "Cashback 40% s.d 100rb",
            tnc = "",
            isGift = 0,
            voucherPrice = "Gratis",
            expired = "31 June 2030"
        )
    }
}
