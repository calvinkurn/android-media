package com.tokopedia.home_account.data.model

import com.tokopedia.home_account.view.viewholder.SettingViewHolder

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
data class SettingDataView(
        val title: String = "",
        val items: MutableList<CommonDataView> = mutableListOf(),
        var isExpanded: Boolean = false,
        var showArrowDown: Boolean = false
)