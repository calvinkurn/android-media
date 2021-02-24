package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.ImpressionableModel
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.user.session.UserSession

class DiscoveryPlayWidgetViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), PlayWidgetListener {
    private lateinit var discoveryPlayWidgetViewModel: DiscoveryPlayWidgetViewModel
    private val playWidgetViewHolder: PlayWidgetViewHolder = PlayWidgetViewHolder(itemView, PlayWidgetCoordinator(fragment.viewLifecycleOwner).apply {
        setListener(this@DiscoveryPlayWidgetViewHolder)
    })

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        discoveryPlayWidgetViewModel = discoveryBaseViewModel as DiscoveryPlayWidgetViewModel
        if (!discoveryBaseViewModel.isPlayWidgetToolsInitialized())
            getSubComponent().inject(discoveryPlayWidgetViewModel)
        discoveryBaseViewModel.getPlayWidgetData()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoveryPlayWidgetViewModel.getPlayWidgetUILiveData().observe(it, { uiModel ->
                if (uiModel != null) {
                    playWidgetViewHolder.bind(uiModel, this)
//                TODO:: Impression GTM
                    if (uiModel is ImpressionableModel)
                        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetImpression(discoveryPlayWidgetViewModel.components, UserSession(fragment.context).userId)
                } else {
                    val playWidgetView = itemView as PlayWidgetView
                    playWidgetView.removeAllViews()
                }
            })

        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoveryPlayWidgetViewModel.getPlayWidgetUILiveData().removeObservers(it)
        }
    }

    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        discoveryPlayWidgetViewModel.hitPlayWidgetService()
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        if (fragment is DiscoveryFragment) {
            fragment.openPlay(discoveryPlayWidgetViewModel.position, appLink)
//        TODO::CLICK GTM
            fragment.getDiscoveryAnalytics().trackPlayWidgetClick(discoveryPlayWidgetViewModel.components, UserSession(fragment.context).userId, appLink)
        }
    }

}