package com.tokopedia.imagepicker_insta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.imagepicker_insta.models.Asset

class ImageAdapter(val dataList: List<Asset>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val selectedPositions = arrayListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if(viewType == 0){
//            return CameraViewHolder.getInstance(parent)
//        }else{
        return PhotosViewHolder.getInstance(parent)
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHolder) {
            holder.setData()
        } else if (holder is PhotosViewHolder) {
            holder.setData(dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}


class CameraViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun getInstance(parent: ViewGroup): CameraViewHolder {
            return CameraViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_item_view_camera, parent, false))
        }
    }

    val imageImage = itemView.findViewById<AppCompatImageView>(R.id.item_view_image_camera)
    fun setData() {

    }
}

class PhotosViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun getInstance(parent: ViewGroup): PhotosViewHolder {
            return PhotosViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_item_view_photos, parent, false))
        }
    }

    val imageImage = itemView.findViewById<AssetImageView>(R.id.item_view_image_photo)
    fun setData(asset: Asset) {
        imageImage.loadAsset(asset)
    }
}