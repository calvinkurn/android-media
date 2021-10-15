package com.tokopedia.wishlist.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wishlist.view.fragment.RevampedWishlistFragment

/**
 * Created by fwidjaja on 14/10/21.
 */
class RevampedWishlistActivity: BaseSimpleActivity() {
    override fun getNewFragment(): RevampedWishlistFragment {
        return RevampedWishlistFragment.newInstance()
    }
}