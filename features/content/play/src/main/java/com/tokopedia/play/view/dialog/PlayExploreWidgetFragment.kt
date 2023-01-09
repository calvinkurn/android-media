package com.tokopedia.play.view.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.databinding.FragmentPlayExploreWidgetBinding
import com.tokopedia.play.ui.explorewidget.ChipItemDecoration
import com.tokopedia.play.ui.explorewidget.ChipsViewHolder
import com.tokopedia.play.ui.explorewidget.ChipsWidgetAdapter
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
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 24/11/22
 */

class PlayExploreWidgetFragment @Inject constructor(
    private val router: Router,
) : DialogFragment(),
    ChipsViewHolder.Chips.Listener,
    PlayWidgetMediumViewHolder.Channel.Listener {

    private var _binding: FragmentPlayExploreWidgetBinding? = null
    private val binding: FragmentPlayExploreWidgetBinding get() = _binding!!

    private val EXPLORE_WIDTH: Int by lazy {
        (getScreenWidth() * 0.75).roundToInt()
    }

    private val EXPLORE_HEIGHT: Int by lazy {
        (getScreenHeight() * 0.95).roundToInt()
    }

    private lateinit var viewModel: PlayViewModel

    private val widgetAdapter = PlayWidgetChannelMediumAdapter(cardChannelListener = this)

    private val layoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(binding.rvWidgets.context, SPAN_CHANNEL)
    }

    private val scrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.submitAction(NextPageWidgets)
            }
        }
    }

    private val chipsAdapter = ChipsWidgetAdapter(this)
    private val chipDecoration by lazy(LazyThreadSafetyMode.NONE) {
        ChipItemDecoration(binding.rvChips.context)
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
        setupList()
        observeState()
    }

    private fun setupHeader() {
        binding.widgetHeader.title = getString(playR.string.play_explore_widget_header_title)
        binding.widgetHeader.closeListener = View.OnClickListener {
            dismiss()
        }
    }

    private fun setupList() {
        binding.rvChips.adapter = chipsAdapter
        binding.rvChips.addItemDecoration(chipDecoration)

        binding.rvWidgets.adapter = widgetAdapter
        binding.rvWidgets.layoutManager = layoutManager
        binding.rvWidgets.addOnScrollListener(scrollListener)

        binding.srExploreWidget.setOnRefreshListener {
            binding.srExploreWidget.isRefreshing = !binding.srExploreWidget.isRefreshing
            viewModel.submitAction(RefreshWidget)
        }
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

                if (cachedState.isChanged {
                        it.exploreWidget.data.widgets
                    })
                    renderWidgets(
                        cachedState.value.exploreWidget.data.state,
                        cachedState.value.exploreWidget.data.widgets.getChannelBlock
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
                    text = chips.state.error.message.orEmpty(),
                    actionText = getString(playR.string.title_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    clickListener = { viewModel.submitAction(RefreshWidget) }).show()
            }
        }
    }

    private fun renderWidgets(state: ResultState, widget: WidgetItemUiModel) {
        when (state) {
            ResultState.Success -> {
                widgetAdapter.setItemsAndAnimateChanges(widget.item.items)
            }
            ResultState.Loading -> {
                widgetAdapter.setItemsAndAnimateChanges(getWidgetShimmering)
            }
            is ResultState.Fail -> {
                Toaster.build(
                    view = requireView(),
                    text = state.error.message.orEmpty(),
                    actionText = getString(playR.string.title_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    clickListener = { viewModel.submitAction(RefreshWidget) }).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setGravity(Gravity.END)
        window.setLayout(
            EXPLORE_WIDTH,
            EXPLORE_HEIGHT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setWindowAnimations(playR.style.ExploreWidgetWindowAnim)

        setupDraggable()
    }

    private fun setupDraggable() {
        view?.setOnTouchListener { vw, motionEvent ->
            if(vw.x >= 700) {
                dismiss()
                return@setOnTouchListener false
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                if(motionEvent.x < 10) vw.x = vw.x - 100 else vw.x = vw.x + 100
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

    override fun onChipsClicked(item: ChipWidgetUiModel) {
        viewModel.submitAction(ClickChipWidget(item))
    }

    /**
     * Card Widget
     */
    override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel, position: Int) {
        router.route(requireContext(), item.appLink)
    }

    override fun onChannelImpressed(view: View, item: PlayWidgetChannelUiModel, position: Int) {
    }

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
    }

    override fun dismiss() {
        viewModel.submitAction(DismissExploreWidget)
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.submitAction(DismissExploreWidget)
        super.onCancel(dialog)
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
