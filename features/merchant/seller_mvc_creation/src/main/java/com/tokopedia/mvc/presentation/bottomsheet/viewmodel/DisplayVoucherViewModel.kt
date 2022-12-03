package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class DisplayVoucherViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _selectedDisplayVoucherType = MutableLiveData<VoucherType>()
    val selectedDisplayVoucherType: LiveData<VoucherType>
        get() = _selectedDisplayVoucherType

    fun setSelectedVoucherChip(couponType: VoucherType) {
        _selectedDisplayVoucherType.postValue(couponType)
    }
}

sealed class VoucherType {
    object Horizontal : VoucherType()
    object Square : VoucherType()
    object Vertical : VoucherType()
}
