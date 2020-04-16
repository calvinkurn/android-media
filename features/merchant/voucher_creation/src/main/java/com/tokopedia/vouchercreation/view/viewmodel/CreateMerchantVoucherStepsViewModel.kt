package com.tokopedia.vouchercreation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.view.uimodel.VoucherCreationStepInfo
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class CreateMerchantVoucherStepsViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mStepPositionLiveData = MutableLiveData<VoucherCreationStepInfo>()
    val stepPositionLiveData : LiveData<VoucherCreationStepInfo>
        get() = mStepPositionLiveData

    fun setStepPosition(stepInfo: VoucherCreationStepInfo) {
        mStepPositionLiveData.value =
                if (mStepPositionLiveData.value != stepInfo) {
                    stepInfo
                } else return
    }

}