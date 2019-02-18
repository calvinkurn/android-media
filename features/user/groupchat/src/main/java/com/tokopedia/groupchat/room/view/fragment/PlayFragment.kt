package com.tokopedia.groupchat.room.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.component.ToasterError
import com.tokopedia.groupchat.GroupChatModuleRouter
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.room.di.DaggerPlayComponent
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.presenter.PlayPresenter
import com.tokopedia.groupchat.room.view.viewstate.PlayViewState
import com.tokopedia.groupchat.room.view.viewstate.PlayViewStateImpl
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

    private var snackbarWebsocket: Snackbar? = null

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

    open lateinit var viewState: PlayViewState
    private lateinit var rootView : View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.play_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        presenter.getPlayInfo(arguments?.getString(PlayActivity.EXTRA_CHANNEL_UUID), onSuccessGetInfo())
    }

    override fun loadData(page: Int) {

    }

    private fun onSuccessGetInfo(): (ChannelInfoViewModel) -> Unit {
        return {
            viewState.onSuccessGetInfoFirstTime(it, childFragmentManager)
            saveGCTokenToCache()
            presenter.openWebSocket(userSession, it.channelId, it.groupChatToken, it.settingGroupChat)
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

//        if (isLollipopOrNewer()) {
//            TransparentStatusBarHelper.assistActivity(activity)
//        }
//        removePaddingStatusBar()

        var toolbar = viewState.getToolbar()

//        if (isLollipopOrNewer()) {
//            activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
//            toolbar?.setPadding(0, getStatusBarHeight(), 0, 0)
//        }
        activity?.let {
            (it as AppCompatActivity).let {
                it.setSupportActionBar(toolbar)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun removePaddingStatusBar() {
        val KEYBOARD_THRESHOLD = 100

        view?.let{
            rootView = it.findViewById<View>(R.id.root_view)
            rootView.viewTreeObserver?.addOnGlobalLayoutListener {
                val heightDiff = rootView.rootView.height - rootView.height

                if (heightDiff > KEYBOARD_THRESHOLD) {
                    removePaddingIfKeyboardIsShowing()
                } else {
                    addPaddingIfKeyboardIsClosed()
                }
            }
        }

    }

    private fun addPaddingIfKeyboardIsClosed() {
        activity?.run{
            if (isLollipopOrNewer() && getSoftButtonsBarSizePort(this) > 0) {
                val container = rootView.findViewById<View>(R.id.container)
                val params = container
                        .layoutParams as ConstraintLayout.LayoutParams
                params.setMargins(0, 0, 0, getSoftButtonsBarSizePort(this))
                container.layoutParams = params
            }
        }

    }

    private fun removePaddingIfKeyboardIsShowing() {
        activity?.run{
            if (isLollipopOrNewer() && getSoftButtonsBarSizePort(this) > 0) {
                val container = rootView.findViewById<View>(R.id.container)
                val params = container.layoutParams as ConstraintLayout.LayoutParams
                params.setMargins(0, 0, 0, 0)
                container.layoutParams = params
            }
        }
    }

    fun getSoftButtonsBarSizePort(activity: FragmentActivity): Int {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            activity.windowManager.defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight)
                realHeight - usableHeight
            else
                0
        }
        return 0
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun isLollipopOrNewer(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
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

    override fun onBackPressed(): Boolean {
        return if (::viewState.isInitialized) {
            viewState.onBackPressed()
        } else
            false
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
        snackbarWebsocket?.dismiss()
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

    override fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel) {
        viewState.onQuickReplyUpdated(it)
    }

    override fun setSnackBarConnectingWebSocket() {
        if (userSession.isLoggedIn) {
            snackbarWebsocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.connecting))
            snackbarWebsocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.show()
            }
        }
    }

    override fun setSnackBarRetryConnectingWebSocket() {
        if (userSession.isLoggedIn) {
            snackbarWebsocket = ToasterError.make(activity?.findViewById<View>(android.R.id.content), getString(R.string.sendbird_error_retry))
            snackbarWebsocket?.let {
                it.view.minimumHeight = resources.getDimension(R.dimen.snackbar_height).toInt()
                it.setAction(getString(R.string.retry), View.OnClickListener {
                    setSnackBarConnectingWebSocket()
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
            presenter.getPlayInfo(arguments?.getString(PlayActivity.EXTRA_CHANNEL_UUID), onSuccessGetInfo())
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
