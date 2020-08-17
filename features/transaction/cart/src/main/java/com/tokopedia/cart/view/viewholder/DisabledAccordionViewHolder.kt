package com.tokopedia.cart.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData

class DisabledAccordionViewHolder(itemView: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(itemView) {

    private val textAccordion by lazy {
        itemView.findViewById<TextView>(R.id.text_accordion)
    }
    private val imgChevron by lazy {
        itemView.findViewById<ImageView>(R.id.img_chevron)
    }

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_accordion
    }

    fun bind(data: DisabledAccordionHolderData) {
        if (data.isCollapsed) {
            imgChevron.rotation = 180f
            textAccordion.text = "Tampilkan lebih Banyak"
        } else {
            imgChevron.rotation = 0f
            textAccordion.text = "Tampilkan lebih Sedikit"
        }
    }
}