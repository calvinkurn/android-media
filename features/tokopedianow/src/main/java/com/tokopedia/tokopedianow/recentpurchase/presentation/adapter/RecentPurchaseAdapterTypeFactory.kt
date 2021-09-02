package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.listener.RepurchaseProductCardListener
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductGridUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseEmptyStateNoHistoryViewHolder
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseLoadingViewHolder
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseProductGridViewHolder
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseSortFilterViewHolder

class RecentPurchaseAdapterTypeFactory(
    private val tokoNowListener: TokoNowView? = null,
    private val tokoNowCategoryGridListener: TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener? = null,
    private val productCardListener: RepurchaseProductCardListener,
    private val emptyStateNoHistorylistener: RepurchaseEmptyStateNoHistoryViewHolder.RepurchaseEmptyStateNoHistoryListener? = null,
    private val tokoNowRecommendationCarouselListener: TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener? = null,
    private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener
) : BaseAdapterTypeFactory(), RecentPurchaseTypeFactory, TokoNowTypeFactory {

    // region Common TokoNow Component
    override fun type(uiModel: TokoNowCategoryGridUiModel): Int = TokoNowCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: TokoNowRecentPurchaseUiModel): Int = TokoNowRecentPurchaseViewHolder.LAYOUT
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowRecommendationCarouselUiModel): Int = TokoNowRecommendationCarouselViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel): Int = TokoNowEmptyStateNoResultViewHolder.LAYOUT
    // endregion

    // region Repurchase Component
    override fun type(uiModel: RepurchaseProductGridUiModel): Int = RepurchaseProductGridViewHolder.LAYOUT
    override fun type(uiModel: RepurchaseLoadingUiModel): Int = RepurchaseLoadingViewHolder.LAYOUT
    override fun type(uiModel: RepurchaseEmptyStateNoHistoryUiModel): Int = RepurchaseEmptyStateNoHistoryViewHolder.LAYOUT
    override fun type(uiModel: RepurchaseSortFilterUiModel): Int = RepurchaseSortFilterViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            // region Common TokoNow Component
            TokoNowCategoryGridViewHolder.LAYOUT -> TokoNowCategoryGridViewHolder(view, tokoNowCategoryGridListener)
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(view, tokoNowListener, tokoNowChooseAddressWidgetListener)
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(view, tokoNowListener)
            TokoNowRecommendationCarouselViewHolder.LAYOUT -> TokoNowRecommendationCarouselViewHolder(view, tokoNowRecommendationCarouselListener)
            TokoNowEmptyStateNoResultViewHolder.LAYOUT -> TokoNowEmptyStateNoResultViewHolder(view, tokoNowEmptyStateNoResultListener)
            // endregion

            // region Repurchase Component
            RepurchaseProductGridViewHolder.LAYOUT -> RepurchaseProductGridViewHolder(view, productCardListener)
            RepurchaseLoadingViewHolder.LAYOUT -> RepurchaseLoadingViewHolder(view)
            RepurchaseEmptyStateNoHistoryViewHolder.LAYOUT -> RepurchaseEmptyStateNoHistoryViewHolder(view, emptyStateNoHistorylistener)
            RepurchaseSortFilterViewHolder.LAYOUT -> RepurchaseSortFilterViewHolder(view)
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }
}