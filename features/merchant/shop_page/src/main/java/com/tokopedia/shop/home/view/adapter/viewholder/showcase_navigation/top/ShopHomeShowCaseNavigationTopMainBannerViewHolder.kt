package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.top

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationTopMainBannerBinding
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.ShopHomeShowCaseNavigationAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeReimagineShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopHomeShowCaseNavigationTopMainBannerViewHolder(
    itemView: View,
    private val listener: ShopHomeReimagineShowcaseNavigationListener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener
) : AbstractViewHolder<ShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_top_main_banner
        private const val SECOND_SHOWCASE_INDEX = 1
        private const val TWELVE_SHOWCASE_INDEX = 12
    }

    private val viewBinding: ItemShopHomeShowcaseNavigationTopMainBannerBinding? by viewBinding()

    override fun bind(model: ShowcaseNavigationUiModel) {
        if (model.appearance is TopMainBannerAppearance) {
            val showcases = model.appearance.showcases
            setupTitle(model)
            setupMainBanner(showcases, model)
            setupShowCaseRecyclerView(
                model.header.isOverrideTheme,
                model.header.colorSchema,
                model.appearance,
                model,
                showcases
            )
            setupColors(model.header.isOverrideTheme, model.header.colorSchema)
            listener.onNavigationBannerImpression(
                uiModel = model,
                tabCount = Int.ZERO,
                tabName = "",
                showcaseId = ""
            )
            setupViewAllIcon(model.appearance)
        }
    }

    private fun setupTitle(model: ShowcaseNavigationUiModel) {
        viewBinding?.tpgTitle?.text = model.appearance.title
        viewBinding?.tpgTitle?.isVisible = model.appearance.title.isNotEmpty()
    }

    private fun setupViewAllIcon(appearance: TopMainBannerAppearance) {
        viewBinding?.iconChevron?.isVisible = appearance.title.isNotEmpty() && appearance.viewAllCtaAppLink.isNotEmpty()
        viewBinding?.iconChevron?.setOnClickListener {
            listener.onNavigationBannerViewAllShowcaseClick(
                appearance.viewAllCtaAppLink,
                appearance,
                appearance.showcases.firstOrNull()?.id.orEmpty()
            )
        }
    }

    private fun setupMainBanner(
        showcases: List<Showcase>,
        uiModel: ShowcaseNavigationUiModel
    ) {
        val firstShowcase = showcases.getOrNull(0)

        firstShowcase?.let {
            viewBinding?.imgMainBanner?.loadImage(firstShowcase.imageUrl)
            viewBinding?.tpgMainBannerTitle?.text = firstShowcase.name

            viewBinding?.imgMainBanner?.visible()
            viewBinding?.tpgMainBannerTitle?.visible()
            viewBinding?.imgBackgroundGradient?.visible()

            viewBinding?.imgMainBanner?.setOnClickListener {
                listener.onNavigationBannerShowcaseClick(
                    selectedShowcase = firstShowcase,
                    uiModel = uiModel,
                    tabCount = Int.ONE,
                    tabName = ""
                )
            }
            viewBinding?.tpgMainBannerTitle?.setOnClickListener {
                listener.onNavigationBannerShowcaseClick(
                    selectedShowcase = firstShowcase,
                    uiModel = uiModel,
                    tabCount = Int.ONE,
                    tabName = ""
                )
            }
        }
    }

    private fun setupShowCaseRecyclerView(
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema,
        appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
        uiModel: ShowcaseNavigationUiModel,
        showcases: List<Showcase>
    ) {
        val filteredShowcases =
            showcases.filterIndexed { index, _ -> index in SECOND_SHOWCASE_INDEX..TWELVE_SHOWCASE_INDEX }

        val showCaseAdapter = ShopHomeShowCaseNavigationAdapter(appearance, uiModel, listener, overrideTheme, colorSchema)

        val recyclerView = viewBinding?.recyclerView
        recyclerView?.visible()
        recyclerView?.apply {
            layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = showCaseAdapter
        }

        showCaseAdapter.submit(filteredShowcases)
    }

    private fun setupColors(overrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
        val chevronColor = if (overrideTheme && colorSchema.listColorSchema.isNotEmpty()) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR)
        } else {
            ContextCompat.getColor(viewBinding?.iconChevron?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        val highEmphasizeColor = if (overrideTheme && colorSchema.listColorSchema.isNotEmpty()) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        } else {
            ContextCompat.getColor(viewBinding?.tpgTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        val mainBannerColor = ContextCompat.getColor(viewBinding?.tpgMainBannerTitle?.context ?: return, R.color.clr_dms_icon_white)

        viewBinding?.apply {
            iconChevron.setImage(
                newIconId = IconUnify.CHEVRON_RIGHT,
                newLightEnable = chevronColor,
                newDarkEnable = chevronColor
            )
            tpgTitle.setTextColor(highEmphasizeColor)
            tpgMainBannerTitle.setTextColor(mainBannerColor)
        }
    }
}
