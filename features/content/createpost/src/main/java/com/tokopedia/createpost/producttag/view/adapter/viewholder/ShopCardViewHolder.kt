package com.tokopedia.createpost.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.ItemProductTagLoadingListBinding
import com.tokopedia.createpost.createpost.databinding.ItemProductTagShopListBinding
import com.tokopedia.createpost.producttag.view.adapter.ShopCardAdapter
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
internal class ShopCardViewHolder private constructor() {

    internal class Shop(
        private val binding: ItemProductTagShopListBinding,
        private val onSelected: (ShopUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopCardAdapter.Model.Shop) {
            binding.apply {
                tvShopName.text = item.shop.shopName
                tvShopLocation.text = item.shop.shopLocation
                ivShopAvatar.loadImageCircle(item.shop.shopImage)

                icShopBadge.showWithCondition(item.shop.isShopHasBadge)
                if(item.shop.isShopHasBadge) icShopBadge.setImage(item.shop.badge)

                val shopStatusText = getShopStatusText(item.shop)
                tvShopStatus.text = shopStatusText
                clShopStatus.showWithCondition(item.shop.isShopAccessible.not())

                btnSeeShop.setOnClickListener {
                    onSelected(item.shop)
                }
            }
        }

        private fun getShopStatusText(shop: ShopUiModel): String {
            return when {
                shop.isClosed -> itemView.context.getString(R.string.cc_shop_closed)
                shop.isModerated -> itemView.context.getString(R.string.cc_shop_moderated)
                shop.isInactive -> itemView.context.getString(R.string.cc_shop_inactive)
                else -> ""
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