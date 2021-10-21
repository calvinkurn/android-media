package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload
import com.tokopedia.review.inbox.R
import java.io.File
import java.util.*

/**
 * Created by Nisie on 2/16/16.
 */
class ImageUploadAdapter constructor(var context: Context) :
    RecyclerView.Adapter<ImageUploadAdapter.ViewHolder>() {
    private var canUpload: Int = 0
    private var isReviewImage: Boolean = false

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<View>(R.id.image_upload) as ImageView
    }

    interface ProductImageListener {
        fun onUploadClicked(position: Int): View.OnClickListener
        fun onImageClicked(position: Int, imageUpload: ImageUpload?): View.OnClickListener
    }

    private var listener: ProductImageListener? = null
    val list: ArrayList<ImageUpload> = ArrayList()
    private val deletedImage: ArrayList<ImageUpload> = ArrayList()

    fun setReviewImage(isReviewImage: Boolean) {
        this.isReviewImage = isReviewImage
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_REVIEW_IMAGE -> ViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.review_listview_image_review_item, viewGroup, false)
            )
            else -> ViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.review_listview_image_upload_review, viewGroup, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_UPLOAD_BUTTON -> bindUploadButton(holder, position)
            else -> bindImage(holder, position)
        }
    }

    private fun bindImage(holder: ViewHolder, position: Int) {
        try {
            Glide.with(holder.image.context)
                .asBitmap()
                .load(File(list[position].fileLoc))
                .centerCrop()
                .into(
                    getRoundedImageViewTarget(
                        holder.image,
                        convertDpToPx(holder.image.context, RADIUS_CORNER.toFloat())
                    )
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.image.setOnClickListener(listener?.onImageClicked(position, list[position]))
        setBorder(holder, position)
    }

    fun convertDpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun setBorder(holder: ViewHolder, position: Int) {
        if (list[position].isSelected) {
            holder.image.setBackgroundColor(
                context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500)
            )
        } else {
            holder.image.setBackgroundColor(
                context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        }
    }

    private fun bindUploadButton(holder: ViewHolder, position: Int) {
        holder.image.setOnClickListener(listener?.onUploadClicked(position))
    }

    override fun getItemCount(): Int {
        return if (list.size < 5) {
            list.size + canUpload
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == list.size && list.size < MAX_IMAGE) {
            return VIEW_UPLOAD_BUTTON
        } else if (isReviewImage) {
            return VIEW_REVIEW_IMAGE
        } else {
            return super.getItemViewType(position)
        }
    }

    fun addList(data: ArrayList<ImageUpload>?) {
        list.clear()
        list.addAll((data)!!)
        notifyDataSetChanged()
    }

    fun addImage(image: ImageUpload) {
        list.add(image)
        notifyDataSetChanged()
    }

    fun removeImage(currentPosition: Int) {
        list.removeAt(currentPosition)
        for (i in currentPosition until list.size) {
            list[i].position = i
        }
        notifyDataSetChanged()
    }

    fun setListener(listener: ProductImageListener) {
        this.listener = listener
    }

    fun setCanUpload(canUpload: Boolean) {
        this.canUpload = if (canUpload) 1 else 0
    }

    var deletedList: List<ImageUpload>?
        get() {
            return deletedImage
        }
        set(deletedImage) {
            this.deletedImage.clear()
            this.deletedImage.addAll((deletedImage)!!)
        }

    companion object {
        private const val VIEW_UPLOAD_BUTTON: Int = 100
        private const val VIEW_REVIEW_IMAGE: Int = 97
        private const val MAX_IMAGE: Int = 5
        const val RADIUS_CORNER: Int = 4
        fun createAdapter(context: Context): ImageUploadAdapter {
            return ImageUploadAdapter(context)
        }

        private fun getRoundedImageViewTarget(
            imageView: ImageView,
            radius: Float
        ): BitmapImageViewTarget {
            return object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable: RoundedBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(
                            imageView.context.resources,
                            resource
                        )
                    circularBitmapDrawable.cornerRadius = radius
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            }
        }
    }

}