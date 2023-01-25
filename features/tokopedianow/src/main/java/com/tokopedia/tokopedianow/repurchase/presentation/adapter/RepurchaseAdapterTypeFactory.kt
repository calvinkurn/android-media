package com.tokopedia.tokopedianow.repurchase.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.listener.RepurchaseProductCardListener
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseEmptyStateNoHistoryViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseLoadingViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseSortFilterViewHolder
import com.tokopedia.tokopedianow.similarproduct.listener.SimilarProductListener

class RepurchaseAdapterTypeFactory(
    private val tokoNowListener: TokoNowView? = null,
    private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener? = null,
    private val tokoNowCategoryMenuListener: TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener? = null,
    private val productCardListener: RepurchaseProductCardListener,
    private val similarProductListener: SimilarProductListener,
    private val emptyStateNoHistorylistener: RepurchaseEmptyStateNoHistoryViewHolder.RepurchaseEmptyStateNoHistoryListener? = null,
    private val tokoNowRecommendationCarouselListener: TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener? = null,
    private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    private val sortFilterListener: RepurchaseSortFilterViewHolder.SortFilterListener,
    private val serverErrorListener: TokoNowServerErrorViewHolder.ServerErrorListener,
    private val tokonowRecomBindPageNameListener: TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener? = null,
    private val productRecommendationListener: TokoNowProductRecommendationListener? = null
) : BaseAdapterTypeFactory(),
    RepurchaseTypeFactory,
    TokoNowCategoryMenuTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowProductRecommendationOocTypeFactory,
    TokoNowEmptyStateNoResultTypeFactory,
    TokoNowServerErrorTypeFactory,
    TokoNowProductRecommendationTypeFactory
{

    // region Common TokoNow Component
    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProductRecommendationOocUiModel): Int = TokoNowProductRecommendationOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel): Int = TokoNowEmptyStateNoResultViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProductRecommendationUiModel): Int = TokoNowProductRecommendationViewHolder.LAYOUT
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
            TokoNowCategoryMenuViewHolder.LAYOUT -> TokoNowCategoryMenuViewHolder(view, tokoNowCategoryMenuListener)
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(view, tokoNowListener, tokoNowChooseAddressWidgetListener)
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(
                view,
                tokoNowEmptyStateOocListener
            )
            TokoNowProductRecommendationOocViewHolder.LAYOUT -> TokoNowProductRecommendationOocViewHolder(
                view,
                tokoNowRecommendationCarouselListener,
                tokonowRecomBindPageNameListener
            )
            TokoNowEmptyStateNoResultViewHolder.LAYOUT -> TokoNowEmptyStateNoResultViewHolder(
                view,
                tokoNowEmptyStateNoResultListener
            )
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(view, serverErrorListener)
            TokoNowProductRecommendationViewHolder.LAYOUT -> TokoNowProductRecommendationViewHolder(view, productRecommendationListener)
            // endregion

            // region Repurchase Component
            RepurchaseProductViewHolder.LAYOUT -> RepurchaseProductViewHolder(view, productCardListener, similarProductListener)
            RepurchaseLoadingViewHolder.LAYOUT -> RepurchaseLoadingViewHolder(view)
            RepurchaseEmptyStateNoHistoryViewHolder.LAYOUT -> RepurchaseEmptyStateNoHistoryViewHolder(view, emptyStateNoHistorylistener)
            RepurchaseSortFilterViewHolder.LAYOUT -> RepurchaseSortFilterViewHolder(view, sortFilterListener)
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }
}
