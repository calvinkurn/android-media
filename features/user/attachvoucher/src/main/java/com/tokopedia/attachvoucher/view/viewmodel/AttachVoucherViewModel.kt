package com.tokopedia.attachvoucher.view.viewmodel

import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor(
        private val getVouchersUseCase: GetVoucherUseCase,
        private val dispatcher: CoroutineDispatchers,
        private val mapper: VoucherMapper
) : BaseViewModel(dispatcher.io) {

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
        launchCatchError(block = {
            val param = generateParams(page, filter.value ?: NO_FILTER)
            val response = getVouchersUseCase(param)
            val vouchers = mapper.map(response)
            withContext(dispatcher.main) {
                onSuccessGetVouchers(vouchers)
            }
        }, onError = {
            withContext(dispatcher.main) {
                onErrorGetVouchers(it)
            }
        })
//        currentPage = page
//        if (getVouchersUseCase.isLoading) {
//            getVouchersUseCase.cancelCurrentLoad()
//        }
//        getVouchersUseCase.getVouchers(
//                page,
//                filter.value ?: NO_FILTER,
//                ::onSuccessGetVouchers,
//                ::onErrorGetVouchers
//        )
    }

    private fun generateParams(page: Int, filter: Int): Map<String, Any> {
        val requestParam = ArrayMap<String, Any>()
        val paramMVFilter = ArrayMap<String, Any>().apply {
            put(GetVoucherUseCase.MVFilter.VoucherStatus.param, GetVoucherUseCase.MVFilter.VoucherStatus.paramOnGoing)
            put(GetVoucherUseCase.MVFilter.PerPage.param, GetVoucherUseCase.MVFilter.PerPage.default)
            put(GetVoucherUseCase.MVFilter.paramPage, page)
        }

        if (filter != GetVoucherUseCase.MVFilter.VoucherType.noFilter) {
            paramMVFilter[GetVoucherUseCase.MVFilter.VoucherType.param] = filter
        }
        requestParam[PARAM_FILTER] = paramMVFilter
        return requestParam
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