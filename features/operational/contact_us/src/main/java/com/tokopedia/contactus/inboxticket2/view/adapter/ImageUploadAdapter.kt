package com.tokopedia.contactus.inboxticket2.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.library.utils.ImageHandler
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import java.io.File

class ImageUploadAdapter(private val context: Context, private val onSelectImageClick: OnSelectImageClick, private val onClickCross: () -> Unit) : RecyclerView.Adapter<ImageUploadAdapter.ImageViewHolder>() {
    private val maxPicUpload = 5
    private val imageUpload: ArrayList<ImageUpload> by lazy { ArrayList<ImageUpload>() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.selected_image_item, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setImage(imageUpload[position])
    }

    override fun getItemCount(): Int {
        return imageUpload.size
    }

    fun addImage(image: ImageUpload) {
        if (imageUpload.size < maxPicUpload) {
            image.imgSrc = -1
            imageUpload.add(image)
            notifyDataSetChanged()
            if (imageUpload.size == maxPicUpload) {
                Toast.makeText(context, R.string.max_image_warning, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUploadedImageList(): ArrayList<ImageUpload> {
        val imageList = ArrayList<ImageUpload>()
        for (image in imageUpload) {
            if (image.imgSrc == -1) imageList.add(image)
        }
        return imageList
    }

    fun clearAll() {
        imageUpload.clear()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image1:ImageUpload?= null
        var selectedImage: ImageView
        var deleteImage: ImageView
        fun setImage(image: ImageUpload?) {
            this.image1 = image
            if (image!!.imgSrc != -1) {
                selectedImage.setImageResource(image.imgSrc)
                deleteImage.visibility = View.GONE
                selectedImage.setOnClickListener { onSelectImageClick.onClick() }
            } else {
                ImageHandler.loadImageFromFile(context, selectedImage, File(image.fileLoc))
                selectedImage.setOnClickListener(null)
                deleteImage.visibility = View.VISIBLE
            }
            deleteImage.setOnClickListener { onViewClicked() }
        }

        fun onViewClicked() {
            if (imageUpload.size==1){
                onClickCross()
            }
            imageUpload.remove(image1)
            notifyDataSetChanged()
        }

        init {
            selectedImage = itemView.findViewById(R.id.selected_image)
            deleteImage = itemView.findViewById(R.id.delete_image)
        }
    }

    interface OnSelectImageClick {
        fun onClick()
    }
}