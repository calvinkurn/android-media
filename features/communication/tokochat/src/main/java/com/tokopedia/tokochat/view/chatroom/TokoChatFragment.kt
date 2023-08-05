package com.tokopedia.tokochat.view.chatroom

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepreview.imagesecure.ImageSecurePreviewActivity
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.analytics.TokoChatAnalytics
import com.tokopedia.tokochat.analytics.TokoChatAnalyticsConstants
import com.tokopedia.tokochat.common.util.OrderStatusType
import com.tokopedia.tokochat.common.util.TokoChatNetworkUtil
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.IC_TOKOFOOD_SOURCE
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.CUSTOMER
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.DEFAULT_CENSOR_PERCENTAGE
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.DRIVER
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.IS_FROM_BUBBLE_KEY
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.TOKOFOOD
import com.tokopedia.tokochat.common.util.TokoChatViewUtil
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.EIGHT_DP
import com.tokopedia.tokochat.common.view.chatroom.TokoChatBaseFragment
import com.tokopedia.tokochat.common.view.chatroom.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat.common.view.chatroom.customview.TokoChatReplyMessageView
import com.tokopedia.tokochat.common.view.chatroom.customview.TokoChatTransactionOrderWidget
import com.tokopedia.tokochat.common.view.chatroom.customview.attachment.TokoChatMenuLayout
import com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.MaskingPhoneNumberBottomSheet
import com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.TokoChatConsentBottomSheet
import com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.TokoChatGuideChatBottomSheet
import com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.TokoChatLongTextBottomSheet
import com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.bubbleawareness.TokoChatBubblesAwarenessBottomSheet
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatAttachmentMenuListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatMessageBubbleListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatMessageCensorListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatReplyAreaListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatTypingListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatHeaderUiModel
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatMessageBubbleUiModel
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatOrderProgressUiModel
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatReminderTickerUiModel
import com.tokopedia.tokochat.config.util.TokoChatErrorLogger
import com.tokopedia.tokochat.databinding.TokochatChatroomFragmentBinding
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.util.TokoChatMediaCleanupStorageWorker
import com.tokopedia.tokochat.util.TokoChatValueUtil
import com.tokopedia.tokochat.util.TokoChatValueUtil.BUBBLES_NOTIF
import com.tokopedia.tokochat.util.TokoChatValueUtil.CHAT_CLOSED_CODE
import com.tokopedia.tokochat.util.TokoChatValueUtil.CHAT_DOES_NOT_EXIST
import com.tokopedia.tokochat.util.TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY
import com.tokopedia.tokochat.util.isFromBubble
import com.tokopedia.tokochat.view.chatroom.bottomsheet.TokoChatGeneralUnavailableBottomSheet
import com.tokopedia.tokochat_common.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import java.io.File
import javax.inject.Inject

open class TokoChatFragment @Inject constructor(
    private var viewModel: TokoChatViewModel,
    private var userSession: UserSessionInterface,
    private var mapper: TokoChatConversationUiMapper,
    private var remoteConfig: RemoteConfig,
    private var networkUtil: TokoChatNetworkUtil
) :
    TokoChatBaseFragment<TokochatChatroomFragmentBinding>(),
    ConversationsGroupBookingListener,
    TokoChatTypingListener,
    TokoChatReplyTextListener,
    TokochatReminderTickerListener,
    TokoChatTransactionOrderWidget.Listener,
    TokoChatImageAttachmentListener,
    TokoChatMessageBubbleListener,
    TokoChatMessageCensorListener,
    TokoChatReplyAreaListener,
    TokoChatAttachmentMenuListener,
    MaskingPhoneNumberBottomSheet.AnalyticsListener,
    TokoChatBubblesAwarenessBottomSheet.AnalyticsListener {

    private var tokoChatAnalytics: TokoChatAnalytics? = null

    private var headerUiModel: TokoChatHeaderUiModel? = null
    private var selfUiModel: TokoChatHeaderUiModel? = null
    private var firstTimeOpen = true
    private var readModeStartsAt: Long = 0
    private var expiresAt: Long = 0

    private val unavailableBottomSheet = TokoChatGeneralUnavailableBottomSheet()
    private val consentBottomSheet = TokoChatConsentBottomSheet()

    override var adapter: TokoChatBaseAdapter = TokoChatBaseAdapter(
        reminderTickerListener = this,
        imageAttachmentListener = this,
        bubbleMessageBubbleListener = this,
        messageCensorListener = this
    )

    override fun getScreenName(): String = TAG

    override fun initInjector() {}

    override fun onClickAttachmentButton() {
        baseBinding?.tokochatLayoutMenu?.toggleAttachmentMenu(
            TokoChatMenuLayout.ChatMenuType.ATTACHMENT_MENU,
            baseBinding?.tokochatReplyBox?.composeArea
        )
        baseBinding?.tokochatReplyBox?.composeArea?.clearFocus()
    }

    override fun shouldShowAttachmentButton(): Boolean {
        return remoteConfig.getBoolean(
            TOKOCHAT_ATTACHMENT_MENU,
            false
        )
    }

    override fun onReplyAreaFocusChanged(isFocused: Boolean) {
        val isAttachmentMenuShown = baseBinding?.tokochatLayoutMenu?.attachmentMenu?.isShown ?: false
        // If focused, keyboard is shown
        // If attachment is shown when focused, hide the attachment menu with toggle
        if (isFocused && isAttachmentMenuShown) {
            onClickAttachmentButton()
        }
    }

    override fun trackClickComposeArea() {
        if (viewModel.channelId.isNotBlank()) {
            tokoChatAnalytics?.clickTextField(
                viewModel.tkpdOrderId,
                TokoChatAnalyticsConstants.BUYER,
                viewModel.source
            )
        }
    }

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        setupBackground()
        setupTrackers()
        setupAttachmentMenu()
        setDataFromArguments(savedInstanceState)
        askTokoChatConsent()
        setupLifeCycleObserver()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        updateReplySectionView()
    }

    override fun onFragmentBackPressed(): Boolean {
        val shouldShowBubblesAwarenessBottomSheet =
            TokoChatValueUtil.shouldShowBubblesAwareness() &&
                (activity?.isFromBubble() == false) &&
                viewModel.shouldShowBottomsheetBubblesCache() &&
                !viewModel.isFromBubble

        return if (shouldShowBubblesAwarenessBottomSheet) {
            showBubblesAwarenessBottomSheet()
            tokoChatAnalytics?.viewOnboardingBottomsheet(
                orderId = viewModel.tkpdOrderId,
                source = viewModel.source,
                role = TokoChatAnalyticsConstants.BUYER
            )
            viewModel.setBubblesPref(hasShownTicker = false)
            addBubbleTicker()
            true
        } else {
            super.onFragmentBackPressed()
        }
    }

    private fun updateReplySectionView() {
        if (isChannelExpired()) {
            showUnavailableBottomSheet()
        } else {
            if (isChannelReadOnly()) {
                // Hide reply component
                setupReplySection(
                    false,
                    getString(com.tokopedia.tokochat_common.R.string.tokochat_message_closed_chat)
                )
            } else {
                // Show reply component
                setupReplySection(true)
            }
        }
    }

    private fun askTokoChatConsent() {
        viewModel.getUserConsent()
    }

    private fun setupLifeCycleObserver() {
        this.lifecycle.addObserver(viewModel)
    }

    private fun setupListeners() {
        baseBinding?.tokochatTransactionOrder?.assignListener(this)
    }

    protected open fun initializeChatRoom(savedInstanceState: Bundle?) {
        setDataFromArguments(savedInstanceState)
        loadChatRoomData()
    }

    private fun loadChatRoomData() {
        addInitialShimmering()
        // Do not init when order id empty
        if (viewModel.gojekOrderId.isNotBlank()) {
            initGroupBooking()
        }
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        initializeChatRoom(savedInstanceState)
    }

    private fun observeUserConsent() {
        observe(viewModel.isNeedConsent) {
            when (it) {
                is Success -> {
                    if (it.data) {
                        showConsentBottomSheet()
                    } else {
                        // Show chat room
                        loadChatRoomData()
                    }
                }
                is Fail -> {
                    // Show chat room
                    loadChatRoomData()
                }
            }
        }
    }

    private fun observeTkpdOrderId() {
        observe(viewModel.tkpdOrderIdLiveData) {
            when (it) {
                is Success -> {
                    viewModel.loadOrderCompletedStatus(viewModel.tkpdOrderId, viewModel.source)
                }
                is Fail -> {
                    hideTransactionLocalLoad()
                    setShowTransactionLocalLoad()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.channelId.isNotEmpty() && viewModel.channelId.isNotBlank()) {
            viewModel.registerActiveChannel(viewModel.channelId)
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.channelId.isNotEmpty() && viewModel.channelId.isNotBlank()) {
            viewModel.deRegisterActiveChannel(viewModel.channelId)
        }
    }

    override fun onDestroy() {
        scheduleRemoveTokoChatMedia()
        removeObservers(viewModel.orderTransactionStatus)
        removeObservers(viewModel.chatRoomTicker)
        removeObservers(viewModel.chatBackground)
        removeObservers(viewModel.isChatConnected)
        removeObservers(viewModel.channelDetail)
        removeObservers(viewModel.error)
        removeObservers(viewModel.tkpdOrderIdLiveData)
        viewModel.cancelCheckConnection()
        super.onDestroy()
    }

    private fun scheduleRemoveTokoChatMedia() {
        context?.let {
            TokoChatMediaCleanupStorageWorker.scheduleWorker(it)
        }
    }

    private fun setDataFromArguments(savedInstanceState: Bundle?) {
        if (viewModel.gojekOrderId.isBlank()) {
            viewModel.gojekOrderId = getParamString(
                ApplinkConst.TokoChat.ORDER_ID_GOJEK,
                arguments,
                savedInstanceState
            )
        }

        if (viewModel.source.isBlank()) {
            viewModel.source = getParamString(
                ApplinkConst.TokoChat.PARAM_SOURCE,
                arguments,
                savedInstanceState
            )
        }

        if (viewModel.tkpdOrderId.isBlank()) {
            viewModel.tkpdOrderId = getParamString(
                ApplinkConst.TokoChat.ORDER_ID_TKPD,
                arguments,
                savedInstanceState
            )
        }

        if (!viewModel.isFromTokoFoodPostPurchase) {
            viewModel.isFromTokoFoodPostPurchase = getParamBoolean(
                ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE,
                arguments,
                savedInstanceState,
                false
            )
        }

        if (viewModel.pushNotifTemplateKey.isBlank()) {
            viewModel.pushNotifTemplateKey = getParamString(
                NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY,
                arguments,
                savedInstanceState,
                ""
            )
        }

        // Only set the flag when detected or has param extra
        val isFromBubbleExtra = getParamBoolean(
            IS_FROM_BUBBLE_KEY,
            arguments,
            savedInstanceState,
            false
        )
        if (activity?.isFromBubble() == true || isFromBubbleExtra) {
            viewModel.isFromBubble = true
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

    private fun setupAttachmentMenu() {
        baseBinding?.tokochatLayoutMenu?.updateAttachmentMenu(
            listener = this,
            showImageAttachment = true
        )
    }

    override fun initObservers() {
        observeTokoChatBackground()
        observeChatRoomTicker()
        observeChannelDetails()
        observeLoadOrderTransactionStatus()
        observeUpdateOrderTransactionStatus()
        observeChatConnection()
        observeImageUploadError()
        observeError()
        observeUserConsent()
    }

    private fun observeError() {
        observe(viewModel.error) {
            logExceptionTokoChat(
                it.first,
                TokoChatErrorLogger.ErrorType.ERROR_PAGE,
                it.second
            )
        }
    }

    private fun observeImageUploadError() {
        observe(viewModel.imageUploadError) {
            if (view != null && context != null) {
                Toaster.build(
                    view = requireView(),
                    text = ErrorHandler.getErrorMessage(context, it.second),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR
                ).show()
            }
            adapter.getImageAttachmentPairWithId(it.first)?.let { (position, element) ->
                element.updateImageState(TokoChatImageBubbleUiModel.ImageState.ERROR_UPLOAD)
                notifyWhenAllowed(position)
            }
        }
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
            if (isChannelReadOnly()) {
                setupReplySection(
                    false,
                    getString(com.tokopedia.tokochat_common.R.string.tokochat_message_closed_chat)
                )
                clearReplyBoxMessage()
            } else {
                tokoChatAnalytics?.clickSendMessage(
                    viewModel.tkpdOrderId,
                    TokoChatAnalyticsConstants.BUYER,
                    viewModel.source
                )
                onSendButtonClicked()
            }
        }
    }

    private fun getChatSendButton(): IconUnify? {
        return baseBinding?.tokochatReplyBox?.findViewById(com.tokopedia.tokochat_common.R.id.tokochat_ic_send_btn)
    }

    private fun onSendButtonClicked() {
        if (isValidComposedMessage() && viewModel.channelId.isNotEmpty()) {
            viewModel.sendMessage(viewModel.channelId, getComposedMessage())
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

    override fun getViewBindingInflate(container: ViewGroup?): TokochatChatroomFragmentBinding {
        return TokochatChatroomFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    private fun setupBackground() {
        viewModel.getTokoChatBackground()
    }

    private fun observeUpdateOrderTransactionStatus() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.updateOrderTransactionStatus.collect {
                when (it) {
                    is Success -> {
                        val state = it.data.tokochatOrderProgress.state
                        updateCallIcon(state)
                        updateShowTransactionWidget(it.data.tokochatOrderProgress)
                        if (state !in listOf(
                                OrderStatusType.CANCELLED,
                                OrderStatusType.COMPLETED
                            )
                        ) {
                            viewModel.updateOrderStatusParam(Pair(viewModel.tkpdOrderId, viewModel.source))
                        }
                    }
                    is Fail -> {
                        logExceptionTokoChat(
                            it.throwable,
                            TokoChatErrorLogger.ErrorType.ERROR_POOL_ORDER_PROGRESS,
                            TokoChatErrorLogger.ErrorDescription.POOL_ORDER_PROGRESS_ERROR
                        )
                        viewModel.updateOrderStatusParam(Pair(viewModel.tkpdOrderId, viewModel.source))
                    }
                }
            }
        }
    }

    private fun observeLoadOrderTransactionStatus() {
        observe(viewModel.orderTransactionStatus) {
            hideTransactionLocalLoad()
            when (it) {
                is Success -> {
                    setShowTransactionWidget(it.data.tokochatOrderProgress)
                    if (it.data.tokochatOrderProgress.state !in listOf(
                            OrderStatusType.CANCELLED,
                            OrderStatusType.COMPLETED
                        )
                    ) {
                        viewModel.updateOrderStatusParam(Pair(viewModel.tkpdOrderId, viewModel.source))
                    }
                    tokoChatAnalytics?.sendPendingImpressionOnImageAttachment(
                        it.data.tokochatOrderProgress.state
                    )
                }
                is Fail -> {
                    logExceptionTokoChat(
                        it.throwable,
                        TokoChatErrorLogger.ErrorType.ERROR_LOAD_ORDER_PROGRESS,
                        TokoChatErrorLogger.ErrorDescription.RENDER_ORDER_PROGRESS_ERROR
                    )
                    setShowTransactionLocalLoad()
                }
            }
        }
    }

    private fun hideTransactionLocalLoad() {
        baseBinding?.tokochatTransactionOrder?.hideTransactionLocalLoad()
    }

    private fun observeTokoChatBackground() {
        observe(viewModel.chatBackground) {
            when (it) {
                is Success -> renderBackground(it.data)
                is Fail -> {
                    // no op
                }
            }
        }
    }

    protected open fun observeChatRoomTicker() {
        observe(viewModel.chatRoomTicker) {
            when (it) {
                is Success -> {
                    val ticker = TokoChatReminderTickerUiModel(
                        it.data.tokochatRoomTicker.message,
                        it.data.tokochatRoomTicker.tickerType
                    )
                    mapper.setFirstTicker(ticker)

                    // If the ticker is not in list, manually add ticker
                    if (adapter.getItems().getOrNull(adapter.itemCount - Int.ONE)
                        !is TokoChatReminderTickerUiModel
                    ) {
                        adapter.addItem(adapter.itemCount, ticker)
                        adapter.notifyItemInserted(adapter.itemCount)
                    }

                    addBubbleTicker()
                }
                is Fail -> {
                    // no op
                }
            }
        }
    }

    private fun addBubbleTicker() {
        if (TokoChatValueUtil.shouldShowBubblesAwareness() &&
            viewModel.shouldShowTickerBubblesCache() &&
            (activity?.isFromBubble() == false) &&
            !viewModel.isFromBubble
        ) {
            val tickerBubble = TokoChatReminderTickerUiModel(
                message = getString(R.string.tokochat_bubbles_ticker_desc),
                tickerType = Ticker.TYPE_ANNOUNCEMENT,
                showCloseButton = true,
                tag = BUBBLES_NOTIF
            )
            mapper.setBubbleTicker(tickerBubble)

            // If the ticker is not in list, manually add ticker
            val tickerBubblePair = adapter.getTickerPairWithTag(BUBBLES_NOTIF)
            if (tickerBubblePair == null) {
                adapter.addItem(Int.ZERO, tickerBubble)
                adapter.notifyItemInserted(Int.ZERO)
                scrollToBottom()
            }
        }
    }

    private fun observeChannelDetails() {
        observe(viewModel.channelDetail) {
            when (it) {
                is Success -> setHeaderData(it.data)
                is Fail -> {
                    handleFailGetChannelDetails(it.throwable)
                }
            }
            observeMemberLeft()
        }
    }

    private fun handleFailGetChannelDetails(error: Throwable) {
        hideShimmeringHeader()
        try {
            val doesChatNotExist: Boolean = if (error is ConversationsNetworkError) {
                error.errorList.firstOrNull()?.code?.contains(
                    CHAT_DOES_NOT_EXIST,
                    ignoreCase = true
                ) ?: false
            } else {
                false
            }
            if (doesChatNotExist) {
                showUnavailableBottomSheet()
            } else {
                showGlobalErrorWithRefreshAction()
            }
        } catch (throwable: Throwable) {
            showGlobalErrorWithRefreshAction()
            logExceptionTokoChat(
                throwable,
                TokoChatErrorLogger.ErrorType.ERROR_PAGE,
                ::handleFailGetChannelDetails.name
            )
        }
    }

    private fun setHeaderData(channelDetails: GroupBookingChannelDetails) {
        channelDetails.members.forEach { member ->
            if (member.ownerType == DRIVER) {
                headerUiModel = TokoChatHeaderUiModel(
                    member.id,
                    member.name,
                    member.userMetadata?.licencePlate ?: "",
                    member.profileUrl ?: "",
                    member.phone
                )
            }
            if (member.ownerType == CUSTOMER) {
                selfUiModel = TokoChatHeaderUiModel(
                    id = member.id,
                    title = member.name,
                    imageUrl = member.profileUrl ?: "",
                    phoneNumber = member.phone
                )
            }
        }
        loadTransactionWidget()
    }

    private fun observeMemberLeft() {
        // reset member left live data before observe to remove old data
        viewModel.resetMemberLeft()
        viewModel.getMemberLeft()?.observe(viewLifecycleOwner) {
            // If the livedata gives null, then do nothing
            // If the livedata gives old data, then do nothing
            if (it != null && it == headerUiModel?.id) {
                showUnavailableBottomSheet()
            }
        }
    }

    private fun loadTransactionWidget(isFromLocalLoad: Boolean = false) {
        baseBinding?.tokochatTransactionOrder?.showShimmeringWidget()
        if (viewModel.tkpdOrderId.isNotBlank()) {
            viewModel.loadOrderCompletedStatus(viewModel.tkpdOrderId, viewModel.source)
        } else {
            if (!isFromLocalLoad) {
                observeTkpdOrderId() // Only need to observe this if tkpdOrderId is empty
            }
            viewModel.translateGojekOrderId(viewModel.gojekOrderId)
        }
    }

    private fun updateShowTransactionWidget(tokoChatOrderProgress: TokoChatOrderProgressResponse.TokoChatOrderProgress) {
        val orderProgressUiModel = TokoChatOrderProgressUiModel(
            isEnable = tokoChatOrderProgress.enable,
            state = tokoChatOrderProgress.state,
            imageUrl = tokoChatOrderProgress.imageUrl,
            invoiceId = tokoChatOrderProgress.invoiceId,
            labelTitle = tokoChatOrderProgress.label.title,
            labelValue = tokoChatOrderProgress.label.value,
            name = tokoChatOrderProgress.name,
            orderId = tokoChatOrderProgress.orderId,
            status = tokoChatOrderProgress.status,
            statusId = tokoChatOrderProgress.statusId,
            appLink = tokoChatOrderProgress.uri
        )
        baseBinding?.tokochatTransactionOrder?.updateTransactionWidget(orderProgressUiModel)
    }

    private fun setShowTransactionWidget(tokoChatOrderProgress: TokoChatOrderProgressResponse.TokoChatOrderProgress) {
        val orderProgressUiModel = TokoChatOrderProgressUiModel(
            isEnable = tokoChatOrderProgress.enable,
            state = tokoChatOrderProgress.state,
            imageUrl = tokoChatOrderProgress.imageUrl,
            invoiceId = tokoChatOrderProgress.invoiceId,
            labelTitle = tokoChatOrderProgress.label.title,
            labelValue = tokoChatOrderProgress.label.value,
            name = tokoChatOrderProgress.name,
            orderId = tokoChatOrderProgress.orderId,
            status = tokoChatOrderProgress.status,
            statusId = tokoChatOrderProgress.statusId,
            appLink = tokoChatOrderProgress.uri
        )

        baseBinding?.tokochatTransactionOrder?.showTransactionWidget(
            orderProgressUiModel
        )

        headerUiModel?.let { header ->
            setupToolbarData(header)
            showHeader()
        }
    }

    private fun observeLiveChannel() {
        viewModel.getLiveChannel(viewModel.channelId)?.observe(viewLifecycleOwner) {
            it?.let { channel ->
                // Show bottom sheet if channel expires
                expiresAt = channel.expiresAt
                if (isChannelExpired()) {
                    showUnavailableBottomSheet()
                } else {
                    // Check if channel is read only
                    val readModeLong = channel.readModeStartsAt ?: 0
                    if (readModeLong != 0L) {
                        readModeStartsAt = readModeLong
                    }
                    if (channel.readOnly || isChannelReadOnly()) {
                        // Hide reply component
                        setupReplySection(
                            false,
                            getString(com.tokopedia.tokochat_common.R.string.tokochat_message_closed_chat)
                        )
                    } else {
                        // Show reply component
                        setupReplySection(true)
                    }
                }
            }
        }
    }

    private fun isChannelExpired(): Boolean {
        return expiresAt < System.currentTimeMillis() && expiresAt > 0
    }

    private fun isChannelReadOnly(): Boolean {
        return readModeStartsAt < System.currentTimeMillis() && readModeStartsAt > 0
    }

    private fun observeChatConnection() {
        observe(viewModel.isChatConnected) { connect ->
            if ((!connect || !isConnectedToNetwork()) &&
                viewModel.channelId.isNotBlank() && viewModel.getUserId().isNotBlank()
            ) {
                if (!errorBottomSheet.isVisible) {
                    errorBottomSheet.setErrorType(getErrorType())
                    errorBottomSheet.setButtonAction {
                        initGroupBooking()
                        errorBottomSheet.dismissBottomSheet()
                    }
                    errorBottomSheet.show(childFragmentManager)
                }
            } else {
                errorBottomSheet.dismissBottomSheet()
            }
        }
    }

    private fun setShowTransactionLocalLoad() {
        baseBinding?.tokochatTransactionOrder?.showLocalLoadTransaction()

        headerUiModel?.let { header ->
            setupToolbarData(header)
            showHeader()
        }
    }

    private fun setupToolbarData(headerUiModel: TokoChatHeaderUiModel) {
        getTokoChatHeader()?.run {
            val userTitle =
                findViewById<Typography>(com.tokopedia.tokochat_common.R.id.tokochat_text_user_title)
            val subTitle =
                findViewById<Typography>(com.tokopedia.tokochat_common.R.id.tokochat_text_user_subtitle)
            val imageUrl =
                findViewById<ImageUnify>(com.tokopedia.tokochat_common.R.id.tokochat_user_avatar)
            val callMenu =
                findViewById<IconUnify>(com.tokopedia.tokochat_common.R.id.tokochat_icon_header_menu)

            userTitle.text = headerUiModel.title
            subTitle.text = TokoChatViewUtil.censorPlatNumber(
                platNumber = headerUiModel.subTitle,
                percentageCensor = DEFAULT_CENSOR_PERCENTAGE
            )
            imageUrl.setImageUrl(headerUiModel.imageUrl)
            imageUrl.show()

            val sourceLogoUrl = getSourceLogoUrl()

            if (sourceLogoUrl.isNotBlank()) {
                val sourceLogo =
                    findViewById<ImageUnify>(com.tokopedia.tokochat_common.R.id.tokochat_iv_source_logo)
                sourceLogo.setImageUrl(sourceLogoUrl)
            }

            callMenu.run {
                val isCallIconDisabled = getOrderState() in listOf(OrderStatusType.COMPLETED, OrderStatusType.CANCELLED)

                if (isCallIconDisabled) {
                    isEnabled = false
                    isClickable = false
                    setImage(IconUnify.CALL, com.tokopedia.unifyprinciples.R.color.Unify_NN300)
                } else {
                    isEnabled = true
                    isClickable = true
                    setImage(IconUnify.CALL)

                    setOnClickListener {
                        if (headerUiModel.phoneNumber.isNotEmpty()) {
                            tokoChatAnalytics?.clickCallButtonFromChatRoom(
                                getOrderState(),
                                viewModel.tkpdOrderId,
                                viewModel.source,
                                TokoChatAnalyticsConstants.BUYER
                            )
                            showMaskingPhoneNumberBottomSheet(headerUiModel.phoneNumber)
                        }
                    }
                }
            }
        }
    }

    private fun updateCallIcon(orderState: String) {
        val isCompletedOrder = orderState in listOf(OrderStatusType.COMPLETED, OrderStatusType.CANCELLED)
        val isSameOrderStatus = orderState == getOrderState()

        if (isCompletedOrder) {
            if (isSameOrderStatus) return

            getTokoChatHeader()?.run {
                val callMenu =
                    findViewById<IconUnify>(com.tokopedia.tokochat_common.R.id.tokochat_icon_header_menu)

                callMenu.run {
                    isEnabled = false
                    isClickable = false
                    setImage(IconUnify.CALL, com.tokopedia.unifyprinciples.R.color.Unify_NN300)
                }
            }
        }
    }

    private fun showMaskingPhoneNumberBottomSheet(driverPhoneNumber: String) {
        view?.let {
            KeyboardHandler.DropKeyboard(it.context, it)
        }
        val bottomSheetMaskingPhoneNumber =
            MaskingPhoneNumberBottomSheet.newInstance(driverPhoneNumber)
        bottomSheetMaskingPhoneNumber.setTrackingListener(this)
        bottomSheetMaskingPhoneNumber.show(childFragmentManager)
    }

    private fun setupReplySection(isShowReplySection: Boolean, expiredMessage: String = "") {
        baseBinding?.tokochatReplyBox?.run {
            shouldShowWithAction(isShowReplySection) {
                this.initLayout(
                    typingListener = this@TokoChatFragment,
                    replyTextListener = this@TokoChatFragment,
                    replyAreaListener = this@TokoChatFragment
                )
            }
        }
        baseBinding?.tokochatExpiredInfo?.shouldShowWithAction(!isShowReplySection) {
            tokoChatAnalytics?.impressOnClosedChatroomTicker(
                viewModel.tkpdOrderId,
                TokoChatAnalyticsConstants.BUYER,
                viewModel.source
            )
            baseBinding?.tokochatExpiredInfo?.setExpiredInfoDesc(expiredMessage)
        }
    }

    private fun getSourceLogoUrl(): String {
        return when (viewModel.source) {
            TOKOFOOD -> IC_TOKOFOOD_SOURCE
            else -> ""
        }
    }

    private fun initGroupBooking() {
        viewModel.resetTypingStatus()
        viewModel.initGroupBooking(
            orderId = viewModel.gojekOrderId,
            groupBookingListener = this
        )
    }

    override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
        hideShimmeringHeader()
        removeShimmering()
        handleOnErrorCreateGroupBooking(error)
    }

    private fun handleOnErrorCreateGroupBooking(error: ConversationsNetworkError) {
        try {
            val errorCode = error.errorList.firstOrNull()?.code ?: ""
            if (errorCode.contains(CHAT_CLOSED_CODE, ignoreCase = true)) {
                showUnavailableBottomSheet()
            } else {
                showGlobalErrorWithRefreshAction()
            }
            logExceptionTokoChat(error, TokoChatErrorLogger.ErrorType.ERROR_PAGE, ::initGroupBooking.name)
        } catch (throwable: Throwable) {
            showGlobalErrorWithRefreshAction()
            logExceptionTokoChat(
                throwable,
                TokoChatErrorLogger.ErrorType.ERROR_PAGE,
                ::handleOnErrorCreateGroupBooking.name
            )
        }
    }

    override fun onGroupBookingChannelCreationStarted() {
        // No op
    }

    override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
        resetRecyclerViewScrollState()
        this@TokoChatFragment.viewModel.channelId = channelUrl
        viewModel.registerActiveChannel(channelUrl)
        viewModel.getGroupBookingChannel(viewModel.channelId)
        removeShimmering()
        observeChatHistory()
        observeLiveChannel()
        viewModel.doCheckChatConnection()
        trackFromPushNotif()
    }

    private fun trackFromPushNotif() {
        if (viewModel.pushNotifTemplateKey.isNotBlank()) {
            tokoChatAnalytics?.clickChatFromPushNotif(
                viewModel.tkpdOrderId,
                viewModel.pushNotifTemplateKey,
                TokoChatAnalyticsConstants.BUYER,
                viewModel.source
            )
        }
    }

    override fun onLoadMore() {
        viewModel.loadPreviousMessages()
        // Turn on the load more flag
        changeLoadMoreStatus(true)
    }

    private fun observeChatHistory() {
        viewModel.getChatHistory(viewModel.channelId)?.observe(viewLifecycleOwner) {
            // First time get Chat History
            handleFirstTimeGetChatHistory()

            // It's from load more func, if recyclerview is loading more
            // Should skip if recyclerview is not loading more message
            if (isRecyclerViewLoadingMore()) {
                // turn off the load more flag
                changeLoadMoreStatus(false)
            }

            // Map conversation message into ui model
            mapConversationMessageToUiModel(it)

            // Mark the chat as read
            viewModel.markChatAsRead(viewModel.channelId)
        }
    }

    protected open fun mapConversationMessageToUiModel(list: List<ConversationsMessage>) {
        val result = mapper.mapToChatUiModel(list, viewModel.getUserId())
        adapter.setItemsAndAnimateChanges(result)
        scrollToBottom()
    }

    private fun handleFirstTimeGetChatHistory() {
        if (firstTimeOpen) {
            firstTimeOpen = false
            viewModel.loadChatRoomTicker()
            observerTyping()
        }
    }

    override fun onStartTyping() {
        viewModel.setTypingStatus(true)
    }

    override fun onStopTyping() {
        viewModel.setTypingStatus(false)
    }

    private fun observerTyping() {
        viewModel.getTypingStatus()?.observe(viewLifecycleOwner) {
            if (headerUiModel != null && it.contains(headerUiModel?.id)) {
                showInterlocutorTypingStatus()
            } else {
                hideInterlocutorTypingStatus()
            }
        }
    }

    override fun trackSeenTicker(element: TokoChatReminderTickerUiModel) {
        tokoChatAnalytics?.impressOnTicker(
            viewModel.tkpdOrderId,
            TokoChatAnalyticsConstants.BUYER,
            viewModel.source
        )
    }

    override fun onLocalLoadRetryClicked() {
        loadTransactionWidget(isFromLocalLoad = true)
    }

    override fun onTransactionWidgetClicked(appLink: String) {
        if (viewModel.isFromTokoFoodPostPurchase) {
            activity?.finish()
        } else {
            if (appLink.isNotBlank()) {
                context?.let {
                    val intent = RouteManager.getIntent(it, appLink)
                    if (viewModel.isFromBubble) {
                        intent.putExtra(IS_FROM_BUBBLE_KEY, true)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    override fun onTransactionWidgetClosed() {
        tokoChatAnalytics?.clickCloseOrderWidget(
            viewModel.tkpdOrderId,
            TokoChatAnalyticsConstants.BUYER,
            viewModel.source
        )
    }

    override fun onClickLinkReminderTicker(
        element: TokoChatReminderTickerUiModel,
        linkUrl: String
    ) {
        if (element.tag == BUBBLES_NOTIF) {
            tokoChatAnalytics?.clickCheckHereOnBoardingTicker(
                orderId = viewModel.tkpdOrderId,
                source = viewModel.source,
                role = TokoChatAnalyticsConstants.BUYER
            )
            showBubblesAwarenessBottomSheet()
        }
        if (linkUrl.isNotEmpty()) {
            context?.let {
                RouteManager.route(it, linkUrl)
            }
        }
    }

    override fun onCloseReminderTicker(element: TokoChatReminderTickerUiModel, position: Int) {
        if (position == adapter.itemCount) {
            mapper.setFirstTicker(null)
        }
        if (element.tag == BUBBLES_NOTIF) {
            mapper.setBubbleTicker(null)
            viewModel.setBubblesPref(hasShownTicker = true)
            tokoChatAnalytics?.clickCloseOnBoardingTicker(
                orderId = viewModel.tkpdOrderId,
                source = viewModel.source,
                role = TokoChatAnalyticsConstants.BUYER
            )
        }
        adapter.removeItem(element)
        if (position >= Int.ZERO) {
            adapter.notifyItemRemoved(position)
        }
    }

    override fun loadImage(
        imageView: ImageView,
        element: TokoChatImageBubbleUiModel,
        isFromRetry: Boolean
    ) {
        if (element.isImageReady) return
        viewModel.getImageWithId(
            imageId = element.imageId,
            channelId = viewModel.channelId,
            onImageReady = { imageFile ->
                onImageReadyToLoad(imageView, element, imageFile)
            },
            onError = {
                onErrorLoadImage(element)
            },
            onDirectLoad = {
                onDirectLoadImage(element)
                impressOnImageAttachment(element, imageView)
            },
            imageView = imageView,
            isFromRetry = isFromRetry
        )
        if (isFromRetry) {
            tokoChatAnalytics?.clickRetryImage(
                attachmentId = element.imageId,
                orderId = viewModel.tkpdOrderId,
                role = tokoChatAnalytics?.getStringRole(element.isSender) ?: "",
                source = viewModel.source
            )
        }
    }

    override fun onImageDelivered(element: TokoChatImageBubbleUiModel) {
        // Remove from cache if image delivered
        viewModel.imageAttachmentMap.remove(element.imageId)
    }

    // error load image handler
    private fun onErrorLoadImage(
        element: TokoChatImageBubbleUiModel
    ) {
        activity?.runOnUiThread {
            // change state to error load
            element.updateImageState(TokoChatImageBubbleUiModel.ImageState.ERROR_LOAD)
            // get item position
            adapter.getImageAttachmentPairWithId(element.imageId)?.let {
                // notify
                notifyWhenAllowed(it.first)
            }
        }
    }

    override fun onCloseMaskingPhoneNumberBottomSheet() {
        val state = getOrderState()
        tokoChatAnalytics?.clickCloseBottomSheetCallDriver(
            state,
            viewModel.tkpdOrderId,
            viewModel.source,
            TokoChatAnalyticsConstants.BUYER
        )
    }

    override fun onConfirmCallOnBottomSheetCallDriver() {
        val state = getOrderState()
        tokoChatAnalytics?.clickConfirmCallOnBottomSheetCallDriver(
            state,
            viewModel.tkpdOrderId,
            viewModel.source,
            TokoChatAnalyticsConstants.BUYER
        )
    }

    private fun getOrderState(): String {
        return baseBinding?.tokochatTransactionOrder?.getTokoChatOrderProgressUiModel()?.state.orEmpty()
    }

    private fun onImageReadyToLoad(
        imageView: ImageView,
        element: TokoChatImageBubbleUiModel,
        imageFile: File?
    ) {
        activity?.runOnUiThread {
            imageFile?.let { file ->
                imageView.loadImage(file.absolutePath, properties = {
                    this.setPlaceHolder(
                        com.tokopedia.tokochat_common.R.drawable.tokochat_bg_image_bubble_gradient
                    )
                    this.setErrorDrawable(
                        com.tokopedia.tokochat_common.R.drawable.tokochat_bg_image_bubble_gradient
                    )
                    this.listener(onSuccess = { _, _ ->
                        impressOnImageAttachment(element, imageView)
                        element.updateImageData(imagePath = file.absolutePath, status = true)
                        onSuccessImageAttachment(element.imageId)
                    }, onError = {
                            onErrorLoadImage(element)
                        })
                    this.transforms(listOf(CenterCrop(), RoundedCorners(EIGHT_DP.toPx())))
                    this.setRoundedRadius(EIGHT_DP.toFloat())
                })
            }
        }
    }

    // Directly load image with glide from byte array
    private fun onDirectLoadImage(element: TokoChatImageBubbleUiModel) {
        activity?.runOnUiThread {
            onSuccessImageAttachment(element.imageId)
        }
    }

    private fun onSuccessImageAttachment(imageId: String) {
        // get item position
        adapter.getImageAttachmentPairWithId(imageId)?.let {
            val position = it.first
            val element = it.second
            if (viewModel.imageAttachmentMap[imageId] != null) {
                when {
                    (element.isFailed()) -> {
                        // change image state to error upload
                        element.updateImageState(TokoChatImageBubbleUiModel.ImageState.ERROR_UPLOAD)
                    }
                    (element.isDummy()) -> {
                        // change image state to error upload
                        element.updateImageState(TokoChatImageBubbleUiModel.ImageState.LOADING_UPLOAD)
                    }
                }
            } else {
                // change image state to success
                element.updateImageState(TokoChatImageBubbleUiModel.ImageState.SUCCESS)
            }

            // notify
            notifyWhenAllowed(position)
        }
    }

    private fun impressOnImageAttachment(
        element: TokoChatImageBubbleUiModel,
        imageView: ImageView
    ) {
        imageView.addOnImpressionListener(element.impressHolder) {
            val state = getOrderState()
            if (state.isNotEmpty()) {
                tokoChatAnalytics?.impressOnImageAttachment(
                    attachmentId = element.imageId,
                    orderStatus = state,
                    orderId = viewModel.tkpdOrderId,
                    role = tokoChatAnalytics?.getStringRole(element.isSender) ?: "",
                    source = viewModel.source
                )
            } else {
                tokoChatAnalytics?.saveImpressionOnImageAttachment(
                    attachmentId = element.imageId,
                    orderStatus = state,
                    orderId = viewModel.tkpdOrderId,
                    role = tokoChatAnalytics?.getStringRole(element.isSender) ?: "",
                    source = viewModel.source
                )
            }
        }
    }

    override fun onClickImage(element: TokoChatImageBubbleUiModel) {
        tokoChatAnalytics?.clickImageAttachment(
            element.imageId,
            getOrderState(),
            viewModel.tkpdOrderId,
            tokoChatAnalytics?.getStringRole(element.isSender) ?: "",
            viewModel.source
        )
        context?.let {
            val intent = ImageSecurePreviewActivity.getCallingIntent(
                it,
                arrayListOf(element.imagePath)
            )
            startActivity(intent)
            tokoChatAnalytics?.impressOnImagePreview(
                element.imageId,
                getOrderState(),
                viewModel.tkpdOrderId,
                tokoChatAnalytics?.getStringRole(element.isSender) ?: "",
                viewModel.source
            )
        }
    }

    override fun resendImage(element: TokoChatImageBubbleUiModel) {
        element.updateImageState(TokoChatImageBubbleUiModel.ImageState.LOADING_UPLOAD)
        // get item position
        adapter.getImageAttachmentPairWithId(element.imageId)?.let {
            // notify
            notifyWhenAllowed(it.first)
        }
        viewModel.resendImage(element.imageId)
    }

    override fun onClickReadMore(element: TokoChatMessageBubbleUiModel) {
        showLongMessageBottomSheet(element)
    }

    private fun logExceptionTokoChat(
        throwable: Throwable,
        errorType: String,
        description: String
    ) {
        TokoChatErrorLogger.logExceptionToServerLogger(
            TokoChatErrorLogger.PAGE.TOKOCHAT,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            description
        )
    }

    private fun showUnavailableBottomSheet() {
        if (unavailableBottomSheet.isVisible) return
        unavailableBottomSheet.setListener(buttonAction = {
            activity?.finish()
        })
        unavailableBottomSheet.show(childFragmentManager)
    }

    private fun showConsentBottomSheet() {
        if (consentBottomSheet.isVisible) return
        consentBottomSheet.setConsentListener(
            submitAction = {
                // Show chat room
                loadChatRoomData()
            },
            dismissAction = {
                tokoChatAnalytics?.clickDismissConsent(
                    role = TokoChatAnalyticsConstants.BUYER,
                    source = viewModel.source
                )
                activity?.finish()
            }
        )
        consentBottomSheet.show(childFragmentManager, TokoChatConsentBottomSheet.TAG)
    }

    private fun showLongMessageBottomSheet(element: TokoChatMessageBubbleUiModel) {
        val senderName = if (element.isSender) {
            selfUiModel?.title
        } else {
            headerUiModel?.title
        }
        val bottomSheet = TokoChatLongTextBottomSheet(
            element.messageText,
            senderName ?: ""
        )
        bottomSheet.show(childFragmentManager, TAG)
    }

    private fun setupTrackers() {
        tokoChatAnalytics = TokoChatAnalytics(
            isFromBubble = (activity?.isFromBubble() ?: false) || viewModel.isFromBubble
        )
    }

    private fun showGlobalErrorWithRefreshAction() {
        showGlobalErrorLayout(onActionClick = {
            initializeChatRoom(null)
        })
    }

    override fun onClickCheckGuide() {
        view?.hideKeyboard()
        TokoChatGuideChatBottomSheet().show(childFragmentManager)
    }

    override fun onClickImageAttachment() {
        context?.let {
            tokoChatAnalytics?.clickAddImageAttachment(
                orderId = viewModel.tkpdOrderId,
                role = TokoChatAnalyticsConstants.BUYER,
                source = viewModel.source
            )
            val intent = MediaPicker.intent(it) {
                pageSource(PageSource.TokoChat)
                modeType(ModeType.IMAGE_ONLY)
                singleSelectionMode()
            }
            mediaPickerResultLauncher.launch(intent)
        }
    }

    /**
     * Result launcher section
     */
    private val mediaPickerResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK || result.data == null) return@registerForActivityResult
        val imageData = MediaPicker.result(result.data)
        processImagePathToUpload(imageData.originalPaths)
    }

    private fun processImagePathToUpload(imagePathList: List<String>): String? {
        imagePathList.firstOrNull()?.let { imagePath ->
            if (imagePath.isNotEmpty()) {
                viewModel.uploadImage(filePath = imagePath) {
                    tokoChatAnalytics?.clickUploadButton(
                        attachmentId = it,
                        orderId = viewModel.tkpdOrderId,
                        role = TokoChatAnalyticsConstants.BUYER,
                        source = viewModel.source
                    )
                    adapter.getImageAttachmentPairWithId(it)?.let { (position, element) ->
                        element.updateImageState(
                            TokoChatImageBubbleUiModel.ImageState.LOADING_UPLOAD
                        )
                        // notify
                        notifyWhenAllowed(position)
                    }
                }
            }
        }
        return null
    }

    private fun notifyWhenAllowed(position: Int) {
        try {
            baseBinding?.tokochatChatroomRv?.post {
                adapter.notifyItemChanged(position)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun showBubblesAwarenessBottomSheet() {
        TokoChatBubblesAwarenessBottomSheet.createInstance().apply {
            setListener(this@TokoChatFragment)
        }.apply {
            setOnDismissListener {
                activity?.finish()
            }
        }.show(childFragmentManager)
    }

    override fun onClickContinue() {
        tokoChatAnalytics?.clickContinueOnboardingBottomSheet(
            orderId = viewModel.tkpdOrderId,
            source = viewModel.source,
            role = TokoChatAnalyticsConstants.BUYER
        )
    }

    override fun onSwipeNext() {
        tokoChatAnalytics?.swipeNextOnboardingBottomsheet(
            orderId = viewModel.tkpdOrderId,
            source = viewModel.source,
            role = TokoChatAnalyticsConstants.BUYER
        )
    }

    override fun onClickEdu() {
        tokoChatAnalytics?.clickSelengkapnyaOnboardingBottomSheet(
            orderId = viewModel.tkpdOrderId,
            source = viewModel.source,
            role = TokoChatAnalyticsConstants.BUYER
        )
    }

    override fun onClickSettingActivation() {
        tokoChatAnalytics?.clickActivateFromOnboardingBottomSheet(
            orderId = viewModel.tkpdOrderId,
            source = viewModel.source,
            role = TokoChatAnalyticsConstants.BUYER
        )
    }

    protected fun isConnectedToNetwork(): Boolean {
        return if (context != null) {
            networkUtil.isNetworkAvailable(requireContext())
        } else {
            false
        }
    }

    override fun getErrorType(): Int {
        return if (isConnectedToNetwork()) {
            GlobalError.SERVER_ERROR
        } else {
            GlobalError.NO_CONNECTION
        }
    }

    companion object {
        private const val TAG = "TokoChatFragment"
        const val TOKOCHAT_ATTACHMENT_MENU = "android_show_attachment_menu_tokochat"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
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
