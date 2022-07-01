package com.tokopedia.wishlistcollection.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetCollectionWishlistAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetAddCollectionWishlist

class WishlistCollectionBottomSheetFragment: Fragment(),
    BottomSheetCollectionWishlistAdapter.ActionListener {
    companion object {
        @JvmStatic
        fun newInstance(): WishlistCollectionBottomSheetFragment {
            return WishlistCollectionBottomSheetFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBottomSheetCollection(childFragmentManager, "2379802552", "pdp")
    }

    private fun showBottomSheetCollection(fragmentManager: FragmentManager, productId: String, source: String) {
        val bottomSheetCollection = BottomSheetAddCollectionWishlist.newInstance(productId, source)
        if (bottomSheetCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCollection.setActionListener(this@WishlistCollectionBottomSheetFragment)
        bottomSheetCollection.show(fragmentManager)
    }

    override fun onCollectionItemClicked() {
        println("++ click collection item")
    }

    override fun onCreateNewCollectionClicked() {
        println("++ create new collection")
    }
}