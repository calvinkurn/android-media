package com.tokopedia.talk

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by Steven
 */

interface ProductTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?
    }

    interface Presenter : CustomerPresenter<View> {
        fun getProductTalk()
    }
}