package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.adapter.DigitalTelcoProductTabAdapter
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalProductSubMenu
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem
import com.tokopedia.topupbills.telco.view.model.DigitalTelcoExtraParam
import com.tokopedia.topupbills.telco.view.widget.DigitalSubMenuWidget
import kotlinx.android.synthetic.main.fragment_digital_telco.*

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoFragment : BaseDaggerFragment() {

    var posCurrentTabExtraParam = DigitalSubMenuWidget.HEADER_LEFT

    override fun getScreenName(): String {
        return getString(R.string.digital_track_title_page)
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderSubMenu()
    }

    fun renderSubMenu() {
        val listMenuTab = mutableListOf<DigitalTabTelcoItem>()
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM_TELCO) as DigitalTelcoExtraParam
            var prepaidExtraParam = DigitalTelcoExtraParam()
            var postpaidExtraParam = DigitalTelcoExtraParam()

            if (digitalTelcoExtraParam.menuId.toInt() == TelcoComponentType.TELCO_PREPAID) {
                prepaidExtraParam = digitalTelcoExtraParam
                posCurrentTabExtraParam = DigitalSubMenuWidget.HEADER_LEFT
            } else if (digitalTelcoExtraParam.menuId.toInt() == TelcoComponentType.TELCO_POSTPAID) {
                postpaidExtraParam = digitalTelcoExtraParam
                posCurrentTabExtraParam = DigitalSubMenuWidget.HEADER_RIGHT
            }
            listMenuTab.add(DigitalTabTelcoItem(DigitalTelcoPrepaidFragment.newInstance(
                    prepaidExtraParam), ""))
            listMenuTab.add(DigitalTabTelcoItem(DigitalTelcoPostpaidFragment.newInstance(
                    postpaidExtraParam), ""))
            val pagerAdapter = DigitalTelcoProductTabAdapter(listMenuTab, childFragmentManager)
            menu_view_pager.adapter = pagerAdapter
            menu_view_pager.currentItem = posCurrentTabExtraParam
        }

        val list = mutableListOf<DigitalProductSubMenu>()
        list.add(DigitalProductSubMenu(TelcoComponentType.TELCO_PREPAID, TelcoComponentName.TELCO_PREPAID))
        list.add(DigitalProductSubMenu(TelcoComponentType.TELCO_POSTPAID, TelcoComponentName.TELCO_POSTPAID))

        header_view.setHeaderActive(list.get(posCurrentTabExtraParam), posCurrentTabExtraParam)
        header_view.setListener(object : DigitalSubMenuWidget.ActionListener {
            override fun onClickSubMenu(subMenu: DigitalProductSubMenu) {
                if (subMenu.id == TelcoComponentType.TELCO_PREPAID) {
                    menu_view_pager.currentItem = 0
                } else {
                    menu_view_pager.currentItem = 1
                }
            }
        })
        header_view.setHeader(list)

        menu_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    header_view.headerLeftActive(list.get(DigitalSubMenuWidget.HEADER_LEFT))
                } else {
                    header_view.headerRightActive(list.get(DigitalSubMenuWidget.HEADER_RIGHT))
                }
            }
        })
    }

    fun onBackPressed() {
        val currentFragment = (menu_view_pager.adapter as DigitalTelcoProductTabAdapter).getItem(menu_view_pager.currentItem)
        (currentFragment as DigitalBaseTelcoFragment).onBackPressed()
    }

    companion object {

        const val EXTRA_PARAM_TELCO = "extra_param_telco"

        fun newInstance(digitalTelcoExtraParam: DigitalTelcoExtraParam): Fragment {
            val fragment = DigitalTelcoFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_TELCO, digitalTelcoExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }

}
