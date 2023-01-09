package com.tokopedia.kol.feature.postdetail.view.activity

import android.app.Activity
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

    private var actionToRefresh: Boolean = false
    var contentDetailFirstPostData : FeedXCard? = null

    override fun getNewFragment(): Fragment {
        val bundle = Bundle().apply {
            putString(PARAM_POST_ID, postId())
            putString(PARAM_ENTRY_POINT, getSource())
            putInt(PARAM_POSITION, getPosition())
            putString(PARAM_VISITED_USER_ID, getVisitedUserID())
            putString(PARAM_VISITED_USER_ENCRYPTED_ID, getVisitedUserEncryptedID())
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

    private fun getVisitedUserID(): String {
        return intent?.extras?.getString(PARAM_VISITED_USER_ID).orEmpty()
    }

    private fun getVisitedUserEncryptedID(): String {
        return intent?.extras?.getString(PARAM_VISITED_USER_ENCRYPTED_ID).orEmpty()
    }

    private fun postId(): String {
        return intent?.data?.lastPathSegment ?: DEFAULT_POST_ID
    }

    private fun getSource(): String {
        return if (checkSourceIsPushNotication()) {
            return SOURCE_PUSH_NOTIFICATION
        } else {
            intent?.extras?.getString(PARAM_ENTRY_POINT) ?: SHARE_LINK
        }
    }

    private fun checkSourceIsPushNotication(): Boolean {
        return intent?.extras?.getString(PARAM_UTM_MEDIUM).isNullOrEmpty()
            .not() && intent?.extras?.getString(PARAM_UTM_MEDIUM)?.contains(
            PARAM_PUSH_NOTIFICATION,
            true
        ) ?: false
    }

    private fun getPosition(): Int {
        return intent?.extras?.getInt(PARAM_POSITION) ?: 0
    }

    fun setContentDetailMainPostData(card: FeedXCard?) {
        this.contentDetailFirstPostData = card
    }

    fun setActionToRefresh(needRefresh: Boolean) {
        this.actionToRefresh = needRefresh
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
        when {
            getSource() == SHARE_LINK || getSource() == PARAM_PUSH_NOTIFICATION -> goToFeed()
            getSource() == SOURCE_USER_PROFILE -> {
                setResultBeforeFinish()
                supportFinishAfterTransition()
            }
            else -> super.onBackPressed()
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

    private fun setResultBeforeFinish() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(PARAM_ACTION_TO_REFRESH, actionToRefresh)
        })
    }

    companion object {
        const val PARAM_POST_ID = "post_id"
        const val DEFAULT_POST_ID = "0"
        const val PARAM_ENTRY_POINT = "entry_point"
        const val PARAM_UTM_MEDIUM = "utm_medium"
        const val PARAM_PUSH_NOTIFICATION = "push"
        const val SOURCE_PUSH_NOTIFICATION = "push_notification"
        const val SHARE_LINK = "share_link"
        const val PARAM_POSITION = "position"
        const val PARAM_VISITED_USER_ID = "visited_user_id"
        const val PARAM_VISITED_USER_ENCRYPTED_ID = "visited_user_encrypted_id"
        const val SOURCE_USER_PROFILE = "user_profile"
        const val PARAM_ACTION_TO_REFRESH = "action_to_refresh"
    }
}
