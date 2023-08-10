package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.ItemShopHomeProductInfoCardBinding
import com.tokopedia.shop.home.view.model.Product

class ShopHomeProductCarouselAdapter :
    RecyclerView.Adapter<ShopHomeProductCarouselAdapter.ProductViewHolder>() {

    private var products = mutableListOf<Product>()
    private var onProductClick: (Product) -> Unit = {}

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


        fun bind(product: Product) {
            binding.imgProduct.loadImage(product.imageUrl)
            binding.tpgProductName.text = product.name

            binding.tpgProductPrice.text = product.price
            binding.tpgSlashedProductPrice.text = product.slashedPrice
            binding.tpgSlashedProductPrice.strikethrough()
            binding.labelDiscount.setLabel(product.slashedPricePercent)

            binding.tpgRating.text = product.rating
            binding.tpgProductSoldCount.text = "Terjual " + product.soldCount

            binding.root.setOnClickListener { onProductClick(product) }
        }

    }

    inner class DiffCallback(
        private val oldItems: List<Product>,
        private val newItems: List<Product>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

    }

    fun submit(newProducts: List<Product>) {
        val diffCallback = DiffCallback(this.products, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.products.clear()

        this.products.addAll(newProducts)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnProductClick(onProductClick: (Product) -> Unit) {
        this.onProductClick = onProductClick
    }
}
