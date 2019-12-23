package com.tokopedia.home.beranda.presentation.view.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.TabBusinessViewPagerAdapter
import com.tokopedia.home.beranda.presentation.view.viewmodel.TabBusinessViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_parent_business_unit.*
import javax.inject.Inject

class TabBusinessFragment : BaseDaggerFragment(), ViewPager.OnPageChangeListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: TabBusinessViewModel
    lateinit var adapter: TabBusinessViewPagerAdapter
    private var positionWidget: Int = 0

    override fun getScreenName(): String {
        return TabBusinessFragment::class.java.simpleName
    }

    companion object {
        const val ITEM_POSITION = "ITEM_POSITION"

        fun newInstance(position: Int) : Fragment {
            val fragment = TabBusinessFragment()
            val bundle = Bundle()
            bundle.putInt(ITEM_POSITION, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjector() {
        val component = DaggerBerandaComponent.builder().baseAppComponent((activity!!.application as BaseMainApplication)
                .baseAppComponent)
                .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            positionWidget = it?.getInt(ITEM_POSITION)!!
        }
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(TabBusinessViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_parent_business_unit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        errorView.visibility = View.GONE
        container.visibility = View.GONE
        temporayPlaceHolders.visibility = View.VISIBLE

        getTabBusinessUnit()

        buttonReload.setOnClickListener {
            errorView.visibility = View.GONE
            container.visibility = View.GONE
            temporayPlaceHolders.visibility = View.VISIBLE
            getTabBusinessUnit()
        }

        viewModel.homeWidget.observe(
                viewLifecycleOwner,
                Observer { onSuccessGetTabBusinessWidget(it) })

        viewModel.homeWidgetErrorAction.observe(
                viewLifecycleOwner,
                Observer { onErrorGetTabBusinessWidget(it.throwable) }
        )
    }

    private fun getTabBusinessUnit() {
        temporayPlaceHolders.visibility = View.VISIBLE
        container.visibility = View.GONE

        viewModel.getTabList(getRawQuery())
    }

    private fun getRawQuery(): String {
        return GraphqlHelper.loadRawString(activity?.resources, R.raw.query_tab_business_widget)
    }

    private fun onSuccessGetTabBusinessWidget(homeWidget: HomeWidget) {
        if (homeWidget.tabBusinessList.isNotEmpty()) {
            errorView.visibility = View.GONE
            container.visibility = View.VISIBLE
            temporayPlaceHolders.visibility = View.GONE
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

            adapter = TabBusinessViewPagerAdapter(childFragmentManager, homeWidget.tabBusinessList, homeWidget.widgetHeader.backColor, positionWidget)
            viewPager.adapter = adapter
            viewPager.offscreenPageLimit = homeWidget.tabBusinessList.size
            viewPager.removeOnPageChangeListener(this)
            viewPager.addOnPageChangeListener(this)
            viewPager.setCanScrollHorizontal(false)
            tabLayout.setupWithViewPager(null)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private fun onErrorGetTabBusinessWidget(throwable: Throwable) {
        throwable.printStackTrace()
        errorView.visibility = View.VISIBLE
        container.visibility = View.GONE
        temporayPlaceHolders.visibility = View.GONE
    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(tabPosition: Int) {
        val tab = viewModel.getTabList(tabPosition)
        tab?.let { tab->
            HomePageTracking.eventClickTabHomeWidget(activity, tab.name.toString().toLowerCase())
            adapter.notifyDataSetChanged()
        }
    }
}
