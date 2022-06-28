package com.tokopedia.wishlistcollection.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.wishlistcommon.util.WishlistHandler

class WishlistCollectionBottomSheetFragment: Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): WishlistCollectionBottomSheetFragment {
            return WishlistCollectionBottomSheetFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WishlistHandler.showBottomSheetCollection(childFragmentManager, "2379802552", "pdp")
    }
}