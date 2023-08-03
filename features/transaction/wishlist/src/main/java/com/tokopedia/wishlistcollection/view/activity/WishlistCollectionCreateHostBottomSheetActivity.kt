package com.tokopedia.wishlistcollection.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionCreateHostBottomSheetFragment

class WishlistCollectionCreateHostBottomSheetActivity : BaseSimpleActivity() {
    override fun getLayoutRes() = R.layout.activity_wishlist_collection_create_bottom_sheet

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): WishlistCollectionCreateHostBottomSheetFragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return WishlistCollectionCreateHostBottomSheetFragment.newInstance(bundle)
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val src = it.getQueryParameter(ApplinkConstInternalPurchasePlatform.PATH_SRC).toEmptyStringIfNull()
            intent.putExtra(ApplinkConstInternalPurchasePlatform.PATH_SRC, src)
        }
    }
}
