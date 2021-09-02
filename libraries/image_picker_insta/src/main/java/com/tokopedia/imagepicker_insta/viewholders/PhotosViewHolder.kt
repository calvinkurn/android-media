package com.tokopedia.imagepicker_insta.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.views.ToggleViewGroup

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

    fun setChecked(count:Int?, isInMultiSelect:Boolean){
        if(count == null){
            assetView.setChecked(false)
        }else{
            assetView.toggleCountView.setMultiCheckEnable(isInMultiSelect)
            assetView.toggleCountView.setCount(count)
            assetView.setChecked(true)
        }
    }
    fun setData(asset: Asset) {
        assetView.loadAssetThumbnail(asset)
    }
}