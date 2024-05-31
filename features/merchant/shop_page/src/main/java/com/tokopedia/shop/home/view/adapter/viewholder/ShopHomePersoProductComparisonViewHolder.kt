package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonColorConfig
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetView
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomePersoProductComparisonBinding
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomePersoProductComparisonUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.shop.common.view.model.ShopPageColorSchema.ColorSchemaName

class ShopHomePersoProductComparisonViewHolder(
    itemView: View,
    private val listener: ShopHomePersoProductComparisonViewHolderListener,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomePersoProductComparisonUiModel>(itemView), ComparisonWidgetInterface, AdsViewListener, AdsItemClickListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_perso_product_comparison
    }

    interface ShopHomePersoProductComparisonViewHolderListener {
        fun onProductCardComparisonImpressed(
            recommendationItem: RecommendationItem,
            comparisonListModel: ComparisonListModel,
            position: Int
        )
        fun onProductCardComparisonClicked(
            recommendationItem: RecommendationItem,
            comparisonListModel: ComparisonListModel,
            position: Int
        )
    }

    private val viewBinding: ItemShopHomePersoProductComparisonBinding? by viewBinding()
    private val comparisonWidget: ComparisonWidgetView? = viewBinding?.comparisonWidget

    override fun bind(uiModel: ShopHomePersoProductComparisonUiModel) {
        setComparisonWidget(uiModel)
    }

    private fun setComparisonWidget(uiModel: ShopHomePersoProductComparisonUiModel) {
        uiModel.recommendationWidget?.let {
            val colorConfig = determineWidgetColorScheme(uiModel.header.colorSchema)
           
            comparisonWidget?.setComparisonWidgetData(
                it,
                this,
                this,
                this,
                RecommendationTrackingModel(
                    "",
                    uiModel.recommendationWidget.title,
                    ShopPageTrackingConstant.CLICK_PG,
                    ShopPageTrackingConstant.SHOP_PAGE_BUYER
                ),
                shopHomeListener.getFragmentTrackingQueue(),
                null,
                colorConfig
            )
        }
    }

    private fun determineWidgetColorScheme(colorSchema: ShopPageColorSchema): ComparisonColorConfig {
        val textColor = colorSchema.listColorSchema.firstOrNull { colorSchema ->
            colorSchema.name == ColorSchemaName.TEXT_HIGH_EMPHASIS.value
        }
        val ctaTextColor = colorSchema.listColorSchema.firstOrNull { colorSchema ->
            colorSchema.name == ColorSchemaName.ICON_ENABLED_HIGH_COLOR.value
        }
        val anchorBackgroundColor = colorSchema.listColorSchema.firstOrNull { colorSchema ->
            colorSchema.name == ColorSchemaName.BG_PRIMARY_COLOR.value
        }

        return if (shopHomeListener.isOverrideTheme()) {
            ComparisonColorConfig(
                textColor = textColor?.value.orEmpty(),
                anchorBackgroundColor = anchorBackgroundColor?.value.orEmpty(),
                ctaTextColor = ctaTextColor?.value.orEmpty(), 
                productCardForceLightMode = true
            )
        } else {
            ComparisonColorConfig()
        }
    }

    override fun onProductCardImpressed(
        recommendationItem: RecommendationItem,
        comparisonListModel: ComparisonListModel,
        position: Int
    ) {
        listener.onProductCardComparisonImpressed(recommendationItem, comparisonListModel, position)
    }

    override fun onProductCardClicked(
        recommendationItem: RecommendationItem,
        comparisonListModel: ComparisonListModel,
        position: Int
    ) {
        listener.onProductCardComparisonClicked(recommendationItem, comparisonListModel, position)
    }

    override fun onAreaClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
    }

    override fun onProductImageClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
    }

    override fun onSellerInfoClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
    }

    override fun onViewAttachedToWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendShowAdsByteIo(itemView.context)
    }

    override fun onViewDetachedFromWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int, visiblePercentage: Int) {
        recomItem.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
    }
}
