package com.tokopedia.profile.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.profile.view.fragment.ProfileEmptyFragment
import com.tokopedia.profile.view.fragment.ProfileFragment

/**
 * @author by milhamj on 17/09/18.
 */
class ProfileActivity : BaseSimpleActivity() {

    companion object {
        const val PARAM_IS_FOLLOWING = "is_following"
        const val IS_FOLLOWING_TRUE = 1
        const val IS_FOLLOWING_FALSE = 0

        const val EXTRA_PARAM_USER_ID = "user_id"
        const val EXTRA_PARAM_AFTER_POST = "after_post"
        const val EXTRA_PARAM_AFTER_EDIT = "after_edit"
        const val EXTRA_PARAM_SUCCESS_POST = "success_post"
        const val TRUE = "true"
        const val ZERO = "0"
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        intent.data?.let { uri ->
            val userId = uri.lastPathSegment
            userId?.let { intent.putExtra(EXTRA_PARAM_USER_ID, userId) }
        }
        intent.extras.let {
            bundle.putAll(it)
        }
        return if (!GlobalConfig.isSellerApp()) {
            hideToolbar()
            ProfileFragment.createInstance(bundle)
        } else {
            ProfileEmptyFragment.createInstance(bundle)
        }
    }

    fun hideToolbar(){
        toolbar.hide()
    }

    override fun onResume() {
        super.onResume()
        if (!GlobalConfig.isSellerApp()) {
            hideToolbar()
        }
    }
}