package com.tokopedia.stories.view.activity

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stories.databinding.ActivityStoriesBinding
import com.tokopedia.stories.di.StoriesInjector
import com.tokopedia.stories.domain.model.StoriesSource
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.utils.ARGS_ENTRY_POINT
import com.tokopedia.stories.view.utils.ARGS_SOURCE
import com.tokopedia.stories.view.utils.ARGS_SOURCE_ID
import com.tokopedia.stories.view.utils.KEY_ARGS
import com.tokopedia.stories.view.utils.KEY_CONFIG_ENABLE_STORIES_ROOM
import com.tokopedia.stories.view.utils.TAG_FRAGMENT_STORIES_GROUP
import com.tokopedia.stories.view.viewmodel.StoriesViewModelFactory
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

    @Inject
    lateinit var viewModelFactory: StoriesViewModelFactory.Creator

    private val storiesArgs: StoriesArgsModel
        get() {
            val path = intent.data?.pathSegments
            return StoriesArgsModel(
                authorId = path?.last().orEmpty(),
                authorType = path?.first().orEmpty(),
                source = intent.data?.getQueryParameter(ARGS_SOURCE).ifNullOrBlank {
                    StoriesSource.SHOP_ENTRY_POINT.value
                },
                sourceId = intent.data?.getQueryParameter(ARGS_SOURCE_ID).orEmpty(),
                entryPoint = intent.data?.getQueryParameter(ARGS_ENTRY_POINT).orEmpty(),
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initFragmentFactory()
        super.onCreate(savedInstanceState)
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

        bundle = Bundle().apply {
            putParcelable(KEY_ARGS, storiesArgs)
        }
    }

    private fun setupViews() {
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isEnableStoriesRoom()) {
            openFragment()
        }
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
