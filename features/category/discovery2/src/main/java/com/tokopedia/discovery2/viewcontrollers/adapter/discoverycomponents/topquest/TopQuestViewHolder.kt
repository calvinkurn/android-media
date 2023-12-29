package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.TopQuestWidgetAnalytics
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.listeners.QuestWidgetStatusCallback
import com.tokopedia.quest_widget.tracker.QuestSource
import com.tokopedia.quest_widget.tracker.QuestTrackerImpl
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.quest_widget.view.QuestWidgetView

class TopQuestViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner), QuestWidgetCallbacks, QuestWidgetStatusCallback {

    private var viewModel: TopQuestViewModel? = null
    private val questWidget: QuestWidgetView = itemView.findViewById(R.id.questWidget)

    init {
        questWidget.setupListeners(this)
        questWidget.setTrackerImpl(getAnalytics())
        questWidget.setQuestWidgetStatusCallback(this)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as TopQuestViewModel
        viewModel?.let {
            getSubComponent().inject(it)
        }
        questWidget.getQuestList(
            page = "${QuestWidgetLocations.DISCO}-${viewModel?.components?.pageEndPoint}",
            source = QuestSource.DISCO,
            position = adapterPosition
        )
    }

    override fun questLogin() {
        viewModel?.position?.let { (fragment as DiscoveryFragment).openLoginScreen(it) }
    }

    override fun deleteQuestWidget() {
    }

    override fun updateQuestWidget(position: Int) {
        viewModel?.shouldUpdate = true
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.navigateData?.observe(it) { applink ->
                if (!applink.isNullOrEmpty() && fragment.activity != null) {
                    RouteManager.route(fragment.activity, applink)
                }
            }
        }
        lifecycleOwner?.let {
            viewModel?.updateQuestData?.observe(it) { shouldUpdate ->
                if (shouldUpdate) {
                    questWidget.getQuestList(
                        page = "${QuestWidgetLocations.DISCO}-${viewModel?.components?.pageEndPoint}",
                        source = QuestSource.DISCO,
                        position = adapterPosition
                    )
                }
            }
            viewModel?.hideSectionLD?.observe(it) { sectionId ->
                (fragment as DiscoveryFragment).handleHideSection(sectionId)
            }
            viewModel?.shouldHideWidget?.observe(it) { shouldHideWidget ->
                if (shouldHideWidget) {
                    questWidget.hide()
                } else {
                    questWidget.show()
                }
            }
            viewModel?.getSyncPageLiveData()?.observe(it) { shouldSync ->
                if (shouldSync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.navigateData?.removeObservers(it)
            viewModel?.updateQuestData?.removeObservers(it)
            viewModel?.hideSectionLD?.removeObservers(it)
            viewModel?.shouldHideWidget?.removeObservers(it)
            viewModel?.getSyncPageLiveData()?.removeObservers(it)
        }
    }

    private fun getAnalytics(): QuestTrackerImpl {
        return TopQuestWidgetAnalytics((fragment as DiscoveryFragment).getDiscoveryAnalytics())
    }

    override fun getQuestWidgetStatus(status: LiveDataResult.STATUS) {
        viewModel?.handleWidgetStatus(status)
    }
}
