package com.tokopedia.settingbank.addeditaccount.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 6/21/18.
 */

interface AddEditBankContract {

    interface View : CustomerView {
        fun getContext(): Context

    }

    interface Presenter : CustomerPresenter<View> {

    }
}