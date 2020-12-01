package com.tokopedia.topads.common.view.adapter.productimagepreview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.TopAdsProductImagePreviewWidget
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyImageButton

private const val DEFAULT_SIZE = 1

class TopAdsProductImagePreviewAdapter : RecyclerView.Adapter<TopAdsProductImagePreviewAdapter.ProductImagePreviewViewHolder>() {

    private var imageList: ArrayList<String>? = null

    private var topAdsImagePreviewClick: TopAdsProductImagePreviewWidget.TopAdsImagePreviewClick? = null

    fun setSelectedProductList(imageList: ArrayList<String>) {
        this.imageList = imageList
        this.imageList?.add(0, "")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImagePreviewViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_topads_product_image_preview, parent, false)
        return ProductImagePreviewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageList?.size ?: DEFAULT_SIZE
    }

    override fun onBindViewHolder(holder: ProductImagePreviewViewHolder, position: Int) {
        if (position == 0) {
            holder.productImage.setImageDrawable(holder.itemView.context.getResDrawable(R.drawable.ic_topads_product_image_preview))
            holder.productImage.setOnClickListener {
                topAdsImagePreviewClick?.onClickPreview(0)
            }
            holder.deleteButton.hide()
        } else {
            imageList?.getOrNull(position)?.let {
                holder.productImage.loadImage(it)
                holder.deleteButton.setImageDrawable(holder.itemView.context.getResDrawable(R.drawable.unify_chips_ic_close))
                holder.deleteButton.show()
                holder.deleteButton.setOnClickListener {
                    imageList?.removeAt(position)
                    topAdsImagePreviewClick?.onDeletePreview(position - 1)
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun setAdapterTopAdsPreviewClick(topAdsImagePreviewClick: TopAdsProductImagePreviewWidget.TopAdsImagePreviewClick?) {
        this.topAdsImagePreviewClick = topAdsImagePreviewClick
    }

    class ProductImagePreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageUnify = itemView.findViewById(R.id.productImage)
        val deleteButton: UnifyImageButton = itemView.findViewById(R.id.ivDelete)
    }
}