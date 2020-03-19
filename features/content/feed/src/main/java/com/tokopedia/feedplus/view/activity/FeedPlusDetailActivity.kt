package com.tokopedia.feedplus.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment

class FeedPlusDetailActivity : BaseSimpleActivity() {

    private lateinit var detailId: String
    companion object {
        const val EXTRA_DETAIL_ID = "extra_detail_id"
        const val EXTRA_ANALYTICS_PAGE_ROW_NUMBER = "EXTRA_ANALYTICS_PAGE_ROW_NUMBER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromIntent()
        super.onCreate(savedInstanceState)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            val uri = it.pathSegments
            detailId = uri[uri.lastIndex]
        }
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putString(EXTRA_DETAIL_ID, detailId)
        return FeedPlusDetailFragment.createInstance(bundle)
    }
}
