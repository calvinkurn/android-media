package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class MerchantVoucherTargetViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mVoucherTargetListData = MutableLiveData<List<VoucherTargetItemUiModel>>()
    val voucherTargetListData : LiveData<List<VoucherTargetItemUiModel>>
        get() = mVoucherTargetListData

    fun setVoucherTargetListData(list: List<VoucherTargetItemUiModel>) {
        mVoucherTargetListData.value = list
    }

}