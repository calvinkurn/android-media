package com.tokopedia.wishlistcollection.view.activity

import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionEditFragment

class WishlistCollectionEditActivity: BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.activity_wishlist_collection_edit

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getToolbarResourceID(): Int = R.id.toolbar

    override fun getNewFragment(): WishlistCollectionEditFragment {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return WishlistCollectionEditFragment.newInstance(bundle)
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }
}
