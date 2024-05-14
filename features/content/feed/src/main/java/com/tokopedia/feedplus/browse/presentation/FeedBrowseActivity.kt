package com.tokopedia.feedplus.browse.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.di.FeedBrowseInjector
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseActivity : BaseActivity() {

    @Inject lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setupStatusBar()
        setContentView(R.layout.activity_feed_browse)

        if (supportFragmentManager.findFragmentByTag(TAG_BROWSE_FRAGMENT) == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.feed_browse_container,
                    FeedBrowseFragment.create(supportFragmentManager, classLoader, intent.extras),
                    TAG_BROWSE_FRAGMENT
                )
            }
        }
    }

    private fun inject() {
        FeedBrowseInjector.get(this).inject(this)
    }

    @SuppressLint("DeprecatedMethod")
    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!this.isDarkMode()) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor =
            ContextCompat.getColor(this, unifyprinciplesR.color.Unify_Background)
    }

    companion object {
        private const val TAG_BROWSE_FRAGMENT = "feed_browse"
    }
}
