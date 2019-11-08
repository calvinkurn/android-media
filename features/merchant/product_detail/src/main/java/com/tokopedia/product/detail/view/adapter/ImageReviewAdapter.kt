package com.tokopedia.product.detail.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.OnImageReviewClick
import com.tokopedia.product.detail.data.util.OnSeeAllReviewClick
import kotlinx.android.synthetic.main.item_image_review.view.*

class ImageReviewAdapter(private val imageReviews: MutableList<ImageReviewItem> = mutableListOf(),
                         private val showSeeAll: Boolean = true,
                         private val onOnImageReviewClick: OnImageReviewClick? = null,
                         private val onOnSeeAllReviewClick: OnSeeAllReviewClick? = null) :
        RecyclerView.Adapter<ImageReviewAdapter.ImageReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageReviewViewHolder {
        return ImageReviewViewHolder(parent.inflateLayout(R.layout.item_image_review))
    }

    override fun getItemCount(): Int = imageReviews.size

    override fun onBindViewHolder(holder: ImageReviewViewHolder, position: Int) {
        holder.bind(imageReviews[position], getItemViewType(position), imageReviews)
    }

    override fun getItemViewType(position: Int): Int {
        return if (showSeeAll && position == TOTAL_REVIEW_IMAGE_VISIBLE - 1) VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER else VIEW_TYPE_IMAGE
    }

    inner class ImageReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: ImageReviewItem, type: Int, listItem: List<ImageReviewItem>) {
            with(view) {
                ImageHandler.loadImageRounded(view.context, image_review, item.imageUrlThumbnail, 16F)
                if (type == VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER) {
                    overlay_see_all.visible()
                    txt_see_all.text = item.imageCount
                    txt_see_all.visible()
                    setOnClickListener {
                        onOnSeeAllReviewClick?.invoke()
                    }
                } else {
                    overlay_see_all.gone()
                    txt_see_all.gone()
                    setOnClickListener {
                        onOnImageReviewClick?.invoke(listItem, adapterPosition)
                    }
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_IMAGE = 77
        private const val VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER = 88
        private const val TOTAL_REVIEW_IMAGE_VISIBLE = 4
    }
}