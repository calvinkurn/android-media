package com.tokopedia.groupchat.room.view.viewstate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.text.BackEditText
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
import com.tokopedia.groupchat.common.design.QuickReplyItemDecoration
import com.tokopedia.groupchat.common.design.SpaceItemDecoration
import com.tokopedia.groupchat.common.util.TextFormatter
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.groupchat.room.view.listener.PlayContract
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
        var fragmentManager: FragmentManager,
        var userSession: UserSessionInterface,
        var view: View,
        var listener: PlayContract.View,
        quickReplyListener: ChatroomContract.QuickReply,
        imageListener: ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener,
        voteAnnouncementListener: ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener,
        sprintSaleViewHolderListener: ChatroomContract.ChatItem.SprintSaleViewHolderListener,
        groupChatPointsViewHolderListener: ChatroomContract.ChatItem.GroupChatPointsViewHolderListener

) : PlayViewState {

    private var toolbar: Toolbar = view.findViewById(R.id.toolbar)
    private lateinit var viewModel: ChannelInfoViewModel
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
                .getResources().getDimension(R.dimen.space_chat).toInt())
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
            showWidgetAboveInput(false)
//            setSprintSaleIcon(null)
        }

        replyEditText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            showWidgetAboveInput(false)
//            setSprintSaleIcon(null)
        }

        replyEditText.setKeyImeChangeListener { keyCode, event ->
            if (KeyEvent.KEYCODE_BACK == event.keyCode) {
                showWidgetAboveInput(true)
            }
        }

        showLoginButton(!userSession.isLoggedIn)

        login.setOnClickListener {
            listener.onLoginClicked(viewModel.channelId)
        }

        chatNotificationView.setOnClickListener {
            attemptResetNewMessageCounter()
            scrollToBottom()
        }
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

    private fun showWidgetAboveInput(show: Boolean) {
        if (show) {
            setPinnedMessage(viewModel.pinnedMessageViewModel)
            setQuickReply(viewModel.quickRepliesViewModel)
        } else {
            setPinnedMessage(null)
            setQuickReply(null)
        }
    }

    override fun onSuccessGetInfo(it: ChannelInfoViewModel) {
        viewModel = it
        showLoginButton(!userSession.isLoggedIn)
        showWidgetAboveInput(userSession.isLoggedIn)
        initVideoFragment()
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
        viewModel.adsImageUrl = it.adsUrl
        viewModel.adsId = it.adsId
        viewModel.adsLink = it.adsLink
        setSponsorData(viewModel.adsId, viewModel.adsImageUrl, viewModel.adsName)
    }

    override fun onVideoUpdated(it: VideoViewModel) {
        viewModel.videoId = it.videoId
        initVideoFragment()
    }

    override fun onChannelFrozen(channelId: String) {
        if (channelId == viewModel.channelId) {
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

                viewModel.bannedMessage?.let {
                    builder.setMessage(it)
                }

                builder.setPositiveButton(R.string.title_ok) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val intent = Intent()
//                    if (viewModel != null) {
//                        intent.putExtra(TOTAL_VIEW, viewModel.totalView)
//                        intent.putExtra(EXTRA_POSITION, viewModel.getChannelPosition())
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
        viewModel.pinnedMessageViewModel = it
        setPinnedMessage(it)
    }

    override fun onSuccessLogin() {
        showLoginButton(userSession.isLoggedIn)
    }

    private fun setPinnedMessage(pinnedMessageViewModel: PinnedMessageViewModel?) {
        if (pinnedMessageViewModel != null) {
            pinnedMessageContainer.visibility = View.VISIBLE
            (pinnedMessageContainer.findViewById(R.id.message) as TextView).text = pinnedMessageViewModel.title
            pinnedMessageContainer.setOnClickListener { view ->
                //                if (channelInfoViewModel != null) {
                //                    analytics.eventClickAdminPinnedMessage(
                //                            String.format("%s - %s", channelInfoViewModel.getChannelId(), pinnedMessage.title))
                //                }
                //
//                    showPinnedMessageBottomSheet(pinnedMessage)
            }
        } else {
            pinnedMessageContainer.visibility = View.GONE
        }
    }

    private fun setQuickReply(quickRepliesViewModel: List<GroupChatQuickReplyItemViewModel>?) {
        quickReplyRecyclerView.visibility = View.GONE
        quickRepliesViewModel?.let {
            if (it.isEmpty()) return
            quickReplyRecyclerView.visibility = View.VISIBLE
            quickReplyAdapter.setList(quickRepliesViewModel)
//            userSession.let {
//                if(it.isLoggedIn){
//                    quickReplyRecyclerView.visibility = View.VISIBLE
//                    quickReplyAdapter.setList(quickRepliesViewModel)
//                } else{
//                    quickReplyRecyclerView.visibility = View.GONE
//                }
//            }
        }
    }

    override fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?) {

        toolbar.title = title

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
        toolbar.subtitle = textParticipant
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    private fun setVisibilityHeader(visible: Int) {
        toolbar.visibility = visible
        channelBanner.visibility = visible
    }

    override fun setSponsorData(adsId: String?, adsImageUrl: String?, adsName: String?) {
        if (adsId == null || adsImageUrl == null) {
            sponsorLayout.visibility = View.GONE
        } else {
            sponsorLayout.visibility = View.VISIBLE
            ImageHandler.loadImage2(sponsorImage, adsImageUrl, R.drawable.loading_page)
            sponsorImage.setOnClickListener {}
        }

        if (sponsorLayout.visibility == View.VISIBLE) {
            //TO DO analytics event view banner
        }
    }

    fun autoPlayVideo() {
        youtubeRunnable.postDelayed({ youTubePlayer?.play() }, PlayFragment.YOUTUBE_DELAY.toLong())
    }

    override fun initVideoFragment() {
        videoContainer.visibility = View.GONE
        val videoId = viewModel.videoId
        videoId?.let {
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
                                            it.cueVideo(viewModel.videoId)
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
        if (channelId == viewModel.channelId) {
            setToolbarParticipantCount(view.context, totalView)
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
        if(login.visibility != VISIBLE){
            chatNotificationView.visibility = VISIBLE
        }
    }

}
