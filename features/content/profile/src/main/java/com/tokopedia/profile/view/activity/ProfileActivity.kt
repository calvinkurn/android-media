package com.tokopedia.profile.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.profile.view.fragment.ProfileFragment

/**
 * @author by milhamj on 17/09/18.
 */

class ProfileActivity : BaseSimpleActivity() {

    companion object {
        const val PARAM_IS_FOLLOWING = "is_following"
        const val IS_FOLLOWING_TRUE = 1
        const val IS_FOLLOWING_FALSE = 0

        private val EXTRA_PARAM_USER_ID = "user_id"
        private val EXTRA_PARAM_POST_ID = "post_id"
        private val ZERO = "0"

        fun createIntent(context: Context?, userId: String): Intent {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(EXTRA_PARAM_USER_ID, userId)
            return intent
        }

        fun createIntentFromFeed(context: Context?, userId: String, postId: Int): Intent {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(EXTRA_PARAM_USER_ID, userId)
            intent.putExtra(EXTRA_PARAM_POST_ID, postId)
            return intent
        }
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.PROFILE)
        @JvmStatic
        fun createIntent(context: Context?, extras: Bundle): Intent {
            return createIntent(context, extras.getString(EXTRA_PARAM_USER_ID, ""))
        }
    }

    override fun getNewFragment(): Fragment = ProfileFragment.createInstance()
}