package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SettingViewHolder {
        return SettingViewHolder(basicView, listener)
    }

}