package com.tokopedia.shop.newinfo.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.newinfo.view.adapter.ShopNewInfoAdapter

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var displayProductsView: TextView? = null
    private var editEtalaseView: TextView? = null

    init {
        displayProductsView = itemView.findViewById(R.id.tv_display_products)
        editEtalaseView = itemView.findViewById(R.id.tv_edit_etalase)
    }

    fun bind(clickListener: ShopNewInfoAdapter.ProductItemClickListener) {
        displayProductsView?.setOnClickListener { clickListener.onDisplayProductsClicked() }
        editEtalaseView?.setOnClickListener { clickListener.onEditEtalaseClicked() }
    }
}