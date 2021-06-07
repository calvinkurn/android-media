package com.tokopedia.home_account.data.model

import com.tokopedia.home_account.view.viewholder.CommonViewHolder

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class CommonDataView(
        val title: String = "",
        val body: String = "",
        val icon: Int = 0,
        val applink: String = "",
        val id: Int = 0,
        var isChecked: Boolean = false,
        var endText: String = "",
        val urlIcon: String = "",
        val isTitleBold: Boolean = false,
        val isBodyBold: Boolean = false,
        val titleColor: String = "",
        val bodyColor: String = "",
        var labelText: String = "",
        override var type: Int = CommonViewHolder.TYPE_DEFAULT
): BaseItemDataView()