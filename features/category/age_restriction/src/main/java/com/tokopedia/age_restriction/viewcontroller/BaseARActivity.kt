package com.tokopedia.age_restriction.viewcontroller

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.tradein_common.viewcontrollers.BaseViewModelActivity
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

abstract class BaseARActivity<T : BaseViewModel> : BaseViewModelActivity<T>() {

    companion object {
        val LOGIN_REQUEST = 514
        val VERIFICATION_REQUEST = 515
        val RESULT_IS_ADULT = 980
        val RESULT_IS_NOT_ADULT = 180
    }

    override fun getVMFactory(): ViewModelProvider.NewInstanceFactory {
        return ViewModelProvider.NewInstanceFactory()
    }
}