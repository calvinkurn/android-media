package com.tokopedia.home_account.view.listener

import com.tokopedia.home_account.data.model.CommonDataView

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
interface HomeAccountUserListener {
    fun onEditProfileClicked()
    fun onSettingItemClicked(item: CommonDataView)
    fun onMemberItemClicked(applink: String)
    fun onSwitchChanged(item: CommonDataView, isActive: Boolean)
}