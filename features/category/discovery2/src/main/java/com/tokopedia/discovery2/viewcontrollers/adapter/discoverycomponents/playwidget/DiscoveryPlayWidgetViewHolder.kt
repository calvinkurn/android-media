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
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.user.session.UserSession

class DiscoveryPlayWidgetViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), PlayWidgetListener, PlayWidgetInListAnalyticListener {
    private lateinit var discoveryPlayWidgetViewModel: DiscoveryPlayWidgetViewModel
    private val playWidgetViewHolder: PlayWidgetViewHolder = PlayWidgetViewHolder(itemView, PlayWidgetCoordinator(fragment.viewLifecycleOwner).apply {
        setListener(this@DiscoveryPlayWidgetViewHolder)
        setAnalyticListener(DefaultPlayWidgetInListAnalyticListener(this@DiscoveryPlayWidgetViewHolder))
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
        }
    }

    override fun onImpressPlayWidget(view: PlayWidgetView, item: PlayWidgetUiModel, verticalWidgetPosition: Int, businessWidgetPosition: Int) {

    }

    override fun onClickChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean, verticalWidgetPosition: Int, businessWidgetPosition: Int) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetClick(discoveryPlayWidgetViewModel.components, UserSession(fragment.context).userId, item.channelId, item.appLink, verticalWidgetPosition, channelPositionInList, isAutoPlay)
    }

    override fun onClickChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean, verticalWidgetPosition: Int, businessWidgetPosition: Int) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetClick(discoveryPlayWidgetViewModel.components, UserSession(fragment.context).userId, item.channelId, item.appLink, verticalWidgetPosition, channelPositionInList, isAutoPlay)
    }

    override fun onImpressChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean, verticalWidgetPosition: Int, businessWidgetPosition: Int) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetImpression(discoveryPlayWidgetViewModel.components, UserSession(fragment.context).userId, item.channelId, verticalWidgetPosition, channelPositionInList, isAutoPlay)
    }

    override fun onImpressChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean, verticalWidgetPosition: Int, businessWidgetPosition: Int) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetImpression(discoveryPlayWidgetViewModel.components, UserSession(fragment.context).userId, item.channelId, verticalWidgetPosition, channelPositionInList, isAutoPlay)
    }
}