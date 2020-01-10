package com.tokopedia.attachvoucher.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor(
        private val getVouchersUseCase: GetVoucherUseCase
) : ViewModel() {

    var shopId: String = ""

    var filter: MutableLiveData<Int> = MutableLiveData()
    var vouchers: MutableLiveData<List<Voucher>> = MutableLiveData()
    var filteredVouchers: LiveData<List<Voucher>> = MutableLiveData()

    fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
        shopId = arguments.getString(ApplinkConst.AttachVoucher.PARAM_SHOP_ID) ?: ""
    }

    fun setFilter(filterType: Int) {
        val currentFilter = filter.value
        if (currentFilter == filterType) {
            filter.value = NO_FILTER
        } else {
            filter.value = filterType
        }
    }

    fun clearFilter() {

    }

    fun loadVouchers() {
        if (shopId.isEmpty()) return
        getVouchersUseCase.getVouchers(
                ::onSuccessGetVouchers,
                ::onErrorGetVouchers,
                shopId.toInt()
        )
    }

    private fun onSuccessGetVouchers(getVoucherResponse: GetVoucherResponse) {
        vouchers.value = getVoucherResponse.vouchers
    }

    private fun onErrorGetVouchers(throwable: Throwable) {

    }

    companion object {
        const val NO_FILTER = -1
    }

}