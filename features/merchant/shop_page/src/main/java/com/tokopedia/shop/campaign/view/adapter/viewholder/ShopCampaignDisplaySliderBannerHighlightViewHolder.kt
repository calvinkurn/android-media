package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignDisplaySliderBannerHighlightAdapter
import com.tokopedia.shop.campaign.view.adapter.SliderBannerHighlightLayoutManager
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ItemShopCampaignSliderBannerHighlightBinding
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignDisplaySliderBannerHighlightViewHolder(
    itemView: View,
    private val listener: Listener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopWidgetDisplaySliderBannerHighlightUiModel>(itemView),
    SliderBannerHighlightLayoutManager.Listener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_slider_banner_highlight
    }

    interface Listener{
        fun onProductImageClicked(productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData)
        fun onProductImageImpression(productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData)
        fun onWidgetSliderBannerHighlightImpression(
            uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
            bindingAdapterPosition: Int
        )
        fun onWidgetSliderBannerHighlightCtaClicked(
            uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel
        )
    }

    private val viewBinding: ItemShopCampaignSliderBannerHighlightBinding? by viewBinding()
    private val textTitle: Typography? = viewBinding?.textTitle
    private val rvProductImage: RecyclerView? = viewBinding?.rvProductImage
    private val buttonCta: UnifyButton? = viewBinding?.buttonCta
    private val buttonPrev: IconUnify? = viewBinding?.prevButton
    private val buttonNext: IconUnify? = viewBinding?.nextButton
    private val adapterSliderBannerHighlight: ShopCampaignDisplaySliderBannerHighlightAdapter by lazy {
        ShopCampaignDisplaySliderBannerHighlightAdapter(listener)
    }
    private val layoutManagerSliderBannerHighlight: SliderBannerHighlightLayoutManager by lazy {
        SliderBannerHighlightLayoutManager(itemView.context, this)
    }

    override fun bind(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        setTitle(uiModel)
        setProductImageCarousel(uiModel)
        setButtonCta(uiModel)
        setBackgroundGradient()
        setWidgetImpression(uiModel)
        setCtaClickedListener(uiModel)
    }

    private fun setBackgroundGradient() {
        val listBackgroundColor = shopCampaignInterface.getListBackgroundColor()
        val colors = IntArray(listBackgroundColor.size)
        for (i in listBackgroundColor.indices) {
            colors[i] = ShopUtil.parseColorFromHexString(listBackgroundColor.getOrNull(i).orEmpty())
        }
        val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        gradient.cornerRadius = 0f
        itemView.background = gradient
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
                setPrevNextButton()
            }
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

    override fun onCheckPrevNextButton(
        firstVisibleItemPos: Int,
        lastVisibleItemPos: Int,
        itemCount: Int
    ) {
        if (firstVisibleItemPos == Int.ZERO) {
            buttonPrev?.gone()
            buttonNext?.show()
        } else if (lastVisibleItemPos == itemCount - Int.ONE) {
            buttonNext?.gone()
            buttonPrev?.show()
        } else {
            buttonPrev?.show()
            buttonNext?.show()
        }
    }
}
