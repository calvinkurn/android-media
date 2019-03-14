package com.tokopedia.home.beranda.presentation.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business.TabBusinessViewPagerAdapter
import com.tokopedia.home.beranda.presentation.view.viewmodel.TabBusinessViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_parent_business_unit.*
import javax.inject.Inject

class TabBusinessFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: TabBusinessViewModel
    lateinit var adapter: TabBusinessViewPagerAdapter

    override fun getScreenName(): String {
        return TabBusinessFragment::class.java.simpleName
    }

    override fun initInjector() {
        val component = DaggerBerandaComponent.builder().baseAppComponent((activity!!.application as BaseMainApplication)
                .baseAppComponent)
                .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(TabBusinessViewModel::class.java)
        }
    }

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_parent_business_unit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getTabBusinessUnit()
        buttonTryAgain.setOnClickListener {
            errorView.visibility = View.GONE
            container.visibility = View.GONE
            temporayPlaceHolders.visibility = View.VISIBLE
            getTabBusinessUnit()
        }
    }

    private fun getTabBusinessUnit() {
        viewModel.getTabBusinessUnit(getRawQuery())
    }

    private fun getRawQuery(): String {
        return GraphqlHelper.loadRawString(activity?.resources, R.raw.query_tab_business_widget)
    }

    override fun onDestroy() {
        viewModel.clearJob()
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.homeWidget.observe(
                this, Observer { when (it) {
            is Success -> onSuccessGetTabBusinessWidget(it.data)
            is Fail -> onErrorGetTabBusinessWidget(it.throwable)
        } })
    }

    private fun onSuccessGetTabBusinessWidget(homeWidget: HomeWidget) {
        errorView.visibility = View.GONE
        container.visibility = View.VISIBLE
        temporayPlaceHolders.visibility = View.GONE

        tabLayout.removeAllTabs()
        addChildTabLayout(homeWidget.tabBusinessList)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        adapter = TabBusinessViewPagerAdapter(childFragmentManager, homeWidget.tabBusinessList)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = homeWidget.tabBusinessList.size
    }

    private fun addChildTabLayout(list: List<HomeWidget.TabItem>) {
        list.forEach {
            val tab = tabLayout.newTab()
            tab.text = it.name
            tabLayout.addTab(tab)
        }
    }

    private fun onErrorGetTabBusinessWidget(throwable: Throwable) {
        errorView.visibility = View.VISIBLE
        container.visibility = View.GONE
        temporayPlaceHolders.visibility = View.GONE
    }

}
