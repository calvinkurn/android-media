package com.tokopedia.kol.feature.postdetail.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.view.fragment.ContentDetailFragment

class ContentDetailActivity : BaseSimpleActivity() {
    var contentDetailFirstPostData : FeedXCard? = null


    override fun getNewFragment(): Fragment {
        val bundle = Bundle().apply {
            putString(
                PARAM_POST_ID, postId()
            )
        }
        return ContentDetailFragment.newInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        hideStatusBar()
    }

    private fun hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun postId(): String {
        return intent?.data?.lastPathSegment ?: DEFAULT_POST_ID
    }

    fun getSource(): String {
        return intent?.extras?.getString(PARAM_SOURCE) ?: SHARE_LINK
    }

    fun setContentDetailMainPostData(card: FeedXCard?) {
        this.contentDetailFirstPostData = card
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_content_detail
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragment_container
    }

    fun getHeaderView(): View? {
        return findViewById(R.id.content_header)
    }

    override fun onBackPressed() {
        if (getSource() == SHARE_LINK) {
            goToFeed()
        } else {
            super.onBackPressed()
        }
    }

    private fun goToFeed() {
        this.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME_FEED)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }
    }

    companion object {
        const val PARAM_POST_ID = "post_id"
        const val DEFAULT_POST_ID = "0"
        const val PARAM_SOURCE = "source"
        const val SHARE_LINK = "share_link"
    }
}