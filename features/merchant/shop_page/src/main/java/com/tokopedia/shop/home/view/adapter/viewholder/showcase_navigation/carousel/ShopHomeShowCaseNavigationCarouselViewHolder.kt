package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.carousel

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationCarouselBannerBinding
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.ShopHomeShowCaseNavigationAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopHomeShowCaseNavigationCarouselViewHolder(
    itemView: View,
    private val listener: ShopHomeShowcaseNavigationListener
) :
    AbstractViewHolder<ShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_carousel_banner
    }

    private val viewBinding: ItemShopHomeShowcaseNavigationCarouselBannerBinding? by viewBinding()


    override fun bind(model: ShowcaseNavigationUiModel) {
        if (model.appearance is CarouselAppearance) {
            val showcases = model.appearance.showcases

            setupColors(model.header.isOverrideTheme, model.header.colorSchema)
            setupTitle(model.appearance.title, showcases)
            setupChevronViewAll(model.appearance.title, model.appearance.viewAllCtaAppLink, model.appearance, showcases)
            setupShowCaseRecyclerView(
                model.header.isOverrideTheme,
                model.header.colorSchema,
                model.appearance,
                model.appearance.showcases,
                model
            )

            listener.onNavigationBannerImpression(
                uiModel = model,
                tabCount = 0,
                tabName = "",
                showcaseId = ""
            )
        }
    }

    private fun setupTitle(title: String, showcases: List<Showcase>) {
        viewBinding?.tpgTitle?.text = title
        viewBinding?.tpgTitle?.isVisible = title.isNotEmpty() && showcases.isNotEmpty()
    }

    private fun setupChevronViewAll(
        title: String,
        viewAllCtaAppLink: String,
        appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
        showcases: List<Showcase>
    ) {
        viewBinding?.iconChevron?.setOnClickListener {
            listener.onNavigationBannerViewAllShowcaseClick(
                viewAllCtaAppLink,
                appearance,
                showcases.firstOrNull()?.id.orEmpty()
            )
        }
        viewBinding?.iconChevron?.isVisible = viewAllCtaAppLink.isNotEmpty() && title.isNotEmpty()
    }

    private fun setupShowCaseRecyclerView(
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema,
        appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
        showcases: List<Showcase>,
        uiModel: ShowcaseNavigationUiModel
    ) {
        val showCaseAdapter = ShopHomeShowCaseNavigationAdapter(appearance, uiModel, listener, overrideTheme, colorSchema)

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

    private fun setupColors(overrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
        val chevronColor = if (overrideTheme && colorSchema.listColorSchema.isNotEmpty()) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_CTA_LINK_COLOR)
        } else {
            ContextCompat.getColor(viewBinding?.iconChevron?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        val highEmphasizeColor = if (overrideTheme && colorSchema.listColorSchema.isNotEmpty()) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        } else {
            ContextCompat.getColor(viewBinding?.tpgTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        viewBinding?.apply {
            iconChevron.setImage(
                newIconId = IconUnify.CHEVRON_RIGHT,
                newLightEnable = chevronColor,
                newDarkEnable = chevronColor
            )
            tpgTitle.setTextColor(highEmphasizeColor)
        }
    }
}
