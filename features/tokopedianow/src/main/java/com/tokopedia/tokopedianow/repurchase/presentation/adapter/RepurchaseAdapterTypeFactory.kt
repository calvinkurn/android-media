package com.tokopedia.tokopedianow.repurchase.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRecommendationCarouselTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.*
import com.tokopedia.tokopedianow.repurchase.presentation.listener.RepurchaseProductCardListener
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseEmptyStateNoHistoryViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseLoadingViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseSortFilterViewHolder

class RepurchaseAdapterTypeFactory(
    private val tokoNowListener: TokoNowView? = null,
    private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener? = null,
    private val tokoNowCategoryGridListener: TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener? = null,
    private val productCardListener: RepurchaseProductCardListener,
    private val emptyStateNoHistorylistener: RepurchaseEmptyStateNoHistoryViewHolder.RepurchaseEmptyStateNoHistoryListener? = null,
    private val tokoNowRecommendationCarouselListener: TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener? = null,
    private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    private val sortFilterListener: RepurchaseSortFilterViewHolder.SortFilterListener,
    private val serverErrorListener: TokoNowServerErrorViewHolder.ServerErrorListener,
    private val tokonowRecomBindPageNameListener: TokoNowRecommendationCarouselViewHolder.TokonowRecomBindPageNameListener? = null
) : BaseAdapterTypeFactory(),
    RepurchaseTypeFactory,
    TokoNowCategoryGridTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowRecommendationCarouselTypeFactory,
    TokoNowEmptyStateNoResultTypeFactory,
    TokoNowServerErrorTypeFactory {

    // region Common TokoNow Component
    override fun type(uiModel: TokoNowCategoryGridUiModel): Int = TokoNowCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowRecommendationCarouselUiModel): Int = TokoNowRecommendationCarouselViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel): Int = TokoNowEmptyStateNoResultViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT
    // endregion

    // region Repurchase Component
    override fun type(uiModel: RepurchaseProductUiModel): Int = RepurchaseProductViewHolder.LAYOUT
    override fun type(uiModel: RepurchaseLoadingUiModel): Int = RepurchaseLoadingViewHolder.LAYOUT
    override fun type(uiModel: RepurchaseEmptyStateNoHistoryUiModel): Int = RepurchaseEmptyStateNoHistoryViewHolder.LAYOUT
    override fun type(uiModel: RepurchaseSortFilterUiModel): Int = RepurchaseSortFilterViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            // region Common TokoNow Component
            TokoNowCategoryGridViewHolder.LAYOUT -> TokoNowCategoryGridViewHolder(view, tokoNowCategoryGridListener)
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(view, tokoNowListener, tokoNowChooseAddressWidgetListener)
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(
                view,
                tokoNowEmptyStateOocListener
            )
            TokoNowRecommendationCarouselViewHolder.LAYOUT -> TokoNowRecommendationCarouselViewHolder(
                view,
                tokoNowRecommendationCarouselListener,
                tokonowRecomBindPageNameListener
            )
            TokoNowEmptyStateNoResultViewHolder.LAYOUT -> TokoNowEmptyStateNoResultViewHolder(
                view,
                tokoNowEmptyStateNoResultListener
            )
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(view, serverErrorListener)
            // endregion

            // region Repurchase Component
            RepurchaseProductViewHolder.LAYOUT -> RepurchaseProductViewHolder(view, productCardListener)
            RepurchaseLoadingViewHolder.LAYOUT -> RepurchaseLoadingViewHolder(view)
            RepurchaseEmptyStateNoHistoryViewHolder.LAYOUT -> RepurchaseEmptyStateNoHistoryViewHolder(view, emptyStateNoHistorylistener)
            RepurchaseSortFilterViewHolder.LAYOUT -> RepurchaseSortFilterViewHolder(view, sortFilterListener)
            // endregion
            else -> super.createViewHolder(view, type)
        }.apply {
            if(this !is RepurchaseProductViewHolder) {
                applyFullSpan()
            }
        }
    }

    private fun AbstractViewHolder<*>.applyFullSpan(): AbstractViewHolder<*> {
        return apply {
            val layoutParams = itemView.layoutParams
                as? StaggeredGridLayoutManager.LayoutParams
            layoutParams?.isFullSpan = true
        }
    }
}