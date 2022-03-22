package com.tokopedia.imagepicker_insta.views.adapters

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.CreatorListData
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.media.loader.loadImageCircle
import kotlinx.android.synthetic.main.imagepicker_insta_content_creator_item.view.*

class ContentCreatorListAdapter(
    private val itemList: MutableList<CreatorListData> = mutableListOf(),
) :
    RecyclerView.Adapter<ContentCreatorListAdapter.ContentCreatorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentCreatorViewHolder {
        return ContentCreatorViewHolder(parent.inflateLayout(R.layout.imagepicker_insta_content_creator_item))

    }

    override fun onBindViewHolder(holder: ContentCreatorViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class ContentCreatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(productData: CreatorListData) {
            with(itemView) {
                val contentCreatorIcon = findViewById<AppCompatImageView>(R.id.content_creator_icon)
                content_creator_name.text = productData.name
                contentCreatorIcon.loadImageCircle(productData.icon)

            }
        }
    }

    fun updateProduct(products: List<CreatorListData>) {
        this.itemList.clear()
        this.itemList.addAll(products)
        notifyDataSetChanged()
    }
}