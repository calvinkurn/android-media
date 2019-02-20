package com.tokopedia.groupchat.room.view.viewstate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.text.BackEditText
import com.tokopedia.groupchat.GroupChatModuleRouter
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.GroupChatAdapter
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.QuickReplyAdapter
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.QuickReplyTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatVideoFragment
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.common.analytics.EEPromotion
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.common.design.QuickReplyItemDecoration
import com.tokopedia.groupchat.common.design.SpaceItemDecoration
import com.tokopedia.groupchat.common.util.TextFormatter
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.groupchat.room.view.fragment.PlayWebviewDialogFragment
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 13/02/19
 */
class PlayViewStateImpl(
        var userSession: UserSessionInterface,
        var analytics: GroupChatAnalytics,
        var view: View,
        var activity: FragmentActivity,
        var listener: PlayContract.View,
        quickReplyListener: ChatroomContract.QuickReply,
        imageListener: ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener,
        voteAnnouncementListener: ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener,
        sprintSaleViewHolderListener: ChatroomContract.ChatItem.SprintSaleViewHolderListener,
        groupChatPointsViewHolderListener: ChatroomContract.ChatItem.GroupChatPointsViewHolderListener,
        sendMessage: (viewModel: PendingChatViewModel) -> Unit

) : PlayViewState {

    private var toolbar: Toolbar = view.findViewById(R.id.toolbar)
    private var viewModel: ChannelInfoViewModel? = null
    private var channelBanner: ImageView = view.findViewById(R.id.channel_banner)
    private var sponsorLayout = view.findViewById<View>(R.id.sponsor_layout)
    private var sponsorImage = view.findViewById<ImageView>(R.id.sponsor_image)
    private var videoContainer = view.findViewById<View>(R.id.video_horizontal)
    private var pinnedMessageContainer = view.findViewById<View>(R.id.pinned_message)
    private var chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_list)
    private var quickReplyRecyclerView = view.findViewById<RecyclerView>(R.id.quick_reply)
    private var chatNotificationView = view.findViewById<View>(R.id.layout_new_chat)
    private var youTubePlayer: YouTubePlayer? = null
    private var replyEditText: BackEditText = view.findViewById(R.id.reply_edit_text)
    private var login: View = view.findViewById(R.id.login)
    private var inputTextWidget: View = view.findViewById(R.id.bottom)
    private var iconQuiz: View = view.findViewById(R.id.icon_quiz)
    private var iconDynamic: View = view.findViewById(R.id.icon_dynamic)
    private var sendButton: View = view.findViewById(R.id.button_send)

    val dynamicIcon = view.findViewById<ImageView>(R.id.icon_dynamic)
    val webviewIcon = view.findViewById<ImageView>(R.id.webview_icon)

    lateinit var overlayDialog: CloseableBottomSheetDialog
    lateinit var pinnedMessageDialog: CloseableBottomSheetDialog
    lateinit var welcomeInfoDialog: CloseableBottomSheetDialog

    private var listMessage: ArrayList<Visitable<*>> = arrayListOf()

    private var quickReplyAdapter: QuickReplyAdapter
    private var adapter: GroupChatAdapter
    private var newMessageCounter: Int = 0

    private var youtubeRunnable: Handler = Handler()

    private var layoutManager: LinearLayoutManager

    init {
        val groupChatTypeFactory = GroupChatTypeFactoryImpl(
                imageListener,
                voteAnnouncementListener,
                sprintSaleViewHolderListener,
                groupChatPointsViewHolderListener
        )

        adapter = GroupChatAdapter.createInstance(groupChatTypeFactory, listMessage)
        layoutManager = LinearLayoutManager(view.context)
        layoutManager.setReverseLayout(true)
        layoutManager.setStackFromEnd(true)
        chatRecyclerView.layoutManager = layoutManager
        chatRecyclerView.adapter = adapter
        val itemDecoration = SpaceItemDecoration(view.context
                .getResources().getDimension(R.dimen.space_play_chat).toInt())
        chatRecyclerView.addItemDecoration(itemDecoration)

        chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    attemptResetNewMessageCounter()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    attemptResetNewMessageCounter()
                }
            }
        })


        val quickReplyTypeFactory = QuickReplyTypeFactoryImpl(quickReplyListener)
        quickReplyRecyclerView.layoutManager = LinearLayoutManager(
                view.context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        quickReplyAdapter = QuickReplyAdapter(quickReplyTypeFactory)
        quickReplyRecyclerView.adapter = quickReplyAdapter

        val quickReplyItemDecoration = QuickReplyItemDecoration(view.context
                .resources.getDimension(R.dimen.dp_16).toInt())
        quickReplyRecyclerView.addItemDecoration(quickReplyItemDecoration)

        replyEditText.setOnClickListener {
            onKeyboardShown()
        }

        replyEditText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            onKeyboardShown()
        }

        replyEditText.setKeyImeChangeListener { keyCode, event ->
            if (KeyEvent.KEYCODE_BACK == event.keyCode) {
                onKeyboardHidden()
            }
        }

        showLoginButton(!userSession.isLoggedIn)

        login.setOnClickListener {
            listener.onLoginClicked(viewModel?.channelId)
        }

        chatNotificationView.setOnClickListener {
            attemptResetNewMessageCounter()
            scrollToBottom()
        }

        sendButton.setOnClickListener {
            var emp = !TextUtils.isEmpty(replyEditText.text.toString().trim { it <= ' ' })
            if (emp) {
                val pendingChatViewModel = PendingChatViewModel(checkText(replyEditText.text.toString()),
                        userSession.userId,
                        userSession.name,
                        userSession.profilePicture,
                        false)
                sendMessage(pendingChatViewModel)
            }
        }
    }

    override fun onKeyboardHidden() {
        showWidgetAboveInput(true)
        inputTextWidget.setBackgroundColor(MethodChecker.getColor(view.context, R.color.transparent))
        sendButton.hide()
        iconDynamic.show()
        iconQuiz.show()
        toolbar.show()

    }

    fun onKeyboardShown() {
        showWidgetAboveInput(false)
        inputTextWidget.setBackgroundColor(MethodChecker.getColor(view.context, R.color.play_transparent))
        sendButton.show()
        iconDynamic.hide()
        iconQuiz.hide()
        toolbar.hide()
//            setSprintSaleIcon(null)
    }

    private fun checkText(replyText: String): String {
        return replyText.replace("<", "&lt;")
    }

    open fun scrollToBottom() {
        Observable.timer(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    chatRecyclerView.scrollToPosition(0)
                }
    }

    private fun attemptResetNewMessageCounter() {
        newMessageCounter = 0
        chatNotificationView.visibility = View.GONE
    }

    override fun onSuccessGetInfoFirstTime(it: ChannelInfoViewModel, childFragmentManager: FragmentManager) {
        viewModel = it

        setToolbarData(it.title, it.bannerUrl, it.totalView, it.blurredBannerUrl)
        setSponsorData(it.adsId, it.adsImageUrl, it.adsName)
        initVideoFragment(childFragmentManager, it.videoId)
        showWidgetAboveInput(userSession.isLoggedIn)

        //TODO map from response
        setDynamicIcon("https://www.tokopedia.com/play/trivia-quiz?campaign=nakamatest")
        setDynamicIconNotification(true)
        setDynamicBackground("https://i.pinimg.com/originals/12/da/77/12da776434178d3a19176fb76048faba.jpg")
        setFloatingIcon("tokopedia://webview?need_login=true&titlebar=false&url=https%3A%2F%2Fwww" +
                ".tokopedia.com%2Fplay%2Ftrivia-quiz%3Fcampaign%3Dtrivia-hitam-putih", "https://i.gifer.com/M8tf.gif")

        showBottomSheetFirstTime(it)

        showLoginButton(!userSession.isLoggedIn)

        it.settingGroupChat?.maxChar?.let {
            replyEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(it))
        }
    }

    /**
     * show overlay behind channel info
     */
    private fun showBottomSheetFirstTime(it: ChannelInfoViewModel) {
        showInfoBottomSheet(it) {
                showOverlayBottomSheet(it)
        }
    }

    override fun getChannelInfo(): ChannelInfoViewModel? {
        return viewModel
    }

    private fun showWidgetAboveInput(isUserLoggedIn: Boolean) {
        if (isUserLoggedIn) {
            viewModel?.let {
                setPinnedMessage(it)
                setQuickReply(it.quickRepliesViewModel)
            }
        } else {
            hidePinnedMessage()
            setQuickReply(null)
        }
    }

    private fun hidePinnedMessage() {
        pinnedMessageContainer.visibility = View.GONE
    }

    private fun showLoginButton(show: Boolean) {
        if (show) {
            login.visibility = View.VISIBLE
            inputTextWidget.visibility = View.GONE
        } else {
            login.visibility = View.GONE
            inputTextWidget.visibility = View.VISIBLE
        }
    }

    override fun onAdsUpdated(it: AdsViewModel) {
        viewModel?.adsImageUrl = it.adsUrl
        viewModel?.adsId = it.adsId
        viewModel?.adsLink = it.adsLink
        setSponsorData(viewModel?.adsId, viewModel?.adsImageUrl, viewModel?.adsName)
    }

    override fun onVideoUpdated(it: VideoViewModel, childFragmentManager: FragmentManager) {
        viewModel?.videoId = it.videoId
        viewModel?.adsId?.let {
            setSponsorData(it, viewModel?.adsImageUrl, viewModel?.adsName)
        }
        initVideoFragment(childFragmentManager, it.videoId)
    }

    override fun onChannelFrozen(channelId: String) {
        if (channelId == viewModel?.channelId) {
            val myAlertDialog = AlertDialog.Builder(view.context)
            myAlertDialog.setTitle(getStringResource(R.string.channel_not_found))
            myAlertDialog.setMessage(getStringResource(R.string.channel_deactivated))
            myAlertDialog.setPositiveButton(getStringResource(R.string.exit_group_chat_ok)) { dialogInterface, i ->
                listener.backToChannelList()
            }
            myAlertDialog.setCancelable(false)
            myAlertDialog.show()
        }
    }

    override fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel) {
        setQuickReply(it.list)
    }

    override fun banUser(userId: String) {
        if (userId == userSession.userId) {
            val errorMessage = getStringResource(R.string.user_is_banned)
            try {
                val builder = AlertDialog.Builder(view.context)
                builder.setTitle(R.string.default_banned_title)

                builder.setMessage(errorMessage)

                viewModel?.bannedMessage?.let {
                    builder.setMessage(it)
                }

                builder.setPositiveButton(R.string.title_ok) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val intent = Intent()
//                    if (viewModel != null) {
//                        intent.putExtra(TOTAL_VIEW, viewModel?.totalView)
//                        intent.putExtra(EXTRA_POSITION, viewModel?.getChannelPosition())
//                    }
//                    setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL, intent)
                    listener.backToChannelList()
                }
                val dialog = builder.create()
                dialog.setCancelable(false)
                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun getStringResource(id: Int): String {
        return view.context.resources.getString(id)
    }

    override fun onChannelDeleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPinnedMessageUpdated(it: PinnedMessageViewModel) {
        viewModel?.pinnedMessageViewModel = it
        setPinnedMessage(viewModel)
    }

    override fun onSuccessLogin() {
        showLoginButton(userSession.isLoggedIn)
    }

    private fun setPinnedMessage(channelInfoViewModel: ChannelInfoViewModel?) {

        if (channelInfoViewModel?.pinnedMessageViewModel == null) {
            pinnedMessageContainer.visibility = View.GONE
        } else {
            pinnedMessageContainer.visibility = View.VISIBLE

            channelInfoViewModel.pinnedMessageViewModel?.let {
                if(it.title.isBlank()) {
                    pinnedMessageContainer.visibility = View.GONE
                    return
                }
                (pinnedMessageContainer.findViewById(R.id.message) as TextView).text =
                        it.title
                pinnedMessageContainer.setOnClickListener { view ->
                    channelInfoViewModel.pinnedMessageViewModel?.let {
                        analytics.eventClickAdminPinnedMessage(
                                String.format("%s - %s", channelInfoViewModel.channelId,
                                        it.title))
                    }
                    showPinnedMessage(channelInfoViewModel)
                }
            }
        }
    }

    private fun setQuickReply(quickRepliesViewModel: List<GroupChatQuickReplyItemViewModel>?) {
        quickReplyRecyclerView.visibility = View.GONE
        quickRepliesViewModel?.let {
            if (it.isEmpty()) return
            quickReplyRecyclerView.visibility = View.VISIBLE
            quickReplyAdapter.setList(quickRepliesViewModel)
        }
    }

    override fun onQuickReplyClicked(message: String?) {
        val text = replyEditText.getText().toString()
        val index = replyEditText.getSelectionStart()
        replyEditText.setText(MethodChecker.fromHtml(String.format(
                "%s %s %s",
                text.substring(0, index),
                message,
                text.substring(index)
        )))
        sendButton.performClick()
    }

    private fun setChannelInfoBottomSheet() {
        //TODO channel Info
    }

    private fun showOverlayBottomSheet(channelInfoViewModel: ChannelInfoViewModel) {
        if (channelInfoViewModel.overlayViewModel == null) {
            return
        }

        if (!::overlayDialog.isInitialized) {
            overlayDialog = CloseableBottomSheetDialog.createInstance(view.context) {
                analytics.eventClickCloseOverlayBackButton(channelInfoViewModel.channelId)
            }
            overlayDialog.setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog

                val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        val view = createOverlayView(channelInfoViewModel)
        overlayDialog.setCustomContentView(view, "",
                channelInfoViewModel.overlayViewModel.isCloseable)
        overlayDialog.setCanceledOnTouchOutside(channelInfoViewModel.overlayViewModel.isCloseable)

        overlayDialog.show()

        analytics.eventViewOverlay(channelInfoViewModel.channelId)

    }

    private fun createOverlayView(channelInfoViewModel: ChannelInfoViewModel): View {
        val overlayView = activity.layoutInflater.inflate(R.layout.layout_interupt_page, null)
        val interruptViewModel = channelInfoViewModel.overlayViewModel.interuptViewModel
        interruptViewModel?.let{
            if (!TextUtils.isEmpty(interruptViewModel.imageUrl)) {
                ImageHandler.loadImage2(overlayView.findViewById(R.id.ivImage) as ImageView, interruptViewModel.imageUrl, R.drawable.loading_page)
                overlayView.findViewById<ImageView>(R.id.ivImage).setOnClickListener {
                    if (!TextUtils.isEmpty(interruptViewModel.imageLink)) {
                        RouteManager.route(view.context, interruptViewModel.imageLink)
                        closeOverlayDialog()
                    }

                    analytics.eventClickOverlayImage(channelInfoViewModel)
                }
            } else
                (overlayView.findViewById(R.id.ivImage) as ImageView).visibility = View.GONE

            if (!TextUtils.isEmpty(interruptViewModel.title))
                (overlayView.findViewById<View>(R.id.tvTitle) as TextView).text = MethodChecker.fromHtml(interruptViewModel.title)
            else
                (overlayView.findViewById<View>(R.id.tvTitle) as TextView).visibility = View.GONE

            if (!TextUtils.isEmpty(interruptViewModel.description))
                (overlayView.findViewById<View>(R.id.tvDesc) as TextView).text = MethodChecker.fromHtml(interruptViewModel.description)
            else
                (overlayView.findViewById<View>(R.id.tvDesc) as TextView).visibility = View.GONE

            (overlayView.findViewById<View>(R.id.btnCta) as ButtonCompat).text = MethodChecker.fromHtml(interruptViewModel.btnTitle)
            (overlayView.findViewById<View>(R.id.btnCta) as ButtonCompat).setOnClickListener { view1 ->

                analytics.eventClickOverlayButton(channelInfoViewModel)

                if (!TextUtils.isEmpty(interruptViewModel.btnLink)) {
                    RouteManager.route(view.context, interruptViewModel.btnLink)
                }
                closeOverlayDialog()
            }
        }

        return overlayView
    }

    private fun closeOverlayDialog() {
        if (::overlayDialog.isInitialized && overlayDialog.isShowing) overlayDialog.dismiss()
    }

    private fun setDynamicBackground(backgroundUrl: String) {
        if (backgroundUrl.isBlank()) {
            activity.window?.setBackgroundDrawable(null)
        } else {
            ImageHandler.loadBackgroundImage(activity.window, backgroundUrl)
        }
    }

    override fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?) {

        toolbar.findViewById<TextView>(R.id.toolbar_title).text = title

        loadImageChannelBanner(view.context, bannerUrl, blurredBannerUrl)

        setToolbarParticipantCount(view.context, TextFormatter.format(totalView))

        when {
            title != null -> setVisibilityHeader(View.VISIBLE)
            else -> setVisibilityHeader(View.GONE)
        }
    }

    override fun loadImageChannelBanner(context: Context, bannerUrl: String?, blurredBannerUrl: String?) {
        if (TextUtils.isEmpty(blurredBannerUrl)) {
            ImageHandler.loadImageBlur(context, channelBanner, bannerUrl)
        } else {
            ImageHandler.LoadImage(channelBanner, blurredBannerUrl)
        }
    }

    private fun setToolbarParticipantCount(context: Context, totalParticipant: String) {
        val textParticipant = String.format("%s %s", totalParticipant, context.getString(R.string.view))
//        toolbar.subtitle = textParticipant
        toolbar.findViewById<TextView>(R.id.toolbar_subtitle).text = textParticipant
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    private fun setVisibilityHeader(visible: Int) {
        toolbar.visibility = visible
        channelBanner.visibility = visible
    }

    fun setSponsorData(adsId: String?, adsImageUrl: String?, adsName: String?) {
        if (adsId == null || adsImageUrl.isNullOrEmpty()) {
            sponsorLayout.visibility = View.GONE
        } else {
            sponsorLayout.visibility = View.VISIBLE
            ImageHandler.loadImage2(sponsorImage, adsImageUrl, R.drawable.loading_page)
            sponsorImage.setOnClickListener {}
        }

        if (sponsorLayout.visibility == View.VISIBLE) {
            //TODO analytics event view banner
        }
    }

    fun autoPlayVideo() {
        youtubeRunnable.postDelayed({ youTubePlayer?.play() }, PlayFragment.YOUTUBE_DELAY.toLong())
    }

    fun initVideoFragment(fragmentManager: FragmentManager, videoId: String) {
        videoContainer.visibility = View.GONE
        videoId.let {
            if (it.isEmpty()) return

            val videoFragment = fragmentManager.findFragmentById(R.id.video_container) as GroupChatVideoFragment
            videoFragment.run {
                videoContainer.visibility = View.VISIBLE
                sponsorLayout.visibility = View.GONE

                youTubePlayer?.let {
                    it.cueVideo(videoId)
                    autoPlayVideo()
                }

                videoFragment.initialize(
                        YoutubePlayerConstant.GOOGLE_API_KEY,
                        object : YouTubePlayer.OnInitializedListener {
                            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
                                if (!wasRestored) {
                                    try {
                                        youTubePlayer = player

                                        youTubePlayer?.let {
                                            it.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                                            it.setShowFullscreenButton(false)
                                            it.cueVideo(viewModel?.videoId)
                                            autoPlayVideo()

//                                            it.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
//                                                internal var TAG = "youtube"
//
//                                                override fun onPlaying() {
//                                                    Log.i(TAG, "onPlaying: ")
//                                                    if (onPlayTime == 0L) {
//                                                        onPlayTime = System.currentTimeMillis() / 1000L
//                                                    }
//                                                    analytics.eventClickAutoPlayVideo(getChannelInfoViewModel()!!.getChannelId())
//                                                }
//
//                                                override fun onPaused() {
//                                                    Log.i(TAG, "onPaused: ")
//                                                    onPauseTime = System.currentTimeMillis() / 1000L
//                                                }
//
//                                                override fun onStopped() {
//                                                    Log.i(TAG, "onStopped: ")
//                                                }
//
//                                                override fun onBuffering(b: Boolean) {
//                                                    Log.i(TAG, "onBuffering: ")
//                                                }
//
//                                                override fun onSeekTo(i: Int) {
//                                                    Log.i(TAG, "onSeekTo: ")
//                                                }
//                                            })
//
//                                            it.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
//                                                internal var TAG = "youtube"
//
//                                                override fun onLoading() {
//                                                    Log.i(TAG, "onLoading: ")
//                                                }
//
//                                                override fun onLoaded(s: String) {
//                                                    Log.i(TAG, "onLoaded: ")
//                                                }
//
//                                                override fun onAdStarted() {
//                                                    Log.i(TAG, "onAdStarted: ")
//                                                }
//
//                                                override fun onVideoStarted() {
//                                                    Log.i(TAG, "onVideoStarted: ")
//                                                }
//
//                                                override fun onVideoEnded() {
//                                                    Log.i(TAG, "onVideoEnded: ")
//                                                    onEndTime = System.currentTimeMillis() / 1000L
//                                                }
//
//                                                override fun onError(errorReason: YouTubePlayer.ErrorReason) {
//                                                    Log.i(TAG, errorReason.declaringClass() + " onError: " + errorReason.name)
//                                                }
//                                            })
                                        }

                                    } catch (e: Exception) {
                                        onInitializationFailure(provider, YouTubeInitializationResult.SERVICE_MISSING)
                                    }

                                }
                            }

                            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
                                Log.e(GroupChatActivity::class.java.simpleName, "Youtube Player View initialization failed")
                            }
                        }
                )

            }
        }
    }

    override fun onTotalViewChanged(channelId: String, totalView: String) {
        if (channelId == viewModel?.channelId) {
            setToolbarParticipantCount(view.context, TextFormatter.format(totalView))
        }
    }

    override fun onMessageReceived(it: Visitable<*>) {
        adapter.addIncomingMessage(it)
        adapter.notifyItemInserted(0)
        scrollToBottomWhenPossible()
    }

    private fun scrollToBottomWhenPossible() {
        if (layoutManager.findFirstVisibleItemPosition() == 0) {
            scrollToBottom()
        } else {
            newMessageCounter += 1
            showNewMessageReceived(newMessageCounter)
        }
    }

    private fun showNewMessageReceived(newMessageCounter: Int) {
        if (login.visibility != VISIBLE) {
            chatNotificationView.visibility = VISIBLE
        }
    }


    private fun setFloatingIcon(redirectUrl: String, iconUrl: String) {
        if (iconUrl.isBlank()
                || redirectUrl.isBlank()
                || !RouteManager.isSupportApplink(view.context, redirectUrl)) {
            return
        }

        if (iconUrl.toLowerCase().endsWith("gif")) {
            ImageHandler.loadGifFromUrl(webviewIcon, iconUrl, R.drawable.ic_loading_toped)
        } else {
            ImageHandler.LoadImage(webviewIcon, iconUrl)
        }

        webviewIcon.setOnClickListener {
            RouteManager.route(view.context, redirectUrl)
        }

    }

    private fun setDynamicIcon(redirectUrl: String) {
        if (redirectUrl.isBlank()) {
            return
        }

        dynamicIcon.setOnClickListener {
            showWebviewBottomSheet(redirectUrl)
        }
    }

    private fun setDynamicIconNotification(hasNotification: Boolean) {

        if (hasNotification) {
            //TODO set red dot
        } else {
            //TODO clear notification
        }
    }

    private fun showWebviewBottomSheet(url: String) {

        val bottomSheetDialog = PlayWebviewDialogFragment.createInstance(url)
        bottomSheetDialog.show(activity.supportFragmentManager, "Custom Bottom Sheet")

    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onSuccessSendMessage(pendingChatViewModel: PendingChatViewModel) {
        val viewModel = ChatViewModel(
                pendingChatViewModel.message!!,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                "",
                userSession.userId,
                userSession.name,
                userSession.profilePicture,
                false,
                false)
        adapter.addReply(viewModel)
        adapter.notifyItemInserted(0)
        setQuickReply(null)
        this.viewModel?.quickRepliesViewModel = null
        scrollToBottom()
    }

    override fun onErrorSendMessage(pendingChatViewModel: PendingChatViewModel, exception: Exception?) {

    }

    override fun afterSendMessage() {
        KeyboardHandler.DropKeyboard(view.context, view)
        onKeyboardHidden()
        replyEditText.text.clear()
    }

    override fun onInfoMenuClicked() {
        viewModel?.run {
            showInfoBottomSheet(this) {}
        }
    }

    override fun onReceiveGamificationNotif(model: GroupChatPointsViewModel) {
        adapter.addIncomingMessage(model)
        adapter.notifyItemInserted(0)
        scrollToBottom()
    }

    private fun showInfoBottomSheet(channelInfoViewModel: ChannelInfoViewModel,
                                    onDismiss : () ->Unit) {
        if (!::welcomeInfoDialog.isInitialized) {
            welcomeInfoDialog = CloseableBottomSheetDialog.createInstance(view.context) {}
        }

        welcomeInfoDialog.setOnDismissListener {
            onDismiss()
            analytics.eventClickJoin(channelInfoViewModel.channelId) }

        val welcomeInfoView = createWelcomeInfoView(channelInfoViewModel)
        welcomeInfoDialog.setOnShowListener() { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        welcomeInfoDialog.setContentView(welcomeInfoView, "")
        view.setOnClickListener(null)
        welcomeInfoDialog.show()

    }

    private fun createWelcomeInfoView(channelInfoViewModel: ChannelInfoViewModel): View {
        val welcomeInfoView = activity.layoutInflater.inflate(R.layout
                .channel_info_bottom_sheet_dialog, null)

        val image = welcomeInfoView.findViewById<ImageView>(R.id.product_image)
        val profile = welcomeInfoView.findViewById<ImageView>(R.id.prof_pict)
        val title = welcomeInfoView.findViewById<TextView>(R.id.title)
        val subtitle = welcomeInfoView.findViewById<TextView>(R.id.subtitle)
        val name = welcomeInfoView.findViewById<TextView>(R.id.name)
        val participant = welcomeInfoView.findViewById<TextView>(R.id.participant)

        participant.text = TextFormatter.format(channelInfoViewModel.totalView.toString())
        name.text = channelInfoViewModel.adminName
        title.text = channelInfoViewModel.title
        subtitle.text = channelInfoViewModel.description

        ImageHandler.loadImage2(image, channelInfoViewModel.image, R.drawable.loading_page)
        ImageHandler.loadImageCircle2(profile.context,
                profile,
                channelInfoViewModel.adminPicture,
                R.drawable.loading_page)

        return welcomeInfoView
    }

    private fun showPinnedMessage(viewModel: ChannelInfoViewModel) {
        if (!::pinnedMessageDialog.isInitialized) {
            pinnedMessageDialog = CloseableBottomSheetDialog.createInstance(view.context) {}
        }

        val pinnedMessageView = createPinnedMessageView(viewModel)
        pinnedMessageDialog.setOnShowListener() { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                pinnedMessageView.findViewById<ImageView>(R.id.thumbnail).visibility = View.VISIBLE
            }
        }

        pinnedMessageDialog.setContentView(pinnedMessageView, "Pinned Chat")
        view.setOnClickListener(null)
        pinnedMessageDialog.show()

    }

    private fun createPinnedMessageView(channelInfoViewModel: ChannelInfoViewModel): View {
        val view = activity.layoutInflater.inflate(R.layout
                .layout_pinned_message_expanded, null)
        ImageHandler.loadImageCircle2(activity, view.findViewById(R.id.pinned_message_avatar) as ImageView, channelInfoViewModel!!.adminPicture, R.drawable.ic_loading_toped_new)
        (view.findViewById<View>(R.id.chat_header).findViewById(R.id.nickname) as TextView).text =
                channelInfoViewModel.adminName
        channelInfoViewModel.pinnedMessageViewModel?.let {
            (view.findViewById(R.id.message) as TextView).text = it.message
            ImageHandler.loadImage(activity, view.findViewById(R.id.thumbnail), it.thumbnail, R
                    .drawable.loading_page)
            if (!TextUtils.isEmpty(it.imageUrl)) {
                view.findViewById<ImageView>(R.id.thumbnail).setOnClickListener {
                    (activity.applicationContext as GroupChatModuleRouter)
                            .openRedirectUrl(activity,
                                    channelInfoViewModel.pinnedMessageViewModel!!.imageUrl)
                }
            }
            view.findViewById<ImageView>(R.id.thumbnail).visibility = View.GONE
        }

        return view
    }

    override fun destroy() {
        youTubePlayer?.release()
        youtubeRunnable.removeCallbacksAndMessages(null)
    }
}
