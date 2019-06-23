package com.tokopedia.report.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.report.R
import com.tokopedia.report.data.util.MerchantReportTracking
import kotlinx.android.synthetic.main.item_filled_photo.view.*

class UploadPhotoAdapter (var type: String,
                          private val addPhotoListener: ((String, Int) -> Unit),
                          private val updateRemoveListener: ((String, List<String>) -> Unit),
                          private var maxTotal: Int = 1): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photosUri = mutableListOf<String>()

    override fun getItemViewType(position: Int): Int {
        return if (photosUri.size < maxTotal && position == photosUri.size){
            TYPE_PLACEHOLDER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_PLACEHOLDER -> PlaceHolder(parent.inflateLayout(R.layout.item_photo_placeholder))
            else -> PhotoItemViewHolder(parent.inflateLayout(R.layout.item_filled_photo))
        }
    }

    override fun getItemCount(): Int = if (photosUri.size < maxTotal) photosUri.size + 1 else maxTotal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotoItemViewHolder){
            holder.bind(photosUri[position])
        }
    }

    fun addPhoto(photoUri: String){
        photosUri.add(photoUri)
        notifyDataSetChanged()
    }

    fun removePhotoAt(position: Int){
        photosUri.removeAt(position)
        notifyDataSetChanged()
        updateRemoveListener.invoke(type, photosUri)
    }

    fun updateMax(max: Int) {
        maxTotal = max
        notifyDataSetChanged()
    }

    fun updatePhoto(photoUris: List<String>) {
        photosUri.clear()
        photosUri.addAll(photoUris)
        notifyDataSetChanged()
    }

    inner class PhotoItemViewHolder(view: View): RecyclerView.ViewHolder(view){

        init {
            itemView.iv_delete.setOnClickListener {
                removePhotoAt(adapterPosition)
            }
        }

        fun bind(photoUri: String){
            ImageHandler.LoadImage(itemView.attached_image, photoUri)
        }
    }

    inner class PlaceHolder(view: View):RecyclerView.ViewHolder(view){
        init {
            itemView.setOnClickListener {
                addPhotoListener.invoke(type, maxTotal - itemCount)
            }
        }
    }

    companion object {
        private const val TYPE_PLACEHOLDER = 1
        private const val TYPE_ITEM = 2
    }
}