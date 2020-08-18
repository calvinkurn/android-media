package com.tokopedia.attachvoucher.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor(
        private val getVouchersUseCase: GetVoucherUseCase
) : ViewModel() {

    val hasNext: Boolean get() = getVouchersUseCase.hasNext
    var currentPage = 0

    private var _filter: MutableLiveData<Int> = MutableLiveData()
    val filter: LiveData<Int> get() = _filter

    private var _error: MutableLiveData<Throwable> = MutableLiveData()
    val error: LiveData<Throwable> get() = _error

    private val _vouchers: MutableLiveData<List<VoucherUiModel>> = MutableLiveData()
    val voucher: LiveData<List<VoucherUiModel>> get() = _vouchers

    fun toggleFilter(filterType: Int) {
        val currentFilter = _filter.value
        if (currentFilter == filterType) {
            _filter.value = NO_FILTER
        } else {
            _filter.value = filterType
        }
    }

    fun loadVouchers(page: Int) {
        currentPage = page
        if (getVouchersUseCase.isLoading) {
            getVouchersUseCase.cancelCurrentLoad()
        }
        getVouchersUseCase.getVouchers(
                page,
                filter.value ?: NO_FILTER,
                ::onSuccessGetVouchers,
                ::onErrorGetVouchers
        )
    }

    fun hasNoFilter(): Boolean {
        return filter.value == null || filter.value == NO_FILTER
    }

    private fun onSuccessGetVouchers(vouchers: List<VoucherUiModel>) {
        _vouchers.value = vouchers
    }

    private fun onErrorGetVouchers(throwable: Throwable) {
        _error.value = throwable
    }

    companion object {
        const val NO_FILTER = GetVoucherUseCase.MVFilter.VoucherType.noFilter
    }

}