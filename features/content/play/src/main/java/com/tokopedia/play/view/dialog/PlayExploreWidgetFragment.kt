package com.tokopedia.play.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.databinding.FragmentPlayExploreWidgetBinding
import com.tokopedia.play.ui.explorewidget.WidgetAdapter
import com.tokopedia.play.ui.explorewidget.WidgetItemViewHolder
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.view.uimodel.action.ClickChipWidget
import com.tokopedia.play.view.uimodel.action.NextPageWidgets
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 24/11/22
 */

class PlayExploreWidgetFragment @Inject constructor(
    private val router: Router,
) : DialogFragment(),
    WidgetItemViewHolder.Chip.Listener,
    WidgetItemViewHolder.Medium.Listener {

    private var _binding: FragmentPlayExploreWidgetBinding? = null
    private val binding: FragmentPlayExploreWidgetBinding get() = _binding!!

    private val EXPLORE_WIDTH: Int by lazy {
        (getScreenWidth() * 0.85).roundToInt()
    }

    private val EXPLORE_HEIGHT: Int by lazy {
        (getScreenHeight() * 0.95).roundToInt()
    }

    private lateinit var viewModel: PlayViewModel

    private val widgetAdapter = WidgetAdapter(this, this)

    private val layoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(binding.rvWidgets.context, RecyclerView.VERTICAL, false)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.childCount - 1 == layoutManager.findLastVisibleItemPosition()) {
                viewModel.submitAction(NextPageWidgets)
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
        binding.widgetHeader.title = "Video lainnya"
        binding.widgetHeader.closeListener = View.OnClickListener {
            dismiss()
        }
    }

    private fun setupList() {
        binding.rvWidgets.adapter = widgetAdapter
        binding.rvWidgets.layoutManager = layoutManager
        binding.rvWidgets.addOnScrollListener(scrollListener)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest {
                val cachedState = it

                if (cachedState.isChanged { it.exploreWidget.data.items })
                    renderWidgets(cachedState.value.exploreWidget.data.items)
            }
        }
    }

    private fun renderWidgets(list: List<WidgetUiModel>) {
        widgetAdapter.setItemsAndAnimateChanges(list)
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

    override fun onWidgetClicked(item: PlayWidgetChannelUiModel) {
        router.route(requireContext(), item.appLink)
    }

    companion object {
        private const val TAG = "PlayExploreWidgetFragment"

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
