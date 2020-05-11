package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.Dispatchers

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListViewModel : BaseViewModel(Dispatchers.Unconfined) {

    private val _voucherList: MutableLiveData<List<VoucherUiModel>> = MutableLiveData()
    val voucherList: LiveData<List<VoucherUiModel>>
        get() = _voucherList

    fun getVoucherList() {

    }
}