package com.tokopedia.payment.setting.authenticate.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard

interface AuthenticateCCContract{
    interface View : BaseListViewListener<TypeAuthenticateCreditCard>{
        fun onErrorUpdateWhiteList(e: Throwable)
        fun onResultUpdateWhiteList(checkWhiteListStatus: CheckWhiteListStatus?)
        fun showProgressLoading()
        fun hideProgressLoading()
        fun getString(resId: Int): String
        fun goToOtpPage(phoneNumber: String)

    }

    interface Presenter : CustomerPresenter<View>{
        fun updateWhiteList(authValue: Int, resources: Resources, isNeedCheckOtp : Boolean = false)
        fun checkWhiteList(resources: Resources)
    }
}