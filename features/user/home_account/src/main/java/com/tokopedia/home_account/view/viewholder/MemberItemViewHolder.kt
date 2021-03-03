package com.tokopedia.home_account.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.MemberItemDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.home_account_item_member.view.*

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class MemberItemViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    fun bind(member: MemberItemDataView) {
        with(itemView) {
            home_account_item_member_subtitle?.text = member.subtitle
            home_account_item_member_title?.text = member.title
            ImageUtils.loadImageWithoutPlaceholderAndError(home_account_item_member_icon, member.icon)
            itemView.setOnClickListener {
                listener.onMemberItemClicked(member.applink, member.type)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_member

        val TYPE_DEFAULT = 1
        val TYPE_TOKOMEMBER = 2
        val TYPE_TOPQUEST = 3
        val TYPE_KUPON_SAYA = 4
    }

}