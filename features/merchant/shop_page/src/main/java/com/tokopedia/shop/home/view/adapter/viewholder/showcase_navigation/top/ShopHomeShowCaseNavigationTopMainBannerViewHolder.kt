package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.top

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationTopMainBannerBinding
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.ShopHomeShowCaseNavigationAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.ShopHomeShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopHomeShowCaseNavigationTopMainBannerViewHolder(
    itemView: View,
    private val listener: ShopHomeShowcaseNavigationListener
) : AbstractViewHolder<ShopHomeShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_top_main_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
        private const val SECOND_SHOWCASE_INDEX = 1
        private const val TWELVE_SHOWCASE_INDEX = 12
    }

    private val viewBinding: ItemShopHomeShowcaseNavigationTopMainBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseNavigationUiModel) {
        if (model.appearance is TopMainBannerAppearance) {
            viewBinding?.tpgTitle?.text = model.appearance.title
            viewBinding?.tpgTitle?.isVisible = model.appearance.title.isNotEmpty()
            viewBinding?.iconChevron?.setOnClickListener {
                listener.onNavigationBannerViewAllShowcaseClick(model.appearance.viewAllCtaAppLink)
            }

            val showcases = model.appearance.showcases

            setupViewAllIcon(showcases)
            setupMainBanner(showcases)
            setupShowCaseRecyclerView(
                model.header.isOverrideTheme,
                model.header.colorSchema,
                model.appearance,
                showcases
            )
            setupColors(model.header.isOverrideTheme, model.header.colorSchema)
        }

    }

    private fun setupViewAllIcon(showcases: List<Showcase>) {
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD
    }

    private fun setupMainBanner(showcases: List<Showcase>) {
        val firstShowcase = showcases.getOrNull(0)

        firstShowcase?.let {
            viewBinding?.imgFirstBanner?.loadImage(firstShowcase.imageUrl)
            viewBinding?.tpgFirstBannerTitle?.text = firstShowcase.name
            viewBinding?.imgFirstBanner?.visible()
            viewBinding?.tpgFirstBannerTitle?.visible()

            viewBinding?.imgFirstBanner?.setOnClickListener { listener.onNavigationBannerShowcaseClick(firstShowcase) }
            viewBinding?.tpgFirstBannerTitle?.setOnClickListener { listener.onNavigationBannerShowcaseClick(firstShowcase)  }
        }
    }

    private fun setupShowCaseRecyclerView(
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema,
        appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
        showcases: List<Showcase>
    ) {
        val filteredShowcases =
            showcases.filterIndexed { index, _ -> index in SECOND_SHOWCASE_INDEX..TWELVE_SHOWCASE_INDEX }

        val showCaseAdapter = ShopHomeShowCaseNavigationAdapter(appearance, listener, overrideTheme, colorSchema)

        val recyclerView = viewBinding?.recyclerView
        recyclerView?.apply {
            layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = showCaseAdapter
        }

        showCaseAdapter.submit(filteredShowcases)
    }

    private fun setupColors(overrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
        val chevronColor = if (overrideTheme) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_CTA_LINK_COLOR)
        } else {
            ContextCompat.getColor(viewBinding?.iconChevron?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        val highEmphasizeColor = if (overrideTheme) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        } else {
            ContextCompat.getColor(viewBinding?.tpgTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        val lowEmphasizeColor = if (overrideTheme) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
        } else {
            ContextCompat.getColor(viewBinding?.tpgFirstBannerTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        viewBinding?.apply {
            iconChevron.setImage(
                newIconId = IconUnify.CHEVRON_RIGHT,
                newLightEnable = chevronColor,
                newDarkEnable = chevronColor
            )
            tpgTitle.setTextColor(highEmphasizeColor)
            tpgFirstBannerTitle.setTextColor(lowEmphasizeColor)
        }
    }
}
