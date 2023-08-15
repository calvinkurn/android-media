package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselProductInfoCardBinding
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselVerticalBannerCardBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselProductCardVerticalBanner
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerItemType
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner

class ShopHomeProductCarouselAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<ShopHomeProductCarouselVerticalBannerItemType>()
    private var onProductClick: (ShopHomeProductCarouselProductCardVerticalBanner) -> Unit = {}
    private var onVerticalBannerClick: (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit = {}

    private var showProductInfo : Boolean = false

    companion object {
        private const val VIEW_TYPE_VERTICAL_BANNER = 1
        private const val VIEW_TYPE_PRODUCT = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_VERTICAL_BANNER) {
            val binding = ItemShopHomeProductCarouselVerticalBannerCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            VerticalBannerViewHolder(binding)
        } else {
            val binding = ItemShopHomeProductCarouselProductInfoCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ProductViewHolder(binding)
        }
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (item is ShopHomeProductCarouselVerticalBannerVerticalBanner) {
            (holder as VerticalBannerViewHolder).bind(item)
        } else {
            (holder as ProductViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]

        if (item is ShopHomeProductCarouselVerticalBannerVerticalBanner) {
            return VIEW_TYPE_VERTICAL_BANNER
        }

        return VIEW_TYPE_PRODUCT
    }

    inner class ProductViewHolder(
        private val binding: ItemShopHomeProductCarouselProductInfoCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopHomeProductCarouselVerticalBannerItemType) {
            val product = item as? ShopHomeProductCarouselProductCardVerticalBanner

            product?.let {
                binding.imgProduct.loadImage(product.imageUrl)

                binding.tpgProductName.text = product.name
                binding.tpgProductName.isVisible = showProductInfo

                binding.tpgProductPrice.text = product.price
                binding.tpgProductPrice.isVisible = showProductInfo

                val isDiscounted = product.slashedPricePercent.isMoreThanZero()

                binding.tpgSlashedProductPrice.text = product.slashedPrice
                binding.tpgSlashedProductPrice.strikethrough()
                binding.tpgSlashedProductPrice.isVisible = showProductInfo && isDiscounted

                val discountPercentage = binding.labelDiscount.context.getString(R.string.shop_page_placeholder_discount_percentage, product.slashedPricePercent)
                binding.labelDiscount.setLabel(discountPercentage)
                binding.labelDiscount.isVisible = showProductInfo && isDiscounted

                val hasRating = product.rating.isNotEmpty()

                binding.imgStar.isVisible = showProductInfo && hasRating
                binding.tpgBullet.isVisible = showProductInfo && hasRating

                binding.tpgRating.text = product.rating
                binding.tpgRating.isVisible = showProductInfo && hasRating

                val hasBeenPurchased = product.soldCount.isMoreThanZero()
                binding.tpgProductSoldCount.text = binding.tpgProductSoldCount.context.getString(R.string.shop_page_placeholder_sold, product.soldCount)
                binding.tpgProductSoldCount.isVisible = showProductInfo && hasBeenPurchased

                binding.root.setOnClickListener { onProductClick(product) }
            }
        }
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

    fun setShowProductInfo(showProductInfo: Boolean) {
        this.showProductInfo = showProductInfo
    }

    fun submit(newProducts: List<ShopHomeProductCarouselVerticalBannerItemType>) {
        val diffCallback = DiffCallback(this.items, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()

        this.items.addAll(newProducts)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnProductClick(onProductClick: (ShopHomeProductCarouselProductCardVerticalBanner) -> Unit) {
        this.onProductClick = onProductClick
    }

    fun setOnVerticalBannerClick(onVerticalBannerClick: (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit) {
        this.onVerticalBannerClick = onVerticalBannerClick
    }
}
