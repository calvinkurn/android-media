package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopItemAdapterModel

class ShopNameViewHolder(itemView: View) : AbstractViewHolder<ShopItemAdapterModel>(itemView) {
    init {
        val tvShopName = itemView.findViewById<TextView>(R.id.tvShopName)
    }

    override fun bind(element: ShopItemAdapterModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        val LAYOUT_ID = R.layout.thank_item_shop_name
    }

}