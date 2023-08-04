package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseTopMainBannerBinding
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseTopMainBannerViewHolder(itemView: View) : AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_top_main_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
        private const val SECOND_BANNER_INDEX = 1
    }

    private val viewBinding: ItemShopHomeShowcaseTopMainBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.showcaseHeader.title
        val showcases = model.tabs.getOrNull(0)?.showcases ?: emptyList()

        setupViewAllIcon(showcases)
        setupMainBanner(showcases)
        if (showcases.size > 1) {
            setupShowCaseRecyclerView(showcases.subList(SECOND_BANNER_INDEX, showcases.size))
        }
    }

    private fun setupViewAllIcon(showcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>) {
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD
    }

    private fun setupMainBanner(showcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>) {
        val firstShowcase = showcases.getOrNull(0)

        firstShowcase?.let {
            viewBinding?.imgFirstBanner?.loadImage(firstShowcase.imageUrl)
            viewBinding?.tpgFirstBannerTitle?.text = firstShowcase.name
            viewBinding?.imgFirstBanner?.visible()
            viewBinding?.tpgFirstBannerTitle?.visible()
        }
    }

    private fun setupShowCaseRecyclerView(
        showcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>
    ) {
        val showCaseAdapter = ShopHomeShowCaseAdapter()

        val recyclerView = viewBinding?.recyclerView
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = showCaseAdapter
        }

        showCaseAdapter.submit(showcases)
    }

}
