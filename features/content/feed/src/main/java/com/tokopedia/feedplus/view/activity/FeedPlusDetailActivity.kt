package com.tokopedia.feedplus.view.activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment

@Suppress("LateinitUsage")
class FeedPlusDetailActivity : BaseSimpleActivity() {

    private lateinit var detailId: String
    private lateinit var shopId: String
    private lateinit var authorType: String
    private lateinit var activityId: String
    private lateinit var shopName: String
    private lateinit var saleType: String
    private lateinit var saleStatus: String
    private var postPosition: Int = DEFAULT_INVALID_POSITION
    private lateinit var contentSlotValue: String
    private lateinit var postType: String
    private var isFollowed: Boolean = false
    private var hasVoucher: Boolean = false

    companion object {
        const val EXTRA_DETAIL_ID = "extra_detail_id"
        const val EXTRA_ANALYTICS_PAGE_ROW_NUMBER = "EXTRA_ANALYTICS_PAGE_ROW_NUMBER"
        const val PARAM_AUTHOR_ID = "shop_id"
        const val PARAM_AUTHOR_TYPE = "author_type"
        const val PARAM_ACTIVITY_ID = "activity_id"
        const val PARAM_IS_FOLLOWED = "is_followed"
        const val PARAM_HAS_VOUCHER = "has_voucher"
        const val PARAM_POST_TYPE = "post_type"
        const val PARAM_SHOP_NAME = "shop_name"
        const val PARAM_SALE_TYPE = "sale_type"
        const val PARAM_SALE_STATUS = "sale_status"
        const val PARAM_POST_POSITION = "position"
        private const val DEFAULT_INVALID_POSITION = -1
        const val PARAM_CONTENT_SLOT_VALUE = "content_slot_value"
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
        shopId = intent.getStringExtra(PARAM_AUTHOR_ID).orEmpty()
        authorType = intent.getStringExtra(PARAM_AUTHOR_TYPE).orEmpty()
        postType = intent.getStringExtra(PARAM_POST_TYPE).orEmpty()
        isFollowed = intent.getBooleanExtra(PARAM_IS_FOLLOWED, false)
        hasVoucher = intent.getBooleanExtra(PARAM_HAS_VOUCHER, false)
        activityId = intent.getStringExtra(PARAM_ACTIVITY_ID).orEmpty()
        shopName = intent.getStringExtra(PARAM_SHOP_NAME).orEmpty()
        saleType = intent.getStringExtra(PARAM_SALE_TYPE).orEmpty()
        saleStatus = intent.getStringExtra(PARAM_SALE_STATUS).orEmpty()
        postPosition = intent.getIntExtra(PARAM_POST_POSITION, DEFAULT_INVALID_POSITION)
        contentSlotValue = intent.getStringExtra(PARAM_CONTENT_SLOT_VALUE).orEmpty()
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putString(EXTRA_DETAIL_ID, detailId)
        bundle.putString(PARAM_AUTHOR_ID, shopId)
        bundle.putString(PARAM_AUTHOR_TYPE, authorType)
        bundle.putString(PARAM_ACTIVITY_ID, activityId)
        bundle.putString(PARAM_POST_TYPE, postType)
        bundle.putInt(PARAM_POST_POSITION, postPosition)
        bundle.putString(PARAM_SHOP_NAME, shopName)
        bundle.putString(PARAM_SALE_STATUS, saleStatus)
        bundle.putString(PARAM_CONTENT_SLOT_VALUE, contentSlotValue)
        bundle.putString(PARAM_SALE_TYPE, saleType)
        bundle.putBoolean(PARAM_IS_FOLLOWED, isFollowed)
        bundle.putBoolean(PARAM_HAS_VOUCHER, hasVoucher)
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
}
