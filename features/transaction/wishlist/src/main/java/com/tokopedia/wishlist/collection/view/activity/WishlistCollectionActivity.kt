package com.tokopedia.wishlist.collection.view.activity

import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.collection.view.fragment.WishlistCollectionFragment

class WishlistCollectionActivity : BaseSimpleActivity(), AppLogInterface {
    override fun getLayoutRes() = R.layout.activity_collection_wishlist

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): WishlistCollectionFragment {
        return WishlistCollectionFragment.newInstance()
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }

    override fun getPageName(): String = PageName.WISHLIST

    override fun shouldTrackEnterPage(): Boolean {
        return true
    }

    override fun isEnterFromWhitelisted() = true
}
