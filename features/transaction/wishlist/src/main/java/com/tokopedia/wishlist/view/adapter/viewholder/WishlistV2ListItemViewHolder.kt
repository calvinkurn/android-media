package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.databinding.WishlistV2ListItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlistcollection.util.WishlistCollectionUtils.clickWithDebounce
import com.tokopedia.productcard.R as RProductCard

class WishlistV2ListItemViewHolder(private val binding: WishlistV2ListItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: WishlistV2TypeLayoutData,
        position: Int,
        isShowCheckbox: Boolean,
        isAutoSelected: Boolean,
        isAddBulkModeFromOthers: Boolean
    ) {
        if (item.dataObject is ProductCardModel) {
            binding.pcListItem.setProductModel(item.dataObject)

            val footerLayout = binding.pcListItem.findViewById<View>(RProductCard.id.productCardFooterLayout)
            val buttonSecondary = footerLayout.findViewById<FrameLayout>(RProductCard.id.buttonThreeDotsWishlist)
            val rlPrimaryButton = footerLayout.findViewById<RelativeLayout>(RProductCard.id.rlPrimaryButtonWishlist)
            val buttonAtc = footerLayout.findViewById<UnifyButton>(RProductCard.id.buttonAddToCartWishlist)
            val buttonSeeSimilarProduct = footerLayout.findViewById<UnifyButton>(RProductCard.id.buttonSeeSimilarProductWishlist)

            if (isShowCheckbox) {
                renderBulkDelete(item, buttonSecondary, rlPrimaryButton, isAutoSelected, isAddBulkModeFromOthers)
            } else {
                renderRegularWishlist(item, buttonSecondary, rlPrimaryButton)
            }

            if (item.dataObject.hasAddToCartWishlist) {
                renderButtonPrimaryAtc(buttonAtc, buttonSeeSimilarProduct, item)
            } else {
                renderButtonSeeSimilarProduct(buttonAtc, buttonSeeSimilarProduct, item)
            }

            binding.pcListItem.setThreeDotsWishlistOnClickListener { actionListener?.onThreeDotsMenuClicked(item.wishlistItem) }
            actionListener?.onViewProductCard(item.wishlistItem, position)
        }
    }

    private fun renderBulkDelete(
        item: WishlistV2TypeLayoutData,
        buttonSecondary: FrameLayout,
        rlPrimaryButton: RelativeLayout,
        isAutoSelected: Boolean,
        isAddBulkModeFromOthers: Boolean
    ) {
        binding.wishlistCheckbox.setOnCheckedChangeListener(null)
        binding.wishlistCheckbox.visible()
        binding.wishlistCheckbox.isChecked = item.isChecked
        binding.wishlistCheckbox.skipAnimation()
        binding.wishlistCheckbox.setOnCheckedChangeListener { _, isChecked ->
            setCheckboxCheckedChangedListener(isChecked, isAddBulkModeFromOthers, isAutoSelected, item.wishlistItem)
        }
        buttonSecondary.gone()
        rlPrimaryButton.gone()
        binding.pcListItem.setOnClickListener {
            binding.wishlistCheckbox.isChecked = !binding.wishlistCheckbox.isChecked
        }
        binding.root.setOnClickListener {
            binding.wishlistCheckbox.isChecked = !binding.wishlistCheckbox.isChecked
        }
    }

    private fun setCheckboxCheckedChangedListener(isChecked: Boolean, isAddBulkModeFromOthers: Boolean, isAutoSelected: Boolean, item: WishlistV2UiModel.Item) {
        if (!isAddBulkModeFromOthers) {
            if (isAutoSelected) {
                actionListener?.onUncheckAutomatedBulkDelete(item.id, isChecked, position)
            } else {
                actionListener?.onCheckBulkOption(item.id, isChecked, position)
            }
        } else {
            actionListener?.onValidateCheckBulkOption(item.id, isChecked, position)
        }
    }

    private fun renderRegularWishlist(item: WishlistV2TypeLayoutData, buttonSecondary: FrameLayout, rlPrimaryButton: RelativeLayout) {
        binding.wishlistCheckbox.gone()
        buttonSecondary.visible()
        rlPrimaryButton.visible()
        binding.pcListItem.clickWithDebounce {
            actionListener?.onProductItemClicked(item.wishlistItem, position)
        }
    }

    private fun renderButtonPrimaryAtc(buttonAtc: UnifyButton, buttonSeeSimilarProduct: UnifyButton, item: WishlistV2TypeLayoutData) {
        buttonSeeSimilarProduct.gone()
        buttonAtc.visible()
        binding.pcListItem.setAddToCartWishlistOnClickListener { actionListener?.onAtc(item.wishlistItem, position) }
    }

    private fun renderButtonSeeSimilarProduct(buttonAtc: UnifyButton, buttonSeeSimilarProduct: UnifyButton, item: WishlistV2TypeLayoutData) {
        buttonAtc.gone()
        buttonSeeSimilarProduct.visible()
        binding.pcListItem.setSeeSimilarProductWishlistOnClickListener {
            actionListener?.onCheckSimilarProduct(
                item.wishlistItem.buttons.primaryButton.url
            )
        }
    }
}
