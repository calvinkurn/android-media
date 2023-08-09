package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.stories.databinding.FragmentStoriesBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoriesPagerAdapter
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesBinding? = null
    private val binding: FragmentStoriesBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

    private val pagerAdapter: StoriesPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StoriesPagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle,
            binding.storiesViewPager,
        ) { selectedPage ->
            viewModelAction(StoriesUiAction.SelectPage(selectedPage))
        }
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelAction(StoriesUiAction.SetInitialData(arguments))

        setupViewPager()
        setupObserver()
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun setupViewPager() = with(binding.storiesViewPager) {
        adapter = pagerAdapter
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun setupUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.storiesUiState.withCache().collectLatest { (prevValue, value) ->
                renderStoriesCategory(prevValue, value)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.storiesUiEvent.collect { event ->
                when (event) {
                    is StoriesUiEvent.NextPage -> manageNextPageEvent(event.position)
                }
            }
        }
    }

    private fun renderStoriesCategory(
        prevValue: StoriesUiModel?,
        value: StoriesUiModel
    ) {
        if (prevValue == StoriesUiModel.Empty || prevValue == value) return

        pagerAdapter.setStoriesData(value)
    }

    private fun manageNextPageEvent(position: Int) = with(binding.storiesViewPager) {
        currentItem = position
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "StoriesFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): StoriesFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoriesFragment
        }
    }

}
