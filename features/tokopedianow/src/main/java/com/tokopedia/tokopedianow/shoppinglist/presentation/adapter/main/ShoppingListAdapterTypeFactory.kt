package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowDividerTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowErrorTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowHeaderSpaceTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowThematicHeaderTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTitleTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowDividerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderSpaceViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowThematicHeaderViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTitleViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common.ShoppingListHorizontalProductCardItemTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListEmptyViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListExpandCollapseViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListRetryViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListProductCartViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListTopCheckAllViewHolder

class ShoppingListAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val headerListener: TokoNowThematicHeaderViewHolder.TokoNowHeaderListener? = null,
    private val chooseAddressListener: TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener? = null,
    private val productCardItemListener: ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener? = null,
    private val retryListener: ShoppingListRetryViewHolder.ShoppingListRetryListener? = null,
    private val errorListener: TokoNowErrorViewHolder.TokoNowErrorListener? = null,
    private val expandCollapseListener: ShoppingListExpandCollapseViewHolder.ShoppingListExpandCollapseListener? = null,
    private val topCheckAllListener: ShoppingListTopCheckAllViewHolder.ShoppingListTopCheckAllListener? = null
):
    BaseAdapterTypeFactory(),
    ShoppingListTypeFactory,
    ShoppingListHorizontalProductCardItemTypeFactory,
    TokoNowThematicHeaderTypeFactory,
    TokoNowHeaderSpaceTypeFactory,
    TokoNowDividerTypeFactory,
    TokoNowTitleTypeFactory,
    TokoNowErrorTypeFactory
{
    override fun type(uiModel: TokoNowThematicHeaderUiModel): Int = TokoNowThematicHeaderViewHolder.LAYOUT
    override fun type(uiModel: TokoNowHeaderSpaceUiModel): Int = TokoNowHeaderSpaceViewHolder.LAYOUT
    override fun type(uiModel: TokoNowDividerUiModel): Int = TokoNowDividerViewHolder.LAYOUT
    override fun type(uiModel: TokoNowTitleUiModel): Int = TokoNowTitleViewHolder.LAYOUT
    override fun type(viewModel: LoadingMoreModel?): Int = TokoNowLoadingMoreViewHolder.LAYOUT
    override fun type(uiModel: TokoNowErrorUiModel): Int = TokoNowErrorViewHolder.LAYOUT

    override fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListProductCartUiModel): Int = ShoppingListProductCartViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListTopCheckAllUiModel): Int = ShoppingListTopCheckAllViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListRetryUiModel): Int = ShoppingListRetryViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListEmptyUiModel): Int = ShoppingListEmptyViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListExpandCollapseUiModel): Int = ShoppingListExpandCollapseViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TokoNowThematicHeaderViewHolder.LAYOUT -> TokoNowThematicHeaderViewHolder(
                itemView = parent,
                listener = headerListener,
                chooseAddressListener = chooseAddressListener,
                tokoNowView = tokoNowView
            )
            TokoNowHeaderSpaceViewHolder.LAYOUT -> TokoNowHeaderSpaceViewHolder(
                itemView = parent
            )
            TokoNowDividerViewHolder.LAYOUT -> TokoNowDividerViewHolder(
                itemView = parent
            )
            TokoNowTitleViewHolder.LAYOUT -> TokoNowTitleViewHolder(
                itemView = parent
            )
            TokoNowLoadingMoreViewHolder.LAYOUT -> TokoNowLoadingMoreViewHolder(
                itemView = parent
            )
            TokoNowErrorViewHolder.LAYOUT -> TokoNowErrorViewHolder(
                itemView = parent,
                listener = errorListener
            )

            ShoppingListHorizontalProductCardItemViewHolder.LAYOUT -> ShoppingListHorizontalProductCardItemViewHolder(
                itemView = parent,
                listener = productCardItemListener
            )
            ShoppingListProductCartViewHolder.LAYOUT -> ShoppingListProductCartViewHolder(
                itemView = parent
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
            else -> super.createViewHolder(parent, type)
        }
    }
}
