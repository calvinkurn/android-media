package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.GwpMiniCartEditorAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.GwpMiniCartEditorErrorViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.GwpMiniCartEditorGiftWidgetViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.GwpMiniCartEditorLoadingViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.GwpMiniCartEditorMessageViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.GwpMiniCartEditorProductViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class GwpMiniCartEditorAdapterFactoryImpl(
    private val listener: GwpMiniCartEditorAdapter.Listener
) : BaseAdapterTypeFactory(), BmgmMiniCartAdapterFactory {

    override fun type(model: GwpMiniCartEditorVisitable.GiftMessageUiModel): Int {
        return GwpMiniCartEditorMessageViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmMiniCartVisitable.ProductUiModel): Int {
        return GwpMiniCartEditorProductViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmMiniCartVisitable.GwpGiftWidgetUiModel): Int {
        return GwpMiniCartEditorGiftWidgetViewHolder.RES_LAYOUT
    }

    override fun type(model: GwpMiniCartEditorVisitable.MiniCartEditorLoadingState): Int {
        return GwpMiniCartEditorLoadingViewHolder.RES_LAYOUT
    }

    override fun type(model: GwpMiniCartEditorVisitable.MiniCartEditorErrorState): Int {
        return GwpMiniCartEditorErrorViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            GwpMiniCartEditorMessageViewHolder.RES_LAYOUT -> GwpMiniCartEditorMessageViewHolder(
                parent
            )

            GwpMiniCartEditorProductViewHolder.RES_LAYOUT -> GwpMiniCartEditorProductViewHolder(
                parent, listener
            )

            GwpMiniCartEditorGiftWidgetViewHolder.RES_LAYOUT -> GwpMiniCartEditorGiftWidgetViewHolder(
                parent, listener
            )

            GwpMiniCartEditorLoadingViewHolder.RES_LAYOUT -> GwpMiniCartEditorLoadingViewHolder(
                parent
            )

            GwpMiniCartEditorErrorViewHolder.RES_LAYOUT -> GwpMiniCartEditorErrorViewHolder(
                parent, listener
            )

            else -> super.createViewHolder(parent, type)
        }
    }
}