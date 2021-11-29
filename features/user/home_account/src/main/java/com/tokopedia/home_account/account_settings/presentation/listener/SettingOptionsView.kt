package com.tokopedia.home_account.account_settings.presentation.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface SettingOptionsView : CustomerView {
    fun refreshSafeSearchOption()

    fun refreshSettingOptionsList()

}