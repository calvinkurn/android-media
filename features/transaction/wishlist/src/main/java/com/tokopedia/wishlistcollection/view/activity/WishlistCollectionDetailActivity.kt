package com.tokopedia.wishlistcollection.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.EXTRA_NEED_REFRESH
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment

class WishlistCollectionDetailActivity: BaseSimpleActivity() {
    private var isNeedRefresh = false
    override fun getLayoutRes() = R.layout.activity_wishlist_collection_detail

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): WishlistCollectionDetailFragment {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        val collectionId: String
        val uri = intent.data
        if (uri != null && uri.pathSegments.size > 1)  {
            val uriSegment = uri.pathSegments
            collectionId = uriSegment[uriSegment.size - 1]
            bundle.putString(ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID, collectionId)
        }
        return WishlistCollectionDetailFragment.newInstance(bundle)
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }

    fun isNeedRefresh(isNeed: Boolean) {
        this.isNeedRefresh = isNeed
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(EXTRA_NEED_REFRESH, isNeedRefresh)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}
