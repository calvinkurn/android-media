package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetView
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.databinding.ItemShopHomePersoProductComparisonBinding
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomePersoProductComparisonUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomePersoProductComparisonViewHolder(
    itemView: View,
    private val listener: ShopHomePersoProductComparisonViewHolderListener,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomePersoProductComparisonUiModel>(itemView), ComparisonWidgetInterface {

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
            comparisonWidget?.setComparisonWidgetData(
                it,
                this,
                RecommendationTrackingModel(
                    "",
                    uiModel.recommendationWidget.title,
                    ShopPageTrackingConstant.CLICK_PG,
                    ShopPageTrackingConstant.SHOP_PAGE_BUYER
                ),
                shopHomeListener.getFragmentTrackingQueue()
            )
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
}
