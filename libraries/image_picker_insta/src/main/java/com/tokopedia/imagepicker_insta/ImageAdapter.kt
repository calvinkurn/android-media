package com.tokopedia.imagepicker_insta

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.viewholders.CameraViewHolder
import com.tokopedia.imagepicker_insta.viewholders.PhotosViewHolder

class ImageAdapter(val dataList: List<Asset>, val contentHeight: Int, var onCameraIconClick: Function0<Unit>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemSelectCallback: Function2<Asset, Boolean, Unit>? = null
    val selectedPositions = mutableSetOf<Int>()


    private val TYPE_CAMERA = 0
    private val TYPE_ASSET = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_CAMERA) {
            return CameraViewHolder.getInstance(parent, contentHeight, onCameraIconClick)
        } else {
            return PhotosViewHolder.getInstance(parent, contentHeight)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList[position] is Camera) return TYPE_CAMERA
        return TYPE_ASSET
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHolder) {
            holder.setData()
        } else if (holder is PhotosViewHolder) {
            holder.setData(dataList[position])
            holder.setChecked(selectedPositions.contains(position))

            holder.itemView.setOnClickListener {
                if (selectedPositions.contains(position)) {
                    //un select
                    selectedPositions.remove(position)

                    itemSelectCallback?.invoke(dataList[position], false)
                } else {
                    //select
                    selectedPositions.add(position)
                    holder.setChecked(true)
                    itemSelectCallback?.invoke(dataList[position], true)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}