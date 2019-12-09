package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.common.DigitalTopupEventTracking
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalProductSubMenu
import com.tokopedia.topupbills.telco.view.widget.DigitalSubMenuWidget
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_digital_telco.*
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoFragment : BaseDaggerFragment() {

    private var posCurrentTabExtraParam = DigitalSubMenuWidget.HEADER_LEFT
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics
    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics
    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_telco, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderSubMenu()
    }

    fun renderSubMenu() {
        val listMenuTab = mutableListOf<TopupBillsTabItem>()
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM_TELCO) as TopupBillsExtraParam
            var prepaidExtraParam = TopupBillsExtraParam()
            var postpaidExtraParam = TopupBillsExtraParam()
            var labelPage = TelcoComponentName.TELCO_PREPAID

            digitalTelcoExtraParam.categoryId.toIntOrNull()?.let {
                rechargeAnalytics.eventDigitalCategoryScreenLaunch(topupAnalytics.getCategoryName(it),
                        digitalTelcoExtraParam.categoryId)
                rechargeAnalytics.trackVisitRechargePushEventRecommendation(it)
            }

            if (digitalTelcoExtraParam.menuId.toInt() == TelcoComponentType.TELCO_PREPAID) {
                prepaidExtraParam = digitalTelcoExtraParam
                posCurrentTabExtraParam = DigitalSubMenuWidget.HEADER_LEFT
            } else if (digitalTelcoExtraParam.menuId.toInt() == TelcoComponentType.TELCO_POSTPAID) {
                postpaidExtraParam = digitalTelcoExtraParam
                posCurrentTabExtraParam = DigitalSubMenuWidget.HEADER_RIGHT
                labelPage = TelcoComponentName.TELCO_POSTPAID
            }
            listMenuTab.add(TopupBillsTabItem(DigitalTelcoPrepaidFragment.newInstance(
                    prepaidExtraParam), ""))
            listMenuTab.add(TopupBillsTabItem(DigitalTelcoPostpaidFragment.newInstance(
                    postpaidExtraParam), ""))
            val pagerAdapter = TopupBillsProductTabAdapter(listMenuTab, childFragmentManager)
            menu_view_pager.adapter = pagerAdapter
            menu_view_pager.currentItem = posCurrentTabExtraParam

            rechargeAnalytics.eventOpenScreen(userSession.isLoggedIn, labelPage,
                    digitalTelcoExtraParam.categoryId)
        }

        val list = mutableListOf<DigitalProductSubMenu>()
        list.add(DigitalProductSubMenu(TelcoComponentType.TELCO_PREPAID, TelcoComponentName.TELCO_PREPAID))
        list.add(DigitalProductSubMenu(TelcoComponentType.TELCO_POSTPAID, TelcoComponentName.TELCO_POSTPAID))

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
        configHeaderActive(posCurrentTabExtraParam, list)

        menu_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                configHeaderActive(position, list)
                topupAnalytics.eventClickTelcoTab(list[position].label)
            }
        })
    }

    fun configHeaderActive(position: Int, list: List<DigitalProductSubMenu>) {
        if (position == 0) {
            header_view.headerLeftActive(list[DigitalSubMenuWidget.HEADER_LEFT])
            topupAnalytics.trackScreenNameTelco(DigitalTopupEventTracking.Screen.DIGITAL_TELCO_PREPAID)
        } else {
            header_view.headerRightActive(list[DigitalSubMenuWidget.HEADER_RIGHT])
            topupAnalytics.trackScreenNameTelco(DigitalTopupEventTracking.Screen.DIGITAL_TELCO_POSTPAID)
        }
    }

    fun onBackPressed() {
        val currentFragment = (menu_view_pager.adapter as TopupBillsProductTabAdapter).getRegisteredFragment(menu_view_pager.currentItem)
        (currentFragment as DigitalBaseTelcoFragment).onBackPressed()
    }

    companion object {

        const val EXTRA_PARAM_TELCO = "extra_param_telco"

        fun newInstance(topupBillsExtraParam: TopupBillsExtraParam): Fragment {
            val fragment = DigitalTelcoFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM_TELCO, topupBillsExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }

}
