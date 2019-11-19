package com.tokopedia.talk.common.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel

/**
 * @author by nisie on 9/6/18.
 */

class TalkProductAttachmentAdapter(private val listener: ProductAttachmentItemClickListener,
                                   private var listProduct: ArrayList<TalkProductAttachmentViewModel>)
    : RecyclerView.Adapter<TalkProductAttachmentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.attach_product_chat_image)
        val productName: TextView = itemView.findViewById(R.id.attach_product_chat_name)
        val productPrice: TextView = itemView.findViewById(R.id.attach_product_chat_price)
    }

    interface ProductAttachmentItemClickListener {
        fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.talk_product_attachment))

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageHandler.LoadImage(holder.productImage,
                listProduct[position].productImage)

        holder.itemView.setOnClickListener {
            listener.onClickProductAttachment(listProduct[position])
        }

        holder.productName.text = MethodChecker.fromHtml(listProduct[position].productName)
        holder.productPrice.text = MethodChecker.fromHtml(listProduct[position].productPrice)

    }

    fun getItem(position: Int): TalkProductAttachmentViewModel {
        return listProduct[position]
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

        ImageHandler.clearImage(holder.productImage)
    }
}

private fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

