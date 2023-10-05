package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.customview.ShopCampaignTabWidgetHeaderView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.databinding.WidgetShopCampaignMultipleImageColumnBinding
import com.tokopedia.shop.home.view.adapter.PaddingItemDecorationShopPage
import com.tokopedia.shop.home.view.adapter.ShopHomeMultipleImageColumnAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignMultipleImageColumnViewHolder(
    itemView: View,
    private val listener: ShopHomeDisplayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_shop_campaign_multiple_image_column
        private const val SPAN_SIZE_SINGLE = 6
        private const val SPAN_SIZE_DOUBLE = 3
        private const val SPAN_SIZE_TRIPLE = 2
        private const val SPAN_SIZE_DOUBLE_DATA_SIZE = 2
        private const val SPAN_SIZE_TRIPLE_DATA_SIZE = 3
        private const val CORNER_RADIUS = 4f
    }
    private val viewBinding: WidgetShopCampaignMultipleImageColumnBinding? by viewBinding()
    private var shopHomeMultipleImageColumnAdapter: ShopHomeMultipleImageColumnAdapter? = null
    private val rvShopMultiple: RecyclerView? = viewBinding?.rvShopMultipleImage
    private var headerView: ShopCampaignTabWidgetHeaderView? = viewBinding?.headerView

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        setHeader(element)
        setWidgetImpressionListener(element)
        val cornerRadius = CORNER_RADIUS.dpToPx()
        shopHomeMultipleImageColumnAdapter = ShopHomeMultipleImageColumnAdapter(listener, cornerRadius)
        val gridLayoutManager = GridLayoutManager(itemView.context, SPAN_SIZE_SINGLE)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (element.data?.size) {
                    SPAN_SIZE_DOUBLE_DATA_SIZE -> SPAN_SIZE_DOUBLE
                    SPAN_SIZE_TRIPLE_DATA_SIZE -> SPAN_SIZE_TRIPLE
                    else -> SPAN_SIZE_SINGLE
                }
            }
        }

        rvShopMultiple?.apply {
            isNestedScrollingEnabled = false
            layoutManager = gridLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecorationShopPage(element.name))
            }
            adapter = shopHomeMultipleImageColumnAdapter
        }
        shopHomeMultipleImageColumnAdapter?.setShopHomeDisplayWidgetUiModelData(element)
        shopHomeMultipleImageColumnAdapter?.setParentPosition(adapterPosition)
        shopHomeMultipleImageColumnAdapter?.setHeightRatio(getHeightRatio(element))
        shopHomeMultipleImageColumnAdapter?.submitList(element.data)
    }

    private fun setWidgetImpressionListener(model: ShopHomeDisplayWidgetUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            listener.onDisplayWidgetImpression(model, adapterPosition)
        }
    }

    private fun getIndexRatio(data: ShopHomeDisplayWidgetUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index).toIntOrZero()
    }

    private fun getHeightRatio(element: ShopHomeDisplayWidgetUiModel): Float {
        val indexZero = getIndexRatio(element, 0).toFloat()
        val indexOne = getIndexRatio(element, 1).toFloat()
        return (indexOne / indexZero)
    }

    private fun setHeader(uiModel: ShopHomeDisplayWidgetUiModel) {
        val title = uiModel.header.title
        if (title.isEmpty()) {
            headerView?.hide()
        } else {
            headerView?.show()
            headerView?.setTitle(title)
            headerView?.configColorMode(shopCampaignInterface.isCampaignTabDarkMode())
        }
    }
}
