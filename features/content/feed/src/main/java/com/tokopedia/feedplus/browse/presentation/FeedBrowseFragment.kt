package com.tokopedia.feedplus.browse.presentation

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
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView
import com.tokopedia.feedplus.databinding.FragmentFeedBrowseBinding
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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

        viewModel.submitAction(FeedBrowseUiAction.LoadInitialPage)
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
                    when (state) {
                        FeedBrowseUiState.Placeholder -> {
                            hideError()
                            showPlaceholder()
                        }
                        is FeedBrowseUiState.Success -> {
                            hideError()
                            showContent()
                            renderHeader(state.title)
                            renderContent(state.widgets)
                        }
                        is FeedBrowseUiState.Error -> {
                            hideContent()
                            showError(state.throwable)
                        }
                    }
                }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .collect { event ->
                    if (event == null) return@collect
                }
        }
    }

    private fun setupView() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.feedBrowseHeader)
        binding.feedBrowseHeader.setBackgroundColor(Color.TRANSPARENT)
        binding.feedBrowseHeader.setNavigationOnClickListener {
            activity?.finish()
        }

        binding.feedBrowseList.adapter = adapter
    }

    private fun showPlaceholder() {
        renderContent(
            listOf(
                FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Title),
                FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Chips),
                FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Cards),
                FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Title),
                FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Cards)
            )
        )
    }

    private fun showError(throwable: Throwable) {
        val errorType = when (throwable) {
            is SocketTimeoutException -> GlobalError.PAGE_FULL
            is UnknownHostException,
            is SocketException,
            is InterruptedIOException -> GlobalError.NO_CONNECTION
            else -> {
                GlobalError.MAINTENANCE
            }
        }
        binding.feedBrowseError.setType(errorType)
        binding.feedBrowseError.show()
    }

    private fun hideError() {
        binding.feedBrowseError.gone()
    }

    private fun showContent() {
        binding.feedBrowseList.show()
    }

    private fun hideContent() {
        binding.feedBrowseList.hide()
    }

    private fun renderHeader(title: String) {
        binding.feedBrowseHeader.title = title
    }

    private fun renderContent(widgets: List<FeedBrowseUiModel>) {
        if (widgets.isEmpty()) return
        adapter.setItemsAndAnimateChanges(widgets)
    }
}
