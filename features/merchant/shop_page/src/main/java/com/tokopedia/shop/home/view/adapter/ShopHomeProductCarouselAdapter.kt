package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.ItemShopHomeProductInfoCardBinding
import com.tokopedia.shop.home.view.listener.ShopHomeProductCarouselListener
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel

class ShopHomeProductCarouselAdapter(
    private val listener: ShopHomeProductCarouselListener
) : RecyclerView.Adapter<ShopHomeProductCarouselAdapter.ProductViewHolder>() {

    private var products = mutableListOf<ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemShopHomeProductInfoCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ProductViewHolder(binding)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    inner class ProductViewHolder(
        private val binding: ItemShopHomeProductInfoCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(product: ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild) {
            binding.imgProduct.loadImage(product.imageUrl)
            binding.root.setOnClickListener { listener.onProductClick(product) }
        }

    }

    inner class DiffCallback(
        private val oldItems: List<ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild>,
        private val newItems: List<ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].imageUrl == newItems[newItemPosition].imageUrl
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

    }

    fun submit(newProducts: List<ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild>) {
        val diffCallback = DiffCallback(this.products, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.products.clear()

        this.products.addAll(newProducts)
        diffResult.dispatchUpdatesTo(this)
    }

}
