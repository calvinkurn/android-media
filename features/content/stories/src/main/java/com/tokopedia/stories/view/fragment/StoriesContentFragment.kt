package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.stories.databinding.FragmentStoriesContentBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.components.indicator.StoriesEventAction
import com.tokopedia.stories.view.components.indicator.StoriesIndicator
import com.tokopedia.stories.view.model.StoriesDataUiModel
import com.tokopedia.stories.view.utils.onPauseEventStories
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import kotlinx.coroutines.flow.collectLatest
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
        setupStoriesView()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        binding.tvCounter.text = "${viewModel.mCounter.plus(1)}"
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesSelectedPage.withCache().collectLatest { (prevState, state) ->
                renderStoriesIndicator(prevState, state)
            }
        }
    }

    private fun renderStoriesIndicator(
        prevState: StoriesDataUiModel?,
        state: StoriesDataUiModel,
    ) {
        if (prevState == state) return

        with(binding.cvStoriesIndicator) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesIndicator(data = state) { event ->
                        storiesEventAction(event)
                    }
                }
            }
        }
    }

    private fun storiesEventAction(event: StoriesEventAction) {
        when (event) {
            StoriesEventAction.NEXT_INDICATOR -> viewModelAction(StoriesUiAction.NextIndicator)
            StoriesEventAction.NEXT_PAGE -> viewModelAction(StoriesUiAction.NextPage)
        }
    }

    private fun setupStoriesView() = with(binding) {
        flStories.onPauseEventStories { isPaused ->
            if (isPaused) {
                viewModelAction(StoriesUiAction.OnPauseStories)
                cvStoriesIndicator.hide()
            }
            else {
                viewModelAction(StoriesUiAction.OnResumeStories)
                cvStoriesIndicator.show()
            }
        }
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
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
