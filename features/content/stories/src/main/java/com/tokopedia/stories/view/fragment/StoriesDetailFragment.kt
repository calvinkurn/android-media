package com.tokopedia.stories.view.fragment

import  android.os.Bundle
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
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.bottomsheet.StoriesThreeDotsBottomSheet
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.StoriesFailedLoad
import com.tokopedia.stories.view.StoriesUnavailable
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.model.isAnyShown
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID
import com.tokopedia.stories.view.utils.TouchEventStories
import com.tokopedia.stories.view.utils.isNetworkError
import com.tokopedia.stories.view.utils.onTouchEventStories
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ContentIsLoaded
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.NextDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PreviousDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ResumeStories
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesDetailFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesDetailBinding? = null
    private val binding: FragmentStoriesDetailBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

    private val mAdapter: StoriesGroupAdapter by lazyThreadSafetyNone {
        StoriesGroupAdapter(object : StoriesGroupAdapter.Listener {
            override fun onClickGroup(position: Int) {
                viewModelAction(StoriesUiAction.SetGroup(position))
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
        setupObserver()
    }

    private fun setupObserver() {
        setupUiStateObserver()
        setupUiEventObserver()
    }

    private fun setupUiStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroupHeader(prevState?.storiesGroup, state.storiesGroup)
                renderStoriesDetail(prevState?.storiesDetail, state.storiesDetail)
                observeBottomSheetStatus(prevState?.bottomSheetStatus, state.bottomSheetStatus)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    StoriesUiEvent.OpenKebab -> {
                        if (groupId != viewModel.mGroupId) return@collectLatest
                        StoriesThreeDotsBottomSheet
                            .getOrCreateFragment(
                                childFragmentManager,
                                requireActivity().classLoader
                            )
                            .show(childFragmentManager)
                    }
                    is StoriesUiEvent.ShowErrorEvent -> showToaster(message = event.message.message.orEmpty(), type = Toaster.TYPE_ERROR)
                    is StoriesUiEvent.ErrorDetailPage -> {
                        if (viewModel.mGroupId != groupId) return@collectLatest
                        if (event.throwable.isNetworkError) setNoInternet(true)
                        else setNoContent(true)
                        showPageLoading(false)
                    }
                    else -> return@collectLatest
                }
            }
        }
    }

    private fun renderStoriesGroupHeader(
        prevState: StoriesGroupUiModel?,
        state: StoriesGroupUiModel,
    ) {
        if (prevState?.groupHeader == state.groupHeader ||
            groupId != state.selectedGroupId
        ) return

        mAdapter.setItems(state.groupHeader)
        mAdapter.notifyItemRangeInserted(mAdapter.itemCount, state.groupHeader.size)
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetailUiModel?,
        state: StoriesDetailUiModel
    ) {
        if (prevState == state ||
            state == StoriesDetailUiModel() ||
            state.detailItems.isEmpty() ||
            state.selectedGroupId != groupId || state.selectedDetailPosition < 0 || state.selectedDetailPositionCached < 0
        ) return

        setNoInternet(false)
        setNoContent(false)

        val currentItem = state.detailItems[state.selectedDetailPosition]

        storiesDetailsTimer(state)
        renderAuthor(currentItem)

        val currContent = state.detailItems.getOrNull(state.selectedDetailPosition)
        if (currContent?.isSameContent == true || currContent == null) return

        showPageLoading(false)

        binding.ivStoriesDetailContent.apply {
            setImageUrl(currContent.imageContent)
            onUrlLoaded = {
                showStoriesComponent(true)
                contentIsLoaded()
            }
        }
        binding.vStoriesKebabIcon.showWithCondition(currContent.menus.isNotEmpty())
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
                        currentPosition = state.selectedDetailPosition,
                        itemCount = state.detailItems.size,
                        data = state.detailItems[state.selectedDetailPosition],
                    ) {
                        /** TODO
                         * all state already updated using the new groupId
                         * but the oldest data that we left (ui action swipe) still there
                         * need other way to stop the oldest data if the position is invalid
                         * it also causing broken timer experience when (ui action swipe)
                         * invalid -> groupId != viewModel.mGroupId
                         **/
                        if (groupId != viewModel.mGroupId) return@StoriesDetailTimer
                        viewModelAction(NextDetail)
                    }
                }
            }
        }
    }

    private fun renderAuthor(state: StoriesDetailItemUiModel) = with(binding.vStoriesPartner) {
        tvPartnerName.text = state.author.name
        ivIcon.setImageUrl(state.author.thumbnailUrl)
        btnFollow.gone()
        if (state.author is StoryAuthor.Shop) ivBadge.setImageUrl(state.author.badgeUrl)
    }


    private fun setupStoriesView() = with(binding) {
        showPageLoading(true)

        icClose.setOnClickListener { activity?.finish() }

        rvStoriesCategory.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        flStoriesPrev.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> {
                    flStoriesNext.hide()
                    flStoriesProduct.hide()
                    showStoriesComponent(false)
                    pauseStories()
                }

                TouchEventStories.RESUME -> {
                    flStoriesNext.show()
                    flStoriesProduct.show()
                    showStoriesComponent(true)
                    resumeStories()
                }

                TouchEventStories.NEXT_PREV -> viewModelAction(PreviousDetail)
            }
        }
        flStoriesNext.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> {
                    flStoriesPrev.hide()
                    flStoriesProduct.hide()
                    showStoriesComponent(false)
                    pauseStories()
                }

                TouchEventStories.RESUME -> {
                    flStoriesPrev.show()
                    flStoriesProduct.show()
                    showStoriesComponent(true)
                    resumeStories()
                }

                TouchEventStories.NEXT_PREV -> viewModelAction(NextDetail)
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
    }

    private fun pauseStories() {
        viewModelAction(PauseStories)
    }

    private fun resumeStories() {
        viewModelAction(ResumeStories)
    }

    private fun contentIsLoaded() {
        viewModelAction(ContentIsLoaded)
    }

    private fun showStoriesComponent(isShow: Boolean) {
        binding.storiesComponent.showWithCondition(isShow)
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
    }

    private fun showPageLoading(isShowLoading: Boolean) = with(binding){
        layoutTimer.llTimer.showWithCondition(isShowLoading)
        layoutDeclarative.loaderDecorativeWhite.showWithCondition(isShowLoading)
        ivStoriesDetailContent.showWithCondition(!isShowLoading)
    }

    private fun showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        clickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        Toaster.build(
            requireView(),
            message,
            type = type,
            actionText = actionText,
            clickListener = clickListener
        ).show()
    }

    private fun setNoInternet(isShow: Boolean) = with(binding.vStoriesNoInet) {
        apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StoriesFailedLoad (onRetryClicked = {
                    viewModelAction(StoriesUiAction.SetArgumentsData(arguments)) //Should be refreshing~
                })
            }
        }
        showWithCondition(isShow)
    }

    private fun setNoContent(isShow: Boolean) = with(binding.vStoriesNoInet) {
        apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StoriesUnavailable (onRetryClicked = {
                    viewModelAction(StoriesUiAction.SetArgumentsData(arguments)) //Should be refreshing~
                })
            }
        }
        showWithCondition(isShow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "StoriesDetailFragment"
        const val STORY_GROUP_ID = "StoriesGroupId"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesDetailFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesDetailFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesDetailFragment::class.java.name
            ) as StoriesDetailFragment
        }
    }
}
