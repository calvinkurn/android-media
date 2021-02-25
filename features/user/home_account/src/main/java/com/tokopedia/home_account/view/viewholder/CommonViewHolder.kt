package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.home_account_item_common.view.*

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class CommonViewHolder(itemView: View, val listener: HomeAccountUserListener) : BaseViewHolder(itemView) {

    fun bind(common: CommonDataView) {
        with(itemView) {
            account_user_item_common_title?.text = common.title
            if (common.icon != 0) {
                account_user_item_common_icon?.setImage(common.icon)
            }
            if (common.urlIcon.isNotEmpty()) {
                ImageHandler.loadImageFit2(account_user_item_common_icon.context, account_user_item_common_icon, common.urlIcon)
            }

            itemView.setOnClickListener {
                listener.onSettingItemClicked(common)
            }
            if (common.endText.isNotEmpty() && common.type != TYPE_SWITCH) {
                account_user_item_common_end_text?.show()
                account_user_item_common_end_text?.text = common.endText
            }

            account_user_item_common_title?.setPadding(0, 0, 0, 0)

            when (common.type) {
                TYPE_WITHOUT_BODY -> {
                    account_user_item_common_body?.hide()
                    account_user_item_common_title?.setPadding(0, 10, 0, 0)
                }
                TYPE_SWITCH -> {
                    itemView.isClickable = false
                    account_user_item_common_switch?.show()
                    account_user_item_common_body?.text = common.body
                    account_user_item_common_switch?.isChecked = common.isChecked
                    account_user_item_common_switch?.setOnCheckedChangeListener { _, isChecked ->
                        listener.onSwitchChanged(common, isChecked, account_user_item_common_switch)
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

        const val TYPE_DEFAULT = 1
        const val TYPE_SWITCH = 2
        const val TYPE_WITHOUT_BODY = 3
    }
}