package com.tokopedia.wishlistcollection.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.EXTRA_NEED_REFRESH
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment

class WishlistCollectionDetailActivity: BaseSimpleActivity() {
    private var isNeedRefresh = false
    override fun getLayoutRes() = R.layout.activity_wishlist_collection_detail

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): WishlistCollectionDetailFragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return WishlistCollectionDetailFragment.newInstance(bundle)
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val collectionId = it.getQueryParameter(ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID).toEmptyStringIfNull()
            intent.putExtra(ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID, collectionId)

            val src = it.getQueryParameter(ApplinkConstInternalPurchasePlatform.PATH_SRC).toEmptyStringIfNull()
            intent.putExtra(ApplinkConstInternalPurchasePlatform.PATH_SRC, src)
        }
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