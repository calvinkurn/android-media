package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.SettingViewHolder

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserSettingDelegate (val listener: HomeAccountUserListener):
        TypedAdapterDelegate<SettingDataView, Any, SettingViewHolder>(
                SettingViewHolder.LAYOUT
        ) {

    private var mExpandedPosition = -1
    override fun onBindViewHolder(item: SettingDataView, holder: SettingViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SettingViewHolder {
        return SettingViewHolder(basicView, listener)
    }

}