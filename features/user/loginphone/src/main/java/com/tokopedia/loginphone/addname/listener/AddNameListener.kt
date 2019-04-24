package com.tokopedia.loginphone.addname.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 23/04/19.
 */
interface AddNameListener{

    interface View : CustomerView{
        fun showLoading()
    }

    interface Presenter : CustomerPresenter<View>{
        fun registerPhoneNumberAndName(name: String)

    }
}