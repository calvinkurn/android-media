package com.tokopedia.people.views.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.views.adapter.UserPostBaseAdapter
import com.tokopedia.people.di.UserProfileModule
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

    override fun getNewFragment(): Fragment? {
        return UserProfileFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle(),
        )
    }

    private fun inject() {
        DaggerUserProfileComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication).baseAppComponent,
            )
            .userProfileModule(UserProfileModule(this))
            .build()
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun forDeeplink() {
        bundle = intent.extras
        if (intent.data != null) {
            bundle = UriUtil.destructiveUriBundle(
                ApplinkConstInternalGlobal.USER_PROFILE_LANDING,
                intent.data,
                bundle,
            )
        }

        bundle?.putString(EXTRA_USERNAME, intent.data?.pathSegments?.get(0))
    }

    companion object {
        val EXTRA_USERNAME = "userName"
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getUserProfile(context: Context, extras: Bundle): Intent {
            return getCallingIntent(context, extras)
        }
    }
}
