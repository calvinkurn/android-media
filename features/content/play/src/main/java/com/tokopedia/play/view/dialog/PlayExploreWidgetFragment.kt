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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.databinding.FragmentPlayExploreWidgetBinding
import com.tokopedia.play.ui.explorewidget.ChipItemDecoration
import com.tokopedia.play.ui.explorewidget.ChipsViewHolder
import com.tokopedia.play.ui.explorewidget.ChipsWidgetAdapter
import com.tokopedia.play.util.isAnyChanged
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.widget.medium.adapter.PlayWidgetChannelMediumAdapter
import com.tokopedia.play.widget.ui.widget.medium.adapter.PlayWidgetMediumViewHolder
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.play.R as playR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 24/11/22
 */

class PlayExploreWidgetFragment @Inject constructor(
    private val router: Router,
    private val trackingQueue: TrackingQueue,
    private val analyticFactory: PlayAnalytic2.Factory,
) : DialogFragment(),
    ChipsViewHolder.Chips.Listener,
    PlayWidgetMediumViewHolder.Channel.Listener {

    private var _binding: FragmentPlayExploreWidgetBinding? = null
    private val binding: FragmentPlayExploreWidgetBinding get() = _binding!!

    private val EXPLORE_WIDTH: Int by lazy {
        (getScreenWidth() * 0.75).roundToInt()
    }

    private lateinit var viewModel: PlayViewModel

    private val widgetAdapter = PlayWidgetChannelMediumAdapter(cardChannelListener = this)

    private val scrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerViewScrollListener(binding.rvWidgets.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.submitAction(NextPageWidgets)
            }
            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                analytic?.scrollExplore()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (requireParentFragment() is PlayUserInteractionFragment) {
            val grandParentActivity =
                ((requireParentFragment() as PlayUserInteractionFragment).parentFragment) as PlayFragment

            viewModel = ViewModelProvider(
                grandParentActivity, grandParentActivity.viewModelProviderFactory
            ).get(PlayViewModel::class.java)
        }
    }

    private var analytic: PlayAnalytic2? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        binding.rvWidgets.adapter = widgetAdapter
        binding.rvWidgets.layoutManager = GridLayoutManager(binding.rvWidgets.context, SPAN_CHANNEL)
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
                    "Cek Sekarang",
                    clickableSpan,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        binding.viewExploreWidgetEmpty.tvDescEmptyExploreWidget.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest {
                val cachedState = it

                if (cachedState.isChanged {
                        it.exploreWidget.data.chips
                    })
                    renderChips(
                        cachedState.value.exploreWidget.data.chips
                    )

                if (cachedState.isAnyChanged (
                        { it.exploreWidget.data.widgets },
                        { it.exploreWidget.data.state }
                    ))
                    renderWidgets(
                        cachedState.value.exploreWidget.data.state,
                        cachedState.value.exploreWidget.data.widgets.getChannelBlock
                    )

                if (analytic != null || cachedState.value.channel.channelInfo.id.isBlank()) return@collectLatest
                analytic = analyticFactory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = it.value.channel.channelInfo,
                )
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
                Toaster.build(
                    view = requireView(),
                    text = chips.state.error.message.orEmpty(), //if empty use from figma
                    actionText = getString(playR.string.play_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    clickListener = { viewModel.submitAction(RefreshWidget) }).show()
            }
        }
    }

    private fun renderWidgets(state: WidgetState, widget: WidgetItemUiModel) {
        when (state) {
            WidgetState.Success -> {
                showEmpty(false)
                widgetAdapter.setItemsAndAnimateChanges(widget.item.items)
            }
            WidgetState.Empty -> {
                showEmpty(true)
            }
            WidgetState.Loading -> {
                widgetAdapter.setItemsAndAnimateChanges(getWidgetShimmering)
            }
            is WidgetState.Fail -> {
                analytic?.impressToasterGlobalError()
                val errMessage = if (state.error is UnknownHostException) getString(playR.string.play_explore_widget_noconn_errmessage) else getString(playR.string.play_explore_widget_default_errmessage)
                Toaster.build(
                    view = requireView(),
                    text = errMessage,
                    actionText = getString(playR.string.play_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    clickListener = {
                        viewModel.submitAction(RefreshWidget)
                        analytic?.clickRetryToaster()
                    }
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setGravity(Gravity.END)
        window.setLayout(
            EXPLORE_WIDTH,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setWindowAnimations(playR.style.ExploreWidgetWindowAnim)

        setupDraggable()
    }

    private fun setupDraggable() {
        view?.setOnTouchListener { vw, motionEvent ->
            var newX = vw.x
            if(vw.x >= 700) {
                dismiss()
                return@setOnTouchListener false
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                newX = if(motionEvent.x < 10) newX - 100 else newX + 100
                vw.animate().xBy(newX)
                true
            }
            false
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

    override fun onChipsImpressed(item: ChipWidgetUiModel, position: Int) {
        analytic?.impressExploreTab(categoryName = item.text, chips = arrayListOf(item), position = position)
    }

    /**
     * Card Widget
     */
    override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
        analytic?.clickContentCard(item, position, viewModel.selectedChips, viewModel.exploreWidgetConfig.autoPlay)
        router.route(requireContext(), item.appLink)
    }

    override fun onChannelImpressed(view: View, item: PlayWidgetChannelUiModel, position: Int) {}

    override fun onMenuActionButtonClicked(
        view: View,
        item: PlayWidgetChannelUiModel,
        position: Int
    ) {
        //No op
    }

    override fun onToggleReminderChannelClicked(
        item: PlayWidgetChannelUiModel,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        viewModel.submitAction(UpdateReminder(item.channelId, reminderType))
        analytic?.clickRemind(item.channelId)
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

    companion object {
        private const val TAG = "PlayExploreWidgetFragment"

        private const val SPAN_CHANNEL = 2

        fun get(fragmentManager: FragmentManager): PlayExploreWidgetFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? PlayExploreWidgetFragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayExploreWidgetFragment {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayExploreWidgetFragment::class.java.name
            ) as PlayExploreWidgetFragment
        }
    }
}
