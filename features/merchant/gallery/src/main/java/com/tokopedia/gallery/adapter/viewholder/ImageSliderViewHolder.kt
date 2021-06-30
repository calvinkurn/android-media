package com.tokopedia.gallery.adapter.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.gallery.R
import com.tokopedia.gallery.customview.RatingView
import com.tokopedia.gallery.viewmodel.ImageReviewItem

class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.review_image_slider_item
    }

    private val imageView: ImageView = itemView.findViewById(R.id.review_image_slider_item_image_view)
    private val date: TextView = itemView.findViewById(R.id.review_image_slider_date)
    private val name: TextView = itemView.findViewById(R.id.review_image_slider_name)
    private val reviewContainer: View = itemView.findViewById(R.id.review_image_slider_container)
    private val rating: ImageView = itemView.findViewById(R.id.review_image_slider_rating)

    fun bind(item: ImageReviewItem) {
        ImageHandler.LoadImage(imageView, item.imageUrlLarge)

        if (!TextUtils.isEmpty(item.reviewerName)) {
            name.text = item.reviewerName
            reviewContainer.visibility = View.VISIBLE
        } else {
            reviewContainer.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(item.formattedDate)) {
            date.text = item.formattedDate
            date.visibility = View.VISIBLE
        } else {
            date.visibility = View.GONE
        }

        if (item.rating != ImageReviewItem.NO_RATING_DATA) {
            ImageHandler.loadImageRounded2(itemView.context, rating, RatingView.getRatingDrawable(item.rating), 0f)
            rating.visibility = View.VISIBLE
        } else {
            rating.visibility = View.GONE
        }
    }
}