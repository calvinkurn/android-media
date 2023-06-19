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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.databinding.PlayDialogExploreWidgetBinding
import com.tokopedia.play.view.dialog.PlayExploreWidgetFragment
import com.tokopedia.play.view.uimodel.action.DismissExploreWidget
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import javax.inject.Inject
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 22/05/23
 */
class PlayExploreWidget @Inject constructor(
    router: Router,
    trackingQueue: TrackingQueue,
    analyticFactory: PlayAnalytic2.Factory
) : DialogFragment() {

    private val fgExplore by lazyThreadSafetyNone {
        PlayExploreWidgetFragment(router, trackingQueue, analyticFactory)
    }

    private val fgCategory by lazyThreadSafetyNone {
        PlayCategoryWidgetFragment(router)
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
    }

    fun setupTab(categories: List<String>) {
        if(categories.size > 1)
            tabs[categories.first()] = fgCategory
        tabs[categories.last()] = fgExplore
    }

    fun moveTab(position: Int) {
        binding.vpPlayExploreWidget.setCurrentItem(position, true)
    }

    override fun onStart() {
        super.onStart()

        val window = dialog?.window ?: return
        window.setGravity(Gravity.END)
        window.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupView() {
        binding.widgetHeader.title = getString(playR.string.play_explore_widget_header_title)
        binding.widgetHeader.closeListener = View.OnClickListener {
            dismiss()
        }
        binding.vpPlayExploreWidget.adapter = vpAdapter
        TabsUnifyMediator(
            binding.tabPlayExploreWidget,
            binding.vpPlayExploreWidget
        ) { tab, position ->
            tab.setCustomText(tabs.keys.elementAtOrNull(position).orEmpty())
        }
        binding.tabPlayExploreWidget.showWithCondition(tabs.size > 1)
        val sheetBehavior = SideSheetBehavior.from(binding.clSheetExploreWidget)
        sheetBehavior.addCallback(sheetCallback)
        sheetBehavior.state = SideSheetBehavior.STATE_EXPANDED
    }

    fun showNow(manager: FragmentManager) {
        if (!isAdded) showNow(manager, TAG)
    }

    fun setFactory(factory: Factory){
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
            tabs.values.elementAt(position) //TODO() handle null or else
    }

    interface Factory {
        fun getViewModelFactory() : ViewModelProvider
    }

    companion object {
        private const val TAG = "PlayExploreWidget"
        fun get(fragmentManager: FragmentManager): PlayExploreWidget? {
            return fragmentManager.findFragmentByTag(TAG) as? PlayExploreWidget
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayExploreWidget {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayExploreWidget::class.java.name
            ) as PlayExploreWidget
        }
    }
}
