package com.tokopedia.people.views.activity

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.people.di.UserProfileInjector
import com.tokopedia.people.views.fragment.UserProfileFragment
import javax.inject.Inject

class UserProfileActivity : BaseSimpleActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()
        forDeeplink()
        super.onCreate(savedInstanceState)
        toolbar.hide()
        setResult(Activity.RESULT_OK, intent)
    }

    override fun getNewFragment(): Fragment {
        return UserProfileFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle(),
        )
    }

    private fun inject() {
        UserProfileInjector.get(this).inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun forDeeplink() {
        if (intent.data == null) {
            finish()
            return
        }
        bundle = intent.extras
        bundle = UriUtil.destructiveUriBundle(
            ApplinkConstInternalGlobal.USER_PROFILE_LANDING,
            intent.data,
            bundle,
        )
        bundle?.putString(EXTRA_USERNAME, intent.data?.pathSegments?.first())
        directToOtherPage(intent.data?.pathSegments)
    }

    private fun directToOtherPage(pathSegment: List<String>?) {
        if (pathSegment == null || pathSegment.size == 1) return

        when (pathSegment.last()) {
            EXTRA_FOLLOWING, EXTRA_FOLLOWERS -> openFollowingFollowersPage(pathSegment)
            else -> return
        }
    }

    private fun openFollowingFollowersPage(pathSegment: List<String>?) {
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, pathSegment?.first())
        bundle.putString(EXTRA_ACTIVE_TAB, pathSegment?.last())

        startActivity(FollowerFollowingListingActivity.getCallingIntent(this, bundle))
        finish()
    }

    companion object {
        const val EXTRA_USERNAME = "user_name"
        const val EXTRA_FOLLOWING = "following"
        const val EXTRA_FOLLOWERS = "followers"

        const val EXTRA_ACTIVE_TAB = "active_tab"
    }
}
