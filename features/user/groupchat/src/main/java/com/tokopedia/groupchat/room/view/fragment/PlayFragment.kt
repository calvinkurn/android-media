package com.tokopedia.groupchat.room.view.fragment

import android.app.Activity
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.channel.view.activity.ChannelActivity
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.room.di.DaggerPlayComponent
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.presenter.PlayPresenter
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.groupchat.room.view.viewstate.PlayViewState
import com.tokopedia.groupchat.room.view.viewstate.PlayViewStateImpl
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamInt
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.sharedata.DefaultShareData
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author : Steven 11/02/19
 */

class PlayFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(), PlayContract.View,
        ChatroomContract.QuickReply,
        ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener,
        ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener,
        ChatroomContract.ChatItem.SprintSaleViewHolderListener,
        ChatroomContract.ChatItem.GroupChatPointsViewHolderListener,
        ChatroomContract.DynamicButtonItem.DynamicButtonListener,
        ChatroomContract.DynamicButtonItem.InteractiveButtonListener{

    private var snackBarWebSocket: Snackbar? = null

    companion object {

        const val GROUP_CHAT_NETWORK_PREFERENCES = "gc_network"

        var VIBRATE_LENGTH = TimeUnit.SECONDS.toMillis(1)
        const val REQUEST_LOGIN = 111
        const val YOUTUBE_DELAY = 1500

        private const val POST_ID = "{post_id}"
        fun createInstance(bundle: Bundle): PlayFragment {
            val fragment = PlayFragment()
            fragment.arguments = bundle
            return fragment
        }

        var DEFAULT_ICON_LIST = arrayListOf(
                R.drawable.ic_green,
                R.drawable.ic_blue,
                R.drawable.ic_yellow,
                R.drawable.ic_pink
        )

    }

    @Inject
    lateinit var presenter: PlayPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: GroupChatAnalytics

    lateinit var viewState: PlayViewState
    private var notifReceiver: BroadcastReceiver? = null
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var networkPreference: SharedPreferences

    private lateinit var channelInfoViewModel: ChannelInfoViewModel
    private var exitDialog: Dialog? = null

    private var enterTimeStamp: Long = 0
    private var timeStampAfterResume: Long = 0
    private var timeStampAfterPause: Long = 0
    private var position = 0
    private var optionsMenuEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(GroupChatAnalytics.PLAY_TRACE)
        setNetworkPreference(savedInstanceState)

        setHasOptionsMenu(true)
        val channelUUID = getParamString(PlayActivity.EXTRA_CHANNEL_UUID, arguments,
                savedInstanceState, "")
        position = getParamInt(PlayActivity.EXTRA_POSITION, arguments,
                savedInstanceState, 0)
        channelInfoViewModel = ChannelInfoViewModel(channelUUID)
    }

    private fun setNetworkPreference(savedInstanceState: Bundle?) {
        activity?.let {
            networkPreference = it.applicationContext.getSharedPreferences(GROUP_CHAT_NETWORK_PREFERENCES,
                    Context.MODE_PRIVATE)
            val editor = networkPreference.edit()
            val useGCP = getParamBoolean(PlayActivity
                    .EXTRA_USE_GCP, arguments, savedInstanceState, false)
            editor.putBoolean(PlayActivity.EXTRA_USE_GCP, useGCP)
            editor.apply()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        enterTimeStamp = System.currentTimeMillis()
        val view = inflater.inflate(R.layout.play_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        loadFirstTime()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.run {
            inflate(R.menu.group_chat_room_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when {
                it.itemId == android.R.id.home -> {
                    backPress()
                    true
                }it.itemId == R.id.action_share -> {
                    shareChannel()
                    true
                }
                it.itemId == R.id.action_overflow -> {
                    onOverflowMenuClicked()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_share).isEnabled = optionsMenuEnable
        menu.findItem(R.id.action_overflow).isEnabled = optionsMenuEnable
    }

    private fun onGetNotif(data: Bundle) {
        val model = GroupChatPointsViewModel(
                data.getString("desc", ""),
                data.getString("applinks", ""),
                data.getString("tkp_code", "")
        )

        if (::viewState.isInitialized) {
            viewState.onReceiveGamificationNotif(model)
        }
    }

    private fun onOverflowMenuClicked() {
        if (::viewState.isInitialized) {
            viewState.onOverflowMenuClicked()
        }
    }

    private fun shareChannel() {
        activity?.let {

            val TAG_CHANNEL = "{channel_url}"

            analytics.eventClickShare(channelInfoViewModel.channelId)

            val link = ChatroomUrl.GROUP_CHAT_URL.replace(TAG_CHANNEL, channelInfoViewModel.channelUrl)

            val description = String.format("%s %s",
                    String.format(getString(R.string.lets_join_channel),
                            channelInfoViewModel.title), "")

            var userId = "0"
            if (userSession.isLoggedIn) {
                userId = userSession.userId
            }

            val shareData = LinkerData.Builder.getLinkerBuilder()
                    .setId(channelInfoViewModel.channelId)
                    .setName(channelInfoViewModel.title)
                    .setTextContent(description)
                    .setDescription(description)
                    .setImgUri(channelInfoViewModel.bannerUrl)
                    .setOgImageUrl(channelInfoViewModel.bannerUrl)
                    .setOgTitle(channelInfoViewModel.title)
                    .setUri(channelInfoViewModel.channelUrl)
                    .setSource(userId) // just using existing variable
                    .setPrice("sharing") // here too
                    .setType(LinkerData.GROUPCHAT_TYPE)
                    .build()

            DefaultShareData(activity, shareData).show()
        }

    }

    private fun onSuccessGetDynamicButtons(): (DynamicButtonsViewModel) -> Unit {
        return {
            viewState.onDynamicButtonUpdated(it)
        }
    }

    private fun onErrorGetDynamicButtons(): (String) -> Unit {
        return {
            viewState.onErrorGetDynamicButtons()
        }
    }

    private fun onSuccessGetStickyComponent(): (StickyComponentViewModel) -> Unit {
        return {
            viewState.onStickyComponentUpdated(it)
        }
    }

    private fun onErrorGetStickyComponent(): (String) -> Unit {
        return {
            viewState.onErrorGetStickyComponent()
        }
    }

    private fun onSuccessGetInfoFirstTime(): (ChannelInfoViewModel) -> Unit {
        return {
            performanceMonitoring.stopTrace()
            onToolbarEnabled(true)

            presenter.getDynamicButtons(channelInfoViewModel.channelId, onSuccessGetDynamicButtons(),
                    onErrorGetDynamicButtons())
            presenter.getStickyComponents(channelInfoViewModel.channelId,
                    onSuccessGetStickyComponent(), onErrorGetStickyComponent())
            presenter.getVideoStream(channelInfoViewModel.channelId, onSuccessGetVideoStream(), onErrorGetVideoStream())
            viewState.onSuccessGetInfoFirstTime(it, childFragmentManager)
            saveGCTokenToCache(it.groupChatToken)
            channelInfoViewModel = it
            presenter.openWebSocket(userSession, it.channelId, it.groupChatToken, it.settingGroupChat, false)
            setExitDialog(it.exitMessage)

        }
    }

    private fun onSuccessGetInfo(): (ChannelInfoViewModel) -> Unit {
        return {
            presenter.getDynamicButtons(channelInfoViewModel.channelId, onSuccessGetDynamicButtons(),
                    onErrorGetDynamicButtons())
            presenter.getStickyComponents(channelInfoViewModel.channelId,
                    onSuccessGetStickyComponent(), onErrorGetStickyComponent())
            presenter.getVideoStream(channelInfoViewModel.channelId, onSuccessGetVideoStream(), onErrorGetVideoStream())
            viewState.onSuccessGetInfo(it)
            saveGCTokenToCache(it.groupChatToken)
            channelInfoViewModel = it
            setExitDialog(it.exitMessage)
        }
    }


    private fun onSuccessGetVideoStream(): (VideoStreamViewModel) -> Unit {
        return {
            viewState.onVideoVerticalUpdated(it)
        }
    }

    private fun onErrorGetVideoStream(): (Throwable) -> Unit {
        return {
            viewState.onErrorVideoVertical()
        }
    }

    private fun onErrorGetInfo(): (String) -> Unit {
        return {
            performanceMonitoring.stopTrace()
            viewState.onErrorGetInfo(it)
            onToolbarEnabled(false)
        }
    }

    private fun onNoInternetConnection(): () -> Unit {
        return {
            performanceMonitoring.stopTrace()
            viewState.onNoInternetConnection()
            onToolbarEnabled(false)
        }
    }

    override fun onToolbarEnabled(isEnabled: Boolean) {
        optionsMenuEnable = isEnabled

        (activity as PlayActivity).let {
            var color: Int
            color = when {
                isEnabled -> R.color.white
                else -> R.color.black_70
            }
            it.changeHomeDrawableColor(color)
        }
    }

    override fun onRetryGetInfo() {
        presenter.getPlayInfo(channelInfoViewModel.channelId, onSuccessGetInfoFirstTime(), onErrorGetInfo(), onNoInternetConnection())
    }

    private fun setExitDialog(exitMessage: ExitMessage?) {
        exitMessage?.let {
            exitDialog = Dialog(this@PlayFragment.activity, Dialog.Type.PROMINANCE)
            exitDialog?.let { dialog ->
                dialog.setTitle(exitMessage.title)
                dialog.setDesc(exitMessage.body)
                dialog.setBtnOk(activity?.getString(R.string.exit_group_chat_yes))
                dialog.setOnOkClickListener {
                    dialog.dismiss()
                }

                dialog.setBtnCancel(activity?.getString(R.string.exit_group_chat_no))
                dialog.setOnCancelClickListener {
                    viewState.getChannelInfo()?.let {
                        analytics.eventUserExit(it.channelId + " " + (System.currentTimeMillis() - enterTimeStamp))
                        if(!viewState.getDurationWatchVideo().isNullOrBlank() &&
                                !viewState.getDurationWatchVideo().equals("0")) {
                            analytics.eventWatchVideoDuration(it.channelId, viewState.getDurationWatchVideo())
                        }
                    }
                    backToChannelList()
                    activity?.finish()
                }
            }
        }
    }

    private fun initView(view: View) {
        activity?.let {
            viewState = PlayViewStateImpl(userSession, analytics, view,
                    it, this, childFragmentManager,this, this, this,
                    this, this, sendMessage(), this, this)
        }
        setToolbarView()
    }

    private fun setToolbarView() {
        val toolbar = viewState.getToolbar()
        activity?.let {
            (it as AppCompatActivity).let {
                it.setSupportActionBar(toolbar)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun getAdapterTypeFactory(): GroupChatTypeFactoryImpl {
        return GroupChatTypeFactoryImpl(this, this, this, this)
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val playComponent = DaggerPlayComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()

            playComponent.inject(this)
            presenter.attachView(this)
        }
    }

    private fun saveGCTokenToCache(groupChatToken: String) {
        userSession.gcToken = groupChatToken
    }

    fun backPress() {
        if (exitDialog != null && !viewState.errorViewShown()) {
            exitDialog!!.show()
        } else {
            activity?.finish()
        }
    }

    override fun addQuickReply(text: String?) {
        viewState.onQuickReplyClicked(text)
    }

    override fun onImageAnnouncementClicked(image: ImageAnnouncementViewModel) {
        if(image.redirectUrl.isBlank()) return
        analytics.eventClickThumbnail(channelInfoViewModel, image.contentImageUrl, image
                .contentImageId, image.contentImageId)
        var applink = RouteManager.routeWithAttribution(context,  image.redirectUrl,
                GroupChatAnalytics.generateTrackerAttribution(GroupChatAnalytics
                        .ATTRIBUTE_IMAGE_ANNOUNCEMENT,
                        channelInfoViewModel.channelUrl,
                        channelInfoViewModel.title))

        openRedirectUrl(applink)
    }

    override fun onVoteComponentClicked(type: String?, name: String?, voteUrl : String) {
        analytics.eventClickVoteComponent(channelInfoViewModel, name)
        if(!userSession.isLoggedIn){
            onLoginClicked(viewState.getChannelInfo()?.channelId)
        }else{
            viewState.onShowOverlayFromVoteComponent(voteUrl)
        }
    }

    override fun onSprintSaleProductClicked(productViewModel: SprintSaleProductViewModel?, position: Int) {

        viewState.getChannelInfo()?.sprintSaleViewModel?.let {
            if (!it.sprintSaleType.equals(SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH, true)) {
                analytics.eventClickSprintSaleProduct(productViewModel, position, channelInfoViewModel)
                openRedirectUrl(it.redirectUrl ?: "")
            }
        }
    }

    override fun onSprintSaleComponentClicked(sprintSaleAnnouncementViewModel: SprintSaleAnnouncementViewModel?) {
        sprintSaleAnnouncementViewModel?.let {
            if ((it.redirectUrl ?: "").isBlank()) {
                return
            }

            if (!it.sprintSaleType.equals(SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH, true)) {
                analytics.eventClickSprintSaleComponent(sprintSaleAnnouncementViewModel,
                        position, channelInfoViewModel)
                openRedirectUrl(it.redirectUrl ?: "")
            }
        }

    }

    override fun onPointsClicked(url: String?) {
        url?.let {
            openRedirectUrl(it)
            analytics.eventClickLoyaltyWidget(channelInfoViewModel.channelId)
        }
    }

    override fun openRedirectUrl(generateLink: String) {
        if (generateLink.isBlank())
            return

        RouteManager.route(activity, ApplinkConst.WEBVIEW, generateLink)
    }

    private fun getInboxChannelsIntent(): Intent? {
        return RouteManager.getIntent(activity, ApplinkConst.GROUPCHAT_LIST)
    }

    override fun onOpenWebSocket(needRefreshInfo: Boolean) {
        snackBarWebSocket?.dismiss()
        if(needRefreshInfo) {
            refreshInfo()
        }
    }

    override fun onTotalViewChanged(participantViewModel: ParticipantViewModel) {
        viewState.onTotalViewChanged(participantViewModel.channelId, participantViewModel.totalView)
    }

    @Suppress("DEPRECATION")
    override fun vibratePhone() {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_LENGTH, VibrationEffect
                        .DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(VIBRATE_LENGTH)
            }
        }
    }

    override fun onPinnedMessageUpdated(it: PinnedMessageViewModel) {
        viewState.onPinnedMessageUpdated(it)
    }

    override fun onAdsUpdated(it: AdsViewModel) {
        viewState.onAdsUpdated(it)
    }

    override fun onChannelDeleted() {
        viewState.onChannelDeleted()
    }

    override fun backToChannelList() {
        activity?.let {
            if (it.isTaskRoot) {
                startActivity(RouteManager.getIntent(it, ApplinkConst.HOME))
            }
            it.finish()
            it.onBackPressed()
        }
    }

    override fun onVideoUpdated(it: VideoViewModel) {
        viewState.onVideoHorizontalUpdated(it)
    }

    override fun onVideoStreamUpdated(it: VideoStreamViewModel) {
        if(isInPipMode() == true) {
            activity?.finish()
        } else {
            viewState.onVideoVerticalUpdated(it)
        }
    }

    override fun handleEvent(it: EventGroupChatViewModel) {
        when {
            it.isFreeze -> {
                viewState.onChannelFrozen(it.channelId)
                onToolbarEnabled(false)
            }
            it.isBanned -> viewState.banUser(it.userId)
        }
    }

    override fun onFinish() {
        activity?.finish()
    }

    override fun showOverlayDialog(it: OverlayViewModel) {
        channelInfoViewModel.overlayViewModel = it
        viewState.onReceiveOverlayMessageFromWebsocket(channelInfoViewModel)
    }

    override fun closeOverlayDialog() {
        viewState.onReceiveCloseOverlayMessageFromWebsocket()
    }

    override fun updateDynamicButton(it: DynamicButtonsViewModel) {
        viewState.onDynamicButtonUpdated(it)
    }

    override fun onSprintSaleReceived(it: SprintSaleAnnouncementViewModel) {
        viewState.onSprintSaleReceived(it)
        addIncomingMessage(it)
    }

    override fun onBackgroundUpdated(it: BackgroundViewModel) {
        viewState.onBackgroundUpdated(it)
    }

    override fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel) {
        viewState.onQuickReplyUpdated(it)
    }

    override fun onStickyComponentReceived(it: StickyComponentViewModel) {
        viewState.onStickyComponentUpdated(it)
    }

    override fun setSnackBarConnectingWebSocket() {
        if (userSession.isLoggedIn && !viewState.errorViewShown()) {
            snackBarWebSocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.connecting))
            snackBarWebSocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.show()
            }
        }
    }

    override fun setSnackBarRetryConnectingWebSocket() {
        if (userSession.isLoggedIn && !viewState.errorViewShown()) {
            snackBarWebSocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.error_websocket_play))
            snackBarWebSocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.setAction(getString(R.string.retry)) {
                    viewState.getChannelInfo()?.let { channelInfo ->
                        presenter.openWebSocket(
                                userSession,
                                channelInfo.channelId,
                                channelInfo.groupChatToken,
                                channelInfo.settingGroupChat,
                                true
                        )
                        setSnackBarConnectingWebSocket()
                    }
                }
                it.show()
            }
        }
    }

    override fun onLoginClicked(channelId: String?) {
        analytics.eventClickLogin(channelId)
        startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.LOGIN), REQUEST_LOGIN)
    }

    private fun loadFirstTime() {
        presenter.getPlayInfo(channelInfoViewModel.channelId, onSuccessGetInfoFirstTime(), onErrorGetInfo(), onNoInternetConnection())
    }

    private fun refreshInfo() {
        presenter.getPlayInfo(channelInfoViewModel.channelId, onSuccessGetInfo(), onErrorGetInfo(), onNoInternetConnection())
    }

    override fun addIncomingMessage(it: Visitable<*>) {
        viewState.onMessageReceived(it)
        trackViewIncomingMessage(it)
    }

    private fun trackViewIncomingMessage(it: Visitable<*>) {
        if (it is ImageAnnouncementViewModel) {
            analytics.eventViewImageAnnouncement(channelInfoViewModel,
                    it.contentImageUrl,
                    it.contentImageId,
                    it.contentImageId)
        }
        else if (it is VoteAnnouncementViewModel){
            analytics.eventViewVote(channelInfoViewModel, it.message)
        }
    }


    private fun sendMessage(): (PendingChatViewModel) -> Unit {
        return {
            presenter.sendMessage(it, ::afterSendMessage, ::onSuccessSendMessage, ::onErrorSendMessage)
        }
    }

    private fun afterSendMessage() {
        viewState.afterSendMessage()
    }

    private fun onSuccessSendMessage(pendingChatViewModel: PendingChatViewModel) {
        viewState.onSuccessSendMessage(pendingChatViewModel)
    }

    private fun onErrorSendMessage(pendingChatViewModel: PendingChatViewModel, exception: Exception?) {
        viewState.onErrorSendMessage(pendingChatViewModel, exception)
        ToasterError.make(activity?.findViewById<View>(android.R.id.content), exception?.message)
    }

    override fun onFloatingIconClicked(it: DynamicButton, applink: String) {
        when (it.contentType) {
            DynamicButtonsViewModel.TYPE_REDIRECT_EXTERNAL -> openRedirectUrl(applink)
            DynamicButtonsViewModel.TYPE_OVERLAY_CTA -> viewState.onShowOverlayCTAFromDynamicButton(it)
            DynamicButtonsViewModel.TYPE_OVERLAY_WEBVIEW -> viewState.onShowOverlayWebviewFromDynamicButton(it)
        }
    }

    override fun onPause() {
        super.onPause()
        timeStampAfterPause = System.currentTimeMillis()
        notifReceiver?.let { tempNotifReceiver ->
            context?.let {
                LocalBroadcastManager.getInstance(it).unregisterReceiver(tempNotifReceiver)
            }
        }
        timeStampAfterPause = System.currentTimeMillis()

        if(isInPipMode() == true) {
            snackBarWebSocket?.dismiss()
            dismissDialog()
        } else {
            presenter.destroyWebSocket()
        }
    }

    override fun onResume() {
        super.onResume()
        viewState.setBottomView()
        kickIfIdleForTooLong()

        viewState.autoAddSprintSale()

        viewState.getChannelInfo()?.let {
            presenter.openWebSocket(userSession, it.channelId, it.groupChatToken, it.settingGroupChat, true)
        }

        viewState.autoPlayVideo()

        if (notifReceiver == null) {
            notifReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.extras != null) {
                        onGetNotif(intent.extras!!)
                    }
                }
            }
        }

        notifReceiver?.let { temp ->
            activity?.applicationContext?.let {
                try {
                    LocalBroadcastManager.getInstance(it).registerReceiver(temp, IntentFilter
                    (TkpdState.LOYALTY_GROUP_CHAT))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        timeStampAfterResume = System.currentTimeMillis()
    }

    private fun kickIfIdleForTooLong() {
        Log.i("play pause", timeStampAfterPause.toString())
        Log.i("play pause now", System.currentTimeMillis().toString())
        var duration = PlayActivity.KICK_THRESHOLD_TIME
        viewState.getChannelInfo()?.kickViewModel?.kickDuration?.let {
            if (it > 0) duration = TimeUnit.SECONDS.toMillis(it)
        }
        if (timeStampAfterPause > 0
                && System.currentTimeMillis() - timeStampAfterPause > duration && duration > 0) {
            showKickUser()
        }
    }

    private fun showKickUser() {
        val dialog = Dialog(activity, Dialog.Type.RETORIC)
        viewState.getChannelInfo()?.kickViewModel?.let {
            dialog.setTitle(it.kickTitle)
            dialog.setDesc(it.kickMessage)
            dialog.setBtnOk(it.kickButtonTitle)
            dialog.setOnOkClickListener {
                dialog.dismiss()
                val intent = Intent()
                viewState.getChannelInfo()?.let {
                    intent.putExtra(PlayActivity.TOTAL_VIEW, it.totalView)
                    intent.putExtra(PlayActivity.EXTRA_POSITION, position)
                }
                activity?.setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL, intent)
                activity?.finish()
            }
            dialog.show()
        }
    }

    override fun onDynamicButtonClicked(it: DynamicButton) {
        analytics.eventClickDynamicButtons(channelInfoViewModel, it)
        when (it.contentType) {
            DynamicButtonsViewModel.TYPE_REDIRECT_EXTERNAL -> openRedirectUrl(it.contentLinkUrl)
            DynamicButtonsViewModel.TYPE_OVERLAY_CTA -> viewState.onShowOverlayCTAFromDynamicButton(it)
            DynamicButtonsViewModel.TYPE_OVERLAY_WEBVIEW -> viewState.onShowOverlayWebviewFromDynamicButton(it)
        }
    }

    override fun onInteractiveButtonClicked(anchorView: LottieAnimationView) {
        viewState.onInteractiveButtonClicked(anchorView)
    }

    override fun onInteractiveButtonViewed(anchorView: LottieAnimationView) {
        viewState.onInteractiveButtonViewed(anchorView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            viewState.onSuccessLogin()
            loadFirstTime()
        }
    }

    override fun onDestroy() {

        if(::viewState.isInitialized){
            viewState?.destroy()
        }
        presenter.detachView()
        super.onDestroy()
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.chat_list)
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun loadData(page: Int) {
        //NOT USED
    }

    override fun onItemClicked(t: Visitable<*>?) {
        //NOT USED
    }

    override fun onSprintSaleIconClicked(sprintSaleViewModel: SprintSaleViewModel?) {
        //NOT USED
    }

    override fun getScreenName(): String {
        return ""
    }

    fun dismissDialog() {
        exitDialog?.dismiss()
        viewState.dismissAllBottomSheet()
    }

    override fun hasVideoVertical(): Boolean {
        return viewState?.verticalVideoShown()
    }

    fun isChannelActive(): Boolean {
        return viewState?.isChannelActive()
    }

    private fun isInPipMode(): Boolean? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity?.isInPictureInPictureMode
        } else {
            false
        }
    }
}
