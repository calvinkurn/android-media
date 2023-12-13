package com.tokopedia.content.product.preview.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.data.ContentUiModel
import com.tokopedia.content.product.preview.databinding.ItemProductContentImageBinding
import com.tokopedia.content.product.preview.databinding.ItemProductContentVideoBinding
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentVideoViewHolder

class ProductContentAdapter(
    private val listener: ProductPreviewListener,
) : Adapter<ViewHolder>() {

    private val _productContentList = mutableListOf<ContentUiModel>()
    private val productContentList: List<ContentUiModel>
        get() = _productContentList

    fun insertData(data: List<ContentUiModel>) {
        _productContentList.clear()
        _productContentList.addAll(data)
        notifyItemRangeInserted(0, productContentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> createImageViewHolder(parent)
            TYPE_VIDEO -> createVideoViewHolder(parent)
            else -> error("View type is not supported")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_IMAGE -> (holder as ProductContentImageViewHolder).bind(productContentList[position])
            TYPE_VIDEO -> (holder as ProductContentVideoViewHolder).bind(productContentList[position])
            else -> error("View holder is not supported")
        }
    }

    override fun getItemCount(): Int = productContentList.size

    override fun getItemViewType(position: Int): Int {
        return when (productContentList[position].type) {
            ContentUiModel.MediaType.Image -> TYPE_IMAGE
            ContentUiModel.MediaType.Video -> TYPE_VIDEO
            ContentUiModel.MediaType.Unknown -> error("Item view type is not supported")
        }
    }

    private fun createImageViewHolder(parent: ViewGroup): ProductContentImageViewHolder {
        return ProductContentImageViewHolder(
            binding = ItemProductContentImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    private fun createVideoViewHolder(parent: ViewGroup): ProductContentVideoViewHolder {
        return ProductContentVideoViewHolder(
            binding = ItemProductContentVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ),
            listener = listener,
        )
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }

}
