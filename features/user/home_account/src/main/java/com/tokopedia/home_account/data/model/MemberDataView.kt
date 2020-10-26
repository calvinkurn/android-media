package com.tokopedia.home_account.data.model

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class MemberDataView(
        val title: String = "",
        val icon: String = "",
        val items: List<MemberItemDataView> = listOf()
)

data class MemberItemDataView(
        val title: String = "",
        val subtitle: String = "",
        val icon: String = "",
        val applink: String = ""
)