package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemGwpMiniCartEditorErrorStateBinding
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.GwpMiniCartEditorAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable
import com.tokopedia.media.loader.loadImage
import com.tokopedia.globalerror.R as globalerrorR

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class GwpMiniCartEditorErrorViewHolder(
    itemView: View,
    private val listener: GwpMiniCartEditorAdapter.Listener
) : AbstractViewHolder<GwpMiniCartEditorVisitable.MiniCartEditorErrorState>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_gwp_mini_cart_editor_error_state
    }

    private val binding by lazy {
        ItemGwpMiniCartEditorErrorStateBinding.bind(itemView)
    }

    override fun bind(element: GwpMiniCartEditorVisitable.MiniCartEditorErrorState) {
        with(binding) {
            imgError.loadImage(globalerrorR.drawable.unify_globalerrors_404)
            btnReload.setOnClickListener {
                listener.onReloadClicked()
            }
        }
    }
}