package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopHomeTerlarisWidgetTrackerDataModel
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.LayoutShopHomeV4TerlarisWidgetBinding
import com.tokopedia.shop.home.view.adapter.ShopHomeReimagineTerlarisAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeReimagineTerlarisViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_v4_terlaris_widget

        const val PRODUCT_THREE = 3
        private const val THREE_COLUMN = 3
    }

    private val viewBinding: LayoutShopHomeV4TerlarisWidgetBinding? by viewBinding()

    override fun bind(element: ShopHomeCarousellProductUiModel) {
        setHeaderSection(element)
        overrideWidgetHeaderTheme(colorSchema = element.header.colorSchema)

        val terlarisWidgetAdapter = ShopHomeReimagineTerlarisAdapter(
            listener = listener,
            isFestivity = element.isFestivity,
            backgroundColor = listener.getBackgroundColor(),
            patternColorType = listener.getPatternColorType(),
            element = element
        )

        val gridLayoutManager = GridLayoutManager(
            itemView.context,
            THREE_COLUMN,
            GridLayoutManager.HORIZONTAL,
            false
        )

        viewBinding?.recyclerView?.apply {
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            adapter = terlarisWidgetAdapter
            layoutManager = gridLayoutManager
        }

        setupImpressionListener(element, element.productList)
    }

    private fun overrideWidgetHeaderTheme(colorSchema: ShopPageColorSchema) {
        viewBinding?.tpgTitle?.setTextColor(
            colorSchema.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
            )
        )

        viewBinding?.tpgSubtitle?.setTextColor(
            colorSchema.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
            )
        )
    }

    private fun setHeaderSection(element: ShopHomeCarousellProductUiModel) {
        val title = element.header.title
        viewBinding?.tpgTitle?.shouldShowWithAction(title.isNotEmpty()) {
            viewBinding?.tpgTitle?.text = title
        }

        val subTitle = element.header.subtitle
        viewBinding?.tpgSubtitle?.shouldShowWithAction(subTitle.isNotEmpty()) {
            viewBinding?.tpgSubtitle?.text = subTitle
        }
    }

    private fun setupImpressionListener(
        element: ShopHomeCarousellProductUiModel,
        carouselData: List<ShopHomeProductUiModel>
    ) {
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.onProductImpression(carouselData, bindingAdapterPosition, element.widgetId)
        }
    }

    interface Listener {
        fun onProductClick(trackerModel: ShopHomeTerlarisWidgetTrackerDataModel)

        fun onProductImpression(
            carouselData: List<ShopHomeProductUiModel>,
            position: Int,
            widgetId: String
        )
        fun getPatternColorType(): String
        fun getBackgroundColor(): String
    }
}
