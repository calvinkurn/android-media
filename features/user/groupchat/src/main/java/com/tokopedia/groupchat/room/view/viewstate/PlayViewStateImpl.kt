package com.tokopedia.groupchat.room.view.viewstate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Build
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
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.text.BackEditText
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.DynamicButtonsAdapter
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.GroupChatAdapter
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.QuickReplyAdapter
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.QuickReplyTypeFactoryImpl
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatVideoFragment
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.InteruptViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.common.design.QuickReplyItemDecoration
import com.tokopedia.groupchat.common.design.SpaceItemDecoration
import com.tokopedia.groupchat.common.util.TextFormatter
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.customview.StickyComponentHelper
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.groupchat.room.view.fragment.PlayWebviewDialogFragment
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * @author : Steven 13/02/19
 */
open class PlayViewStateImpl(
        var userSession: UserSessionInterface,
        var analytics: GroupChatAnalytics,
        var view: View,
        var activity: FragmentActivity,
        var listener: PlayContract.View,
        var fragmentManager: FragmentManager,
        quickReplyListener: ChatroomContract.QuickReply,
        imageListener: ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener,
        voteAnnouncementListener: ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener,
        sprintSaleViewHolderListener: ChatroomContract.ChatItem.SprintSaleViewHolderListener,
        groupChatPointsViewHolderListener: ChatroomContract.ChatItem.GroupChatPointsViewHolderListener,
        sendMessage: (viewModel: PendingChatViewModel) -> Unit,
        dynamicButtonClickListener: ChatroomContract.DynamicButtonItem.DynamicButtonListener,
        interactiveButtonClickListener: ChatroomContract.DynamicButtonItem.InteractiveButtonListener

) : PlayViewState {

    private var viewModel: ChannelInfoViewModel? = null
    private var stickyComponentViewModel: StickyComponentViewModel? = null
    private var dynamicButtonsViewModel: DynamicButtonsViewModel? = null
    private var videoStreamViewModel: VideoStreamViewModel? = null
    private var listMessage: ArrayList<Visitable<*>> = arrayListOf()

    private var quickReplyAdapter: QuickReplyAdapter
    private var dynamicButtonAdapter: DynamicButtonsAdapter
    private var adapter: GroupChatAdapter

    private var toolbar: Toolbar = view.findViewById(R.id.toolbar)
    private var channelBanner: ImageView = view.findViewById(R.id.channel_banner)
    private var sponsorLayout = view.findViewById<View>(R.id.sponsor_layout)
    private var sponsorImage = view.findViewById<ImageView>(R.id.sponsor_image)
    private var videoHorizontalContainer = view.findViewById<View>(R.id.video_horizontal)
    private var pinnedMessageContainer = view.findViewById<View>(R.id.pinned_message)
    private var chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_list)
    private var quickReplyRecyclerView = view.findViewById<RecyclerView>(R.id.quick_reply)
    private var chatNotificationView = view.findViewById<View>(R.id.layout_new_chat)
    private var youTubePlayer: YouTubePlayer? = null
    private var replyEditText: BackEditText = view.findViewById(R.id.reply_edit_text)
    private var login: View = view.findViewById(R.id.login)
    private var inputTextWidget: View = view.findViewById(R.id.bottom)
    private var sendButton: View = view.findViewById(R.id.button_send)
    private var dynamicButtonRecyclerView: RecyclerView = view.findViewById(R.id.buttons)
    private var liveIndicator: View = toolbar.findViewById(R.id.toolbar_live)
    private var stickyComponent: View = view.findViewById(R.id.sticky_component)
    private val webviewIcon = view.findViewById<ImageView>(R.id.webview_icon)
    private var errorView: View = view.findViewById(R.id.card_retry)
    private var loadingView: View = view.findViewById(R.id.loading_view)
    private var hideVideoToggle: View = view.findViewById(R.id.hide_video_toggle)
    private var showVideoToggle: View = view.findViewById(R.id.show_video_toggle)
    private var spaceChatVideo: View = view.findViewById(R.id.top_space_guideline)
    private var interactionGuideline = view.findViewById<FrameLayout>(R.id.interaction_button_guideline)
    private var bufferContainer = view.findViewById<View>(R.id.video_buffer_container)
    private var bufferDimContainer = view.findViewById<View>(R.id.dim_video_vertical)
    private var videoFragment = fragmentManager.findFragmentById(R.id.video_container) as GroupChatVideoFragment

    private lateinit var overlayDialog: CloseableBottomSheetDialog
    private lateinit var pinnedMessageDialog: CloseableBottomSheetDialog
    private lateinit var webviewDialog: PlayWebviewDialogFragment

    private var youtubeRunnable: Handler = Handler()
    private var layoutManager: LinearLayoutManager

    private var newMessageCounter: Int = 0
    private var onPlayTime: Long = 0
    private var onPauseTime: Long = 0
    private var onEndTime: Long = 0
    private var onLeaveTime: Long = 0
    private val onTrackingTime: Long = 0

    private var defaultBackground = arrayListOf(
            R.drawable.bg_play_1,
            R.drawable.bg_play_2,
            R.drawable.bg_play_3
    )

    private var defaultType = arrayListOf(
            "default", "default1", "default2", "default3")

    private var interactionAnimationHelper: InteractionAnimationHelper
    private var overflowMenuHelper: OverflowMenuHelper
    private var videoVerticalHelper: VideoVerticalHelper
    private var videoHorizontalHelper: VideoHorizontalHelper
    private var sponsorHelper: SponsorHelper
    private var welcomeHelper: PlayWelcomeHelper

    init {
        val groupChatTypeFactory = GroupChatTypeFactoryImpl(
                imageListener,
                voteAnnouncementListener,
                sprintSaleViewHolderListener,
                groupChatPointsViewHolderListener
        )

        adapter = GroupChatAdapter.createInstance(groupChatTypeFactory, listMessage)
        layoutManager = LinearLayoutManager(view.context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        chatRecyclerView.layoutManager = layoutManager
        chatRecyclerView.adapter = adapter
        val itemDecoration = SpaceItemDecoration(view.context
                .resources.getDimension(R.dimen.space_play_chat).toInt())
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

        var dynamicButtonTypeFactory = DynamicButtonTypeFactoryImpl(
                dynamicButtonClickListener, interactiveButtonClickListener, interactionGuideline)

        dynamicButtonRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        var buttonSpace = SpaceItemDecoration(activity.getResources()
                .getDimension(R.dimen.dp_8).toInt(), 2)
        dynamicButtonAdapter = DynamicButtonsAdapter(dynamicButtonTypeFactory)
        dynamicButtonRecyclerView.adapter = dynamicButtonAdapter
        dynamicButtonRecyclerView.addItemDecoration(buttonSpace)

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

        setBottomView()

        login.setOnClickListener {
            listener.onLoginClicked(viewModel?.channelId)
        }

        chatNotificationView.setOnClickListener {
            attemptResetNewMessageCounter()
            scrollToBottom()
        }

        sendButton.setOnClickListener {
            val emp = !TextUtils.isEmpty(replyEditText.text.toString().trim { it <= ' ' })
            if (emp) {
                val pendingChatViewModel = PendingChatViewModel(checkText(replyEditText.text.toString()),
                        userSession.userId,
                        userSession.name,
                        userSession.profilePicture,
                        false)
                sendMessage(pendingChatViewModel)
            }
        }
        errorView.setOnClickListener {  }

        interactionAnimationHelper = InteractionAnimationHelper(interactionGuideline)
        var videoVerticalContainer = (activity as PlayActivity).findViewById<FrameLayout>(R.id.playerView)
        var rootView = (activity as PlayActivity).findViewById<View>(R.id.root_view)
        overflowMenuHelper = OverflowMenuHelper(
                viewModel,
                activity,
                onInfoMenuClicked(),
                toggleHorizontalVideo(),
                videoHorizontalContainer,
                changeQualityVideoVertical(),
                videoVerticalContainer,
                toggleVerticalVideo()
        )
        videoVerticalHelper = VideoVerticalHelper(
                bufferContainer,
                bufferDimContainer,
                activity.supportFragmentManager,
                videoVerticalContainer,
                rootView,
                setChatListHasSpaceOnTop()
        )
        videoHorizontalHelper = VideoHorizontalHelper(
                viewModel,
                hideVideoToggle,
                showVideoToggle,
                videoHorizontalContainer,
                youTubePlayer,
                setChatListHasSpaceOnTop(),
                liveIndicator,
                analytics
        )
        sponsorHelper = SponsorHelper(viewModel, sponsorLayout, sponsorImage, analytics, listener)
        welcomeHelper = PlayWelcomeHelper(viewModel, analytics, activity, view)

        errorView.setOnClickListener {}
    }

    override fun onErrorVideoVertical() {
        videoVerticalHelper.showRetryOnly()
    }

    override fun onDynamicButtonUpdated(it: DynamicButtonsViewModel) {
        viewModel?.let { viewModel ->
            dynamicButtonsViewModel = it

            webviewIcon.hide()
            if (!it.floatingButton.imageUrl.isBlank() && !it.floatingButton.contentLinkUrl.isBlank()) {
                it.floatingButton.run {
                    setFloatingIcon(this)
                }
            }

            dynamicButtonAdapter.clearList()
            dynamicButtonRecyclerView.hide()
            if (!it.listDynamicButton.isEmpty()) {
                analytics.eventViewDynamicButtons(viewModel, it.listDynamicButton)
                dynamicButtonAdapter.addList(it.listDynamicButton)
                dynamicButtonRecyclerView.show()
            }

            if(it.interactiveButton.isEnabled) {
                dynamicButtonAdapter.addList(it.interactiveButton)
                interactionAnimationHelper.updateInteractionIcon(it.interactiveButton.balloonList)
                dynamicButtonRecyclerView.show()
            } else {
                interactionAnimationHelper.destroy()
            }
        }
    }

    override fun onInteractiveButtonViewed(anchorView: LottieAnimationView) {
        analytics.eventViewInteractionButton(viewModel?.channelId)
        interactionAnimationHelper.fakeShot(anchorView)
    }

    override fun onErrorGetDynamicButtons() {
        dynamicButtonAdapter.addList(ArrayList())
    }

    override fun onStickyComponentUpdated(stickyComponentViewModel: StickyComponentViewModel) {
        this.stickyComponentViewModel = stickyComponentViewModel
        showStickComponent(stickyComponentViewModel)
    }

    override fun onErrorGetStickyComponent() {
        hideStickyComponent()
    }

    private fun hideStickyComponent() {
        stickyComponent.visibility = View.GONE
    }

    private fun showStickComponent(item: StickyComponentViewModel?) {
        stickyComponent.hide()
        item?.run {
            if (title.isNullOrEmpty() || !userSession.isLoggedIn) return
            StickyComponentHelper.setView(stickyComponent, item)
            stickyComponent.setOnClickListener {
                viewModel?.let {
                    analytics.eventClickStickyComponent(item, it)
                    var applink = RouteManager.routeWithAttribution(activity, item.redirectUrl,
                            GroupChatAnalytics.generateTrackerAttribution(
                                    GroupChatAnalytics.ATTRIBUTE_PROMINENT_BUTTON,
                                    it.channelUrl,
                                    it.title
                            ))
                    listener.openRedirectUrl(applink)
                }

            }

            stickyComponent.animate().setDuration(200)
                    .alpha(1f)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            viewModel?.let {
                                analytics.eventShowStickyComponent(item, it)
                            }
                            stickyComponent.show()
                        }
                    })

            if (item.stickyTime != 0) {
                Observable.timer(item.stickyTime.toLong(), TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            stickyComponent.animate().setDuration(200)
                                    .alpha(0f)
                                    .setListener(object : AnimatorListenerAdapter() {
                                        override fun onAnimationEnd(animation: Animator) {
                                            stickyComponent.hide()
                                            stickyComponentViewModel = null
                                        }
                                    })
                        }
            }
        }
    }

    override fun onKeyboardHidden() {
        showWidgetAboveInput(true)
        inputTextWidget.setBackgroundColor(MethodChecker.getColor(view.context, R.color.transparent))
        sendButton.hide()
        dynamicButtonRecyclerView.show()
        scrollToBottom()

    }

    final override fun setBottomView() {
        showLoginButton(!userSession.isLoggedIn)
        if (userSession.isLoggedIn) {
            onKeyboardHidden()
        }
    }

    private fun onKeyboardShown() {
        showWidgetAboveInput(false)
        inputTextWidget.setBackgroundColor(MethodChecker.getColor(view.context, R.color.play_transparent))
        sendButton.show()
        dynamicButtonRecyclerView.hide()
//            setSprintSaleIcon(null)
        scrollToBottom()
    }

    private fun checkText(replyText: String): String {
        return replyText.replace("<", "&lt;")
    }

    private fun scrollToBottom() {
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
        showBottomSheetFirstTime(it)
        setDefaultBackground()
        onSuccessGetInfo(it)

    }

    override fun onSuccessGetInfo(it: ChannelInfoViewModel) {
        loadingView.hide()
        var needCueVideo = viewModel?.videoId != it.videoId
        viewModel = it
        if (it.isFreeze) {
            onChannelFrozen(it.channelId)
            listener.onToolbarEnabled(false)
            return
        }

        setToolbarData(it.title, it.bannerUrl, it.totalView, it.blurredBannerUrl)
        if(needCueVideo) {
            onVideoHorizontalUpdated(VideoViewModel(it.videoId, it.isVideoLive))
        }
        showLoginButton(!userSession.isLoggedIn)
        it.settingGroupChat?.maxChar?.let {
            replyEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(it))
        }
        errorView.hide()

        setPinnedMessage(it)
        onBackgroundUpdated(it.backgroundViewModel)
        viewModel?.infoUrl = it.infoUrl
        autoAddSprintSale()

        setBottomView()
        videoHorizontalHelper.assignViewModel(it)
        sponsorHelper.assignViewModel(it)
        sponsorHelper.setSponsor()
        overflowMenuHelper.assignViewModel(it)
    }

    fun setDefaultBackground() {
        var background = defaultBackground[0]
        activity.window?.setBackgroundDrawable(MethodChecker.getDrawable(view.context, background))
    }

    override fun onBackgroundUpdated(it: BackgroundViewModel) {
        var background: Int
        lateinit var url: String
        it.let {
            var index = defaultType.indexOf(it.default)
            background = defaultBackground[Math.max(0, index - 1)]
            url = it.url

            if (url.isBlank()) {
                activity.window?.setBackgroundDrawable(MethodChecker.getDrawable(view.context, background))
            } else {
                ImageHandler.loadBackgroundImage(activity.window, url)
            }
        }
    }

    /**
     * show overlay behind channel info
     */
    private fun showBottomSheetFirstTime(it: ChannelInfoViewModel) {
        welcomeHelper.showInfoBottomSheet(it) {
            if (it.overlayViewModel != null
                    && it.overlayViewModel.interuptViewModel != null
                    && !it.overlayViewModel.interuptViewModel!!.btnLink!!.isBlank())
                showOverlayBottomSheet(it)
        }
    }

    override fun getChannelInfo(): ChannelInfoViewModel? {
        return viewModel
    }

    private fun showWidgetAboveInput(isShow: Boolean) {
        if (isShow) {
            viewModel?.let {
                setPinnedMessage(it)
                setQuickReply(it.quickRepliesViewModel)
                showStickComponent(stickyComponentViewModel)
            }
        } else {
            hideStickyComponent()
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
        viewModel?.let { viewModel ->
            viewModel.adsImageUrl = it.adsUrl
            viewModel.adsId = it.adsId
            viewModel.adsLink = it.adsLink
            sponsorHelper.assignViewModel(viewModel)
            sponsorHelper.setSponsor()
        }
    }

    override fun onChannelFrozen(channelId: String) {
        viewModel?.let { viewModel ->
            var channelName = viewModel.title
            if (channelId == viewModel.channelId) {
                setEmptyState(R.drawable.ic_play_end,
                        view.context.resources.getString((R.string.play_has_end), channelName),
                        getStringResource(R.string.play_next_event),
                        getStringResource(R.string.play_start_shopping),
                        listener::onFinish)
                loadingView.hide()
                errorView.show()
                setToolbarWhite()
                showLoginButton(false)
            }
        }

    }

    override fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel) {
        setQuickReply(it.list)
    }

    override fun banUser(userId: String) {
        if (userId == userSession.userId) {
            viewModel?.banViewModel?.let {
                var dialog = Dialog(activity, Dialog.Type.RETORIC)
                dialog.setTitle(it.bannedTitle)
                dialog.setDesc(it.bannedMessage)
                dialog.setBtnOk(it.bannedButtonTitle)
                dialog.setOnOkClickListener {
                    listener.backToChannelList()
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }


    private fun getStringResource(id: Int): String {
        return view.context.resources.getString(id)
    }

    override fun onPinnedMessageUpdated(it: PinnedMessageViewModel) {
        viewModel?.run {
            pinnedMessageViewModel = it
            setPinnedMessage(viewModel)
        }

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
                if (it.title.isBlank()) {
                    pinnedMessageContainer.visibility = View.GONE
                    return
                }
                (pinnedMessageContainer.findViewById(R.id.message) as TextView).text =
                        it.title
                (pinnedMessageContainer.findViewById(R.id.nickname) as TextView).text =
                        channelInfoViewModel.adminName
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
        val text = replyEditText.text.toString()
        val index = replyEditText.selectionStart
        replyEditText.setText(MethodChecker.fromHtml(String.format(
                "%s %s %s",
                text.substring(0, index),
                message,
                text.substring(index)
        )))
        sendButton.performClick()

        analytics.eventClickQuickReply(
                String.format("%s - %s", viewModel?.channelId, message))

    }

    private fun showOverlayBottomSheet(channelInfoViewModel: ChannelInfoViewModel) {
        if (channelInfoViewModel.overlayViewModel == null) {
            return
        }

        if (!::overlayDialog.isInitialized) {
            overlayDialog = CloseableBottomSheetDialog.createInstance(view.context,
                    { analytics.eventClickCloseOverlayCloseButton(channelInfoViewModel.channelId) },
                    { analytics.eventClickCloseOverlayBackButton(channelInfoViewModel.channelId) })

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
        interruptViewModel?.let {
            if (!TextUtils.isEmpty(interruptViewModel.imageUrl)) {
                ImageHandler.loadImage2(overlayView.findViewById(R.id.ivImage) as ImageView, interruptViewModel.imageUrl, R.drawable.loading_page)
                overlayView.findViewById<ImageView>(R.id.ivImage).setOnClickListener {
                    if (!TextUtils.isEmpty(interruptViewModel.imageLink)) {
                        var applink = RouteManager.routeWithAttribution(view.context, interruptViewModel.imageLink,
                                GroupChatAnalytics.generateTrackerAttribution(
                                        GroupChatAnalytics.ATTRIBUTE_OVERLAY_IMAGE,
                                        channelInfoViewModel.channelUrl,
                                        channelInfoViewModel.title
                                ))
                        listener.openRedirectUrl(applink)
                        closeOverlayDialog()
                    }

                    analytics.eventClickOverlayImage(channelInfoViewModel, channelInfoViewModel.overlayViewModel.overlayId, interruptViewModel.btnTitle,
                            interruptViewModel.imageUrl)
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

                analytics.eventClickOverlayButton(channelInfoViewModel,
                        channelInfoViewModel.overlayViewModel.overlayId,
                        interruptViewModel.btnTitle,
                        interruptViewModel.imageUrl)

                if (!TextUtils.isEmpty(interruptViewModel.btnLink)) {
                    var applink = RouteManager.routeWithAttribution(view.context, interruptViewModel.btnLink,
                            GroupChatAnalytics.generateTrackerAttribution(
                                    GroupChatAnalytics.ATTRIBUTE_OVERLAY_BUTTON,
                                    channelInfoViewModel.channelUrl,
                                    channelInfoViewModel.title
                            ))
                    listener.openRedirectUrl(applink)
                }
                closeOverlayDialog()
            }
        }

        return overlayView
    }

    private fun closeOverlayDialog() {
        if (::overlayDialog.isInitialized && overlayDialog.isShowing) overlayDialog.dismiss()
    }

    override fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?) {

        toolbar.setBackgroundResource(R.color.transparent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }

        toolbar.findViewById<TextView>(R.id.toolbar_title).text = title
        setToolbarParticipantCount(view.context, TextFormatter.format(totalView))

        toolbar.findViewById<TextView>(R.id.toolbar_title).setTextColor(MethodChecker.getColor(activity, R.color.white))
        toolbar.findViewById<TextView>(R.id.toolbar_subtitle).setTextColor(MethodChecker.getColor(activity, R.color.white))

        when {
            title != null -> setVisibilityHeader(View.VISIBLE)
            else -> setVisibilityHeader(View.GONE)
        }
    }

    private fun setToolbarWhite() {
        sponsorHelper.hideSponsor()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
            toolbar.setBackgroundResource(R.color.white)
        } else {
            toolbar.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow)
        }
        var title = toolbar.findViewById<TextView>(R.id.toolbar_title)
        var subtitle = toolbar.findViewById<TextView>(R.id.toolbar_subtitle)
        subtitle.hide()
        toolbar.findViewById<TextView>(R.id.toolbar_live).hide()
        title.text = getStringResource(R.string.play_title)
        subtitle.text = ""
        title.setTextColor(MethodChecker.getColor(title.context, R.color.black_70))
        subtitle.setTextColor(MethodChecker.getColor(title.context, R.color.black_70))
    }

    override fun errorViewShown(): Boolean {
        return errorView.isShown
    }

    private fun setToolbarParticipantCount(context: Context, totalParticipant: String) {
        val textParticipant = String.format("%s %s", totalParticipant, context.getString(R.string.view))
        toolbar.findViewById<TextView>(R.id.toolbar_subtitle).text = textParticipant
        toolbar.findViewById<TextView>(R.id.toolbar_subtitle).show()
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    private fun setVisibilityHeader(visible: Int) {
        toolbar.visibility = visible
        channelBanner.visibility = visible
    }

    override fun onVideoVerticalUpdated(it: VideoStreamViewModel) {
        var it = VideoStreamViewModel(
                true,
                false,
                "https://scontent-sin6-1.cdninstagram.com/vp/2a1b5cba5df6f097605c516a3c7d58d3/5D1FB5C0/t50.12441-16/55450199_131136041277815_5339080047835994825_n.mp4?_nc_ht=scontent-sin6-1.cdninstagram.com",
                "https://scontent-sin6-2.cdninstagram.com/vp/37c031a24fd60eb087a6c1b1072ad5d8/5D208424/t50.12441-16/53306725_332584844027284_3716503313000746737_n.mp4?_nc_ht=scontent-sin6-2.cdninstagram.com"
        )
        if(it.isActive) {
            videoVerticalHelper.setData(it)
            videoVerticalHelper.playVideo(VideoVerticalHelper.VIDEO_480)
            overflowMenuHelper.setQualityVideo(VideoVerticalHelper.VIDEO_480)
            videoHorizontalHelper.hideVideoAndToggle()
            videoHorizontalHelper.hideAllToggle()
        } else {
            videoVerticalHelper.stopVideo()
            setChatListHasSpaceOnTop().invoke(VideoVerticalHelper.VERTICAL_WITHOUT_VIDEO)
        }
    }

    override fun autoPlayVideo() {
        try {
            youtubeRunnable?.postDelayed({ youTubePlayer?.play() }, PlayFragment.YOUTUBE_DELAY.toLong())
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onVideoHorizontalUpdated(it: VideoViewModel) {
        viewModel?.let { viewModel ->
            viewModel.videoId = it.videoId
            viewModel.isVideoLive = it.videoLive
            initVideoFragment(it.videoId, it.videoLive)
            videoHorizontalHelper.assignViewModel(viewModel)
            sponsorHelper.assignViewModel(viewModel)
            sponsorHelper.setSponsor()
            overflowMenuHelper.assignViewModel(viewModel)
            videoVerticalHelper.stopVideo()
        }
    }

    private fun initVideoFragment(videoId: String, isVideoLive: Boolean) {
        videoHorizontalHelper.hideVideoAndToggle()
        if(!videoId.isNullOrBlank()){
            val videoFragment = fragmentManager.findFragmentById(R.id.video_container) as GroupChatVideoFragment
            videoFragment.run {
                videoHorizontalHelper.showVideoOnly(isVideoLive)
                sponsorHelper.hideSponsor()
                youTubePlayer?.let {
                    it.cueVideo(videoId)
                    autoPlayVideo()
                }
                videoFragment.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, getVideoInitializer(videoId))
            }
        } else {
            setChatListHasSpaceOnTop().invoke(VideoHorizontalHelper.HORIZONTAL_WITHOUT_VIDEO)
            videoHorizontalHelper.hideVideoAndToggle()
        }
    }

    fun getVideoInitializer(videoId: String): YouTubePlayer.OnInitializedListener{
        return object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
                if (!wasRestored) {
                    try {
                        youTubePlayer = player

                        youTubePlayer?.let {
                            it.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                            it.setShowFullscreenButton(false)
                            it.cueVideo(videoId)
                            autoPlayVideo()

                            it.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
                                internal var TAG = "youtube"

                                override fun onPlaying() {
                                    if (onPlayTime == 0L) {
                                        onPlayTime = System.currentTimeMillis() / 1000L
                                    }
                                    analytics.eventClickAutoPlayVideo(viewModel?.channelId)
                                    videoHorizontalHelper.onPlayed()
                                }

                                override fun onPaused() {
                                    videoHorizontalHelper.onPaused()
                                    analytics.eventClickPauseVideo(viewModel?.channelId)
                                    onPauseTime = System.currentTimeMillis() / 1000L
                                }

                                override fun onStopped() {
                                }

                                override fun onBuffering(b: Boolean) {
                                }

                                override fun onSeekTo(i: Int) {}
                            })

                            it.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                                override fun onLoading() {
                                }

                                override fun onLoaded(s: String) {
                                }

                                override fun onAdStarted() {
                                }

                                override fun onVideoStarted() {
                                }

                                override fun onVideoEnded() {
                                    onEndTime = System.currentTimeMillis() / 1000L
                                }

                                override fun onError(errorReason: YouTubePlayer.ErrorReason) {
                                }
                            })

                            videoHorizontalHelper.assignPlayer(it)
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
    }

    override fun getDurationWatchVideo(): String? {
        if (onPlayTime == 0L) return null
        if (onEndTime != 0L) {
            return (onEndTime - onPlayTime).toString()
        } else if (onPauseTime != 0L) {
            return (onPauseTime - onPlayTime).toString()
        } else {
            onLeaveTime = System.currentTimeMillis() / 1000L
            return (onLeaveTime - onPlayTime).toString()
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


    private fun setFloatingIcon(floatingButton: DynamicButton) {
        webviewIcon.hide()
        if (floatingButton.imageUrl.isBlank()
                || floatingButton.contentLinkUrl.isBlank()) {
            return
        }

        if (floatingButton.imageUrl.toLowerCase().endsWith("gif")) {
            ImageHandler.loadGifFromUrl(webviewIcon, floatingButton.imageUrl, R.drawable.ic_loading_toped)
        } else {
            ImageHandler.LoadImage(webviewIcon, floatingButton.imageUrl)
        }
        webviewIcon.show()

        viewModel?.let {
            analytics.eventViewProminentButton(it, floatingButton)
        }

        webviewIcon.setOnClickListener {
            viewModel?.let {
                analytics.eventClickProminentButton(it, floatingButton)

                if (!userSession.isLoggedIn) {
                    listener.onLoginClicked(it.channelId)
                } else {
                    val applink = RouteManager.routeWithAttribution(view.context, floatingButton.contentLinkUrl, GroupChatAnalytics.generateTrackerAttribution(
                            GroupChatAnalytics.ATTRIBUTE_PROMINENT_BUTTON,
                            it.channelUrl,
                            it.title
                    ))
                    listener.onFloatingIconClicked(floatingButton, applink)
                }
            }
        }

    }

    private fun showWebviewBottomSheet(url: String) {

        if (url.isBlank())
            return

        if (!::webviewDialog.isInitialized) {
            webviewDialog = PlayWebviewDialogFragment.createInstance(url)
        } else {
            webviewDialog.setUrl(url)
        }

        if (!webviewDialog.isAdded)
            webviewDialog.show(activity.supportFragmentManager, "Webview Bottom Sheet")
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

    override fun onErrorGetInfo(it: String) {
        setEmptyState(R.drawable.ic_play_overload,
                getStringResource(R.string.error_overload_play),
                it.replace("channelName", viewModel?.title ?: "", false),
                getStringResource(R.string.title_try_again),
                listener::onRetryGetInfo)
        loadingView.hide()
        errorView.show()
        setToolbarWhite()
    }

    override fun onNoInternetConnection() {
        setEmptyState(R.drawable.ic_play_no_connection,
                getStringResource(R.string.no_connection_play),
                getStringResource(R.string.try_connection_play),
                getStringResource(R.string.title_try_again),
                listener::onRetryGetInfo)
        loadingView.hide()
        errorView.show()
        setToolbarWhite()
    }

    override fun onChannelDeleted() {
        setEmptyState(R.drawable.ic_group_chat,
                getStringResource(R.string.next_event_warn),
                getStringResource(R.string.join_and_win_prize),
                getStringResource(R.string.check_next_event_schedule),
                checkNextEvent())
    }

    private fun checkNextEvent(): () -> Unit {
        return {
            val adsLink = "tokopedia://webview?url=https://tokopedia.link/playfreezestate"
            listener.openRedirectUrl(adsLink)
            activity?.finish()
        }
    }

    override fun afterSendMessage() {
        KeyboardHandler.DropKeyboard(view.context, view)
        onKeyboardHidden()
        replyEditText.text.clear()
    }

    private fun onInfoMenuClicked(): ()-> Unit{
        return {
            viewModel?.run {
                showWebviewBottomSheet(infoUrl)
            }
        }
    }

    override fun onOverflowMenuClicked() {
        viewModel?.run {
            overflowMenuHelper.showOverflowMenuBottomSheet()
        }
    }

    override fun onReceiveGamificationNotif(model: GroupChatPointsViewModel) {
        adapter.addIncomingMessage(model)
        adapter.notifyItemInserted(0)
        scrollToBottom()
    }

    override fun onReceiveOverlayMessageFromWebsocket(channelInfoViewModel: ChannelInfoViewModel) {
        viewModel = channelInfoViewModel
        viewModel?.let {
            if (userSession.isLoggedIn) {
                if (welcomeHelper.isShowingDialog()) {
                    welcomeHelper.setOnDismissListener{checkShowWhichBottomSheet(channelInfoViewModel)}
                } else if (::pinnedMessageDialog.isInitialized && pinnedMessageDialog.isShowing) {
                    pinnedMessageDialog.setOnDismissListener { onDismiss ->
                        checkShowWhichBottomSheet(channelInfoViewModel)

                    }
                } else if (::overlayDialog.isInitialized && overlayDialog.isShowing) {
                    overlayDialog.dismiss()
                    checkShowWhichBottomSheet(channelInfoViewModel)

                } else {
                    checkShowWhichBottomSheet(channelInfoViewModel)
                }

            }
        }
    }

    private fun checkShowWhichBottomSheet(channelInfoViewModel: ChannelInfoViewModel) {
        if (channelInfoViewModel.overlayViewModel == null)
            return

        when (channelInfoViewModel.overlayViewModel.type) {
            OverlayViewModel.TYPE_CTA -> showOverlayBottomSheet(channelInfoViewModel)
            OverlayViewModel.TYPE_WEBVIEW -> showWebviewBottomSheet(channelInfoViewModel
                    .overlayViewModel.redirectUrl)
        }
    }

    override fun onReceiveCloseOverlayMessageFromWebsocket() {
        if (::overlayDialog.isInitialized && overlayDialog.isShowing) {
            overlayDialog.dismiss()
        } else if (::webviewDialog.isInitialized && webviewDialog.isVisible) {
            webviewDialog.dismiss()
        }
    }

    private fun showPinnedMessage(viewModel: ChannelInfoViewModel) {
        if (!::pinnedMessageDialog.isInitialized) {
            pinnedMessageDialog = CloseableBottomSheetDialog.createInstanceRounded(view.context)
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

        pinnedMessageDialog.setCustomContentView(pinnedMessageView, "", false)
        pinnedMessageDialog.show()

    }

    private fun createPinnedMessageView(channelInfoViewModel: ChannelInfoViewModel): View {
        val view = activity.layoutInflater.inflate(R.layout
                .layout_pinned_message_expanded, null)
        (view.findViewById(R.id.nickname) as TextView).text = getStringResource(R.string.from) + " " + channelInfoViewModel.adminName
        channelInfoViewModel.pinnedMessageViewModel?.let {
            (view.findViewById(R.id.message) as TextView).text = it.message
            ImageHandler.loadImage(activity, view.findViewById(R.id.thumbnail), it.thumbnail, R
                    .drawable.loading_page)
            if (!TextUtils.isEmpty(it.imageUrl)) {
                view.findViewById<ImageView>(R.id.thumbnail).setOnClickListener {
                    listener.openRedirectUrl(channelInfoViewModel.pinnedMessageViewModel!!.imageUrl)
                }
            }
            view.findViewById<ImageView>(R.id.thumbnail).visibility = View.GONE
        }
        view.setOnClickListener(null)
        return view
    }

    private fun setEmptyState(imageResId: Int, titleText: String, bodyText: String, buttonText: String, action: () -> Unit) {
        val imageView = errorView.findViewById<ImageView>(R.id.image)
        val title = errorView.findViewById<TextView>(R.id.title)
        val body = errorView.findViewById<TextView>(R.id.body)
        val button = errorView.findViewById<View>(R.id.button)
        val buttonTxt = errorView.findViewById<TextView>(R.id.button_text)

        ImageHandler.loadImageWithId(imageView, imageResId)
        title.text = titleText
        body.text = bodyText
        buttonTxt.text = buttonText
        button.setOnClickListener {
            action()
        }
    }

    private fun setChatListHasSpaceOnTop(): (Int) -> Unit {
        return {
            var layoutParams = spaceChatVideo.layoutParams
            layoutParams.height = it.dpToPx(view.context.resources.displayMetrics)
            spaceChatVideo.layoutParams = layoutParams
            spaceChatVideo.show()

            val fadingEdgeLength = when (it){
                VideoVerticalHelper.VERTICAL_WITH_VIDEO -> view.context.resources.getDimensionPixelSize(R.dimen.dp_0)
                VideoHorizontalHelper.HORIZONTAL_WITH_VIDEO -> view.context.resources.getDimensionPixelSize(R.dimen.dp_8)
                else -> {
                    view.context.resources.getDimensionPixelSize(R.dimen.dp_24)
                }
            }
            chatRecyclerView.setFadingEdgeLength(fadingEdgeLength)
            chatRecyclerView.invalidate()
        }
    }

    private fun toggleHorizontalVideo(): (Boolean) -> Unit {
        return {
            if(it) {
                videoHorizontalHelper.showVideo()
                videoVerticalHelper.stopVideo()
            } else {
                videoHorizontalHelper.hideVideo()
            }
        }
    }

    private fun toggleVerticalVideo(): (Boolean) -> Unit {
        return {
            if(it) {
                videoHorizontalHelper.hideVideoAndToggle()
                youTubePlayer?.release()
                youTubePlayer = null
            } else {
                videoVerticalHelper.stopVideo()
            }
        }
    }

    private fun changeQualityVideoVertical(): (Int) -> Unit {
        return {
            videoVerticalHelper.playVideo(it)
        }
    }

    override fun onShowOverlayCTAFromDynamicButton(button: DynamicButton) {
        viewModel?.let {

            analytics.eventClickOverlayCTAButton(it.channelId, button.contentButtonText)

            it.overlayViewModel = OverlayViewModel(
                    button.buttonId,
                    OverlayViewModel.TYPE_CTA,
                    button.contentLinkUrl,
                    true,
                    0,
                    InteruptViewModel(
                            "",
                            button.contentText,
                            button.contentImageUrl,
                            button.contentLinkUrl,
                            button.contentButtonText,
                            button.contentLinkUrl
                    )
            )

            showOverlayBottomSheet(it)
        }

    }

    override fun onSprintSaleReceived(it: SprintSaleAnnouncementViewModel) {
        var item = SprintSaleViewModel(
                campaignId = it.campaignId,
                listProduct = it.listProducts,
                campaignName = it.campaignName,
                startDate = it.startDate,
                endDate = it.endDate,
                redirectUrl = it.redirectUrl.toString(),
                sprintSaleType = it.sprintSaleType)
        viewModel?.sprintSaleViewModel = item
    }

    override fun onShowOverlayFromVoteComponent(voteUrl: String) {
        showWebviewBottomSheet(voteUrl)
    }

    override fun onShowOverlayWebviewFromDynamicButton(it: DynamicButton) {
        showWebviewBottomSheet(it.contentLinkUrl)
    }

    override fun destroy() {
        youTubePlayer?.release()
        youtubeRunnable.removeCallbacksAndMessages(null)
        interactionAnimationHelper.destroy()
    }

    override fun autoAddSprintSale() {

        var sprintSaleViewModel = viewModel?.sprintSaleViewModel
        var channelInfoViewModel = viewModel

        if (sprintSaleViewModel != null
                && isValidSprintSale(sprintSaleViewModel)
                && sprintSaleViewModel.sprintSaleType != null
                && !sprintSaleViewModel.sprintSaleType!!.equals(SprintSaleViewModel.TYPE_UPCOMING, ignoreCase = true)
                && !sprintSaleViewModel.sprintSaleType!!.equals(SprintSaleViewModel.TYPE_FINISHED, ignoreCase = true)
                && channelInfoViewModel != null) {

//            trackViewSprintSaleComponent(sprintSaleViewModel)

            val sprintSaleAnnouncementViewModel = SprintSaleAnnouncementViewModel(
                    sprintSaleViewModel.campaignId!!,
                    Date().time,
                    Date().time,
                    "0",
                    "0",
                    channelInfoViewModel.adminName,
                    channelInfoViewModel.adminPicture,
                    false,
                    true,
                    sprintSaleViewModel.redirectUrl,
                    sprintSaleViewModel.listProduct!!,
                    sprintSaleViewModel.campaignName!!,
                    sprintSaleViewModel.getStartDate(),
                    sprintSaleViewModel.getEndDate(),
                    sprintSaleViewModel.sprintSaleType!!
            )

            Observable.timer(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (adapter.list.size == 0
                                || adapter.getItemAt(0) != null
                                && adapter.getItemAt(0) !is SprintSaleAnnouncementViewModel) {
                            adapter.addIncomingMessage(sprintSaleAnnouncementViewModel)
                            adapter.notifyItemInserted(0)
                            listener.vibratePhone()
                        }
                    }
        }
    }


    private fun isValidSprintSale(sprintSaleViewModel: SprintSaleViewModel?): Boolean {
        return (sprintSaleViewModel != null
                && sprintSaleViewModel.getStartDate() != 0L
                && sprintSaleViewModel.getEndDate() != 0L
                && sprintSaleViewModel.getEndDate() > getCurrentTime())
    }

    override fun onInteractiveButtonClicked(anchorView: LottieAnimationView) {
        analytics.eventClickInteractionButton(viewModel?.channelId)
        interactionAnimationHelper.shootInteractionButton(anchorView, 1)
    }


    private fun getCurrentTime(): Long {
        return Date().time / 1000L
    }
}
