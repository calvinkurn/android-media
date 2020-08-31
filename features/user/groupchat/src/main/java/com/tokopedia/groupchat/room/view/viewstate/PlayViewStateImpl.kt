package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.VISIBLE
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
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
import com.tokopedia.globalerror.GlobalError
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
import com.tokopedia.groupchat.common.data.GroupChatUrl
import com.tokopedia.groupchat.common.design.QuickReplyItemDecoration
import com.tokopedia.groupchat.common.design.SpaceItemDecoration
import com.tokopedia.groupchat.common.util.TextFormatter
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.groupchat.room.view.fragment.PlayWebviewDialogFragment
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.viewmodel.ChatPermitViewModel
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentsViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Toaster
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
        private val sendMessage: (viewModel: PendingChatViewModel) -> Unit,
        dynamicButtonClickListener: ChatroomContract.DynamicButtonItem.DynamicButtonListener,
        interactiveButtonClickListener: ChatroomContract.DynamicButtonItem.InteractiveButtonListener

) : PlayViewState {

    private val currentOrientation: Int
        get() = activity.resources.configuration.orientation

    private val isLandscape: Boolean
        get() = currentOrientation == Configuration.ORIENTATION_LANDSCAPE

    private val isPortrait: Boolean
        get() = currentOrientation == Configuration.ORIENTATION_PORTRAIT

    private var viewModel: ChannelInfoViewModel? = null
    private var chatPermitViewModel: ChatPermitViewModel? = null
    private var dynamicButtonsViewModel: DynamicButtonsViewModel? = null
    private var listMessage: ArrayList<Visitable<*>> = arrayListOf()

    private var quickReplyAdapter: QuickReplyAdapter
    private var dynamicButtonAdapter: DynamicButtonsAdapter
    private var adapter: GroupChatAdapter

    private var clLayout: ConstraintLayout = view.findViewById(R.id.cl_layout)
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
    private var loginChatButton: View = view.findViewById(R.id.login)
    private var inputTextWidget: View = view.findViewById(R.id.bottom)
    private var sendButton: View = view.findViewById(R.id.button_send)
    private var dynamicButtonRecyclerView: RecyclerView = view.findViewById(R.id.buttons)
    private var liveIndicator: View = toolbar.findViewById(R.id.toolbar_live)
    private var stickyComponent: RecyclerView = view.findViewById(R.id.sticky_component)
    private val webviewIcon = view.findViewById<ImageView>(R.id.webview_icon)
    private var errorView: GlobalError = view.findViewById(R.id.card_retry)
    private var loadingView: View = view.findViewById(R.id.loading_view)
    private var hideVideoToggle: View = view.findViewById(R.id.hide_video_toggle)
    private var showVideoToggle: View = view.findViewById(R.id.show_video_toggle)
    private var spaceChatVideo: View = view.findViewById(R.id.top_space_guideline)
    private var interactionGuideline = view.findViewById<FrameLayout>(R.id.interaction_button_guideline)
    private var bufferContainer = view.findViewById<View>(R.id.video_buffer_container)
    private var gradientBackground = view.findViewById<View>(R.id.top_guideline)

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

    private var interactionAnimationHelper: InteractionAnimationHelper
    private var overflowMenuHelper: OverflowMenuHelper
    private var videoVerticalHelper: VideoVerticalHelper
    private var videoHorizontalHelper: VideoHorizontalHelper
    private var sponsorHelper: SponsorHelper
    private var welcomeHelper: PlayWelcomeHelper
    private var backgroundHelper: PlayBackgroundHelper
    private var stickyComponentHelper: StickyComponentHelper

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
                .resources.getDimension(com.tokopedia.groupchat.R.dimen.space_play_chat).toInt())
        chatRecyclerView.addItemDecoration(itemDecoration)

        chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    attemptResetNewMessageCounter()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
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
                .resources.getDimension(com.tokopedia.design.R.dimen.dp_16).toInt())
        quickReplyRecyclerView.addItemDecoration(quickReplyItemDecoration)

        val dynamicButtonTypeFactory = DynamicButtonTypeFactoryImpl(
                dynamicButtonClickListener, interactiveButtonClickListener, interactionGuideline)

        dynamicButtonRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val buttonSpace = SpaceItemDecoration(activity.getResources()
                .getDimension(com.tokopedia.design.R.dimen.dp_8).toInt(), 2)
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

        loginChatButton.setOnClickListener {
            listener.onLoginClicked(viewModel?.channelId)
        }

        chatNotificationView.setOnClickListener {
            attemptResetNewMessageCounter()
            scrollToBottom()
        }

        sendButton.setOnClickListener {
            shouldSendMessage(replyEditText.text.toString(), isQuickReply = false)
        }
        errorView.setOnClickListener {  }

        backgroundHelper = PlayBackgroundHelper(viewModel, activity)
        interactionAnimationHelper = InteractionAnimationHelper(interactionGuideline)
        val videoVerticalContainer = (activity as PlayActivity).findViewById<FrameLayout>(R.id.playerView)
        val rootView = (activity as PlayActivity).findViewById<View>(R.id.root_view)
        overflowMenuHelper = OverflowMenuHelper(
                viewModel,
                activity,
                onInfoMenuClicked(),
                toggleHorizontalVideo(),
                videoHorizontalContainer,
                changeQualityVideoVertical(),
                videoVerticalContainer,
                toggleVerticalVideo(),
                analytics
        )
        videoVerticalHelper = VideoVerticalHelper(
                viewModel,
                bufferContainer,
                activity.supportFragmentManager,
                videoVerticalContainer,
                rootView,
                setChatListHasSpaceOnTop(),
                backgroundHelper,
                analytics,
                gradientBackground
        )
        videoHorizontalHelper = VideoHorizontalHelper(
                viewModel,
                clLayout,
                hideVideoToggle,
                showVideoToggle,
                videoHorizontalContainer,
                youTubePlayer,
                setChatListHasSpaceOnTop(),
                analytics,
                activity,
                toolbar
        )
        sponsorHelper = SponsorHelper(viewModel, sponsorLayout, sponsorImage, analytics, listener)
        welcomeHelper = PlayWelcomeHelper(viewModel, analytics, activity, view)
        stickyComponentHelper = StickyComponentHelper(stickyComponent, userSession, analytics, viewModel, openLink(), activity)
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

    override fun onStickyComponentUpdated(stickyComponentViewModel: StickyComponentsViewModel) {
        stickyComponentHelper.assignModel(stickyComponentViewModel)
    }

    override fun onErrorGetStickyComponent() {
        stickyComponentHelper.hide()
    }

    override fun onKeyboardHidden() {
        showWidgetAboveInput(true)
        inputTextWidget.setBackgroundColor(MethodChecker.getColor(view.context, com.tokopedia.design.R.color.transparent))
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
        inputTextWidget.setBackgroundColor(MethodChecker.getColor(view.context, com.tokopedia.groupchat.R.color.play_transparent))
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
                .subscribe (
                        {chatRecyclerView.scrollToPosition(0)},
                        { it.printStackTrace() }
                )
    }

    private fun attemptResetNewMessageCounter() {
        newMessageCounter = 0
        chatNotificationView.visibility = View.GONE
    }

    override fun onSuccessGetInfoFirstTime(it: ChannelInfoViewModel, childFragmentManager: FragmentManager) {
        showBottomSheetFirstTime(it)
        backgroundHelper.setDefaultBackground()
        onSuccessGetInfo(it)
    }

    override fun onSuccessGetInfo(it: ChannelInfoViewModel) {
        loadingView.hide()

        val needCueVideo = viewModel?.videoId != it.videoId
        viewModel = it
        viewModel?.infoUrl = it.infoUrl

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
        videoVerticalHelper.assignViewModel(it)
        videoHorizontalHelper.assignViewModel(it)
        sponsorHelper.assignViewModel(it)
        sponsorHelper.setSponsor()
        overflowMenuHelper.assignViewModel(it)
        stickyComponentHelper.assignViewModel(it)

        chatPermitViewModel = it.chatPermitViewModel
    }

    override fun onBackgroundUpdated(it: BackgroundViewModel) {
        backgroundHelper.setBackground(it)
    }

    /**
     * show overlay behind channel info
     */
    private fun showBottomSheetFirstTime(it: ChannelInfoViewModel) {
        welcomeHelper.showInfoBottomSheet(it, userSession.isLoggedIn) {
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
                stickyComponentHelper.showIfNotEmpty()
            }
        } else {
            stickyComponentHelper.hide()
            hidePinnedMessage()
            setQuickReply(null)
        }
    }

    private fun hidePinnedMessage() {
        pinnedMessageContainer.visibility = View.GONE
    }

    private fun showLoginButton(show: Boolean) {
        if (show && isPortrait && !errorView.isVisible) {
            loginChatButton.visibility = View.VISIBLE
            inputTextWidget.visibility = View.GONE
        } else {
            loginChatButton.visibility = View.GONE
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
        viewModel?.isFreeze = true
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
            if (isPortrait) pinnedMessageContainer.visibility = View.VISIBLE

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
            if (isPortrait) quickReplyRecyclerView.visibility = View.VISIBLE
            quickReplyAdapter.setList(quickRepliesViewModel)
        }
    }

    override fun onQuickReplyClicked(message: String?) {
        shouldSendMessage(
                MethodChecker.fromHtml(message),
                isQuickReply = true
        )

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

                val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

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
                ImageHandler.loadImage2(overlayView.findViewById(R.id.ivImage) as ImageView, interruptViewModel.imageUrl, com.tokopedia.abstraction.R.drawable.loading_page)
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

    private fun closePinnedMessageDialog() {
        if (::pinnedMessageDialog.isInitialized && pinnedMessageDialog.isShowing) pinnedMessageDialog.dismiss()
    }

    private fun closeWebViewDialog() {
        if (::webviewDialog.isInitialized) webviewDialog.dismiss()
    }

    override fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?) {

        toolbar.setBackgroundResource(com.tokopedia.design.R.color.transparent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }

        toolbar.findViewById<TextView>(R.id.toolbar_title).text = title
        setToolbarParticipantCount(view.context, TextFormatter.format(totalView))

        toolbar.findViewById<TextView>(R.id.toolbar_title).setTextColor(MethodChecker.getColor(activity, com.tokopedia.design.R.color.white))
        toolbar.findViewById<TextView>(R.id.toolbar_subtitle).setTextColor(MethodChecker.getColor(activity, com.tokopedia.design.R.color.white))

        when {
            title != null -> setVisibilityHeader(View.VISIBLE)
            else -> setVisibilityHeader(View.GONE)
        }
    }

    private fun setToolbarWhite() {
        sponsorHelper.hideSponsor()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
            toolbar.setBackgroundResource(com.tokopedia.design.R.color.white)
        } else {
            toolbar.setBackgroundResource(com.tokopedia.design.R.drawable.bg_white_toolbar_drop_shadow)
        }
        var title = toolbar.findViewById<TextView>(R.id.toolbar_title)
        var subtitle = toolbar.findViewById<TextView>(R.id.toolbar_subtitle)
        subtitle.hide()
        toolbar.findViewById<TextView>(R.id.toolbar_live).hide()
        title.text = getStringResource(R.string.play_title)
        subtitle.text = ""
        title.setTextColor(MethodChecker.getColor(title.context, com.tokopedia.design.R.color.black_70))
        subtitle.setTextColor(MethodChecker.getColor(title.context, com.tokopedia.design.R.color.black_70))
    }

    override fun errorViewShown(): Boolean {
        return errorView.isShown
    }

    override fun verticalVideoShown(): Boolean {
        return videoVerticalHelper.isVideoShown()
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
        videoVerticalHelper.setData(it)
        setLiveLabel(it.isLive)
        if(it.isActive && it.androidStreamSD.isNotBlank()) {
            videoVerticalHelper.playVideo(VideoVerticalHelper.VIDEO_480)
            overflowMenuHelper.setQualityVideo(VideoVerticalHelper.VIDEO_480)
            videoHorizontalHelper.hideVideoAndToggle()
            sponsorHelper.assignVideoVertical(true)
            setChatListHasSpaceOnTop().invoke(VideoVerticalHelper.VERTICAL_WITH_VIDEO)
            videoHorizontalHelper.clearDataVideoHorizontal()
            youTubePlayer?.release()
            youTubePlayer = null
        } else {
            videoVerticalHelper.stopVideo()
            setChatListHasSpaceOnTop().invoke(VideoVerticalHelper.VERTICAL_WITHOUT_VIDEO)
            sponsorHelper.assignVideoVertical(false)
            overflowMenuHelper.setQualityVideo(0)
        }
    }

    override fun autoPlayVideo() {
        try {
            youtubeRunnable?.postDelayed({ playVideo() }, PlayFragment.YOUTUBE_DELAY.toLong())
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playVideo() {
        try {
            youTubePlayer?.play()
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
            videoVerticalHelper.setData(VideoStreamViewModel())
        }
    }

    private fun initVideoFragment(videoId: String, isVideoLive: Boolean) {
        setLiveLabel(isVideoLive)
        videoHorizontalHelper.hideVideoAndToggle()
        if(!videoId.isNullOrBlank()){
            videoVerticalHelper.releasePlayer()
            val videoFragment = fragmentManager.findFragmentById(R.id.video_container) as GroupChatVideoFragment
            videoFragment.run {
                videoHorizontalHelper.showVideoOnly()
                sponsorHelper.hideSponsor()
                youTubePlayer?.let {
                    it.cueVideo(videoId)
                    autoPlayVideo()
                }
                videoFragment.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, getVideoInitializer(videoId))
            }
            setChatListHasSpaceOnTop().invoke(VideoHorizontalHelper.HORIZONTAL_WITH_VIDEO)
        } else {
            setChatListHasSpaceOnTop().invoke(VideoHorizontalHelper.HORIZONTAL_WITHOUT_VIDEO)
            videoHorizontalHelper.hideVideoAndToggle()
            youTubePlayer?.release()
            youTubePlayer = null
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
                            it.fullscreenControlFlags = videoHorizontalHelper.getYoutubeFullScreenControlFlags(activity.resources.configuration.orientation)
                            it.setOnFullscreenListener { isFullScreen ->
                                doOnFullScreenChanged(isFullScreen)
                            }
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
        if (loginChatButton.visibility != VISIBLE && isPortrait) {
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
            ImageHandler.loadGifFromUrl(webviewIcon, floatingButton.imageUrl, com.tokopedia.abstraction.R.drawable.ic_loading_toped)
        } else {
            ImageHandler.LoadImage(webviewIcon, floatingButton.imageUrl)
        }
        if (isPortrait) webviewIcon.show()

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

        if (URLUtil.isNetworkUrl(url)) {
            if (!::webviewDialog.isInitialized) {
                webviewDialog = PlayWebviewDialogFragment.createInstance(url)
            } else {
                webviewDialog.setUrl(url)
            }

            if (!webviewDialog.isAdded)
                webviewDialog.show(activity.supportFragmentManager, "Webview Bottom Sheet")
        } else {
            RouteManager.route(activity, url)
        }
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

    override fun onErrorGetInfo(globalError: Int) {
        showLoginButton(false)
        setEmptyStateByType(globalError, action = listener::onRetryGetInfo)
        loadingView.hide()
        errorView.show()
        setToolbarWhite()
    }

    override fun onNoInternetConnection() {
        showLoginButton(false)
        setEmptyStateByType(GlobalError.NO_CONNECTION, action = listener::onRetryGetInfo)
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
        replyEditText.text?.clear()
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

    override fun dismissAllBottomSheet() {
        overflowMenuHelper.hideBottomSheet()
        welcomeHelper.hideBottomSheet()
        closeOverlayDialog()
        closePinnedMessageDialog()
        closeWebViewDialog()
    }

    override fun onOrientationChanged(orientation: Int) {
        videoHorizontalHelper.onOrientationChanged(orientation)
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            inputTextWidget.gone()
            quickReplyRecyclerView.gone()
            pinnedMessageContainer.gone()
            chatNotificationView.gone()
            stickyComponentHelper.hide()
            webviewIcon.gone()
            loginChatButton.gone()

            backgroundHelper.setEmptyBackground()
        }
        else {
            if (errorView.visibility != View.VISIBLE) {
                showLoginButton(!userSession.isLoggedIn)
                quickReplyRecyclerView.visibility = if (quickReplyAdapter.itemCount > 0) View.VISIBLE else View.GONE
                pinnedMessageContainer.visibility = if (viewModel?.pinnedMessageViewModel != null &&
                        viewModel?.pinnedMessageViewModel?.title?.isNotEmpty() == true &&
                        viewModel?.pinnedMessageViewModel?.title?.isNotBlank() == true) View.VISIBLE else View.GONE
                stickyComponentHelper.showIfNotEmpty()
                webviewIcon.visibility = if (webviewIcon.drawable == null) View.GONE else View.VISIBLE

                val backgroundModel = backgroundHelper.backgroundViewModel
                if (backgroundModel == null) backgroundHelper.setDefaultBackground()
                else backgroundHelper.setBackground(backgroundModel)
            }
        }
    }

    override fun exitFullScreen() {
        videoHorizontalHelper?.exitFullScreen()
    }

    private fun showPinnedMessage(viewModel: ChannelInfoViewModel) {
        if (!::pinnedMessageDialog.isInitialized) {
            pinnedMessageDialog = CloseableBottomSheetDialog.createInstanceRounded(view.context)
        }

        val pinnedMessageView = createPinnedMessageView(viewModel)
        pinnedMessageDialog.setOnShowListener() { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
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
            ImageHandler.loadImage(activity, view.findViewById(R.id.thumbnail), it.thumbnail, com.tokopedia.abstraction.R
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

    private fun setEmptyStateByType(globalErrorType: Int, action:() -> Unit) {
        if (globalErrorType in 0..4)
            errorView.setType(globalErrorType)
        else
            errorView.setType(GlobalError.SERVER_ERROR)
        if (globalErrorType == GlobalError.SERVER_ERROR
                || globalErrorType == GlobalError.PAGE_FULL
                || globalErrorType == GlobalError.NO_CONNECTION) {
            errorView.errorAction.setOnClickListener {
                loadingView.show()
                action()
            }
        } else {
            errorView.errorAction.setOnClickListener {
                listener.backToChannelList()
            }
        }
    }

    private fun setEmptyState(imageResId: Int, titleText: String, bodyText: String, buttonText: String, action: () -> Unit) {
        val imageView = errorView.errorIllustration
        val title = errorView.errorTitle
        val body = errorView.errorDescription
        val button = errorView.errorAction

        ImageHandler.loadImageWithId(imageView, imageResId)
        title.text = titleText
        body.text = bodyText
        button.text = buttonText
        button.setOnClickListener {
            action()
        }
    }

    private fun openLink(): (String) -> Unit {
        return {
            listener.openRedirectUrl(it)
        }
    }

    private fun setChatListHasSpaceOnTop(): (Int) -> Unit {
        return {
            val layoutParams = chatRecyclerView.layoutParams as ConstraintLayout.LayoutParams

            val fadingEdgeLength = when (it){
                VideoVerticalHelper.VERTICAL_WITH_VIDEO -> view.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_0)
                VideoHorizontalHelper.HORIZONTAL_WITH_VIDEO -> view.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8)
                else -> {
                    view.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_24)
                }
            }
            chatRecyclerView.setFadingEdgeLength(fadingEdgeLength)

            val displayMetrics = view.context.resources.displayMetrics
            val height: Int
            when (it){
                VideoVerticalHelper.VERTICAL_WITH_VIDEO -> {
                    height = 120.dpToPx(displayMetrics)
                    spaceChatVideo.hide()
                }
                VideoHorizontalHelper.HORIZONTAL_WITH_VIDEO -> {
                    height = 0
                    spaceChatVideo.hide()
                }
                VideoVerticalHelper.VERTICAL_WITHOUT_VIDEO -> {
                    height = 0
                    if(!videoVerticalHelper.videoStreamViewModel.androidStreamSD.isNullOrBlank()) {
                        spaceChatVideo.show()
                    } else {
                        spaceChatVideo.hide()
                    }
                }
                else -> {
                    height = 0
                    spaceChatVideo.show()
                }
            }

            layoutParams.height = height
            chatRecyclerView.layoutParams = layoutParams
        }
    }

    override fun isChannelActive(): Boolean {
        viewModel?.isFreeze?.let {
            return !it
        }
        return false
    }

    override fun setChatPermitDisabled(chatPermitViewModel: ChatPermitViewModel) {
        this.chatPermitViewModel = chatPermitViewModel
    }

    override fun onChatDisabledError(message: String, action: String) {
        Toaster.make(
                view,
                chatPermitViewModel?.errorMessage?:message,
                type = Toaster.TYPE_ERROR,
                duration = Snackbar.LENGTH_LONG,
                actionText = action,
                clickListener = View.OnClickListener {
                    RouteManager.route(view.context, GroupChatUrl.FAQ_URL)
                }
        )
    }

    private fun toggleHorizontalVideo(): (Boolean) -> Unit {
        return {
            if(it) {
                videoHorizontalHelper.showVideo()
                videoVerticalHelper.stopVideo()
                setChatListHasSpaceOnTop().invoke(VideoHorizontalHelper.HORIZONTAL_WITH_VIDEO)
            } else {
                videoHorizontalHelper.hideVideo()
                setChatListHasSpaceOnTop().invoke(VideoHorizontalHelper.HORIZONTAL_WITHOUT_VIDEO)
            }
        }
    }

    private fun toggleVerticalVideo(): (Boolean) -> Unit {
        return {
            if(it) {
                videoHorizontalHelper.hideVideoAndToggle()
                sponsorHelper.hideSponsor()
                setChatListHasSpaceOnTop().invoke(VideoVerticalHelper.VERTICAL_WITH_VIDEO)
            } else {
                sponsorHelper.setSponsor()
                videoVerticalHelper.stopVideo()
                setChatListHasSpaceOnTop().invoke(VideoVerticalHelper.VERTICAL_WITHOUT_VIDEO)
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
                    .subscribe( {
                        if (adapter.list.size == 0
                                || adapter.getItemAt(0) != null
                                && adapter.getItemAt(0) !is SprintSaleAnnouncementViewModel) {
                            adapter.addIncomingMessage(sprintSaleAnnouncementViewModel)
                            adapter.notifyItemInserted(0)
                            listener.vibratePhone()
                        }
                    }, {it.printStackTrace()})
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

    private fun shouldSendMessage(rawMessage: CharSequence, isQuickReply: Boolean) {
        val emp = !TextUtils.isEmpty(rawMessage.trim { it <= ' ' })
        if (emp) {
            val pendingChatViewModel = PendingChatViewModel(checkText(rawMessage.toString()),
                    userSession.userId,
                    userSession.name,
                    userSession.profilePicture,
                    isInfluencer = false,
                    isChatDisabled = !(chatPermitViewModel?.isChatDisabled?:false),
                    isQuickReply = isQuickReply)
            sendMessage(pendingChatViewModel)
        }
    }

    private fun setLiveLabel(isLive: Boolean) = liveIndicator.showWithCondition(isLive)

    private fun doOnFullScreenChanged(isFullScreen: Boolean) {
        activity.requestedOrientation = if (isFullScreen) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
