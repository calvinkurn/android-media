package com.tokopedia.tokochat.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.databinding.FragmentTokoChatBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.util.TokoChatErrorLogger
import com.tokopedia.tokochat.util.TokoChatViewUtil.loadByteArrayImage
import com.tokopedia.tokochat.view.bottomsheet.MaskingPhoneNumberBottomSheet
import com.tokopedia.tokochat.view.mapper.TokoChatConversationUiMapper
import com.tokopedia.tokochat_common.view.uimodel.TokoChatHeaderUiModel
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.util.OrderStatusType
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil.IC_TOKOFOOD_SOURCE
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.DRIVER
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.TOKOFOOD
import com.tokopedia.tokochat_common.view.fragment.TokoChatBaseFragment
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat_common.view.customview.TokoChatReplyMessageView
import com.tokopedia.tokochat_common.view.customview.TokoChatTransactionOrderWidget
import com.tokopedia.tokochat_common.view.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat_common.view.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat_common.view.listener.TokoChatTypingListener
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatOrderProgressUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatReminderTickerUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class TokoChatFragment : TokoChatBaseFragment<FragmentTokoChatBinding>(),
    ConversationsGroupBookingListener,
    TokoChatTypingListener,
    TokoChatReplyTextListener,
    TokochatReminderTickerListener,
    TokoChatTransactionOrderWidget.Listener,
    TokoChatImageAttachmentListener {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var mapper: TokoChatConversationUiMapper

    private var headerUiModel: TokoChatHeaderUiModel? = null
    private var channelId = ""
    private var source: String = ""
    private var tkpdOrderId: String = ""
    private var firstTimeOpen = true
    private var isFromTokoFoodPostPurchase = false

    override var adapter: TokoChatBaseAdapter = TokoChatBaseAdapter(this, this)

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        getComponent(TokoChatComponent::class.java).inject(this)
    }

    override fun additionalSetup() {
        AndroidThreeTen.init(context?.applicationContext)
    }

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        setDataFromArguments(savedInstanceState)
        setupBackground()
        initializeChatProfile()
        initGroupBooking(savedInstanceState)
        loadTransactionWidget()
    }

    override fun onStart() {
        super.onStart()
        if (channelId.isNotEmpty() && channelId.isNotBlank()) {
            viewModel.registerActiveChannel(channelId)
        }
    }

    override fun onStop() {
        super.onStop()
        if (channelId.isNotEmpty() && channelId.isNotBlank()) {
            viewModel.deRegisterActiveChannel(channelId)
        }
    }

    override fun onDestroy() {
        removeObservers(viewModel.orderTransactionStatus)
        removeObservers(viewModel.chatRoomTicker)
        removeObservers(viewModel.chatBackground)
        removeObservers(viewModel.isChatConnected)
        removeObservers(viewModel.channelDetail)
        removeObservers(viewModel.error)
        super.onDestroy()
    }

    private fun setDataFromArguments(savedInstanceState: Bundle?) {
        source = getParamString(ApplinkConst.TokoChat.PARAM_SOURCE, arguments,
            savedInstanceState)
        tkpdOrderId = getParamString(
            ApplinkConst.TokoChat.ORDER_ID_TKPD,
            arguments,
            savedInstanceState
        )
        isFromTokoFoodPostPurchase = getParamBoolean(
            ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE,
            arguments,
            savedInstanceState,
            false
        )
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
        observeMemberLeft()
        observeLoadOrderTransactionStatus()
        observeUpdateOrderTransactionStatus()
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

    private fun observeUpdateOrderTransactionStatus() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.updateOrderTransactionStatus.collect {
                when (it) {
                    is Success -> {
                        setShowTransactionWidget(it.data.tokochatOrderProgress)
                        if (it.data.tokochatOrderProgress.state !in listOf(OrderStatusType.CANCELLED, OrderStatusType.COMPLETED)) {
                            viewModel.updateOrderStatusParam(Pair(tkpdOrderId, source))
                        }
                    }
                    is Fail -> {
                        logExceptionTokoChat(
                            it.throwable,
                            TokoChatErrorLogger.ErrorType.ERROR_POOL_ORDER_PROGRESS,
                            TokoChatErrorLogger.ErrorDescription.POOL_ORDER_PROGRESS_ERROR
                        )
                        viewModel.updateOrderStatusParam(Pair(tkpdOrderId, source))
                    }
                }
            }
        }
    }

    private fun observeLoadOrderTransactionStatus() {
        observe(viewModel.orderTransactionStatus) {
            when (it) {
                is Success -> {
                    setShowTransactionWidget(it.data.tokochatOrderProgress)
                    if (it.data.tokochatOrderProgress.state !in listOf(OrderStatusType.CANCELLED, OrderStatusType.COMPLETED)) {
                        viewModel.updateOrderStatusParam(Pair(tkpdOrderId, source))
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
                        showHeader()
                    }
                }
                is Fail -> {
                    hideShimmeringHeader()
                    showSnackbarError(it.throwable.message.toString())
                }
            }
        }
    }

    private fun observeMemberLeft() {
        observe(viewModel.getMemberLeft()) {
            Log.d("MEMBER LEFT - CONV", it)
            // TODO: Add bottomsheet for not available chat
        }
    }

    private fun loadTransactionWidget() {
        baseBinding?.tokochatTransactionOrder?.showShimmeringWidget()
        viewModel.loadOrderCompletedStatus(tkpdOrderId, source)
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
    }

    private fun observeLiveChannel() {
        observe(viewModel.getLiveChannel(channelId)) {
            it?.let { channel ->
                // Show bottom sheet if channel expires
                if (channel.expiresAt < System.currentTimeMillis()) {

                } else {
                    // Check if channel is read only
                    if (channel.readOnly || (channel.readModeStartsAt?: 0) < System.currentTimeMillis()) {
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

    private fun setShowTransactionLocalLoad() {
        baseBinding?.tokochatTransactionOrder?.showLocalLoadTransaction()
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

            val sourceLogoUrl = getSourceLogoUrl(source)

            if (sourceLogoUrl.isNotBlank()) {
                val sourceLogo = findViewById<ImageUnify>(R.id.tokochat_iv_source_logo)
                sourceLogo.setImageUrl(sourceLogoUrl)
            }

            callMenu.run {
                setImage(IconUnify.CALL)

                setOnClickListener {
                    if (headerUiModel.phoneNumber.isNotEmpty()) {
                        showMaskingPhoneNumberBottomSheet(headerUiModel.phoneNumber)
                    }
                }
            }
        }
    }

    private fun showMaskingPhoneNumberBottomSheet(driverPhoneNumber: String) {
        val bottomSheetMaskingPhoneNumber = MaskingPhoneNumberBottomSheet.newInstance(driverPhoneNumber)
        bottomSheetMaskingPhoneNumber.show(childFragmentManager)
    }

    private fun setupReplySection(isShowReplySection: Boolean, expiredMessage: String = "") {
        baseBinding?.tokochatReplyBox?.run {
            shouldShowWithAction(isShowReplySection) {
                this.initLayout(this@TokoChatFragment, this@TokoChatFragment)
            }
        }
        baseBinding?.tokochatExpiredInfo?.shouldShowWithAction(!isShowReplySection) {
            baseBinding?.tokochatExpiredInfo?.setExpiredInfoDesc(expiredMessage)
        }
    }

    private fun getSourceLogoUrl(source: String?): String {
        return when (source) {
            TOKOFOOD -> IC_TOKOFOOD_SOURCE
            else -> ""
        }
    }

    private fun initializeChatProfile() {
        val userId = viewModel.getUserId()
        if (userId.isEmpty() || userId.isBlank()) {
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
            groupBookingListener = this
        )
    }

    override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
        removeShimmering()
        var errorMessage = error.getErrorMessage()
        if (errorMessage.isEmpty()) {
            errorMessage = error.toString()
            showSnackbarError(errorMessage)
        }
    }

    override fun onGroupBookingChannelCreationStarted() {
        // No op
    }

    override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
        resetRecyclerViewScrollState()
        this@TokoChatFragment.channelId = channelUrl
        viewModel.registerActiveChannel(channelUrl)
        viewModel.getGroupBookingChannel(channelId)
        removeShimmering()
        observeChatHistory()
        observeLiveChannel()
    }

    override fun onLoadMore() {
        viewModel.loadPreviousMessages()
        // Turn on the load more flag
        changeLoadMoreStatus(true)
    }

    private fun observeChatHistory() {
        observe(viewModel.getChatHistory(channelId)) {
            // First time get Chat History
            if (firstTimeOpen) {
                firstTimeOpen = false
                viewModel.loadChatRoomTicker()
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
            viewModel.markChatAsRead(channelId)
        }
    }

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

    override fun trackSeenTicker(element: TokoChatReminderTickerUiModel) {
        // TODO: Tracker
    }

    override fun onLocalLoadRetryClicked() {
        loadTransactionWidget()
    }

    override fun onTransactionWidgetClicked(appLink: String) {
        if (isFromTokoFoodPostPurchase) {
            activity?.finish()
        } else {
            if (appLink.isNotBlank()) {
                context?.let {
                    RouteManager.route(it, appLink)
                }
            }
        }
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
        loader: LoaderUnify?
    ) {
        viewModel.getImageWithId(element.imageId, channelId) { imageResult, responseBody ->
            element.updateImageUrl(imageResult.data.url)
            context?.let { ctx ->
                loadByteArrayImage(ctx, imageView, responseBody.byteStream()) {
                    loader?.hide()
                }
            }
        }
    }

    private fun logExceptionTokoChat(
        throwable: Throwable,
        errorType: String,
        description: String,
    ){
        TokoChatErrorLogger.logExceptionToServerLogger(
            TokoChatErrorLogger.PAGE.TOKOCHAT,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            description
        )
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
