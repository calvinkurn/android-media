package com.tokopedia.contactus.inboxtickets.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxtickets.data.ImageUpload
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import java.io.File

private const val DUMMY_IMAGE_RESOURCE_FOR_LOCAL_FILE = -1

class ImageUploadAdapter(
    private val context: Context,
    private val onSelectImageClick: OnSelectImageClick,
    private val onClickCross: () -> Unit
) : RecyclerView.Adapter<ImageUploadAdapter.ImageViewHolder>() {
    private val maxPicUpload = 5
    private val imageUpload: ArrayList<ImageUpload> by lazy { ArrayList<ImageUpload>() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(context).inflate(R.layout.selected_image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setImage(imageUpload[position])
    }

    override fun getItemCount(): Int {
        return imageUpload.size
    }

    fun addImage(image: ImageUpload) {
        if (imageUpload.size < maxPicUpload) {
            image.imgSrc = DUMMY_IMAGE_RESOURCE_FOR_LOCAL_FILE
            imageUpload.add(image)
            notifyItemChanged(imageUpload.size - 1)
            if (imageUpload.size == maxPicUpload) {
                Toast.makeText(context, R.string.max_image_warning, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUploadedImageList(): ArrayList<ImageUpload> {
        val imageList = ArrayList<ImageUpload>()
        for (image in imageUpload) {
            if (image.imgSrc == DUMMY_IMAGE_RESOURCE_FOR_LOCAL_FILE) imageList.add(image)
        }
        return imageList
    }

    fun clearAll() {
        imageUpload.clear()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageUpload? = null
        private var selectedImage: ImageView = itemView.findViewById(R.id.selected_image)
        private var deleteImage: ImageView = itemView.findViewById(R.id.delete_image)
        @SuppressLint("DeprecatedMethod")
        fun setImage(image: ImageUpload) {
            this.image = image
            if (image.imgSrc != -1) {
                selectedImage.setImageResource(image.imgSrc)
                deleteImage.hide()
                selectedImage.setOnClickListener { onSelectImageClick.onClick() }
            } else {
                selectedImage.loadImage(File(image.fileLoc.orEmpty()).toUri())
                selectedImage.setOnClickListener(null)
                deleteImage.show()
            }
            deleteImage.setOnClickListener { onViewClicked() }
        }

        private fun onViewClicked() {
            if (imageUpload.size == 1) {
                onClickCross()
            }
            imageUpload.remove(image)
            notifyItemChanged(position)
        }
    }

    interface OnSelectImageClick {
        fun onClick()
    }
}
