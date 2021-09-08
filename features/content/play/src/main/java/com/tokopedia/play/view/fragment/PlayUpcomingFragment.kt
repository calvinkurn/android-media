package com.tokopedia.play.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.action.ClickFollowAction
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameAction
import com.tokopedia.play.view.uimodel.action.ClickRemindMeUpcomingChannel
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.viewcomponent.ToolbarViewComponent
import com.tokopedia.play.view.viewcomponent.UpcomingActionButtonViewComponent
import com.tokopedia.play.view.viewcomponent.UpcomingTimerViewComponent
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class PlayUpcomingFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dispatchers: CoroutineDispatchers,
    private val analytic: PlayAnalytic
): TkpdBaseV4Fragment(),
    ToolbarViewComponent.Listener,
    UpcomingActionButtonViewComponent.Listener
{

    private val toolbarView by viewComponent { ToolbarViewComponent(it, R.id.view_toolbar, this) }
    private val upcomingTimer by viewComponent { UpcomingTimerViewComponent(it, R.id.view_upcoming_timer) }
    private val actionButton by viewComponent { UpcomingActionButtonViewComponent(it, R.id.btn_action, this) }

    private lateinit var ivUpcomingCover: AppCompatImageView
    private lateinit var tvUpcomingTitle: AppCompatTextView

    private lateinit var playViewModel: PlayViewModel
    private lateinit var playParentViewModel: PlayParentViewModel

    private val offset8 by lazy { requireContext().resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3) }

    override fun getScreenName(): String = "Play Upcoming"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)

        val currentActivity = requireActivity()
        if (currentActivity is PlayActivity) {
            playParentViewModel = ViewModelProvider(currentActivity, currentActivity.getViewModelFactory()).get(PlayParentViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupInsets()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()

        playViewModel.upcomingInfo?.let {
            actionButton.setButtonStatus(
                if(!it.isReminderSet) UpcomingActionButtonViewComponent.Status.REMIND_ME
                else UpcomingActionButtonViewComponent.Status.HIDDEN
            )
        }
    }

    private fun initView(view: View) {
        ivUpcomingCover = view.findViewById(R.id.iv_upcoming_cover)
        tvUpcomingTitle = view.findViewById(R.id.tv_upcoming_title)
    }

    private fun setupView(view: View) {
        playViewModel.upcomingInfo?.let {
            if(it.coverUrl.isNotEmpty())
                Glide.with(view).load(it.coverUrl).into(ivUpcomingCover)

            tvUpcomingTitle.text = it.title

            upcomingTimer.setupTimer(it.startTime)
        }

        toolbarView.setShareInfo(playViewModel.latestCompleteChannelData.shareInfo)
    }

    private fun setupObserver() {
        playViewModel.observableUpcomingInfo.observe(viewLifecycleOwner) {
            if(it.isAlreadyLive) {
                upcomingTimer.stopTimer()
                actionButton.setButtonStatus(UpcomingActionButtonViewComponent.Status.WATCH_NOW)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                renderToolbarView(state.followStatus, state.partnerName)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiEvent.collect { event ->
                when(event) {
                    is OpenPageEvent -> openPageByApplink(applink = event.applink, params = event.params.toTypedArray(), requestCode = event.requestCode, pipMode = event.pipMode)
                    is RemindMeEvent -> {
                        if(event.isSuccess) {
                            actionButton.setButtonStatus(UpcomingActionButtonViewComponent.Status.HIDDEN)
                            doShowToaster(message = getTextFromUiString(event.message))
                        }
                        else {
                            actionButton.setButtonStatus(UpcomingActionButtonViewComponent.Status.REMIND_ME)
                            doShowToaster(
                                message = getTextFromUiString(event.message),
                                toasterType = Toaster.TYPE_ERROR,
                                actionText = getString(R.string.play_try_again)
                            ) { actionButton.onButtonClick() }
                        }
                    }
                }
            }
        }
    }

    private fun getTextFromUiString(uiString: UiString): String {
        return when (uiString) {
            is UiString.Text -> uiString.text
            is UiString.Resource -> getString(uiString.resource)
        }
    }

    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false, pipMode: Boolean = false) {
        if (pipMode && playViewModel.isPiPAllowed && !playViewModel.isFreezeOrBanned) {
            playViewModel.requestPiPBrowsingPage(
                OpenApplinkUiModel(applink = applink, params = params.toList(), requestCode, shouldFinish)
            )
        } else {
            openApplink(applink, *params, requestCode = requestCode, shouldFinish = shouldFinish)
        }
    }

    private fun openApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
        if (requestCode == null) {
            RouteManager.route(context, applink, *params)
        } else {
            val intent = RouteManager.getIntent(context, applink, *params)
            startActivityForResult(intent, requestCode)
        }
        activity?.overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) activity?.finish()
    }

    private fun setupInsets() {
        toolbarView.rootView.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams

            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun renderToolbarView(
        followStatus: PlayPartnerFollowStatus,
        partnerName: String
    ) {
        toolbarView.setFollowStatus(followStatus)
        toolbarView.setPartnerName(partnerName)
    }

    override fun onClickActionButton() {
        playViewModel.observableUpcomingInfo.value?.let {
            if(it.isAlreadyLive) playParentViewModel.refreshChannel()
            else playViewModel.submitAction(ClickRemindMeUpcomingChannel)
        }
    }

    override fun onBackButtonClicked(view: ToolbarViewComponent) {
        (requireActivity() as PlayActivity).onBackPressed(isSystemBack = false)
    }

    override fun onMoreButtonClicked(view: ToolbarViewComponent) {
        TODO("Not yet implemented")
    }

    override fun onFollowButtonClicked(view: ToolbarViewComponent) {
        playViewModel.submitAction(ClickFollowAction)
    }

    override fun onPartnerNameClicked(view: ToolbarViewComponent) {
        playViewModel.submitAction(ClickPartnerNameAction)
    }

    override fun onCartButtonClicked(view: ToolbarViewComponent) {
        TODO("Not yet implemented")
    }

    override fun onCopyButtonClicked(view: ToolbarViewComponent, content: String) {
        copyToClipboard(content)
        showLinkCopiedToaster()

        analytic.clickCopyLink()
    }

    private fun copyToClipboard(content: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("play-room", content))
    }

    private fun showLinkCopiedToaster() {
        doShowToaster(message = getString(R.string.play_link_copied))
    }

    private fun doShowToaster(
        toasterType: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        message: String,
        onClick: ((View) -> Unit) = { }
    ) {
        Toaster.toasterCustomBottomHeight = if(toasterType == Toaster.TYPE_ERROR) actionButton.rootView.height + offset8 else 0
        Toaster.build(
            requireView(),
            message,
            type = toasterType,
            actionText = actionText,
            clickListener = onClick
        ).show()
    }
}