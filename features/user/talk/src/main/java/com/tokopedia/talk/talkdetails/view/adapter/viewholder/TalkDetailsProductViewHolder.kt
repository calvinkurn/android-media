package com.tokopedia.talk.talkdetails.view.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsHeaderProductViewModel

/**
 * Created by Hendri on 29/08/18.
 */
class TalkDetailsProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val productName:TextView = itemView.findViewById(R.id.productName)
    val productImage:ImageView = itemView.findViewById(R.id.productAvatar)

    fun bind(element:TalkDetailsHeaderProductViewModel?){
        element?.let {
            productName.text = it.name
            ImageHandler.loadImageRounded2Target(itemView.context, productImage, it.image ?: "")
        }
    }

    companion object {
        val LAYOUT = R.layout.inbox_talk_item_product_header
    }
}