package com.tokopedia.home_account.view.viewholder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.home_account_expandable_layout.view.*

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SettingViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    var rotationAngle = 0F

    fun bind(setting: SettingDataView) {
        with(itemView) {
            if(setting.title.isNotEmpty()) {
                home_account_expandable_layout_title?.visibility = View.VISIBLE
                home_account_expandable_layout_title?.text = setting.title
            }
            else {
                home_account_expandable_layout_title?.visibility = View.GONE
            }

            if(setting.showArrowDown){
                itemView.home_account_expandable_arrow?.show()
            }else {
                itemView.home_account_expandable_arrow?.hide()
            }
            setupItemAdapter(itemView, setting)
        }
    }

    private fun setupItemAdapter(itemView: View, setting: SettingDataView) {
        val adapter = HomeAccountUserCommonAdapter(listener, CommonViewHolder.LAYOUT)
        adapter.list = setting.items
        itemView.home_account_expandable_layout_rv?.adapter = adapter
        itemView.home_account_expandable_layout_rv?.layoutManager = LinearLayoutManager(itemView.home_account_expandable_layout_rv?.context, LinearLayoutManager.VERTICAL, false)
        itemView.home_account_expandable_layout_rv?.isNestedScrollingEnabled = false
        expandCollapseItem(itemView, setting.isExpanded)

        itemView.setOnClickListener {
            setting.isExpanded = !setting.isExpanded
            rotationAngle = if (rotationAngle == 0F) 180F else 0F //toggle
            itemView?.home_account_expandable_arrow?.animate()?.rotation(rotationAngle)?.setDuration(400)?.start()
            expandCollapseItem(itemView, setting.isExpanded)
        }
    }

    private fun expandCollapseItem(itemView: View, isExpanded: Boolean){
        if(isExpanded){
            itemView.home_account_expandable_layout_rv?.show()
        }else {
            itemView.home_account_expandable_layout_rv?.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_expandable_layout

        val SETTING_ORIENTATION_VERTICAL = 1
    }

}