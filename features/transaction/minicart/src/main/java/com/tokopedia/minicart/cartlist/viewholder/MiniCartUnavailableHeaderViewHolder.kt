package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.unifyprinciples.Typography

class MiniCartUnavailableHeaderViewHolder(private val view: View,
                                          private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartUnavailableHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_unavailable_header
    }

    private val textDisabledItemCount: Typography? by lazy {
        view.findViewById(R.id.text_disabled_item_count)
    }
    private val textDelete: Typography? by lazy {
        view.findViewById(R.id.text_delete)
    }

    override fun bind(element: MiniCartUnavailableHeaderUiModel) {
        val message = textDisabledItemCount?.context?.getString(R.string.mini_cart_title_unavailable_header, element.unavailableItemCount) ?: ""
        textDisabledItemCount?.text = message
        textDelete?.setOnClickListener {
            listener.onBulkDeleteUnavailableItems()
        }
    }

}