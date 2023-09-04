package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TypeFactory
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateDivider
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryEmptyStateDividerViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2HeaderViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2TabViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.TokoNowFeedbackWidgetTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener

class CategoryL2AdapterTypeFactory(
    private var tokoNowView: TokoNowView? = null,
    private var emptyStateNoResultListener: TokoNowEmptyStateNoResultListener? = null,
    private var productRecommendationListener: TokoNowProductRecommendationListener? = null,
    private var categoryMenuListener: TokoNowCategoryMenuListener? = null,
    private var feedbackWidgetListener: FeedbackWidgetListener? = null,
    chooseAddressListener: TokoNowChooseAddressWidgetListener,
    productAdsCarouselListener: ProductAdsCarouselListener,
) : BaseCategoryAdapterTypeFactory(chooseAddressListener, productAdsCarouselListener, tokoNowView),
    CategoryL2TypeFactory,
    TokoNowEmptyStateNoResultTypeFactory,
    TokoNowProductRecommendationTypeFactory,
    TokoNowCategoryMenuTypeFactory,
    TokoNowFeedbackWidgetTypeFactory {

    override fun type(uiModel: CategoryL2HeaderUiModel): Int = CategoryL2HeaderViewHolder.LAYOUT

    override fun type(uiModel: CategoryL2TabUiModel): Int = CategoryL2TabViewHolder.LAYOUT

    override fun type(uiModel: CategoryEmptyStateDivider): Int = CategoryEmptyStateDividerViewHolder.LAYOUT

    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel) = TokoNowEmptyStateNoResultViewHolder.LAYOUT

    override fun type(uiModel: TokoNowProductRecommendationUiModel): Int = TokoNowProductRecommendationViewHolder.LAYOUT

    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT

    override fun type(uiModel: TokoNowFeedbackWidgetUiModel): Int = TokoNowFeedbackWidgetViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CategoryL2HeaderViewHolder.LAYOUT -> {
                CategoryL2HeaderViewHolder(view)
            }
            CategoryL2TabViewHolder.LAYOUT -> {
                CategoryL2TabViewHolder(view, tokoNowView)
            }
            TokoNowEmptyStateNoResultViewHolder.LAYOUT -> TokoNowEmptyStateNoResultViewHolder(
                itemView = view,
                tokoNowEmptyStateNoResultListener = emptyStateNoResultListener
            )
            TokoNowProductRecommendationViewHolder.LAYOUT -> TokoNowProductRecommendationViewHolder(
                itemView = view,
                listener = productRecommendationListener
            )
            TokoNowCategoryMenuViewHolder.LAYOUT -> TokoNowCategoryMenuViewHolder(
                itemView = view,
                listener = categoryMenuListener
            )
            TokoNowFeedbackWidgetViewHolder.LAYOUT -> TokoNowFeedbackWidgetViewHolder(
                itemView = view,
                listener = feedbackWidgetListener
            )
            CategoryEmptyStateDividerViewHolder.LAYOUT -> CategoryEmptyStateDividerViewHolder(
                itemView = view
            )
            else -> super.createViewHolder(view, type)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        emptyStateNoResultListener = null
        productRecommendationListener = null
        categoryMenuListener = null
        feedbackWidgetListener = null
    }
}
