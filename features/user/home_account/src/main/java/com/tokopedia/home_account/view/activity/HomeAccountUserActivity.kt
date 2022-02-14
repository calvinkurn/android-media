package com.tokopedia.home_account.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.R
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.sessioncommon.di.SessionModule

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
open class HomeAccountUserActivity: BaseSimpleActivity(), HasComponent<HomeAccountUserComponents>, onAppBarCollapseListener {

    private var homeAccountUserComponents: HomeAccountUserComponents? = null

    lateinit var fragment: HomeAccountUserFragment

    override fun getTagFragment(): String = TAG

    override fun getNewFragment(): Fragment? {
        fragment = createHomeAccountUserFragment() as HomeAccountUserFragment
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
        updateTitle(getString(R.string.home_account_title))
    }

    override fun getComponent(): HomeAccountUserComponents = homeAccountUserComponents ?: initializeHomeAccountUserComponents()

    protected open fun createHomeAccountUserFragment(): Fragment =
        HomeAccountUserFragment.newInstance(intent?.extras)

    protected open fun initializeHomeAccountUserComponents(): HomeAccountUserComponents {
        return DaggerHomeAccountUserComponents.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .homeAccountUserModules(HomeAccountUserModules(this))
            .homeAccountUserUsecaseModules(HomeAccountUserUsecaseModules())
            .homeAccountUserQueryModules(HomeAccountUserQueryModules())
            .sessionModule(SessionModule())
            .build().also {
                homeAccountUserComponents = it
            }
    }


    override fun showToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
        }
    }

    override fun hideToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
        }
    }

    companion object {
        val TAG = HomeAccountUserActivity::class.java.name
    }
}