package com.tokopedia.play.view.dialog

import android.os.Bundle
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.databinding.FragmentPlayExploreWidgetBinding
import com.tokopedia.play.ui.explorewidget.PlayExploreWidgetCoordinator
import com.tokopedia.play.ui.explorewidget.adapter.ChipsWidgetAdapter
import com.tokopedia.play.ui.explorewidget.adapter.WidgetAdapter
import com.tokopedia.play.ui.explorewidget.itemdecoration.ChipItemDecoration
import com.tokopedia.play.ui.explorewidget.viewholder.ChipsViewHolder
import com.tokopedia.play.util.isAnyChanged
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.BasePlayFragment
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.ExploreWidgetState
import com.tokopedia.play.view.uimodel.ExploreWidgetType
import com.tokopedia.play.view.uimodel.TabMenuUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.view.uimodel.action.ClickChipWidget
import com.tokopedia.play.view.uimodel.action.EmptyPageWidget
import com.tokopedia.play.view.uimodel.action.FetchWidgets
import com.tokopedia.play.view.uimodel.action.NextPageWidgets
import com.tokopedia.play.view.uimodel.action.RefreshWidget
import com.tokopedia.play.view.uimodel.action.UpdateReminder
import com.tokopedia.play.view.uimodel.event.ShowInfoEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.view.uimodel.getChipsShimmering
import com.tokopedia.play.view.uimodel.getWidgetShimmering
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.play.R as playR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 24/11/22
 */
class PlayExploreWidgetFragment @Inject constructor(
    private val router: Router,
    private val trackingQueue: TrackingQueue,
    private val analyticFactory: PlayAnalytic2.Factory
) : BasePlayFragment(),
    ChipsViewHolder.Chips.Listener,
    PlayWidgetListener,
    PlayWidgetAnalyticListener {

    private var _binding: FragmentPlayExploreWidgetBinding? = null
    private val binding: FragmentPlayExploreWidgetBinding get() = _binding!!

    private val coordinator: PlayExploreWidgetCoordinator =
        PlayExploreWidgetCoordinator(this).apply {
            setListener(this@PlayExploreWidgetFragment)
            setAnalyticListener(this@PlayExploreWidgetFragment)
        }

    private val widgetAdapter = WidgetAdapter(coordinator)

    private val widgetLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(binding.rvWidgets.context, RecyclerView.VERTICAL, false)
    }

    private val shimmerLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        StaggeredGridLayoutManager(SPAN_SHIMMER, StaggeredGridLayoutManager.VERTICAL)
    }

    private val scrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerViewScrollListener(binding.rvWidgets.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.submitAction(NextPageWidgets(ExploreWidgetType.Default))
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                if (binding.rvWidgets.getChildAt(0) != null) {
                    binding.srExploreWidget.isEnabled = widgetLayoutManager.findFirstVisibleItemPosition() == 0 && binding.rvWidgets.getChildAt(0).top == 0
                }
                super.onScrolled(view, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    analytic?.scrollExplore(viewModel.widgetInfo, ExploreWidgetType.Default)
            }

            override fun checkLoadMore(view: RecyclerView?, dx: Int, dy: Int) {
                if (dx < 0 && dy < 0) return
                val lastVisibleItem = widgetLayoutManager.findLastVisibleItemPosition()
                val firstVisibleItem = widgetLayoutManager.findFirstVisibleItemPosition()
                if ((firstVisibleItem == 0 && lastVisibleItem == layoutManager.itemCount - 1) && hasNextPage) {
                    loadMoreNextPage()
                } else {
                    super.checkLoadMore(view, dx, dy)
                }
            }
        }
    }

    private val chipsScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    analytic?.impressExploreTab(
                        categoryName = viewModel.selectedChips,
                        chips = getVisibleChips()
                    )
                }
            }
        }
    }

    private val chipsAdapter = ChipsWidgetAdapter(this)
    private val chipDecoration by lazy(LazyThreadSafetyMode.NONE) {
        ChipItemDecoration(binding.rvChips.context)
    }

    private val clickableSpan by lazy(LazyThreadSafetyMode.NONE) {
        object : ClickableSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.color = MethodChecker.getColor(requireContext(), unifyR.color.Unify_GN500)
                tp.isUnderlineText = false
                tp.typeface = com.tokopedia.unifyprinciples.Typography.getFontType(
                    requireContext(),
                    true,
                    com.tokopedia.unifyprinciples.Typography.DISPLAY_3
                )
            }

            override fun onClick(widget: View) {
                viewModel.submitAction(EmptyPageWidget(ExploreWidgetType.Default))
            }
        }
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(requireView(), viewLifecycleOwner) }
    )

    private var analytic: PlayAnalytic2? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayExploreWidgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        fetchWidget()
        observeState()
    }

    private fun setupView() {
        binding.rvChips.adapter = chipsAdapter
        binding.rvChips.addItemDecoration(chipDecoration)
        binding.rvChips.addOnScrollListener(chipsScrollListener)

        binding.rvWidgets.adapter = widgetAdapter
        binding.rvWidgets.layoutManager = widgetLayoutManager
        binding.rvWidgets.addOnScrollListener(scrollListener)
        binding.rvWidgets.itemAnimator = null

        binding.srExploreWidget.setOnRefreshListener {
            binding.srExploreWidget.isRefreshing = !binding.srExploreWidget.isRefreshing
            viewModel.submitAction(RefreshWidget)
            analytic?.swipeRefresh()
        }

        binding.viewExploreWidgetEmpty.tvDescEmptyExploreWidget.text =
            buildSpannedString {
                append(getString(playR.string.play_empty_desc_explore_widget))
                append(
                    getString(playR.string.play_explore_widget_empty),
                    clickableSpan,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        binding.viewExploreWidgetEmpty.tvDescEmptyExploreWidget.movementMethod =
            LinkMovementMethod.getInstance()

        /**
         * Swipe Refresh always fill to screen need a bit adjustment here
         */
        binding.rvWidgets.doOnPreDraw {
            val mWidth = binding.rvWidgets.measuredWidth
            binding.srExploreWidget.layoutParams.width = mWidth
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest { cachedState ->

                if (cachedState.isChanged {
                        it.exploreWidget.data.chips
                    }
                ) {
                    renderChips(
                        cachedState.value.exploreWidget.data.chips
                    )
                }

                if (cachedState.isAnyChanged(
                        { it.exploreWidget.data.widgets },
                        { it.exploreWidget.data.state }
                    )
                ) {
                    renderWidgets(
                        cachedState.value.exploreWidget.data.state,
                        cachedState.value.exploreWidget.data.widgets
                    )
                }

                if (analytic != null || cachedState.value.channel.channelInfo.id.isBlank()) return@collectLatest
                analytic = analyticFactory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = cachedState.value.channel.channelInfo
                )
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is ShowInfoEvent -> {
                        toaster.showToasterInView(
                            message = getTextFromUiString(event.message),
                            view = requireView()
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderChips(chips: TabMenuUiModel) {
        when (chips.state) {
            ResultState.Success -> {
                chipsAdapter.setItemsAndAnimateChanges(chips.items)
            }

            ResultState.Loading -> {
                chipsAdapter.setItemsAndAnimateChanges(getChipsShimmering)
            }

            is ResultState.Fail -> {
                toaster.showError(
                    err = chips.state.error,
                    actionLabel = getString(playR.string.play_try_again),
                    duration = Toaster.LENGTH_LONG,
                    actionListener = {
                        analytic?.clickRetryToaster()
                        viewModel.submitAction(RefreshWidget)
                    }
                )
            }
        }
    }

    private fun renderWidgets(
        state: ExploreWidgetState,
        widget: List<WidgetUiModel>
    ) {
        setLayoutManager(state)
        showEmpty(state is ExploreWidgetState.Empty)

        when (state) {
            ExploreWidgetState.Success -> {
                widgetAdapter.setItemsAndAnimateChanges(widget)
                scrollListener.updateStateAfterGetData()
            }

            ExploreWidgetState.Loading -> {
                widgetAdapter.setItemsAndAnimateChanges(getWidgetShimmering)
            }

            is ExploreWidgetState.Fail -> {
                analytic?.impressToasterGlobalError()
                toaster.showToaster(
                    message = generateErrorMessage(state.error),
                    actionLabel = getString(playR.string.play_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionListener = {
                        analytic?.clickRetryToaster()
                        run { state.onRetry() }
                    }
                )
            }

            else -> {
                // no-op
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Chips Listener
     */

    override fun onChipsClicked(item: ChipWidgetUiModel) {
        viewModel.submitAction(ClickChipWidget(item))
        analytic?.clickExploreTab(item.text)
    }

    /**
     * Card Widget
     */
    override fun onToggleReminderClicked(
        view: PlayWidgetLargeView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        viewModel.submitAction(UpdateReminder(channelId, reminderType))
        analytic?.clickRemind(channelId)
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int
    ) {
        analytic?.clickContentCard(
            selectedChannel = item,
            position = channelPositionInList,
            widgetInfo = viewModel.widgetInfo.copy(exploreWidgetConfig = viewModel.widgetInfo.exploreWidgetConfig.copy(categoryName = viewModel.selectedChips)),
            config = viewModel.exploreWidgetConfig,
            type = ExploreWidgetType.Default
        )
        router.route(context, item.appLink)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int
    ) {
        analytic?.impressChannelCard(
            item = item,
            position = channelPositionInList,
            widgetInfo = viewModel.widgetInfo,
            config = viewModel.exploreWidgetConfig,
            type = ExploreWidgetType.Default
        )
    }

    private fun showEmpty(needToShow: Boolean) {
        if (needToShow) {
            binding.srExploreWidget.gone()
            binding.viewExploreWidgetEmpty.root.visible()
        } else {
            binding.srExploreWidget.visible()
            binding.viewExploreWidgetEmpty.root.gone()
        }
    }

    private fun setLayoutManager(state: ExploreWidgetState) {
        if (state is ExploreWidgetState.Fail) return

        binding.rvWidgets.layoutManager =
            if (state is ExploreWidgetState.Loading) shimmerLayoutManager else widgetLayoutManager
        scrollListener.updateLayoutManager(binding.rvWidgets.layoutManager)
    }

    private fun getVisibleChips(): Map<ChipWidgetUiModel, Int> {
        val chips = chipsAdapter.getItems()
        val layoutManager = binding.rvChips.layoutManager
        if (chips.isNotEmpty() && layoutManager is LinearLayoutManager) {
            val firstPos = layoutManager.findFirstVisibleItemPosition()
            val endPos = layoutManager.findLastVisibleItemPosition()
            if (firstPos > -1 && endPos < chips.size) {
                return (firstPos..endPos)
                    .filter { chips[it] is ChipWidgetUiModel }
                    .associateBy { chips[it] as ChipWidgetUiModel }
            }
        }
        return emptyMap()
    }

    private fun getTextFromUiString(uiString: UiString): String {
        return when (uiString) {
            is UiString.Text -> uiString.text
            is UiString.Resource -> getString(uiString.resource)
        }
    }

    private fun fetchWidget() {
        viewModel.submitAction(FetchWidgets(ExploreWidgetType.Default))
    }

    companion object {
        private const val TAG = "PlayExploreWidgetFragment"

        private const val SPAN_SHIMMER = 2

        fun get(fragmentManager: FragmentManager): PlayExploreWidgetFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? PlayExploreWidgetFragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayExploreWidgetFragment {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayExploreWidgetFragment::class.java.name
            ) as PlayExploreWidgetFragment
        }
    }
}
