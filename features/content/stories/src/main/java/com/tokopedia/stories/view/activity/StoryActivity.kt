package com.tokopedia.stories.view.activity

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.databinding.ActivityStoryBinding
import com.tokopedia.stories.di.StoryInjector
import com.tokopedia.stories.view.fragment.StoryGroupFragment
import javax.inject.Inject


class StoryActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var _binding: ActivityStoryBinding? = null
    private val binding: ActivityStoryBinding
        get() = _binding!!

    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        getData()
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun inject() {
        StoryInjector.get(this).inject(this)
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
            putString(SHOP_ID, path[SHOP_ID_INDEX])
            if (path.size > STORIES_ID_INDEX) putString(STORIES_ID, path[STORIES_ID_INDEX])
        }
    }

    private fun setupViews() {
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment()
    }

    private fun openFragment() {
        supportFragmentManager.executePendingTransactions()
        val existingFragment = supportFragmentManager.findFragmentByTag(StoryGroupFragment.TAG)
        if (existingFragment is StoryGroupFragment && existingFragment.isVisible) return

        supportFragmentManager.beginTransaction().apply {
            add(
                binding.fragmentContainer.id,
                StoryGroupFragment.getFragment(
                    fragmentManager = supportFragmentManager,
                    classLoader = classLoader,
                    bundle = bundle ?: Bundle(),
                ),
                StoryGroupFragment.TAG,
            )
        }.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val STORIES_ID = "stories_id"
        private const val SHOP_ID_INDEX = 1
        private const val STORIES_ID_INDEX = 2
    }

}
