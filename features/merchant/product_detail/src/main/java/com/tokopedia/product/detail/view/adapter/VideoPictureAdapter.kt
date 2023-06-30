package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaContainerType
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductPictureViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductVideoViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductVideoViewHolder.Companion.VIDEO_TYPE
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

/**
 * Created by Yehezkiel on 23/11/20
 */
class VideoPictureAdapter(
    private val listener: DynamicProductDetailListener?,
    private val componentTrackDataModel: ComponentTrackDataModel?,
    private val containerType: MediaContainerType
) : RecyclerView.Adapter<AbstractViewHolder<MediaDataModel>>() {

    val currentList: MutableList<MediaDataModel> = mutableListOf()

    fun submitList(newList: List<MediaDataModel>) {
        val diffCallback = VideoPictureDiffUtil(currentList.toMutableList(), newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        currentList.clear()
        currentList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun isPicture(position: Int): Boolean {
        val item = currentList.getOrNull(position)
        return item != null && !item.isVideoType()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<MediaDataModel> {
        return if (viewType == MEDIA_VIDEO_VIEW_TYPE) {
            ProductVideoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(ProductVideoViewHolder.LAYOUT, parent, false),
                listener
            )
        } else {
            ProductPictureViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(ProductPictureViewHolder.LAYOUT, parent, false),
                listener,
                componentTrackDataModel,
                containerType = containerType
            )
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<MediaDataModel>, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].type == VIDEO_TYPE) {
            MEDIA_VIDEO_VIEW_TYPE
        } else {
            MEDIA_PICTURE_VIEW_TYPE
        }
    }

    private fun getFirstPicturePosition(): Int {
        return currentList.indexOfFirst { !it.isVideoType() }
    }

    companion object {
        const val MEDIA_PICTURE_VIEW_TYPE = 1
        const val MEDIA_VIDEO_VIEW_TYPE = 2
    }
}

class VideoPictureDiffUtil(
    private val oldList: List<MediaDataModel>,
    private val newList: List<MediaDataModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].urlOriginal == newList[newItemPosition].urlOriginal &&
                oldList[oldItemPosition].url300 == newList[newItemPosition].url300 &&
                oldList[oldItemPosition].type == newList[newItemPosition].type &&
                oldList[oldItemPosition].urlThumbnail == newList[newItemPosition].urlThumbnail
    }
}
