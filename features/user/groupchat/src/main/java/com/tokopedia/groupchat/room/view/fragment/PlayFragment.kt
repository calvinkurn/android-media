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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.groupchat.GroupChatModuleRouter
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.channel.view.activity.ChannelActivity
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity.PAUSE_RESUME_TRESHOLD_TIME
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
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.groupchat.room.view.viewstate.PlayViewState
import com.tokopedia.groupchat.room.view.viewstate.PlayViewStateImpl
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamInt
import com.tokopedia.kotlin.util.getParamString
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
        ChatroomContract.ChatItem.GroupChatPointsViewHolderListener {

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
    }

    @Inject
    lateinit var presenter: PlayPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: GroupChatAnalytics

    lateinit var viewState: PlayViewState
    private lateinit var notifReceiver: BroadcastReceiver
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
                }
                it.itemId == R.id.action_info -> {
                    onInfoClicked()
                    true
                }
                it.itemId == R.id.action_share -> {
                    shareChannel()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_info).isEnabled = optionsMenuEnable
        menu.findItem(R.id.action_share).isEnabled = optionsMenuEnable
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

    private fun onInfoClicked() {
        if (::viewState.isInitialized) {
            viewState.onInfoMenuClicked()
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

            (it.applicationContext as GroupChatModuleRouter).shareGroupChat(
                    it,
                    channelInfoViewModel.channelId,
                    channelInfoViewModel.title,
                    description,
                    channelInfoViewModel.bannerUrl,
                    channelInfoViewModel.channelUrl,
                    userId,
                    "sharing"
            )
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

    private fun onSuccessGetInfo(): (ChannelInfoViewModel) -> Unit {
        return {
            performanceMonitoring.stopTrace()
            onToolbarEnabled(true)

            presenter.getDynamicButtons(channelInfoViewModel.channelId, onSuccessGetDynamicButtons(),
                    onErrorGetDynamicButtons())
            presenter.getStickyComponents(channelInfoViewModel.channelId,
                    onSuccessGetStickyComponent(), onErrorGetStickyComponent())

            viewState.onSuccessGetInfoFirstTime(it, childFragmentManager)
            saveGCTokenToCache(it.groupChatToken)
            channelInfoViewModel = it
            presenter.openWebSocket(userSession, it.channelId, it.groupChatToken, it.settingGroupChat)
            setExitDialog(it.exitMessage)

        }
    }

    private fun onErrorGetInfo(): (String) -> Unit {
        return {
            performanceMonitoring.stopTrace()
            viewState.onErrorGetInfo(it)
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
        presenter.getPlayInfo(channelInfoViewModel.channelId, onSuccessGetInfo(), onErrorGetInfo())
    }

    private fun setExitDialog(exitMessage: ExitMessage?) {
        exitMessage?.let {
            exitDialog = Dialog(this@PlayFragment.activity, Dialog.Type.PROMINANCE)
            exitDialog?.let { dialog ->
                dialog.setTitle(exitMessage.title)
                dialog.setDesc(exitMessage.body)
                dialog.setBtnOk(activity?.getString(R.string.exit_group_chat_yes))
                dialog.setOnOkClickListener {
                    viewState.getChannelInfo()?.let {
                        analytics.eventUserExit(it.channelId + " " + (System.currentTimeMillis() - enterTimeStamp))
                        if(!viewState.getDurationWatchVideo().isNullOrBlank() &&
                                !viewState.getDurationWatchVideo().equals("0")) {
                            analytics.eventWatchVideoDuration(it.channelId, viewState.getDurationWatchVideo())
                        }
                    }

                    activity?.let {
                        if (it.isTaskRoot) {
                            (activity as PlayActivity).startActivity(getInboxChannelsIntent())
                        }
                    }
                    activity?.finish()
                }

                dialog.setBtnCancel(activity?.getString(R.string.exit_group_chat_no))
                dialog.setOnCancelClickListener { dialog.dismiss() }
            }
        }
    }

    private fun initView(view: View) {
        activity?.let {
            viewState = PlayViewStateImpl(userSession, analytics, view,
                    it, this, this, this, this,
                    this, this, sendMessage())
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
        if (exitDialog != null) {
            exitDialog!!.show()
        } else {
            activity?.finish()
        }
    }

    override fun addQuickReply(text: String?) {
        viewState.onQuickReplyClicked(text)
    }

    override fun onImageAnnouncementClicked(image: ImageAnnouncementViewModel) {
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

        (activity?.applicationContext as GroupChatModuleRouter).openRedirectUrl(activity, generateLink)
    }

    private fun getInboxChannelsIntent(): Intent? {
        return (context as GroupChatModuleRouter).getInboxChannelsIntent(activity)
    }

    override fun onOpenWebSocket() {
        snackBarWebSocket?.dismiss()
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
                startActivity((it.applicationContext as GroupChatModuleRouter).getInboxChannelsIntent(context))
            }
            it.finish()
            it.onBackPressed()
        }
    }

    override fun onVideoUpdated(it: VideoViewModel) {
        viewState.onVideoUpdated(it, childFragmentManager)
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
            snackBarWebSocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.sendbird_error_retry))
            snackBarWebSocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.setAction(getString(R.string.retry)) {
                    viewState.getChannelInfo()?.let { channelInfo ->
                        presenter.openWebSocket(
                                userSession,
                                channelInfo.channelId,
                                channelInfo.groupChatToken,
                                channelInfo.settingGroupChat
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
        startActivityForResult((activity!!.applicationContext as GroupChatModuleRouter)
                .getLoginIntent(activity), REQUEST_LOGIN)
    }

    private fun loadFirstTime() {
        presenter.getPlayInfo(channelInfoViewModel.channelId, onSuccessGetInfo(), onErrorGetInfo())
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
            analytics.eventClickSendChat(channelInfoViewModel.channelId)
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

    override fun onDynamicIconClicked(it: DynamicButtonsViewModel.Button) {
        analytics.eventClickDynamicButtons(channelInfoViewModel, it)
        when (it.contentType) {
            DynamicButtonsViewModel.TYPE_REDIRECT_EXTERNAL -> openRedirectUrl(it.contentLinkUrl)
            DynamicButtonsViewModel.TYPE_OVERLAY_CTA -> viewState.onShowOverlayCTAFromDynamicButton(it)
            DynamicButtonsViewModel.TYPE_OVERLAY_WEBVIEW -> viewState.onShowOverlayWebviewFromDynamicButton(it)
        }
    }

    override fun onPause() {
        super.onPause()
        timeStampAfterPause = System.currentTimeMillis()
        presenter.destroyWebSocket()
        if (canPause()) {
            if (::notifReceiver.isInitialized) {
                context?.let {
                    LocalBroadcastManager.getInstance(it).unregisterReceiver(notifReceiver)
                }
            }
            timeStampAfterPause = System.currentTimeMillis()
        }
    }

    override fun onResume() {
        super.onResume()
        viewState.setBottomView()
        kickIfIdleForTooLong()
        if (canResume()) {

            viewState.autoAddSprintSale()

            viewState.getChannelInfo()?.let {
                presenter.openWebSocket(userSession, it.channelId, it.groupChatToken, it.settingGroupChat)
            }

            if (::notifReceiver.isInitialized) {
                notifReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        if (intent.extras != null) {
                            onGetNotif(intent.extras!!)
                        }
                    }
                }
            }

            activity?.applicationContext?.let {
                try {
                    LocalBroadcastManager.getInstance(it).registerReceiver(notifReceiver, IntentFilter
                    (TkpdState.LOYALTY_GROUP_CHAT))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            timeStampAfterResume = System.currentTimeMillis()
        }
    }

    private fun canResume(): Boolean {
        return timeStampAfterResume == 0L || timeStampAfterResume > 0
                && System.currentTimeMillis() - timeStampAfterResume > PAUSE_RESUME_TRESHOLD_TIME
    }

    private fun canPause(): Boolean {
        return (timeStampAfterPause == 0L || (timeStampAfterPause > 0
                && System.currentTimeMillis() - timeStampAfterPause > PAUSE_RESUME_TRESHOLD_TIME
                && canResume()))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            viewState.onSuccessLogin()
            loadFirstTime()
        }
    }

    override fun onDestroy() {
        viewState.destroy()
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

}
