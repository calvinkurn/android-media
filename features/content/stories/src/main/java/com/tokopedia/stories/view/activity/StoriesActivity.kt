package com.tokopedia.stories.view.activity

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.stories.databinding.ActivityStoriesBinding
import com.tokopedia.stories.di.StoriesInjector
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var _binding: ActivityStoriesBinding? = null
    private val binding: ActivityStoriesBinding
        get() = _binding!!

    private var bundle: Bundle? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<StoriesViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        getData()
        super.onCreate(savedInstanceState)
        setupViews()
        observeEvent()
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

        val path = data.pathSegments
        bundle = Bundle().apply {
            putString(SHOP_ID, path[SHOP_ID_INDEX])
        }
    }

    private fun setupViews() {
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment()
        binding.vStoriesOnboarding.ivOnboardClose.setOnClickListener {
            binding.vStoriesOnboarding.root.gone()
        }
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

    private fun observeEvent() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is StoriesUiEvent.OnboardShown -> binding.vStoriesOnboarding.root.showWithCondition(event.needToShow)
                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val SHOP_ID_INDEX = 1
    }

}
