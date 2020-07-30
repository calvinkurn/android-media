package com.tokopedia.attachvoucher.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tokopedia.attachvoucher.data.EmptyVoucherUiModel
import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor(
        private val getVouchersUseCase: GetVoucherUseCase
) : ViewModel() {

    private var _filter: MutableLiveData<Int> = MutableLiveData()
    private var _vouchers: MutableLiveData<List<VoucherUiModel>> = MutableLiveData()
    private var _error: MutableLiveData<Throwable> = MutableLiveData()

    val filter: LiveData<Int> get() = _filter
    val error: LiveData<Throwable> get() = _error
    val filteredVouchers: LiveData<List<VoucherUiModel>> = Transformations.map(_filter) {
        val fVouchers = _vouchers.value?.filter { voucher ->
            _filter.value == NO_FILTER || _filter.value == voucher.type
        } ?: emptyList()
        _vouchers.value?.let { vouchers ->
            if (fVouchers.isEmpty() && vouchers.isNotEmpty()) {
                return@map listOf(EmptyVoucherUiModel())
            }
        }
        return@map fVouchers
    }

    fun toggleFilter(filterType: Int) {
        val currentFilter = _filter.value
        if (currentFilter == filterType) {
            _filter.value = NO_FILTER
        } else {
            _filter.value = filterType
        }
    }

    fun loadVouchers(shopId: String) {
        if (shopId.isEmpty()) return
        getVouchersUseCase.getVouchers(
                ::onSuccessGetVouchers,
                ::onErrorGetVouchers,
                shopId.toInt()
        )
    }

    private fun onSuccessGetVouchers(getVoucherResponse: GetVoucherResponse) {
        _vouchers.value = getVoucherResponse.vouchers
        _filter.value = NO_FILTER
    }

    private fun onErrorGetVouchers(throwable: Throwable) {
        _error.value = throwable
    }

    companion object {
        const val NO_FILTER = -1
    }

}