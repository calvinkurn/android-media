package com.tokopedia.attachvoucher.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.attachvoucher.data.Voucher
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor() : ViewModel() {

    var filter: MutableLiveData<Int> = MutableLiveData()
    var vouchers: LiveData<List<Voucher>> = MutableLiveData()
    var filteredVouchers: LiveData<List<Voucher>> = MutableLiveData()

    fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
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

    companion object {
        const val NO_FILTER = -1
    }

}