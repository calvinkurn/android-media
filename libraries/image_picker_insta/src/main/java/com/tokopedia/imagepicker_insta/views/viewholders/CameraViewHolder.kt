package com.tokopedia.imagepicker_insta.views.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R

class CameraViewHolder(val listItemView: View, val onCameraIconClick:Function0<Unit>) : RecyclerView.ViewHolder(listItemView) {

    companion object {
        fun getInstance(parent: ViewGroup, contentHeight: Int, onCameraIconClick:Function0<Unit>): CameraViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_item_view_camera, parent, false)
            v.layoutParams.apply {
                height = contentHeight
            }
            return CameraViewHolder(v,onCameraIconClick)
        }
    }

    fun setData() {
        listItemView.setOnClickListener {
            onCameraIconClick.invoke()
        }
    }
}