package com.tokopedia.content.product.preview.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.databinding.ItemProductContentImageBinding
import com.tokopedia.content.product.preview.databinding.ItemProductContentLoadingBinding
import com.tokopedia.content.product.preview.databinding.ItemProductContentVideoBinding
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.model.ProductVideoModel
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentLoadingViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentVideoViewHolder

class ProductContentAdapter(
    private val listener: ProductPreviewListener,
) : Adapter<ViewHolder>() {

    private val _productContentList = mutableListOf<ProductVideoModel>()
    private val productContentList: List<ProductVideoModel>
        get() = _productContentList

    fun insertData(data: List<ProductVideoModel>) {
        _productContentList.clear()
        _productContentList.addAll(data)
        notifyItemRangeInserted(0, productContentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                ProductContentImageViewHolder(
                    binding = ItemProductContentImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }

            TYPE_VIDEO -> {
                ProductContentVideoViewHolder(
                    binding = ItemProductContentVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    ),
                    listener = listener,
                )
            }

            else -> {
                ProductContentLoadingViewHolder(
                    ItemProductContentLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_IMAGE -> (holder as ProductContentImageViewHolder).bind()
            TYPE_VIDEO -> (holder as ProductContentVideoViewHolder).bind(productContentList[position])
            else -> (holder as ProductContentLoadingViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = productContentList.size

    override fun getItemViewType(position: Int): Int {
        return TYPE_VIDEO
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }

}
