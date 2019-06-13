package com.tokopedia.settingnotif.usersetting.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface SettingFieldContract {

    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<View> {

    }

}