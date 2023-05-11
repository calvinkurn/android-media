package com.tokopedia.tokopedianow.category.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryHeaderSpaceViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryTitleViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryTitleViewHolder.CategoryTitleListener
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener

class CategoryAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowCategoryMenuListener: TokoNowCategoryMenuListener? = null,
    private val categoryTitleListener: CategoryTitleListener? = null
): BaseAdapterTypeFactory(),
    CategoryTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowCategoryMenuTypeFactory
{
    /* Category Component Ui Model */
    override fun type(uiModel: CategoryHeaderSpaceUiModel): Int = CategoryHeaderSpaceViewHolder.LAYOUT
    override fun type(uiModel: CategoryTitleUiModel): Int = CategoryTitleViewHolder.LAYOUT

    /* Global Component Ui Model */
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            /* Category Component View Holder */
            CategoryHeaderSpaceViewHolder.LAYOUT -> CategoryHeaderSpaceViewHolder(
                itemView = view
            )
            CategoryTitleViewHolder.LAYOUT -> CategoryTitleViewHolder(
                itemView = view,
                listener = categoryTitleListener
            )

            /* Global Component View Holder */
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(
                itemView = view,
                tokoNowView = tokoNowView,
                tokoNowChooseAddressWidgetListener = tokoNowChooseAddressWidgetListener
            )
            TokoNowCategoryMenuViewHolder.LAYOUT -> TokoNowCategoryMenuViewHolder(
                itemView = view,
                listener = tokoNowCategoryMenuListener
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
