package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.content.common.util.withCache
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.analytic.StoriesAnalytic
import com.tokopedia.stories.analytic.StoriesEEModel
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesItemContentType.IMAGE
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesItemContentType.VIDEO
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.STORY_GROUP_ID
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
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesDetailFragment @Inject constructor(
    private val analytic: StoriesAnalytic,
) : TkpdBaseV4Fragment() {

    private val mParentPage: StoriesGroupFragment
        get() = (requireParentFragment() as StoriesGroupFragment)

    private var _binding: FragmentStoriesDetailBinding? = null
    private val binding: FragmentStoriesDetailBinding
        get() = _binding!!

    val viewModelProvider by lazyThreadSafetyNone { mParentPage.viewModelProvider }

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelProvider }

    private val mAdapter: StoriesGroupAdapter by lazyThreadSafetyNone {
        StoriesGroupAdapter(object : StoriesGroupAdapter.Listener {
            override fun onClickGroup(position: Int) {
                viewModelAction(StoriesUiAction.SelectGroup(position, false))
            }
        })
    }

    private val mLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private val groupId: String
        get() = arguments?.getString(STORY_GROUP_ID).orEmpty()

    private val isEligiblePage: Boolean
        get() = groupId == viewModel.mGroupId

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
            viewModel.storiesMainData.withCache().collectLatest { (prevState, state) ->
                renderStoriesGroupHeader(prevState, state)
                renderStoriesDetail(
                    prevState?.groupItems?.get(prevState.selectedGroupPosition)?.detail,
                    state.groupItems[state.selectedGroupPosition].detail,
                )
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.storiesEvent.collect { event ->
                when (event) {
                    is StoriesUiEvent.ErrorDetailPage -> {
                        if (!isEligiblePage) return@collect
                        if (event.throwable.isNetworkError) {
                            // TODO handle error network here
                            showToast("error detail network ${event.throwable}")
                        } else {
                            // TODO handle error fetch here
                            showToast("error detail content ${event.throwable}")
                        }
                        showPageLoading(false)
                    }

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

        val templateTracker = state.groupItems[state.selectedGroupPosition].detail.detailItems[
            state.groupItems[state.selectedGroupPosition].detail.selectedDetailPosition].meta
            .templateTracker


//        TODO on click group
//        analytic.sendClickStoryCircleEvent(
//            entryPoint = mParentPage.entryPoint,
//            storiesId = "stories id",
//            partnerId = mParentPage.authorId,
//            creatorType = "asgc",
//            contentType = "image",
//            currentCircle = state.groupHeader[state.selectedGroupPosition].title,
//            templateTracker = templateTracker,
//            promotions = state.groupHeader.mapIndexed { index, storiesGroupHeader ->
//                StoriesEEModel(
//                    creativeName = storiesGroupHeader.title,
//                    creativeSlot = index.plus(1).toString(),
//                    itemId = "${state.groupHeader[state.selectedGroupPosition].title} - storycat impressed - ${mParentPage.authorId}",
//                    itemName = "/ - $templateTracker - stories"
//                )
//            },
//        )

        analytic.sendViewStoryCircleEvent(
            entryPoint = mParentPage.entryPoint,
            storiesId = "stories id",
            partnerId = mParentPage.authorId,
            creatorType = "asgc",
            contentType = "image",
            currentCircle = state.groupHeader[state.selectedGroupPosition].title,
            templateTracker = templateTracker,
            promotions = state.groupHeader.mapIndexed { index, storiesGroupHeader ->
                StoriesEEModel(
                    creativeName = storiesGroupHeader.title,
                    creativeSlot = index.plus(1).toString(),
                    itemId = "${state.groupHeader[state.selectedGroupPosition].title} - storycat impressed - ${mParentPage.authorId}",
                    itemName = "/ - $templateTracker - stories"
                )
            },
        )
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetailUiModel?,
        state: StoriesDetailUiModel
    ) {
        if (prevState == state ||
            state == StoriesDetailUiModel() ||
            state.selectedGroupId != groupId
        ) return

        if (state.detailItems.isEmpty()) {
            // TODO handle error empty data state here
            Toast.makeText(
                requireContext(),
                "data stories $groupId is empty",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        storiesDetailsTimer(state)

        val currContent = state.detailItems[state.selectedDetailPosition]
        if (currContent.isSameContent) return

        when (currContent.content.type) {
            IMAGE -> {
                binding.layoutStoriesContent.ivStoriesDetailContent.apply {
                    setImageUrl(currContent.content.data)
                    onUrlLoaded = {
                        contentIsLoaded()
                        analytic.sendImpressionStoriesContent(currContent.id, mParentPage.authorId)
                    }
                }
            }

            VIDEO -> {
                // TODO handle video content here
            }
        }

        showPageLoading(false)
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
                    ) { if (isEligiblePage) viewModelAction(NextDetail) }
                }
            }
        }
    }

    private fun setupStoriesView() = with(binding) {
        showPageLoading(true)

        icClose.setOnClickListener {
            analytic.sendClickExitStoryRoomEvent(
                eventLabel = "${mParentPage.entryPoint} - ${mParentPage.authorId} - storiesId - asgc - image - nextStoriesId - currentCircle"
            )
            activity?.finish()
        }

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
                    analytic.sendClickTapPreviousContentEvent(
                        eventLabel = "${mParentPage.entryPoint} - ${mParentPage.authorId} - storiesId - asgc - image - currentCircle - nextStoriesId"
                    )
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
                    analytic.sendClickTapNextContentEvent(
                        eventLabel = "${mParentPage.entryPoint} - ${mParentPage.authorId} - storiesId - asgc - image - currentCircle - nextStoriesId"
                    )
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
