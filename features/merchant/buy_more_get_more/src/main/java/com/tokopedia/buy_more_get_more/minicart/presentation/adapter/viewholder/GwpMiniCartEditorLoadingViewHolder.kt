package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class GwpMiniCartEditorLoadingViewHolder(
    itemView: View
) : AbstractViewHolder<GwpMiniCartEditorVisitable.MiniCartEditorLoadingState>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_gwp_mini_cart_editor_loading_state
    }

    override fun bind(element: GwpMiniCartEditorVisitable.MiniCartEditorLoadingState) {
        /* no-op */
    }
}