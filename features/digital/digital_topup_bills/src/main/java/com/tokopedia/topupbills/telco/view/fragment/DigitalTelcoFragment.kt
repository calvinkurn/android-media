package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.adapter.DigitalTelcoProductTabAdapter
import com.tokopedia.topupbills.telco.view.model.DigitalProductSubMenu
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem
import com.tokopedia.topupbills.telco.view.widget.DigitalSubMenuWidget

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoFragment : BaseDaggerFragment() {

    private lateinit var headerView: DigitalSubMenuWidget
    private lateinit var viewPager: ViewPager

    override fun getScreenName(): String {
        return DigitalTelcoFragment::class.java.simpleName
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco, container, false)
        headerView = view.findViewById(R.id.header_view)
        viewPager = view.findViewById(R.id.menu_view_pager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderSubMenu()
    }

    fun renderSubMenu() {
        val listMenuTab = mutableListOf<DigitalTabTelcoItem>()
        listMenuTab.add(DigitalTabTelcoItem(DigitalTelcoPrepaidFragment.newInstance(), ""))
        listMenuTab.add(DigitalTabTelcoItem(DigitalTelcoPostpaidFragment.newInstance(), ""))
        val pagerAdapter = DigitalTelcoProductTabAdapter(listMenuTab, childFragmentManager)
        viewPager.adapter = pagerAdapter

        val list = mutableListOf<DigitalProductSubMenu>()
        list.add(DigitalProductSubMenu(TelcoComponentType.TELCO_PREPAID, TelcoComponentName.TELCO_PREPAID))
        list.add(DigitalProductSubMenu(TelcoComponentType.TELCO_POSTPAID, TelcoComponentName.TELCO_POSTPAID))
        headerView.setHeaderActive(DigitalSubMenuWidget.HEADER_LEFT)
        headerView.setListener(object : DigitalSubMenuWidget.ActionListener {
            override fun onClickSubMenu(subMenu: DigitalProductSubMenu) {
                if (subMenu.id == TelcoComponentType.TELCO_PREPAID) {
                    viewPager.setCurrentItem(0)
                } else {
                    viewPager.setCurrentItem(1)
                }
            }
        })
        headerView.setHeader(list)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    headerView.headerLeftActive(list.get(DigitalSubMenuWidget.HEADER_LEFT))
                } else {
                    headerView.headerRightActive(list.get(DigitalSubMenuWidget.HEADER_RIGHT))
                }
            }
        })
    }

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoFragment()
            return fragment
        }
    }

}
