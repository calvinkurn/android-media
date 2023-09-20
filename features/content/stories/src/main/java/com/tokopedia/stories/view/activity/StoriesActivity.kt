package com.tokopedia.stories.view.activity

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.ActivityStoriesBinding
import com.tokopedia.stories.di.StoriesInjector
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import com.tokopedia.stories.view.utils.SHOP_ID
import com.tokopedia.stories.view.utils.SHOP_ID_INDEX_APP_LINK
import com.tokopedia.stories.view.utils.TAG_FRAGMENT_STORIES_GROUP
import javax.inject.Inject

class StoriesActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var _binding: ActivityStoriesBinding? = null
    private val binding: ActivityStoriesBinding
        get() = _binding!!

    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        getData()
        super.onCreate(savedInstanceState)
        setupViews()
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.Stories_Theme, true)
        return theme
    }

    private fun inject() {
        StoriesInjector.get(this).inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun getData() {
        val data = intent.data
        if (data == null) {
            finish()
            return
        }

        val id = data.lastPathSegment.ifNullOrBlank {
            "" //TODO() get from query param
        }
        bundle = Bundle().apply {
            putString(SHOP_ID, id)
        }
    }

    private fun setupViews() {
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment()
    }

    private fun openFragment() {
        supportFragmentManager.apply {
            executePendingTransactions()
            val existingFragment = findFragmentByTag(TAG_FRAGMENT_STORIES_GROUP)
            if (existingFragment is StoriesGroupFragment && existingFragment.isVisible) return
            beginTransaction().apply {
                replace(
                    binding.fragmentContainer.id,
                    getStoriesFragment(),
                    TAG_FRAGMENT_STORIES_GROUP,
                )
            }.commit()
        }
    }

    private fun getStoriesFragment(): Fragment {
        return StoriesGroupFragment.getFragment(
            fragmentManager = supportFragmentManager,
            classLoader = classLoader,
            bundle = bundle ?: Bundle(),
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
