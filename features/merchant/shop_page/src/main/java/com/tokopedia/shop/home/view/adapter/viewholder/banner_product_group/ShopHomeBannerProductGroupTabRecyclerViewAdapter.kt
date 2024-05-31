package com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupShimmerBinding
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupVerticalBannerCardBinding
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupWithProductInfoBinding
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupWithoutProductInfoBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper.toProductCardModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShimmerItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShopHomeBannerProductGroupItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType

class ShopHomeBannerProductGroupTabRecyclerViewAdapter(
    private val isOverrideTheme: Boolean,
    private val backgroundColor: String,
    private val patternColorType: String,
    private val isFestivity: Boolean
) : RecyclerView.Adapter<ViewHolder>() {

    private var items = mutableListOf<ShopHomeBannerProductGroupItemType>()
    private var onProductClick: (ProductItemType, Int) -> Unit = { _, _ -> }
    private var onVerticalBannerClick: (VerticalBannerItemType) -> Unit = {}
    private var onProductCardDrawn: (Int) -> Unit = {}
    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_VERTICAL_BANNER = 1
        private const val VIEW_TYPE_PRODUCT_WITH_PRODUCT_INFO = 2
        private const val VIEW_TYPE_PRODUCT_WITHOUT_PRODUCT_INFO = 3
        private const val CORNER_RADIUS = 12
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
            VIEW_TYPE_PRODUCT_WITH_PRODUCT_INFO -> {
                val binding = ItemShopHomeBannerProductGroupWithProductInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProductWithInfoViewHolder(binding)
            }
            VIEW_TYPE_PRODUCT_WITHOUT_PRODUCT_INFO -> {
                val binding = ItemShopHomeBannerProductGroupWithoutProductInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProductWithoutInfoViewHolder(binding)
            }
            else -> {
                val binding = ItemShopHomeBannerProductGroupWithProductInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProductWithInfoViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ShimmerItemType -> (holder as ShimmerViewHolder).bind(item)
            is ProductItemType -> bindProductCard(item, holder)
            is VerticalBannerItemType -> (holder as VerticalBannerViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(val item = items[position]) {
            is ShimmerItemType -> VIEW_TYPE_SHIMMER
            is VerticalBannerItemType -> VIEW_TYPE_VERTICAL_BANNER
            is ProductItemType -> getProductViewType(item)
        }
    }

    private fun bindProductCard(item: ProductItemType, holder: ViewHolder) {
        val showProductInfo = item.showProductInfo
        if (showProductInfo) {
            (holder as ProductWithInfoViewHolder).bind(item)
        } else {
            (holder as ProductWithoutInfoViewHolder).bind(item)
        }
    }

    private fun getProductViewType(item: ProductItemType): Int {
        val showProductInfo = item.showProductInfo
        return if (showProductInfo) {
            VIEW_TYPE_PRODUCT_WITH_PRODUCT_INFO
        } else {
            VIEW_TYPE_PRODUCT_WITHOUT_PRODUCT_INFO
        }
    }

    inner class ShimmerViewHolder(
        private val binding: ItemShopHomeBannerProductGroupShimmerBinding
    ) : ViewHolder(binding.root) {
        fun bind(item: ShimmerItemType) {
            binding.shimmerMainBanner.isVisible = item.showShimmer
            binding.shimmerFirstProduct.root.isVisible = item.showShimmer
            binding.shimmerSecondProduct.root.isVisible = item.showShimmer
            binding.shimmerThirdProduct.root.isVisible = item.showShimmer
        }
    }

    inner class ProductWithInfoViewHolder(
        private val binding: ItemShopHomeBannerProductGroupWithProductInfoBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: ShopHomeBannerProductGroupItemType) {
            val product = (item as? ProductItemType) ?: return
            renderProductCard(product)
            binding.root.setOnClickListener {
                val productIndex = bindingAdapterPosition.inc()
                onProductClick(product, productIndex)
            }
        }

        private fun renderProductCard(product: ProductItemType) {
            val productCardModel = product.toProductCardModel(
                isOverrideTheme = isOverrideTheme,
                patternColorType = patternColorType,
                backgroundColor = backgroundColor,
                isFestivity = isFestivity
            )
            binding.productCardGridView.setProductModel(productCardModel)
        }
    }

    inner class ProductWithoutInfoViewHolder(
        private val binding: ItemShopHomeBannerProductGroupWithoutProductInfoBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: ShopHomeBannerProductGroupItemType) {
            val product = (item as? ProductItemType) ?: return
            renderProductImage(product)
            binding.root.setOnClickListener {
                val productIndex = bindingAdapterPosition.inc()
                onProductClick(product, productIndex)
            }
        }

        private fun renderProductImage(product: ProductItemType) {
            binding.imgProduct.loadImage(product.imageUrl)
            binding.imgProduct.cornerRadius = CORNER_RADIUS
        }
    }

    inner class VerticalBannerViewHolder(
        private val binding: ItemShopHomeBannerProductGroupVerticalBannerCardBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: ShopHomeBannerProductGroupItemType) {
            val banner = item as? VerticalBannerItemType

            banner?.let {
                binding.imgVerticalBanner.loadImage(banner.imageUrl)

                if (item.verticalBannerHeight.isMoreThanZero()) {
                    binding.imgVerticalBanner.height(item.verticalBannerHeight)
                }
                binding.root.setOnClickListener { onVerticalBannerClick(banner) }
            }
        }

        private fun ImageView.height(sizePx: Int) {
            val layoutParams = this.layoutParams
            layoutParams.height = sizePx

            this.layoutParams = layoutParams
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

    fun setOnProductClick(onProductClick: (ProductItemType, Int) -> Unit) {
        this.onProductClick = onProductClick
    }

    fun setOnVerticalBannerClick(onVerticalBannerClick: (VerticalBannerItemType) -> Unit) {
        this.onVerticalBannerClick = onVerticalBannerClick
    }

    fun setOnProductCardDrawn(onProductCardDrawn: (Int) -> Unit) {
        this.onProductCardDrawn = onProductCardDrawn
    }
}
