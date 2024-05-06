package com.tokopedia.people.views.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.views.fragment.FollowerFollowingListingFragment
import javax.inject.Inject

class FollowerFollowingListingActivity : BaseSimpleActivity() {

    private var bundle: Bundle? = null

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()
        forDeeplink()
        super.onCreate(savedInstanceState)
        toolbar.hide()
        setResult(Activity.RESULT_OK, intent)
    }

    override fun getNewFragment(): Fragment {
        return FollowerFollowingListingFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle()
        )
    }

    private fun inject() {
        DaggerUserProfileComponent.factory()
            .create(
                (applicationContext as BaseMainApplication).baseAppComponent,
                this
            )
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun forDeeplink() {
        if (intent.data == null && intent.extras == null) {
            finish()
            return
        }
        val userName =
            intent.data?.pathSegments?.first() ?: intent.extras?.getString(EXTRA_USERNAME)
        val activeTab =
            intent.data?.pathSegments?.last() ?: intent.extras?.getString(EXTRA_ACTIVE_TAB)

        bundle = intent.extras
        bundle?.putString(EXTRA_USERNAME, userName)
        bundle?.putString(EXTRA_ACTIVE_TAB, activeTab)
    }

    companion object {
        const val EXTRA_USERNAME = "user_name"
        const val EXTRA_ACTIVE_TAB = "active_tab"

        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, FollowerFollowingListingActivity::class.java)
            intent.putExtras(extras)
            return intent
        }
    }
}
