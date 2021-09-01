package com.tokopedia.imagepicker_insta

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.imagepicker_insta.fragment.MainFragmentContract
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.viewholders.CameraViewHolder
import com.tokopedia.imagepicker_insta.viewholders.PhotosViewHolder
import com.tokopedia.unifycomponents.Toaster

class ImageAdapter(
    val dataList: List<ImageAdapterData>,
    val contentHeight: Int,
    val mainFragmentContract: MainFragmentContract,
    val maxMultiSelectLimit: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemSelectCallback: Function2<ImageAdapterData, Boolean, Unit>? = null
    val selectedPositions = mutableSetOf<Int>()

    fun isSelectedPositionsEmpty(): Boolean {
        return selectedPositions.isEmpty()
    }

    fun addSelectedItem(position: Int) {
        selectedPositions.add(position)
    }

    fun clearSelectedItems() {
        selectedPositions.clear()
    }

    private val TYPE_CAMERA = 0
    private val TYPE_ASSET = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_CAMERA) {
            return CameraViewHolder.getInstance(parent, contentHeight) {
                mainFragmentContract.handleOnCameraIconTap()
            }
        } else {
            return PhotosViewHolder.getInstance(parent, contentHeight)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList[position].asset is Camera) return TYPE_CAMERA
        return TYPE_ASSET
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHolder) {
            holder.setData()
        } else if (holder is PhotosViewHolder) {
            holder.setData(dataList[position].asset)
            holder.setChecked(selectedPositions.contains(position))

            holder.itemView.setOnClickListener {
                if (selectedPositions.contains(position)) {
                    //un select
                    selectedPositions.remove(position)
                    holder.setChecked(false)
                    itemSelectCallback?.invoke(dataList[position], false)
                } else {
                    if (selectedPositions.size != maxMultiSelectLimit) {
                        //select
                        selectedPositions.add(position)
                        holder.setChecked(true)
                        itemSelectCallback?.invoke(dataList[position], true)
                    }else{
                        mainFragmentContract.showToast("Max selection limit reached")
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}