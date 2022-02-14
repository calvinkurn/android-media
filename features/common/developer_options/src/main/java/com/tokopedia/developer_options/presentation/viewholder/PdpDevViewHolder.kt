package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.ProductDetailDevActivity
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.unifycomponents.UnifyButton

class PdpDevViewHolder(
    itemView: View
): AbstractViewHolder<PdpDevUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dev_opt_pdp_dev
    }

    override fun bind(element: PdpDevUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.pdp_dev_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                val intent = Intent(this, ProductDetailDevActivity::class.java)
                startActivity(intent)
            }
        }
    }
}