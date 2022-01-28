package com.tokopedia.play.broadcaster.setup.product.view.adapter

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
internal class ProductListAdapter : BaseDiffUtilAdapter<ProductUiModel>() {

    init {
        delegatesManager.addDelegate(Delegate())
    }

    override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem == newItem
    }

    private class Delegate : TypedAdapterDelegate<ProductUiModel, ProductUiModel, ViewHolder>(R.layout.view_empty) {

        override fun onBindViewHolder(item: ProductUiModel, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder(
                ItemProductListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }

    private class ViewHolder(
        private val binding: ItemProductListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductUiModel) {
            binding.imgProduct.loadImage(item.imageUrl)
            binding.tvName.text = item.name
            binding.tvStock.text = itemView.context.getString(
                R.string.play_bro_etalase_product_stock, item.stock
            )

            when(item.price) {
                is OriginalPrice -> {
                    binding.tvPrice.text = item.price.price
                    binding.llDiscount.visibility = View.GONE
                }
                is DiscountedPrice -> {
                    binding.tvPrice.text = item.price.originalPrice
                    binding.labelDiscountPercentage.text = itemView.context.getString(
                        R.string.play_bro_product_discount_template,
                        item.price.discountPercent
                    )
                    binding.tvDiscountPrice.text = item.price.discountedPrice
                    binding.llDiscount.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvPrice.text = ""
                    binding.llDiscount.visibility = View.GONE
                }
            }
        }
    }
}