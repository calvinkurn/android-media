package com.tokopedia.talk.talkdetails.view.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsProductAttachViewModel

/**
 * Created by Hendri on 29/08/18.
 */
class TalkDetailsProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val productName:TextView = itemView.findViewById(R.id.attach_product_chat_name)
    val productPrice:TextView = itemView.findViewById(R.id.attach_product_chat_price)
    val productImage:ImageView = itemView.findViewById(R.id.attach_product_chat_image)

    fun bind(element:TalkDetailsProductAttachViewModel?){
        element?.let {
            productName.text = it.name
            productPrice.text = it.price
            ImageHandler.loadImageRounded2Target(itemView.context, productImage, it.image ?: "")
        }
    }

    companion object {
        val LAYOUT = R.layout.attach_product_base_layout
    }
}