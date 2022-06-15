package com.tokopedia.wishlist.view.activity

import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.view.fragment.WishlistV2CollectionFragment
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment

class WishlistV2CollectionActivity: BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.activity_collection_wishlist_v2

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): WishlistV2CollectionFragment {
        return WishlistV2CollectionFragment.newInstance()
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }
}