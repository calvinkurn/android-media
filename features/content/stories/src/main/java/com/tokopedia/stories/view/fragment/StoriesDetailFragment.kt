package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
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
                viewModelAction(StoriesUiAction.SetGroup(position, false))
            }
        })
    }

    private val mLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private val groupId: String
        get() = arguments?.getString(STORY_GROUP_ID).orEmpty()

    override fun getScreenName(): String {
        return TAG
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
            }
        }
    }

    private fun setupUiEventObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is StoriesUiEvent.ErrorDetailPage -> {
                        if (viewModel.mGroupId != groupId) return@collect
                        if (event.throwable.isNetworkError) {
                            // TODO handle error network here
                            showToast("error detail network ${event.throwable}")
                        }
                        else {
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
            state.selectedGroupId != groupId
        ) return

        if (state.detailItems.isEmpty()) {
            // TODO handle error empty data state here
            Toast.makeText(
                requireContext(),
                "Don't worry this is debug: ask BE team why data stories $groupId is empty :)"
                , Toast.LENGTH_LONG
            ).show()
            return
        }

        storiesDetailsTimer(state)

        val currContent = state.detailItems[state.selectedDetailPosition]
        if (currContent.isSameContent) return

        showPageLoading(false)

        binding.ivStoriesDetailContent.apply {
            setImageUrl(currContent.imageContent)
            onUrlLoaded = {
                contentIsLoaded()
            }
        }
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
        rvStoriesCategory.showWithCondition(!isShowLoading)
        ivStoriesDetailContent.showWithCondition(!isShowLoading)
        layoutDetailLoading.container.showWithCondition(isShowLoading)
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
