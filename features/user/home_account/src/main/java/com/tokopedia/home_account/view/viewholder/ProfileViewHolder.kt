package com.tokopedia.home_account.view.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.SpanningLinearLayoutManager
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.home_account_expandable_layout.view.*
import kotlinx.android.synthetic.main.home_account_item_profile.view.*
import kotlinx.android.synthetic.main.home_account_member.view.*


/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ProfileViewHolder(itemView: View, val listener: HomeAccountUserListener): BaseViewHolder(itemView) {


    fun bind(profile: ProfileDataView) {
        with(itemView) {
            account_user_item_profile_name?.text = profile.name
            account_user_item_profile_phone?.text = profile.phone
            account_user_item_profile_email?.text = profile.email
            account_user_item_profile_edit?.setOnClickListener { listener.onEditProfileClicked() }

            if(profile.backdrop.isNotEmpty()){
//                ImageHandler.loadBackgroundImage(account_user_item_profile_backdrop, profile.backdrop)
            }
            loadImage(account_user_item_profile_avatar, profile.avatar)

            setupMemberAdapter(itemView, profile)
            setupFinancialAdapter(itemView, profile)

            val params: ViewGroup.LayoutParams = account_user_item_profile_backdrop.layoutParams
            params.height = params.height - (home_account_member_card.height/2)
            account_user_item_profile_backdrop.layoutParams = params

        }
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        ImageUtils.loadImageCircleWithPlaceHolder(imageView.context, imageView, imageUrl)
    }

    private fun setupFinancialAdapter(itemView: View, profile: ProfileDataView) {
        itemView?.home_account_expandable_layout_title?.text = profile.financial.title

        val adapter = HomeAccountUserCommonAdapter(listener, CommonViewHolder.LAYOUT_FINANCIAL)
        adapter.list = profile.financial.items
        itemView.home_account_expandable_layout_rv?.adapter = adapter
        itemView.home_account_expandable_layout_rv?.layoutManager = LinearLayoutManager(itemView.home_account_expandable_layout_rv?.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupMemberAdapter(itemView: View, profile: ProfileDataView) {
        itemView.home_account_member_layout_title?.text = profile.members.title
        ImageUtils.loadImageWithoutPlaceholderAndError(itemView.home_account_member_layout_member_icon, profile.members.icon)

        val adapter = HomeAccountMemberAdapter(listener)
        adapter.list = profile.members.items
        itemView.home_account_member_layout_rv?.adapter = adapter
        val layoutManager = SpanningLinearLayoutManager(itemView.home_account_member_layout_rv?.context, LinearLayoutManager.HORIZONTAL, false)

        val verticalDivider = ContextCompat.getDrawable(itemView.context, R.drawable.vertical_divider)
        val dividerItemDecoration = DividerItemDecoration(itemView.home_account_member_layout_rv.context,
                layoutManager.orientation)

        verticalDivider?.run {
            dividerItemDecoration.setDrawable(this)
        }

        itemView.home_account_member_layout_rv.addItemDecoration(dividerItemDecoration)
        itemView.home_account_member_layout_rv?.layoutManager = layoutManager
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_profile
    }

    interface CardRuntimeHeightListener {
        fun setCardLayoutHeight(height: Int)
    }

}