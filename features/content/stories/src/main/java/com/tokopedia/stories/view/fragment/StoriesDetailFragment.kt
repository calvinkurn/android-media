package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler.ImageLoaderStateListener
import com.tokopedia.content.common.util.withCache
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.analytic.StoriesAnalytics
import com.tokopedia.stories.analytic.StoriesEEModel
import com.tokopedia.stories.bottomsheet.StoriesThreeDotsBottomSheet
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.IMAGE
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.VIDEO
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.STORIES_GROUP_ID
import com.tokopedia.stories.view.utils.TAG_FRAGMENT_STORIES_DETAIL
import com.tokopedia.stories.view.utils.TouchEventStories
import com.tokopedia.stories.view.utils.isNetworkError
import com.tokopedia.stories.view.utils.loadImage
import com.tokopedia.stories.view.utils.onTouchEventStories
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ContentIsLoaded
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.NextDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PreviousDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ResumeStories
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType
import com.tokopedia.stories.view.viewmodel.state.isAnyShown
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesDetailFragment @Inject constructor(
    private val analyticFactory: StoriesAnalytics.Factory,
) : TkpdBaseV4Fragment() {

    private val mParentPage: StoriesGroupFragment
        get() = (requireParentFragment() as StoriesGroupFragment)

    private var _binding: FragmentStoriesDetailBinding? = null
    private val binding: FragmentStoriesDetailBinding
        get() = _binding!!

    val viewModelProvider by lazyThreadSafetyNone { mParentPage.viewModelProvider }

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelProvider }

    private val analytic: StoriesAnalytics get() = analyticFactory.create(mParentPage.authorId)

    private val mAdapter: StoriesGroupAdapter by lazyThreadSafetyNone {
        StoriesGroupAdapter(object : StoriesGroupAdapter.Listener {
            override fun onClickGroup(position: Int, data: StoriesGroupHeader) {
                trackClickGroup(position, data)
                viewModelAction(StoriesUiAction.SelectGroup(position, false))
            }

            override fun onGroupImpressed(data: StoriesGroupHeader) {
                viewModelAction(StoriesUiAction.CollectImpressedGroup(data))
            }
        })
    }

    private val mLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private val groupId: String
        get() = arguments?.getString(STORIES_GROUP_ID).orEmpty()

    private val isEligiblePage: Boolean
        get() = groupId == viewModel.mGroup.groupId

    override fun getScreenName(): String {
        return TAG_FRAGMENT_STORIES_DETAIL
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
            viewModel.storiesState.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroupHeader(prevState?.storiesMainData, state.storiesMainData)
                renderStoriesDetail(
                    prevState?.storiesMainData?.groupItems?.get(prevState.storiesMainData.selectedGroupPosition.orZero())?.detail,
                    state.storiesMainData.groupItems[state.storiesMainData.selectedGroupPosition].detail,
                )
                observeBottomSheetStatus(prevState?.bottomSheetStatus, state.bottomSheetStatus)
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesEvent.collect { event ->
                if (!isEligiblePage) return@collect
                when (event) {
                    is StoriesUiEvent.EmptyDetailPage -> {
                        // TODO handle empty data here
                        showToast("data stories $groupId is empty")
                        showPageLoading(false)
                    }

                    is StoriesUiEvent.ErrorDetailPage -> {
                        if (event.throwable.isNetworkError) {
                            // TODO handle error network here
                            showToast("error detail network ${event.throwable}")
                        } else {
                            // TODO handle error fetch here
                            showToast("error detail content ${event.throwable}")
                        }
                        showPageLoading(false)
                    }

                    StoriesUiEvent.OpenKebab -> {
                        if (groupId != viewModel.mGroup.groupId) return@collect
                        StoriesThreeDotsBottomSheet
                            .getOrCreateFragment(
                                childFragmentManager,
                                requireActivity().classLoader
                            )
                            .show(childFragmentManager)
                    }

                    is StoriesUiEvent.ShowErrorEvent -> showToaster(
                        message = event.message.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )

                    else -> return@collect
                }
            }
        }
    }

    private fun renderStoriesGroupHeader(
        prevState: StoriesUiModel?,
        state: StoriesUiModel,
    ) {
        if (prevState?.groupHeader == state.groupHeader ||
            groupId != state.selectedGroupId
        ) return

        mAdapter.setItems(state.groupHeader)
        mAdapter.notifyItemRangeInserted(mAdapter.itemCount, state.groupHeader.size)
        binding.layoutDetailLoading.categoriesLoader.hide()
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetail?,
        state: StoriesDetail
    ) {
        if (prevState == state ||
            state == StoriesDetail() ||
            state.selectedGroupId != groupId
        ) return

        val currentItem = state.detailItems[state.selectedDetailPosition]

        storiesDetailsTimer(state)
        renderAuthor(currentItem)

        val currContent = state.detailItems.getOrNull(state.selectedDetailPosition)
        if (currContent?.isSameContent == true || currContent == null) return

        when (currContent.content.type) {
            IMAGE -> {
                binding.layoutStoriesContent.ivStoriesDetailContent.apply {
                    loadImage(
                        currContent.content.data,
                        listener = object : ImageLoaderStateListener {
                            override fun successLoad() {
                                contentIsLoaded()
                                analytic.sendImpressionStoriesContent(currContent.id)
                            }

                            override fun failedLoad() {
                                // TODO add some action when fail load image?
                            }
                        })
                }
            }

            VIDEO -> {
                // TODO handle video content here
            }
        }

        showPageLoading(false)
        binding.vStoriesKebabIcon.showWithCondition(currContent.menus.isNotEmpty())
    }

    private fun observeBottomSheetStatus(
        prevState: Map<BottomSheetType, Boolean>?,
        state: Map<BottomSheetType, Boolean>,
    ) {
        if (prevState == state) return
        if (state.isAnyShown.orFalse()) pauseStories() else resumeStories()
        showPageLoading(false)
    }

    private fun storiesDetailsTimer(state: StoriesDetail) {
        with(binding.cvStoriesDetailTimer) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesDetailTimer(
                        currentPosition = state.selectedDetailPosition,
                        itemCount = state.detailItems.size,
                        data = state.detailItems[state.selectedDetailPosition],
                    ) { if (isEligiblePage) viewModelAction(NextDetail) }
                }
            }
        }
    }

    private fun renderAuthor(state: StoriesDetailItem) = with(binding.vStoriesPartner) {
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

                TouchEventStories.NEXT_PREV -> {
                    trackTapPreviousDetail()
                    viewModelAction(PreviousDetail)
                }
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

                TouchEventStories.NEXT_PREV -> {
                    trackTapNextDetail()
                    viewModelAction(NextDetail)
                }
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

    private fun showPageLoading(isShowLoading: Boolean) = with(binding) {
        rvStoriesCategory.showWithCondition(!isShowLoading)
        layoutStoriesContent.container.showWithCondition(!isShowLoading)
        layoutDetailLoading.container.showWithCondition(isShowLoading)
    }

    private fun trackClickGroup(position: Int, data: StoriesGroupHeader) {
        analytic.sendClickStoryCircleEvent(
            entryPoint = mParentPage.entryPoint,
            currentCircle = data.groupName,
            promotions = listOf(
                StoriesEEModel(
                    creativeName = "",
                    creativeSlot = position.plus(1).toString(),
                    itemId = "${data.groupId} - ${data.groupName} - ${mParentPage.authorId}",
                    itemName = "/ - stories"
                ),
            ),
        )
    }

    private fun trackImpressionDetail(storiesId: String) {
        analytic.sendImpressionStoriesContent(storiesId)
    }

    private fun trackTapPreviousDetail() {
        analytic.sendClickTapPreviousContentEvent(
            entryPoint = mParentPage.entryPoint,
            storiesId = viewModel.mDetail.id,
            creatorType = "asgc",
            contentType = viewModel.mDetail.content.type.value,
            currentCircle = viewModel.mGroup.groupName,
        )
    }

    private fun trackTapNextDetail() {
        analytic.sendClickTapNextContentEvent(
            entryPoint = mParentPage.entryPoint,
            storiesId = viewModel.mDetail.id,
            creatorType = "asgc",
            contentType = viewModel.mDetail.content.type.value,
            currentCircle = viewModel.mGroup.groupName,
        )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesDetailFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG_FRAGMENT_STORIES_DETAIL) as? StoriesDetailFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesDetailFragment::class.java.name
            ) as StoriesDetailFragment
        }
    }
}
