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
internal class ProductListAdapter(
    onSelected: (ProductUiModel) -> Unit,
) : BaseDiffUtilAdapter<ProductListAdapter.Model>() {

    init {
        delegatesManager.addDelegate(Delegate(onSelected))
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    private class Delegate(
        private val onSelected: (ProductUiModel) -> Unit,
    ) : TypedAdapterDelegate<Model, Model, ViewHolder>(R.layout.view_empty) {

        override fun onBindViewHolder(item: Model, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder(
                ItemProductListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected,
            )
        }
    }

    private class ViewHolder(
        private val binding: ItemProductListBinding,
        private val onSelected: (ProductUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                binding.checkboxProduct.isChecked = !binding.checkboxProduct.isChecked
            }
        }

        fun bind(item: Model) {
            binding.imgProduct.loadImage(item.product.imageUrl)
            binding.tvName.text = item.product.name
            binding.tvStock.text = itemView.context.getString(
                R.string.play_bro_product_chooser_stock, item.product.stock
            )

            setCheckboxManually(item)

            when(item.product.price) {
                is OriginalPrice -> {
                    binding.tvPrice.text = item.product.price.price
                    binding.llDiscount.visibility = View.GONE
                }
                is DiscountedPrice -> {
                    binding.tvPrice.text = item.product.price.originalPrice
                    binding.labelDiscountPercentage.text = itemView.context.getString(
                        R.string.play_bro_product_discount_template,
                        item.product.price.discountPercent
                    )
                    binding.tvDiscountPrice.text = item.product.price.discountedPrice
                    binding.llDiscount.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvPrice.text = ""
                    binding.llDiscount.visibility = View.GONE
                }
            }
        }

        private fun setCheckboxManually(item: Model) {
            binding.checkboxProduct.setOnCheckedChangeListener(null)
            binding.checkboxProduct.isChecked = item.isSelected
            binding.checkboxProduct.setOnCheckedChangeListener { _, _ -> onSelected(item.product) }
        }
    }

    data class Model(
        val product: ProductUiModel,
        val isSelected: Boolean,
    )
}