package com.tokopedia.digital.home.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeRecommendationBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageRecommendationBannerModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemRecommendationBannerAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class RechargeHomepageRecommendationBannerViewHolder(
    private val binding: ViewRechargeHomeRecommendationBannerBinding,
    private val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageRecommendationBannerModel>(binding.root) {

    override fun bind(element: RechargeHomepageRecommendationBannerModel) {
        if (element.section.items.size >= GRID_COLUMN) {
            renderTitle(element)
            renderSeeAllButton(element)
            renderRecommendationBannerItems(element)
        } else {
            with(binding) {
                tvRechargeRecomBannerTitle.hide()
                tvRechargeRecomBannerSeeAll.hide()
                rvRechargeRecomBanner.hide()
            }
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun renderTitle(element: RechargeHomepageRecommendationBannerModel) {
        with(binding) {
            tvRechargeRecomBannerTitle.text = element.section.title
            tvRechargeRecomBannerTitle.show()
        }
    }

    private fun renderSeeAllButton(element: RechargeHomepageRecommendationBannerModel) {
        with(binding) {
            if (element.section.applink.isNotEmpty()) {
                tvRechargeRecomBannerSeeAll.text = element.section.textLink
                tvRechargeRecomBannerSeeAll.setOnClickListener {
                    listener.onRechargeBannerAllItemClicked(element.section)
                }
                tvRechargeRecomBannerSeeAll.show()
            } else {
                tvRechargeRecomBannerSeeAll.hide()
            }
        }
    }

    private fun renderRecommendationBannerItems(element: RechargeHomepageRecommendationBannerModel) {
        with(binding) {
            val adapter = RechargeItemRecommendationBannerAdapter(element.section.items, listener)
            val layoutManager = GridLayoutManager(root.context, GRID_COLUMN)

            rvRechargeRecomBanner.layoutManager = layoutManager
            rvRechargeRecomBanner.adapter = adapter
            rvRechargeRecomBanner.show()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_recommendation_banner

        private const val GRID_COLUMN = 2
    }
}