package com.tokopedia.home.account.presentation.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface SettingOptionsView : CustomerView {
    fun refreshSafeSearchOption()

    fun refreshSettingOptionsList()

}