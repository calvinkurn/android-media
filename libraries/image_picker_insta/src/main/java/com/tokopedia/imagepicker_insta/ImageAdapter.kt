package com.tokopedia.imagepicker_insta

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.fragment.MainFragmentContract
import com.tokopedia.imagepicker_insta.mediaImporter.VideoImporter
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.VideoData
import com.tokopedia.imagepicker_insta.viewholders.CameraViewHolder
import com.tokopedia.imagepicker_insta.viewholders.PhotosViewHolder
import com.tokopedia.imagepicker_insta.viewholders.VideosViewHolder
import com.tokopedia.unifycomponents.Toaster

class ImageAdapter(
    val dataList: List<ImageAdapterData>,
    val contentHeight: Int,
    val mainFragmentContract: MainFragmentContract,
    val maxMultiSelectLimit: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemSelectCallback: Function2<ImageAdapterData, Boolean, Unit>? = null
    var onItemLongClick: Function1<ImageAdapterData, Unit>? = null

    /**
     *Position and count
     * */
    val selectedPositionMap = mutableMapOf<Int, Int>()

    val INVALID_KEY = -1

    fun isSelectedPositionsEmpty(): Boolean {
        return selectedPositionMap.isEmpty()
    }

    fun addSelectedItem(position: Int): Boolean {
        if (dataList[position].asset is VideoData) {
            if (!(dataList[position].asset as VideoData).canBeSelected) {
                return false
            }
        }
        selectedPositionMap[position] = selectedPositionMap.size + 1
        return true
    }

    fun clearSelectedItems() {
        selectedPositionMap.clear()
    }

    private val TYPE_CAMERA = 0
    private val TYPE_PHOTO = 1
    private val TYPE_VIDEO = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CAMERA -> CameraViewHolder.getInstance(parent, contentHeight) {
                mainFragmentContract.handleOnCameraIconTap()
            }
            TYPE_VIDEO -> VideosViewHolder.getInstance(parent, contentHeight)
            else -> PhotosViewHolder.getInstance(parent, contentHeight)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList[position].asset is Camera) return TYPE_CAMERA
        if (dataList[position].asset is VideoData) return TYPE_VIDEO
        return TYPE_PHOTO
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHolder) {
            holder.setData()
        } else if (holder is PhotosViewHolder) {
            holder.setData(dataList[position])
            holder.setChecked(selectedPositionMap[position], mainFragmentContract.isMultiSelectEnable())

            holder.itemView.setOnClickListener {
                handleSelectionUnSelection(holder, position)
            }

            holder.itemView.setOnLongClickListener {

                if (!mainFragmentContract.isMultiSelectEnable()) {

                    onItemLongClick?.invoke(dataList[position])

                    //Remove previously selected items
                    if (selectedPositionMap.isNotEmpty()) {
                        val selectedItemKey = selectedPositionMap.keys.first()
                        dataList[selectedItemKey].isSelected = false
                        selectedPositionMap.clear()
                    }
                }

                handleSelectionUnSelection(holder, position)

                return@setOnLongClickListener true
            }
        }
    }

    private fun handleSelectionUnSelection(holder: PhotosViewHolder, position: Int) {
        if (selectedPositionMap.contains(position)) {

            if (dataList[position].asset != mainFragmentContract.getAssetInPreview()) {
                itemSelectCallback?.invoke(dataList[position], true)
            } else {
                unSelectItem(position, holder)
            }

        } else {
            selectItem(position, holder)
        }
    }

    private fun unSelectItem(position: Int, holder: PhotosViewHolder? = null) {
        val circleCount = selectedPositionMap[position]

        val isSelectedNextItem = selectNextItem(circleCount)

        selectedPositionMap.remove(position)

        for ((k, v) in selectedPositionMap) {
            if (circleCount != null) {
                if (v > circleCount) {
                    selectedPositionMap[k] = v - 1
                    notifyItemChanged(k)
                }
            }
        }

        holder?.setChecked(null, mainFragmentContract.isMultiSelectEnable())
        if (!isSelectedNextItem) {
            itemSelectCallback?.invoke(dataList[position], false)
        }

    }

    private fun selectNextItem(circleCount: Int?): Boolean {
        if (mainFragmentContract.isMultiSelectEnable() && circleCount != null) {
            val previousSelectedPos = findPreviousSelectedAdapterPosition(circleCount)
            val nextSelectedPos = findNextSelectedAdapterPosition(circleCount)

            if (nextSelectedPos != INVALID_KEY) {
                itemSelectCallback?.invoke(dataList[nextSelectedPos], true)
                return true
            } else if (previousSelectedPos != INVALID_KEY) {
                itemSelectCallback?.invoke(dataList[previousSelectedPos], true)
                return true
            }
        }
        return false
    }

    fun findPreviousSelectedAdapterPosition(circleCount: Int): Int {
        var tempLowCount = 0
        var key = INVALID_KEY
        for ((k, v) in selectedPositionMap) {
            if (v < circleCount && v > tempLowCount) {
                tempLowCount = v
                key = k
            }
        }
        return key
    }

    fun findNextSelectedAdapterPosition(circleCount: Int): Int {
        var tempHighCount = selectedPositionMap.size + 1
        var key = INVALID_KEY
        for ((k, v) in selectedPositionMap) {
            if (v > circleCount && v < tempHighCount) {
                tempHighCount = v
                key = k
            }
        }
        return key
    }

    private fun selectItem(position: Int, holder: PhotosViewHolder) {
        if (dataList[position].asset is VideoData) {
            if (!(dataList[position].asset as VideoData).canBeSelected) {
                mainFragmentContract.showToast("Video harus berdurasi maksimum ${VideoImporter.DURATION_MAX_LIMIT} detik.", Toaster.TYPE_ERROR)
                return
            }
        }
        if (selectedPositionMap.size != maxMultiSelectLimit) {

            if (!mainFragmentContract.isMultiSelectEnable() && selectedPositionMap.isNotEmpty()) {
                //Remove previously selected item
                val previouslySelectedItemPosition = selectedPositionMap.keys.first()
                unSelectItem(previouslySelectedItemPosition)
                notifyItemChanged(previouslySelectedItemPosition)
            }
            addSelectedItem(position)
            holder.setChecked(selectedPositionMap.size, mainFragmentContract.isMultiSelectEnable())
            itemSelectCallback?.invoke(dataList[position], true)
        } else {
            mainFragmentContract.showToast("Max selection limit reached", Toaster.TYPE_ERROR)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}