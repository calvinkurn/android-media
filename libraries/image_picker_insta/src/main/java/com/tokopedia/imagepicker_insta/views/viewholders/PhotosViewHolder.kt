package com.tokopedia.imagepicker_insta.views.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.views.ToggleViewGroup

open class PhotosViewHolder(photoView: View) : RecyclerView.ViewHolder(photoView) {
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

    fun updateMask(isInMultiSelect:Boolean,isCurrentlySelected:Boolean){
        if(isInMultiSelect){
            if(isCurrentlySelected){
                assetView.maskImageView.onDrawableId = R.drawable.imagepicker_insta_ic_grey_mask
            }else{
                assetView.maskImageView.onDrawableId = R.drawable.imagepicker_insta_rect_grey_empty
            }
        }else{
            assetView.maskImageView.onDrawableId = R.drawable.imagepicker_insta_ic_grey_mask
        }
    }

    open fun setData(imageAdapterData: ImageAdapterData, contentHeight: Int) {
        assetView.loadAssetThumbnail(imageAdapterData.asset, contentHeight)
    }
}