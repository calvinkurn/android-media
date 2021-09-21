package com.tokopedia.shop.settings.common.view.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.shop.settings.R
import com.tokopedia.unifyprinciples.Typography

class MenuViewHolder(private val listener: ItemMenuListener?, view: View): BaseViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_menu
    }

    private fun setupUi(text: String) {
        itemView.apply {
            val tpMenu: Typography = findViewById(R.id.tp_menu)
            val divider: View = findViewById(R.id.divider)
            tpMenu.text = text
            if (adapterPosition == listener?.itemMenuSize()?.minus(1)) {
                divider.gone()
            }
        }.setOnClickListener {
            listener?.onItemMenuClicked(text, adapterPosition)
        }
    }

    fun bind(text: String) {
        setupUi(text)
    }

    interface ItemMenuListener {
        fun onItemMenuClicked(text: String, position: Int)
        fun itemMenuSize(): Int
    }
}