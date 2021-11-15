package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.state.PlayPartnerUiState
import com.tokopedia.play.view.uimodel.state.PlayUpcomingInfoUiState
import com.tokopedia.play.view.uimodel.state.PlayUpcomingState
import com.tokopedia.play.view.viewcomponent.ToolbarViewComponent
import com.tokopedia.play.view.viewcomponent.UpcomingActionButtonViewComponent
import com.tokopedia.play.view.viewcomponent.UpcomingTimerViewComponent
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play.view.viewmodel.PlayUpcomingViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.R as commonR
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
    private val analytic: PlayAnalytic
): TkpdBaseV4Fragment(),
    ToolbarViewComponent.Listener,
    UpcomingActionButtonViewComponent.Listener,
    UpcomingTimerViewComponent.Listener
{

    private val toolbarView by viewComponent { ToolbarViewComponent(it, R.id.view_toolbar, this) }
    private val upcomingTimer by viewComponent { UpcomingTimerViewComponent(it, R.id.view_upcoming_timer, this) }
    private val actionButton by viewComponent { UpcomingActionButtonViewComponent(it, R.id.btn_action, this) }

    private lateinit var ivUpcomingCover: AppCompatImageView
    private lateinit var tvUpcomingTitle: AppCompatTextView

    private lateinit var playUpcomingViewModel: PlayUpcomingViewModel
    private lateinit var playParentViewModel: PlayParentViewModel

    private val offset8 by lazy { requireContext().resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3) }

    override fun getScreenName(): String = "Play Upcoming"

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playUpcomingViewModel = ViewModelProvider(this, viewModelFactory).get(PlayUpcomingViewModel::class.java)

        val currentActivity = requireActivity()
        if (currentActivity is PlayActivity) {
            playParentViewModel = ViewModelProvider(currentActivity, currentActivity.getViewModelFactory()).get(PlayParentViewModel::class.java)
        }

        playUpcomingViewModel.initPage(channelId, playParentViewModel.getLatestChannelStorageData(channelId))
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
        sendImpression()
        initView(view)
        setupInsets()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        playUpcomingViewModel.startSSE(channelId)
    }

    private fun sendImpression() {
        playUpcomingViewModel.submitAction(ImpressUpcomingChannel)
    }

    private fun initView(view: View) {
        ivUpcomingCover = view.findViewById(R.id.iv_upcoming_cover)
        tvUpcomingTitle = view.findViewById(R.id.tv_upcoming_title)
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playUpcomingViewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                renderToolbarView(state.partner, state.share.shouldShow)
                renderUpcomingInfo(prevState?.upcomingInfo, state.upcomingInfo)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playUpcomingViewModel.uiEvent.collect { event ->
                when(event) {
                    is OpenPageEvent -> openApplink(applink = event.applink, params = event.params.toTypedArray(), requestCode = event.requestCode)
                    is CopyToClipboardEvent -> copyToClipboard(event.content)
                    is RemindMeEvent -> {
                        if(event.isSuccess) {
                            doShowToaster(message = getTextFromUiString(event.message))
                        }
                        else {
                            doShowToaster(
                                message = getTextFromUiString(event.message),
                                toasterType = Toaster.TYPE_ERROR,
                                actionText = getString(R.string.play_try_again)
                            ) { actionButton.onButtonClick() }
                        }
                    }
                    is ShowInfoEvent -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getTextFromUiString(event.message)
                        )
                    }
                    is ShowErrorEvent -> {
                        val errMessage = if (event.errMessage == null) {
                            ErrorHandler.getErrorMessage(
                                context, event.error, ErrorHandler.Builder()
                                    .className(PlayViewModel::class.java.simpleName)
                                    .build()
                            )
                        } else {
                            val (_, errCode) = ErrorHandler.getErrorMessagePair(
                                context, event.error, ErrorHandler.Builder()
                                    .className(PlayViewModel::class.java.simpleName)
                                    .build()
                            )
                            getString(
                                commonR.string.play_custom_error_handler_msg,
                                getTextFromUiString(event.errMessage),
                                errCode
                            )
                        }
                        doShowToaster(
                            toasterType = Toaster.TYPE_ERROR,
                            message = errMessage
                        )
                    }
                    RefreshChannel -> playParentViewModel.refreshChannel()
                    else -> { }
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
        partnerState: PlayPartnerUiState,
        isShareable: Boolean,
    ) {
        toolbarView.setFollowStatus(partnerState.followStatus)
        toolbarView.setPartnerName(partnerState.name)

        toolbarView.setIsShareable(isShareable)

        toolbarView.showCart(false)
    }

    private fun renderUpcomingInfo(prevState: PlayUpcomingInfoUiState?, currState: PlayUpcomingInfoUiState) {
        if(prevState?.generalInfo != currState.generalInfo) {
            currState.generalInfo.let {
                if(it.coverUrl.isNotEmpty())
                    Glide.with(requireView()).load(it.coverUrl).into(ivUpcomingCover)

                tvUpcomingTitle.text = it.title

                upcomingTimer.setupTimer(it.startTime)
            }
        }

        when(currState.state) {
            PlayUpcomingState.RemindMe -> {
                actionButton.setButtonStatus(UpcomingActionButtonViewComponent.Status.REMIND_ME)
            }
            PlayUpcomingState.WatchNow -> {
                upcomingTimer.stopTimer()
                actionButton.setButtonStatus(UpcomingActionButtonViewComponent.Status.WATCH_NOW)
            }
            PlayUpcomingState.Reminded, PlayUpcomingState.Unknown -> {
                actionButton.setButtonStatus(UpcomingActionButtonViewComponent.Status.HIDDEN)
            }
            else -> {}
        }
    }

    override fun onTimerFinish(view: UpcomingTimerViewComponent) {
        playUpcomingViewModel.submitAction(UpcomingTimerFinish)
    }

    override fun onClickActionButton() {
        playUpcomingViewModel.submitAction(ClickUpcomingButton)
    }

    override fun onBackButtonClicked(view: ToolbarViewComponent) {
        (requireActivity() as PlayActivity).onBackPressed(isSystemBack = false)
    }

    override fun onMoreButtonClicked(view: ToolbarViewComponent) { }

    override fun onFollowButtonClicked(view: ToolbarViewComponent) {
        playUpcomingViewModel.submitAction(ClickFollowUpcomingAction)
    }

    override fun onPartnerNameClicked(view: ToolbarViewComponent) {
        playUpcomingViewModel.submitAction(ClickPartnerNameUpcomingAction)
    }

    override fun onCartButtonClicked(view: ToolbarViewComponent) { }

    override fun onCopyButtonClicked(view: ToolbarViewComponent) {
        playUpcomingViewModel.submitAction(ClickShareUpcomingAction)

        analytic.clickCopyLink()
    }

    private fun copyToClipboard(content: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("play-room", content))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        playUpcomingViewModel.submitAction(
            OpenUpcomingPageResultAction(isSuccess = resultCode == Activity.RESULT_OK, requestCode = requestCode)
        )
        super.onActivityResult(requestCode, resultCode, data)
    }
}