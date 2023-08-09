package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignDisplaySliderBannerHighlightAdapter
import com.tokopedia.shop.campaign.view.adapter.SliderBannerHighlightLayoutManager
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ItemShopCampaignSliderBannerHighlightBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignDisplaySliderBannerHighlightViewHolder(
    itemView: View,
    private val listener: Listener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopWidgetDisplaySliderBannerHighlightUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_slider_banner_highlight
        private const val INT_TWO = 2
    }

    interface Listener {
        fun onProductImageClicked(
            widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
            productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
            position: Int
        )

        fun onProductImageImpression(
            widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
            productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
            position: Int
        )
        fun onWidgetSliderBannerHighlightImpression(
            uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
            bindingAdapterPosition: Int
        )
        fun onWidgetSliderBannerHighlightCtaClicked(
            widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        )
    }

    private val viewBinding: ItemShopCampaignSliderBannerHighlightBinding? by viewBinding()
    private val textTitle: Typography? = viewBinding?.textTitle
    private val rvProductImage: RecyclerView? = viewBinding?.rvProductImage
    private val buttonCta: Typography? = viewBinding?.buttonCta
    private val buttonPrev: IconUnify? = viewBinding?.prevButton
    private val buttonNext: IconUnify? = viewBinding?.nextButton
    private val adapterSliderBannerHighlight: ShopCampaignDisplaySliderBannerHighlightAdapter by lazy {
        ShopCampaignDisplaySliderBannerHighlightAdapter(listener, uiModel)
    }
    private val layoutManagerSliderBannerHighlight: SliderBannerHighlightLayoutManager by lazy {
        SliderBannerHighlightLayoutManager(itemView.context)
    }
    private val rvCircularLoopScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstItemVisible: Int = layoutManager.findFirstVisibleItemPosition()
            if (firstItemVisible != Int.ONE && (firstItemVisible % getTotalProductSize() == Int.ONE)) {
                layoutManager.scrollToPosition(Int.ONE)
            } else if (firstItemVisible != Int.ONE && firstItemVisible > getTotalProductSize() && (firstItemVisible % getTotalProductSize() > Int.ONE)) {
                layoutManager.scrollToPosition(firstItemVisible % getTotalProductSize())
            } else if (firstItemVisible == Int.ZERO) {
                layoutManager.scrollToPositionWithOffset(
                    getTotalProductSize(),
                    -recyclerView.computeHorizontalScrollOffset()
                )
            }
        }
    }
    private var uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel? = null

    override fun bind(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        this.uiModel = uiModel
        setTitle(uiModel)
        setProductImageCarousel(uiModel)
        setButtonCta(uiModel)
        setBackgroundGradient()
        setWidgetImpression(uiModel)
        setCtaClickedListener(uiModel)
    }

    private fun setBackgroundGradient() {
        val listBackgroundColor = shopCampaignInterface.getListBackgroundColor()
        if (listBackgroundColor.size == INT_TWO) {
            if (listBackgroundColor.isNotEmpty()) {
                val colors = IntArray(listBackgroundColor.size)
                for (i in listBackgroundColor.indices) {
                    colors[i] =
                        ShopUtil.parseColorFromHexString(listBackgroundColor.getOrNull(i).orEmpty())
                }
                try {
                    val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
                    gradient.cornerRadius = 0f
                    itemView.background = gradient
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    private fun setCtaClickedListener(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        buttonCta?.setOnClickListener {
            listener.onWidgetSliderBannerHighlightCtaClicked(uiModel)
        }
    }

    private fun setWidgetImpression(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        itemView.addOnImpressionListener(uiModel.impressHolder) {
            listener.onWidgetSliderBannerHighlightImpression(uiModel, bindingAdapterPosition)
        }
    }

    private fun setPrevNextButton() {
        buttonPrev?.apply {
            show()
            setOnClickListener {
                val firstVisiblePos =
                    layoutManagerSliderBannerHighlight.findFirstVisibleItemPosition()
                rvProductImage?.smoothScrollToPosition(firstVisiblePos.minus(Int.ONE))
            }
        }
        buttonNext?.apply {
            show()
            setOnClickListener {
                val lastVisiblePos =
                    layoutManagerSliderBannerHighlight.findLastVisibleItemPosition()
                rvProductImage?.smoothScrollToPosition(lastVisiblePos.plus(Int.ONE))
            }
        }
    }

    private fun setProductImageCarousel(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        rvProductImage?.apply {
            if (uiModel.listHighlightProductData.isEmpty()) {
                gone()
                hidePrevNextButton()
            } else {
                show()
                layoutManager = layoutManagerSliderBannerHighlight
                adapter = adapterSliderBannerHighlight
                adapterSliderBannerHighlight.submit(uiModel.listHighlightProductData)
                addOnScrollListener(rvCircularLoopScrollListener)
                setPrevNextButton()
            }
            scrollToPosition(adapterSliderBannerHighlight.itemCount / INT_TWO)
        }
    }

    private fun hidePrevNextButton() {
        buttonPrev?.gone()
        buttonNext?.gone()
    }

    private fun setButtonCta(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        buttonCta?.text = uiModel.header.ctaText
    }

    private fun setTitle(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        textTitle?.text = uiModel.header.title
    }

    private fun getTotalProductSize(): Int {
        return uiModel?.listHighlightProductData?.size.orZero()
    }
}
