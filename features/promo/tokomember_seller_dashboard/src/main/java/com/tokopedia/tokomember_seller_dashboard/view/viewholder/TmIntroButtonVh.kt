package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroButtonItem
import kotlinx.android.synthetic.main.tm_dash_intro_button_item.view.*

class TmIntroButtonVh(private val listener: TokomemberIntroButtonListener, view: View)
    : AbstractViewHolder<TokomemberIntroButtonItem>(view) {

    private val btnContinue = itemView.btnContinue

    override fun bind(element: TokomemberIntroButtonItem?) {
        element?.apply {
            btnContinue.text = element.text
        }
        btnContinue.setOnClickListener {
            listener.onButtonItemClick(adapterPosition)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_button_item
    }

    interface TokomemberIntroButtonListener {
        fun onButtonItemClick(position: Int)
    }

}
