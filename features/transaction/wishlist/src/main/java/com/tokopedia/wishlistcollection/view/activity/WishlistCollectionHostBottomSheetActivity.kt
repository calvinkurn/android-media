package com.tokopedia.wishlistcollection.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_PRODUCT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_SRC
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionHostBottomSheetFragment

class WishlistCollectionHostBottomSheetActivity: BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.activity_wishlist_collection_bottomsheet

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): WishlistCollectionHostBottomSheetFragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return WishlistCollectionHostBottomSheetFragment.newInstance(bundle)
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val productId = it.getQueryParameter(PATH_PRODUCT_ID).toEmptyStringIfNull()
            intent.putExtra(PATH_PRODUCT_ID, productId)

            val src = it.getQueryParameter(PATH_SRC).toEmptyStringIfNull()
            intent.putExtra(PATH_SRC, src)
        }
    }
}