package com.tokopedia.catalogcommon.viewholder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerHeroBinding
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.util.DrawableExtension.createGradientDrawable
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicator
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicatorListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HeroBannerViewHolder(
    itemView: View,
    private val heroBannerListener: HeroBannerListener? = null
): AbstractViewHolder<HeroBannerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_banner_hero
        private const val CIRCULAR_CARD_RADIUS = 100F
        private const val RECTANGULAR_CARD_RADIUS = 30F
        private const val BANNER_PREMIUM_RATIO = "1:1.5"
        private const val BANNER_REGULAR_RATIO = "1:1"
    }

    private val binding by viewBinding<WidgetItemBannerHeroBinding>()
    private var isAutosliding = true
    private var brandImageCount = 0
    private var brandDescriptions: List<String> = emptyList()
    private val bannerAdapter = ImageSliderAdapter(itemView.context)
    private var brandImageUrl: List<String> = emptyList()

    init {
        binding?.carouselBanner?.setupCarouselBanner()
        binding?.bannerIndicator?.setupCarouselIndicator()
    }

    private fun BannerIndicator.setupCarouselIndicator() {
        setBannerListener(object : BannerIndicatorListener {
            override fun onChangePosition(index: Int, position: Int) {
                if (isAutosliding) {
                    val imgPosition = position % brandImageCount
                    binding?.carouselBanner?.setCurrentItem(imgPosition.inc(), true)
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

        // regular views
        tfTitleBanner.isVisible = !isPremium
        iuBrandCard.isVisible = !isPremium
        iuBrand.isVisible = !isPremium
    }

    private fun WidgetItemBannerHeroBinding.renderPremiumBrandData(element: HeroBannerUiModel) {
        val colorBg = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_Black)
        tfTitleBannerPremium.text = element.brandTitle
        iuBrandPremium.loadImage(element.brandIconUrl) {
            listener(onSuccess = { bitmap, _ ->
                iuBrandPremiumCard.background = createCardBackground(bitmap ?: return@listener)
            })
        }
        bgGradient.background = createGradientDrawable(colorBottom = colorBg)
        brandDescriptions = element.brandDescriptions
        tfSubtitleBannerPremium.text = brandDescriptions.firstOrNull().orEmpty()
        (binding?.carouselBanner?.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = BANNER_PREMIUM_RATIO
    }

    private fun WidgetItemBannerHeroBinding.renderRegularBrandData(element: HeroBannerUiModel) {
        tfTitleBanner.text = element.brandTitle
        iuBrand.loadImage(element.brandIconUrl) {
            listener(onSuccess = { bitmap, _ ->
                iuBrandCard.background = createCardBackground(bitmap ?: return@listener)
            })
        }
        (binding?.carouselBanner?.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = BANNER_REGULAR_RATIO
    }

    private fun ViewPager.setupCarouselBanner() {
        adapter = bannerAdapter
        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (bannerAdapter.count > Int.ONE) {
                    if (position == bannerAdapter.count.orZero().dec()) {
                        setCurrentItem(bannerAdapter.getFirstPosition(), false)
                    } else if (position == Int.ZERO) {
                        setCurrentItem(bannerAdapter.getLastPosition(), false)
                    }

                    var indicatorPosition = position.dec()
                    if (indicatorPosition >= brandImageCount) {
                        indicatorPosition = Int.ZERO
                    } else if (indicatorPosition.isLessThanZero()) {
                        indicatorPosition = brandImageCount.dec()
                    }
                    binding?.tfSubtitleBannerPremium?.text =
                        brandDescriptions.getOrNull(indicatorPosition).orEmpty()
                    binding?.bannerIndicator?.setBannerIndicators(brandImageCount, indicatorPosition)
                    heroBannerListener?.onHeroBannerImpression(
                        indicatorPosition,
                        brandDescriptions,
                        brandImageUrl
                    )
                } else {
                    binding?.bannerIndicator?.pauseAnimation()
                    heroBannerListener?.onHeroBannerImpression(
                        Int.ZERO,
                        brandDescriptions,
                        brandImageUrl
                    )
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == SCROLL_STATE_DRAGGING) {
                    binding?.bannerIndicator?.pauseAnimation()
                } else {
                    binding?.bannerIndicator?.continueAnimation()
                }
            }
        })
    }

    private fun createCardBackground(bitmap: Bitmap): Drawable {
        val cardColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White)
        val radius = if (bitmap.width/bitmap.height <= 1.1) {
            RECTANGULAR_CARD_RADIUS
        } else {
            CIRCULAR_CARD_RADIUS
        }
        return createGradientDrawable(cardColor, cardColor, radius)
    }

    override fun bind(element: HeroBannerUiModel) {
        brandImageCount = element.brandImageUrls.size
        brandImageUrl = element.brandImageUrls
        binding?.configViewsVisibility(element.isPremium)
        if (element.isPremium) {
            binding?.renderPremiumBrandData(element)
        } else {
            binding?.renderRegularBrandData(element)
        }

        bannerAdapter.setImages(element.brandImageUrls)
        binding?.carouselBanner?.currentItem = bannerAdapter.getFirstPosition()
        binding?.bannerIndicator?.setBannerIndicators(brandImageCount)
        binding?.tfTitleBanner?.setTextColor(element.widgetTextColor.orDefaultColor(itemView.context))
    }
}

class ImageSliderAdapter(private val context: Context): PagerAdapter() {

    companion object {
        private const val LAST_POSITION_OFFSET = 2
    }

    private var images: MutableList<String> = mutableListOf()

    override fun getCount() = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.carousel_item_sample_layout, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.iuBanner)

        imageView.loadImage(images.getOrNull(position).orEmpty())
        container.addView(itemView)
        return itemView
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    fun setImages(imageList: List<String>) {
        if (imageList.size == Int.ONE) {
            images = imageList.toMutableList()
        } else {
            images = mutableListOf()
            images.add(imageList.lastOrNull().orEmpty())
            images.addAll(imageList)
            images.add(imageList.firstOrNull().orEmpty())
        }
        notifyDataSetChanged()
    }

    fun getFirstPosition() = Int.ONE

    fun getLastPosition() = count - LAST_POSITION_OFFSET
}
