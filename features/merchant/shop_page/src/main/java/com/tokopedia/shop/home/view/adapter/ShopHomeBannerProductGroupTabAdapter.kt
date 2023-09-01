package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.setAdaptiveLabelDiscountColor
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupProductInfoCardBinding
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupShimmerBinding
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupVerticalBannerCardBinding
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShimmerItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShopHomeBannerProductGroupItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopHomeBannerProductGroupTabAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<ShopHomeBannerProductGroupItemType>()
    private var onProductClick: (ProductItemType) -> Unit = {}
    private var onVerticalBannerClick: (VerticalBannerItemType) -> Unit = {}

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_VERTICAL_BANNER = 1
        private const val VIEW_TYPE_PRODUCT = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_SHIMMER -> {
                val binding = ItemShopHomeBannerProductGroupShimmerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ShimmerViewHolder(binding)
            }

            VIEW_TYPE_VERTICAL_BANNER -> {
                val binding = ItemShopHomeBannerProductGroupVerticalBannerCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                VerticalBannerViewHolder(binding)
            }
            VIEW_TYPE_PRODUCT -> {
                val binding = ItemShopHomeBannerProductGroupProductInfoCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProductViewHolder(binding)
            }
            else -> {
                val binding = ItemShopHomeBannerProductGroupProductInfoCardBinding.inflate(
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
            is ShimmerItemType -> (holder as ShimmerViewHolder).bind(item)
            is ProductItemType -> (holder as ProductViewHolder).bind(item)
            is VerticalBannerItemType -> (holder as VerticalBannerViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is ShimmerItemType -> VIEW_TYPE_SHIMMER
            is VerticalBannerItemType -> VIEW_TYPE_VERTICAL_BANNER
            is ProductItemType -> VIEW_TYPE_PRODUCT
        }
    }

    inner class ShimmerViewHolder(
        private val binding: ItemShopHomeBannerProductGroupShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShimmerItemType) {
            binding.shimmerMainBanner.isVisible = item.showShimmer
            binding.shimmerFirstProduct.root.isVisible = item.showShimmer
            binding.shimmerSecondProduct.root.isVisible = item.showShimmer
            binding.shimmerThirdProduct.root.isVisible = item.showShimmer
        }
    }

    inner class ProductViewHolder(
        private val binding: ItemShopHomeBannerProductGroupProductInfoCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopHomeBannerProductGroupItemType) {
            val product = item as? ProductItemType
            
            product?.let {
                binding.imgProduct.loadImage(product.imageUrl)

                if (item.showProductInfo) binding.tpgProductName.text = product.name

                renderProductPrice(product)
                renderSlashedProductPrice(product)
                renderProductRating(product)
                renderProductSoldCount(product)
                renderBulletSeparator(product)
                setupColors(item.overrideTheme, item.colorSchema)

                binding.root.setOnClickListener { onProductClick(product) }
            }
        }

        private fun renderBulletSeparator(product: ProductItemType) {
            val hasBeenPurchased = product.soldCount.isNotEmpty()
            val hasRating = product.rating.isNotEmpty()

            val showBulletSeparator = hasBeenPurchased && hasRating && product.showProductInfo
            binding.tpgBullet.isVisible = showBulletSeparator
        }

        private fun renderProductSoldCount(product: ProductItemType) {
            val hasBeenPurchased = product.soldCount.isNotEmpty()
            if (product.showProductInfo && hasBeenPurchased) {
                binding.tpgProductSoldCount.text = product.soldCount
            }
        }

        private fun renderProductRating(product: ProductItemType) {
            val hasRating = product.rating.isNotEmpty()
            val showProductRating = product.showProductInfo && hasRating

            binding.tpgRating.isVisible = showProductRating
            binding.imgStar.isVisible = showProductRating

            if (showProductRating) {
                binding.tpgRating.text = product.rating
                binding.imgStar.setBackgroundResource(R.drawable.ic_shop_home_star_filled)
            }
        }

        private fun renderProductPrice(product: ProductItemType) {
            if (product.showProductInfo) {
                binding.tpgProductPrice.text = product.price
            }
        }

        private fun renderSlashedProductPrice(product: ProductItemType) {
            val isDiscounted = product.slashedPricePercent.isMoreThanZero()
            val showSlashedPrice = product.showProductInfo && isDiscounted

            binding.tpgSlashedProductPrice.isVisible = showSlashedPrice
            binding.labelDiscount.isVisible = showSlashedPrice

            if (showSlashedPrice) {
                binding.tpgSlashedProductPrice.text = product.slashedPrice
                binding.tpgSlashedProductPrice.strikethrough()

                val discountPercentage = binding.labelDiscount.context.getString(R.string.shop_page_placeholder_discount_percentage, product.slashedPricePercent)
                binding.labelDiscount.text = discountPercentage
                binding.labelDiscount.setAdaptiveLabelDiscountColor(!product.overrideTheme)
            }
        }

        private fun setupColors(overrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
            val highEmphasizeColor = if (overrideTheme) {
                colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
            } else {
                ContextCompat.getColor(binding.tpgProductName.context ?: return, unifycomponentsR.color.Unify_NN950)
            }

            val lowEmphasizeColor = if (overrideTheme) {
                colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
            } else {
                ContextCompat.getColor(binding.tpgProductName.context ?: return, unifycomponentsR.color.Unify_NN600)
            }

            val disabledTextColor = if (overrideTheme) {
                colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.DISABLED_TEXT_COLOR)
            } else {
                ContextCompat.getColor(binding.tpgProductName.context ?: return, unifycomponentsR.color.Unify_NN400)
            }

            binding.apply {
                tpgProductName.setTextColor(highEmphasizeColor)
                tpgProductPrice.setTextColor(highEmphasizeColor)

                tpgSlashedProductPrice.setTextColor(disabledTextColor)

                tpgRating.setTextColor(lowEmphasizeColor)
                tpgProductSoldCount.setTextColor(lowEmphasizeColor)
            }
        }
    }

    inner class VerticalBannerViewHolder(
        private val binding: ItemShopHomeBannerProductGroupVerticalBannerCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopHomeBannerProductGroupItemType) {
            val banner = item as? VerticalBannerItemType

            banner?.let {
                binding.imgVerticalBanner.loadImage(banner.imageUrl)
                binding.root.setOnClickListener { onVerticalBannerClick(banner) }
            }
        }
    }

    inner class DiffCallback(
        private val oldItems: List<ShopHomeBannerProductGroupItemType>,
        private val newItems: List<ShopHomeBannerProductGroupItemType>
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

    fun submit(newItems: List<ShopHomeBannerProductGroupItemType>) {
        val diffCallback = DiffCallback(this.items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()

        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnProductClick(onProductClick: (ProductItemType) -> Unit) {
        this.onProductClick = onProductClick
    }

    fun setOnVerticalBannerClick(onVerticalBannerClick: (VerticalBannerItemType) -> Unit) {
        this.onVerticalBannerClick = onVerticalBannerClick
    }
}
