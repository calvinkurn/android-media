package com.tokopedia.stories.view.activity

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stories.databinding.ActivityStoriesBinding
import com.tokopedia.stories.di.StoriesInjector
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import com.tokopedia.stories.view.utils.KEY_CONFIG_ENABLE_STORIES_ROOM
import com.tokopedia.stories.view.utils.SHOP_ID
import com.tokopedia.stories.view.utils.SHOP_ID_INDEX_APP_LINK
import com.tokopedia.stories.view.utils.TAG_FRAGMENT_STORIES_GROUP
import javax.inject.Inject
import com.tokopedia.stories.R as storiesR

class StoriesActivity : BaseActivity() {

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var _binding: ActivityStoriesBinding? = null
    private val binding: ActivityStoriesBinding
        get() = _binding!!

    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        initFragmentFactory()
        getData()
        setupViews()
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(storiesR.style.Stories_Theme, true)
        return theme
    }

    private fun inject() {
        StoriesInjector.get(this).inject(this)
    }

    private fun initFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun getData() {
        val data = intent.data
        if (data == null) {
            finish()
            return
        }

        val path = data.pathSegments
        bundle = Bundle().apply {
            putString(SHOP_ID, path[SHOP_ID_INDEX_APP_LINK])
        }
    }

    private fun setupViews() {
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isEnableStoriesRoom()) openFragment()
        else finish()
    }

    private fun isEnableStoriesRoom(): Boolean {
        return remoteConfig.getBoolean(KEY_CONFIG_ENABLE_STORIES_ROOM, true)
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
