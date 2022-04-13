package com.tokopedia.play.broadcaster.setup.product.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemProductListBinding
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
internal class ProductListAdapter(
    onSelected: (ProductUiModel) -> Unit,
    private val onLoading: () -> Unit,
) : BaseDiffUtilAdapter<ProductListAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(ProductListAdapterDelegate.Product(onSelected))
            .addDelegate(ProductListAdapterDelegate.Loading())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (position == (itemCount - 1)) onLoading()
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return if (oldItem is Model.Product && newItem is Model.Product) {
            oldItem.product.id == newItem.product.id
        } else if (oldItem is Model.Loading && newItem is Model.Loading) false
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Model, newItem: Model): Bundle? {
        return if (oldItem is Model.Product && newItem is Model.Product) {
            Bundle().apply {
                putBoolean(IS_PRODUCT_CHECKED_PAYLOAD, newItem.isSelected)
            }
        } else null
    }

    sealed class Model {
        data class Product(
            val product: ProductUiModel,
            val isSelected: Boolean,
        ) : Model()

        object Loading : Model()
    }

    companion object {
        internal const val IS_PRODUCT_CHECKED_PAYLOAD = "is_product_checked"
    }
}