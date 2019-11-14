package com.tokopedia.product.detail.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.review.ImageAttachment
import com.tokopedia.product.detail.data.util.OnImageReviewClicked
import kotlinx.android.synthetic.main.item_most_helpful_image.view.*


class MostHelpfulReviewAdapter(private val imageList: List<ImageAttachment>,
                               private val reviewId: String,
                               private val moreItemCount: Int = 0,
                               private val onImageHelpfulReviewClick: OnImageReviewClicked? = null) : RecyclerView.Adapter<MostHelpfulReviewAdapter.MostHelpfulReviewViewHolder>() {

    companion object {
        const val VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER = 122
        const val VIEW_TYPE_IMAGE = 133
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MostHelpfulReviewViewHolder {
        return MostHelpfulReviewViewHolder(parent.inflateLayout(R.layout.item_most_helpful_image))
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: MostHelpfulReviewViewHolder, position: Int) {
        when (imageList.size) {
            1 -> render1Image(holder)
            2 -> render2Image(holder)
            3 -> render3Image(position, holder)
        }

        holder.itemView.setOnClickListener {
            onImageHelpfulReviewClick?.invoke(imageList.map { it.imageUrl }, position, reviewId)
        }

        holder.bind(imageList[position], getItemViewType(position), moreItemCount)
    }

    override fun getItemViewType(position: Int): Int {
        return if (moreItemCount != 0 && position == imageList.size - 1) VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER else VIEW_TYPE_IMAGE
    }

    private fun render3Image(position: Int, holder: MostHelpfulReviewViewHolder) {
        if (position == 0) {
            (holder.itemView.img_attachment_review.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "1:1"
        } else {
            (holder.itemView.img_attachment_review.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "2.1:1"
        }
    }

    private fun render1Image(holder: MostHelpfulReviewViewHolder) {
        (holder.itemView.img_attachment_review.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "h,2:1"
    }

    private fun render2Image(holder: MostHelpfulReviewViewHolder) {
        (holder.itemView.img_attachment_review.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "1:1"
    }

    inner class MostHelpfulReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: ImageAttachment, type: Int, moreItemCount: Int) {
            ImageHandler.loadImageRounded(itemView.context, itemView.img_attachment_review, image.imageUrl, 16F)
            if (type == VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER) {
                itemView.overlay_see_more.show()
                itemView.txt_see_more.text = itemView.context.getString(R.string.txt_more_item_plus, moreItemCount)
            } else {
                itemView.overlay_see_more.hide()
            }
        }

    }
}
