package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.unifycomponents.UnifyButton

class PdpDevViewHolder(
    itemView: View,
    private val listener: PdpDevListener
): AbstractViewHolder<PdpDevUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dev_opt_pdp_dev
    }

    override fun bind(element: PdpDevUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.pdp_dev_btn)
        btn.text = element.text
        btn.setOnClickListener {
           listener.onClickPdpDevBtn()
        }
    }

    interface PdpDevListener {
        fun onClickPdpDevBtn()
    }
}