package com.tokopedia.home_account.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.view.HomeAccountUserFragment
import com.tokopedia.home_account.view.SpanningLinearLayoutManager
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.home_account_expandable_layout.*
import kotlinx.android.synthetic.main.home_account_item_profile.*
import kotlinx.android.synthetic.main.home_account_member.*

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class HomeAccountUserActivity: BaseSimpleActivity(), HasComponent<HomeAccountUserComponents>, onAppBarCollapseListener {

//    override fun getLayoutRes(): Int = R.layout.home_account_user_activity
//
//    override fun getParentViewResourceID(): Int = R.id.home_account_activity_parent_view
//
//    override fun getToolbarResourceID(): Int = R.id.home_account_activity_toolbar

    override fun getNewFragment(): Fragment? {
        return HomeAccountUserFragment.newInstance(intent?.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        home_account_activity_collapsing_toolbar?.setExpandedTitleColor(Color.TRANSPARENT)
        toolbar.visibility = View.GONE
        updateTitle(getString(R.string.home_account_title))
    }

    fun setupToolbarContent(profile: ProfileDataView) {
        account_user_item_profile_name?.text = profile.name
        account_user_item_profile_phone?.text = profile.phone
        account_user_item_profile_email?.text = profile.email

        account_user_item_profile_edit?.setOnClickListener {
            val i = RouteManager.getIntent(this, ApplinkConstInternalGlobal.SETTING_PROFILE)
            startActivity(i)
        }

        loadImage(account_user_item_profile_avatar, profile.avatar)

        setupFinancialAdapter(profile)
        setupMemberAdapter(profile)
        val params: ViewGroup.LayoutParams = account_user_item_profile_backdrop.layoutParams
        params.height = params.height - (include2.height/2)
        account_user_item_profile_backdrop.layoutParams = params
    }

    private fun setupFinancialAdapter(profile: ProfileDataView) {
        home_account_expandable_layout_title?.text = profile.financial.title

        val adapter = HomeAccountUserCommonAdapter(object: HomeAccountUserListener {
            override fun onEditProfileClicked() {}
            override fun onMemberItemClicked(applink: String) {}
            override fun onSettingItemClicked(item: CommonDataView) {}
            override fun onSwitchChanged(item: CommonDataView, isActive: Boolean) {}
        }, CommonViewHolder.LAYOUT_FINANCIAL)

        adapter.list = profile.financial.items
        home_account_expandable_layout_rv?.adapter = adapter
        home_account_expandable_layout_rv?.layoutManager = LinearLayoutManager(home_account_expandable_layout_rv?.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupMemberAdapter(profile: ProfileDataView) {
        home_account_member_layout_title?.text = profile.members.title
        ImageUtils.loadImageWithoutPlaceholderAndError(home_account_member_layout_member_icon, profile.members.icon)

        val adapter = HomeAccountMemberAdapter(object: HomeAccountUserListener {
            override fun onEditProfileClicked() {}
            override fun onMemberItemClicked(applink: String) {
                if(applink.isNotEmpty()) {
                    val intent = RouteManager.getIntent(this@HomeAccountUserActivity, applink)
                    startActivity(intent)
                }
            }
            override fun onSettingItemClicked(item: CommonDataView) {}
            override fun onSwitchChanged(item: CommonDataView, isActive: Boolean) {}
        })
        adapter.list = profile.members.items
        home_account_member_layout_rv?.adapter = adapter
        val layoutManager = SpanningLinearLayoutManager(home_account_member_layout_rv?.context, LinearLayoutManager.HORIZONTAL, false)

        val verticalDivider = ContextCompat.getDrawable(this, R.drawable.vertical_divider)
        val dividerItemDecoration = DividerItemDecoration(home_account_member_layout_rv.context,
                layoutManager.orientation)

        verticalDivider?.run {
            dividerItemDecoration.setDrawable(this)
        }

        home_account_member_layout_rv.addItemDecoration(dividerItemDecoration)
        home_account_member_layout_rv?.layoutManager = layoutManager
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        ImageUtils.loadImageCircleWithPlaceHolder(imageView.context, imageView, imageUrl)
    }

    override fun getComponent(): HomeAccountUserComponents {
        return DaggerHomeAccountUserComponents.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .homeAccountUserModules(HomeAccountUserModules(this))
                .homeAccountUserUsecaseModules(HomeAccountUserUsecaseModules())
                .homeAccountUserQueryModules(HomeAccountUserQueryModules())
                .sessionModule(SessionModule())
                .build()
    }

    override fun showToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.design.R.dimen.dp_4)
        }
    }

    override fun hideToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.design.R.dimen.dp_0)
        }
    }
}