package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.MemberDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.MemberViewHolder

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserMemberDelegate (val listener: HomeAccountUserListener):
        TypedAdapterDelegate<MemberDataView, Any, MemberViewHolder>(
                MemberViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: MemberDataView, holder: MemberViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): MemberViewHolder {
        return MemberViewHolder(basicView, listener)
    }
}