package com.tokopedia.tokochat.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.gojek.courier.lifecycle.LifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.util.TokoChatCourierConnectionLifecycle
import com.tokopedia.tokochat.databinding.FragmentTokoChatBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.mapper.TokoChatConversationUiMapper
import com.tokopedia.tokochat.view.uimodel.TokoChatHeaderUiModel
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.DRIVER
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat_common.view.customview.TokoChatReplyMessageView
import com.tokopedia.tokochat_common.view.fragment.TokoChatBaseFragment
import com.tokopedia.tokochat_common.view.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat_common.view.listener.TokoChatTypingListener
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokochatReminderTickerUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoChatFragment : TokoChatBaseFragment<FragmentTokoChatBinding>(),
    TokoChatTypingListener,
    TokoChatReplyTextListener,
    TokochatReminderTickerListener {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    @Inject
    lateinit var mapper: TokoChatConversationUiMapper

    private var headerUiModel: TokoChatHeaderUiModel? = null
    private var channelId = ""
    private var firstTimeOpen = true

    override var adapter: TokoChatBaseAdapter = TokoChatBaseAdapter(this)

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        getComponent(TokoChatComponent::class.java).inject(this)
    }

    override fun additionalSetup() {
        AndroidThreeTen.init(context?.applicationContext)
    }

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        setupBackground()
        setupReplySection(
            true,
            getString(com.tokopedia.tokochat_common.R.string.tokochat_message_closed_chat)
        )
        initializeChatProfile()
        initGroupBooking(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        TokoChatCourierConnectionLifecycle.publishSubject.onNext(LifecycleEvent.Started)
        if (channelId.isNotEmpty()) {
            viewModel.registerActiveChannel(channelId)
        }
    }

    override fun onStop() {
        super.onStop()
        TokoChatCourierConnectionLifecycle.publishSubject.onNext(LifecycleEvent.Stopped)
        if (channelId.isNotEmpty()) {
            viewModel.deRegisterActiveChannel(channelId)
        }
    }

    private fun renderBackground(url: String) {
        baseBinding?.tokochatIvBgChat?.let {
            Glide.with(it.context)
                .load(url)
                .centerInside()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(it)
        }
    }

    override fun initObservers() {
        observeTokoChatBackground()
        observeChatRoomTicker()
        observeChannelDetails()
        observerTyping()
    }

    override fun disableSendButton(isExceedLimit: Boolean) {
        val chatSendButton = getChatSendButton()
        chatSendButton?.background =
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    com.tokopedia.tokochat_common.R.drawable.bg_tokochat_send_btn_disabled
                )
            }
        chatSendButton?.setOnClickListener {
            if (!isExceedLimit) {
                showSnackbarError(getString(com.tokopedia.tokochat_common.R.string.tokochat_desc_empty_text_box))
            }
        }
    }

    override fun enableSendButton() {
        val chatSendButton = getChatSendButton()
        chatSendButton?.background = context?.let {
            ContextCompat.getDrawable(
                it,
                com.tokopedia.tokochat_common.R.drawable.bg_tokochat_send_btn
            )
        }
        chatSendButton?.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private fun getChatSendButton(): IconUnify? {
        return baseBinding?.tokochatReplyBox?.findViewById(com.tokopedia.tokochat_common.R.id.tokochat_ic_send_btn)
    }

    private fun onSendButtonClicked() {
        if (isValidComposedMessage() && channelId.isNotEmpty()) {
            viewModel.sendMessage(channelId, getComposedMessage())
            getComposeMessageArea()?.onSendMessage()
            clearReplyBoxMessage()
        }
    }

    private fun clearReplyBoxMessage() {
        getComposeMessageArea()?.composeArea?.setText("")
    }

    private fun showSnackbarError(message: String) {
        view?.let {
            Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun isValidComposedMessage(): Boolean {
        val message = getComposedMessage()
        return message.isNotBlank()
    }

    private fun getComposedMessage(message: String? = null): String {
        return if (message != null && message.isNotEmpty()) {
            message
        } else {
            getComposeMessageArea()?.getComposedText() ?: ""
        }
    }

    private fun getComposeMessageArea(): TokoChatReplyMessageView? {
        return baseBinding?.tokochatReplyBox
    }

    override fun getViewBindingInflate(container: ViewGroup?): FragmentTokoChatBinding {
        return FragmentTokoChatBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    private fun setupBackground() {
        viewModel.getTokoChatBackground()
    }

    private fun observeTokoChatBackground() {
        observe(viewModel.chatBackground) {
            when (it) {
                is Success -> renderBackground(it.data)
                is Fail -> {
                    //no op
                }
            }
        }
    }

    private fun observeChatRoomTicker() {
        observe(viewModel.chatRoomTicker) {
            when (it) {
                is Success -> {
                    val ticker = TokochatReminderTickerUiModel(
                        it.data.tokochatRoomTicker.message,
                        it.data.tokochatRoomTicker.tickerType
                    )
                    mapper.setFirstTicker(ticker)

                    // If the ticker is not in list, manually add ticker
                    if (!adapter.getItems().contains(ticker)) {
                        adapter.addItem(adapter.itemCount, ticker)
                        adapter.notifyItemInserted(adapter.itemCount)
                    }
                }
                is Fail -> {
                    //no op
                }
            }
        }
    }

    private fun observeChannelDetails() {
        observe(viewModel.channelDetail) {
            when (it) {
                is Success -> {
                    it.data.members.forEach { member ->
                        if (member.ownerType == DRIVER) {
                            headerUiModel = TokoChatHeaderUiModel(
                                member.id,
                                member.name,
                                member.userMetadata?.licencePlate ?: "",
                                member.profileUrl?: "",
                                member.phone
                            )
                        }
                    }
                    headerUiModel?.let { header ->
                        setupToolbarData(header)
                    }
                }
                is Fail -> {
                    showSnackbarError(it.throwable.message.toString())
                }
            }
        }
    }

    private fun setupToolbarData(headerUiModel: TokoChatHeaderUiModel) {
        getTokoChatHeader()?.run {
            val userTitle = findViewById<Typography>(R.id.tokochat_text_user_title)
            val subTitle = findViewById<Typography>(R.id.tokochat_text_user_subtitle)
            val imageUrl = findViewById<ImageUnify>(R.id.tokochat_user_avatar)
            val callMenu = findViewById<IconUnify>(R.id.tokochat_icon_header_menu)

            userTitle.text = headerUiModel.title
            subTitle.text = headerUiModel.subTitle
            imageUrl.setImageUrl(headerUiModel.imageUrl)
            imageUrl.show()

            callMenu.run {
                setImage(IconUnify.CALL)
                setOnClickListener { }
            }
        }
    }

    private fun setupReplySection(isShowReplySection: Boolean, expiredMessage: String) {
        baseBinding?.tokochatReplyBox?.run {
            shouldShowWithAction(isShowReplySection) {
                this.initLayout(this@TokoChatFragment, this@TokoChatFragment)
            }
        }
        baseBinding?.tokochatExpiredInfo?.shouldShowWithAction(!isShowReplySection) {
            baseBinding?.tokochatExpiredInfo?.setExpiredInfoDesc(expiredMessage)
        }
    }

    private fun initializeChatProfile() {
        val userId = viewModel.getUserId()
        if (userId.isEmpty()) {
            viewModel.initializeProfile()
        }
    }

    private fun initGroupBooking(savedInstanceState: Bundle?) {
        val gojekOrderId = getParamString(
            ApplinkConst.TokoChat.ORDER_ID_GOJEK,
            arguments,
            savedInstanceState
        )
        viewModel.resetTypingStatus()
        viewModel.initGroupBooking(
            orderId = gojekOrderId,
            groupBookingListener = getGroupBookingListener()
        )
    }

    private fun getGroupBookingListener(): ConversationsGroupBookingListener {
        return object : ConversationsGroupBookingListener {

            override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
                removeShimmering()
                var errorMessage = error.getErrorMessage()
                if (errorMessage.isEmpty()) {
                    errorMessage = error.toString()
                    showSnackbarError(errorMessage)
                }
            }

            override fun onGroupBookingChannelCreationStarted() {}

            override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
                this@TokoChatFragment.channelId = channelUrl
                viewModel.registerActiveChannel(channelUrl)
                viewModel.getGroupBookingChannel(channelId)
                removeShimmering()
                observeChatHistory()
            }

        }
    }

    private fun observeChatHistory() {
        observe(viewModel.getChatHistory(channelId)) {
            // First time get Chat History
            if (firstTimeOpen) {
                firstTimeOpen = false
                viewModel.loadChatRoomTicker(channelId)
            }

            // Map conversation message into ui model
            val result = mapper.mapToChatUiModel(it, viewModel.getUserId())
            adapter.setItemsAndAnimateChanges(result)
            scrollToBottom()

            // Mark the chat as read
            viewModel.markChatAsRead(channelId)
        }
    }

    /**
     * Tokochat Config Common
     */

    /**
     * System : Chat Ticker
     */

    override fun onStartTyping() {
        viewModel.setTypingStatus(true)
    }

    override fun onStopTyping() {
        viewModel.setTypingStatus(false)
    }

    private fun observerTyping() {
        viewModel.getTypingStatus().observe(viewLifecycleOwner) {
            if (headerUiModel != null && it.contains(headerUiModel?.id)) {
                showInterlocutorTypingStatus()
            } else {
                hideInterlocutorTypingStatus()
            }
        }
    }

    /**
     * Listeners
     */

    override fun trackSeenTicker(element: TokochatReminderTickerUiModel) {
        // TODO: Tracker
    }

    override fun onClickLinkReminderTicker(
        element: TokochatReminderTickerUiModel,
        linkUrl: String
    ) {
        if (linkUrl.isNotEmpty()) {
            context?.let {
                RouteManager.route(it, linkUrl)
            }
        }
    }

    override fun onCloseReminderTicker(element: TokochatReminderTickerUiModel, position: Int) {
        adapter.removeItem(element)
        mapper.setFirstTicker(null)
    }

    companion object {
        private const val TAG = "TokoChatFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): TokoChatFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatFragment::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatFragment
        }
    }
}
