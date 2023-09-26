package com.tokopedia.stories.view.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.withCache
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.stories.analytics.StoriesAnalytics
import com.tokopedia.stories.analytics.StoriesEEModel
import com.tokopedia.stories.databinding.FragmentStoriesGroupBinding
import com.tokopedia.stories.view.adapter.StoriesGroupPagerAdapter
import com.tokopedia.stories.view.animation.StoriesPageAnimation
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.KEY_ARGS
import com.tokopedia.stories.view.utils.TAG_FRAGMENT_STORIES_GROUP
import com.tokopedia.stories.view.utils.isNetworkError
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModelFactory
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesGroupFragment @Inject constructor(
    private val viewModelFactory: StoriesViewModelFactory.Creator,
    private val analyticFactory: StoriesAnalytics.Factory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesGroupBinding? = null
    private val binding: FragmentStoriesGroupBinding
        get() = _binding!!

    val args: StoriesArgsModel
        @SuppressLint("DeprecatedMethod")
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(KEY_ARGS, StoriesArgsModel::class.java) ?: StoriesArgsModel()
        } else {
            arguments?.getParcelable(KEY_ARGS) ?: StoriesArgsModel()
        }

    val viewModelProvider get() = viewModelFactory.create(requireActivity(), args)

    private val analytic: StoriesAnalytics get() = analyticFactory.create(args)

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelProvider }

    private var mTrackGroupChanged = false

    private val pagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            showSelectedGroupHighlight(position)
            viewModelAction(StoriesUiAction.SetMainData(position))
            trackMoveGroup()
        }
    }

    private val pagerAdapter: StoriesGroupPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StoriesGroupPagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle,
            shopId = args.authorId
        )
    }

    override fun getScreenName(): String {
        return TAG_FRAGMENT_STORIES_GROUP
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

        viewModelAction(StoriesUiAction.SetInitialData)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModelAction(StoriesUiAction.SaveInstanceStateData)
    }

    override fun onPause() {
        super.onPause()
        viewModelAction(PauseStories)
        trackImpressionGroup()
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
            viewModel.storiesState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroup(prevState?.storiesMainData, state.storiesMainData)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesEvent.collect { event ->
                when (event) {
                    is StoriesUiEvent.SelectGroup -> selectGroupPosition(event.position, event.showAnimation)
                    is StoriesUiEvent.ErrorGroupPage -> {
                        showPageLoading(false)
                        if (event.throwable.isNetworkError) {
                            setNoInternet(true)
                            binding.layoutStoriesNoInet.btnStoriesNoInetRetry.setOnClickListener { run { event.onClick() } }
                        } else {
                            setFailed(true)
                            binding.layoutStoriesFailed.btnStoriesFailedLoad.setOnClickListener { run { event.onClick() } }
                        }
                    }
                    StoriesUiEvent.EmptyGroupPage -> {
                        setEmptyPage(true)
                        showPageLoading(false)
                    }
                    StoriesUiEvent.FinishedAllStories -> activity?.finish()
                    else -> return@collect
                }
            }
        }
    }

    private fun renderStoriesGroup(
        prevState: StoriesUiModel?,
        state: StoriesUiModel
    ) {
        if (prevState == state ||
            pagerAdapter.getCurrentData().size == state.groupItems.size ||
            state.selectedGroupPosition < 0
        ) return

        setNoInternet(false)
        setFailed(false)

        pagerAdapter.setStoriesGroup(state)
        pagerAdapter.notifyItemRangeChanged(pagerAdapter.itemCount, state.groupItems.size)
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

    private fun setNoInternet(isShow: Boolean) = with(binding.layoutStoriesNoInet) {
        root.showWithCondition(isShow)
        icCloseLoading.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setFailed(isShow: Boolean) = with(binding.layoutStoriesFailed) {
        root.showWithCondition(isShow)
        icCloseLoading.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setEmptyPage(isShow: Boolean) = with(binding.layoutNoContent) {
        root.translationZ = 1f
        root.showWithCondition(isShow)
        icCloseLoading.show()
        icCloseLoading.setOnClickListener {
            activity?.finish()
        }
    }

    private fun trackImpressionGroup() {
        analytic.sendViewStoryCircleEvent(
            currentCircle = viewModel.mGroup.groupId,
            promotions = viewModel.impressedGroupHeader.mapIndexed { index, storiesGroupHeader ->
                StoriesEEModel(
                    creativeName = "",
                    creativeSlot = index.plus(1).toString(),
                    itemId = "${storiesGroupHeader.groupId} - ${storiesGroupHeader.groupName} - ${args.authorId}",
                    itemName = "/ - stories",
                )
            },
        )
    }

    private fun trackMoveGroup() {
        if (!mTrackGroupChanged) {
            mTrackGroupChanged = true
            return
        }

        analytic.sendClickMoveToOtherGroup()
    }

    private fun trackExitRoom() {
        analytic.sendClickExitStoryRoomEvent(
            storiesId = viewModel.mDetail.id,
            creatorType = "asgc",
            contentType = viewModel.mDetail.content.type.value,
            currentCircle = viewModel.mGroup.groupName,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackExitRoom()
        binding.storiesGroupViewPager.unregisterOnPageChangeCallback(pagerListener)
        _binding = null
    }

    companion object {
        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): StoriesGroupFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG_FRAGMENT_STORIES_GROUP) as? StoriesGroupFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesGroupFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoriesGroupFragment
        }
    }
}
