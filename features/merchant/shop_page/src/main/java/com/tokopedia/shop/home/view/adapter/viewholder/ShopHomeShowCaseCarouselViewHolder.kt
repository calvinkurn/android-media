package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseCarouselBannerBinding
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseCarouselViewHolder(itemView: View) :
    AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_carousel_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
    }

    private val viewBinding: ItemShopHomeShowcaseCarouselBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.showcaseHeader.title
        viewBinding?.tpgTitle?.isVisible = model.showcaseHeader.title.isNotEmpty() && model.tabs.isNotEmpty()

        val showcases = model.tabs.getOrNull(0)?.showcases ?: emptyList()
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD

        setupShowCaseRecyclerView(showcases)
    }

    private fun setupShowCaseRecyclerView(
        showcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>
    ) {
        val showCaseAdapter = ShopHomeShowCaseAdapter()

        val recyclerView = viewBinding?.recyclerView
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(
                recyclerView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = showCaseAdapter
        }

        showCaseAdapter.submit(showcases)
    }
}
