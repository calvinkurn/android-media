package com.tokopedia.feedplus.browse.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTracker
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseIntent
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.databinding.FragmentFeedCategoryInspirationBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
class FeedCategoryInspirationFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val tracker: FeedBrowseTracker
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedCategoryInspirationBinding? = null
    private val binding get() = _binding!!

    private val chipsListener = object : FeedBrowseChipsViewHolder.Listener {
        override fun onChipClicked(
            viewHolder: FeedBrowseChipsViewHolder,
            slotId: String,
            chip: WidgetMenuModel
        ) {
            viewModel.onIntent(FeedBrowseIntent.SelectChipWidget(slotId, chip))
        }

        override fun onChipSelected(
            viewHolder: FeedBrowseChipsViewHolder,
            slotId: String,
            chip: WidgetMenuModel
        ) {
            viewModel.onIntent(FeedBrowseIntent.FetchCardsWidget(slotId, chip))
        }
    }

    private val bannerListener = object : FeedBrowseBannerViewHolder.Listener {
        override fun onBannerClicked(
            viewHolder: FeedBrowseBannerViewHolder,
            item: FeedBrowseItemListModel.Banner
        ) {
//            showToast(item.title)
            startActivity(Intent(requireContext(), FeedCategoryInspirationActivity::class.java))
        }
    }

    private val adapter by lazy { FeedBrowseAdapter(chipsListener, bannerListener) }

    private val viewModel by viewModels<FeedBrowseViewModel> { viewModelFactory }

    override fun getScreenName(): String {
        return "Feed Browse Inspiration"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedCategoryInspirationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observe()

        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.header)
        binding.header.setBackgroundColor(Color.TRANSPARENT)
        binding.header.setNavigationOnClickListener {
            exitPage()
        }

        val layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = adapter.getSpanSizeLookup()
        }
        binding.rvCategoryInspiration.layoutManager = layoutManager
        binding.rvCategoryInspiration.itemAnimator = null
        binding.rvCategoryInspiration.addItemDecoration(
            FeedBrowseItemDecoration(
                binding.rvCategoryInspiration.resources,
                layoutManager.spanCount
            )
        )
        binding.rvCategoryInspiration.adapter = adapter
    }

    private fun exitPage() {
        tracker.sendClickBackExitEvent()
        requireActivity().finish()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { state ->
                    when (state) {
                        FeedBrowseUiState.Placeholder -> {
//                            hideError()
//                            showPlaceholder()
                        }
                        is FeedBrowseUiState.Success -> {
//                            hideError()
//                            showContent()
//
//                            renderHeader(if (prevState is FeedBrowseUiState.Success) prevState.title else null, state.title)
//                            renderContent(state.widgets)
                            adapter.setList(state.widgets)
                        }
                        is FeedBrowseUiState.Error -> {
//                            hideContent()
//                            showError(state.throwable)
                        }
                    }
                }
        }
    }
}
