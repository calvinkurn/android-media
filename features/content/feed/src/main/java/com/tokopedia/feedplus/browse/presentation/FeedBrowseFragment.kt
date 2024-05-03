package com.tokopedia.feedplus.browse.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.calculateWindowSizeClass
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseImpressionManager
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTrackerImpl
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalAuthorsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.HorizontalStoriesViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseStatefulModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.databinding.FragmentFeedBrowseBinding
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val impressionManager: FeedBrowseImpressionManager,
    private val router: Router,
    trackerFactory: FeedBrowseTrackerImpl.Factory
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedBrowseBinding? = null
    private val binding get() = _binding!!

    private val tracker by lazyThreadSafetyNone {
        trackerFactory.create(FeedBrowseTrackerImpl.PREFIX_BROWSE_PAGE)
    }

    private val bannerListener = object : FeedBrowseBannerViewHolder.Item.Listener {
        override fun onBannerImpressed(
            viewHolder: FeedBrowseBannerViewHolder.Item,
            item: FeedBrowseItemListModel.Banner.Item
        ) {
            tracker.viewInspirationBanner(item.banner, item.slotInfo, item.index)
        }

        override fun onBannerClicked(
            viewHolder: FeedBrowseBannerViewHolder.Item,
            item: FeedBrowseItemListModel.Banner.Item
        ) {
            tracker.clickInspirationBanner(item.banner, item.slotInfo, item.index)
            router.route(context, item.banner.appLink)
        }
    }

    private val chipsListener = object : ChipsViewHolder.Listener {
        override fun onChipImpressed(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        ) {
            tracker.viewChipsWidget(
                chip,
                widgetModel.slotInfo,
                chipPosition
            )
        }

        override fun onChipClicked(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        ) {
            tracker.clickChipsWidget(
                chip,
                widgetModel.slotInfo,
                chipPosition
            )
            viewModel.onAction(FeedBrowseAction.SelectChipWidget(widgetModel.slotInfo.id, chip))
        }

        override fun onChipSelected(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel
        ) {
            viewModel.onAction(FeedBrowseAction.FetchCardsWidget(widgetModel.slotInfo.id, chip))
        }
    }

    private val channelListener = object : FeedBrowseHorizontalChannelsViewHolder.Listener {
        override fun onRetry(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            slotId: String,
            menu: WidgetMenuModel
        ) {
            viewModel.onAction(FeedBrowseAction.FetchCardsWidget(slotId, menu))
        }

        override fun onRefresh(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            slotId: String,
            menu: WidgetMenuModel
        ) {
            viewModel.onAction(FeedBrowseAction.FetchCardsWidget(slotId, menu))
        }

        override fun onCardImpressed(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalChannels,
            channel: PlayWidgetChannelUiModel,
            channelPosition: Int
        ) {
            tracker.viewChannelCard(
                item = channel,
                config = widgetModel.itemState.config,
                slotInfo = widgetModel.slotInfo,
                channelPositionInList = channelPosition
            )
        }

        override fun onCardClicked(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalChannels,
            channel: PlayWidgetChannelUiModel,
            channelPosition: Int
        ) {
            router.route(context, channel.appLink)
            tracker.clickChannelCard(
                item = channel,
                config = widgetModel.itemState.config,
                slotInfo = widgetModel.slotInfo,
                channelPositionInList = channelPosition
            )
        }
    }

    private val creatorListener = object : FeedBrowseHorizontalAuthorsViewHolder.Listener {
        override fun onWidgetImpressed(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        ) {
            tracker.viewAuthorWidget(item, widgetModel.slotInfo, authorWidgetPosition)
        }

        override fun onChannelClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        ) {
            tracker.clickAuthorChannelCard(item, widgetModel.slotInfo, authorWidgetPosition)
            router.route(context, item.contentAppLink)
        }

        override fun onAuthorClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        ) {
            tracker.clickAuthorName(item, widgetModel.slotInfo, authorWidgetPosition)
            router.route(context, item.appLink)
        }
    }

    private val storyListener = object : HorizontalStoriesViewHolder.Listener {
        override fun onWidgetImpressed(
            viewHolder: HorizontalStoriesViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalStories,
            item: StoryNodeModel,
            storyWidgetPosition: Int
        ) {
            tracker.viewStoryWidget(item, widgetModel.slotInfo, storyWidgetPosition)
        }

        override fun onClicked(
            viewHolder: HorizontalStoriesViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalStories,
            item: StoryNodeModel,
            storyWidgetPosition: Int
        ) {
            tracker.clickStoryWidget(item, widgetModel.slotInfo, storyWidgetPosition)
            router.route(context, item.appLink)
        }
    }

    private val adapter by viewLifecycleBound(
        {
            FeedBrowseAdapter(
                it.viewLifecycleOwner.lifecycleScope,
                requireActivity().calculateWindowSizeClass(),
                chipsListener,
                bannerListener,
                channelListener,
                creatorListener,
                storyListener
            )
        }
    )

    private val viewModel: FeedBrowseViewModel by viewModels { viewModelFactory }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            tracker.clickBackExitBrowsePage()
            requireActivity().finish()
        }
    }

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
    ): View {
        _binding = FragmentFeedBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()

        if (activity?.lastNonConfigurationInstance == null) {
            viewModel.onAction(FeedBrowseAction.LoadInitialPage)
            tracker.openScreenBrowseFeedPage()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(FeedBrowseAction.UpdateStoriesStatus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .withCache()
                .collectLatest { cachedState ->

                    val state = cachedState.value
                    val prevState = cachedState.prevValue

                    when (state) {
                        FeedBrowseUiState.Placeholder -> {
                            hideError()
                            adapter.setPlaceholder()
                        }
                        is FeedBrowseUiState.Success -> {
                            hideError()
                            showContent()

                            renderHeader(state.headerDetail)
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

    private fun setupView() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.feedBrowseHeader.header)
        binding.feedBrowseHeader.let { feedHeader ->
            feedHeader.onBackClicked {
                onBackPressedCallback.handleOnBackPressed()
            }
        }

        val layoutManager = GridLayoutManager(context, adapter.spanCount).apply {
            spanSizeLookup = adapter.getSpanSizeLookup()
        }
        binding.feedBrowseList.layoutManager = layoutManager
        binding.feedBrowseList.itemAnimator = null
        binding.feedBrowseList.addItemDecoration(
            FeedBrowseItemDecoration(
                binding.feedBrowseList.resources,
                layoutManager.spanCount
            )
        )
        binding.feedBrowseList.adapter = adapter
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
        binding.feedBrowseError.setActionClickListener {
            if (errorType == GlobalError.MAINTENANCE) {
                onBackPressedCallback.handleOnBackPressed()
            } else {
                viewModel.onAction(FeedBrowseAction.LoadInitialPage)
            }
        }
        binding.feedBrowseError.setType(errorType)
        if (errorType == GlobalError.NO_CONNECTION) {
            binding.feedBrowseError.errorSecondaryAction.show()
            binding.feedBrowseError.errorSecondaryAction.text =
                getString(contentcommonR.string.content_global_error_secondary_text)
            binding.feedBrowseError.setSecondaryActionClickListener {
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
            }
        }
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

    private fun renderHeader(newHeaderDetail: HeaderDetailModel) {
        binding.feedBrowseHeader.let {
            if (!newHeaderDetail.isShowSearchBar) {
                it.header?.title = newHeaderDetail.title
            } else {
                it.setSearchBar(false, newHeaderDetail.searchBarPlaceholder)
                it.setSearchbarFocusListener { view, focusState ->
                    searchbarFocusHandler(view, focusState)
                }
            }
        }
    }

    private fun renderContent(widgets: List<FeedBrowseStatefulModel>) {
        impressionManager.onNewWidgets(widgets.map { it.model })
        if (widgets.isEmpty()) {
            adapter.setPlaceholder()
        } else {
            adapter.setItems(widgets) {
                if (_binding == null) return@setItems
                binding.feedBrowseList.invalidateItemDecorations()
            }
        }
    }

    private fun searchbarFocusHandler(view: View, focusState: Boolean) {
        if (focusState) {
            tracker.clickSearchbar()

            viewModel.getHeaderDetail().also {
                val intent = router.getIntent(context, it.applink.ifNull { ApplinkConstInternalContent.INTERNAL_FEED_LOCAL_BROWSE })
                intent.putExtra(FeedLocalSearchActivity.TAG_PLACEHOLDER_PARAM, it.searchBarPlaceholder)
                startActivity(intent)
            }
        }

        view.clearFocus()
    }

    companion object {

        fun create(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle?
        ): FeedBrowseFragment {
            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedBrowseFragment::class.java.name
            ).apply {
                arguments = bundle
            } as FeedBrowseFragment
        }
    }
}
