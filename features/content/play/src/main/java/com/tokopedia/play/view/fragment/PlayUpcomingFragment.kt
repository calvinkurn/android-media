package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.databinding.FragmentPlayUpcomingBinding
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.PlayUpcomingUiEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.state.DescriptionUiState
import com.tokopedia.play.view.uimodel.state.PlayUpcomingInfoUiState
import com.tokopedia.play.view.uimodel.state.PlayUpcomingState
import com.tokopedia.play.view.viewcomponent.ShareExperienceViewComponent
import com.tokopedia.play.view.viewcomponent.ToolbarRoomViewComponent
import com.tokopedia.play.view.viewcomponent.UpcomingActionButtonViewComponent
import com.tokopedia.play.view.viewcomponent.UpcomingTimerViewComponent
import com.tokopedia.play.view.viewcomponent.UpcomingDescriptionViewComponent
import com.tokopedia.play.view.viewcomponent.partnerinfo.PartnerInfoViewComponent
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play.view.viewmodel.PlayUpcomingViewModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.recreateView
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.ShareModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class PlayUpcomingFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dispatchers: CoroutineDispatchers,
    private val analytic: PlayNewAnalytic,
    private val router: Router,
): TkpdBaseV4Fragment(),
    ToolbarRoomViewComponent.Listener,
    PartnerInfoViewComponent.Listener,
    UpcomingActionButtonViewComponent.Listener,
    UpcomingTimerViewComponent.Listener,
    ShareExperienceViewComponent.Listener,
    UpcomingDescriptionViewComponent.Listener
{

    private val toolbarView by viewComponent { ToolbarRoomViewComponent(it, R.id.view_toolbar_room, this) }
    private val partnerInfoView by viewComponent { PartnerInfoViewComponent(it, this) }
    private val upcomingTimer by viewComponent { UpcomingTimerViewComponent(it, R.id.view_upcoming_timer, this) }
    private val actionButton by viewComponent { UpcomingActionButtonViewComponent(it, R.id.btn_action, this) }
    private val shareExperienceView by viewComponent { ShareExperienceViewComponent(it, R.id.view_upcoming_share_experience, childFragmentManager, this, this, requireContext(), dispatchers) }
    private val description by viewComponent { UpcomingDescriptionViewComponent(it, R.id.tv_upcoming_description, this) }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(it.requireView(), it.viewLifecycleOwner) },
    )

    private lateinit var playUpcomingViewModel: PlayUpcomingViewModel
    private lateinit var playParentViewModel: PlayParentViewModel

    private val offset8 by lazy { requireContext().resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3) }

    override fun getScreenName(): String = "Play Upcoming"

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    private var _binding: FragmentPlayUpcomingBinding? = null
    private val binding: FragmentPlayUpcomingBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playUpcomingViewModel = ViewModelProvider(this, viewModelFactory).get(PlayUpcomingViewModel::class.java)

        val currentActivity = requireActivity()
        if (currentActivity is PlayActivity) {
            playParentViewModel = ViewModelProvider(currentActivity, currentActivity.getViewModelFactory()).get(PlayParentViewModel::class.java)
        }
    }

    private fun setupPage(){
        try {
            playUpcomingViewModel.initPage(channelId, playParentViewModel.getLatestChannelStorageData(channelId))
        }
        catch (e: Exception){}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayUpcomingBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInsets()
        setupObserver()
        setupTapCover()
    }

    override fun onResume() {
        super.onResume()
        setupPage()
        playUpcomingViewModel.startSSE(channelId)
    }

    override fun onPause() {
        super.onPause()
        try {
            playParentViewModel.setLatestChannelStorageData(
                channelId, playUpcomingViewModel.latestChannelData
            )
            sendImpression()
        }
        catch (e: Exception) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreateView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        shareExperienceView.handleRequestPermissionResult(requestCode, grantResults)
    }

    private fun sendImpression() {
        analytic.impressUpcomingPage(channelId)
        if(!playUpcomingViewModel.isWidgetShown) analytic.impressCoverWithoutComponent(channelId)
        if(playUpcomingViewModel.isWidgetShown) analytic.impressDescription(channelId)
    }

    private fun renderDescription(prevState: DescriptionUiState?, state: DescriptionUiState){
        if(prevState?.isExpand != state.isExpand) {
            description.setupExpand(state.isExpand)
            binding.vOverlay.showWithCondition(state.isExpand)
        }
        if(prevState?.isShown != state.isShown) description.rootView.showWithCondition(state.isShown)
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playUpcomingViewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                renderToolbarView(state.channel)
                renderPartnerInfoView(prevState?.partner, state.partner)
                renderShareView(prevState?.channel, state.channel)
                renderUpcomingInfo(prevState?.upcomingInfo, state.upcomingInfo)
                renderDescription(prevState?.description, state.description)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playUpcomingViewModel.uiEvent.collect { event ->
                when(event) {
                    is PlayUpcomingUiEvent.OpenPageEvent -> openApplink(applink = event.applink, params = event.params.toTypedArray(), requestCode = event.requestCode)
                    is PlayUpcomingUiEvent.CopyToClipboardEvent -> copyToClipboard(event.content)
                    is PlayUpcomingUiEvent.RemindMeEvent -> {
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
                    is PlayUpcomingUiEvent.ShowInfoEvent -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getTextFromUiString(event.message)
                        )
                    }
                    is PlayUpcomingUiEvent.ShowInfoWithActionEvent -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getTextFromUiString(event.message),
                            actionText = getString(R.string.play_action_ok)
                        ) { event.action() }
                    }
                    PlayUpcomingUiEvent.RefreshChannelEvent -> playParentViewModel.refreshChannel()
                    is PlayUpcomingUiEvent.SaveTemporarySharingImage -> shareExperienceView.saveTemporaryImage(event.imageUrl)
                    is PlayUpcomingUiEvent.OpenSharingOptionEvent -> {
                        shareExperienceView.showSharingOptions(event.title, event.coverUrl, event.userId, event.channelId)
                    }
                    is PlayUpcomingUiEvent.OpenSelectedSharingOptionEvent -> {
                        SharingUtil.executeShareIntent(event.shareModel, event.linkerShareResult, activity, view, event.shareString)
                    }
                    PlayUpcomingUiEvent.CloseShareExperienceBottomSheet -> shareExperienceView.dismiss()
                    PlayUpcomingUiEvent.ErrorGenerateShareLink -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getString(R.string.play_sharing_error_generate_link),
                            actionText = getString(R.string.play_sharing_refresh)
                        )
                    }
                    is PlayUpcomingUiEvent.ShowError -> toaster.showError(event.error)
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
            router.route(context, applink, *params)
        } else {
            val intent = router.getIntent(context, applink, *params)
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
        channel: PlayChannelDetailUiModel,
    ) {
        toolbarView.setTitle(channel.channelInfo.title)
    }

    private fun renderPartnerInfoView(
        prevState: PlayPartnerInfo?,
        state: PlayPartnerInfo
    ) {
        if (prevState == state) return
        partnerInfoView.setInfo(state)
    }

    private fun renderShareView(
        prevState: PlayChannelDetailUiModel?,
        state: PlayChannelDetailUiModel,
    ) {
        if(prevState != state) shareExperienceView.setIsShareable(state.shareInfo.shouldShow)
    }

    private fun renderUpcomingInfo(prevState: PlayUpcomingInfoUiState?, currState: PlayUpcomingInfoUiState) {
        if(prevState?.info != currState.info) {
            currState.info.let {
                if(it.coverUrl.isNotEmpty()) {
                    binding.ivUpcomingCover.setImageUrl(it.coverUrl)
                }
                description.setupText(it.description)
                upcomingTimer.setupTimer(it.startTime)
            }
        }
        if(currState.state is PlayUpcomingState.WatchNow) upcomingTimer.stopTimer()
        actionButton.setButtonStatus(currState.state)
    }

    override fun onTimerFinish(view: UpcomingTimerViewComponent) {
        playUpcomingViewModel.submitAction(UpcomingTimerFinish)
    }

    override fun onClickActionButton() {
        handleUpcomingClickAnalytic()
        playUpcomingViewModel.submitAction(ClickUpcomingButton)
    }

    private fun handleUpcomingClickAnalytic(){
        when (val status = playUpcomingViewModel.remindState) {
            is PlayUpcomingState.ReminderStatus -> {
                if(status.isReminded)
                    analytic.clickCancelRemindMe(channelId)
                else
                    analytic.clickRemindMe(channelId)
            }
            PlayUpcomingState.WatchNow -> {
                analytic.clickWatchNow(channelId)
            }
            else -> {}
        }
    }

    override fun onBackButtonClicked(view: ToolbarRoomViewComponent) {
        (requireActivity() as PlayActivity).onBackPressed(isSystemBack = false)
    }

    override fun onPartnerInfoClicked(view: PartnerInfoViewComponent, applink: String) {
        playUpcomingViewModel.submitAction(ClickPartnerNameUpcomingAction(applink))
    }

    override fun onFollowImpressed(view: PartnerInfoViewComponent) {
        analytic.impressFollow(channelId)
    }

    override fun onFollowButtonClicked(view: PartnerInfoViewComponent) {
        analytic.clickFollowUniversal(channelId)
        playUpcomingViewModel.submitAction(ClickFollowUpcomingAction)
    }

    override fun onShareIconClick(view: ShareExperienceViewComponent) {
        analytic.clickShareButton(channelId, playUpcomingViewModel.partnerId, playUpcomingViewModel.channelType.value)
        playUpcomingViewModel.submitAction(ClickShareUpcomingAction)
    }

    override fun onShareOpenBottomSheet(view: ShareExperienceViewComponent) {
        playUpcomingViewModel.submitAction(ShowShareExperienceUpcomingAction)
        if(playUpcomingViewModel.isCustomSharingAllowed) analytic.impressShareBottomSheet(channelId, playUpcomingViewModel.partnerId, playUpcomingViewModel.channelType.value)
    }

    override fun onShareOptionClick(view: ShareExperienceViewComponent, shareModel: ShareModel) {
        analytic.clickSharingOption(channelId, playUpcomingViewModel.partnerId, playUpcomingViewModel.channelType.value, shareModel.channel, playUpcomingViewModel.isSharingBottomSheet)
        playUpcomingViewModel.submitAction(ClickSharingOptionUpcomingAction(shareModel))
    }

    override fun onShareOptionClosed(view: ShareExperienceViewComponent) {
        analytic.closeShareBottomSheet(channelId, playUpcomingViewModel.partnerId, playUpcomingViewModel.channelType.value, playUpcomingViewModel.isSharingBottomSheet)
    }

    override fun onScreenshotTaken(view: ShareExperienceViewComponent) {
        playUpcomingViewModel.submitAction(ScreenshotTakenUpcomingAction)
        if(playUpcomingViewModel.isCustomSharingAllowed) analytic.takeScreenshotForSharing(channelId, playUpcomingViewModel.partnerId, playUpcomingViewModel.channelType.value)
    }

    override fun onSharePermissionAction(view: ShareExperienceViewComponent, label: String) {
        analytic.clickSharePermission(channelId, playUpcomingViewModel.partnerId, playUpcomingViewModel.channelType.value, label)
    }

    override fun onHandleShareFallback(view: ShareExperienceViewComponent) {
        playUpcomingViewModel.submitAction(CopyLinkUpcomingAction)
    }

    override fun onShareIconImpressed(view: ShareExperienceViewComponent) {
        analytic.impressShare(channelId)
    }

    override fun onTextClicked(view: UpcomingDescriptionViewComponent) {
        if(playUpcomingViewModel.isExpanded) analytic.clickSeeLessDescription(channelId) else analytic.clickSeeAllDescription(channelId)
        playUpcomingViewModel.submitAction(ExpandDescriptionUpcomingAction)
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

    fun setResultBeforeFinish() {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            if (channelId.isNotEmpty()) putExtra(EXTRA_CHANNEL_ID, channelId)
            putExtra(EXTRA_IS_REMINDER, playUpcomingViewModel.isReminderSet)
        })
    }

    private fun setupTapCover(){
        binding.ivUpcomingCover.setOnClickListener {
            playUpcomingViewModel.submitAction(TapCover)
            if (!playUpcomingViewModel.isExpanded) {
                analytic.clickCover(channelId)
            } else return@setOnClickListener
        }
    }

    companion object {
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val EXTRA_IS_REMINDER = "EXTRA_IS_REMINDER"
    }
}
