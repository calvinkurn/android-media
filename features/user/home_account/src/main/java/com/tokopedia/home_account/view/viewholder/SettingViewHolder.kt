package com.tokopedia.home_account.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import kotlinx.android.synthetic.main.home_account_expandable_layout.view.*

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SettingViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    fun bind(setting: SettingDataView) {
        with(itemView) {
            home_account_expandable_layout_title?.text = setting.title
            setupItemAdapter(itemView, setting)
        }
    }

    private fun setupItemAdapter(itemView: View, setting: SettingDataView) {
        val adapter = HomeAccountUserCommonAdapter(listener)
        adapter.list = setting.items
        itemView.home_account_expandable_layout_rv?.adapter = adapter
        itemView.home_account_expandable_layout_rv?.layoutManager = LinearLayoutManager(itemView.home_account_expandable_layout_rv?.context, LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        val LAYOUT = R.layout.home_account_expandable_layout
    }

}