package com.tokopedia.stories.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.databinding.ActivityStoriesBinding
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import javax.inject.Inject

abstract class StoriesBaseActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel by viewModels<StoriesViewModel> { viewModelFactory }

    private var _binding: ActivityStoriesBinding? = null
    private val binding: ActivityStoriesBinding
        get() = _binding!!

    abstract fun inject()

    abstract fun getIntentData()

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()

        super.onCreate(savedInstanceState)

        getIntentData()

        setupViewBinding()
    }

    private fun setupViewBinding() {
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
