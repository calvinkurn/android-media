package com.tokopedia.home_account.view.viewholder

import android.content.res.Configuration
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home_account.R
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.SpanningLinearLayoutManager
import com.tokopedia.home_account.view.adapter.HomeAccountFinancialAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.home_account_financial.view.*
import kotlinx.android.synthetic.main.home_account_item_profile.view.*
import kotlinx.android.synthetic.main.home_account_member.view.*


/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ProfileViewHolder(itemView: View, val listener: HomeAccountUserListener, val financialAdapter: HomeAccountFinancialAdapter?, val memberAdapter: HomeAccountMemberAdapter?): BaseViewHolder(itemView) {

    fun bind(profile: ProfileDataView) {
        with(itemView) {
            account_user_item_profile_name?.text = profile.name
            if(profile.phone.isNotEmpty()) {
                account_user_item_profile_phone?.text = Utils.formatPhoneNumber(profile.phone)
            }else {
                account_user_item_profile_phone?.hide()
                account_user_item_profile_name?.run {
                    account_user_item_profile_name?.setPadding(paddingLeft, 8, paddingRight, paddingBottom)
                }
            }
            account_user_item_profile_email?.text = profile.email
            account_user_item_profile_edit?.setOnClickListener { listener.onEditProfileClicked() }

            loadImage(account_user_item_profile_avatar, profile.avatar)

            setupMemberAdapter(itemView, profile)
            setupFinancialAdapter(itemView, profile)

            val mode = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
            when (mode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    account_user_item_profile_container?.setBackgroundResource(R.drawable.ic_account_backdrop_dark)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    account_user_item_profile_container?.setBackgroundResource(R.drawable.ic_account_backdrop)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
                else -> {}
            }

            listener.onItemViewBinded(adapterPosition, itemView, profile)
            listener.onProfileAdapterReady(financialAdapter!!, memberAdapter!!)
        }
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        ImageUtils.loadImageCircleWithPlaceHolder(imageView.context, imageView, imageUrl)
    }

    private fun setupFinancialAdapter(itemView: View, profile: ProfileDataView) {
        itemView?.home_account_financial_layout_title?.text = profile.financial?.title
        financialAdapter?.list = profile.financial?.items ?: mutableListOf()
        itemView.home_account_financial_layout_rv?.adapter = financialAdapter
        itemView.home_account_financial_layout_rv?.layoutManager = SpanningLinearLayoutManager(itemView.home_account_financial_layout_rv?.context, LinearLayoutManager.HORIZONTAL, false, minWidth = 180)
    }

    private fun setupMemberAdapter(itemView: View, profile: ProfileDataView) {
        itemView.home_account_member_layout_title?.text = profile.members?.title
        ImageUtils.loadImageWithoutPlaceholderAndError(itemView.home_account_member_layout_member_icon, profile.members?.icon ?: "")

        memberAdapter?.list = profile.members?.items?: arrayListOf()
        itemView.home_account_member_layout_rv?.adapter = memberAdapter
        itemView.home_account_member_layout_rv?.setHasFixedSize(true)
        val layoutManager = SpanningLinearLayoutManager(itemView.home_account_member_layout_rv?.context, LinearLayoutManager.HORIZONTAL, false)
        val verticalDivider = ContextCompat.getDrawable(itemView.context, R.drawable.vertical_divider)
        if(itemView.context?.isDarkMode() == true) {
            verticalDivider?.mutate()?.setColorFilter(itemView.resources.getColor(R.color.vertical_divider_dark), PorterDuff.Mode.SRC_IN)
        } else {
            verticalDivider?.mutate()?.setColorFilter(itemView.resources.getColor(R.color.vertical_divider_light), PorterDuff.Mode.SRC_IN)
        }
        val dividerItemDecoration = DividerItemDecoration(itemView.home_account_member_layout_rv.context,
                layoutManager.orientation)

        verticalDivider?.run {
            dividerItemDecoration.setDrawable(this)
        }

        if(itemView.home_account_member_layout_rv.itemDecorationCount < 1) {
            itemView.home_account_member_layout_rv.addItemDecoration(dividerItemDecoration)
        }
        itemView.home_account_member_layout_rv?.layoutManager = layoutManager

        itemView.home_account_member_layout_rv?.isLayoutFrozen = true
        itemView.home_account_member_layout_member_forward?.setOnClickListener {
            listener.onSettingItemClicked(
                    CommonDataView(applink = ApplinkConst.TOKOPOINTS)
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_profile
    }
}