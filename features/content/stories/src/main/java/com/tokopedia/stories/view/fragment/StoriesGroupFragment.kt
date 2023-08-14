package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.stories.databinding.FragmentStoriesGroupBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoriesGroupPagerAdapter
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesGroupFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesGroupBinding? = null
    private val binding: FragmentStoriesGroupBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

    private val pagerAdapter: StoriesGroupPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StoriesGroupPagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle,
        )
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelAction(StoriesUiAction.SetArgumentsData(arguments))

        setupViewPager()
        setupObserver()
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun setupViewPager() = with(binding.storiesGroupViewPager) {
        adapter = pagerAdapter
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModelAction(StoriesUiAction.SetGroupMainData(position))
                super.onPageSelected(position)
            }
        })
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun setupUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroup(prevState?.storiesGroup, state.storiesGroup)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is StoriesUiEvent.SelectGroup -> manageNextPageEvent(event.position)
                }
            }
        }
    }

    private fun renderStoriesGroup(
        prevState: List<StoriesGroupUiModel>?,
        state: List<StoriesGroupUiModel>,
    ) {
        if (prevState == state) return

        pagerAdapter.setStoriesGroup(state)
    }

    private fun manageNextPageEvent(position: Int) = with(binding.storiesGroupViewPager) {
        currentItem = position
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "StoriesGroupFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): StoriesGroupFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesGroupFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesGroupFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoriesGroupFragment
        }
    }

}
