package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.presentation.adapter.PayLaterPagerAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.paylater.presentation.widget.PayLaterSignupBottomSheet
import kotlinx.android.synthetic.main.fragment_paylater.*
import java.text.FieldPosition
import javax.inject.Inject


class PayLaterFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    override fun getScreenName(): String {
        return "PayLater & Cicilan"
    }

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paylater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderTabAndViewPager()
        initListeners()
        payLaterViewModel.getPayLaterData()
        payLaterViewModel.getPayLaterApplicationStatus()
    }

    private fun initListeners() {
        paylaterDaftarWidget.setOnClickListener {
            PayLaterSignupBottomSheet.show(Bundle(), childFragmentManager)
        }
        paylaterTabLayout.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position != SIMULATION_TAB_INDEX)
                    daftarGroup.gone()
                else daftarGroup.visible()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

        })
        payLaterViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position!= 0) daftarGroup.gone()
                else daftarGroup.visible()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    private fun renderTabAndViewPager() {
        payLaterViewPager.adapter = getViewPagerAdapter()
        paylaterTabLayout?.run {
            setupWithViewPager(payLaterViewPager)
            addNewTab(context.getString(R.string.payLater_simulation_tab_title))
            addNewTab(context.getString(R.string.payLater_offer_details_tab_title))
        }
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list = mutableListOf<Fragment>()
        list.add(SimulationFragment.newInstance())
        list.add(PayLaterOffersFragment.newInstance())
        val pagerAdapter = PayLaterPagerAdapter(context!!, childFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }

    companion object {
        const val SIMULATION_TAB_INDEX = 0

        @JvmStatic
        fun newInstance() =
                PayLaterFragment()
    }
}