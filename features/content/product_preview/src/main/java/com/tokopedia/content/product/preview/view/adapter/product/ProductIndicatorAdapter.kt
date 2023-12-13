package com.tokopedia.content.product.preview.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.data.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.databinding.ItemProductIndicatorBinding
import com.tokopedia.content.product.preview.view.listener.ProductPreviewIndicatorListener
import com.tokopedia.content.product.preview.view.viewholder.product.ProductIndicatorViewHolder

class ProductIndicatorAdapter(
    private val listener: ProductPreviewIndicatorListener,
) : Adapter<ViewHolder>() {

    private val _productIndicatorList = mutableListOf<ProductIndicatorUiModel>()
    private val productIndicatorList: List<ProductIndicatorUiModel>
        get() = _productIndicatorList

    fun insertData(data: List<ProductIndicatorUiModel>) {
        _productIndicatorList.clear()
        _productIndicatorList.addAll(data)
        notifyItemRangeInserted(0, productIndicatorList.size)
    }

    fun updateSelected(position: Int) {
        val newData = _productIndicatorList.mapIndexed { index, productIndicatorUiModel ->
            productIndicatorUiModel.copy(selected = index == position)
        }

        val diffCallback = ProductIndicatorCallback(productIndicatorList, newData)
        val diffData = DiffUtil.calculateDiff(diffCallback)
        _productIndicatorList.clear()
        _productIndicatorList.addAll(newData)
        diffData.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductIndicatorViewHolder(
            binding = ItemProductIndicatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ),
            listener = listener,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ProductIndicatorViewHolder).bind(productIndicatorList[position])
    }

    override fun getItemCount(): Int = productIndicatorList.size

}
