package com.tokopedia.createpost.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.databinding.ItemProductTagLoadingListBinding
import com.tokopedia.createpost.createpost.databinding.ItemProductTagShopListBinding
import com.tokopedia.createpost.producttag.view.adapter.ShopCardAdapter
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel
import com.tokopedia.shopwidget.shopcard.ShopCardListener

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
internal class ShopCardViewHolder private constructor() {

    internal class Shop(
        private val binding: ItemProductTagShopListBinding,
        private val onSelected: (ShopUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopCardAdapter.Model.Shop) {
            binding.shopCardView.apply {
                setShopCardModel(item.shop.toShopCard(), object : ShopCardListener {
                    override fun onItemImpressed() { }

                    override fun onItemClicked() {
//                        shopListener.onItemClicked(shopDataViewItem)
                    }

                    override fun onProductItemImpressed(productPreviewIndex: Int) { }

                    override fun onProductItemClicked(productPreviewIndex: Int) {
//                        val productItem = shopDataViewItem.productList.getOrNull(productPreviewIndex) ?: return
//
//                        shopListener.onProductItemClicked(productItem)
                    }
                })
//                setProductModel(item.product.toProductCard())
//                setOnClickListener { onSelected(item.product) }
            }
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ShopUiModel) -> Unit
            ) = Shop(
                binding = ItemProductTagShopListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected = onSelected,
            )
        }
    }

    internal class Loading(
        binding: ItemProductTagLoadingListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        companion object {

            fun create(parent: ViewGroup) = Loading(
                ItemProductTagLoadingListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }
}