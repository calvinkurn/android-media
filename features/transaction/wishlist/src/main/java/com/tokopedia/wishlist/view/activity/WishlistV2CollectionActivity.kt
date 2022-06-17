package com.tokopedia.wishlist.view.activity

import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.view.fragment.CollectionWishlistFragment

class WishlistV2CollectionActivity: BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.activity_collection_wishlist

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): CollectionWishlistFragment {
        return CollectionWishlistFragment.newInstance()
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }
}