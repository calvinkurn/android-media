package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.circular_view_pager.presentation.widgets.pageIndicator.CircularPageIndicator
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BANNER_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemBannerAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageBannerViewHolder(itemView: View,
                                       val listener: OnItemBindListener,
                                       private val isEmpty: Boolean = false)
    : AbstractViewHolder<RechargeHomepageBannerModel>(itemView),
        CircularListener {
    private lateinit var slidesList: List<RechargeHomepageSections.Item>
    private val circularViewPager: CircularViewPager = itemView.findViewById(R.id.circular_view_pager)
    private val indicatorView: CircularPageIndicator = itemView.findViewById(R.id.indicator_banner)
    private val seeAllPromo: TextView = itemView.findViewById(R.id.see_all_promo)
    private val adapter = RechargeItemBannerAdapter(listOf(), this)

    override fun bind(element: RechargeHomepageBannerModel) {
        if (!isEmpty) {
            slidesList = element.section.items
            initSeeAllPromo()
            initBanner(slidesList.map { CircularModel(it.id, it.mediaUrl) })
        }
    }

    private fun initSeeAllPromo(){
        seeAllPromo.setOnClickListener { onPromoAllClick() }
    }

    private fun initBanner(list: List<CircularModel>){
        circularViewPager.setIndicatorPageChangeListener(object: CircularViewPager.IndicatorPageChangeListener{
            override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                indicatorView.animatePageSelected(newIndicatorPosition)
            }
        })

        circularViewPager.setPageChangeListener(object: CircularPageChangeListener {
            override fun onPageScrolled(position: Int) {
                onPromoScrolled(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        circularViewPager.setAdapter(adapter)
        circularViewPager.setItemList(list)
        indicatorView.createIndicators(circularViewPager.indicatorCount, circularViewPager.indicatorPosition)
    }

    override fun onClick(position: Int) {
        if (::slidesList.isInitialized && slidesList.size > position) {
            listener.onRechargeSectionItemClicked(slidesList[position], position, BANNER_CLICK)
        }
    }

    private fun onPromoScrolled(position: Int) {
        if (::slidesList.isInitialized) {
            listener.onRechargeSectionItemImpression(listOf(slidesList[position]), BANNER_IMPRESSION)
        }
    }

    private fun onPromoAllClick() {
        listener.onRechargeBannerAllItemClicked()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_banner
        // TODO: Add empty banner layout
        @LayoutRes
        val LAYOUT_EMPTY = R.layout.view_recharge_home_banner
    }
}
