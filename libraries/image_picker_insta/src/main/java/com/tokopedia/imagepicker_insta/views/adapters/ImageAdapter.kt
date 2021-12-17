package com.tokopedia.imagepicker_insta.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.fragment.AdapterErrorType
import com.tokopedia.imagepicker_insta.fragment.ImagePickerFragmentContract
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.VideoData
import com.tokopedia.imagepicker_insta.views.viewholders.CameraViewHolder
import com.tokopedia.imagepicker_insta.views.viewholders.PhotosViewHolder
import com.tokopedia.imagepicker_insta.views.viewholders.VideosViewHolder

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
    var selectionOrder = SelectionOrder()

    fun isSelectedPositionsEmpty(): Boolean {
        return selectedPositionMap.isEmpty()
    }

    fun addSelectedItem(position: Int): Boolean {
        if (dataList[position].asset is VideoData) {
            if (!(dataList[position].asset as VideoData).canBeSelected) {
                return false
            }
        }
        selectionOrder.insert(dataList[position])
        selectedPositionMap[dataList[position]] = selectedPositionMap.size + 1
        return true
    }

    fun addSelectedItem(data: ImageAdapterData) {
        selectionOrder.insert(data)
        selectedPositionMap[data] = selectedPositionMap.size + 1
    }

    fun clearSelectedItems() {
        selectionOrder.clear()
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
            holder.updateMask(mainFragmentContract.isMultiSelectEnable(),
                mainFragmentContract.getAssetInPreview() == currentImageAdapterData.asset)
            holder.setChecked(selectedPositionMap[currentImageAdapterData], mainFragmentContract.isMultiSelectEnable())

            holder.itemView.setOnClickListener {
                handleSelectionUnSelection(holder, position)
            }

            holder.itemView.setOnLongClickListener {
                handleLongClick(currentImageAdapterData,holder,position)
                return@setOnLongClickListener true
            }
        }
    }

    private fun handleLongClick(currentImageAdapterData:ImageAdapterData, holder: PhotosViewHolder, position: Int){
        if (!mainFragmentContract.isMultiSelectEnable()) {

            onItemLongClick?.invoke(currentImageAdapterData)

            //Remove previously selected items
            if (selectedPositionMap.isNotEmpty()) {
                val listOfIndexes = getListOfIndexWhichAreSelected()
                clearSelectedItems()
                listOfIndexes.forEach {
                    notifyItemChanged(it)
                }
            }
        }

        handleSelectionUnSelection(holder, position)
    }

    private fun handleSelectionUnSelection(holder: PhotosViewHolder, position: Int) {
        val item = dataList[position]
        if (selectedPositionMap.contains(item)) {

            if (item.asset != mainFragmentContract.getAssetInPreview()) {
                itemSelectCallback?.invoke(item, true)
                selectionOrder.insert(item)
                notifyItems()
            } else if (mainFragmentContract.isMultiSelectEnable()){
                unSelectItem(position, holder)
            }

        } else {
            selectItem(position)
        }
    }

    private fun unSelectItem(position: Int, holder: PhotosViewHolder? = null) {
        val item = dataList[position]
        val circleCount = selectedPositionMap[item]

        val isSelectedNextItem = selectNextItem(circleCount)

        selectionOrder.remove(item)
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

        holder?.updateMask(mainFragmentContract.isMultiSelectEnable(), false)
        holder?.setChecked(null, mainFragmentContract.isMultiSelectEnable())
        if (!isSelectedNextItem) {
            itemSelectCallback?.invoke(dataList[position], false)
        }
    }

    fun notifyItems() {
        val firstPos = Math.max(0, layoutManager.findFirstVisibleItemPosition())
        val lastPos = Math.min(layoutManager.findLastVisibleItemPosition(), dataList.size - 1)
        (firstPos..lastPos).forEach { index ->
            notifyItemChanged(index)
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
        val previousSelectedItem = selectionOrder.getPreviousSelectedItem()
        if (mainFragmentContract.isMultiSelectEnable() && circleCount != null && previousSelectedItem!=null) {
            itemSelectCallback?.invoke(previousSelectedItem, true)
            return true
        }
        return false
    }

    private fun selectItem(position: Int) {
        if (dataList[position].asset is VideoData) {
            if (!(dataList[position].asset as VideoData).canBeSelected) {
                mainFragmentContract.showErrorToast(AdapterErrorType.VIDEO_DURATION)
                return
            }
        }
        if (selectedPositionMap.size != maxMultiSelectLimit) {

            if (!mainFragmentContract.isMultiSelectEnable() && selectedPositionMap.isNotEmpty()) {
                //Remove previously selected item
                val rangeList = getListOfIndexWhichAreSelected()
                clearSelectedItems()
                if (!rangeList.isNullOrEmpty()) {
                    notifyItemChanged(rangeList.first())
                }
            }
            addSelectedItem(position)
            notifyItems()
            itemSelectCallback?.invoke(dataList[position], true)
        } else {
            mainFragmentContract.showErrorToast(AdapterErrorType.MULTISELECT)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}