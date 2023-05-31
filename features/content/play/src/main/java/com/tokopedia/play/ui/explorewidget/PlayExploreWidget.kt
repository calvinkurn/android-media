package com.tokopedia.play.ui.explorewidget

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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.databinding.PlayDialogExploreWidgetBinding
import com.tokopedia.play.view.dialog.PlayExploreWidgetFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import javax.inject.Inject
import kotlin.math.roundToInt
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
    } // TODO() setup apply here only passed router

    private val fgCategory by lazyThreadSafetyNone {
        PlayCategoryWidgetFragment(router) //TODO() dont pass via constructor use lateint
    }

    private val tabs = mapOf<String, Fragment>(
        "Category" to fgCategory,
        "Eksplor" to PlayExploreWidgetFragment(router, trackingQueue, analyticFactory),
    )

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

    override fun onResume() {
        super.onResume()

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
            dismiss()
        }
        binding.vpPlayExploreWidget.adapter = vpAdapter
        TabsUnifyMediator(
            binding.tabPlayExploreWidget,
            binding.vpPlayExploreWidget
        ) { tab, position ->
            tab.setCustomText(tabs.keys.elementAt(position)) //TODO(): handle null or else
        }
        val sheetBehavior = SideSheetBehavior.from(binding.clSheetExploreWidget)
        sheetBehavior.addCallback(sheetCallback)
        sheetBehavior.state = SideSheetBehavior.STATE_EXPANDED
    }

    fun showNow(manager: FragmentManager) {
        if (!isAdded) showNow(manager, TAG)
    }

    override fun dismiss() {
        if (!isVisible) return
        super.dismiss()
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
