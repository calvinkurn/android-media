package com.tokopedia.attachvoucher.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachvoucher.data.FilterParam
import com.tokopedia.attachvoucher.data.GetVoucherParam
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor(
    private val getVouchersUseCase: GetVoucherUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val hasNext: Boolean get() = getVouchersUseCase.hasNext
    var isLoading = false
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
            setFilter(NO_FILTER)
        } else {
            setFilter(filterType)
        }
    }

    fun setFilter(filterType: Int?) {
        _filter.value = filterType
    }

    fun loadVouchers(page: Int) {
        if (isLoading) {
            cancelCurrentLoad()
        }
        startLoading()
        currentPage = page
        viewModelScope.launch {
            try {
                val param = generateParams(page, filter.value ?: NO_FILTER)
                val vouchers = getVouchersUseCase(param)
                onSuccessGetVouchers(vouchers)
                stopLoading()
            } catch (throwable: Throwable) {
                onErrorGetVouchers(throwable)
                stopLoading()
            }
        }
    }

    private fun stopLoading() {
        isLoading = false
    }

    private fun startLoading() {
        isLoading = true
    }

    fun cancelCurrentLoad() {
        cancel()
        stopLoading()
    }

    private fun generateParams(page: Int, filter: Int): FilterParam {
        val paramMVFilter = GetVoucherParam(GetVoucherUseCase.MVFilter.VoucherStatus.paramOnGoing, GetVoucherUseCase.MVFilter.PerPage.default, page)

        if (filter != GetVoucherUseCase.MVFilter.VoucherType.noFilter) {
            paramMVFilter.voucher_type = filter
        }
        return FilterParam(paramMVFilter)
    }

    fun hasNoFilter(): Boolean {
        return when (filter.value) {
            null -> true
            NO_FILTER -> true
            else -> false
        }
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
