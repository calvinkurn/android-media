package com.tokopedia.topchat.chatroom.view.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.*
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.activity.TopchatReportWebViewActivity
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactoryImpl
import com.tokopedia.topchat.chatroom.view.adapter.util.LoadMoreTopBottomScrollListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.BroadcastSpamHandlerViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.QuotationViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuStickerView
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuView
import com.tokopedia.topchat.chatroom.view.custom.TransactionOrderProgressLayout
import com.tokopedia.topchat.chatroom.view.customview.TopChatRoomDialog
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewStateImpl
import com.tokopedia.topchat.chatroom.view.listener.*
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.chatroom.view.viewmodel.*
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.custom.ToolTipStickerPopupWindow
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import javax.inject.Inject
import kotlin.math.abs

/**
 * @author : Steven 29/11/18
 */

class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View, TypingListener,
        SendButtonListener, ImagePickerListener, ChatTemplateListener,
        HeaderMenuListener, DualAnnouncementListener, TopChatVoucherListener,
        InvoiceThumbnailListener, QuotationViewHolder.QuotationListener,
        TransactionOrderProgressLayout.Listener, ChatMenuStickerView.StickerMenuListener,
        StickerViewHolder.Listener, DeferredViewHolderAttachment, CommonViewHolderListener, SearchListener,
        BroadcastSpamHandlerViewHolder.Listener {

    @Inject
    lateinit var presenter: TopChatRoomPresenter

    @Inject
    lateinit var topChatRoomDialog: TopChatRoomDialog

    @Inject
    lateinit var analytics: TopChatAnalytics

    @Inject
    lateinit var settingAnalytics: ChatSettingsAnalytics

    @Inject
    lateinit var session: UserSessionInterface

    private lateinit var fpm: PerformanceMonitoring
    private lateinit var alertDialog: Dialog
    private lateinit var customMessage: String
    private lateinit var adapter: TopChatRoomAdapter
    private lateinit var toolTip: ToolTipStickerPopupWindow
    private var indexFromInbox = -1
    private var isMoveItemInboxToTop = false
    private var remoteConfig: RemoteConfig? = null
    private var sourcePage: String = ""
    private var createTime: String = ""
    private var searchQuery: String = ""
    private var delaySendMessage: String = ""
    private var delaySendSticker: Sticker? = null

    private val REQUEST_GO_TO_SHOP = 111
    private val TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 112
    private val REQUEST_GO_TO_SETTING_TEMPLATE = 113
    private val REQUEST_GO_TO_SETTING_CHAT = 114
    private val REQUEST_GO_TO_NORMAL_CHECKOUT = 115
    private val REQUEST_ATTACH_INVOICE = 116
    private val REQUEST_ATTACH_VOUCHER = 117
    private val REQUEST_REPORT_USER = 118

    private var seenAttachedProduct = HashSet<Int>()
    private var seenAttachedBannedProduct = HashSet<Int>()
    private var composeArea: EditText? = null
    private var orderProgress: TransactionOrderProgressLayout? = null
    private var chatMenu: ChatMenuView? = null
    private var rvLayoutManager: LinearLayoutManager? = null
    private var rvScrollListener: LoadMoreTopBottomScrollListener? = null
    private var fbNewUnreadMessage: FloatingButtonUnify? = null
    private var tvTotalUnreadMessage: Typography? = null
    private var rv: RecyclerView? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view
    override fun getAnalytic(): TopChatAnalytics = analytics
    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFireBase()
        initTooltipPopup()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topchat_chatroom, container, false).also {
            bindView(it)
            initStickerView()
            initFbNewUnreadMessage()
        }
    }

    private fun initFbNewUnreadMessage() {
        val customView = layoutInflater.inflate(R.layout.custom_fb_new_unread_message, null).apply {
            tvTotalUnreadMessage = this.findViewById(R.id.txt_new_unread_message)
        }
        fbNewUnreadMessage?.setMargins(0, 0, 0, 0)
        fbNewUnreadMessage?.addItem(customView)
    }

    private fun bindView(view: View?) {
        composeArea = view?.findViewById(R.id.new_comment)
        orderProgress = view?.findViewById(R.id.ll_transaction_progress)
        chatMenu = view?.findViewById(R.id.fl_chat_menu)
        rv = view?.findViewById(recyclerViewResourceId)
        fbNewUnreadMessage = view?.findViewById(R.id.fb_new_unread_message)
    }

    private fun initStickerView() {
        chatMenu?.setStickerListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPresenter(savedInstanceState)
        setupArguments(savedInstanceState)
        setupAttachmentsPreview(savedInstanceState)
        setupAlertDialog()
        setupAnalytic()
        setupBeforeReplyTime()
        loadInitialData()
        initLoadMoreListener()
    }

    private fun setupBeforeReplyTime() {
        if (createTime.isNotEmpty()) {
            presenter.setBeforeReplyTime(createTime)
        }
    }

    private fun initLoadMoreListener() {
        rvScrollListener = object : LoadMoreTopBottomScrollListener(rvLayoutManager) {
            override fun loadMoreTop() {
                showTopLoading()
                presenter.loadTopChat(messageId, ::onErrorGetTopChat, ::onSuccessGetTopChat)
            }

            override fun loadMoreDown() {
                showBottomLoading()
                presenter.loadBottomChat(messageId, ::onErrorGetBottomChat, ::onSuccessGetBottomChat)
            }
        }.also {
            rv?.addOnScrollListener(it)
        }
    }

    private fun onSuccessGetTopChat(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        rvScrollListener?.finishTopLoadingState()
        adapter.removeLastHeaderDateIfSame(chatRoom)
        renderList(chatRoom.listChat)
        updateHasNextState(chat)
        loadChatRoomSettings(chatRoom)
        presenter.loadAttachmentData(messageId.toInt(), chatRoom)
    }

    private fun onErrorGetTopChat(throwable: Throwable) {
        hideTopLoading()
        showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, throwable))
        rvScrollListener?.finishTopLoadingState()
    }


    private fun onSuccessGetBottomChat(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        rvScrollListener?.finishBottomLoadingState()
        adapter.removeLatestHeaderDateIfSame(chatRoom.listChat)
        adapter.setLatestHeaderDate(chatRoom.latestHeaderDate)
        updateNewUnreadMessageState(chat)
        renderBottomList(chatRoom.listChat)
        updateHasNextAfterState(chat)
        presenter.loadAttachmentData(messageId.toInt(), chatRoom)
    }

    private fun updateNewUnreadMessageState(chat: ChatReplies) {
        if (!chat.hasNextAfter) {
            onViewReachBottomMostChat()
        }
    }

    private fun renderBottomList(listChat: List<Visitable<*>>) {
        adapter.hideBottomLoading()
        if (listChat.isNotEmpty()) {
            adapter.addBottomData(listChat)
        }
    }

    private fun onErrorGetBottomChat(throwable: Throwable) {
        rvScrollListener?.finishBottomLoadingState()
        adapter.hideBottomLoading()
        showSnackbarError(ErrorHandler.getErrorMessage(view?.context, throwable))
    }

    private fun showBottomLoading() {
        adapter.showBottomLoading()
    }

    private fun showTopLoading() {
        showLoading()
    }

    private fun hideTopLoading() {
        hideLoading()
    }

    private fun initTooltipPopup() {
        toolTip = ToolTipStickerPopupWindow(context, presenter)
    }

    private fun setupAnalytic() {
        analytics.setSourcePage(sourcePage)
    }

    override fun onCreateViewState(view: View): BaseChatViewState {
        return TopChatViewStateImpl(
                view, this, this, this,
                this, this, this, this,
                (activity as BaseChatToolbarActivity).getToolbar(), analytics
        )
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            context?.let {
                val chatComponent = DaggerChatComponent.builder()
                        .baseAppComponent(((activity as Activity).application as BaseMainApplication).baseAppComponent)
                        .chatRoomContextModule(ChatRoomContextModule(it))
                        .build()
                chatComponent.inject(this)
            }
            presenter.attachView(this)
        }
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true).also {
            rvLayoutManager = it
        }
    }

    override fun loadInitialData() {
        showLoading()
        if (messageId.isNotEmpty()) {
            presenter.getExistingChat(
                    messageId,
                    ::onErrorInitiateData,
                    ::onSuccessGetExistingChatFirstTime
            )
            presenter.connectWebSocket(messageId)
            presenter.getOrderProgress(messageId)
        } else {
            presenter.getMessageId(
                    toUserId,
                    toShopId,
                    source,
                    onError(),
                    onSuccessGetMessageId()
            )
        }
    }

    private fun initFireBase() {
        fpm = PerformanceMonitoring.start(getFpmKey())
        remoteConfig = FirebaseRemoteConfigImpl(activity)
    }

    private fun getFpmKey() = if (GlobalConfig.isSellerApp()) {
        TopChatAnalytics.FPM_DETAIL_CHAT_SELLERAPP
    } else {
        TopChatAnalytics.FPM_DETAIL_CHAT
    }

    private fun setupPresenter(savedInstanceState: Bundle?) {
        presenter.attachView(this)
    }

    private fun setupArguments(savedInstanceState: Bundle?) {
        customMessage = getParamString(ApplinkConst.Chat.CUSTOM_MESSAGE, arguments, savedInstanceState)
        sourcePage = getParamString(ApplinkConst.Chat.SOURCE_PAGE, arguments, savedInstanceState)
        indexFromInbox = getParamInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, arguments, savedInstanceState)
        createTime = getParamString(ApplinkConst.Chat.SEARCH_CREATE_TIME, arguments, savedInstanceState)
        searchQuery = getParamString(ApplinkConst.Chat.SEARCH_PRODUCT_KEYWORD, arguments, savedInstanceState)
    }

    private fun setupAttachmentsPreview(savedInstanceState: Bundle?) {
        presenter.initProductPreview(savedInstanceState)
        presenter.initInvoicePreview(savedInstanceState)
        presenter.initAttachmentPreview()
    }

    private fun setupAlertDialog() {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }
        hideLoading()
    }

    private fun onUnblockChatClicked(): () -> Unit {
        return {
            analytics.trackClickUnblockChat(shopId)
            presenter.unblockChat(messageId, opponentRole, onError(), onSuccessUnblockChat())
        }
    }

    private fun onSuccessUnblockChat(): (BlockedStatus) -> Unit {
        return {
            view?.let {
                Toaster.make(it, String.format(getString(com.tokopedia.chat_common.R.string.chat_unblocked_text),
                        opponentName), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
            }
            getViewState().removeChatBlocked(it)
        }
    }

    private fun onSuccessGetMessageId(): (String) -> Unit {
        return {
            this.messageId = it
            loadInitialData()
        }
    }

    private fun onSuccessGetExistingChatFirstTime(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        setupFirstTimeOnly(chatRoom, chat)
        setupFirstPage(chatRoom, chat)
        fpm.stopTrace()
    }

    private fun setupFirstTimeOnly(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        getViewState().isPromoBlocked = chatRoom.blockedStatus.isPromoBlocked
        updateViewData(chatRoom)
        checkCanAttachVoucher()
        presenter.getShopFollowingStatus(shopId, ::onErrorGetShopFollowingStatus, ::onSuccessGetShopFollowingStatus)
        orderProgress?.renderIfExist()
        getViewState().onSuccessLoadFirstTime(chatRoom, onToolbarClicked(), this, alertDialog, onUnblockChatClicked())
        getViewState().onSetCustomMessage(customMessage)
        presenter.getTemplate(chatRoom.isSeller())
        presenter.getStickerGroupList(chatRoom)
        showStickerOnBoardingTooltip()
    }

    private fun setupFirstPage(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        adapter.setLatestHeaderDate(chatRoom.latestHeaderDate)
        renderList(chatRoom.listChat)
        updateHasNextState(chat)
        updateHasNextAfterState(chat)
        loadChatRoomSettings(chatRoom)
        presenter.loadAttachmentData(messageId.toInt(), chatRoom)
    }

    private fun updateHasNextAfterState(chat: ChatReplies) {
        val hasNextAfter = chat.hasNextAfter
        rvScrollListener?.updateHasNextAfterState(chat)
        if (hasNextAfter) {
            showBottomLoading()
        }
    }

    private fun updateHasNextState(chat: ChatReplies) {
        val hasNext = chat.hasNext
        rvScrollListener?.updateHasNextState(chat)
        if (hasNext) {
            showTopLoading()
        }
    }

    private fun showStickerOnBoardingTooltip() {
        if (!presenter.isStickerTooltipAlreadyShow()) {
            toolTip.showAtTop(getViewState().chatStickerMenuButton)
        }
    }

    private fun checkCanAttachVoucher() {
        if (amISeller) {
            chatMenu?.addVoucherAttachmentMenu()
        }
    }

    private fun onErrorGetShopFollowingStatus(throwable: Throwable) {
        getViewState().isShopFollowed = false
    }

    private fun onSuccessGetShopFollowingStatus(isFollow: Boolean) {
        getViewState().isShopFollowed = isFollow
        addBroadCastSpamHandler(isFollow)
    }

    private fun addBroadCastSpamHandler(isFollow: Boolean) {
        if (getViewState().isPromoBlocked || isFollow || presenter.isInTheMiddleOfThePage()) return
        val broadCastHandlerPosition = adapter.addBroadcastSpamHandler()
        if (broadCastHandlerPosition != RecyclerView.NO_POSITION) {
            val firstVisible = rvLayoutManager?.findFirstCompletelyVisibleItemPosition() ?: return
            val threshold = 1
            if (abs(firstVisible - broadCastHandlerPosition) <= threshold) {
                rv?.smoothScrollToPosition(broadCastHandlerPosition)
            }
        }
    }

    private fun onToolbarClicked(): () -> Unit {
        return {
            analytics.trackHeaderClicked(shopId)
            goToDetailOpponent()
        }
    }

    private fun goToDetailOpponent() {
        if (opponentRole.toLowerCase() == ChatRoomHeaderViewModel.Companion.ROLE_USER) {
            goToProfile(opponentId)
        } else if (opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_SHOP)) {
            onGoToShop()
        }
    }

    private fun goToProfile(opponentId: String) {
        RouteManager.route(activity, ApplinkConst.PROFILE.replace("{user_id}", opponentId))
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        getViewState().showErrorWebSocket(isWebSocketError)
    }

    private fun loadChatRoomSettings(chatRoom: ChatroomViewModel) {
        if (chatRoom.canLoadMore) return
        presenter.loadChatRoomSettings(messageId, ::onSuccessLoadChatRoomSetting)
    }

    private fun onSuccessLoadChatRoomSetting(widgets: List<Visitable<TopChatTypeFactory>>) {
        adapter.addWidgetHeader(widgets)
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            hideLoading()
            showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, it))
        }
    }

    private fun onErrorInitiateData(throwable: Throwable) {
        hideLoading()
        showGetListError(throwable)
        showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, throwable))
        fpm.stopTrace()
    }

    override fun getScreenName(): String {
        return TopChatAnalytics.SCREEN_CHAT_ROOM
    }

    override fun getUserSession(): UserSessionInterface {
        return session
    }

    private fun getViewState(): TopChatViewStateImpl {
        return viewState as TopChatViewStateImpl
    }

    override fun onImageAnnouncementClicked(viewModel: ImageAnnouncementViewModel) {
        analytics.trackClickImageAnnouncement(viewModel.blastId.toString(), viewModel.attachmentId)
        super.onImageAnnouncementClicked(viewModel)
    }

    override fun onRetrySendImage(element: ImageUploadViewModel) {
        val bottomSheetBuilder = CheckedBottomSheetBuilder(activity).setMode(BottomSheetBuilder.MODE_LIST)
        bottomSheetBuilder.addItem(InboxMessageConstant.RESEND, R.string.resend, null)
        bottomSheetBuilder.addItem(InboxMessageConstant.DELETE, R.string.delete, null)
        bottomSheetBuilder.expandOnStart(true).setItemClickListener {
            when (it.itemId) {
                InboxMessageConstant.RESEND -> {
                    presenter.startUploadImages(element)
                    removeDummy(element)
                }
                InboxMessageConstant.DELETE -> {
                    removeDummy(element)
                }
            }
        }.createDialog().show()
    }

    override fun onProductClicked(element: ProductAttachmentViewModel) {
        super.onProductClicked(element)
        context?.let {
            analytics.eventClickProductThumbnailEE(it, element, session)
        }

        analytics.trackProductAttachmentClicked()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        super.onReceiveMessageEvent(visitable)
        getViewState().scrollDownWhenInBottom()
        isMoveItemInboxToTop = true
    }

    override fun loadData(page: Int) {}

    override fun onImageUploadClicked(imageUrl: String, replyTime: String) {
        analytics.trackClickImageUpload()
        activity?.let {
            val strings: ArrayList<String> = ArrayList()
            strings.add(imageUrl)

            it.startActivity(ImagePreviewActivity.getCallingIntent(it,
                    strings,
                    null, 0))
        }

    }

    private fun onAttachProductClicked() {
        val intent = TopChatInternalRouter.Companion.getAttachProductIntent(activity as Activity,
                shopId.toString(),
                "",
                getUserSession().shopId == shopId.toString())
        startActivityForResult(intent, TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE)
    }

    override fun clearEditText() {
        getViewState().clearEditText()
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return TopChatTypeFactoryImpl(
                this, this, this, this,
                this, this, this, this,
                this, this, this, this
        )
    }

    override fun renderOrderProgress(chatOrder: ChatOrderProgress) {
        orderProgress?.render(this, chatOrder)
    }

    override fun getChatMenuView(): ChatMenuView? {
        return getViewState().chatMenu
    }

    override fun updateAttachmentsView(attachments: ArrayMap<String, Attachment>) {
        val firstVisible = getFirstVisibleItemPosition() ?: return
        val lastVisible = getLastVisibleItemPosition() ?: return
        adapter.updateAttachmentView(firstVisible, lastVisible, attachments)
    }

    override fun showUnreadMessage(newUnreadMessage: Int) {
        tvTotalUnreadMessage?.text = newUnreadMessage.toString()
        fbNewUnreadMessage?.setOnClickListener {
            resetItemList()
            presenter.getExistingChat(
                    messageId, ::onErrorResetChatToFirstPage, ::onSuccessResetChatToFirstPage
            )
            onViewReachBottomMostChat()
        }
        if (fbNewUnreadMessage?.isVisible == false) {
            fbNewUnreadMessage?.visibility = View.VISIBLE
        }
    }

    private fun onViewReachBottomMostChat() {
        presenter.resetUnreadMessage()
        presenter.readMessage()
        hideUnreadMessage()
    }

    override fun hideUnreadMessage() {
        if (fbNewUnreadMessage?.isVisible == true) {
            fbNewUnreadMessage?.visibility = View.GONE
        }
    }

    override fun removeBroadcastHandler() {
        adapter.removeBroadcastHandler()
    }

    private fun getFirstVisibleItemPosition(): Int? {
        var firstVisible = rvLayoutManager?.findFirstVisibleItemPosition() ?: return null
        val partialVisible = firstVisible - 1
        if (adapter.dataExistAt(partialVisible)) {
            firstVisible = partialVisible
        }
        return firstVisible
    }

    private fun getLastVisibleItemPosition(): Int? {
        var lastVisible = rvLayoutManager?.findLastVisibleItemPosition() ?: return null
        val partialVisible = lastVisible + 1
        if (adapter.dataExistAt(partialVisible)) {
            lastVisible = partialVisible
        }
        return lastVisible
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        if (adapterTypeFactory !is TopChatTypeFactoryImpl) {
            throw IllegalStateException("getAdapterTypeFactory() must return TopChatTypeFactoryImpl")
        }
        val typeFactory = adapterTypeFactory as TopChatTypeFactoryImpl
        return TopChatRoomAdapter(context, typeFactory).also {
            adapter = it
        }
    }

    override fun addDummyMessage(visitable: Visitable<*>) {
        getViewState().addMessage(visitable)
        getViewState().scrollDownWhenInBottom()
    }

    override fun removeDummy(visitable: Visitable<*>) {
        getViewState().removeDummy(visitable)
    }

    override fun onErrorUploadImage(errorMessage: String, it: ImageUploadViewModel) {
        showSnackbarError(errorMessage)
        getViewState().showRetryUploadImages(it, true)
    }

    override fun prepareListener() {
        view?.findViewById<View>(R.id.send_but)?.setOnClickListener {
            onSendButtonClicked()
        }
    }

    override fun onSendButtonClicked() {
        if (presenter.isInTheMiddleOfThePage() && isValidComposedMessage()) {
            resetItemList()
            delaySendMessage()
            presenter.getExistingChat(
                    messageId, ::onErrorResetChatToFirstPage, ::onSuccessResetChatToFirstPage
            )
        } else {
            sendMessage()
            removeBroadcastHandler()
        }
    }

    private fun resetItemList() {
        rvScrollListener?.reset()
        adapter.reset()
        presenter.resetChatUseCase()
        showLoading()
        getViewState().hideProductPreviewLayout()
        getViewState().scrollToBottom()
    }


    private fun isValidComposedMessage(): Boolean {
        val message = getComposedMessage()
        return presenter.isValidReply(message)
    }

    private fun onErrorResetChatToFirstPage(throwable: Throwable) {
        hideLoading()
        showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, throwable))
        clearAttachmentPreviews()
        delaySendMessage = ""
    }

    private fun getComposedMessage(message: String? = null): String {
        return if (message != null && message.isNotEmpty()) {
            message
        } else {
            composeArea?.text?.toString() ?: ""
        }
    }

    private fun sendMessage(message: String? = null) {
        val sendMessage: String = getComposedMessage(message)
        val startTime = SendableViewModel.generateStartTime()
        presenter.sendAttachmentsAndMessage(
                messageId, sendMessage, startTime, opponentId, onSendingMessage()
        )
    }

    private fun delaySendMessage() {
        delaySendMessage = getComposedMessage()
        clearEditText()
    }

    private fun onSuccessResetChatToFirstPage(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        if (delaySendMessage.isNotEmpty()) {
            sendMessage(delaySendMessage)
        }
        if (delaySendSticker != null) {
            sendSticker(delaySendSticker)
        }
        setupFirstPage(chatRoom, chat)
        delaySendMessage = ""
        delaySendSticker = null
    }

    override fun onClickSticker(sticker: Sticker) {
        if (presenter.isInTheMiddleOfThePage()) {
            resetItemList()
            delaySendSticker(sticker)
            presenter.getExistingChat(
                    messageId, ::onErrorResetChatToFirstPage, ::onSuccessResetChatToFirstPage
            )
        } else {
            sendSticker(sticker)
        }
    }

    private fun delaySendSticker(sticker: Sticker) {
        delaySendSticker = sticker
    }

    private fun sendSticker(sticker: Sticker?) {
        if (sticker == null) return
        removeBroadcastHandler()
        val startTime = SendableViewModel.generateStartTime()
        presenter.sendAttachmentsAndSticker(
                messageId,
                sticker,
                startTime,
                opponentId,
                onSendingMessage()
        )
    }

    private fun onSendingMessage(): () -> Unit {
        return {
            analytics.eventSendMessage()
            getViewState().scrollToBottom()
            clearEditText()
        }
    }

    override fun getStringResource(id: Int): String {
        activity?.let {
            return it.getString(id)
        }
        return ""
    }

    override fun showSnackbarError(stringResource: String) {
        view?.let {
            Toaster.make(it, stringResource, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun onStartTyping() {
        presenter.startTyping()
    }

    override fun onStopTyping() {
        presenter.stopTyping()
    }

    override fun addTemplateString(message: String?) {
        message?.let {
            getViewState().addTemplateString(message)
        }
    }

    override fun onSuccessGetTemplate(list: List<Visitable<Any>>) {
        getViewState().setTemplate(list)
    }

    override fun onErrorGetTemplate() {
        getViewState().setTemplate(null)
    }

    override fun pickImageToUpload() {
        activity?.let {
            val builder = ImagePickerBuilder(
                    it.getString(com.tokopedia.imagepicker.R.string.choose_image),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA),
                    GalleryType.IMAGE_ONLY,
                    ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                    null,
                    true,
                    null,
                    ImagePickerMultipleSelectionBuilder(null, null, 0, 1)
            )
            val intent = ImagePickerActivity.getIntent(context, builder)
            startActivityForResult(intent, TopChatRoomActivity.REQUEST_CODE_CHAT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GO_TO_SETTING_TEMPLATE -> {
                presenter.getTemplate(getUserSession().shopId == shopId.toString())
            }
            TopChatRoomActivity.REQUEST_CODE_CHAT_IMAGE -> {
                if (resultCode != RESULT_OK || data == null) {
                    return
                }
                processImagePathToUpload(data)?.let { model ->
                    remoteConfig?.getBoolean(RemoteConfigKey.TOPCHAT_COMPRESS).let {
                        if (it == null || it == false) {
                            presenter.startUploadImages(model)
                        } else {
                            presenter.startCompressImages(model)
                        }
                    }
                }
            }
            TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE -> onProductAttachmentSelected(data)
            REQUEST_GO_TO_SHOP -> onReturnFromShopPage(resultCode, data)
            REQUEST_GO_TO_SETTING_CHAT -> onReturnFromChatSetting(resultCode, data)
            REQUEST_GO_TO_NORMAL_CHECKOUT -> onReturnFromNormalCheckout(resultCode, data)
            REQUEST_ATTACH_INVOICE -> onAttachInvoiceSelected(data, resultCode)
            REQUEST_ATTACH_VOUCHER -> onAttachVoucherSelected(data, resultCode)
            REQUEST_REPORT_USER -> onReturnFromReportUser(data, resultCode)
        }
    }

    private fun onReturnFromReportUser(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        val result = data.getStringExtra(TopChatInternalRouter.Companion.RESULT_KEY_REPORT_USER)
        val payload = data.getStringExtra(TopChatInternalRouter.Companion.RESULT_KEY_PAYLOAD_REPORT_USER)
        if (result == TopChatInternalRouter.Companion.RESULT_REPORT_BLOCK_PROMO) {
            onClickBlockPromo()
        } else if (result == TopChatInternalRouter.Companion.RESULT_REPORT_TOASTER && payload != null) {
            showToasterConfirmation(payload)
        }
    }

    private fun onAttachInvoiceSelected(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        presenter.initInvoicePreview(data.extras)
        presenter.initAttachmentPreview()
    }

    private fun onAttachVoucherSelected(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        presenter.initVoucherPreview(data.extras)
        presenter.initAttachmentPreview()
    }

    private fun onReturnFromNormalCheckout(resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        if (data == null) return
        val message = data.getStringExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)
                ?: return
        view?.let {
            Toaster.showNormalWithAction(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    getString(R.string.chat_check_cart),
                    onClickSeeButtonOnAtcSuccessToaster()
            )
        }
    }

    private fun onClickSeeButtonOnAtcSuccessToaster(): View.OnClickListener {
        return View.OnClickListener {
            analytics.eventClickSeeButtonOnAtcSuccessToaster()
            RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
        }
    }

    private fun onReturnFromChatSetting(resultCode: Int, data: Intent?) {
        data?.let {
            val blockedStatus = BlockedStatus(
                    it.getBooleanExtra(TopChatInternalRouter.Companion.RESULT_CHAT_SETTING_IS_BLOCKED, false),
                    it.getBooleanExtra(TopChatInternalRouter.Companion.RESULT_CHAT_SETTING_IS_PROMO_BLOCKED, false),
                    it.getStringExtra(TopChatInternalRouter.Companion.RESULT_CHAT_SETTING_BLOCKED_UNTIL)
            )

            if (resultCode == RESULT_OK) {
                getViewState().onCheckChatBlocked(opponentRole, opponentName,
                        blockedStatus, onUnblockChatClicked())
            }

        }

    }

    private fun onReturnFromShopPage(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            getViewState().isShopFollowed = data.getBooleanExtra(EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP, false)
        }
    }

    private fun onProductAttachmentSelected(data: Intent?) {
        if (data == null)
            return

        if (!data.hasExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY))
            return

        val resultProducts: ArrayList<ResultProduct> = data.getParcelableArrayListExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
        presenter.initProductPreviewFromAttachProduct(resultProducts)
    }

    private fun processImagePathToUpload(data: Intent): ImageUploadViewModel? {

        val imagePathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
        if (imagePathList == null || imagePathList.size <= 0) {
            return null
        }
        val imagePath = imagePathList[0]

        if (!TextUtils.isEmpty(imagePath)) {
            return generateChatViewModelWithImage(imagePath)
        }
        return null
    }

    private fun generateChatViewModelWithImage(imageUrl: String): ImageUploadViewModel {
        return ImageUploadViewModel(
                messageId,
                opponentId,
                (System.currentTimeMillis() / 1000).toString(),
                imageUrl,
                SendableViewModel.generateStartTime()
        )
    }

    private fun showDialogConfirmToAbortUpload() {
        context?.run {
            topChatRoomDialog.createAbortUploadImage(
                    this, alertDialog,
                    View.OnClickListener {
                        finishActivity()
                    }
            ).show()
        }
    }

    override fun onClickBuyFromProductAttachment(element: ProductAttachmentViewModel) {
        analytics.eventClickBuyProductAttachment(element)
        val buyPageIntent = presenter.getBuyPageIntent(context, element, sourcePage)
        startActivity(buyPageIntent)
    }

    override fun onClickATCFromProductAttachment(element: ProductAttachmentViewModel) {
        analytics.eventClickAddToCartProductAttachment(element, session)
        val atcPageIntent = presenter.getAtcPageIntent(context, element, sourcePage)
        startActivityForResult(atcPageIntent, REQUEST_GO_TO_NORMAL_CHECKOUT)
    }

    override fun onGoToShop() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP.replace("{shop_id}", shopId
                .toString()))
        startActivityForResult(intent, REQUEST_GO_TO_SHOP)
    }

    override fun followUnfollowShop(actionFollow: Boolean) {
        analytics.eventFollowUnfollowShop(actionFollow, shopId.toString())
        presenter.followUnfollowShop(shopId.toString(), onErrorFollowUnfollowShop(), onSuccessFollowUnfollowShop())
    }

    private fun onErrorFollowUnfollowShop(): (Throwable) -> Unit {
        return {
            showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, it))
        }
    }

    private fun onSuccessFollowUnfollowShop(): (Boolean) -> Unit {
        return {
            if (it) {
                val isFollow = !getViewState().isShopFollowed
                if (isFollow) {
                    onSuccessFollowShopFromBcHandler()
                    adapter.removeBroadcastHandler()
                } else {
                    onSuccessUnFollowShopFromBcHandler()
                    addBroadCastSpamHandler(isFollow)
                }
            }
        }
    }

    override fun onDeleteConversation() {
        showLoading()
        presenter.deleteChat(messageId, onError(), onSuccessDeleteConversation())
    }

    private fun onSuccessDeleteConversation(): () -> Unit {
        return {
            hideLoading()
            activity?.let {
                val intent = Intent()
                val bundle = Bundle()
                bundle.putString(ApplinkConst.Chat.MESSAGE_ID, messageId)
                bundle.putInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, indexFromInbox)
                intent.putExtras(bundle)
                it.setResult(TopChatInternalRouter.Companion.CHAT_DELETED_RESULT_CODE, intent)
                it.finish()
            }
        }
    }

    override fun onGoToChatSetting(blockedStatus: BlockedStatus) {
        (activity as Activity).let {
            val intent = TopChatInternalRouter.Companion.getChatSettingIntent(it,
                    messageId,
                    opponentRole,
                    opponentName,
                    blockedStatus.isBlocked,
                    getViewState().isPromoBlocked,
                    blockedStatus.blockedUntil,
                    shopId)
            startActivityForResult(intent, REQUEST_GO_TO_SETTING_CHAT)
        }

        analytics.trackOpenChatSetting()

    }

    override fun onGoToReportUser() {
        context?.let {
            analytics.eventClickReportUser(opponentId)
            val reportUrl = getChatReportUrl()
            val intent = TopchatReportWebViewActivity.getStartIntent(it, reportUrl)
            startActivityForResult(intent, REQUEST_REPORT_USER)
        }
    }

    override fun onClickBlockPromo() {
        val broadCastHandler = adapter.findBroadcastHandler()
        requestBlockPromo(broadCastHandler)
    }

    override fun onClickAllowPromo() {
        requestAllowPromo()
    }

    private fun getChatReportUrl() = "${TkpdBaseURL.CHAT_REPORT_URL}$messageId"

    override fun onDualAnnouncementClicked(redirectUrl: String, attachmentId: String, blastId: Int) {
        analytics.trackClickImageAnnouncement(blastId.toString(), attachmentId)
        if (redirectUrl.isNotEmpty()) {
            onGoToWebView(redirectUrl, attachmentId)
        }
    }

    override fun onVoucherCopyClicked(voucherCode: String, messageId: String, replyId: String, blastId: String, attachmentId: String, replyTime: String?, fromUid: String?) {
        analytics.eventVoucherCopyClicked(voucherCode)
        presenter.copyVoucherCode(fromUid, replyId, blastId, attachmentId, replyTime)
        activity?.run {
            val snackbar = Snackbar.make(findViewById(android.R.id.content), getString(com.tokopedia.merchantvoucher.R.string.title_voucher_code_copied),
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(activity!!.getString(com.tokopedia.merchantvoucher.R.string.close), { snackbar.dismiss() })
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    override fun onVoucherClicked(data: MerchantVoucherViewModel) {
        analytics.eventVoucherThumbnailClicked()
        activity?.let {
            val intent = MerchantVoucherDetailActivity.createIntent(it, data.voucherId,
                    data, shopId.toString())
            startActivityForResult(intent, MerchantVoucherListFragment.REQUEST_CODE_MERCHANT_DETAIL)
        }
    }

    override fun onBackPressed(): Boolean {
        if (super.onBackPressed()) return true
        if (presenter.isUploading()) {
            showDialogConfirmToAbortUpload()
        } else {
            finishActivity()
        }
        return true
    }

    private fun finishActivity() {
        activity?.let {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putBoolean(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP, isMoveItemInboxToTop)
            bundle.putParcelable(TopChatInternalRouter.Companion.RESULT_LAST_ITEM, getViewState().getLastItem())
            bundle.putString(ApplinkConst.Chat.MESSAGE_ID, messageId)
            bundle.putInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, indexFromInbox)
            intent.putExtras(bundle)
            it.setResult(RESULT_OK, intent)
            it.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        toolTip.dismiss()
    }

    override fun trackSeenProduct(element: ProductAttachmentViewModel) {
        if (seenAttachedProduct.add(element.productId)) {
            analytics.eventSeenProductAttachment(requireContext(), element, session)

            // this for experimentation of DATA
            if (remoteConfig?.getBoolean(RemoteConfigKey.CHAT_EVER_SEEN_PRODUCT, false) ?: false) {
                analytics.eventSeenProductAttachmentBeta(requireContext(), element, session)
            }
        }
    }

    override fun trackSeenBannedProduct(viewModel: BannedProductAttachmentViewModel) {
        if (seenAttachedBannedProduct.add(viewModel.productId)) {
            analytics.eventSeenBannedProductAttachment(viewModel)
        }
    }

    override fun onClickInvoiceThumbnail(url: String, id: String) {
        onGoToWebView(url, id)
    }

    override fun trackClickInvoice(viewModel: AttachInvoiceSentViewModel) {
        analytics.trackClickInvoice(viewModel)
    }

    override fun getStringArgument(key: String, savedInstanceState: Bundle?): String {
        return getParamString(key, arguments, savedInstanceState)
    }

    override fun getBooleanArgument(key: String, savedInstanceState: Bundle?): Boolean {
        return getParamBoolean(key, arguments, savedInstanceState, false)
    }

    override fun focusOnReply() {
        getViewState().focusOnReply()
    }

    override fun showAttachmentPreview(attachmentPreview: ArrayList<SendablePreview>) {
        getViewState().showAttachmentPreview(attachmentPreview)
    }

    override fun onEmptyProductPreview() {
        presenter.clearAttachmentPreview()
    }

    override fun clearAttachmentPreviews() {
        getViewState().clearAttachmentPreview()
    }

    override fun getShopName(): String {
        return opponentName
    }

    override fun sendAnalyticAttachmentSent(attachment: SendablePreview) {
        if (attachment is InvoicePreviewUiModel) {
            analytics.invoiceAttachmentSent(attachment)
        } else if (attachment is SendableProductPreview) {
            analytics.trackSendProductAttachment()
        }
    }

    override fun createAttachmentMenus(): List<AttachmentMenu> {
        return listOf(
                ProductMenu(),
                ImageMenu(),
                InvoiceMenu()
        )
    }

    override fun onClickAttachProduct(menu: AttachmentMenu) {
        analytics.eventAttachProduct()
        analytics.trackChatMenuClicked(menu.label)
        onAttachProductClicked()
    }

    override fun onClickAttachImage(menu: AttachmentMenu) {
        analytics.eventPickImage()
        analytics.trackChatMenuClicked(menu.label)
        pickImageToUpload()
    }

    override fun onClickAttachVoucher(voucherMenu: VoucherMenu) {
        analytics.trackChatMenuClicked(voucherMenu.label)
        pickVoucherToUpload()
    }

    override fun onClickAttachInvoice(menu: AttachmentMenu) {
        analytics.trackChatMenuClicked(menu.label)
        pickInvoiceToUpload()
    }

    private fun pickVoucherToUpload() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATTACH_VOUCHER)
        startActivityForResult(intent, REQUEST_ATTACH_VOUCHER)
    }

    private fun pickInvoiceToUpload() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATTACH_INVOICE).apply {
            putExtra(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, messageId)
            putExtra(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME, opponentName)
        }
        startActivityForResult(intent, REQUEST_ATTACH_INVOICE)
    }

    override fun onClickBannedProduct(viewModel: BannedProductAttachmentViewModel) {
        analytics.eventClickBannedProduct(viewModel)
        presenter.onClickBannedProduct(viewModel.liteUrl)
    }

    override fun redirectToBrowser(url: String) {
        if (url.isEmpty()) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun getSupportChildFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun onClickAddToWishList(product: ProductAttachmentViewModel, success: () -> Unit) {
        val productId = product.productId.toString()
        analytics.eventClickAddToWishList(productId)
        if (product.isWishListed()) {
            showSuccessToastWishListCta(R.string.title_topchat_already_success_atw)
        } else {
            requestNetworkAddToWishList(productId, success)
        }
    }

    private fun requestNetworkAddToWishList(productId: String, success: () -> Unit) {
        presenter.addToWishList(productId, session.userId, object : WishListActionListener {
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
            override fun onSuccessAddWishlist(productId: String?) {
                success()
                showSuccessToastWishListCta(R.string.title_topchat_success_atw)
            }

            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                if (errorMessage == null) return
                view?.let {
                    Toaster.make(it, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun showSuccessToastWishListCta(@StringRes successMessageRes: Int) {
        view?.let {
            val successMessage = it.context.getString(successMessageRes)
            val ctaText = it.context.getString(R.string.cta_topchat_success_atw)
            Toaster.make(
                    it,
                    successMessage,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    ctaText,
                    View.OnClickListener { goToWishList() }
            )
        }
    }

    private fun goToWishList() {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    override fun onClickRemoveFromWishList(productId: String, success: () -> Unit) {
        analytics.eventClickRemoveFromWishList(productId)
        presenter.removeFromWishList(productId, session.userId, object : WishListActionListener {
            override fun onSuccessAddWishlist(productId: String?) {}
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {
                success()
                view?.let {
                    val successMessage = it.context.getString(R.string.title_topchat_success_rfw)
                    Toaster.make(
                            it,
                            successMessage,
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_NORMAL
                    )
                }
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                if (errorMessage == null) return
                view?.let {
                    Toaster.make(
                            it,
                            errorMessage,
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                    )
                }
            }
        })
    }

    override fun trackClickQuotation(msg: QuotationUiModel) {
        analytics.eventClickQuotation(msg)
    }

    override fun trackClickProductThumbnail(product: ProductAttachmentViewModel) {
        analytics.eventClickProductThumbnail(product)
    }

    companion object {
        fun createInstance(bundle: Bundle): BaseChatFragment {
            return TopChatRoomFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onStickerOpened() {
        getViewState().onStickerOpened()
        toolTip.dismissOnBoarding()
    }

    override fun onStickerClosed() {
        getViewState().onStickerClosed()
    }

    override fun getFragmentActivity(): FragmentActivity? {
        return activity
    }

    override fun getLoadedChatAttachments(): ArrayMap<String, Attachment> {
        return presenter.attachments
    }

    override fun isSeller(): Boolean {
        return amISeller
    }

    override fun getSearchQuery(): String {
        return searchQuery
    }

    override fun requestFollowShop(element: BroadcastSpamHandlerUiModel) {
        presenter.requestFollowShop(shopId, {
            element.stopFollowShop()
            onSuccessFollowShopFromBcHandler()
            adapter.removeBroadcastHandler(element)
        }, {
            element.stopFollowShop()
            onErrorFollowShopFromBcHandler(it)
            adapter.updateBroadcastHandlerState(element)
        })
    }

    override fun requestBlockPromo(element: BroadcastSpamHandlerUiModel?) {
        presenter.requestBlockPromo(messageId, { until ->
            getViewState().isPromoBlocked = true
            element?.stopBlockPromo()
            onSuccessBlockPromoFromBcHandler(until)
            element?.let {
                adapter.removeBroadcastHandler(it)
            }
        }, {
            element?.stopBlockPromo()
            onErrorBlockPromoFromBcHandler(it)
            element?.let {
                adapter.updateBroadcastHandlerState(it)
            }
        })
    }

    private fun requestAllowPromo() {
        presenter.requestAllowPromo(messageId, {
            getViewState().isPromoBlocked = false
            addBroadCastSpamHandler(getViewState().isShopFollowed)
            onSuccessAllowPromoFromBcHandler()
        }, {
            onErrorAllowPromoFromBcHandler(it)
        })
    }

    private fun onSuccessAllowPromoFromBcHandler() {
        context?.let {
            showToasterConfirmation(it.getString(R.string.title_success_allow_promo))
        }
    }

    private fun onErrorAllowPromoFromBcHandler(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToasterError(errorMessage)
    }

    private fun onSuccessBlockPromoFromBcHandler(until: String) {
        context?.let {
            showToasterConfirmation(it.getString(R.string.title_success_block_promo, until))
        }
    }

    private fun onErrorBlockPromoFromBcHandler(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToasterError(errorMessage)
    }

    private fun onSuccessFollowShopFromBcHandler() {
        getViewState().isShopFollowed = true
        context?.let {
            showToasterConfirmation(it.getString(R.string.title_success_follow_shop))
        }
    }

    private fun onSuccessUnFollowShopFromBcHandler() {
        getViewState().isShopFollowed = false
        context?.let {
            showToasterConfirmation(it.getString(R.string.title_success_unfollow_shop))
        }
    }

    private fun onErrorFollowShopFromBcHandler(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToasterError(errorMessage)
    }

    private fun showToasterConfirmation(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, "Oke")
                    .show()
        }
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }
}
