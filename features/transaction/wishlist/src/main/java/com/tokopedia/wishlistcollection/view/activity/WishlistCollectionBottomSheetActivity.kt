package com.tokopedia.wishlistcollection.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionBottomSheetFragment

class WishlistCollectionBottomSheetActivity: BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.activity_wishlist_collection_bottomsheet

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): Fragment {
        return WishlistCollectionBottomSheetFragment.newInstance()
    }
}