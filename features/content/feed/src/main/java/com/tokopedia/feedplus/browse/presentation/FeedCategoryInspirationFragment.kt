package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.feedplus.browse.presentation.adapter.FeedCategoryInspirationAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationIntent
import com.tokopedia.feedplus.databinding.FragmentFeedCategoryInspirationBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
class FeedCategoryInspirationFragment @Inject constructor(
    viewModelFactory: ViewModelFactory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedCategoryInspirationBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { FeedCategoryInspirationAdapter() }

    private val viewModel by viewModels<FeedCategoryInspirationViewModel> { viewModelFactory }

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

        viewModel.onIntent(FeedCategoryInspirationIntent.InitPage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvCategoryInspiration.layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = adapter.getSpanSizeLookup()
        }
        binding.rvCategoryInspiration.adapter = adapter
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    adapter.submitList(it.itemList)
                }
            }
        }
    }
}
