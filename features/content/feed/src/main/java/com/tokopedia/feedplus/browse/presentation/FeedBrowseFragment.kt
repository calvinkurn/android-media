package com.tokopedia.feedplus.browse.presentation

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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.browse.di.FeedBrowseComponent
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.FragmentFeedBrowseBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseFragment : BaseDaggerFragment() {

    private var _binding: FragmentFeedBrowseBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { FeedBrowseAdapter() }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
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
        binding.feedBrowseList.adapter = adapter
    }

    private fun renderHeader(title: String) {
        binding.feedBrowseHeader.title = title
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.feedBrowseHeader)
    }

    private fun renderContent(widgets: List<FeedBrowseUiModel>) {
        adapter.setItems(widgets)
        adapter.notifyDataSetChanged()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(FeedBrowseComponent::class.java).inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
