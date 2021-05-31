package com.tokopedia.home_account.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
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

    private var rotationAngle = 0F

    var adapter: HomeAccountUserCommonAdapter? = null

    fun refreshCommonAdapter(){
        adapter?.notifyDataSetChanged()
    }

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
        listener.onItemViewBinded(adapterPosition, itemView, setting)
    }

    private fun setupItemAdapter(itemView: View, setting: SettingDataView) {
        adapter = HomeAccountUserCommonAdapter(listener, CommonViewHolder.LAYOUT)
        adapter?.list = setting.items
        itemView.home_account_expandable_layout_rv?.adapter = adapter
        itemView.home_account_expandable_layout_rv?.layoutManager = LinearLayoutManager(itemView.home_account_expandable_layout_rv?.context, LinearLayoutManager.VERTICAL, false)
        itemView.home_account_expandable_layout_rv?.isNestedScrollingEnabled = false
        expandCollapseItem(itemView, setting.isExpanded)

        itemView.setOnClickListener {
            if(itemView.home_account_expandable_arrow.visibility == View.VISIBLE) {
                val id : Int = when (setting.title) {
                    TITLE_APP_SETTING -> AccountConstants.SettingCode.SETTING_APP_SETTING
                    TITLE_ABOUT_TOKOPEDIA -> AccountConstants.SettingCode.SETTING_ABOUT_TOKOPEDIA
                    else -> 0
                }
                listener.onSettingItemClicked(CommonDataView(id = id, body = setting.title))
                setting.isExpanded = !setting.isExpanded
                rotationAngle = if (rotationAngle == 0F) 180F else 0F //toggle
                itemView?.home_account_expandable_arrow?.animate()?.rotation(rotationAngle)?.setDuration(400)?.start()
                expandCollapseItem(itemView, setting.isExpanded)
            }
        }
        adapter?.run {
            listener.onCommonAdapterReady(adapterPosition,this)
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
        val TITLE_APP_SETTING = "Pengaturan Aplikasi"
        val TITLE_ABOUT_TOKOPEDIA = "Seputar Tokopedia"
    }

}