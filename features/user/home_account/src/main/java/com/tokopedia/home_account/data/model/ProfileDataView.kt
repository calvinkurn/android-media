package com.tokopedia.home_account.data.model

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ProfileDataView (
        val name: String = "",
        val phone: String = "",
        val email: String = "",
        val avatar: String = "",
        val members: MemberDataView? = MemberDataView(),
        val financial: SettingDataView? = SettingDataView(),
        val backdrop: String = ""
)