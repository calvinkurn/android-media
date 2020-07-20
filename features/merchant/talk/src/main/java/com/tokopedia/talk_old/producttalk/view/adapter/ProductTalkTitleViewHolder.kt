package com.tokopedia.talk_old.producttalk.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.producttalk.view.viewmodel.ProductTalkTitleViewModel

class ProductTalkTitleViewHolder(val v: View) :
        AbstractViewHolder<ProductTalkTitleViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.product_talk_item_product_header
    }

    val productImage: ImageView = itemView.findViewById(R.id.talkProductAvatar)
    val productName : TextView = itemView.findViewById(R.id.talkProductName)
    val productPrice : TextView = itemView.findViewById(R.id.talkProductPrice)

    override fun bind(element: ProductTalkTitleViewModel) {
        productName.text = element.name
        ImageHandler.loadImage(itemView.context, productImage, element.avatar, com.tokopedia.design.R.drawable.loading_page)
        productPrice.text = element.price

    }

}
