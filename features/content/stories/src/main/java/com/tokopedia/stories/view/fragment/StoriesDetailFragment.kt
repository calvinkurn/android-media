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
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.bottomsheet.StoriesProductBottomSheet
import com.tokopedia.stories.bottomsheet.StoriesThreeDotsBottomSheet
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.animation.StoriesProductNotch
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.model.isAnyShown
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID
import com.tokopedia.stories.view.utils.TouchEventStories
import com.tokopedia.stories.view.utils.onTouchEventStories
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.NextDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PreviousDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ResumeStories
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesDetailFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val router: Router,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesDetailBinding? = null
    private val binding: FragmentStoriesDetailBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

    private val mAdapter: StoriesGroupAdapter by lazyThreadSafetyNone {
        StoriesGroupAdapter(object : StoriesGroupAdapter.Listener {
            override fun onClickGroup(position: Int) {
                viewModelAction(StoriesUiAction.SetGroupMainData(position))
            }
        })
    }

    private val mLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private val groupId: String
        get() = arguments?.getString(STORIES_GROUP_ID).orEmpty()

    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is StoriesThreeDotsBottomSheet -> {}
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStoriesView()
        observeEvent()
    }

    override fun onResume() {
        super.onResume()
        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroup(prevState?.storiesGroup, state.storiesGroup)
                renderStoriesDetail(prevState?.storiesDetail, state.storiesDetail)
                observeBottomSheetStatus(prevState?.bottomSheetStatus, state.bottomSheetStatus)
            }
        }
    }

    private fun renderStoriesGroup(
        prevState: List<StoriesGroupUiModel>?,
        state: List<StoriesGroupUiModel>,
    ) {
        if (prevState == state) return
        mAdapter.setItems(state)
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetailUiModel?,
        state: StoriesDetailUiModel,
    ) {
        if (prevState == state || state == StoriesDetailUiModel.Empty) return

        storiesDetailsTimer(state)
        binding.ivStoriesDetailContent.setImageUrl(state.imageContent)
        binding.vStoriesProductIcon.tvPlayProductCount.text = state.productCount.toString() //TODO map as string
        renderAuthor(state)
        renderNotch(state)
    }

    private fun observeBottomSheetStatus(
        prevState: Map<BottomSheetType, Boolean>?,
        state: Map<BottomSheetType, Boolean>,
    ) {
        if (prevState == state) return
        if (state.isAnyShown.orFalse()) pauseStories() else resumeStories()
    }

    private fun storiesDetailsTimer(state: StoriesDetailUiModel) {
        with(binding.cvStoriesDetailTimer) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesDetailTimer(
                        itemCount = viewModel.mDetailMaxInGroup,
                        data = state,
                    ) { viewModelAction(NextDetail) }
                }
            }
        }
        isShouldShowStoriesComponent(true)
    }

    private fun renderAuthor(state: StoriesDetailUiModel) = with(binding.vStoriesPartner) {
        tvPartnerName.text = state.author.name
        ivIcon.setImageUrl(state.author.thumbnailUrl)
        btnFollow.setOnClickListener {
            //TODO(): Follow
        }
        if (state.author is StoryAuthor.Shop)
            ivBadge.setImageUrl(state.author.badgeUrl)
    }


    private fun setupStoriesView() = with(binding) {
        binding.icClose.hide()
        icClose.setOnClickListener {
            activity?.finish()
        }

        rvStoriesCategory.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        flStoriesPrev.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> pauseStories()

                TouchEventStories.RESUME -> resumeStories()

                TouchEventStories.NEXT_PREV -> viewModelAction(PreviousDetail)
                else -> {}
            }
        }
        flStoriesNext.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> pauseStories()

                TouchEventStories.RESUME -> resumeStories()

                TouchEventStories.NEXT_PREV -> viewModelAction(NextDetail)

                TouchEventStories.SWIPE_UP -> {
                    if (groupId != viewModel.groupId) return@onTouchEventStories
                    viewModelAction(StoriesUiAction.OpenProduct)
                }
                else -> {}
            }
        }
        flStoriesProduct.setOnClickListener {
            showToast("show product bottom sheet")
            // pause stories when product bottom sheet shown
            // and resume when product bottom sheet dismissed
            // pause -> viewModelAction(StoriesUiAction.PauseStories)
            // resume -> viewModelAction(StoriesUiAction.ResumeStories)
        }
        vStoriesKebabIcon.setOnClickListener {
            viewModelAction(StoriesUiAction.OpenKebabMenu)
        }
        vStoriesProductIcon.root.setOnClickListener {
            viewModelAction(StoriesUiAction.OpenProduct)
        }
    }

    private fun renderNotch(state: StoriesDetailUiModel) {
        with(binding.notchStoriesProduct) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesProductNotch(state.productCount) {
                        viewModelAction(StoriesUiAction.OpenProduct)
                    }
                }
            }
        }
    }

    private fun pauseStories() {
        isShouldShowStoriesComponent(false)
        viewModelAction(PauseStories)
    }

    private fun resumeStories() {
        isShouldShowStoriesComponent(true)
        viewModelAction(ResumeStories)
    }

    private fun isShouldShowStoriesComponent(isShow: Boolean) {
        binding.icClose.showWithCondition(isShow)
        binding.rvStoriesCategory.showWithCondition(isShow)
        binding.cvStoriesDetailTimer.showWithCondition(isShow)
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    StoriesUiEvent.OpenKebab -> {
                        if (groupId != viewModel.groupId) return@collectLatest
                        StoriesThreeDotsBottomSheet
                            .getOrCreateFragment(
                                childFragmentManager,
                                requireActivity().classLoader
                            )
                            .show(childFragmentManager)
                    }

                    StoriesUiEvent.OpenProduct -> {
                        if (groupId != viewModel.groupId) return@collectLatest

                        StoriesProductBottomSheet.getOrCreateFragment(
                            childFragmentManager,
                            requireActivity().classLoader
                        )
                            .show(childFragmentManager)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun goTo(appLink: String) {
        router.route(requireContext(), appLink)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "StoriesDetailFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): StoriesDetailFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesDetailFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesDetailFragment::class.java.name
            ) as StoriesDetailFragment
        }
    }

}
