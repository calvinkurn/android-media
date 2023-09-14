package com.tokopedia.stories.view.activity

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.stories.R
import com.tokopedia.stories.databinding.ActivityStoriesBinding
import com.tokopedia.stories.di.StoriesInjector
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import com.tokopedia.stories.view.utils.SHOP_ID
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
        supportFragmentManager.executePendingTransactions()
        val existingFragment = supportFragmentManager.findFragmentByTag(StoriesGroupFragment.TAG)
        if (existingFragment is StoriesGroupFragment && existingFragment.isVisible) return

        supportFragmentManager.beginTransaction().apply {
            add(
                binding.fragmentContainer.id,
                StoriesGroupFragment.getFragment(
                    fragmentManager = supportFragmentManager,
                    classLoader = classLoader,
                    bundle = bundle ?: Bundle(),
                ),
                StoriesGroupFragment.TAG,
            )
        }.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
