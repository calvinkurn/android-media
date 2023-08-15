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
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.stories.databinding.FragmentStoriesDetailBinding
import com.tokopedia.stories.utils.withCache
import com.tokopedia.stories.view.adapter.StoriesGroupAdapter
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimer
import com.tokopedia.stories.view.components.indicator.StoriesDetailTimerEvent.NEXT
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.utils.TouchEventStories
import com.tokopedia.stories.view.utils.onTouchEventStories
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.NextDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PauseStories
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.PreviousDetail
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction.ResumeStories
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StoriesDetailFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
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
            }
        }
    }

    private fun renderStoriesGroup(
        prevState: List<StoriesGroupUiModel>?,
        state: List<StoriesGroupUiModel>,
    ) {
        if (prevState == state) return
        mAdapter.setItems(state)
        binding.icClose.show()
    }

    private fun renderStoriesDetail(
        prevState: StoriesDetailUiModel?,
        state: StoriesDetailUiModel,
    ) {
        if (prevState == state || state == StoriesDetailUiModel.Empty) return

        storiesDetailsTimer(state)

        binding.ivStoriesDetailContent.setImageUrl(state.imageContent)
    }

    private fun storiesDetailsTimer(state: StoriesDetailUiModel) {
        with(binding.cvStoriesDetailTimer) {
            apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    StoriesDetailTimer(
                        itemCount = viewModel.mDetailMaxInGroup,
                        data = state,
                    ) { event ->
                        when (event) {
                            NEXT -> viewModelAction(NextDetail)
                        }
                    }
                }
            }
        }
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
            }
        }
        flStoriesNext.onTouchEventStories { event ->
            when (event) {
                TouchEventStories.PAUSE -> pauseStories()

                TouchEventStories.RESUME -> resumeStories()

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

    private fun pauseStories() = with(binding) {
//        icClose.hide()
//        rvStoriesCategory.hide()
//        cvStoriesDetailTimer.hide()
        viewModelAction(PauseStories)
    }

    private fun resumeStories() = with(binding) {
        icClose.show()
        rvStoriesCategory.show()
        cvStoriesDetailTimer.show()
        viewModelAction(ResumeStories)
    }

    private fun viewModelAction(event: StoriesUiAction) {
        viewModel.submitAction(event)
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
