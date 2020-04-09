package com.tokopedia.talk_old.addtalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
* @author : Steven 17/09/18
*/

interface AddTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?
        fun onErrorCreateTalk(throwable: Throwable?)
        fun onSuccessCreateTalk(productId: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun send(productId: String, text: String)

    }
}