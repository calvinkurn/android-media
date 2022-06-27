package com.tokopedia.wishlistcommon.util

import androidx.fragment.app.FragmentManager
import com.tokopedia.wishlistcommon.view.bottomsheet.BottomSheetAddCollectionWishlist

object WishlistHandler {

    fun showBottomSheetCollection(fragmentManager: FragmentManager, productId: String, source: String) {
        val bottomSheetCollection = BottomSheetAddCollectionWishlist.newInstance(productId, source)
        if (bottomSheetCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCollection.show(fragmentManager)
    }
}