package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerHeroBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerHeroNavigationBinding
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.util.DrawableExtension.createGradientDrawable
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicator
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicatorListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class HeroBannerViewHolder(
    itemView: View,
    private val heroBannerListener: HeroBannerListener? = null
): AbstractViewHolder<HeroBannerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_banner_hero
        private const val CIRCULAR_CARD_RADIUS = 100F
    }

    private val binding by viewBinding<WidgetItemBannerHeroBinding>()
    private var isAutosliding = true
    private var brandImageCount = 0
    private var brandDescriptions: List<String> = emptyList()

    init {
        binding?.carouselBanner?.setupCarouselBanner()
        binding?.bannerIndicator?.setupCarouselIndicator()
    }

    private fun CarouselUnify.setupCarouselBanner() {
        indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
        infinite = true
        bannerItemMargin = Int.ZERO
        onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
            override fun onActiveIndexChanged(prev: Int, current: Int) {
                if (!isAutosliding) {
                    binding?.bannerIndicator?.setBannerIndicators(brandImageCount, current)
                    isAutosliding = true
                    binding?.bannerIndicator?.continueAnimation()
                    binding?.tfSubtitleBannerPremium?.text =
                        brandDescriptions.getOrNull(current).orEmpty()
                }
            }
        }
        onDragEventListener = object : CarouselUnify.OnDragEventListener {
            override fun onDrag(progress: Float) {
                isAutosliding = false
                binding?.bannerIndicator?.pauseAnimation()
            }
        }
    }

    private fun BannerIndicator.setupCarouselIndicator() {
        setBannerListener(object : BannerIndicatorListener {
            override fun onChangePosition(position: Int) {
                if (isAutosliding) {
                    val imgPosition = position % brandImageCount
                    binding?.carouselBanner?.activeIndex = imgPosition
                    binding?.tfSubtitleBannerPremium?.text =
                        brandDescriptions.getOrNull(imgPosition).orEmpty()
                }
            }
            override fun getCurrentPosition(position: Int) {
                // no-op
            }
        })
    }

    private fun WidgetItemBannerHeroBinding.configViewsVisibility(isPremium: Boolean) {
        // premium views
        tfTitleBannerPremium.isVisible = isPremium
        tfSubtitleBannerPremium.isVisible = isPremium
        iuBrandPremium.isVisible = isPremium
        iuBrandPremiumCard.isVisible = isPremium
        bgGradient.isVisible = isPremium
        navigation.root.isVisible = isPremium

        // regular views
        tfTitleBanner.isVisible = !isPremium
        iuBrandCard.isVisible = !isPremium
        iuBrand.isVisible = !isPremium
    }

    private fun WidgetItemBannerHeroBinding.renderPremiumBrandData(element: HeroBannerUiModel) {
        val cardColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        val colorBg = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black)
        tfTitleBannerPremium.text = element.brandTitle
        iuBrandPremium.loadImage(element.brandIconUrl)
        iuBrandPremiumCard.background = createGradientDrawable(cardColor, cardColor, CIRCULAR_CARD_RADIUS)
        bgGradient.background = createGradientDrawable(colorBottom = colorBg)
        brandDescriptions = element.brandDescriptions
    }

    private fun WidgetItemBannerHeroBinding.renderRegularBrandData(element: HeroBannerUiModel) {
        val cardColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        tfTitleBanner.text = element.brandTitle
        iuBrand.loadImage(element.brandIconUrl)
        iuBrandCard.background = createGradientDrawable(cardColor, cardColor, CIRCULAR_CARD_RADIUS)
    }

    private fun WidgetItemBannerHeroNavigationBinding.setupNavigation(element: HeroBannerUiModel) {
        val cardColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White_68)
        iconBack.background = createGradientDrawable(cardColor, cardColor, CIRCULAR_CARD_RADIUS)
        bgRightMenu.background = createGradientDrawable(cardColor, cardColor, CIRCULAR_CARD_RADIUS)
        iconBack.setOnClickListener { heroBannerListener?.onNavBackClicked() }
        iconMoreMenu.setOnClickListener { heroBannerListener?.onNavMoreMenuClicked() }
        iconShare.setOnClickListener { heroBannerListener?.onNavShareClicked() }
    }

    override fun bind(element: HeroBannerUiModel) {
        brandImageCount = element.brandImageUrls.size
        binding?.configViewsVisibility(element.isPremium)
        if (element.isPremium) {
            binding?.renderPremiumBrandData(element)
        } else {
            binding?.renderRegularBrandData(element)
        }
        binding?.carouselBanner?.addImages(ArrayList(element.brandImageUrls))
        binding?.bannerIndicator?.setBannerIndicators(brandImageCount)
        binding?.navigation?.setupNavigation(element)
    }
}
