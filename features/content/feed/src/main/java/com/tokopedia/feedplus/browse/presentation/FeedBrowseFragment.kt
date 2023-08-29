package com.tokopedia.feedplus.browse.presentation

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.FragmentFeedBrowseBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedBrowseBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { FeedBrowseAdapter() }

    private val viewModel: FeedBrowseViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()
        observeUiEvent()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { state ->
                    if (state == null) return@collectLatest

                    renderHeader(state.title)
                    renderContent(state.widgets)
                }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .collect { event ->
                    if (event == null) return@collect
                }
        }
    }

    private fun setupView() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.feedBrowseHeader)
        binding.feedBrowseHeader.setBackgroundColor(Color.TRANSPARENT)
        binding.feedBrowseHeader.setNavigationOnClickListener {
            activity?.finish()
        }

        binding.feedBrowseList.adapter = adapter
        binding.feedBrowseList.addItemDecoration(
            object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.top = resources.getDimensionPixelOffset(R.dimen.feed_space_12)
                    outRect.bottom = resources.getDimensionPixelOffset(R.dimen.feed_space_12)
                }
            }
        )
    }

    private fun renderHeader(title: String) {
        binding.feedBrowseHeader.title = title
    }

    private fun renderContent(widgets: List<FeedBrowseUiModel>) {
        adapter.setItems(widgets)
        adapter.notifyDataSetChanged()
    }
}
