package com.tokopedia.feedplus.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment

class FeedPlusDetailActivity : BaseSimpleActivity() {

    companion object {
        const val EXTRA_DETAIL_ID = "extra_detail_id"
        const val EXTRA_ANALYTICS_PAGE_ROW_NUMBER = "EXTRA_ANALYTICS_PAGE_ROW_NUMBER"
    }

    object DeepLinkIntents {

        @JvmStatic
        @DeepLink(ApplinkConst.FEED_DETAILS)
        fun getCallingIntent(context: Context?, extras: Bundle): Intent? {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, FeedPlusDetailActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }

        fun getIntent(activity: FragmentActivity?, detailId: String?,
                      pageRowNumber: String?): Intent? {
            val intent = Intent(activity, FeedPlusDetailActivity::class.java)
            intent.putExtra(EXTRA_DETAIL_ID, detailId)
            intent.putExtra(EXTRA_ANALYTICS_PAGE_ROW_NUMBER, pageRowNumber)
            return intent
        }

    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) bundle.putAll(intent.extras)
        return FeedPlusDetailFragment.createInstance(bundle)
    }
}
