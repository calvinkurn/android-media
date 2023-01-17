package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.RechargeItemBannerAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageBannerViewHolder(itemView: View,
                                       val listener: RechargeHomepageItemListener)
    : AbstractViewHolder<RechargeHomepageBannerModel>(itemView),
    CircularListener {
    private lateinit var slidesList: List<RechargeHomepageSections.Item>
    private val adapter = RechargeItemBannerAdapter(listOf(), this)

    override fun bind(element: RechargeHomepageBannerModel) {
        val bind = ViewRechargeHomeBannerBinding.bind(itemView)
        slidesList = element.section.items
        if (slidesList.isNotEmpty()) {
            initSeeAllPromo(bind,element.section)
            initBanner(bind, element.section)
        } else {
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun initSeeAllPromo(
        bind: ViewRechargeHomeBannerBinding,
        section: RechargeHomepageSections.Section
    ) {
        with(bind){
            if (section.textLink.isNotEmpty()) {
                seeAllPromo.show()
                seeAllPromo.text = section.textLink
                seeAllPromo.setOnClickListener { onPromoAllClick(section) }
            } else {
                seeAllPromo.hide()
            }
        }
        
    }

    private fun initBanner(
        bind: ViewRechargeHomeBannerBinding,
        section: RechargeHomepageSections.Section
    ){
        val list = slidesList.map { CircularModel(it.id.toIntSafely(), it.mediaUrl) }
        with (bind) {
            circularViewPager.setIndicatorPageChangeListener(object : CircularViewPager.IndicatorPageChangeListener {
                override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                    indicatorBanner.animatePageSelected(newIndicatorPosition)
                }
            })

            circularViewPager.setPageChangeListener(object : CircularPageChangeListener {
                override fun onPageScrolled(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            circularViewPager.setAdapter(adapter)
            circularViewPager.setItemList(list)
            indicatorBanner.createIndicators(circularViewPager.indicatorCount, circularViewPager.indicatorPosition)
            root.addOnImpressionListener(section) {
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
