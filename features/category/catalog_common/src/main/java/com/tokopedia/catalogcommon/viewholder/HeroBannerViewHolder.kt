package com.tokopedia.catalogcommon.viewholder

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerHeroBinding
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicatorListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding


@SuppressLint("ClickableViewAccessibility")
class HeroBannerViewHolder(itemView: View):
    AbstractViewHolder<HeroBannerUiModel>(itemView)
{

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_banner_hero
    }

    private val binding by viewBinding<WidgetItemBannerHeroBinding>()
    private var isAutosliding = true
    private var brandImageCount = 0

    init {
        binding?.carouselBanner?.apply {
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            infinite = true
            bannerItemMargin = Int.ZERO
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    if (!isAutosliding) {
                        binding?.bannerIndicator?.setBannerIndicators(brandImageCount, current)
                        isAutosliding = true
                        binding?.bannerIndicator?.continueAnimation()
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
        binding?.bannerIndicator?.setBannerListener(object : BannerIndicatorListener {
            override fun onChangePosition(position: Int) {
                if (isAutosliding) {
                    val imgPosition = position % brandImageCount
                    binding?.carouselBanner?.activeIndex = imgPosition
                }
            }

            override fun getCurrentPosition(position: Int) {
                // no-op
            }
        })
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
    }

    private fun WidgetItemBannerHeroBinding.configViewsVisibility(isPremium: Boolean) {
        // premium views
        tfTitleBannerPremium.isVisible = isPremium
        tfSubtitleBannerPremium.isVisible = isPremium
        iuBrandPremium.isVisible = isPremium
        iuBrandPremiumCard.isVisible = isPremium
        bgGradient.isVisible = isPremium

        // regular views
        tfTitleBanner.isVisible = !isPremium
        iuBrandCard.isVisible = !isPremium
        iuBrand.isVisible = !isPremium
    }

    private fun WidgetItemBannerHeroBinding.renderPremiumBrandData(element: HeroBannerUiModel) {
        val cardColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        val colorBg = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black)
        tfTitleBannerPremium.text = element.brandTitle
        tfSubtitleBannerPremium.text = element.brandDesc
        iuBrandPremium.loadImage(element.brandIconUrl)
        iuBrandPremiumCard.background = createGradientDrawable(cardColor, cardColor, 100f)
        bgGradient.background = createGradientDrawable(colorBottom = colorBg)
    }

    private fun WidgetItemBannerHeroBinding.renderRegularBrandData(element: HeroBannerUiModel) {
        val cardColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        tfTitleBanner.text = element.brandTitle
        iuBrand.loadImage(element.brandIconUrl)
        iuBrandCard.background = createGradientDrawable(cardColor, cardColor, 100f)
    }

    private fun createGradientDrawable(
        colorTop: Int = 0x00,
        colorBottom: Int = 0x00,
        cornerRadius: Float = 0f
    ): Drawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(colorTop, colorBottom)
        ).apply {
            this.cornerRadius = cornerRadius
        }
    }
}
