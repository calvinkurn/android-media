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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.databinding.FragmentStoryDetailBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoryGroupAdapter
import com.tokopedia.stories.view.components.indicator.StoryDetailTimer
import com.tokopedia.stories.view.model.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryGroupUiModel
import com.tokopedia.stories.view.utils.TouchEventStory
import com.tokopedia.stories.view.utils.onTouchEventStory
import com.tokopedia.stories.view.viewmodel.StoryViewModel
import com.tokopedia.stories.view.viewmodel.action.StoryUiAction
import com.tokopedia.stories.view.viewmodel.action.StoryUiAction.NextDetail
import com.tokopedia.stories.view.viewmodel.action.StoryUiAction.PauseStory
import com.tokopedia.stories.view.viewmodel.action.StoryUiAction.PreviousDetail
import com.tokopedia.stories.view.viewmodel.action.StoryUiAction.ResumeStory
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoryDetailFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoryDetailBinding? = null
    private val binding: FragmentStoryDetailBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoryViewModel> { viewModelFactory }

    private val mAdapter: StoryGroupAdapter by lazyThreadSafetyNone {
        StoryGroupAdapter(object : StoryGroupAdapter.Listener {
            override fun onClickGroup(position: Int) {
                viewModelAction(StoryUiAction.SetGroup(position))
            }
        })
    }

    private val mLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStoryView()
    }

    override fun onPause() {
        super.onPause()
        pauseStory()
    }

    override fun onResume() {
        super.onResume()
        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderStoryGroup(prevState?.storyGroup, state.storyGroup)
                renderStoryDetail(prevState?.storyDetail, state.storyDetail)
            }
        }
    }

    private fun renderStoryGroup(
        prevState: StoryGroupUiModel?,
        state: StoryGroupUiModel,
    ) {
        if (prevState == state) return
        mAdapter.setItems(state.groupItems)
    }

    private fun renderStoryDetail(
        prevState: StoryDetailUiModel?,
        state: StoryDetailUiModel,
    ) {
        if (prevState == state || state == StoryDetailUiModel()) return

        storyDetailsTimer(state)

        val prevContent = prevState?.detailItems
        val currContent = state.detailItems[state.selectedPosition]
        if (!prevContent.isNullOrEmpty() &&
            prevContent[prevState.selectedPosition].imageContent == currContent.imageContent
        ) return

        binding.ivStoryDetailContent.apply {
            setImageUrl(currContent.imageContent)
            onUrlLoaded = {
                viewModelAction(ResumeStory)
            }
        }
        showStoryComponent(true)
    }

    private fun storyDetailsTimer(state: StoryDetailUiModel) {
        with(binding.cvStoryDetailTimer) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoryDetailTimer(
                        currentPosition = state.selectedPosition,
                        itemCount = state.detailItems.size,
                        data = state.detailItems[state.selectedPosition],
                    ) { viewModelAction(NextDetail) }
                }
            }
        }
    }

    private fun setupStoryView() = with(binding) {
        binding.icClose.hide()
        icClose.setOnClickListener {
            activity?.finish()
        }

        rvStoryCategory.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        flStoryPrev.onTouchEventStory { event ->
            when (event) {
                TouchEventStory.PAUSE -> {
                    flStoryNext.hide()
                    flStoryProduct.hide()
                    showStoryComponent(false)
                    pauseStory()
                }

                TouchEventStory.RESUME -> {
                    flStoryNext.show()
                    flStoryProduct.show()
                    showStoryComponent(true)
                    resumeStory()
                }

                TouchEventStory.NEXT_PREV -> viewModelAction(PreviousDetail)
            }
        }
        flStoryNext.onTouchEventStory { event ->
            when (event) {
                TouchEventStory.PAUSE -> {
                    flStoryPrev.hide()
                    flStoryProduct.hide()
                    showStoryComponent(false)
                    pauseStory()
                }

                TouchEventStory.RESUME -> {
                    flStoryPrev.show()
                    flStoryProduct.show()
                    showStoryComponent(true)
                    resumeStory()
                }

                TouchEventStory.NEXT_PREV -> viewModelAction(NextDetail)
            }
        }
        flStoryProduct.setOnClickListener {
            showToast("show product bottom sheet")
            // pause story when product bottom sheet shown
            // and resume when product bottom sheet dismissed
            // pause -> viewModelAction(StoryUiAction.PauseStory)
            // resume -> viewModelAction(StoryUiAction.ResumeStory)
        }
    }

    private fun pauseStory() {
        viewModelAction(PauseStory)
    }

    private fun resumeStory() {
        viewModelAction(ResumeStory)
    }

    private fun showStoryComponent(isShow: Boolean) {
        binding.storyComponent.showWithCondition(isShow)
    }

    private fun viewModelAction(event: StoryUiAction) {
        viewModel.submitAction(event)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "StoryDetailFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): StoryDetailFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoryDetailFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoryDetailFragment::class.java.name
            ) as StoryDetailFragment
        }
    }

}
