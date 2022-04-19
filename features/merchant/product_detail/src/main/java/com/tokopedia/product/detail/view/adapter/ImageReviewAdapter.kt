package com.tokopedia.product.detail.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.OnImageReviewClick
import com.tokopedia.product.detail.data.util.OnSeeAllReviewClick
import com.tokopedia.product.detail.databinding.ItemImageReviewBinding

class ImageReviewAdapter(private val imageReviews: MutableList<ImageReviewItem> = mutableListOf(),
                         private val showSeeAll: Boolean = true,
                         private val onOnImageReviewClick: OnImageReviewClick? = null,
                         private val onOnSeeAllReviewClick: OnSeeAllReviewClick? = null,
                         private val componentTrackDataModel: ComponentTrackDataModel? = null) :
        RecyclerView.Adapter<ImageReviewAdapter.ImageReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageReviewViewHolder {
            return ImageReviewViewHolder(parent.inflateLayout(R.layout.item_image_review))
    }

    override fun getItemCount(): Int = imageReviews.size

    override fun onBindViewHolder(holder: ImageReviewViewHolder, position: Int) {
        holder.bind(imageReviews[position], getItemViewType(position), imageReviews)
    }

    override fun getItemViewType(position: Int): Int {
        return if ((showSeeAll && position == TOTAL_REVIEW_IMAGE_VISIBLE_NEW_VIEWHOLDER - 1))  {
            VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER
        }
        else VIEW_TYPE_IMAGE
    }

    inner class ImageReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemImageReviewBinding.bind(view)

        fun bind(item: ImageReviewItem, type: Int, listItem: List<ImageReviewItem>) {
            with(binding) {
                imageReview.loadImageRounded(item.imageUrlThumbnail, ROUNDED_IMAGE_EDGES)
                if (type == VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER) {
                    overlaySeeAll.visible()
                    txtSeeAll.text = item.imageCount
                    txtSeeAll.visible()
                    view.setOnClickListener {
                        onOnSeeAllReviewClick?.invoke(componentTrackDataModel)
                    }
                } else {
                    overlaySeeAll.gone()
                    txtSeeAll.gone()
                    view.setOnClickListener {
                        onOnImageReviewClick?.invoke(listItem, adapterPosition, componentTrackDataModel, item.rawImageCount ?: "")
                    }
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_IMAGE = 77
        private const val VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER = 88
        private const val TOTAL_REVIEW_IMAGE_VISIBLE_NEW_VIEWHOLDER = 5
        private const val ROUNDED_IMAGE_EDGES = 16f
    }
}