package com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.ShopWithTopProducts
import com.tokopedia.vouchercreation.product.share.domain.usecase.GetShopAndTopProductsUseCase
import com.tokopedia.vouchercreation.product.voucherlist.view.bottomsheet.CouponFilterBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetVoucherListUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class CouponListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getVoucherListUseCase: GetVoucherListUseCase

    @RelaxedMockK
    lateinit var cancelVoucherUseCase: CancelVoucherUseCase

    @RelaxedMockK
    lateinit var getShopAndTopProductsUseCase: GetShopAndTopProductsUseCase

    @RelaxedMockK
    lateinit var getCouponDetailUseCase: GetCouponDetailUseCase

    val viewModel: CouponListViewModel by lazy {
        spyk(CouponListViewModel(
            CoroutineTestDispatchersProvider,
            getVoucherListUseCase,
            cancelVoucherUseCase,
            getShopAndTopProductsUseCase,
            getCouponDetailUseCase,
        ))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun cleanup() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `When setting filter search param Expect search param changed and invoked`() {
        viewModel.setStatusFilter(VoucherStatus.ONGOING)
        viewModel.setCouponSearchKeyword("")
        viewModel.setSelectedFilterType(CouponFilterBottomSheet.FilterType.CASHBACK)
        viewModel.setSelectedFilterTarget(CouponFilterBottomSheet.FilterTarget.PRIVATE)

        assertEquals(viewModel.selectedFilterType.getOrAwaitValue(), CouponFilterBottomSheet.FilterType.CASHBACK)
        assertEquals(viewModel.selectedFilterTarget.getOrAwaitValue(), CouponFilterBottomSheet.FilterTarget.PRIVATE)
        assertEquals(viewModel.couponSearchKeyword, "")
        assertEquals(viewModel.couponStatusFilter, VoucherStatus.ONGOING)
    }

    @Test
    fun `When getCouponList success Expect couponList value invoked`() {
        coEvery {
            getVoucherListUseCase.executeOnBackground()
        } returns emptyList()

        viewModel.getCouponList(0)
        val couponlistResult = viewModel.couponList.getOrAwaitValue()
        assert(couponlistResult is Success)
    }

    @Test
    fun `When gettingCouponList error Expect couponList Fail invoked`() {
        coEvery {
            getVoucherListUseCase.executeOnBackground()
        } throws MessageErrorException()

        viewModel.getCouponList(0)
        val couponlistResult = viewModel.couponList.getOrAwaitValue()
        assert(couponlistResult is Fail)
    }

    @Test
    fun `When getCouponDetail success Expect couponDetail value invoked`() {
        coEvery {
            getCouponDetailUseCase.executeOnBackground()
        } returns generateCouponUiModel()

        viewModel.getCouponDetail(0)
        val result = viewModel.couponDetail.getOrAwaitValue()
        assert(result is Success)
    }

    @Test
    fun `When getCouponDetail error Expect couponDetail Fail invoked`() {
        coEvery {
            getCouponDetailUseCase.executeOnBackground()
        } throws MessageErrorException()

        viewModel.getCouponDetail(0)
        val result = viewModel.couponDetail.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When getShopAndTopProducts success Expect shopWithTopProducts value invoked`() {
        coEvery {
            getShopAndTopProductsUseCase.execute(any(), any())
        } returns ShopWithTopProducts(emptyList(), ShopBasicDataResult())

        viewModel.setCoupon(generateCouponUiModel())
        viewModel.getCoupon()?.let {
            viewModel.getShopAndTopProducts(it)
            val result = viewModel.shopWithTopProducts.getOrAwaitValue()
            assert(result is Success)
        }
    }

    @Test
    fun `When getShopAndTopProducts error Expect shopWithTopProducts Fail invoked`() {
        coEvery {
            getShopAndTopProductsUseCase.execute(any(), any())
        } throws MessageErrorException()

        viewModel.getShopAndTopProducts(generateCouponUiModel())
        val result = viewModel.shopWithTopProducts.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When cancelCoupon cancel success Expect cancelCoupon value invoked`() {
        coEvery {
            cancelVoucherUseCase.executeOnBackground()
        } returns 0

        viewModel.cancelCoupon(0, CancelVoucherUseCase.CancelStatus.DELETE)
        val result = viewModel.cancelCoupon.getOrAwaitValue()
        assert(result is Success)
    }

    @Test
    fun `When cancelCoupon cancel error Expect cancelCoupon Fail invoked`() {
        coEvery {
            cancelVoucherUseCase.executeOnBackground()
        } throws MessageErrorException()

        viewModel.cancelCoupon(0, CancelVoucherUseCase.CancelStatus.DELETE)
        val result = viewModel.cancelCoupon.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When cancelCoupon stop success Expect stopCoupon value invoked`() {
        coEvery {
            cancelVoucherUseCase.executeOnBackground()
        } returns 0

        viewModel.cancelCoupon(0, CancelVoucherUseCase.CancelStatus.STOP)
        val result = viewModel.stopCoupon.getOrAwaitValue()
        assert(result is Success)
    }

    @Test
    fun `When cancelCoupon stop error Expect stopCoupon Fail invoked`() {
        coEvery {
            cancelVoucherUseCase.executeOnBackground()
        } throws MessageErrorException()

        viewModel.cancelCoupon(0, CancelVoucherUseCase.CancelStatus.STOP)
        val result = viewModel.stopCoupon.getOrAwaitValue()
        assert(result is Fail)
    }

    private fun generateCouponUiModel() = CouponUiModel(
        1,
        "",
        VoucherTypeConst.CASHBACK,
        "",
        "",
        "",
        "",
        VoucherStatusConst.ONGOING,
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
        true,
        "",
        emptyList(),
        emptyList(),
        0)

    @Suppress("UNCHECKED_CAST")
    private fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        return data as T
    }
}