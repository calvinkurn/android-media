package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession

class DiscoveryPlayWidgetViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), PlayWidgetListener, PlayWidgetInListAnalyticListener {
    private var discoveryPlayWidgetViewModel: DiscoveryPlayWidgetViewModel? = null
    private val playWidgetViewHolder: PlayWidgetViewHolder = PlayWidgetViewHolder(
        itemView,
        PlayWidgetCoordinator(fragment).apply {
            setListener(this@DiscoveryPlayWidgetViewHolder)
            setAnalyticListener(DefaultPlayWidgetInListAnalyticListener(this@DiscoveryPlayWidgetViewHolder))
        }
    )

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        discoveryPlayWidgetViewModel = discoveryBaseViewModel as DiscoveryPlayWidgetViewModel
        if (!discoveryBaseViewModel.isPlayWidgetToolsInitialized()) {
            discoveryPlayWidgetViewModel?.let {
                getSubComponent().inject(it)
            }
        }
        discoveryBaseViewModel.getPlayWidgetData()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoveryPlayWidgetViewModel?.getPlayWidgetUILiveData()?.observe(it) { uiModel ->
                if (uiModel != null) {
                    playWidgetViewHolder.bind(uiModel, this)
                } else {
                    val playWidgetView = itemView as PlayWidgetView
                    playWidgetView.removeAllViews()
                }
            }
            discoveryPlayWidgetViewModel?.reminderObservable?.observe(it) { result ->
                when (result) {
                    is Success -> showToast(
                        when (result.data) {
                            PlayWidgetReminderType.Reminded -> fragment.getString(com.tokopedia.play.widget.R.string.play_widget_success_add_reminder)
                            PlayWidgetReminderType.NotReminded -> fragment.getString(com.tokopedia.play.widget.R.string.play_widget_success_remove_reminder)
                        },
                        Toaster.TYPE_NORMAL
                    )

                    is Fail -> showToast(fragment.getString(com.tokopedia.play.widget.R.string.play_widget_error_reminder), Toaster.TYPE_ERROR)
                }
            }
            discoveryPlayWidgetViewModel?.reminderLoginEvent?.observe(it) { shouldLogin ->
                if (shouldLogin) {
                    discoveryPlayWidgetViewModel?.position?.let { it1 -> (fragment as DiscoveryFragment).openLoginScreen(it1) }
                }
            }

            discoveryPlayWidgetViewModel?.getSyncPageLiveData()?.observe(fragment.viewLifecycleOwner) {
                if (it) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }
            discoveryPlayWidgetViewModel?.hideSectionLD?.observe(fragment.viewLifecycleOwner) { sectionId ->
                (fragment as DiscoveryFragment).handleHideSection(sectionId)
            }
        }
    }

    private fun showToast(message: String, type: Int = Toaster.TYPE_NORMAL) {
        fragment.activity?.let {
            SnackbarManager.getContentView(it)
        }?.let { contentView ->
            Toaster.build(contentView, message, Toast.LENGTH_SHORT, type).show()
        }
    }

    override fun onToggleReminderClicked(view: PlayWidgetMediumView, channelId: String, reminderType: PlayWidgetReminderType, position: Int) {
        discoveryPlayWidgetViewModel?.shouldUpdatePlayWidgetToggleReminder(channelId, reminderType)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoveryPlayWidgetViewModel?.getPlayWidgetUILiveData()?.removeObservers(it)
            discoveryPlayWidgetViewModel?.reminderObservable?.removeObservers(it)
            discoveryPlayWidgetViewModel?.reminderLoginEvent?.removeObservers(it)
            discoveryPlayWidgetViewModel?.getSyncPageLiveData()?.removeObservers(it)
            discoveryPlayWidgetViewModel?.hideSectionLD?.removeObservers(it)
        }
    }

    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        discoveryPlayWidgetViewModel?.hitPlayWidgetService()
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        if (fragment is DiscoveryFragment) {
            discoveryPlayWidgetViewModel?.position?.let { fragment.openPlay(it, appLink) }
        }
    }

    override fun onImpressPlayWidget(
        view: PlayWidgetView,
        item: PlayWidgetUiModel,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
    }

    override fun onClickChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetClick(
                it,
                UserSession(fragment.context).userId,
                item.channelId,
                item.appLink,
                "",
                verticalWidgetPosition,
                channelPositionInList,
                config.autoPlay
            )
        }
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetClick(
                it,
                UserSession(fragment.context).userId,
                item.channelId,
                item.appLink,
                item.partner.id,
                verticalWidgetPosition,
                channelPositionInList,
                config.autoPlay
            )
        }
    }

    override fun onImpressChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetImpression(
                it,
                UserSession(fragment.context).userId,
                item.channelId,
                "",
                verticalWidgetPosition,
                channelPositionInList,
                config.autoPlay
            )
        }
    }

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetImpression(
                it,
                UserSession(fragment.context).userId,
                item.channelId,
                item.partner.id,
                verticalWidgetPosition,
                channelPositionInList,
                config.autoPlay
            )
        }
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView, verticalWidgetPosition: Int) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetBannerClick(
                it,
                UserSession(fragment.context).userId,
                verticalWidgetPosition
            )
        }
    }

    override fun onClickBannerCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetBannerClick(
                it,
                UserSession(fragment.context).userId,
                verticalWidgetPosition
            )
        }
    }

    override fun onClickViewAll(view: PlayWidgetSmallView, verticalWidgetPosition: Int) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetLihatSemuaClick(
                it,
                UserSession(fragment.context).userId,
                verticalWidgetPosition
            )
        }
    }

    override fun onClickViewAll(view: PlayWidgetMediumView, verticalWidgetPosition: Int) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetLihatSemuaClick(
                it,
                UserSession(fragment.context).userId,
                verticalWidgetPosition
            )
        }
    }

    override fun onImpressOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackPlayWidgetOverLayImpression(
                    it,
                    UserSession(fragment.context).userId,
                    verticalWidgetPosition,
                    channelPositionInList,
                    item.overlayImageAppLink
                )
        }
    }

    override fun onClickOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetOverLayClick(
                it,
                UserSession(fragment.context).userId,
                verticalWidgetPosition,
                channelPositionInList,
                item.overlayImageAppLink
            )
        }
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int,
        config: PlayWidgetConfigUiModel
    ) {
        discoveryPlayWidgetViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPlayWidgetReminderClick(
                it,
                UserSession(fragment.context).userId,
                verticalWidgetPosition,
                channelPositionInList,
                item.channelId,
                isRemindMe
            )
        }
    }
}
