package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.stories.databinding.FragmentStoriesContentBinding
import com.tokopedia.stories.view.components.indicator.StoriesIndicator
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import javax.inject.Inject

class StoriesContentFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesContentBinding? = null
    private val binding: FragmentStoriesContentBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onResume() {
        super.onResume()

        binding.tvCounter.text = viewModel.mCounter.toString()
    }

    private fun setupViews() {
        setupStoriesIndicator()
    }

    private fun setupStoriesIndicator() {
        binding.cvStoriesIndicator.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StoriesIndicator(storiesCount = 10, storiesCurrentPosition = 1, paused = false) {
                    showToast("Habiss")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "StoriesContentFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): StoriesContentFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesContentFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesContentFragment::class.java.name
            ) as StoriesContentFragment
        }
    }

}
