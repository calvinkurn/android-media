package com.tokopedia.feedplus.browse.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTracker
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseIntent
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel2
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.databinding.FragmentFeedBrowseBinding
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
class FeedBrowseFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    coroutineDispatchers: CoroutineDispatchers,
    private val tracker: FeedBrowseTracker
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedBrowseBinding? = null
    private val binding get() = _binding!!

//    private val channelListener = object : FeedBrowseChannelViewHolder2.Listener {
//        override fun onRetryClicked(
//            extraParam: WidgetRequestModel,
//            widgetModel: FeedBrowseUiModel.Channel
//        ) {
//            viewModel.submitAction(
//                FeedBrowseUiAction.FetchCards(extraParam, widgetModel.id)
//            )
//        }
//
//        override fun onCardImpressed(
//            channelModel: PlayWidgetChannelUiModel,
//            config: PlayWidgetConfigUiModel,
//            widgetModel: FeedBrowseUiModel.Channel,
//            channelPositionInList: Int,
//            verticalWidgetPosition: Int
//        ) {
//            tracker.sendViewChannelCardEvent(
//                item = channelModel,
//                config = config,
//                widget = widgetModel,
//                channelPositionInList = channelPositionInList,
//                verticalWidgetPosition = verticalWidgetPosition
//            )
//        }
//
//        override fun onCardClicked(
//            channelModel: PlayWidgetChannelUiModel,
//            config: PlayWidgetConfigUiModel,
//            widgetModel: FeedBrowseUiModel.Channel,
//            channelPositionInList: Int,
//            verticalWidgetPosition: Int
//        ) {
//            tracker.sendClickChannelCardEvent(
//                item = channelModel,
//                config = config,
//                widget = widgetModel,
//                channelPositionInList = channelPositionInList,
//                verticalWidgetPosition = verticalWidgetPosition
//            )
//            goToPage(channelModel.appLink)
//        }
//
//        override fun onChipImpressed(
//            chipModel: FeedBrowseChipUiModel,
//            widgetModel: FeedBrowseUiModel.Channel,
//            chipPositionInList: Int,
//            verticalWidgetPosition: Int
//        ) {
//            tracker.sendViewChipsWidgetEvent(
//                chipModel,
//                widgetModel,
//                chipPositionInList,
//                verticalWidgetPosition
//            )
//        }
//
//        override fun onChipClicked(
//            chipModel: FeedBrowseChipUiModel,
//            widgetModel: FeedBrowseUiModel.Channel
//        ) {
//            viewModel.submitAction(FeedBrowseUiAction.SelectChip(chipModel, widgetModel.id))
//        }
//
//        override fun onChipSelected(
//            chipModel: FeedBrowseChipUiModel,
//            widgetModel: FeedBrowseUiModel.Channel,
//            chipPositionInList: Int,
//            verticalWidgetPosition: Int
//        ) {
//            tracker.sendClickChipsWidgetEvent(
//                chipModel,
//                widgetModel,
//                chipPositionInList,
//                verticalWidgetPosition
//            )
//            viewModel.submitAction(
//                FeedBrowseUiAction.FetchCards(chipModel.extraParam, widgetModel.id)
//            )
//        }
//
//        override fun onWidgetShouldRefresh(
//            extraParam: WidgetRequestModel,
//            widgetModel: FeedBrowseUiModel.Channel
//        ) {
//            viewModel.submitAction(
//                FeedBrowseUiAction.FetchCards(extraParam, widgetModel.id)
//            )
//        }
//    }

    private val bannerListener = object : FeedBrowseBannerViewHolder.Listener {
        override fun onBannerClicked(
            viewHolder: FeedBrowseBannerViewHolder,
            item: FeedBrowseItemListModel.Banner
        ) {
//            showToast(item.title)
            startActivity(Intent(requireContext(), FeedCategoryInspirationActivity::class.java))
        }
    }

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

    private val adapter by viewLifecycleBound(
        { FeedBrowseAdapter(chipsListener, bannerListener) }
    )

    private val viewModel: FeedBrowseViewModel by viewModels { viewModelFactory }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitPage()
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

        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)
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
                .withCache()
                .collectLatest { cachedState ->

                    val state = cachedState.value
                    val prevState = cachedState.prevValue

                    when (state) {
                        FeedBrowseUiState.Placeholder -> {
                            hideError()
                            showPlaceholder()
                        }
                        is FeedBrowseUiState.Success -> {
                            hideError()
                            showContent()

                            renderHeader(if (prevState is FeedBrowseUiState.Success) prevState.title else null, state.title)
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
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.feedBrowseHeader)
        binding.feedBrowseHeader.setBackgroundColor(Color.TRANSPARENT)
        binding.feedBrowseHeader.setNavigationOnClickListener {
            exitPage()
        }

        val layoutManager = GridLayoutManager(context, 2).apply {
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

    private fun showPlaceholder() {
//        renderContent(
//            listOf(
//                FeedBrowseUiModel.Placeholder(type = FeedBrowsePlaceholderView.Type.Title),
//                FeedBrowseUiModel.Placeholder(type = FeedBrowsePlaceholderView.Type.Chips),
//                FeedBrowseUiModel.Placeholder(type = FeedBrowsePlaceholderView.Type.Cards),
//                FeedBrowseUiModel.Placeholder(type = FeedBrowsePlaceholderView.Type.Title),
//                FeedBrowseUiModel.Placeholder(type = FeedBrowsePlaceholderView.Type.Cards)
//            )
//        )
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
                exitPage()
            } else {
                viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)
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

    private fun renderHeader(cachedTitle: String?, newTitle: String) {
        if (cachedTitle == newTitle) return
        binding.feedBrowseHeader.title = newTitle
    }

    private fun renderContent(widgets: List<FeedBrowseUiModel2>) {
//        if (binding.feedBrowseList.isComputingLayout ||
//            binding.feedBrowseList.scrollState != RecyclerView.SCROLL_STATE_IDLE
//        ) {
//            return
//        }
        adapter.setList(widgets) {
            if (_binding == null) return@setList
            binding.feedBrowseList.invalidateItemDecorations()
        }
    }

    private fun goToPage(appLink: String) {
        RouteManager.route(requireContext(), appLink)
    }

    private fun exitPage() {
        tracker.sendClickBackExitEvent()
        requireActivity().finish()
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
