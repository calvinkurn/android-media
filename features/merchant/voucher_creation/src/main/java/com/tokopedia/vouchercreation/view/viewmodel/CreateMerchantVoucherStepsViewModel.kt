package com.tokopedia.vouchercreation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.view.uimodel.VoucherCreationStepInfo
import kotlinx.coroutines.Dispatchers

class CreateMerchantVoucherStepsViewModel : BaseViewModel(Dispatchers.Main) {

    private val mStepPositionLiveData = MutableLiveData<VoucherCreationStepInfo>()
    val stepPositionLiveData : LiveData<VoucherCreationStepInfo>
        get() = mStepPositionLiveData



}