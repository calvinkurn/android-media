package com.tokopedia.talk.talkdetails.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel

class AttachingProductListAdapter(var data: ArrayList<TalkProductAttachmentViewModel>,
                                  var listener: ProductAttachingItemClickListener) :
        RecyclerView.Adapter<AttachedProductViewHolder>() {

    interface ProductAttachingItemClickListener {
        fun onDeleteAttachProduct(element: TalkProductAttachmentViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.attached_product_item, parent, false)
        return AttachedProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AttachedProductViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    fun clearAllElements() {
        data.clear()
        notifyDataSetChanged()
    }

    fun remove(element: TalkProductAttachmentViewModel) {
        val position = data.indexOf(element)
        data.remove(element)
        notifyItemRemoved(position)
    }
}

class AttachedProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image = itemView.findViewById<ImageView>(R.id.attach_product_chat_image)
    val name = itemView.findViewById<TextView>(R.id.attach_product_chat_name)
    val price = itemView.findViewById<TextView>(R.id.attach_product_chat_price)
    val deleteButton = itemView.findViewById<ImageView>(R.id.delete_button)

    fun bind(element: TalkProductAttachmentViewModel,
             listener: AttachingProductListAdapter.ProductAttachingItemClickListener) {
        ImageHandler.loadImageRounded2(itemView.context, image, element.productImage)
        name.text = element.productName
        name.maxLines = 1
        price.text = element.productPrice

        deleteButton.visibility = View.VISIBLE
        deleteButton.setOnClickListener {
            listener.onDeleteAttachProduct(element)
        }
    }
}