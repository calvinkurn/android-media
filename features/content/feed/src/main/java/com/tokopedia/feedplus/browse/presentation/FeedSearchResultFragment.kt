package com.tokopedia.feedplus.browse.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.calculateWindowSizeClass
import com.tokopedia.content.common.util.withCache
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTrackerImpl
import com.tokopedia.feedplus.browse.presentation.adapter.FeedSearchResultAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.CategoryInspirationItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.factory.FeedSearchResultViewModelFactory
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.action.FeedSearchResultAction
import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent
import com.tokopedia.feedplus.browse.presentation.model.state.FeedSearchResultPageState
import com.tokopedia.feedplus.databinding.FragmentFeedSearchResultBinding
import com.tokopedia.feedplus.domain.mapper.toAuthorWidgetModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedplusR
import com.tokopedia.content.common.R as contentcommonR

internal class FeedSearchResultFragment @Inject constructor(
    private val viewModelFactoryCreator: FeedSearchResultViewModelFactory.Creator,
    private val router: Router,
    trackerFactory: FeedBrowseTrackerImpl.Factory
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedSearchResultBinding? = null
    private val binding: FragmentFeedSearchResultBinding get() = _binding!!

    private val viewModel: FeedSearchResultViewModel by viewModels {
        viewModelFactoryCreator.create(
            this,
            arguments?.getString(FeedSearchResultActivity.KEYWORD_PARAM).orEmpty()
        )
    }

    private val tracker by lazyThreadSafetyNone {
        trackerFactory.create(FeedBrowseTrackerImpl.PREFIX_LOCAL_SEARCH_PAGE)
    }

    private val adapter: FeedSearchResultAdapter by lazyThreadSafetyNone {
        FeedSearchResultAdapter(
            this.viewLifecycleOwner.lifecycleScope,
            requireActivity().calculateWindowSizeClass(),
            cardListener
        )
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

    private val cardListener = object : InspirationCardViewHolder.Item.Listener {
        override fun onImpressed(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
            tracker.viewChannelCard(
                model.item,
                model.config,
                model.slotInfo,
                model.index
            )
        }

        override fun onClicked(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
            tracker.clickChannelCard(
                model.item,
                model.config,
                model.slotInfo,
                model.index
            )
            router.route(context, model.item.appLink)
        }

        override fun onAuthorClicked(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
            tracker.clickAuthorName(
                item = model.item.partner.toAuthorWidgetModel(model.item.channelId),
                slotInfo = model.slotInfo,
                widgetPositionInList = model.index,
            )
            router.route(context, model.item.partner.appLink)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            tracker.clickBackExit()
            requireActivity().finish()
        }
    }

    override fun getScreenName(): String = "Search Result Fragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            onBackPressedCallback
        )
    }

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

        tracker.openScreenBrowseFeedPage()

        setupView()
        setupObserver()

        viewModel.submitAction(FeedSearchResultAction.LoadResult)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvSearchResult.removeOnScrollListener(loadMoreListener)

        _binding = null
    }

    private fun setupView() {
        binding.srpHeader.onBackClicked {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.srpHeader.setSearchbarFocusListener { view, focusState ->
            view.clearFocus()

            if (focusState) {
                tracker.clickSearchbar()

                val intent = router.getIntent(context, ApplinkConstInternalContent.INTERNAL_FEED_LOCAL_BROWSE).apply {
                    putExtra(FeedLocalSearchActivity.TAG_KEYWORD, viewModel.searchKeyword)
                }

                startActivity(intent)
            }
        }

        binding.rvSearchResult.let {
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
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.withCache().collectLatest {
                    val (prev, curr) = it

                    renderSearchBar(prev?.searchKeyword, curr.searchKeyword)
                    renderContents(prev?.pageState, curr.pageState, curr.contents, curr.hasNextPage)
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

    private fun renderContents(
        prevPageState: FeedSearchResultPageState?,
        pageState: FeedSearchResultPageState,
        contents: List<FeedSearchResultContent>,
        hasNextPage: Boolean,
    ) {
        if (prevPageState == pageState) return

        when(pageState) {
            FeedSearchResultPageState.LOADING -> {
                if (contents.isEmpty()) {
                    adapter.setShimmer()
                }
            }
            FeedSearchResultPageState.SUCCESS -> {
                val finalContents = contents + if (hasNextPage) {
                    listOf(FeedSearchResultContent.Loading)
                } else {
                    emptyList()
                }

                adapter.setItems(finalContents)
            }
            FeedSearchResultPageState.RESTRICTED -> {
                binding.errorView.apply {
                    errorTitle.text = getString(feedplusR.string.feed_local_search_restricted_title)
                    errorDescription.text = getString(feedplusR.string.feed_local_search_restricted_desc)
                    errorSecondaryAction.text = getString(feedplusR.string.feed_local_search_restricted_cta)

                    errorAction.hide()
                    errorSecondaryAction.show()
                    errorIllustration.loadImage(getString(feedplusR.string.feed_search_restricted_illustration))

                    setSecondaryActionClickListener {
                        val template = getString(contentcommonR.string.feed_webview_template)
                        val link = getString(feedplusR.string.feed_content_tnc_link)

                        router.route(requireContext(), template.format(ApplinkConst.WEBVIEW, link))
                    }
                }
            }
            FeedSearchResultPageState.NOT_FOUND -> {
                binding.errorView.apply {
                    errorTitle.text = getString(feedplusR.string.feed_local_search_not_found_title)
                    errorDescription.text = getString(feedplusR.string.feed_local_search_not_found_desc)
                    errorAction.text = getString(feedplusR.string.feed_local_search_not_found_cta)

                    errorAction.show()
                    errorSecondaryAction.hide()
                    errorIllustration.loadImage(getString(feedplusR.string.feed_search_not_found_illustration))

                    setActionClickListener {
                        val intent = router.getIntent(requireContext(), ApplinkConstInternalContent.INTERNAL_FEED_LOCAL_BROWSE)
                        intent.putExtra(FeedLocalSearchActivity.TAG_KEYWORD, viewModel.searchKeyword)
                        startActivity(intent)

                        activity?.finish()
                    }
                }
            }
            FeedSearchResultPageState.INTERNAL_ERROR -> {
                binding.errorView.apply {
                    setType(GlobalError.SERVER_ERROR)

                    setActionClickListener {
                        viewModel.submitAction(FeedSearchResultAction.LoadResult)
                    }
                }
            }
            else -> {
                binding.errorView.setType(GlobalError.NO_CONNECTION)
            }
        }

        if (shouldShowResult(pageState)) {
            showResult()
        } else {
            showError()
        }
    }

    private fun shouldShowResult(pageState: FeedSearchResultPageState): Boolean {
        return pageState == FeedSearchResultPageState.LOADING || pageState == FeedSearchResultPageState.SUCCESS
    }

    private fun showResult() {
        binding.rvSearchResult.show()
        binding.errorView.hide()
    }

    private fun showError() {
        binding.rvSearchResult.hide()
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
