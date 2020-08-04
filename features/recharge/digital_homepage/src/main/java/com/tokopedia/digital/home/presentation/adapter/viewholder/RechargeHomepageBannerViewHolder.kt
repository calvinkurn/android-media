package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemBannerAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.view_recharge_home_banner.view.*

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageBannerViewHolder(itemView: View,
                                       val listener: OnItemBindListener)
    : AbstractViewHolder<RechargeHomepageBannerModel>(itemView),
        CircularListener {
    private lateinit var slidesList: List<RechargeHomepageSections.Item>
    private val adapter = RechargeItemBannerAdapter(listOf(), this)

    override fun bind(element: RechargeHomepageBannerModel) {
        slidesList = element.section.items
        if (slidesList.isNotEmpty()) {
            initSeeAllPromo(element.section)
            initBanner(element.section)
        } else {
            // TODO: Show shimmering
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun initSeeAllPromo(section: RechargeHomepageSections.Section){
        itemView.see_all_promo.setOnClickListener { onPromoAllClick(section) }
    }

    private fun initBanner(section: RechargeHomepageSections.Section){
        val list = slidesList.map { CircularModel(it.id, it.mediaUrl) }
        with (itemView) {
            circular_view_pager.setIndicatorPageChangeListener(object : CircularViewPager.IndicatorPageChangeListener {
                override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                    indicator_banner.animatePageSelected(newIndicatorPosition)
                }
            })

            circular_view_pager.setPageChangeListener(object : CircularPageChangeListener {
                override fun onPageScrolled(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            circular_view_pager.setAdapter(adapter)
            circular_view_pager.setItemList(list)
            indicator_banner.createIndicators(circular_view_pager.indicatorCount, circular_view_pager.indicatorPosition)
            addOnImpressionListener(section) {
                listener.onRechargeBannerImpression(section)
            }
        }
    }

    override fun onClick(position: Int) {
        if (::slidesList.isInitialized && slidesList.size > position) {
            listener.onRechargeSectionItemClicked(slidesList[position])
        }
    }

    private fun onPromoAllClick(section: RechargeHomepageSections.Section) {
        listener.onRechargeBannerAllItemClicked(section)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_banner
    }
}
