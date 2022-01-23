package com.tokopedia.catalog.adapter.gallery

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.loadImageRounded

class CatalogImageReviewAdapter(private val imageReviews: ArrayList<CatalogImage> = arrayListOf(),
                                private val showSeeAll: Boolean = true,
                                private val catalogDetailListener: CatalogDetailListener?) :
        RecyclerView.Adapter<CatalogImageReviewAdapter.ImageReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageReviewViewHolder {
            return ImageReviewViewHolder(parent.inflateLayout(R.layout.item_review_image))
    }

    override fun getItemCount(): Int = imageReviews.size

    override fun onBindViewHolder(holder: ImageReviewViewHolder, position: Int) {
        holder.bind(imageReviews[position], getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_IMAGE
    }

    inner class ImageReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: CatalogImage, type: Int) {
            with(view) {
                findViewById<AppCompatImageView>(R.id.image_review)?.loadImageRounded(item.imageURL ?: "", ROUNDED_IMAGE_EDGES)
                view.setOnClickListener {
                    catalogDetailListener?.onReviewImageClicked(adapterPosition,imageReviews)
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_IMAGE = 77
        private const val ROUNDED_IMAGE_EDGES = 16f
    }
}