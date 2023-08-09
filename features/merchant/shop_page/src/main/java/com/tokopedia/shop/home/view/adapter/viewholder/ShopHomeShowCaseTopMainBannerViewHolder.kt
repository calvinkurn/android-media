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
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseTopMainBannerViewHolder(
    itemView: View, private val
    listener: ShopHomeShowcaseListener
) : AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_top_main_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
        private const val SECOND_SHOWCASE_INDEX = 1
        private const val TWELVE_SHOWCASE_INDEX = 12
    }

    private val viewBinding: ItemShopHomeShowcaseTopMainBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.showcaseHeader.title
        viewBinding?.tpgTitle?.isVisible =
            model.showcaseHeader.title.isNotEmpty() && model.tabs.isNotEmpty()
        viewBinding?.iconChevron?.setOnClickListener { listener.onViewAllShowcaseClick(model.showcaseHeader) }

        val showcases = model.tabs.getOrNull(0)?.showcases ?: emptyList()

        setupViewAllIcon(showcases)
        setupMainBanner(showcases)
        setupShowCaseRecyclerView(showcases)
    }

    private fun setupViewAllIcon(showcases: List<ShopHomeShowcaseUiModel.Tab.Showcase>) {
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD
    }

    private fun setupMainBanner(showcases: List<ShopHomeShowcaseUiModel.Tab.Showcase>) {
        val firstShowcase = showcases.getOrNull(0)

        firstShowcase?.let {
            viewBinding?.imgFirstBanner?.loadImage(firstShowcase.imageUrl)
            viewBinding?.tpgFirstBannerTitle?.text = firstShowcase.name
            viewBinding?.imgFirstBanner?.visible()
            viewBinding?.tpgFirstBannerTitle?.visible()

            viewBinding?.imgFirstBanner?.setOnClickListener { listener.onShowcaseClick(firstShowcase) }
            viewBinding?.tpgFirstBannerTitle?.setOnClickListener { listener.onShowcaseClick(firstShowcase)  }
        }
    }

    private fun setupShowCaseRecyclerView(
        showcases: List<ShopHomeShowcaseUiModel.Tab.Showcase>
    ) {
        val filteredShowcases =
            showcases.filterIndexed { index, _ -> index in SECOND_SHOWCASE_INDEX..TWELVE_SHOWCASE_INDEX }

        val showCaseAdapter = ShopHomeShowCaseAdapter(ShopHomeShowcaseUiModel.WidgetStyle.ROUNDED_CORNER, listener)

        val recyclerView = viewBinding?.recyclerView
        recyclerView?.apply {
            layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = showCaseAdapter
        }

        showCaseAdapter.submit(filteredShowcases)
    }

}
