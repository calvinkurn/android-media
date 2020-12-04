package com.tokopedia.home_account.data.model

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class CommonDataView(
        val title: String = "",
        val body: String = "",
        val icon: Int = 0,
        val type: Int = 1,
        val applink: String = "",
        val id: Int = 0,
        var isChecked: Boolean = false,
        var endText: String = "",
        val urlIcon: String = ""
)