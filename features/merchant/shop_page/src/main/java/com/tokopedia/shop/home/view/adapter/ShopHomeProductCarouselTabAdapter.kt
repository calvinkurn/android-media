package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselProductInfoCardBinding
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselShimmerBinding
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselVerticalBannerCardBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselProductCard
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselShimmer
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerItemType
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner

class ShopHomeProductCarouselTabAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<ShopHomeProductCarouselVerticalBannerItemType>()
    private var onProductClick: (ShopHomeProductCarouselProductCard) -> Unit = {}
    private var onVerticalBannerClick: (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit = {}

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_VERTICAL_BANNER = 1
        private const val VIEW_TYPE_PRODUCT = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_SHIMMER -> {
                val binding = ItemShopHomeProductCarouselShimmerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ShimmerViewHolder(binding)
            }

            VIEW_TYPE_VERTICAL_BANNER -> {
                val binding = ItemShopHomeProductCarouselVerticalBannerCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                VerticalBannerViewHolder(binding)
            }
            VIEW_TYPE_PRODUCT -> {
                val binding = ItemShopHomeProductCarouselProductInfoCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProductViewHolder(binding)
            }
            else -> {
                val binding = ItemShopHomeProductCarouselProductInfoCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProductViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            ShopHomeProductCarouselShimmer -> {}
            is ShopHomeProductCarouselProductCard -> (holder as ProductViewHolder).bind(item)
            is ShopHomeProductCarouselVerticalBannerVerticalBanner -> (holder as VerticalBannerViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            ShopHomeProductCarouselShimmer -> VIEW_TYPE_SHIMMER
            is ShopHomeProductCarouselVerticalBannerVerticalBanner -> VIEW_TYPE_VERTICAL_BANNER
            is ShopHomeProductCarouselProductCard -> VIEW_TYPE_PRODUCT
        }
    }

    inner class ShimmerViewHolder(
        binding: ItemShopHomeProductCarouselShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class ProductViewHolder(
        private val binding: ItemShopHomeProductCarouselProductInfoCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopHomeProductCarouselVerticalBannerItemType) {
            val product = item as? ShopHomeProductCarouselProductCard
            
            product?.let {
                binding.imgProduct.loadImage(product.imageUrl)

                binding.tpgProductName.text = product.name
                binding.tpgProductName.showIfOrInvisible(product.showProductInfo)

                renderProductPrice(product)
                renderSlashedProductPrice(product)
                renderProductRating(product)
                renderProductSoldCount(product)

                binding.root.setOnClickListener { onProductClick(product) }
            }
        }

        private fun renderProductSoldCount(product: ShopHomeProductCarouselProductCard) {
            val hasBeenPurchased = product.soldCount.isNotEmpty()
            binding.tpgProductSoldCount.text = product.soldCount
            binding.tpgProductSoldCount.isVisible = product.showProductInfo && hasBeenPurchased
        }

        private fun renderProductRating(product: ShopHomeProductCarouselProductCard) {
            val hasRating = product.rating.isNotEmpty()

            binding.imgStar.isVisible = product.showProductInfo && hasRating
            binding.tpgBullet.isVisible = product.showProductInfo && hasRating

            binding.tpgRating.text = product.rating
            binding.tpgRating.isVisible = product.showProductInfo && hasRating
        }

        private fun renderProductPrice(product: ShopHomeProductCarouselProductCard) {
            binding.tpgProductPrice.text = product.price
            binding.tpgProductPrice.showIfOrInvisible(product.showProductInfo)
        }

        private fun renderSlashedProductPrice(product: ShopHomeProductCarouselProductCard) {
            val isDiscounted = product.slashedPricePercent.isMoreThanZero()
            binding.tpgSlashedProductPrice.text = product.slashedPrice
            binding.tpgSlashedProductPrice.strikethrough()
            binding.tpgSlashedProductPrice.isVisible = product.showProductInfo && isDiscounted

            val discountPercentage = binding.labelDiscount.context.getString(R.string.shop_page_placeholder_discount_percentage, product.slashedPricePercent)
            binding.labelDiscount.setLabel(discountPercentage)
            binding.labelDiscount.isVisible = product.showProductInfo && isDiscounted
        }
    }
    
    
    private fun View.showIfOrInvisible(show: Boolean) {
        if (show) visible() else invisible()
    }
    
    inner class VerticalBannerViewHolder(
        private val binding: ItemShopHomeProductCarouselVerticalBannerCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopHomeProductCarouselVerticalBannerItemType) {
            val banner = item as? ShopHomeProductCarouselVerticalBannerVerticalBanner

            banner?.let {
                binding.imgVerticalBanner.loadImage(banner.imageUrl)
                binding.root.setOnClickListener { onVerticalBannerClick(banner) }
            }
        }
    }

    inner class DiffCallback(
        private val oldItems: List<ShopHomeProductCarouselVerticalBannerItemType>,
        private val newItems: List<ShopHomeProductCarouselVerticalBannerItemType>
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

    fun submit(newItems: List<ShopHomeProductCarouselVerticalBannerItemType>) {
        val diffCallback = DiffCallback(this.items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()

        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnProductClick(onProductClick: (ShopHomeProductCarouselProductCard) -> Unit) {
        this.onProductClick = onProductClick
    }

    fun setOnVerticalBannerClick(onVerticalBannerClick: (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit) {
        this.onVerticalBannerClick = onVerticalBannerClick
    }
}
