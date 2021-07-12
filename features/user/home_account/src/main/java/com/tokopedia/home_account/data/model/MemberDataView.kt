package com.tokopedia.home_account.data.model

import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class MemberDataView(
        val title: String = "",
        val icon: String = "",
        val isError: Boolean = false,
        val items: ArrayList<MemberItemDataView> = arrayListOf()
)

data class MemberItemDataView(
        val title: String = "",
        val subtitle: String = "",
        val icon: String = "",
        val applink: String = "",
        override var type: Int = MemberItemViewHolder.TYPE_DEFAULT
): BaseItemDataView()