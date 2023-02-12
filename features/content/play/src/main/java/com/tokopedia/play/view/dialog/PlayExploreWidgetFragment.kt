package com.tokopedia.play.view.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.databinding.FragmentPlayExploreWidgetBinding
import com.tokopedia.play.ui.explorewidget.*
import com.tokopedia.play.ui.explorewidget.adapter.ChipsWidgetAdapter
import com.tokopedia.play.ui.explorewidget.adapter.WidgetAdapter
import com.tokopedia.play.ui.explorewidget.itemdecoration.ChipItemDecoration
import com.tokopedia.play.ui.explorewidget.viewholder.ChipsViewHolder
import com.tokopedia.play.util.isAnyChanged
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.ExploreWidgetInitialState
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.util.AnimationUtils
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.awaitLayout
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.play.R as playR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 24/11/22
 */
@Suppress("LateinitUsage")
class PlayExploreWidgetFragment @Inject constructor(
    private val router: Router,
    private val trackingQueue: TrackingQueue,
    private val analyticFactory: PlayAnalytic2.Factory
) : DialogFragment(),
    ChipsViewHolder.Chips.Listener,
    PlayWidgetListener,
    PlayWidgetAnalyticListener {

    private var _binding: FragmentPlayExploreWidgetBinding? = null
    private val binding: FragmentPlayExploreWidgetBinding get() = _binding!!

    private lateinit var viewModel: PlayViewModel

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
                viewModel.submitAction(NextPageWidgets)
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                if (binding.rvWidgets.getChildAt(0) != null) {
                    binding.srExploreWidget.isEnabled = widgetLayoutManager.findFirstVisibleItemPosition() == 0 && binding.rvWidgets.getChildAt(0).top == 0
                }
                super.onScrolled(view, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) analytic?.scrollExplore()
            }

            override fun checkLoadMore(view: RecyclerView?, dx: Int, dy: Int) {
                if (dx < 0 && dy < 0) return
                val lastVisibleItem = widgetLayoutManager.findLastVisibleItemPosition()
                val firstVisibleItem = widgetLayoutManager.findFirstVisibleItemPosition()
                if ((firstVisibleItem == 0 && lastVisibleItem == layoutManager.itemCount - 1) && hasNextPage)
                    loadMoreNextPage()
                else
                    super.checkLoadMore(view, dx, dy)
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
            }

            override fun onClick(widget: View) {
                viewModel.submitAction(EmptyPageWidget)
            }
        }
    }

    private lateinit var sliderAnimation: SpringAnimation

    private val gestureDetector by lazyThreadSafetyNone {
        GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    var newX = view?.x.orZero()
                    val diffX = e2.x - e1.x
                    if (diffX > 0) {
                        newX += VIEW_TRANSLATION_THRESHOLD
                    } else {
                        newX -= VIEW_TRANSLATION_THRESHOLD
                    }

                    if (newX < 0) return false

                    sliderAnimation = AnimationUtils.addSpringAnim(
                        view = binding.root,
                        property = SpringAnimation.TRANSLATION_X,
                        startPosition = binding.root.x,
                        finalPosition = newX,
                        stiffness = SpringForce.STIFFNESS_LOW,
                        dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY,
                        velocity = VIEW_TRANSLATION_VELOCITY
                    )
                    sliderAnimation.start()
                    return true
                }
            }
        )
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(requireView(), viewLifecycleOwner) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (requireParentFragment() is PlayUserInteractionFragment) {
            val grandParentActivity =
                ((requireParentFragment() as PlayUserInteractionFragment).parentFragment) as PlayFragment

            viewModel = ViewModelProvider(
                grandParentActivity,
                grandParentActivity.viewModelProviderFactory
            ).get(PlayViewModel::class.java)
        }
    }

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

        setupHeader()
        setupView()
        observeState()
        observeEvent()
    }

    private fun setupHeader() {
        binding.widgetHeader.title = getString(playR.string.play_explore_widget_header_title)
        binding.widgetHeader.closeListener = View.OnClickListener {
            analytic?.clickCloseExplore()
            dismiss()
        }
    }

    private fun setupView() {
        binding.rvChips.adapter = chipsAdapter
        binding.rvChips.addItemDecoration(chipDecoration)
        binding.rvChips.addOnScrollListener(chipsScrollListener)

        binding.rvWidgets.adapter = widgetAdapter
        binding.rvWidgets.layoutManager = widgetLayoutManager
        binding.rvWidgets.addOnScrollListener(scrollListener)

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
            viewModel.uiState.withCache().collectLatest {
                val cachedState = it

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
                        cachedState.value.exploreWidget.data.widgets,
                        cachedState.value.exploreWidget.data.param
                    )
                }

                if (analytic != null || cachedState.value.channel.channelInfo.id.isBlank()) return@collectLatest
                analytic = analyticFactory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = it.value.channel.channelInfo
                )
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    ExploreWidgetInitialState -> scrollListener.resetState()
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
        widget: List<WidgetUiModel>,
        param: WidgetParamUiModel
    ) {
        setLayoutManager(state)
        showEmpty(state is ExploreWidgetState.Empty)

        when (state) {
            ExploreWidgetState.Success -> {
                widgetAdapter.setItemsAndAnimateChanges(widget)
                scrollListener.updateStateAfterGetData()
                scrollListener.setHasNextPage(param.hasNextPage)
            }
            ExploreWidgetState.Loading -> {
                widgetAdapter.setItemsAndAnimateChanges(getWidgetShimmering)
            }
            is ExploreWidgetState.Fail -> {
                analytic?.impressToasterGlobalError()
                val errMessage =
                    if (state.error is UnknownHostException) {
                        getString(playR.string.play_explore_widget_noconn_errmessage)
                    } else {
                        getString(
                            playR.string.play_explore_widget_default_errmessage
                        )
                    }

                toaster.showToaster(
                    message = errMessage,
                    actionLabel = getString(playR.string.play_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionListener = {
                        analytic?.clickRetryToaster()
                        viewModel.submitAction(RefreshWidget)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setGravity(Gravity.END)
        window.setLayout(
            (getScreenWidth() * EXPLORE_WIDGET_WIDTH_RATIO).roundToInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setWindowAnimations(playR.style.ExploreWidgetWindowAnim)

        getScreenLocation()
    }

    private fun getScreenLocation() {
        fun setupDraggable() {
            view?.setOnTouchListener { vw, motionEvent ->
                if (vw.x >= DIALOG_VISIBILITY_THRESHOLD) {
                    dismiss()
                    return@setOnTouchListener false
                }
                gestureDetector.onTouchEvent(motionEvent)
                vw.performClick()
                true
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            view?.awaitLayout()
            setupDraggable()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showNow(manager: FragmentManager) {
        if (!isAdded) showNow(manager, TAG)
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
            item,
            channelPositionInList,
            viewModel.selectedChips,
            viewModel.exploreWidgetConfig.autoPlay
        )
        activity?.finish()
        router.route(requireContext(), item.appLink)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int
    ) {
        analytic?.impressChannelCard(
            item,
            config,
            channelPositionInList,
            viewModel.selectedChips
        )
    }

    override fun dismiss() {
        if (!isVisible) return
        viewModel.submitAction(DismissExploreWidget)
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.submitAction(DismissExploreWidget)
        super.onCancel(dialog)
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

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onDetach() {
        super.onDetach()
        if (::sliderAnimation.isInitialized) sliderAnimation.cancel()
    }

    private fun setLayoutManager(state: ExploreWidgetState) {
        binding.rvWidgets.layoutManager = if (state is ExploreWidgetState.Loading) shimmerLayoutManager else widgetLayoutManager
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

    companion object {
        private const val TAG = "PlayExploreWidgetFragment"

        private const val SPAN_SHIMMER = 2

        private const val DIALOG_VISIBILITY_THRESHOLD = 600f
        private const val VIEW_TRANSLATION_THRESHOLD = 100f
        private const val VIEW_TRANSLATION_VELOCITY = 16f
        private const val EXPLORE_WIDGET_WIDTH_RATIO = 0.75

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
