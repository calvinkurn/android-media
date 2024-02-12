package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowDividerTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowHeaderSpaceTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowHeaderTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTitleTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowDividerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderSpaceViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTitleViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListProductInCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListProductInCartViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListTopCheckAllViewHolder

class ShoppingListAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val headerListener: TokoNowHeaderViewHolder.TokoNowHeaderListener? = null,
    private val chooseAddressListener: TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener? = null,
    private val shoppingListHorizontalProductCardItemListener: ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener? = null
):
    BaseAdapterTypeFactory(),
    ShoppingListTypeFactory,
    ShoppingListHorizontalProductCardItemTypeFactory,
    TokoNowHeaderTypeFactory,
    TokoNowHeaderSpaceTypeFactory,
    TokoNowDividerTypeFactory,
    TokoNowTitleTypeFactory
{
    override fun type(uiModel: TokoNowHeaderUiModel): Int = TokoNowHeaderViewHolder.LAYOUT
    override fun type(uiModel: TokoNowHeaderSpaceUiModel): Int = TokoNowHeaderSpaceViewHolder.LAYOUT
    override fun type(uiModel: TokoNowDividerUiModel): Int = TokoNowDividerViewHolder.LAYOUT
    override fun type(uiModel: TokoNowTitleUiModel): Int = TokoNowTitleViewHolder.LAYOUT

    override fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListProductInCartUiModel): Int = ShoppingListProductInCartViewHolder.LAYOUT
    override fun type(uiModel: ShoppingListTopCheckAllUiModel): Int = ShoppingListTopCheckAllViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TokoNowHeaderViewHolder.LAYOUT -> TokoNowHeaderViewHolder(
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

            ShoppingListHorizontalProductCardItemViewHolder.LAYOUT -> ShoppingListHorizontalProductCardItemViewHolder(
                itemView = parent,
                listener = shoppingListHorizontalProductCardItemListener
            )
            ShoppingListProductInCartViewHolder.LAYOUT -> ShoppingListProductInCartViewHolder(
                itemView = parent
            )
            ShoppingListTopCheckAllViewHolder.LAYOUT -> ShoppingListTopCheckAllViewHolder(
                itemView = parent
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
