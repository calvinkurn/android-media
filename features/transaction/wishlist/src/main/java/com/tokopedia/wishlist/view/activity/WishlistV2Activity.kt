package com.tokopedia.wishlist.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment

/**
 * Created by fwidjaja on 14/10/21.
 */
class WishlistV2Activity: BaseSimpleActivity() {
    override fun getNewFragment(): WishlistV2Fragment {
        return WishlistV2Fragment.newInstance()
    }
}