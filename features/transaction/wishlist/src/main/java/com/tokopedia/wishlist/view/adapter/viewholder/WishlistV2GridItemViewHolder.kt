package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2GridItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2GridItemViewHolder(private val binding: WishlistV2GridItemBinding,
                                   private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistV2TypeLayoutData, position: Int, isShowCheckbox: Boolean) {
        if (item.dataObject is ProductCardModel) {
            binding.pcGridItem.setProductModel(item.dataObject)

            val footerLayout = binding.pcGridItem.findViewById<View>(R.id.productCardFooterLayout)
            val buttonSecondary = footerLayout.findViewById<FrameLayout>(R.id.buttonThreeDotsWishlist)
            val rlPrimaryButton = footerLayout.findViewById<RelativeLayout>(R.id.rlPrimaryButtonWishlist)
            val buttonAtc = footerLayout.findViewById<UnifyButton>(R.id.buttonAddToCartWishlist)
            val buttonSeeSimilarProduct = footerLayout.findViewById<UnifyButton>(R.id.buttonSeeSimilarProductWishlist)

            if (isShowCheckbox) {
                renderBulkDelete(item, buttonSecondary, rlPrimaryButton)

            } else {
                renderRegularWishlist(item, buttonSecondary, rlPrimaryButton)
            }

            if (item.dataObject.hasAddToCartWishlist) {
                renderButtonPrimaryAtc(buttonAtc, buttonSeeSimilarProduct, item)
            } else {
                renderButtonSeeSimilarProduct(buttonAtc, buttonSeeSimilarProduct, item)
            }

            binding.pcGridItem.setThreeDotsWishlistOnClickListener { actionListener?.onThreeDotsMenuClicked(item.wishlistItem) }
            actionListener?.onViewProductCard(item.wishlistItem, position)
        }
    }

    private fun renderBulkDelete(item: WishlistV2TypeLayoutData, buttonSecondary: FrameLayout, rlPrimaryButton: RelativeLayout) {
        binding.wishlistCheckbox.setOnCheckedChangeListener(null)
        binding.wishlistCheckbox.visible()
        binding.wishlistCheckbox.isChecked = item.isChecked
        binding.wishlistCheckbox.skipAnimation()
        binding.wishlistCheckbox.setOnClickListener {
            actionListener?.onCheckBulkDeleteOption(item.wishlistItem.id, binding.wishlistCheckbox.isChecked, position)
        }
        buttonSecondary.gone()
        rlPrimaryButton.gone()
        binding.pcGridItem.setOnClickListener {
            binding.wishlistCheckbox.isChecked = !binding.wishlistCheckbox.isChecked
            actionListener?.onCheckBulkDeleteOption(item.wishlistItem.id, binding.wishlistCheckbox.isChecked, position)
        }
        binding.root.setOnClickListener {
            binding.wishlistCheckbox.isChecked = !binding.wishlistCheckbox.isChecked
            actionListener?.onCheckBulkDeleteOption(item.wishlistItem.id, binding.wishlistCheckbox.isChecked, position)
        }
    }

    private fun renderRegularWishlist(item: WishlistV2TypeLayoutData, buttonSecondary: FrameLayout, rlPrimaryButton: RelativeLayout) {
        binding.wishlistCheckbox.gone()
        buttonSecondary.visible()
        rlPrimaryButton.visible()
        binding.pcGridItem.setOnClickListener {
            actionListener?.onProductItemClicked(item.wishlistItem, position)
        }
    }

    private fun renderButtonPrimaryAtc(buttonAtc: UnifyButton, buttonSeeSimilarProduct: UnifyButton, item: WishlistV2TypeLayoutData) {
        buttonSeeSimilarProduct.gone()
        buttonAtc.visible()
        binding.pcGridItem.setAddToCartWishlistOnClickListener { actionListener?.onAtc(item.wishlistItem, position) }
    }

    private fun renderButtonSeeSimilarProduct(buttonAtc: UnifyButton, buttonSeeSimilarProduct: UnifyButton, item: WishlistV2TypeLayoutData) {
        buttonAtc.gone()
        buttonSeeSimilarProduct.visible()
        binding.pcGridItem.setSeeSimilarProductWishlistOnClickListener {
            actionListener?.onCheckSimilarProduct(
                    item.wishlistItem.buttons.primaryButton.url
            )
        }
    }
}