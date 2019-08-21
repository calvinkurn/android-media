package com.tokopedia.profilecompletion.addname.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.sessioncommon.data.register.RegisterInfo

/**
 * @author by nisie on 23/04/19.
 */
interface AddNameListener{

    interface View : CustomerView{
        fun showLoading()
        fun onErrorRegister(throwable: Throwable)
        fun onSuccessRegister(registerInfo: RegisterInfo)
    }

    interface Presenter : CustomerPresenter<View>{
        fun registerPhoneNumberAndName(name: String, phoneNumber : String)

    }
}