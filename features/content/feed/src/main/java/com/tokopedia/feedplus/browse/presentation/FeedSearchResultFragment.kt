package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.withCache
import com.tokopedia.feedplus.browse.presentation.adapter.FeedSearchResultAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.CategoryInspirationItemDecoration
import com.tokopedia.feedplus.browse.presentation.factory.FeedSearchResultViewModelFactory
import com.tokopedia.feedplus.browse.presentation.model.action.FeedSearchResultAction
import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent
import com.tokopedia.feedplus.browse.presentation.model.state.FeedSearchResultPageState
import com.tokopedia.feedplus.databinding.FragmentFeedSearchResultBinding
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedplusR

internal class FeedSearchResultFragment @Inject constructor(
    private val viewModelFactoryCreator: FeedSearchResultViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedSearchResultBinding? = null
    private val binding: FragmentFeedSearchResultBinding get() = _binding!!

    private val viewModel: FeedSearchResultViewModel by viewModels {
        viewModelFactoryCreator.create(
            this,
            arguments?.getString(FeedSearchResultActivity.KEYWORD_PARAM).orEmpty()
        )
    }

    private val adapter: FeedSearchResultAdapter by lazyThreadSafetyNone {
        FeedSearchResultAdapter(this.viewLifecycleOwner.lifecycleScope)
    }

    private val loadMoreListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            if (lastVisibleItemPosition >= adapter.itemCount - LOAD_PAGE_THRESHOLD) {
                viewModel.submitAction(FeedSearchResultAction.LoadResult)
            }
        }
    }

    override fun getScreenName(): String = "Search Result Fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedSearchResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserver()

        viewModel.submitAction(FeedSearchResultAction.LoadResult)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.resultRv.removeOnScrollListener(loadMoreListener)

        _binding = null
    }

    private fun setupView() {
        binding.srpHeader.onBackClicked {
            activity?.finish()
        }

        binding.resultRv.let {
            val layoutManager = GridLayoutManager(context, adapter.spanCount).apply {
                spanSizeLookup = adapter.getSpanSizeLookup()
            }
            it.layoutManager = layoutManager
            it.itemAnimator = null
            it.addOnScrollListener(loadMoreListener)
            it.addItemDecoration(
                CategoryInspirationItemDecoration(
                    it.resources,
                    layoutManager.spanCount
                )
            )
            it.adapter = adapter
        }

        binding.errorView.setActionClickListener {
            viewModel.submitAction(FeedSearchResultAction.LoadResult)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.withCache().collectLatest {
                    val (prev, curr) = it

                    renderSearchBar(prev?.searchKeyword, curr.searchKeyword)
                    renderPageState(prev?.pageState, curr.pageState, curr.contents, curr.hasNextPage)
                    renderContents(prev?.contents, curr.contents)
                }
            }
        }
    }

    private fun renderSearchBar(
        prevKeyword: String?,
        keyword: String
    ) {
        if (prevKeyword == keyword) return

        binding.srpHeader.setSearchbarText(keyword)
    }

    private fun renderPageState(
        prevPageState: FeedSearchResultPageState?,
        pageState: FeedSearchResultPageState,
        contents: List<FeedSearchResultContent>,
        hasNextPage: Boolean,
    ) {
        if (prevPageState == pageState) return

        when(pageState) {
            is FeedSearchResultPageState.Loading -> {
                if (contents.isEmpty()) {
                    adapter.setShimmer()
                }
                showResult()
            }
            is FeedSearchResultPageState.Success -> {
                val finalContents = contents + if (hasNextPage) {
                    listOf(FeedSearchResultContent.Loading)
                } else {
                    emptyList()
                }

                adapter.setItems(finalContents)
                showResult()
            }
            is FeedSearchResultPageState.Restricted -> {
                binding.errorView.apply {
                    errorTitle.text = getString(feedplusR.string.feed_local_search_restricted_title)
                    errorDescription.text = getString(feedplusR.string.feed_local_search_restricted_desc)
                    errorSecondaryAction.text = getString(feedplusR.string.feed_local_search_restricted_cta)

                    errorAction.hide()
                    errorSecondaryAction.show()
                    errorIllustration.setImageResource(feedplusR.drawable.feed_search_restricted_illustration)
                }

                showError()
            }
            is FeedSearchResultPageState.NotFound -> {
                binding.errorView.apply {
                    errorTitle.text = getString(feedplusR.string.feed_local_search_not_found_title)
                    errorDescription.text = getString(feedplusR.string.feed_local_search_not_found_desc)
                    errorAction.text = getString(feedplusR.string.feed_local_search_not_found_cta)

                    errorAction.show()
                    errorSecondaryAction.hide()
                    errorIllustration.setImageResource(feedplusR.drawable.feed_search_not_found_illustration)
                }

                showError()
            }
            is FeedSearchResultPageState.InternalError -> {
                binding.errorView.setType(GlobalError.SERVER_ERROR)
                showError()
            }
            else -> {
                binding.errorView.setType(GlobalError.NO_CONNECTION)
                showError()
            }
        }
    }

    private fun renderContents(
        prevContents: List<FeedSearchResultContent>?,
        contents: List<FeedSearchResultContent>
    ) {
        if (prevContents == contents) return

        adapter.setItems(contents)
    }

    private fun showResult() {
        binding.resultRv.show()
        binding.errorView.hide()
    }

    private fun showError() {
        binding.resultRv.hide()
        binding.errorView.show()
    }

    companion object {

        private const val LOAD_PAGE_THRESHOLD = 2

        fun create(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle?
        ): FeedSearchResultFragment {
            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedSearchResultFragment::class.java.name
            ).apply {
                arguments = bundle
            } as FeedSearchResultFragment
        }
    }
}
