package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherSource
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterVoucherViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var viewModel: FilterVoucherViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FilterVoucherViewModel(CoroutineTestDispatchersProvider)
    }

    @Test
    fun `When setStatusFilter not Processing, Expect trigger changes livedata`() {
        val givenStatus = VoucherStatus.ENDED
        with(viewModel) {
            setupFilterData(FilterModel())
            setStatusFilter(givenStatus)
            assert(givenStatus in filterData.getOrAwaitValue().status)
        }
    }

    @Test
    fun `When setStatusFilter is Processing, Expect trigger changes livedata`() {
        val givenStatus = VoucherStatus.PROCESSING
        with(viewModel) {
            setupFilterData(FilterModel())
            setStatusFilter(givenStatus)
            assert(VoucherStatus.NOT_STARTED in filterData.getOrAwaitValue().status)
            assert(VoucherStatus.ONGOING in filterData.getOrAwaitValue().status)
            assert(VoucherStatus.ENDED in filterData.getOrAwaitValue().status)
            assert(VoucherStatus.STOPPED in filterData.getOrAwaitValue().status)
        }
    }

    @Test
    fun `When setVoucherType, Expect trigger changes livedata`() {
        val givenType = VoucherServiceType.SHOP_VOUCHER
        with(viewModel) {
            setupFilterData(FilterModel())
            // trigger toggle
            setVoucherType(givenType)
            assert(givenType !in filterData.getOrAwaitValue().voucherType)
            // trigger toggle again
            setVoucherType(givenType)
            assert(givenType in filterData.getOrAwaitValue().voucherType)
        }
    }

    @Test
    fun `When setPromoType, Expect trigger changes livedata`() {
        val givenPromo = PromoType.CASHBACK
        with(viewModel) {
            setupFilterData(FilterModel())
            setPromoType(givenPromo)
            assert(givenPromo in filterData.getOrAwaitValue().promoType)
        }
    }

    @Test
    fun `When setSource, Expect trigger changes livedata`() {
        val givenSource = VoucherSource.SELLER_BUDGET
        with(viewModel) {
            setupFilterData(FilterModel())
            setSource(givenSource)
            assert(givenSource in filterData.getOrAwaitValue().source)
        }
    }

    @Test
    fun `When setTarget, Expect trigger changes livedata`() {
        val givenTarget = VoucherTarget.PRIVATE
        with(viewModel) {
            setupFilterData(FilterModel())
            setTarget(givenTarget)
            assert(givenTarget in filterData.getOrAwaitValue().target)
        }
    }

    @Test
    fun `When setTargetBuyer, Expect trigger changes livedata`() {
        val givenTargetBuyer = VoucherTargetBuyer.ALL_BUYER
        with(viewModel) {
            setupFilterData(FilterModel())
            setTargetBuyer(givenTargetBuyer)
            assert(givenTargetBuyer in filterData.getOrAwaitValue().targetBuyer)
        }
    }

    @Test
    fun `When getValueOrDefault is null, Expect trigger changes livedata`() {
        with(viewModel) {
            setTargetBuyer(VoucherTargetBuyer.ALL_BUYER)
            assert(filterData.getOrAwaitValue() == null)
        }
    }

    @Test
    fun `When resetSelection, Expect trigger changes livedata`() {
        with(viewModel) {
            resetSelection()
            assert(filterData.getOrAwaitValue() == FilterModel())
        }
    }

}
