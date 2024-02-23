package com.tokopedia.home_account.account_settings.presentation.viewmodel

open class SettingItemUIModel {
    @JvmField
    var id = -1
    @JvmField
    var title = ""
    @JvmField
    var subtitle: String? = null
    var iconResource = -1
    var isHideArrow = false

    constructor(id: Int, title: String) {
        this.id = id
        this.title = title
    }

    constructor(id: Int, title: String, subtitle: String?) {
        this.id = id
        this.title = title
        this.subtitle = subtitle
    }
}
