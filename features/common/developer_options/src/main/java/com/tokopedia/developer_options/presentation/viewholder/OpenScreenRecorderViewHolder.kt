package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.OpenScreenRecorderUiModel
import com.tokopedia.unifycomponents.UnifyButton

class OpenScreenRecorderViewHolder(
    itemView: View,
    private val listener: OpenScreenRecorderListener
): AbstractViewHolder<OpenScreenRecorderUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_open_screen_recorder
    }

    override fun bind(element: OpenScreenRecorderUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.open_screen_recorder)
        btn.text = element.text
        btn.setOnClickListener {
            listener.onClickScreenRecorderBtn()
        }
    }

    interface OpenScreenRecorderListener {
        fun onClickScreenRecorderBtn()
    }
}