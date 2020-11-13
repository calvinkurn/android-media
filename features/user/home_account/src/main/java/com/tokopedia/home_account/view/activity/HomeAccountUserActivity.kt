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

    override fun getNewFragment(): Fragment? {
        return HomeAccountUserFragment.newInstance(intent?.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
        updateTitle(getString(R.string.home_account_title))
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