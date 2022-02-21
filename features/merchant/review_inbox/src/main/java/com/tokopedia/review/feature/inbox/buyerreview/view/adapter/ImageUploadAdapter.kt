package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload
import com.tokopedia.review.inbox.R
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
        holder.image.loadImageRounded(list[position].picSrc, convertDpToPx(holder.itemView.context))
        holder.image.setOnClickListener(listener?.onImageClicked(position, list[position]))
        setBorder(holder, position)
    }

    private fun setBorder(holder: ViewHolder, position: Int) {
        if (list[position].isSelected) {
            holder.image.setBackgroundColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            )
        } else {
            holder.image.setBackgroundColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        }
    }

    private fun bindUploadButton(holder: ViewHolder, position: Int) {
        holder.image.setOnClickListener(listener?.onUploadClicked(position))
    }

    private fun convertDpToPx(context: Context): Float {
        return RADIUS * context.resources.displayMetrics.density
    }

    override fun getItemCount(): Int {
        return if (list.size < MAX_IMAGE) {
            list.size + canUpload
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size && list.size < MAX_IMAGE) {
            VIEW_UPLOAD_BUTTON
        } else if (isReviewImage) {
            VIEW_REVIEW_IMAGE
        } else {
            super.getItemViewType(position)
        }
    }

    fun addList(data: ArrayList<ImageUpload>) {
        list.clear()
        list.addAll(data)
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

    var deletedList: List<ImageUpload>
        get() {
            return deletedImage
        }
        set(deletedImage) {
            this.deletedImage.clear()
            this.deletedImage.addAll(deletedImage)
        }

    companion object {
        private const val VIEW_UPLOAD_BUTTON = 100
        private const val VIEW_REVIEW_IMAGE = 97
        private const val MAX_IMAGE = 5
        private const val RADIUS = 4
        fun createAdapter(context: Context): ImageUploadAdapter {
            return ImageUploadAdapter(context)
        }
    }

}