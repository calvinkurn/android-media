package com.tokopedia.home_account.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.MemberDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.home_account_member.view.*

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class MemberViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {

    fun bind(member: MemberDataView) {
        with(itemView) {
            ImageUtils.loadImageWithoutPlaceholderAndError(home_account_member_layout_member_icon, member.icon)

            home_account_member_layout_title?.text = member.title
            setupItemAdapter(this, member)
        }
    }

    private fun setupItemAdapter(itemView: View, members: MemberDataView) {
        val adapter = HomeAccountMemberAdapter(listener)
        adapter.list = members.items
        itemView.home_account_member_layout_rv?.adapter = adapter
        itemView.home_account_member_layout_rv?.layoutManager = GridLayoutManager(itemView.home_account_member_layout_rv?.context, 1, GridLayoutManager.HORIZONTAL, false)
    }

    companion object {
        val LAYOUT = R.layout.home_account_member
    }

}