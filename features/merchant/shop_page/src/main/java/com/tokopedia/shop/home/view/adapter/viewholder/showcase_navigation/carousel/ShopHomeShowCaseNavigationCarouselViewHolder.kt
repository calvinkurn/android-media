package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.carousel

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationCarouselBannerBinding
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.ShopHomeShowCaseNavigationAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.ShopHomeShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseNavigationCarouselViewHolder(
    itemView: View,
    private val listener: ShopHomeShowcaseNavigationListener
) :
    AbstractViewHolder<ShopHomeShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_carousel_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 12
    }

    private val viewBinding: ItemShopHomeShowcaseNavigationCarouselBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseNavigationUiModel) {
        if (model.appearance is CarouselAppearance) {
            val showcases = model.appearance.showcases
            viewBinding?.tpgTitle?.text = model.appearance.title
            viewBinding?.tpgTitle?.isVisible = model.appearance.title.isNotEmpty() && showcases.isNotEmpty()
            viewBinding?.iconChevron?.setOnClickListener { listener.onNavigationBannerViewAllShowcaseClick(model.appearance.viewAllCtaAppLink) }
            viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD

            setupShowCaseRecyclerView(model.appearance, showcases)
        }
    }

    private fun setupShowCaseRecyclerView(
        appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
        showcases: List<Showcase>
    ) {
        val showCaseAdapter = ShopHomeShowCaseNavigationAdapter(appearance, listener)

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
