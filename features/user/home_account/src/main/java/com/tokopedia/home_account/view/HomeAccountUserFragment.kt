package com.tokopedia.home_account.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.di.DaggerHomeAccountUserComponents
import com.tokopedia.home_account.di.HomeAccountUserModules
import com.tokopedia.home_account.di.HomeAccountUserQueryModules
import com.tokopedia.home_account.di.HomeAccountUserUsecaseModules
import com.tokopedia.home_account.view.adapter.HomeAccountUserAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.sessioncommon.di.SessionModule
import kotlinx.android.synthetic.main.home_account_user_fragment.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserFragment : BaseDaggerFragment(), HomeAccountUserListener {

    var adapter: HomeAccountUserAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(HomeAccountUserViewModel::class.java) }

    override fun getScreenName(): String = "homeAccountUserFragment"

    override fun initInjector() {
        DaggerHomeAccountUserComponents.builder()
                .baseAppComponent(getComponent(BaseAppComponent::class.java))
                .homeAccountUserModules(HomeAccountUserModules(context!!))
                .homeAccountUserUsecaseModules(HomeAccountUserUsecaseModules())
                .homeAccountUserQueryModules(HomeAccountUserQueryModules())
                .sessionModule(SessionModule())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_account_user_fragment, container, false)
    }

    private fun setupObserver() {
        viewModel.profileData.observe(viewLifecycleOwner, Observer {
            addItem(it)
        })
        viewModel.settingData.observe(viewLifecycleOwner, Observer {
            addItem(it)
        })
        viewModel.settingApplication.observe(viewLifecycleOwner, Observer {
            addItem(it)
        })
        viewModel.aboutTokopedia.observe(viewLifecycleOwner, Observer {
            addItem(it)
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        adapter = HomeAccountUserAdapter(this)
        setupList()
        viewModel.getInitialData()
    }

    private fun setupList() {
        home_account_user_fragment_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        home_account_user_fragment_rv?.adapter = adapter
    }

    private fun addItem(item: Any) {
        adapter?.addItem(item)
    }

    override fun onEditProfileClicked() {
        goToApplink(ApplinkConstInternalGlobal.SETTING_PROFILE)
    }

    private fun goToApplink(applink: String) {
        val intent = RouteManager.getIntent(context, applink)
        startActivity(intent)
    }

    override fun onSettingItemClicked(applink: String) {
        if (applink.isNotEmpty()) {
            goToApplink(applink)
        }
    }

    override fun onSwitchChanged(item: CommonDataView, isActive: Boolean) {
        println("onSwitchChanged $isActive")
    }

    companion object {
        fun newInstance(bundle: Bundle?): Fragment {
            return HomeAccountUserFragment().apply {
                arguments = bundle
            }
        }
    }
}