package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ContentRechargeHomepageLastItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageProductCardCustomLastItemModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.loader.loadImage

class RechargeHomepageLastItemViewHolder(
    private val binding: ContentRechargeHomepageLastItemBinding,
    private val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageProductCardCustomLastItemModel.LastItem>(binding.root) {

    override fun bind(element: RechargeHomepageProductCardCustomLastItemModel.LastItem) {
        renderView(element)
        applyCarousel()

        binding.root.setOnClickListener {
            listener.onRechargeBannerAllItemClicked(element.section)
        }
    }

    private fun renderView(element: RechargeHomepageProductCardCustomLastItemModel.LastItem) {
        with(binding) {
            cardViewRechargeHomepageLastItem.title = getString(R.string.recharge_homepage_last_item_title)
            cardViewRechargeHomepageLastItem.description = getString(R.string.recharge_homepage_last_item_subtitle)
            cardViewRechargeHomepageLastItem.ctaIconView.setImage(newIconId = IconUnify.CHEVRON_RIGHT)
            cardViewRechargeHomepageLastItem.setCta(getString(R.string.recharge_home_banner_see_all_label)) {
                listener.onRechargeBannerAllItemClicked(element.section)
            }
        }
    }

    private fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        with(binding) {
            val layoutParams = cardViewRechargeHomepageLastItem.layoutParams
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            cardViewRechargeHomepageLastItem.layoutParams = layoutParams
        }
    }

    companion object {
        val LAYOUT = R.layout.content_recharge_homepage_last_item
    }
}