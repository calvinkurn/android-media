package com.tokopedia.groupchat.room.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.groupchat.GroupChatModuleRouter
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo
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
import com.tokopedia.groupchat.room.view.viewstate.PlayViewState
import com.tokopedia.groupchat.room.view.viewstate.PlayViewStateImpl
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
    lateinit var analytics : GroupChatAnalytics

    lateinit var viewState: PlayViewState

    private lateinit var channelInfoViewModel : ChannelInfoViewModel
    private var exitDialog : Dialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val channelUUID = getParamString(PlayActivity.EXTRA_CHANNEL_UUID, arguments,
                savedInstanceState, "")
        channelInfoViewModel = ChannelInfoViewModel(channelUUID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.play_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        presenter.getPlayInfo(channelInfoViewModel.channelId, onSuccessGetInfo())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.run{inflate(R.menu.group_chat_room_menu, menu)}
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let{
            return when {
                it.itemId == android.R.id.home -> {
                    backPress()
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

    private fun shareChannel() {
        activity?.let{

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

    override fun loadData(page: Int) {

    }

    private fun onSuccessGetInfo(): (ChannelInfoViewModel) -> Unit {
        return {
            viewState.onSuccessGetInfoFirstTime(it, childFragmentManager)
            saveGCTokenToCache()
            channelInfoViewModel = it
            presenter.openWebSocket(userSession, it.channelId, it.groupChatToken, it.settingGroupChat)
            setExitDialog(it.exitMessage)
        }
    }

    private fun setExitDialog(exitMessage: ExitMessage?) {
        exitMessage?.let {
            exitDialog = Dialog(this@PlayFragment.activity, Dialog.Type.PROMINANCE)
            exitDialog?.let { dialog ->
                dialog.setTitle(exitMessage.title)
                dialog.setDesc(exitMessage.body)
                dialog.setBtnOk(activity?.getString(R.string.exit_group_chat_yes))
                dialog.setOnOkClickListener {
                    //                    analytics.eventUserExit(getChannelInfoViewModel()!!.getChannelId() + " " + getDurationOnGroupChat())
//                    if (isTaskRoot()) {
//                        startActivity((getApplicationContext() as GroupChatModuleRouter).getInboxChannelsIntent(context))
//                    }
//                    if (onPlayTime != 0L) {
//                        analytics.eventWatchVideoDuration(getChannelInfoViewModel()!!.getChannelId(), getDurationWatchVideo())
//                    }
                    activity?.finish()
                    activity?.onBackPressed()
                }

                dialog.setBtnCancel(activity?.getString(R.string.exit_group_chat_no))
                dialog.setOnCancelClickListener { dialog.dismiss() }
            }
        }
    }

    private fun initView(view: View) {
        activity?.let {
            viewState = PlayViewStateImpl(userSession, view,
                    it,this, this, this, this,
                    this, this, sendMessage())
        }
        setToolbarView(view)
    }

    private fun setToolbarView(view: View) {
        val toolbar = viewState.getToolbar()
        activity?.let {
            (it as AppCompatActivity).let {
                it.setSupportActionBar(toolbar)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
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

    override fun getAdapterTypeFactory(): GroupChatTypeFactoryImpl {
        return GroupChatTypeFactoryImpl(this, this, this, this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun getScreenName(): String {
        return ""
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

    private fun saveGCTokenToCache() {
        userSession.gcToken = "asd"
    }

    fun backPress() {
//        if (onPlayTime != 0L) {
//            analytics.eventWatchVideoDuration(getChannelInfoViewModel()!!.getChannelId(), getDurationWatchVideo())
//        }
        if(exitDialog != null){
            exitDialog!!.show()
        } else {
            activity?.finish()
            activity?.onBackPressed()
        }
    }

    override fun addQuickReply(text: String?) {
//        if (activity != null
//                && activity is GroupChatContract.View
//                && (activity as GroupChatContract.View).getChannelInfoViewModel() != null) {
//            analytics.eventClickQuickReply(
//                    String.format("%s - %s", (activity as GroupChatContract.View).getChannelInfoViewModel()!!.getChannelId(), message))
//        }

        viewState.onQuickReplyClicked(text)
    }

    override fun onImageAnnouncementClicked(url: String?) {

    }

    override fun onVoteComponentClicked(type: String?, name: String?) {

    }

    override fun onSprintSaleProductClicked(sprintSaleViewModel: SprintSaleProductViewModel?, position: Int) {

    }

    override fun onSprintSaleComponentClicked(sprintSaleAnnouncementViewModel: SprintSaleAnnouncementViewModel?) {

    }

    override fun onSprintSaleIconClicked(sprintSaleViewModel: SprintSaleViewModel?) {

    }

    override fun onPointsClicked(url: String?) {

    }

    override fun onOpenWebSocket() {
        snackBarWebSocket?.dismiss()
    }

    override fun onMessageReceived(item: Visitable<*>, hideMessage: Boolean) {

    }

    override fun onTotalViewChanged(participantViewModel: ParticipantViewModel) {
        viewState.onTotalViewChanged(participantViewModel.channelId, participantViewModel.totalView)
    }

    override fun vibratePhone() {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibrator?.let{
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
            if(it.isTaskRoot){
                startActivity((it.applicationContext as GroupChatModuleRouter).getInboxChannelsIntent(context))
            }
            it.finish()
            it.onBackPressed()
        }
    }

    override fun onVideoUpdated(it: VideoViewModel) {
        viewState.onVideoUpdated(it, childFragmentManager)

    }

    override fun handleEvent(event: EventGroupChatViewModel) {
        when {
            event.isFreeze -> viewState.onChannelFrozen(event.channelId)
            event.isBanned -> viewState.banUser(event.userId)
        }
    }

    override fun showOverlayDialog(it: OverlayViewModel) {

    }

    override fun closeOverlayDialog() {

    }


    override fun updateDynamicButton(it: ButtonsPojo) {
        viewState.onDynamicButtonUpdated(it)
    }

    override fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel) {
        viewState.onQuickReplyUpdated(it)
    }

    override fun setSnackBarConnectingWebSocket() {
        if (userSession.isLoggedIn) {
            snackBarWebSocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.connecting))
            snackBarWebSocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.show()
            }
        }
    }

    override fun setSnackBarRetryConnectingWebSocket() {
        if (userSession.isLoggedIn) {
            snackBarWebSocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.sendbird_error_retry))
            snackBarWebSocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.setAction(getString(R.string.retry), View.OnClickListener {
                    viewState.getChannelInfo()?.let {
                        presenter.getPlayInfo(it.channelId, onSuccessGetInfo())
                        setSnackBarConnectingWebSocket()
                    }
                })
                it.show()
            }
        }
    }

    override fun onLoginClicked(channelId: String?) {
//      analytics.eventClickLogin(channelId)

        startActivityForResult((activity!!.applicationContext as GroupChatModuleRouter)
                .getLoginIntent(activity), REQUEST_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            viewState.onSuccessLogin()
            presenter.getPlayInfo(channelInfoViewModel.channelId, onSuccessGetInfo())
        }
    }

    override fun addIncomingMessage(it: Visitable<*>) {
        viewState.onMessageReceived(it)
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

    override fun openOverlay(it: String) {

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewState.onKeyboardHidden()
        if (canResume()) {
//            kickIfIdleForTooLong()
//
//            if (viewModel != null && viewModel.getChannelInfoViewModel() != null
//                    && !isFirstTime) {
//                if (!channelInfoDialog.isShowing() || loading.getVisibility() != View.VISIBLE) {
//                    showLoading()
//                    presenter.refreshChannelInfo(viewModel.getChannelUuid())
//                }
//
//            }
//
//            if (notifReceiver == null) {
//                notifReceiver = object : BroadcastReceiver() {
//                    override fun onReceive(context: Context, intent: Intent) {
//                        if (intent.extras != null) {
//                            onGetNotif(intent.extras!!)
//                        }
//                    }
//                }
//            }
//
//            try {
//                LocalBroadcastManager.getInstance(this).registerReceiver(notifReceiver, IntentFilter(TkpdState.LOYALTY_GROUP_CHAT))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//            if (viewModel != null) {
//                viewModel.setTimeStampAfterResume(System.currentTimeMillis())
//            }
//            }
        }
    }

    private fun canResume(): Boolean {
            return true
    //        return viewModel != null && (viewModel.getTimeStampAfterResume() == 0L || viewModel.getTimeStampAfterResume() > 0 && System.currentTimeMillis() - viewModel.getTimeStampAfterResume() > PAUSE_RESUME_TRESHOLD_TIME)
        }

    private fun canPause(): Boolean {
        return true
//        return viewModel != null && (viewModel.getTimeStampAfterPause() == 0L || (viewModel.getTimeStampAfterPause() > 0 && System.currentTimeMillis() - viewModel.getTimeStampAfterPause() > PAUSE_RESUME_TRESHOLD_TIME
//                && canResume()))
    }

    override fun onDestroy() {
        viewState.destroy()
        presenter.detachView()
        super.onDestroy()
    }

}
