package com.tokopedia.digital.widget.view.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.digital.R
import com.tokopedia.digital.product.di.DigitalProductComponentInstance
import com.tokopedia.digital.product.view.compoundview.DigitalWrapContentViewPager
import com.tokopedia.digital.widget.view.adapter.RechargeViewPagerAdapter
import com.tokopedia.digital.widget.view.model.category.Category
import com.tokopedia.digital.widget.view.presenter.DigitalWidgetContract
import com.tokopedia.digital.widget.view.presenter.DigitalWidgetPresenter
import com.tokopedia.user.session.UserSession
import java.util.*
import javax.inject.Inject

/**
 * Created by Rizky on 15/11/18.
 */
class DigitalWidgetFragment: BaseDaggerFragment(), DigitalWidgetContract.View {
    val WIDGET_RECHARGE_TAB_LAST_SELECTED = "WIDGET_RECHARGE_TAB_LAST_SELECTED"

    private lateinit var cacheHandler: LocalCacheHandler
    private var rechargeViewPagerAdapter: RechargeViewPagerAdapter? = null

    @Inject
    lateinit var userSession: UserSession

    @Inject
    lateinit var digitalWidgetPresenter: DigitalWidgetPresenter

    private lateinit var tab_layout_widget: TabLayout
    private lateinit var view_pager_widget: DigitalWrapContentViewPager
    private lateinit var container: LinearLayout
    private lateinit var pulsa_place_holders: RelativeLayout
    private lateinit var error_view: LinearLayout
    private lateinit var text_error_message: TextView
    private lateinit var button_try_again: Button

    companion object {

        fun newInstance(): DigitalWidgetFragment {
            val fragment = DigitalWidgetFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.fragment_digital_widget, container, false)

        tab_layout_widget = rootview.findViewById(R.id.tab_layout_widget)
        view_pager_widget = rootview.findViewById(R.id.view_pager_widget)
        this.container = rootview.findViewById(R.id.container)
        pulsa_place_holders = rootview.findViewById(R.id.pulsa_place_holders)
        error_view = rootview.findViewById(R.id.error_view)
        text_error_message = rootview.findViewById(R.id.text_error_message)
        button_try_again = rootview.findViewById(R.id.button_try_again)

        button_try_again.setOnClickListener {
            error_view.visibility = View.GONE
            this.container.visibility = View.GONE
            pulsa_place_holders.visibility = View.VISIBLE

            digitalWidgetPresenter.fetchDataRechargeCategory()
        }
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        digitalWidgetPresenter.attachView(this)
        digitalWidgetPresenter.fetchDataRechargeCategory()
    }

    override fun getScreenName(): String {
        return DigitalWidgetFragment::class.java.simpleName
    }

    override fun initInjector() {
        cacheHandler = LocalCacheHandler(activity, WIDGET_RECHARGE_TAB_LAST_SELECTED)
        DigitalProductComponentInstance.getDigitalProductComponent(activity!!.application)
                .inject(this)
    }

    override fun renderDataRechargeCategory(rechargeCategory: List<Category>) {
        val newRechargePositions = ArrayList<Int>()
        if (rechargeCategory.isEmpty()) {
            return
        }

        pulsa_place_holders.visibility = View.GONE
        showDigitalWidget()
        tab_layout_widget.removeAllTabs()
        addChildTabLayout(rechargeCategory, newRechargePositions)
        getPositionFlagNewRecharge(newRechargePositions)
        tab_layout_widget.tabMode = TabLayout.MODE_SCROLLABLE

        if (rechargeViewPagerAdapter == null) {
            rechargeViewPagerAdapter = RechargeViewPagerAdapter(childFragmentManager, rechargeCategory.toMutableList())
            view_pager_widget.adapter = rechargeViewPagerAdapter
        } else {
            rechargeViewPagerAdapter?.addFragments(rechargeCategory)
        }
        addTabLayoutListener(rechargeViewPagerAdapter)
        view_pager_widget.offscreenPageLimit = rechargeCategory.size
        setTabSelected(rechargeCategory.size)
        rechargeViewPagerAdapter?.notifyDataSetChanged()
    }

    override fun failedRenderDataRechargeCategory() {
    }

    override fun renderErrorNetwork(resId: Int) {
        container.visibility = View.GONE
        pulsa_place_holders.visibility = View.GONE
        error_view.visibility = View.VISIBLE

        text_error_message.text = getString(resId)
    }

    private fun showDigitalWidget() {
        container.visibility = View.VISIBLE
        (tab_layout_widget.parent as LinearLayout).visibility = View.VISIBLE
    }

    private fun addChildTabLayout(rechargeCategory: List<Category>, newRechargePositions: MutableList<Int>) {
        for (i in rechargeCategory.indices) {
            val category = rechargeCategory[i]
            val tab = tab_layout_widget.newTab()
            tab.text = category.attributes.name
            tab_layout_widget.addTab(tab)
            if (category.attributes.isNew) {
                newRechargePositions.add(i)
            }
        }
    }

    private fun getPositionFlagNewRecharge(newRechargePositions: List<Int>) {
        for (positionRecharge in newRechargePositions) {
            val tv = ((tab_layout_widget.getChildAt(0) as LinearLayout)
                    .getChildAt(positionRecharge) as LinearLayout).getChildAt(1) as TextView
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    context?.resources?.let { ResourcesCompat.getDrawable(it, R.drawable.ic_digital_recharge_circle, null) }, null
            )
        }
    }

    private fun addTabLayoutListener(rechargeViewPagerAdapter: RechargeViewPagerAdapter?) {
        view_pager_widget.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout_widget))

        tab_layout_widget.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager_widget.setCurrentItem(tab.position, false)
                rechargeViewPagerAdapter?.notifyDataSetChanged()
                if (tab.text != null) {
//                    UnifyTracking.eventClickWidgetBar(tab.text!!.toString())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val focus = com.tokopedia.abstraction.common.utils.view.CommonUtils.getActivity(context).currentFocus
                if (focus != null) {
                    hideKeyboard(focus)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val focus = com.tokopedia.abstraction.common.utils.view.CommonUtils.getActivity(context).currentFocus
                if (focus != null) {
                    hideKeyboard(focus)
                }
            }
        })
    }

    private fun setTabSelected(categorySize: Int) {
        val positionTab = cacheHandler.getInt(WIDGET_RECHARGE_TAB_LAST_SELECTED)!!
        if (positionTab != -1 && positionTab < categorySize) {
            view_pager_widget.postDelayed({ view_pager_widget.currentItem = positionTab }, 300)
            tab_layout_widget.getTabAt(positionTab)!!.select()
        } else {
            view_pager_widget.currentItem = 0
        }
    }

    private fun hideKeyboard(v: View) {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    override fun onDestroy() {
        digitalWidgetPresenter.detachView()
        super.onDestroy()
    }

}