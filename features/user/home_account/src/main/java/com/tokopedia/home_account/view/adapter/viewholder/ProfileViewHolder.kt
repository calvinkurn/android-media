package com.tokopedia.home_account.view.adapter.viewholder

import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.SpanningLinearLayoutManager
import com.tokopedia.home_account.view.adapter.HomeAccountBalanceAndPointAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.home_account_balance_and_point.view.*
import kotlinx.android.synthetic.main.home_account_item_profile.view.*
import kotlinx.android.synthetic.main.home_account_member.view.*
import kotlinx.android.synthetic.main.home_account_profile.view.*


/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ProfileViewHolder(
    itemView: View,
    val listener: HomeAccountUserListener,
    private val balanceAndPointAdapter: HomeAccountBalanceAndPointAdapter?,
    private val memberAdapter: HomeAccountMemberAdapter?
) : BaseViewHolder(itemView) {

    fun bind(profile: ProfileDataView) {
        with(itemView) {
            account_user_item_profile_name?.text = profile.name
            if (profile.phone.isNotEmpty()) {
                account_user_item_profile_phone?.text = Utils.formatPhoneNumber(profile.phone)
            } else {
                account_user_item_profile_phone?.hide()
                account_user_item_profile_name?.run {
                    account_user_item_profile_name?.setPadding(
                        paddingLeft,
                        8,
                        paddingRight,
                        paddingBottom
                    )
                }
            }

            if (profile.name.toLowerCase().contains(DEFAULT_NAME)) {
                account_user_item_profile_icon_warning_name?.show()
                account_user_item_profile_icon_warning_name?.setOnClickListener {
                    listener.onIconWarningClicked(
                        profile
                    )
                }
            } else account_user_item_profile_icon_warning_name?.hide()

            if (profile.phone != profile.email) {
                account_user_item_profile_email?.text = profile.email
            }
            account_user_item_profile_edit?.setOnClickListener { listener.onEditProfileClicked() }
            home_account_profile_section?.setOnClickListener { listener.onProfileClicked() }

            loadImage(account_user_item_profile_avatar, profile.avatar)

            setupMemberAdapter(itemView)
            setupBalanceAndPointAdapter(itemView)

            setBackground(context, account_user_item_profile_container)
            listener.onItemViewBinded(adapterPosition, itemView, profile)
            memberAdapter?.let { memberAdapter ->
                listener.onProfileAdapterReady(memberAdapter)
            }
        }
    }

    private fun setBackground(
        context: Context?,
        accountUserItemProfileContainer: ConstraintLayout?
    ) {
        val mode = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                accountUserItemProfileContainer?.setBackgroundResource(R.drawable.ic_account_backdrop_dark)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                accountUserItemProfileContainer?.setBackgroundResource(R.drawable.ic_account_backdrop)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
            else -> {
            }
        }
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        ImageUtils.loadImageCircleWithPlaceHolder(imageView.context, imageView, imageUrl)
    }

    private fun setupBalanceAndPointAdapter(itemView: View) {
        if (isShowViewMoreWallet()) {
            itemView.home_account_view_more?.show()
            itemView.home_account_view_more?.setOnClickListener {
                listener.onSettingItemClicked(
                    CommonDataView(
                        id = AccountConstants.SettingCode.SETTING_VIEW_ALL_BALANCE,
                        applink = ApplinkConstInternalGlobal.FUNDS_AND_INVESTMENT
                    )
                )
            }
        } else {
            itemView.home_account_view_more?.hide()
        }

        itemView.home_account_balance_and_point_rv?.adapter = balanceAndPointAdapter
        itemView.home_account_balance_and_point_rv?.setHasFixedSize(true)
        val layoutManager = SpanningLinearLayoutManager(
            itemView.home_account_balance_and_point_rv?.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val verticalDivider =
            ContextCompat.getDrawable(itemView.context, R.drawable.vertical_divider)
        if (itemView.context?.isDarkMode() == true) {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_dark),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_light),
                PorterDuff.Mode.SRC_IN
            )
        }
        val dividerItemDecoration = DividerItemDecoration(
            itemView.home_account_balance_and_point_rv.context,
            layoutManager.orientation
        )

        verticalDivider?.run {
            dividerItemDecoration.setDrawable(this)
        }

        if (itemView.home_account_balance_and_point_rv.itemDecorationCount < 1) {
            itemView.home_account_balance_and_point_rv.addItemDecoration(dividerItemDecoration)
        }
        itemView.home_account_balance_and_point_rv?.layoutManager = layoutManager

        itemView.home_account_balance_and_point_rv?.isLayoutFrozen = true
    }

    private fun setupMemberAdapter(itemView: View) {
        itemView.home_account_member_layout_member_forward?.setOnClickListener {
            listener.onSettingItemClicked(
                CommonDataView(
                    id = AccountConstants.SettingCode.SETTING_MORE_MEMBER,
                    applink = ApplinkConst.TOKOPOINTS
                )
            )
        }

        itemView.home_account_member_layout_rv?.adapter = memberAdapter
        itemView.home_account_member_layout_rv?.setHasFixedSize(true)
        val layoutManager = SpanningLinearLayoutManager(
            itemView.home_account_member_layout_rv?.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val verticalDivider =
            ContextCompat.getDrawable(itemView.context, R.drawable.vertical_divider)
        if (itemView.context?.isDarkMode() == true) {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_dark),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_light),
                PorterDuff.Mode.SRC_IN
            )
        }
        val dividerItemDecoration = DividerItemDecoration(
            itemView.home_account_member_layout_rv.context,
            layoutManager.orientation
        )

        verticalDivider?.run {
            dividerItemDecoration.setDrawable(this)
        }

        if (itemView.home_account_member_layout_rv.itemDecorationCount < 1) {
            itemView.home_account_member_layout_rv.addItemDecoration(dividerItemDecoration)
        }
        itemView.home_account_member_layout_rv?.layoutManager = layoutManager

        itemView.home_account_member_layout_rv?.isLayoutFrozen = true
    }

    private fun isShowViewMoreWallet(): Boolean {
        return if(itemView.context != null){
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(itemView.context)
            firebaseRemoteConfig.getBoolean(RemoteConfigKey.SETTING_SHOW_VIEW_MORE_WALLET_TOGGLE, false)
        } else {
            false
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_profile
        private const val DEFAULT_NAME = "toppers-"
    }
}