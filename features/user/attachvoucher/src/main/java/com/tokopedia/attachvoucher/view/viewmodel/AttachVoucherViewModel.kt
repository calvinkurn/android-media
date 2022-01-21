package com.tokopedia.attachvoucher.view.viewmodel

import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachvoucher.data.FilterParam
import com.tokopedia.attachvoucher.data.GetVoucherParam
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor(
        private val getVouchersUseCase: GetVoucherUseCase,
        private val dispatcher: CoroutineDispatchers,
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
            _filter.value = NO_FILTER
        } else {
            _filter.value = filterType
        }
    }

    fun loadVouchers(page: Int) {
        if (isLoading) {
            cancelCurrentLoad()
        }
        startLoading()
        currentPage = page
        launchCatchError(block = {
            val param = generateParams(page, filter.value ?: NO_FILTER)
            val vouchers = getVouchersUseCase(param)
            onSuccessGetVouchers(vouchers)
            stopLoading()
        }, onError = {
            onErrorGetVouchers(it)
            stopLoading()
        })
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
        const val PARAM_FILTER = "Filter"
    }

}