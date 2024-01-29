package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowHeaderSpaceTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowHeaderTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderSpaceViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.HorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.seeallcategory.persentation.viewholder.SeeAllCategoryItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.HorizontalProductCardItemViewHolder

class ShoppingListAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val headerListener: TokoNowHeaderViewHolder.TokoNowHeaderListener? = null,
    private val chooseAddressListener: TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener? = null,
):
    BaseAdapterTypeFactory(),
    ShoppingListTypeFactory,
    TokoNowHeaderTypeFactory,
    TokoNowHeaderSpaceTypeFactory
{
    override fun type(uiModel: TokoNowHeaderUiModel): Int = TokoNowHeaderViewHolder.LAYOUT
    override fun type(uiModel: TokoNowHeaderSpaceUiModel): Int = TokoNowHeaderSpaceViewHolder.LAYOUT
    override fun type(uiModel: HorizontalProductCardItemUiModel): Int = SeeAllCategoryItemViewHolder.LAYOUT
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
            HorizontalProductCardItemViewHolder.LAYOUT -> HorizontalProductCardItemViewHolder(
                itemView = parent
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
