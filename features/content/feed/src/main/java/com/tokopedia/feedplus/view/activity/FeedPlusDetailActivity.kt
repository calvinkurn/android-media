package com.tokopedia.feedplus.view.activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment

class FeedPlusDetailActivity : BaseSimpleActivity() {

    private lateinit var detailId: String
    private lateinit var shopId: String
    private lateinit var activityId: String

    companion object {
        const val EXTRA_DETAIL_ID = "extra_detail_id"
        const val EXTRA_ANALYTICS_PAGE_ROW_NUMBER = "EXTRA_ANALYTICS_PAGE_ROW_NUMBER"
        const val PARAM_SHOP_ID = "shop_id"
        const val PARAM_ACTIVITY_ID = "activity_id"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromIntent()
        hideStatusBar()
        super.onCreate(savedInstanceState)
    }

    private fun hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            val uri = it.pathSegments
            detailId = uri[uri.lastIndex]
        }
        shopId = intent.getStringExtra(PARAM_SHOP_ID)
        activityId = intent.getStringExtra(PARAM_ACTIVITY_ID)

    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putString(EXTRA_DETAIL_ID, detailId)
        bundle.putString(PARAM_SHOP_ID, shopId)
        bundle.putString(PARAM_ACTIVITY_ID, activityId)
        return FeedPlusDetailFragment.createInstance(bundle)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_feed_detail
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragment_container
    }

    fun getShopInfoLayout(): View? {
        return findViewById(R.id.shop_info_card)
    }

    fun getFooterLayout(): View? {
        return findViewById(R.id.footer)
    }

}
