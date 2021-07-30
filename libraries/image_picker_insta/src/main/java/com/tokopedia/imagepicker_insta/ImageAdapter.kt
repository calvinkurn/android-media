package com.tokopedia.imagepicker_insta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.views.ToggleViewGroup

class ImageAdapter(val dataList: List<Asset>, val contentHeight:Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val selectedPositions = mutableSetOf<Int>()

    private val TYPE_CAMERA = 0
    private val TYPE_ASSET = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_CAMERA){
            return CameraViewHolder.getInstance(parent,contentHeight)
        }else{
            return PhotosViewHolder.getInstance(parent, contentHeight)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList[position]is Camera) return TYPE_CAMERA
        return TYPE_ASSET
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHolder) {
            holder.setData()
        } else if (holder is PhotosViewHolder) {
            holder.setData(dataList[position])
            holder.itemView.setOnClickListener {
                if(selectedPositions.contains(position)){
                    //un select
                    selectedPositions.remove(position)
                    holder.setChecked(false)
                }else{
                    //select
                    selectedPositions.add(position)
                    holder.setChecked(true)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}


class CameraViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun getInstance(parent: ViewGroup,contentHeight: Int): CameraViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_item_view_camera, parent, false)
            v.layoutParams.apply {
                height = contentHeight
            }
            return CameraViewHolder(v)
        }
    }

    val imageImage = itemView.findViewById<AppCompatImageView>(R.id.item_view_image_camera)
    fun setData() {

    }
}

class PhotosViewHolder(val photoView: View) : RecyclerView.ViewHolder(photoView) {
    companion object {
        fun getInstance(parent: ViewGroup, contentHeight: Int): PhotosViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_item_view_photos, parent, false)
            v.layoutParams.apply {
                height = contentHeight
            }
            return PhotosViewHolder(v)
        }
    }

    val assetView = itemView.findViewById<ToggleViewGroup>(R.id.item_view_image_photo)
    fun setChecked(isChecked:Boolean){
        assetView.setChecked(isChecked)
    }
    fun setData(asset: Asset) {
        assetView.loadAsset(asset)
    }
}