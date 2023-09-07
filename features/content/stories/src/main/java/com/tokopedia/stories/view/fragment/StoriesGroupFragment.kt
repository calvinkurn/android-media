package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.stories.databinding.FragmentStoriesGroupBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoriesGroupPagerAdapter
import com.tokopedia.stories.view.animation.StoriesPageAnimation
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.utils.SHOP_ID
import com.tokopedia.stories.view.utils.isNetworkError
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModelFactory
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ResumeStories
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesGroupFragment @Inject constructor(
    private val viewModelFactory: StoriesViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesGroupBinding? = null
    private val binding: FragmentStoriesGroupBinding
        get() = _binding!!

    private val shopId: String
        get() = arguments?.getString(SHOP_ID).orEmpty()

    val viewModelProvider get() = viewModelFactory.create(shopId)

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelProvider }

    private val pagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            showSelectedGroupHighlight(position)
            viewModelAction(StoriesUiAction.SetGroupMainData(position))
            super.onPageSelected(position)
        }
    }

    private val pagerAdapter: StoriesGroupPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StoriesGroupPagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle
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
        setupViews()
        setupObserver()

        initializeData(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModelAction(StoriesUiAction.SaveInstanceStateData(outState))
    }

    override fun onPause() {
        super.onPause()
        viewModelAction(PauseStories)
    }

    override fun onResume() {
        super.onResume()
        viewModelAction(ResumeStories)
    }

    private fun initializeData(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) viewModelAction(StoriesUiAction.SetArgumentsData(arguments))
        else viewModelAction(StoriesUiAction.GetSavedInstanceStateData(savedInstanceState))
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun setupViews() = with(binding) {
        showPageLoading(true)

        layoutGroupLoading.icCloseLoading.setOnClickListener { activity?.finish() }
        if (storiesGroupViewPager.adapter != null) return@with
        storiesGroupViewPager.adapter = pagerAdapter
        storiesGroupViewPager.setPageTransformer(StoriesPageAnimation())
        storiesGroupViewPager.registerOnPageChangeCallback(pagerListener)
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun showSelectedGroupHighlight(position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.tvHighlight.text = pagerAdapter.getCurrentPageGroupName(position)
            binding.tvHighlight.show()
            binding.tvHighlight.animate().alpha(1f)
            delay(1000)
            binding.tvHighlight.animate().alpha(0f)
        }
    }

    private fun setupUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroup(prevState?.storiesGroup, state.storiesGroup)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is StoriesUiEvent.SelectGroup -> selectGroupPosition(event.position, event.showAnimation)
                    StoriesUiEvent.FinishedAllStories -> activity?.finish()
                    is StoriesUiEvent.ErrorGroupPage -> {
                        if (event.throwable.isNetworkError) {
                            // TODO handle error network here
                            showToast("error group network ${event.throwable}")
                        } else {
                            // TODO handle error fetch here
                            showToast("error group content ${event.throwable}")
                        }
                        showPageLoading(false)
                    }
                    else -> return@collect
                }
            }
        }
    }

    private fun renderStoriesGroup(
        prevState: StoriesGroupUiModel?,
        state: StoriesGroupUiModel
    ) {
        if (prevState == null || pagerAdapter.getCurrentData().size == state.groupItems.size) return

        if (state.groupItems.isEmpty()) {
            // TODO handle error empty data state here
            Toast.makeText(
                requireContext(),
                "data categories is empty",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        pagerAdapter.setStoriesGroup(state)
        pagerAdapter.notifyItemRangeChanged(0, state.groupItems.size)
        selectGroupPosition(state.selectedGroupPosition, false)

        showPageLoading(false)
    }

    private fun selectGroupPosition(position: Int, showAnimation: Boolean) = with(binding.storiesGroupViewPager) {
        if (position < 0) return@with

        setCurrentItem(position, showAnimation)
    }

    private fun showPageLoading(isShowLoading: Boolean) = with(binding) {
        layoutGroupLoading.container.showWithCondition(isShowLoading)
        storiesGroupViewPager.showWithCondition(!isShowLoading)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.storiesGroupViewPager.unregisterOnPageChangeCallback(pagerListener)
        _binding = null
    }

    companion object {
        const val TAG = "StoriesGroupFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
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
