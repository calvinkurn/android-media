package com.tokopedia.topads.common.view.adapter.productimagepreview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyImageButton

private const val DEFAULT_SIZE = 1
class TopAdsProductImagePreviewAdapter() : RecyclerView.Adapter<TopAdsProductImagePreviewAdapter.ProductImagePreviewViewHolder>() {

    private var imageList: ArrayList<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImagePreviewViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_topads_product_image_preview, parent, false)
        return ProductImagePreviewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageList?.size ?: DEFAULT_SIZE
    }

    override fun onBindViewHolder(holder: ProductImagePreviewViewHolder, position: Int) {
        if(position==0){
            holder.productImage.setImageDrawable(holder.itemView.context.getResDrawable(R.drawable.ic_topads_product_image_preview))
            holder.deleteButton.hide()
        }else{
            holder.deleteButton.setImageDrawable(holder.itemView.context.getResDrawable(R.drawable.unify_chips_ic_close))
            holder.deleteButton.show()
        }
    }

    class ProductImagePreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage = itemView.findViewById<ImageUnify>(R.id.productImage)
        val deleteButton = itemView.findViewById<UnifyImageButton>(R.id.ivDelete)
    }
}