package com.tokopedia.affiliate.ui.custom

import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel

abstract class AffiliateBaseFragment<T : BaseViewModel> : BaseViewModelFragment<T>() {
    companion object {
        const val WARNING = "warning"
        const val ERROR = "error"
        const val ANNOUNCEMENT = "announcement"
    }
    fun onGetValidateUserData(validateUserdata: AffiliateValidateUserData?) {
        validateUserdata?.validateAffiliateUserStatus?.userStatusData?.let {
            if (it.isEligible == false) {
                onNotEligible()
            } else if (it.isRegistered == false) {
                onUserNotRegistered()
            } else if (it.isSystemDown == true) {
                onSystemDown()
            } else if (it.isReviewed == true) {
                onReviewed()
            } else {
                onUserValidated()
            }
        }
    }

    abstract fun onSystemDown()
    abstract fun onReviewed()
    abstract fun onUserNotRegistered()
    abstract fun onNotEligible()
    abstract fun onUserValidated()
}
