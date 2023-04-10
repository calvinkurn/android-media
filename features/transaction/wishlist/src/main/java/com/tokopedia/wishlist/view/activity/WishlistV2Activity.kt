package com.tokopedia.wishlist.view.activity

import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment

/**
 * Created by fwidjaja on 14/10/21.
 */
class WishlistV2Activity : BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.activity_wishlist_v2

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): WishlistV2Fragment {
        val bundle = Bundle()
        return WishlistV2Fragment.newInstance(bundle)
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }
}
