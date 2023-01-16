package com.tokopedia.tokochat.view.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.google.android.material.snackbar.Snackbar
import com.tokochat.tokochat_config_common.util.TokoChatErrorLogger
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepreview.imagesecure.ImageSecurePreviewActivity
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat.analytics.TokoChatAnalytics
import com.tokopedia.tokochat.analytics.TokoChatAnalyticsConstants
import com.tokopedia.tokochat.databinding.TokochatChatroomFragmentBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.util.TokoChatMediaCleanupStorageWorker
import com.tokopedia.tokochat.util.TokoChatValueUtil.CHAT_CLOSED_CODE
import com.tokopedia.tokochat.util.TokoChatValueUtil.CHAT_DOES_NOT_EXIST
import com.tokopedia.tokochat.util.TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY
import com.tokopedia.tokochat.view.chatroom.bottomsheet.MaskingPhoneNumberBottomSheet
import com.tokopedia.tokochat.view.chatroom.bottomsheet.TokoChatGeneralUnavailableBottomSheet
import com.tokopedia.tokochat_common.util.OrderStatusType
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil.IC_TOKOFOOD_SOURCE
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.CUSTOMER
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.DRIVER
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.TOKOFOOD
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.EIGHT_DP
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat_common.view.customview.TokoChatReplyMessageView
import com.tokopedia.tokochat_common.view.customview.TokoChatTransactionOrderWidget
import com.tokopedia.tokochat_common.view.customview.bottomsheet.TokoChatLongTextBottomSheet
import com.tokopedia.tokochat_common.view.fragment.TokoChatBaseFragment
import com.tokopedia.tokochat_common.view.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat_common.view.listener.TokoChatMessageBubbleListener
import com.tokopedia.tokochat_common.view.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat_common.view.listener.TokoChatTypingListener
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatHeaderUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatOrderProgressUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatReminderTickerUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import java.io.File
import javax.inject.Inject

class TokoChatFragment :
    TokoChatBaseFragment<TokochatChatroomFragmentBinding>(),
    ConversationsGroupBookingListener,
    TokoChatTypingListener,
    TokoChatReplyTextListener,
    TokochatReminderTickerListener,
    TokoChatTransactionOrderWidget.Listener,
    TokoChatImageAttachmentListener,
    TokoChatMessageBubbleListener,
    MaskingPhoneNumberBottomSheet.AnalyticsListener {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var mapper: TokoChatConversationUiMapper

    @Inject
    lateinit var tokoChatAnalytics: TokoChatAnalytics

    private var headerUiModel: TokoChatHeaderUiModel? = null
    private var selfUiModel: TokoChatHeaderUiModel? = null
    private var firstTimeOpen = true
    private var readModeStartsAt: Long = 0

    private val unavailableBottomSheet = TokoChatGeneralUnavailableBottomSheet()

    override var adapter: TokoChatBaseAdapter = TokoChatBaseAdapter(
        this,
        this,
        this
    )

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        getComponent(TokoChatComponent::class.java).inject(this)
    }

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        setupBackground()
        setupTrackers()
        initializeChatRoom(savedInstanceState)
    }

    private fun initializeChatRoom(savedInstanceState: Bundle?) {
        setDataFromArguments(savedInstanceState)
        if (viewModel.gojekOrderId.isNotBlank()) { // Do not init when order id empty
            initGroupBooking()
        }
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        initializeChatRoom(savedInstanceState)
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
        observeMemberLeft()
        observeLoadOrderTransactionStatus()
        observeUpdateOrderTransactionStatus()
        observeChatConnection()
        observeError()
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
                tokoChatAnalytics.clickSendMessage(
                    viewModel.channelId,
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

    private fun observeChatRoomTicker() {
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
                }
                is Fail -> {
                    // no op
                }
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
        observe(viewModel.getMemberLeft()) {
            showUnavailableBottomSheet()
        }
    }

    private fun loadTransactionWidget() {
        baseBinding?.tokochatTransactionOrder?.showShimmeringWidget()
        viewModel.loadOrderCompletedStatus(viewModel.tkpdOrderId, viewModel.source)
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
            this,
            orderProgressUiModel
        )

        headerUiModel?.let { header ->
            setupToolbarData(header)
            showHeader()
        }
    }

    private fun observeLiveChannel() {
        observe(viewModel.getLiveChannel(viewModel.channelId)) {
            it?.let { channel ->
                // Show bottom sheet if channel expires
                if (channel.expiresAt < System.currentTimeMillis() && channel.expiresAt > 0) {
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
            subTitle.text = headerUiModel.subTitle
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
                            tokoChatAnalytics.clickCallButtonFromChatRoom(
                                getOrderState(),
                                viewModel.tkpdOrderId,
                                viewModel.channelId,
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
                this.initLayout(this@TokoChatFragment, this@TokoChatFragment)
            }
        }
        baseBinding?.tokochatExpiredInfo?.shouldShowWithAction(!isShowReplySection) {
            tokoChatAnalytics.impressOnClosedChatroomTicker(
                viewModel.channelId,
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
            tokoChatAnalytics.clickChatFromPushNotif(
                viewModel.channelId,
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
        observe(viewModel.getChatHistory(viewModel.channelId)) {
            // First time get Chat History
            if (firstTimeOpen) {
                firstTimeOpen = false
                viewModel.loadChatRoomTicker()
                observerTyping()
            }

            // It's from load more func, if recyclerview is loading more
            // Should skip if recyclerview is not loading more message
            if (isRecyclerViewLoadingMore()) {
                // turn off the load more flag
                changeLoadMoreStatus(false)
            }

            // Map conversation message into ui model
            val result = mapper.mapToChatUiModel(it, viewModel.getUserId())
            adapter.setItemsAndAnimateChanges(result)
            scrollToBottom()

            // Mark the chat as read
            viewModel.markChatAsRead(viewModel.channelId)
        }
    }

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

    override fun trackSeenTicker(element: TokoChatReminderTickerUiModel) {
        tokoChatAnalytics.impressOnTicker(
            viewModel.channelId,
            TokoChatAnalyticsConstants.BUYER,
            viewModel.source
        )
    }

    override fun onLocalLoadRetryClicked() {
        loadTransactionWidget()
    }

    override fun onTransactionWidgetClicked(appLink: String) {
        if (viewModel.isFromTokoFoodPostPurchase) {
            activity?.finish()
        } else {
            if (appLink.isNotBlank()) {
                context?.let {
                    RouteManager.route(it, appLink)
                }
            }
        }
    }

    override fun onTransactionWidgetClosed() {
        tokoChatAnalytics.clickCloseOrderWidget(
            viewModel.channelId,
            TokoChatAnalyticsConstants.BUYER,
            viewModel.source
        )
    }

    override fun onClickLinkReminderTicker(
        element: TokoChatReminderTickerUiModel,
        linkUrl: String
    ) {
        if (linkUrl.isNotEmpty()) {
            context?.let {
                RouteManager.route(it, linkUrl)
            }
        }
    }

    override fun onCloseReminderTicker(element: TokoChatReminderTickerUiModel, position: Int) {
        adapter.removeItem(element)
        if (position == adapter.itemCount) {
            mapper.setFirstTicker(null)
        }
    }

    override fun loadImage(
        imageView: ImageView,
        element: TokoChatImageBubbleUiModel,
        loader: LoaderUnify?,
        retryIcon: ImageUnify?,
        isFromRetry: Boolean
    ) {
        viewModel.getImageWithId(
            imageId = element.imageId,
            channelId = viewModel.channelId,
            onImageReady = { imageFile ->
                onImageReadyToLoad(imageView, element, loader, retryIcon, imageFile)
            },
            onError = {
                activity?.runOnUiThread {
                    loader?.hide()
                    retryIcon?.show()
                    element.updateShouldRetry(true)
                }
            },
            onDirectLoad = {
                onDirectLoadImage(element, loader, retryIcon)
                impressOnImageAttachment(element, imageView)
            },
            imageView = imageView,
            isFromRetry = isFromRetry
        )
    }

    override fun onCloseMaskingPhoneNumberBottomSheet() {
        val state = getOrderState()
        tokoChatAnalytics.clickCloseBottomSheetCallDriver(
            state,
            viewModel.tkpdOrderId,
            viewModel.channelId,
            viewModel.source,
            TokoChatAnalyticsConstants.BUYER
        )
    }

    override fun onConfirmCallOnBottomSheetCallDriver() {
        val state = getOrderState()
        tokoChatAnalytics.clickConfirmCallOnBottomSheetCallDriver(
            state,
            viewModel.tkpdOrderId,
            viewModel.channelId,
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
        loader: LoaderUnify?,
        retryIcon: ImageUnify?,
        imageFile: File?
    ) {
        activity?.runOnUiThread {
            loader?.hide()
            retryIcon?.hide()
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
                    })
                    this.transforms(listOf(CenterCrop(), RoundedCorners(EIGHT_DP.toPx())))
                    this.setRoundedRadius(EIGHT_DP.toFloat())
                })
                element.updateImageData(imagePath = file.absolutePath, status = true)
            }
            element.updateShouldRetry(false)
        }
    }

    private fun onDirectLoadImage(
        element: TokoChatImageBubbleUiModel,
        loader: LoaderUnify?,
        retryIcon: ImageUnify?
    ) {
        loader?.hide()
        retryIcon?.hide()
        element.updateShouldRetry(false)
    }

    private fun impressOnImageAttachment(
        element: TokoChatImageBubbleUiModel,
        imageView: ImageView
    ) {
        imageView.addOnImpressionListener(element.impressHolder) {
            tokoChatAnalytics.impressOnImageAttachment(
                element.imageId,
                getOrderState(),
                viewModel.tkpdOrderId,
                viewModel.channelId,
                TokoChatAnalyticsConstants.BUYER,
                viewModel.source
            )
        }
    }

    override fun onClickImage(element: TokoChatImageBubbleUiModel) {
        tokoChatAnalytics.clickImageAttachment(
            element.imageId,
            getOrderState(),
            viewModel.tkpdOrderId,
            viewModel.channelId,
            TokoChatAnalyticsConstants.BUYER,
            viewModel.source
        )
        context?.let {
            val intent = ImageSecurePreviewActivity.getCallingIntent(
                it,
                arrayListOf(element.imagePath)
            )
            startActivity(intent)
            tokoChatAnalytics.impressOnImagePreview(
                element.imageId,
                getOrderState(),
                viewModel.tkpdOrderId,
                viewModel.channelId,
                TokoChatAnalyticsConstants.BUYER,
                viewModel.source
            )
        }
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
        getComposeMessageArea()?.setTracker(
            trackOnClickComposeArea = {
                if (viewModel.channelId.isNotBlank()) {
                    tokoChatAnalytics.clickTextField(
                        viewModel.channelId,
                        TokoChatAnalyticsConstants.BUYER,
                        viewModel.source
                    )
                }
            }
        )
    }

    private fun showGlobalErrorWithRefreshAction() {
        showGlobalErrorLayout(onActionClick = {
            initializeChatRoom(null)
        })
    }

    companion object {
        private const val TAG = "TokoChatFragment"

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
