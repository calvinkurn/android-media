package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowDividerTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowErrorTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowLocalLoadTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowThematicHeaderTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTitleTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowLocalLoadUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowDividerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowThematicHeaderViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLocalLoadViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTitleViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common.ShoppingListHorizontalProductCardItemTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListLoadingMoreUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListEmptyViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListExpandCollapseViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListRetryViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListCartProductViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListLoadingMoreViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListTopCheckAllViewHolder

class ShoppingListAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val headerListener: TokoNowThematicHeaderViewHolder.TokoNowHeaderListener? = null,
    private val productCardItemListener: ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener? = null,
    private val retryListener: ShoppingListRetryViewHolder.ShoppingListRetryListener? = null,
    private val errorListener: TokoNowErrorViewHolder.TokoNowErrorListener? = null,
    private val expandCollapseListener: ShoppingListExpandCollapseViewHolder.ShoppingListExpandCollapseListener? = null,
    private val topCheckAllListener: ShoppingListTopCheckAllViewHolder.ShoppingListTopCheckAllListener? = null,
    private val cartProductListener: ShoppingListCartProductViewHolder.ShoppingListCartProductListener? = null,
    private val emptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener? = null,
    private val productRecommendationOocListener: TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener? = null,
    private val productRecommendationOocBindListener: TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener? = null,
    private val localLoadListener: TokoNowLocalLoadViewHolder.TokoNowLocalLoadListener? = null
):
    BaseAdapterTypeFactory(),
    ShoppingListTypeFactory,
    ShoppingListHorizontalProductCardItemTypeFactory,
    TokoNowThematicHeaderTypeFactory,
    TokoNowDividerTypeFactory,
    TokoNowTitleTypeFactory,
    TokoNowErrorTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowProductRecommendationOocTypeFactory,
    TokoNowLocalLoadTypeFactory
{
    override fun type(uiModel: TokoNowThematicHeaderUiModel): Int = TokoNowThematicHeaderViewHolder.LAYOUT
    override fun type(uiModel: TokoNowDividerUiModel): Int = TokoNowDividerViewHolder.LAYOUT
    override fun type(uiModel: TokoNowTitleUiModel): Int = TokoNowTitleViewHolder.LAYOUT
    override fun type(uiModel: TokoNowErrorUiModel): Int = TokoNowErrorViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProductRecommendationOocUiModel): Int = TokoNowProductRecommendationOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowLocalLoadUiModel): Int = TokoNowLocalLoadViewHolder.LAYOUT

    override fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListCartProductUiModel): Int = ShoppingListCartProductViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListTopCheckAllUiModel): Int = ShoppingListTopCheckAllViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListRetryUiModel): Int = ShoppingListRetryViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListEmptyUiModel): Int = ShoppingListEmptyViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListExpandCollapseUiModel): Int = ShoppingListExpandCollapseViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListLoadingMoreUiModel): Int = TokoNowLoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TokoNowThematicHeaderViewHolder.LAYOUT -> TokoNowThematicHeaderViewHolder(
                itemView = parent,
                listener = headerListener,
                tokoNowView = tokoNowView
            )
            TokoNowDividerViewHolder.LAYOUT -> TokoNowDividerViewHolder(
                itemView = parent
            )
            TokoNowTitleViewHolder.LAYOUT -> TokoNowTitleViewHolder(
                itemView = parent
            )
            TokoNowErrorViewHolder.LAYOUT -> TokoNowErrorViewHolder(
                itemView = parent,
                listener = errorListener
            )
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(
                itemView = parent,
                listener = emptyStateOocListener
            )
            TokoNowProductRecommendationOocViewHolder.LAYOUT -> TokoNowProductRecommendationOocViewHolder(
                itemView = parent,
                recommendationCarouselListener = productRecommendationOocListener,
                recommendationCarouselWidgetBindPageNameListener = productRecommendationOocBindListener
            )
            TokoNowLocalLoadViewHolder.LAYOUT -> TokoNowLocalLoadViewHolder(
                itemView = parent,
                listener = localLoadListener
            )

            ShoppingListHorizontalProductCardItemViewHolder.LAYOUT -> ShoppingListHorizontalProductCardItemViewHolder(
                itemView = parent,
                listener = productCardItemListener
            )
            ShoppingListCartProductViewHolder.LAYOUT -> ShoppingListCartProductViewHolder(
                itemView = parent,
                listener = cartProductListener
            )
            ShoppingListTopCheckAllViewHolder.LAYOUT -> ShoppingListTopCheckAllViewHolder(
                itemView = parent,
                listener = topCheckAllListener
            )
            ShoppingListRetryViewHolder.LAYOUT -> ShoppingListRetryViewHolder(
                itemView = parent,
                listener = retryListener
            )
            ShoppingListEmptyViewHolder.LAYOUT -> ShoppingListEmptyViewHolder(
                itemView = parent
            )
            ShoppingListExpandCollapseViewHolder.LAYOUT -> ShoppingListExpandCollapseViewHolder(
                itemView = parent,
                listener = expandCollapseListener
            )
            ShoppingListLoadingMoreViewHolder.LAYOUT -> ShoppingListLoadingMoreViewHolder(
                itemView = parent
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
