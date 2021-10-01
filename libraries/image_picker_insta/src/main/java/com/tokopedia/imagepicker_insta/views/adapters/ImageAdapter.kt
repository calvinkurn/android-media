package com.tokopedia.imagepicker_insta.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.fragment.ImagePickerFragmentContract
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.VideoData
import com.tokopedia.imagepicker_insta.util.VideoUtil
import com.tokopedia.imagepicker_insta.views.viewholders.CameraViewHolder
import com.tokopedia.imagepicker_insta.views.viewholders.PhotosViewHolder
import com.tokopedia.imagepicker_insta.views.viewholders.VideosViewHolder
import com.tokopedia.unifycomponents.Toaster

class ImageAdapter(
    val dataList: List<ImageAdapterData>,
    val contentHeight: Int,
    val mainFragmentContract: ImagePickerFragmentContract,
    val maxMultiSelectLimit: Int,
    val layoutManager: GridLayoutManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemSelectCallback: Function2<ImageAdapterData, Boolean, Unit>? = null
    var onItemLongClick: Function1<ImageAdapterData, Unit>? = null

    /**
     * Data and count
     * */
    val selectedPositionMap = mutableMapOf<ImageAdapterData, Int>()

    fun isSelectedPositionsEmpty(): Boolean {
        return selectedPositionMap.isEmpty()
    }

    fun addSelectedItem(position: Int): Boolean {
        if (dataList[position].asset is VideoData) {
            if (!(dataList[position].asset as VideoData).canBeSelected) {
                return false
            }
        }
        selectedPositionMap[dataList[position]] = selectedPositionMap.size + 1
        return true
    }

    fun addSelectedItem(data: ImageAdapterData) {
        selectedPositionMap[data] = selectedPositionMap.size + 1
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
            val currentImageAdapterData = dataList[position]
            holder.setData(currentImageAdapterData, holder.itemView.layoutParams.height)
            holder.setChecked(selectedPositionMap[currentImageAdapterData], mainFragmentContract.isMultiSelectEnable())

            holder.itemView.setOnClickListener {
                handleSelectionUnSelection(holder, position)
            }

            holder.itemView.setOnLongClickListener {

                if (!mainFragmentContract.isMultiSelectEnable()) {

                    onItemLongClick?.invoke(currentImageAdapterData)

                    //Remove previously selected items
                    if (selectedPositionMap.isNotEmpty()) {
                        val listOfIndexes = getListOfIndexWhichAreSelected()
                        selectedPositionMap.clear()
                        listOfIndexes.forEach {
                            notifyItemChanged(it)
                        }
                    }
                }

                handleSelectionUnSelection(holder, position)

                return@setOnLongClickListener true
            }
        }
    }

    private fun handleSelectionUnSelection(holder: PhotosViewHolder, position: Int) {
        val item = dataList[position]
        if (selectedPositionMap.contains(item)) {

            if (item.asset != mainFragmentContract.getAssetInPreview()) {
                itemSelectCallback?.invoke(item, true)
            } else {
                unSelectItem(position, holder)
            }

        } else {
            selectItem(position, holder)
        }
    }

    private fun unSelectItem(position: Int, holder: PhotosViewHolder? = null) {
        val item = dataList[position]
        val circleCount = selectedPositionMap[item]

        val isSelectedNextItem = selectNextItem(circleCount)

        selectedPositionMap.remove(item)

        //Notify items to update circle count
        for ((k, v) in selectedPositionMap) {
            if (circleCount != null) {
                if (v > circleCount) {
                    selectedPositionMap[k] = v - 1
                }
            }
        }

        //Logic to notify items
        notifyItems()

        holder?.setChecked(null, mainFragmentContract.isMultiSelectEnable())
        if (!isSelectedNextItem) {
            itemSelectCallback?.invoke(dataList[position], false)
        }
    }

    fun notifyItems() {
        val firstPos = Math.max(0, layoutManager.findFirstVisibleItemPosition())
        val lastPos = Math.min(layoutManager.findLastVisibleItemPosition(), dataList.size - 1)
        (firstPos..lastPos).forEach { index ->
//            val isSelected = selectedPositionMap[dataList[index]]
//            if (isSelected != null) {
                notifyItemChanged(index)
//            }
        }
    }

    fun getListOfIndexWhichAreSelected(): List<Int> {
        val list = arrayListOf<Int>()
        val firstPos = Math.max(0, layoutManager.findFirstVisibleItemPosition())
        val lastPos = Math.min(layoutManager.findLastVisibleItemPosition(), dataList.size - 1)
        (firstPos..lastPos).forEach { index ->
            val isSelected = selectedPositionMap[dataList[index]]
            if (isSelected != null) {
                list.add(index)
            }
        }
        return list
    }

    private fun selectNextItem(circleCount: Int?): Boolean {
        if (mainFragmentContract.isMultiSelectEnable() && circleCount != null) {
            val previousSelectedItem = findPreviousSelectedAdapterPosition(circleCount)
            val nextSelectedItem = findNextSelectedAdapterPosition(circleCount)

            if (nextSelectedItem != null) {
                itemSelectCallback?.invoke(nextSelectedItem, true)
                return true
            } else if (previousSelectedItem != null) {
                itemSelectCallback?.invoke(previousSelectedItem, true)
                return true
            }
        }
        return false
    }

    fun findPreviousSelectedAdapterPosition(circleCount: Int): ImageAdapterData? {
        var tempLowCount = 0
        var key: ImageAdapterData? = null
        for ((k, v) in selectedPositionMap) {
            if (v < circleCount && v > tempLowCount) {
                tempLowCount = v
                key = k
            }
        }
        return key
    }

    fun findNextSelectedAdapterPosition(circleCount: Int): ImageAdapterData? {
        var tempHighCount = selectedPositionMap.size + 1
        var key: ImageAdapterData? = null
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
                mainFragmentContract.showToast("Video harus berdurasi maksimum ${VideoUtil.DURATION_MAX_LIMIT} detik.", Toaster.TYPE_ERROR)
                return
            }
        }
        if (selectedPositionMap.size != maxMultiSelectLimit) {

            if (!mainFragmentContract.isMultiSelectEnable() && selectedPositionMap.isNotEmpty()) {
                //Remove previously selected item
                val rangeList = getListOfIndexWhichAreSelected()
                selectedPositionMap.clear()
                if (!rangeList.isNullOrEmpty()) {
                    notifyItemChanged(rangeList.first())
                }
            }
            addSelectedItem(position)
            holder.setChecked(selectedPositionMap.size, mainFragmentContract.isMultiSelectEnable())
            itemSelectCallback?.invoke(dataList[position], true)
        } else {
            mainFragmentContract.showToast("Oops, maksimal upload 5 media, Hapus salah satu media jika ingin menggantinya.", Toaster.TYPE_ERROR)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}