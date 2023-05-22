package com.tokopedia.play.ui.explorewidget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.databinding.PlayDialogExploreWidgetBinding
import com.tokopedia.play.view.dialog.PlayExploreWidgetFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import javax.inject.Inject

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
    } // setup apply here

    private val tabs = mapOf<String, Fragment>(
        "Category" to PlayExploreWidgetFragment(router, trackingQueue, analyticFactory),
        "Eksplor" to PlayExploreWidgetFragment(router, trackingQueue, analyticFactory),
    )

    private val vpAdapter by lazyThreadSafetyNone {
        Adapter(requireActivity(), tabs)
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

    private fun setupView() {
        binding.vpPlayExploreWidget.adapter = vpAdapter
        TabsUnifyMediator(
            binding.tabPlayExploreWidget,
            binding.vpPlayExploreWidget
        ) { tab, position ->
            tab.setCustomText(tabs.keys.elementAt(position)) //handle null or else
        }
    }

    fun showNow(manager: FragmentManager) {
        if (!isAdded) showNow(manager, TAG)
    }

    internal class Adapter(
        fragment: FragmentActivity,
        private val tabs: Map<String, Fragment>
    ) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = tabs.size

        override fun createFragment(position: Int): Fragment =
            tabs.values.elementAt(position) //handle null or else
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
