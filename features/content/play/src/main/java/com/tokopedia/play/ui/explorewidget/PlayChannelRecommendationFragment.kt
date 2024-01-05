package com.tokopedia.play.ui.explorewidget

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.sidesheet.SideSheetBehavior
import com.tokopedia.content.common.util.sidesheet.SideSheetCallback
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.databinding.PlayDialogExploreWidgetBinding
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.dialog.PlayExploreWidgetFragment
import com.tokopedia.play.view.uimodel.action.DismissExploreWidget
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 22/05/23
 */
@Suppress("LateinitUsage")
class PlayChannelRecommendationFragment @Inject constructor(
    router: Router,
    private val trackingQueue: TrackingQueue,
    private val analyticFactory: PlayAnalytic2.Factory
) : DialogFragment() {

    private val fgExplore by lazyThreadSafetyNone {
        PlayExploreWidgetFragment(router, trackingQueue, analyticFactory)
    }

    private val fgCategory by lazyThreadSafetyNone {
        PlayCategoryWidgetFragment(router, trackingQueue, analyticFactory)
    }

    private val tabs = mutableMapOf<String, Fragment>()

    private val vpAdapter by lazyThreadSafetyNone {
        Adapter(childFragmentManager, lifecycle, tabs)
    }

    private val sheetCallback by lazyThreadSafetyNone {
        object : SideSheetCallback() {
            override fun onStateChanged(sheet: View, newState: Int) {
                if (newState == SideSheetBehavior.STATE_HIDDEN) dismiss()
            }

            override fun onSlide(sheet: View, slideOffset: Float) {}
        }
    }

    private var _binding: PlayDialogExploreWidgetBinding? = null
    private val binding: PlayDialogExploreWidgetBinding get() = _binding!!

    private var factory: Factory? = null
    private lateinit var viewModel: PlayViewModel

    private var analytic: PlayAnalytic2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vmFactory = factory?.getViewModelFactory() ?: ViewModelProvider(this)
        viewModel = vmFactory[PlayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlayDialogExploreWidgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeState()
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setWindowAnimations(playR.style.ExploreWidgetWindowAnim)
    }

    private fun setupTab(categories: List<String>) {
        if (categories.size > 1)
            tabs[categories.first()] = fgCategory
        tabs[categories.last()] = fgExplore

        TabsUnifyMediator(
            binding.tabPlayExploreWidget,
            binding.vpPlayExploreWidget
        ) { tab, position ->
            tab.setCustomText(tabs.keys.elementAtOrNull(position).orEmpty())
        }
    }

    fun moveTab(position: Int) {
        binding.vpPlayExploreWidget.setCurrentItem(position, true)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest { cachedState ->
                if (cachedState.isChanged { it.channel.channelRecomConfig } && cachedState.value.channel.channelRecomConfig.categoryWidgetConfig.hasCategory) {
                    binding.tabPlayExploreWidget.getUnifyTabLayout().getTabAt(0)
                        ?.setCustomText(viewModel.exploreWidgetTabs.first())
                    binding.tabPlayExploreWidget.getUnifyTabLayout().getTabAt(1)
                        ?.setCustomText(viewModel.exploreWidgetTabs.last())
                }

                if (analytic != null || cachedState.value.channel.channelInfo.id.isBlank()) return@collectLatest
                analytic = analyticFactory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = cachedState.value.channel.channelInfo
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val window = dialog?.window ?: return
        window.setGravity(Gravity.END)
        window.setLayout(
            (getScreenWidth() * 0.75).roundToInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupView() {
        binding.widgetHeader.title = getString(playR.string.play_explore_widget_header_title)
        binding.widgetHeader.closeListener = View.OnClickListener {
            analytic?.clickCloseExplore()
            dismiss()
        }
        binding.vpPlayExploreWidget.adapter = vpAdapter
        setupTab(viewModel.exploreWidgetTabs)
        binding.tabPlayExploreWidget.showWithCondition(tabs.size > 1)
        val sheetBehavior = SideSheetBehavior.from(binding.clSheetExploreWidget)
        sheetBehavior.addCallback(sheetCallback)
        sheetBehavior.state = SideSheetBehavior.STATE_EXPANDED
    }

    fun showNow(manager: FragmentManager) {
        if (!isAdded) showNow(manager, TAG)
    }

    fun setFactory(factory: Factory) {
        this.factory = factory
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

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    internal class Adapter(
        fragment: FragmentManager,
        lifecycle: Lifecycle,
        private val tabs: Map<String, Fragment>
    ) :
        FragmentStateAdapter(fragment, lifecycle) {
        override fun getItemCount(): Int = tabs.size

        override fun createFragment(position: Int): Fragment =
            tabs.values.elementAt(position)
    }

    interface Factory {
        fun getViewModelFactory(): ViewModelProvider
    }

    companion object {
        private const val TAG = "PlayChannelRecommendationFragment"
        fun get(fragmentManager: FragmentManager): PlayChannelRecommendationFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? PlayChannelRecommendationFragment
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayChannelRecommendationFragment {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayChannelRecommendationFragment::class.java.name
            ) as PlayChannelRecommendationFragment
        }
    }
}
