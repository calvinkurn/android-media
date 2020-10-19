package com.tokopedia.home_account.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import kotlinx.android.synthetic.main.home_account_item_common.view.*

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class CommonViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    fun bind(common: CommonDataView) {
        with(itemView) {
            account_user_item_common_title?.text = common.title
            if(common.icon != 0) {
                this.context?.run {
                    account_user_item_common_icon?.setImageDrawable(ContextCompat.getDrawable(this, common.icon))
                }
            }
            itemView.setOnClickListener { listener.onSettingItemClicked(common.applink) }

            when(common.type) {
                TYPE_WITHOUT_BODY -> {
                    account_user_item_common_body?.visibility = View.GONE
                }
                TYPE_SWITCH -> {
                    account_user_item_common_switch?.visibility = View.VISIBLE
                    account_user_item_common_body?.text = common.body
                    account_user_item_common_switch?.setOnCheckedChangeListener { _, isChecked ->
                        listener.onSwitchChanged(common, isChecked)
                    }
                }
                else -> {
                    account_user_item_common_body?.text = common.body
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_common

        val TYPE_DEFAULT = 1
        val TYPE_SWITCH = 2
        val TYPE_WITHOUT_BODY = 3
    }

}