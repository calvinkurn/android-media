package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import javax.inject.Inject

class DisplayVoucherViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _selectedDisplayVoucherType = MutableLiveData<ImageRatio>()
    val selectedDisplayVoucherType: LiveData<ImageRatio>
        get() = _selectedDisplayVoucherType

    fun setSelectedVoucherChip(couponType: ImageRatio) {
        _selectedDisplayVoucherType.postValue(couponType)
    }
}
