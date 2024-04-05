package com.tokopedia.feedplus.browse.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.feedplus.browse.di.DaggerFeedBrowseComponent
import com.tokopedia.feedplus.databinding.ActivityFragmentOnlyBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class FeedSearchResultActivity: BaseActivity() {
    @Inject lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityFragmentOnlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setupStatusBar()
        binding = ActivityFragmentOnlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            replace(
                binding.root.id,
                FeedSearchResultFragment.create(supportFragmentManager, classLoader, intent.extras),
                TAG_SEARCH_RESULT_FRAGMENT
            )
        }
    }

    private fun inject() {
        DaggerFeedBrowseComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
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
        private const val TAG_SEARCH_RESULT_FRAGMENT = "feed_search_result"
        const val KEYWORD_PARAM = "search_keyword_param"
    }
}
