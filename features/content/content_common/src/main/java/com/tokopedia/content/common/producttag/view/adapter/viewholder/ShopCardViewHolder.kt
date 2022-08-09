package com.tokopedia.content.common.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.*
import com.tokopedia.content.common.producttag.view.adapter.ShopCardAdapter
import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
internal class ShopCardViewHolder private constructor() {

    internal class Shop(
        private val binding: ItemProductTagShopListBinding,
        private val onSelected: (ShopUiModel, Int) -> Unit,
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
                    onSelected(item.shop, adapterPosition)
                }

                root.setOnClickListener {
                    onSelected(item.shop, adapterPosition)
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
                onSelected: (ShopUiModel, Int) -> Unit
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

    internal class EmptyState(
        private val binding: ItemGlobalSearchEmptyStateListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        fun bind(item: ShopCardAdapter.Model.EmptyState) {
            val context = itemView.context
            binding.emptyState.apply {
                setImageUrl(context.getString(R.string.img_search_no_shop))
                setPrimaryCTAText("")
                setSecondaryCTAText("")

                if(item.hasFilterApplied) {
                    setTitle(context.getString(R.string.cc_global_search_shop_filter_not_found_title))
                    setDescription(context.getString(R.string.cc_global_search_shop_filter_not_found_desc))
                    setOrientation(EmptyStateUnify.Orientation.VERTICAL)
                }
                else {
                    setTitle(context.getString(R.string.cc_global_search_shop_query_not_found_title))
                    setDescription(context.getString(R.string.cc_global_search_shop_query_not_found_desc))
                    setOrientation(EmptyStateUnify.Orientation.HORIZONTAL)
                }
            }
        }

        companion object {

            fun create(parent: ViewGroup) = EmptyState(
                ItemGlobalSearchEmptyStateListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }
}