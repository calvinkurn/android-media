package com.tokopedia.talk.addtalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
* @author : Steven 17/09/18
*/

interface AddTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?
    }

    interface Presenter : CustomerPresenter<View> {

    }
}