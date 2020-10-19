package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.ProfileViewHolder

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserAdapterDelegate (val listener: HomeAccountUserListener) :
        TypedAdapterDelegate<ProfileDataView, Any, ProfileViewHolder>(
                ProfileViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: ProfileDataView, holder: ProfileViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProfileViewHolder {
        return ProfileViewHolder(basicView, listener)
    }
}