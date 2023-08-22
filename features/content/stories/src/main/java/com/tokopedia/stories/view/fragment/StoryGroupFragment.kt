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
import com.tokopedia.stories.databinding.FragmentStoryGroupBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoryGroupPagerAdapter
import com.tokopedia.stories.view.animation.ZoomOutPageTransformer
import com.tokopedia.stories.view.model.StoryUiModel.StoryGroupUiModel
import com.tokopedia.stories.view.viewmodel.StoryViewModel
import com.tokopedia.stories.view.viewmodel.action.StoryUiAction
import com.tokopedia.stories.view.viewmodel.event.StoryUiEvent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoryGroupFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoryGroupBinding? = null
    private val binding: FragmentStoryGroupBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoryViewModel> { viewModelFactory }

    private val pagerAdapter: StoryGroupPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StoryGroupPagerAdapter(
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
        _binding = FragmentStoryGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelAction(StoryUiAction.SetArgumentsData(arguments))

        setupViewPager()
        setupObserver()
    }

    private fun viewModelAction(event: StoryUiAction) {
        viewModel.submitAction(event)
    }

    private fun setupViewPager() = with(binding.storyGroupViewPager) {
        setPageTransformer(ZoomOutPageTransformer())
        adapter = pagerAdapter
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModelAction(StoryUiAction.SetGroupMainData(position))
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
                renderStoryGroup(prevState?.storyGroup, state.storyGroup)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is StoryUiEvent.SelectGroup -> manageNextPageEvent(event.position)
                }
            }
        }
    }

    private fun renderStoryGroup(
        prevState: List<StoryGroupUiModel>?,
        state: List<StoryGroupUiModel>,
    ) {
        if (prevState == state) return

        pagerAdapter.setStoryGroup(state)
    }

    private fun manageNextPageEvent(position: Int) = with(binding.storyGroupViewPager) {
        currentItem = position
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "StoryGroupFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): StoryGroupFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoryGroupFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoryGroupFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoryGroupFragment
        }
    }

}
