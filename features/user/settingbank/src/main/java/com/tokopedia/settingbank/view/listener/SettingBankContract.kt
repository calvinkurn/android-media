package com.tokopedia.settingbank.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 6/7/18.
 */

interface SettingBankContract {

    interface View : CustomerView {
        fun getContext(): Context

        fun showLoading()

        fun hideLoading()

    }

    interface Presenter : CustomerPresenter<View> {

    }
}