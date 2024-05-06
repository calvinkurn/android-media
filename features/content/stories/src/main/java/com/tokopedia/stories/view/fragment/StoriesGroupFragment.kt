package com.tokopedia.stories.view.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.withCache
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.stories.R
import com.tokopedia.stories.analytics.StoriesAnalytics
import com.tokopedia.stories.analytics.StoriesEEModel
import com.tokopedia.stories.databinding.FragmentStoriesGroupBinding
import com.tokopedia.stories.domain.model.StoriesSource
import com.tokopedia.stories.view.adapter.StoriesGroupPagerAdapter
import com.tokopedia.stories.view.animation.StoriesPageAnimation
import com.tokopedia.stories.view.custom.StoriesErrorView
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.KEY_ARGS
import com.tokopedia.stories.view.utils.TAG_FRAGMENT_STORIES_GROUP
import com.tokopedia.stories.view.utils.isNetworkError
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModelFactory
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.SetMainData
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class StoriesGroupFragment @Inject constructor(
    private val viewModelFactory: StoriesViewModelFactory.Creator,
    private val analyticFactory: StoriesAnalytics.Factory
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

    val viewModelProvider get() = viewModelFactory.create(args)

    private val analytic: StoriesAnalytics get() = analyticFactory.create(args)

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelProvider }

    private var mTrackGroupChanged = false

    private val pagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModelAction(SetMainData(position))
            trackMoveGroup()
            showSelectedGroupHighlight(position)
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

        setupTopInsets()

        layoutGroupLoading.icCloseLoading.setOnClickListener { activity?.finish() }
        storiesGroupViewPager.setPageTransformer(StoriesPageAnimation())
        storiesGroupViewPager.registerOnPageChangeCallback(pagerListener)

        binding.vStoriesOnboarding.setOnClickListener {
            dismissOnboard()
        }
        binding.vStoriesOnboarding.findViewById<IconUnify>(R.id.iv_onboard_close).setOnClickListener {
            dismissOnboard()
        }
    }

    private fun dismissOnboard() {
        viewModelAction(StoriesUiAction.ResumeStories())
        binding.vStoriesOnboarding.gone()
    }

    private fun setupTopInsets() = with(binding) {
        root.doOnApplyWindowInsets { _, insets, _, _ ->
            val topInsetsMargin = (insets.getInsets(WindowInsetsCompat.Type.statusBars())).top
            root.setMargin(top = topInsetsMargin, bottom = 0, left = 0, right = 0)
        }
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun showSelectedGroupHighlight(position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (args.source != StoriesSource.BROWSE_WIDGET.value) {
                binding.tvHighlight.text = pagerAdapter.getCurrentPageGroupName(position)
                binding.tvHighlight.show()
                binding.tvHighlight.animate().alpha(1f)
                delay(1.seconds)
                binding.tvHighlight.animate().alpha(0f)
            } else {
                delay(DELAY_PREVENT_STORIES_STUCK_RACE_CONDITION)
            }
            viewModelAction(StoriesUiAction.PageIsSelected)
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
                    is StoriesUiEvent.SelectGroup -> selectGroupPosition(
                        event.position,
                        event.showAnimation
                    )

                    is StoriesUiEvent.ErrorGroupPage -> {
                        showPageLoading(false)
                        setErrorType(if (event.throwable.isNetworkError) StoriesErrorView.Type.NoInternet else StoriesErrorView.Type.FailedLoad) { event.onClick() }
                    }

                    StoriesUiEvent.EmptyGroupPage -> {
                        setErrorType(StoriesErrorView.Type.EmptyStories)
                        showPageLoading(false)
                    }

                    StoriesUiEvent.FinishedAllStories -> activity?.finish()
                    is StoriesUiEvent.OnboardShown -> {
                        binding.vStoriesOnboarding.show()
                        binding.vStoriesOnboarding.checkAnim()
                    }
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
        ) {
            return
        }

        hideError()

        binding.storiesGroupViewPager.adapter = pagerAdapter
        pagerAdapter.setStoriesGroup(state)
        selectGroupPosition(state.selectedGroupPosition, false)

        showPageLoading(false)
    }

    private fun selectGroupPosition(position: Int, showAnimation: Boolean) =
        with(binding.storiesGroupViewPager) {
            if (position < 0) return@with

            setCurrentItem(position, showAnimation)
        }

    private fun showPageLoading(isShowLoading: Boolean) = with(binding) {
        layoutGroupLoading.container.showWithCondition(isShowLoading)
        storiesGroupViewPager.showWithCondition(!isShowLoading)
    }

    private fun setErrorType(errorType: StoriesErrorView.Type, onClick: () -> Unit = {}) =
        with(binding.vStoriesError) {
            show()
            type = errorType
            setAction { onClick() }
            setCloseAction { activity?.finish() }
        }

    private fun hideError() = binding.vStoriesError.gone()

    private fun trackImpressionGroup() {
        analytic.sendViewStoryCircleEvent(
            currentCircle = viewModel.mGroup.groupName,
            promotions = viewModel.impressedGroupHeader.mapIndexed { index, storiesGroupHeader ->
                StoriesEEModel(
                    creativeName = "",
                    creativeSlot = index.plus(1).toString(),
                    itemId = "${viewModel.mGroup.groupId} - ${storiesGroupHeader.groupId} - ${viewModel.validAuthorId}",
                    itemName = "/ - stories"
                )
            }
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
            contentType = viewModel.mDetail.content.type,
            storyType = viewModel.mDetail.storyType,
            currentCircle = viewModel.mGroup.groupName
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackExitRoom()
        binding.storiesGroupViewPager.unregisterOnPageChangeCallback(pagerListener)
        _binding = null
    }

    companion object {

        private const val DELAY_PREVENT_STORIES_STUCK_RACE_CONDITION = 800L

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): StoriesGroupFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG_FRAGMENT_STORIES_GROUP) as? StoriesGroupFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesGroupFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoriesGroupFragment
        }
    }
}
