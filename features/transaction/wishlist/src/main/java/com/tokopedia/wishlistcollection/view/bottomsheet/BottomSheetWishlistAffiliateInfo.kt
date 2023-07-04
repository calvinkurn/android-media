package com.tokopedia.wishlistcollection.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.wishlist.databinding.BottomsheetWishlistAffiliateInfoBinding

class BottomSheetWishlistAffiliateInfo {

    fun show(isAffiliate: Boolean, context: Context, fragmentManager: FragmentManager) {
        val bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = true
            isSkipCollapseState = true
            overlayClickDismiss = true
            clearContentPadding = true

            setTitle(context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_info_title))
            val binding = BottomsheetWishlistAffiliateInfoBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            )
            binding.setupChildView(isAffiliate, context)
            setChild(binding.root)
        }
        bottomSheet.show(fragmentManager, "")
    }

    private fun BottomsheetWishlistAffiliateInfoBinding.setupChildView(
        isAffiliate: Boolean,
        context: Context
    ) {
        val contentBottom = StringBuilder().apply {
            if (!isAffiliate) {
                append(context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_info_content_non_affiliate_additional_info))
                append("\n\n")
            }
            append(context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_info_content_bottom))
        }
        labelWishlistCollectionInfoContentBottom.text = contentBottom
        if (isAffiliate) {
            labelWishlistCollectionInfoSubFooter.visible()
            labelWishlistCollectionInfoFooter.text = context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_info_footer)
            labelWishlistCollectionInfoSubFooter.text = context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_info_sub_footer)
            btnAffiliateAction.text =
                context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_check_affiliate)
        } else {
            labelWishlistCollectionInfoSubFooter.gone()
            labelWishlistCollectionInfoFooter.text = context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_info_footer_non_affiliate)
            btnAffiliateAction.text =
                context.getString(com.tokopedia.wishlist.R.string.collection_affiliate_register_affiliate)
        }
    }
}
