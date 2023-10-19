package com.tokopedia.createpost.view.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.createpost.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify

/**
 * @author by shruti on 01/08/21.
 */

class CaptionPagePreviewImageAdapter(
    private val onItemClick: ((Int) -> Unit)? = null,
) : RecyclerView.Adapter<CaptionPagePreviewImageAdapter.ContentCaptionPostViewHolder>() {

    private val itemList: MutableList<MediaModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContentCaptionPostViewHolder {
        return ContentCaptionPostViewHolder(parent.inflateLayout(R.layout.content_preview_post_view_item))

    }

    override fun onBindViewHolder(holder: ContentCaptionPostViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ContentCaptionPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val previewPageImage: ImageUnify = view.findViewById(R.id.content_preview_page_image)
        private val previewPageProductTag: CardView = view.findViewById(R.id.content_preview_page_product_tag)

        fun bind(imageItemData: MediaModel) {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
            previewPageImage.setImageUrl(imageItemData.path)
            previewPageProductTag.showWithCondition(imageItemData.products.isNotEmpty())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProduct(products: List<MediaModel>) {
        this.itemList.clear()
        this.itemList.addAll(products)
        notifyDataSetChanged()
    }

}
