package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetDiscoveryAnalyticModel
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class DiscoveryPlayWidgetViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), PlayWidgetListener {
    private lateinit var discoveryPlayWidgetViewModel: DiscoveryPlayWidgetViewModel
    private val playWidgetViewHolder: PlayWidgetViewHolder = PlayWidgetViewHolder(itemView, PlayWidgetCoordinator(fragment).apply {
        setListener(this@DiscoveryPlayWidgetViewHolder)
        setAnalyticModel(PlayWidgetDiscoveryAnalyticModel())
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
            discoveryPlayWidgetViewModel.reminderObservable.observe(it, { result ->
                when (result) {
                    is Success -> showToast(
                            when (result.data) {
                                PlayWidgetReminderType.Reminded -> fragment.getString(com.tokopedia.play.widget.R.string.play_widget_success_add_reminder)
                                PlayWidgetReminderType.NotReminded -> fragment.getString(com.tokopedia.play.widget.R.string.play_widget_success_remove_reminder)
                            }, Toaster.TYPE_NORMAL)
                    is Fail -> showToast(fragment.getString(com.tokopedia.play.widget.R.string.play_widget_error_reminder), Toaster.TYPE_ERROR)
                }
            })
            discoveryPlayWidgetViewModel.reminderLoginEvent.observe(it, { shouldLogin ->
                if (shouldLogin)
                    (fragment as DiscoveryFragment).openLoginScreen(discoveryPlayWidgetViewModel.position)
            })

        }
    }

    private fun showToast(message: String, type: Int = Toaster.TYPE_NORMAL) {
        Toaster.build(itemView, message, Toast.LENGTH_SHORT, type).show()
    }

    override fun onToggleReminderClicked(view: PlayWidgetMediumView, channelId: String, reminderType: PlayWidgetReminderType, position: Int) {
        discoveryPlayWidgetViewModel.shouldUpdatePlayWidgetToggleReminder(channelId, reminderType)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoveryPlayWidgetViewModel.getPlayWidgetUILiveData().removeObservers(it)
            discoveryPlayWidgetViewModel.reminderObservable.removeObservers(it)
            discoveryPlayWidgetViewModel.reminderLoginEvent.removeObservers(it)
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
}