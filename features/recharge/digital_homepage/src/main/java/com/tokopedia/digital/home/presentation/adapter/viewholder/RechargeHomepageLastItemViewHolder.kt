package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ContentRechargeHomepageLastItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageProductCardCustomLastItemModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.media.loader.loadImage

class RechargeHomepageLastItemViewHolder(
    private val binding: ContentRechargeHomepageLastItemBinding,
    private val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageProductCardCustomLastItemModel.LastItem>(binding.root) {

    override fun bind(element: RechargeHomepageProductCardCustomLastItemModel.LastItem) {
        renderBackground(element)
        applyCarousel()

        binding.root.setOnClickListener {
            listener.onRechargeBannerAllItemClicked(element.section)
        }
    }

    private fun renderBackground(element: RechargeHomepageProductCardCustomLastItemModel.LastItem) {
        with(binding) {
            rechargeHomepageLastItemBackground.loadImage(element.section.mediaUrl)
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