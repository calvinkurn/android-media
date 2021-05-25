package com.tokopedia.topchat.chatroom.view.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams.Companion.ATC_FROM_TOPCHAT
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.*
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATE_MESSAGE
import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel
import com.tokopedia.topchat.chatroom.data.activityresult.ReviewRequestResult
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.service.UploadImageBroadcastListener
import com.tokopedia.topchat.chatroom.service.UploadImageBroadcastReceiver
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity.Companion.REQUEST_CODE_CHAT_IMAGE
import com.tokopedia.topchat.chatroom.view.activity.TopchatReportWebViewActivity
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactoryImpl
import com.tokopedia.topchat.chatroom.view.adapter.util.LoadMoreTopBottomScrollListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwQuestionViewHolder
import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder
import com.tokopedia.topchat.chatroom.view.custom.*
import com.tokopedia.topchat.chatroom.view.customview.TopChatRoomDialog
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewStateImpl
import com.tokopedia.topchat.chatroom.view.listener.*
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.*
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import com.tokopedia.topchat.common.util.TopChatSellerReviewHelper
import com.tokopedia.topchat.common.util.Utils
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs

/**
 * @author : Steven 29/11/18
 */

open class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View, TypingListener,
        SendButtonListener, ImagePickerListener, ChatTemplateListener,
        HeaderMenuListener, DualAnnouncementListener, TopChatVoucherListener,
        InvoiceThumbnailListener, QuotationViewHolder.QuotationListener,
        TransactionOrderProgressLayout.Listener, ChatMenuStickerView.StickerMenuListener,
        StickerViewHolder.Listener, DeferredViewHolderAttachment, CommonViewHolderListener,
        SearchListener, BroadcastSpamHandlerViewHolder.Listener,
        RoomSettingFraudAlertViewHolder.Listener, ReviewViewHolder.Listener,
        TopchatProductAttachmentListener, UploadImageBroadcastListener,
        SrwQuestionViewHolder.Listener, SrwLinearLayout.Listener, ReplyBoxTextListener {

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

    @Inject
    lateinit var sellerReviewHelper: TopChatSellerReviewHelper

    private lateinit var fpm: PerformanceMonitoring
    private lateinit var customMessage: String
    private lateinit var adapter: TopChatRoomAdapter
    private var indexFromInbox = -1
    private var isMoveItemInboxToTop = false
    private var remoteConfig: RemoteConfig? = null
    private var sourcePage: String = ""
    private var createTime: String = ""
    private var searchQuery: String = ""
    private var delaySendMessage: String = ""
    private var delaySendSticker: Sticker? = null
    private var delaySendSrw: QuestionUiModel? = null

    //This used only for set extra in finish activity
    private var isFavoriteShop: Boolean? = null

    private val REQUEST_GO_TO_SHOP = 111
    private val TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 112
    private val REQUEST_GO_TO_SETTING_TEMPLATE = 113
    private val REQUEST_ATTACH_INVOICE = 116
    private val REQUEST_ATTACH_VOUCHER = 117
    private val REQUEST_REPORT_USER = 118
    private val REQUEST_REVIEW = 119
    private val REQUEST_UPDATE_STOCK = 120

    private var seenAttachedProduct = HashSet<String>()
    private var seenAttachedBannedProduct = HashSet<String>()
    private val reviewRequest = Stack<ReviewRequestResult>()
    private var composeArea: EditText? = null
    private var orderProgress: TransactionOrderProgressLayout? = null
    private var chatMenu: ChatMenuView? = null
    private var rvLayoutManager: LinearLayoutManager? = null
    private var rvScrollListener: LoadMoreTopBottomScrollListener? = null
    private var fbNewUnreadMessage: FloatingButtonUnify? = null
    private var tvTotalUnreadMessage: Typography? = null
    private var rv: RecyclerView? = null
    private var rvSrw: SrwLinearLayout? = null
    private var rvContainer: CoordinatorLayout? = null
    private var chatBackground: ImageView? = null
    private var sendButton: IconUnify? = null
    private var textWatcher: MessageTextWatcher? = null
    private var sendButtontextWatcher: SendButtonTextWatcher? = null
    protected var topchatViewState: TopChatViewStateImpl? = null
    private var uploadImageBroadcastReceiver: BroadcastReceiver? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view
    override fun getAnalytic(): TopChatAnalytics = analytics
    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        initFireBase()
        registerUploadImageReceiver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topchat_chatroom, container, false).also {
            bindView(it)
            initSrw()
            initUserLocation()
            initObserver()
            initReplyTextWatcher()
            initStickerView()
            initFbNewUnreadMessage()
            initTextComposeBackground()
        }
    }

    private fun initSrw() {
        rvSrw?.initialize(this, this)
    }

    private fun initUserLocation() {
        context?.let {
            val userLocation = ChooseAddressUtils.getLocalizingAddressData(it) ?: return@let
            presenter.initUserLocation(userLocation)
        }
    }

    private fun initObserver() {
        presenter.srw.observe(viewLifecycleOwner, Observer {
            rvSrw?.updateStatus(it)
            updateSrwState()
        })
    }

    override fun updateSrwState() {
        if (shouldShowSrw()) {
            rvSrw?.renderSrwState()
            getViewState().hideTemplateChat()
        } else {
            rvSrw?.hideSrw()
            getViewState().showTemplateChatIfReady(
                    adapter.isLastMessageBroadcast(), !isSeller()
            )
        }
    }

    override fun hasProductPreviewShown(): Boolean {
        return getViewState().hasProductPreviewShown()
    }

    override fun hasNoSrw(): Boolean {
        return rvSrw?.isAllowToShow() == false && rvSrw?.isSuccessState() == true
    }

    override fun collapseSrw() {
        rvSrw?.isExpanded = false
    }

    override fun expandSrw() {
        rvSrw?.isExpanded = true
    }

    override fun shouldShowSrw(): Boolean {
        return !isSeller() && hasProductPreviewShown() &&
                rvSrw?.isAllowToShow() == true ||
                (rvSrw?.isErrorState() == true && hasProductPreviewShown()) ||
                (rvSrw?.isLoadingState() == true && hasProductPreviewShown())
    }

    private fun initReplyTextWatcher() {
        textWatcher = MessageTextWatcher(this)
        composeArea?.addTextChangedListener(textWatcher)
        sendButtontextWatcher = SendButtonTextWatcher(this)
        composeArea?.addTextChangedListener(sendButtontextWatcher)
    }

    private fun initTextComposeBackground() {
        val bgComposeArea = ViewUtil.generateBackgroundWithShadow(
                composeArea,
                com.tokopedia.unifyprinciples.R.color.Unify_N0,
                R.dimen.dp_topchat_20,
                R.dimen.dp_topchat_20,
                R.dimen.dp_topchat_20,
                R.dimen.dp_topchat_20,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
                R.dimen.dp_topchat_2,
                R.dimen.dp_topchat_1,
                Gravity.CENTER
        )
        val paddingStart = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7).toInt()
        val paddingEnd = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8).toInt()
        val paddingTop = resources.getDimension(R.dimen.dp_topchat_11).toInt()
        val paddingBottom = resources.getDimension(R.dimen.dp_topchat_10).toInt()
        composeArea?.background = bgComposeArea
        composeArea?.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
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
        rvSrw = view?.findViewById(R.id.rv_srw)
        rvContainer = view?.findViewById(R.id.rv_container)
        fbNewUnreadMessage = view?.findViewById(R.id.fb_new_unread_message)
        chatBackground = view?.findViewById(R.id.iv_bg_chat)
        sendButton = view?.findViewById(R.id.send_but)
    }

    private fun initStickerView() {
        chatMenu?.setStickerListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackground()
        setupPresenter(savedInstanceState)
        setupArguments(savedInstanceState)
        setupAttachmentsPreview(savedInstanceState)
        hideLoading()
        setupAnalytic()
        setupBeforeReplyTime()
        loadInitialData()
        initLoadMoreListener()
        onReplyBoxEmpty()
    }

    private fun setupBackground() {
        presenter.getBackground()
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
        presenter.loadAttachmentData(messageId.toLongOrZero(), chatRoom)
    }

    private fun onErrorGetTopChat(throwable: Throwable) {
        hideTopLoading()
        view?.let {
            showSnackbarError(ErrorHandler.getErrorMessage(it.context, throwable))
        }
        rvScrollListener?.finishTopLoadingState()
    }


    private fun onSuccessGetBottomChat(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        rvScrollListener?.finishBottomLoadingState()
        adapter.removeLatestHeaderDateIfSame(chatRoom.listChat)
        adapter.setLatestHeaderDate(chatRoom.latestHeaderDate)
        updateNewUnreadMessageState(chat)
        renderBottomList(chatRoom.listChat)
        updateHasNextAfterState(chat)
        presenter.loadAttachmentData(messageId.toLongOrZero(), chatRoom)
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

    private fun setupAnalytic() {
        analytics.setSourcePage(sourcePage)
    }

    override fun onCreateViewState(view: View): BaseChatViewState {
        return TopChatViewStateImpl(
                view, this, this, this,
                this, this, this, this,
                (activity as BaseChatToolbarActivity).getToolbar(), analytics
        ).also {
            topchatViewState = it
        }
    }

    override fun initInjector() {
        getComponent(ChatComponent::class.java).inject(this)
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

    override fun updateProductStock(
            product: ProductAttachmentViewModel,
            adapterPosition: Int,
            parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    ) {
        var id = product.parentId
        if(id == "0") {
            id = product.productId
        }
        val intent = RouteManager.getIntent(
                context, ApplinkConstInternalMarketplace.RESERVED_STOCK,
                id, product.shopId.toString()
        )
        presenter.addOngoingUpdateProductStock(id, product, adapterPosition, parentMetaData)
        startActivityForResult(intent, REQUEST_UPDATE_STOCK)
    }

    override fun trackClickUpdateStock(product: ProductAttachmentViewModel) {
        analytics.trackClickUpdateStock(product)
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
        getViewState().attachFragmentView(this)
    }

    private fun setupArguments(savedInstanceState: Bundle?) {
        customMessage = getParamString(ApplinkConst.Chat.CUSTOM_MESSAGE, arguments, savedInstanceState)
        sourcePage = getParamString(ApplinkConst.Chat.SOURCE_PAGE, arguments, savedInstanceState)
        indexFromInbox = getParamInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, arguments, savedInstanceState)
        createTime = getParamString(ApplinkConst.Chat.SEARCH_CREATE_TIME, arguments, savedInstanceState)
        searchQuery = getParamString(ApplinkConst.Chat.SEARCH_PRODUCT_KEYWORD, arguments, savedInstanceState)
    }

    private fun setupAttachmentsPreview(savedInstanceState: Bundle?) {
        initProductPreview(savedInstanceState)
        initInvoicePreview(savedInstanceState)
        presenter.initAttachmentPreview()
    }

    private fun onSuccessGetMessageId(): (String) -> Unit {
        return {
            this.messageId = it
            loadInitialData()
        }
    }

    private fun onSuccessGetExistingChatFirstTime(
            chatRoom: ChatroomViewModel, chat: ChatReplies
    ) {
        setupFirstTimeOnly(chatRoom, chat)
        setupFirstPage(chatRoom, chat)
        setupPostFirstPage()
        fpm.stopTrace()
        setupDummyData()
    }

    private fun setupPostFirstPage() {
        presenter.getShopFollowingStatus(
                shopId, ::onErrorGetShopFollowingStatus, ::onSuccessGetShopFollowingStatus
        )
    }

    private fun setupDummyData() {
        for (dummy in UploadImageChatService.dummyMap) {
            if (dummy.messageId == messageId) {
                dummy.visitable?.let {
                    addDummyMessage(it)
                    if (dummy.isFail) {
                        getViewState().showRetryUploadImages(it as ImageUploadViewModel, true)
                    }
                }
            }
        }
    }

    private fun setupFirstTimeOnly(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        updateViewData(chatRoom)
        checkCanAttachVoucher()
        orderProgress?.renderIfExist()
        getViewState().onSuccessLoadFirstTime(
                chatRoom, onToolbarClicked(), this
        )
        getViewState().onSetCustomMessage(customMessage)
        presenter.getTemplate(chatRoom.isSeller())
        presenter.getStickerGroupList(chatRoom)
        if (!isSeller()) {
            presenter.getSmartReplyWidget(messageId)
        }
    }

    override fun onRetrySrw() {
        presenter.getSmartReplyWidget(messageId)
    }

    override fun trackViewSrw() {
        analytics.eventViewSrw(shopId, session.userId)
    }

    private fun setupFirstPage(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        adapter.setLatestHeaderDate(chatRoom.latestHeaderDate)
        renderList(chatRoom.listChat)
        updateHasNextState(chat)
        updateHasNextAfterState(chat)
        loadChatRoomSettings(chatRoom)
        presenter.loadAttachmentData(messageId.toLongOrZero(), chatRoom)
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
        if (getViewState().blockStatus.isPromoBlocked ||
                isFollow || presenter.isInTheMiddleOfThePage()
        ) return
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
        return { thr ->
            hideLoading()
            view?.let {
                showSnackbarError(ErrorHandler.getErrorMessage(it.context, thr))
            }
        }
    }

    private fun onErrorInitiateData(throwable: Throwable) {
        hideLoading()
        showGetListError(throwable)
        view?.let {
            showSnackbarError(ErrorHandler.getErrorMessage(it.context, throwable))
        }
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
        context?.let {
            val bs = TopchatBottomSheetBuilder.getErrorUploadImageBs(it,
                    onRetryClicked = {
                        removeDummy(element)
                        resendImage(element)
                    },
                    onDeleteClicked = {
                        removeDummy(element)
                    }
            )
            bs.show(childFragmentManager, "Retry Image Upload")
        }
    }

    private fun resendImage(element: ImageUploadViewModel) {
        //change the retry value
        element.isRetry = false
        presenter.startUploadImages(element)
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
                this, this, this, this,
                this, this
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

    override fun onSendAndReceiveMessage() {
        adapter.removeBroadcastHandler()
        updateSrwState()
    }

    override fun renderBackground(url: String) {
        chatBackground?.let {
            Glide.with(it.context)
                    .load(url)
                    .centerInside()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(it)
        }
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

    override fun onSendButtonClicked() {
        if (presenter.isInTheMiddleOfThePage() && isValidComposedMessage()) {
            resetItemList()
            delaySendMessage()
            presenter.getExistingChat(
                    messageId, ::onErrorResetChatToFirstPage, ::onSuccessResetChatToFirstPage
            )
        } else {
            sendMessage()
            onSendAndReceiveMessage()
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
        view?.let {
            showSnackbarError(ErrorHandler.getErrorMessage(it.context, throwable))
        }
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
        textWatcher?.cancelJob()
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
        delaySendSticker?.let {
            sendSticker(it)
        }
        delaySendSrw?.let {
            sendSrwQuestion(it)
        }
        setupFirstPage(chatRoom, chat)
        delaySendMessage = ""
        delaySendSticker = null
        delaySendSrw = null
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
        onSendAndReceiveMessage()
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
            sellerReviewHelper.hasRepliedChat = true
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

    private fun showSnackbarError(throwable: Throwable) {
        view?.let {
            val errorMsg = ErrorHandler.getErrorMessage(it.context, throwable)
            Toaster.make(it, errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
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
        val isLastMessageBroadcast = adapter.isLastMessageBroadcast()
        val amIBuyer = !isSeller()
        getViewState().setTemplate(list, isLastMessageBroadcast, amIBuyer)
    }

    override fun onErrorGetTemplate() {
        getViewState().setTemplate(null)
    }

    override fun pickImageToUpload() {
        activity?.let {
            val builder = ImagePickerBuilder.getOriginalImageBuilder(it)
                    .withSimpleMultipleSelection(maxPick = 1).apply {
                        maxFileSizeInKB = MAX_SIZE_IMAGE_PICKER
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            startActivityForResult(intent, REQUEST_CODE_CHAT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GO_TO_SETTING_TEMPLATE -> onReturnFromSettingTemplate()
            REQUEST_CODE_CHAT_IMAGE -> onReturnFromChooseImage(resultCode, data)
            TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE -> onProductAttachmentSelected(data)
            REQUEST_GO_TO_SHOP -> onReturnFromShopPage(resultCode, data)
            REQUEST_ATTACH_INVOICE -> onAttachInvoiceSelected(data, resultCode)
            REQUEST_ATTACH_VOUCHER -> onAttachVoucherSelected(data, resultCode)
            REQUEST_REPORT_USER -> onReturnFromReportUser(data, resultCode)
            REQUEST_REVIEW -> onReturnFromReview(data, resultCode)
            REQUEST_UPDATE_STOCK -> onReturnFromUpdateStock(data, resultCode)
        }
    }

    private fun onReturnFromUpdateStock(data: Intent?, resultCode: Int) {
        if (resultCode == RESULT_OK && data != null) {
            val productId = data.getStringExtra(ProductManageCommonConstant.EXTRA_PRODUCT_ID)
            var stockCount = data.getIntExtra(
                    ProductManageCommonConstant.EXTRA_UPDATED_STOCK, 0
            )
            var status = data.getStringExtra(
                    ProductManageCommonConstant.EXTRA_UPDATED_STATUS
            ) ?: return
            var productName = data.getStringExtra(ProductManageCommonConstant.EXTRA_PRODUCT_NAME)
            val updateProductResult = presenter.onGoingStockUpdate[productId] ?: return
            val variantResult = getVariantResultUpdateStock(
                    data, updateProductResult.product.productId)
            variantResult?.let {
                stockCount = variantResult.stockCount
                status = variantResult.status.name
                productName = "$productName - ${variantResult.productName}"
            }
            showToasterMsgFromUpdateStock(updateProductResult, productName, status)
            adapter.updateProductStock(updateProductResult, stockCount, status)
            presenter.onGoingStockUpdate.remove(productId)
        } else {
            val errorMsg = data?.extras?.getString(EXTRA_UPDATE_MESSAGE) ?: return
            showToasterError(errorMsg)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getVariantResultUpdateStock(
            data: Intent, productId: String?
    ): UpdateCampaignVariantResult? {
        val resultMap = data.getSerializableExtra(
                ProductManageCommonConstant.EXTRA_UPDATE_VARIANTS_MAP
        )
        val variantMap = resultMap as? HashMap<String, UpdateCampaignVariantResult>
        return variantMap?.get(productId)
    }

    private fun showToasterMsgFromUpdateStock(
            updateProductResult: UpdateProductStockResult,
            productName: String?, currentStatus: String
    ) {
        val previousStatus = if (updateProductResult.product.hasEmptyStock()) {
            ProductStatus.INACTIVE.name
        } else {
            ProductStatus.ACTIVE.name
        }
        val name = productName?.ellipsize(20) ?: return
        var msg = ""
        when {
            // update active product stock
            previousStatus == ProductStatus.ACTIVE.name &&
                    currentStatus == ProductStatus.ACTIVE.name -> {
                msg = context?.getString(R.string.title_success_update_stock, name) ?: ""
            }
            // deactivate
            previousStatus == ProductStatus.ACTIVE.name &&
                    currentStatus == ProductStatus.INACTIVE.name -> {
                msg = context?.getString(
                        R.string.title_success_deactivate_product_status, name
                ) ?: ""
            }
            // activate
            previousStatus == ProductStatus.INACTIVE.name &&
                    currentStatus == ProductStatus.ACTIVE.name -> {
                msg = context?.getString(
                        R.string.title_success_activate_product_status, name
                ) ?: ""
            }
        }
        view?.let {
            if (msg.isNotEmpty()) {
                Toaster.build(
                        it, msg, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL
                ).show()
            }
        }
    }

    private fun onReturnFromReview(data: Intent?, resultCode: Int) {
        val reviewRequestResult = if (!reviewRequest.empty())
            reviewRequest.pop() else null
        reviewRequestResult ?: return
        if (resultCode == RESULT_OK && data != null) {
            val reviewClickAt = data.getIntExtra(
                    ReputationCommonConstants.ARGS_RATING, -1
            )
            val state = data.getIntExtra(
                    ReputationCommonConstants.ARGS_REVIEW_STATE, -1
            )
            adapter.updateReviewState(
                    reviewRequestResult.review, reviewRequestResult.lastKnownPosition,
                    reviewClickAt, state
            )
            if (state == ReputationCommonConstants.REVIEWED) {
                showSnackBarSuccessReview()
            }
        } else {
            adapter.resetReviewState(
                    reviewRequestResult.review, reviewRequestResult.lastKnownPosition
            )
        }
    }

    private fun showSnackBarSuccessReview() {
        view?.let { view ->
            Toaster.toasterCustomCtaWidth = 116.toPx()
            Toaster.build(
                    view,
                    getString(R.string.title_topchat_review_success),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.title_topchat_review_other),
                    View.OnClickListener {
                        RouteManager.route(it.context, ApplinkConst.REPUTATION)
                    }
            ).show()
        }
    }

    private fun onReturnFromChooseImage(resultCode: Int, data: Intent?) {
        viewState.hideAttachmentMenu()
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        processImagePathToUpload(data)?.let { model ->
            remoteConfig?.getBoolean(RemoteConfigKey.TOPCHAT_COMPRESS).let {
                if (it == null || it == false) {
                    onSendAndReceiveMessage()
                    presenter.startUploadImages(model)
                } else {
                    presenter.startCompressImages(model)
                }
                sellerReviewHelper.hasRepliedChat = true
            }
        }
    }

    private fun onReturnFromSettingTemplate() {
        presenter.getTemplate(getUserSession().shopId == shopId.toString())
    }

    private fun onReturnFromReportUser(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        val result = data.getStringExtra(TopChatInternalRouter.Companion.RESULT_KEY_REPORT_USER)
        val payload = data.getStringExtra(TopChatInternalRouter.Companion.RESULT_KEY_PAYLOAD_REPORT_USER)
        if (result == TopChatInternalRouter.Companion.RESULT_REPORT_BLOCK_PROMO) {
            onClickBlockPromo()
        } else if (result == TopChatInternalRouter.Companion.RESULT_REPORT_BLOCK_USER) {
            blockChat()
        } else if (result == TopChatInternalRouter.Companion.RESULT_REPORT_TOASTER && payload != null) {
            showToasterConfirmation(payload)
        }
    }

    private fun onAttachInvoiceSelected(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        initInvoicePreview(data.extras)
        presenter.initAttachmentPreview()
    }

    private fun onAttachVoucherSelected(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        initVoucherPreview(data.extras)
        presenter.initAttachmentPreview()
    }

    private fun onReturnFromShopPage(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            getViewState().isShopFollowed = data.getBooleanExtra(EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP, false)
        }
    }

    private fun onProductAttachmentSelected(data: Intent?) {
        if (data == null || !data.hasExtra(TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)) return
        val resultProducts: ArrayList<ResultProduct>? = data.getParcelableArrayListExtra(
                TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY
        )
        resultProducts?.let {
            presenter.initProductPreviewFromAttachProduct(it)
        }
    }

    private fun processImagePathToUpload(data: Intent): ImageUploadViewModel? {

        val imagePathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
        if (imagePathList.size <= 0) {
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
        context?.let {
            topChatRoomDialog.createAbortUploadImage(it) {
                finishActivity()
            }.show()
        }
    }

    override fun onClickBuyFromProductAttachment(element: ProductAttachmentViewModel) {
        analytics.eventClickBuyProductAttachment(element)
        doBuyAndAtc(element) {
            analytics.trackSuccessDoBuyAndAtc(
                    element, it,
                    topchatViewState?.chatRoomViewModel?.shopName ?: "",
                    element.getBuyEventAction()
            )
            RouteManager.route(context, ApplinkConst.CART)
        }
    }

    override fun onClickATCFromProductAttachment(element: ProductAttachmentViewModel) {
        analytics.eventClickAddToCartProductAttachment(element, session)
        doBuyAndAtc(element) {
            analytics.trackSuccessDoBuyAndAtc(
                    element, it,
                    topchatViewState?.chatRoomViewModel?.shopName ?: "",
                    element.getAtcEventAction()
            )
            val msg = it.message.getOrNull(0) ?: ""
            rvContainer?.let { view ->
                Toaster.build(
                        view,
                        msg,
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        view.context.getString(R.string.title_topchat_see_cart),
                        View.OnClickListener {
                            analytics.eventClickSeeButtonOnAtcSuccessToaster()
                            RouteManager.route(context, ApplinkConst.CART)
                        }
                ).show()
            }
        }
    }

    private fun doBuyAndAtc(
            element: ProductAttachmentViewModel,
            onSuccess: (response: DataModel) -> Unit = {}
    ) {
        val buyParam = getAtcBuyParam(element)
        presenter.addProductToCart(buyParam, {
            onSuccess(it)
        }, { msg ->
            showToasterError(msg)
        })
    }

    private fun getAtcBuyParam(element: ProductAttachmentViewModel): RequestParams {
        val addToCartRequestParams = AddToCartRequestParams(
                productId = element.productId.toLongOrZero(),
                shopId = element.shopId.toInt(),
                quantity = element.minOrder,
                atcFromExternalSource = ATC_FROM_TOPCHAT
        )
        return RequestParams.create().apply {
            putObject(
                    AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST,
                    addToCartRequestParams
            )
        }
    }

    override fun onGoToShop() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP.replace("{shop_id}", shopId
                .toString()))
        startActivityForResult(intent, REQUEST_GO_TO_SHOP)
    }

    override fun followUnfollowShop(actionFollow: Boolean) {
        analytics.eventFollowUnfollowShop(actionFollow, shopId.toString())
        presenter.followUnfollowShop(shopId.toString(), ::onErrorFollowUnfollowShop, onSuccessFollowUnfollowShop())
    }

    private fun onErrorFollowUnfollowShop(throwable: Throwable) {
        context?.let {
            showSnackbarError(ErrorHandler.getErrorMessage(it, throwable))
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
                isFavoriteShop = isFollow
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
                bundle.putInt(
                        TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX,
                        indexFromInbox
                )
                intent.putExtras(bundle)
                it.setResult(TopChatInternalRouter.Companion.CHAT_DELETED_RESULT_CODE, intent)
                it.finish()
            }
        }
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

    override fun onClickBlockChatFraudAlert() {
        getViewState().showConfirmationBlockChat()
    }

    override fun blockChat() {
        presenter.blockChat(messageId, {
            getViewState().setChatBlockStatus(true)
            getViewState().onCheckChatBlocked(opponentRole, opponentName, getViewState().blockStatus)
            context?.let {
                showToasterConfirmation(it.getString(R.string.title_success_block_chat))
            }
        }, {
            val errorMessage = ErrorHandler.getErrorMessage(context, it)
            showToasterError(errorMessage)
        })
    }

    override fun unBlockChat() {
        analytics.trackClickUnblockChat(shopId)
        presenter.unBlockChat(messageId, {
            getViewState().setChatBlockStatus(false)
            getViewState().onCheckChatBlocked(opponentRole, opponentName, getViewState().blockStatus)
            context?.let {
                showToasterConfirmation(it.getString(R.string.title_success_unblock_chat))
            }
        }, {
            val errorMessage = ErrorHandler.getErrorMessage(context, it)
            showToasterError(errorMessage)
        })
    }

    private fun getChatReportUrl(): String {
        var url = "${TkpdBaseURL.CHAT_REPORT_URL}$messageId"
        if (isSeller()) {
            url += "?isSeller=1"
        }
        return url
    }

    override fun onDualAnnouncementClicked(
            redirectUrl: String, attachmentId: String, blastId: Long
    ) {
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
            snackbar.setAction(this.getString(com.tokopedia.merchantvoucher.R.string.close), { snackbar.dismiss() })
            snackbar.setActionTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
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
            isFavoriteShop?.let {
                bundle.putString(ApplinkConst.Chat.SHOP_FOLLOWERS_CHAT_KEY, it.toString())
            }
            intent.putExtras(bundle)
            it.setResult(RESULT_OK, intent)
            it.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.launch(Dispatchers.IO) {
            sellerReviewHelper.saveMessageId(messageId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        unregisterUploadImageReceiver()
    }

    override fun trackSeenProduct(element: ProductAttachmentViewModel) {
        if (seenAttachedProduct.add(element.productId)) {
            analytics.eventSeenProductAttachment(requireContext(), element, session, amISeller)

            // this for experimentation of DATA
            if (remoteConfig?.getBoolean(RemoteConfigKey.CHAT_EVER_SEEN_PRODUCT, false) == true) {
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

    private fun getStringArgument(key: String, savedInstanceState: Bundle?): String {
        return getParamString(key, arguments, savedInstanceState)
    }

    private fun getBooleanArgument(key: String, savedInstanceState: Bundle?): Boolean {
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

    override fun onStickerOpened() {
        getViewState().onStickerOpened()
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
        presenter.followUnfollowShop(
                action = ToggleFavouriteShopUseCase.Action.FOLLOW,
                shopId = shopId.toString(),
                onSuccess = {
                    element.stopFollowShop()
                    onSuccessFollowShopFromBcHandler()
                    adapter.removeBroadcastHandler(element)
                    isFavoriteShop = true
                },
                onError = {
                    element.stopFollowShop()
                    onErrorFollowShopFromBcHandler(it)
                    adapter.updateBroadcastHandlerState(element)
                }
        )
    }

    override fun requestBlockPromo(element: BroadcastSpamHandlerUiModel?) {
        presenter.requestBlockPromo(messageId, { response ->
            getViewState().setChatPromoBlockStatus(true, response.chatBlockResponse.chatBlockStatus.validDate)
            element?.stopBlockPromo()
            onSuccessBlockPromoFromBcHandler(response)
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
            getViewState().setChatPromoBlockStatus(false)
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

    private fun onSuccessBlockPromoFromBcHandler(response: ChatSettingsResponse) {
        context?.let {
            val until = Utils.getDateTime(response.chatBlockResponse.chatBlockStatus.validDate)
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

    private fun initProductPreview(savedInstanceState: Bundle?) {
        val stringProductPreviews = getStringArgument(ApplinkConst.Chat.PRODUCT_PREVIEWS, savedInstanceState)
        if (stringProductPreviews.isEmpty()) return
        val listType = object : TypeToken<List<ProductPreview>>() {}.type
        val productPreviews = CommonUtil.fromJson<List<ProductPreview>>(
                stringProductPreviews,
                listType
        )
        for (productPreview in productPreviews) {
            if (productPreview.notEnoughRequiredData()) continue
            val sendAbleProductPreview = SendableProductPreview(productPreview)
            presenter.addAttachmentPreview(sendAbleProductPreview)
        }
    }

    private fun initInvoicePreview(savedInstanceState: Bundle?) {
        val id = getStringArgument(ApplinkConst.Chat.INVOICE_ID, savedInstanceState)
        val invoiceCode = getStringArgument(ApplinkConst.Chat.INVOICE_CODE, savedInstanceState)
        val productName = getStringArgument(ApplinkConst.Chat.INVOICE_TITLE, savedInstanceState)
        val date = getStringArgument(ApplinkConst.Chat.INVOICE_DATE, savedInstanceState)
        val imageUrl = getStringArgument(ApplinkConst.Chat.INVOICE_IMAGE_URL, savedInstanceState)
        val invoiceUrl = getStringArgument(ApplinkConst.Chat.INVOICE_URL, savedInstanceState)
        val statusId = getStringArgument(ApplinkConst.Chat.INVOICE_STATUS_ID, savedInstanceState)
        val status = getStringArgument(ApplinkConst.Chat.INVOICE_STATUS, savedInstanceState)
        val totalPriceAmount = getStringArgument(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, savedInstanceState)
        val invoiceViewModel = InvoicePreviewUiModel(
                id.toIntOrNull() ?: InvoicePreviewUiModel.INVALID_ID,
                invoiceCode,
                productName,
                date,
                imageUrl,
                invoiceUrl,
                statusId.toIntOrNull() ?: InvoicePreviewUiModel.INVALID_ID,
                status,
                totalPriceAmount
        )
        if (invoiceViewModel.enoughRequiredData()) {
            presenter.clearAttachmentPreview()
            presenter.addAttachmentPreview(invoiceViewModel)
        }
    }

    private fun initVoucherPreview(extras: Bundle?) {
        val stringVoucherPreview = getStringArgument(ApplinkConst.AttachVoucher.PARAM_VOUCHER_PREVIEW, extras)
        if (stringVoucherPreview.isEmpty()) return
        val voucherPreview = CommonUtil.fromJson<VoucherPreview>(stringVoucherPreview, VoucherPreview::class.java)
        val sendableVoucher = SendableVoucherPreview(voucherPreview)
        if (!presenter.hasEmptyAttachmentPreview()) {
            presenter.clearAttachmentPreview()
        }
        presenter.addAttachmentPreview(sendableVoucher)
    }

    override fun startReview(starCount: Int, review: ReviewUiModel, lastKnownPosition: Int) {
        val uriString = Uri.parse(review.reviewCard.reviewUrl).buildUpon()
                .appendQueryParameter(PARAM_RATING, starCount.toString())
                .build()
                .toString()
        val intent = RouteManager.getIntent(context, uriString)
        val reviewRequestResult = ReviewRequestResult(
                review, lastKnownPosition
        )
        reviewRequest.push(reviewRequestResult)
        startActivityForResult(intent, REQUEST_REVIEW)
    }

    override fun trackReviewCardImpression(element: ReviewUiModel) {
        analytics.trackReviewCardImpression(element, isSeller(), session.userId)
    }

    override fun trackReviewCardClick(element: ReviewUiModel) {
        analytics.trackReviewCardClick(element, isSeller(), session.userId)
    }

    private fun registerUploadImageReceiver() {
        uploadImageBroadcastReceiver = UploadImageBroadcastReceiver(this)
        activity?.let {
            val intentFilters = IntentFilter().apply {
                addAction(UploadImageChatService.BROADCAST_UPLOAD_IMAGE)
            }
            uploadImageBroadcastReceiver?.let { receiver ->
                LocalBroadcastManager.getInstance(it).registerReceiver(receiver, intentFilters)
            }
        }
    }

    override fun onSuccessUploadImageWithService(intent: Intent) {
        if (messageId == getResultMessageId(intent)) {
            val image = intent.getParcelableExtra<ImageUploadServiceModel>(UploadImageChatService.IMAGE)
            image?.let {
                removeDummy(ImageUploadMapper.mapToImageUploadViewModel(it))
            }
        }
    }

    override fun onErrorUploadImageWithService(intent: Intent) {
        if (messageId == getResultMessageId(intent)) {
            val errorMessage = intent.getStringExtra(UploadImageChatService.ERROR_MESSAGE) ?: ""
            val position = intent.getIntExtra(UploadImageChatService.RETRY_POSITION, -1)
            if (position > -1 && position < UploadImageChatService.dummyMap.size) {
                val dummyTarget = UploadImageChatService.dummyMap[position]
                onErrorUploadImage(errorMessage, dummyTarget.visitable as ImageUploadViewModel)
            }
        }
    }

    private fun getResultMessageId(intent: Intent): String {
        return intent.getStringExtra(UploadImageChatService.MESSAGE_ID) ?: ""
    }

    private fun unregisterUploadImageReceiver() {
        activity?.let {
            uploadImageBroadcastReceiver?.let { receiver ->
                LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver)
            }
        }
    }

    override fun onClickSrwQuestion(question: QuestionUiModel) {
        if (presenter.isInTheMiddleOfThePage()) {
            resetItemList()
            delaySendSrwQuestion(question)
            presenter.getExistingChat(
                    messageId, ::onErrorResetChatToFirstPage, ::onSuccessResetChatToFirstPage
            )
        } else {
            sendSrwQuestion(question)
        }
    }

    override fun trackClickSrwQuestion(element: QuestionUiModel) {
        val productIds = presenter.getProductIdPreview()
        val trackProductIds = productIds.joinToString(separator = ", ")
        analytics.eventClickSrw(shopId, session.userId, trackProductIds, element)
    }

    private fun delaySendSrwQuestion(question: QuestionUiModel) {
        delaySendSrw = question
    }

    private fun sendSrwQuestion(question: QuestionUiModel) {
        onSendAndReceiveMessage()
        val startTime = SendableViewModel.generateStartTime()
        presenter.sendAttachmentsAndSrw(
                messageId,
                question,
                startTime,
                opponentId,
                onSendingMessage()
        )
    }

    private fun String.ellipsize(maxChar: Int): String {
        return if (length > maxChar) {
            "${substring(0, maxChar)}..."
        } else {
            this
        }
    }

    override fun onReplyBoxEmpty() {
        sendButton?.background = MethodChecker.getDrawable(context, R.drawable.bg_topchat_send_btn_disabled)
        sendButton?.setOnClickListener {
            showSnackbarError(getString(R.string.topchat_desc_empty_text_box))
        }
    }

    override fun onReplyBoxNotEmpty() {
        sendButton?.background = MethodChecker.getDrawable(context, R.drawable.bg_topchat_send_btn)
        sendButton?.setOnClickListener {
            onSendButtonClicked()
        }
    }

    companion object {
        const val PARAM_RATING = "rating"
        const val PARAM_UTM_SOURCE = "utmSource"
        const val REVIEW_SOURCE_TOPCHAT = "android_topchat"
        private const val MAX_SIZE_IMAGE_PICKER = 20360
        fun createInstance(bundle: Bundle): BaseChatFragment {
            return TopChatRoomFragment().apply {
                arguments = bundle
            }
        }
    }
}
