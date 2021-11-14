package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.wishlist.data.model.WishlistV2DataModel
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2ListItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment.Companion.ATC_WISHLIST

class WishlistV2ListItemViewHolder(
    private val binding: WishlistV2ListItemBinding,
    private val actionListener: WishlistV2Adapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, position: Int, isShowCheckbox: Boolean) {
        val isChecked = item.dataObject as Boolean
        val productCardModel = mapWishlistDataToProductModel(item.wishlistItem)
        binding.wishlistItem.setProductModel(productCardModel)

        if (isShowCheckbox) {
            binding.wishlistCheckbox.visible()
            binding.wishlistCheckbox.setOnCheckedChangeListener(null)
            binding.wishlistCheckbox.isChecked = isChecked
            binding.wishlistCheckbox.setOnCheckedChangeListener(checkboxListener(item.wishlistItem, position))
        } else {
            binding.wishlistCheckbox.gone()
        }

        if (productCardModel.tambahKeranjangButton) {
            binding.wishlistItem.setTambahKeranjangButtonClickListener { actionListener?.onAtc(item.wishlistItem) }
        } else {
            binding.wishlistItem.setLihatBarangSerupaButtonClickListener {
                actionListener?.onCheckSimilarProduct(
                    item.wishlistItem.buttons.primaryButton.url
                )
            }
        }

        binding.wishlistItem.setSecondaryButtonClickListener {
            actionListener?.onThreeDotsMenuClicked(
                item.wishlistItem
            )
        }
    }

    private fun checkboxListener(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: Int): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            actionListener?.onCheckBulkDeleteOption(wishlistItem.id, position, isChecked)
        }
    }

    private fun mapWishlistDataToProductModel(wishlistItem: WishlistV2Response.Data.WishlistV2.Item): ProductCardModel {

        val listGroupLabel = arrayListOf<ProductCardModel.LabelGroup>()

        wishlistItem.labelGroup.forEach { labelGroupItem ->
            val labelGroup = ProductCardModel.LabelGroup(
                position = labelGroupItem.position,
                title = labelGroupItem.title,
                type = labelGroupItem.type,
                imageUrl = labelGroupItem.url
            )
            listGroupLabel.add(labelGroup)
        }

        val isButtonAtc = wishlistItem.buttons.primaryButton.action == ATC_WISHLIST
        return ProductCardModel(
            productImageUrl = wishlistItem.imageUrl,
            isWishlistVisible = true,
            productName = wishlistItem.name,
            shopName = wishlistItem.shop.name,
            formattedPrice = wishlistItem.priceFmt,
            shopLocation = wishlistItem.shop.location,
            isShopRatingYellow = true,
            hasSecondaryButton = true,
            tambahKeranjangButton = isButtonAtc,
            lihatBarangSerupaButton = !isButtonAtc,
            labelGroupList = listGroupLabel
        )
    }
}