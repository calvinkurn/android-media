package com.tokopedia.topchat.chatroom.view.fragment

import android.app.Activity.CLIPBOARD_SERVICE
import android.app.Activity.RESULT_OK
import android.content.*
import android.content.ClipData
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
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
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.BaseChatUiModel.Builder.Companion.generateCurrentReplyTime
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.*
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker.common.*
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_SOURCE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATE_MESSAGE
import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel
import com.tokopedia.topchat.chatroom.data.activityresult.ReviewRequestResult
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.BlockActionType
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.WrapperChatSetting
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.service.UploadImageBroadcastListener
import com.tokopedia.topchat.chatroom.service.UploadImageBroadcastReceiver
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity.Companion.REQUEST_ATTACH_IMAGE
import com.tokopedia.topchat.chatroom.view.activity.TopchatReportWebViewActivity
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactoryImpl
import com.tokopedia.topchat.chatroom.view.adapter.layoutmanager.TopchatLinearLayoutManager
import com.tokopedia.topchat.chatroom.view.adapter.util.CenterSmoothScroller
import com.tokopedia.topchat.chatroom.view.adapter.util.LoadMoreTopBottomScrollListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwBubbleViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwQuestionViewHolder
import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder
import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder.MENU_ID_COPY_TO_CLIPBOARD
import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder.MENU_ID_REPLY
import com.tokopedia.topchat.chatroom.view.custom.*
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.topchat.chatroom.view.custom.message.TopchatMessageRecyclerView
import com.tokopedia.topchat.chatroom.view.customview.TopChatRoomDialog
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewStateImpl
import com.tokopedia.topchat.chatroom.view.listener.*
import com.tokopedia.topchat.chatroom.view.onboarding.ReplyBubbleOnBoarding
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.*
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.custom.TopChatKeyboardHandler
import com.tokopedia.topchat.common.util.TopChatSellerReviewHelper
import com.tokopedia.topchat.common.util.Utils
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel.Companion.SHOP_TYPE_TOKONOW
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepreview.imagesecure.ImageSecurePreviewActivity
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity.Companion.IS_FROM_ANOTHER_CALL
import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder.MENU_ID_DELETE_BUBBLE
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ReminderTickerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.uimodel.SendableVoucherPreviewUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil

/**
 * @author : Steven 29/11/18
 */

open class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View, TypingListener,
    SendButtonListener, ImagePickerListener, ChatTemplateListener,
    HeaderMenuListener, DualAnnouncementListener, TopChatVoucherListener,
    InvoiceThumbnailListener,
    TransactionOrderProgressLayout.Listener, ChatMenuStickerView.StickerMenuListener,
    StickerViewHolder.Listener, DeferredViewHolderAttachment, CommonViewHolderListener,
    SearchListener, BroadcastSpamHandlerViewHolder.Listener,
    RoomSettingFraudAlertViewHolder.Listener, ReviewViewHolder.Listener,
    TopchatProductAttachmentListener, UploadImageBroadcastListener,
    SrwQuestionViewHolder.Listener, ReplyBoxTextListener, SrwBubbleViewHolder.Listener,
    FlexBoxChatLayout.Listener, ReplyBubbleAreaMessage.Listener,
    ReminderTickerViewHolder.Listener, ProductBundlingListener {

    @Inject
    lateinit var topChatRoomDialog: TopChatRoomDialog

    @Inject
    lateinit var analytics: TopChatAnalytics

    @Inject
    lateinit var session: UserSessionInterface

    @Inject
    lateinit var sellerReviewHelper: TopChatSellerReviewHelper

    @Inject
    lateinit var viewModel: TopChatViewModel

    @Inject
    lateinit var replyBubbleOnBoarding: ReplyBubbleOnBoarding

    @Inject
    lateinit var abTestPlatform: AbTestPlatform

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
    private var interlocutorShopType: String = ""

    //This used only for set extra in finish activity
    private var isFavoriteShop: Boolean? = null

    private var seenAttachedProduct = HashSet<String>()
    private var seenAttachedBannedProduct = HashSet<String>()
    private var seenAttachmentVoucher = HashSet<String>()
    private var seenAttachmentProductBundling = HashSet<String>()
    private val reviewRequest = Stack<ReviewRequestResult>()
    private var composeMsgArea: ComposeMessageAreaConstraintLayout? = null
    private var orderProgress: TransactionOrderProgressLayout? = null
    private var chatMenu: ChatMenuView? = null
    private var rvLayoutManager: TopchatLinearLayoutManager? = null
    private var rvScrollListener: LoadMoreTopBottomScrollListener? = null
    private var fbNewUnreadMessage: FloatingButtonUnify? = null
    private var tvTotalUnreadMessage: Typography? = null
    private var rv: TopchatMessageRecyclerView? = null
    private var rvSrw: SrwFrameLayout? = null
    private var replyCompose: ReplyBubbleAreaMessage? = null
    private var rvContainer: CoordinatorLayout? = null
    private var chatBackground: ImageView? = null
    private var sendButton: IconUnify? = null
    protected var topchatViewState: TopChatViewStateImpl? = null
    private var uploadImageBroadcastReceiver: BroadcastReceiver? = null
    private var smoothScroller: CenterSmoothScroller? = null

    /**
     * Ticker Reminder flag, only 1 ticker can exist in 1 session
     */
    private var isTickerNotShownYet = true

    var chatRoomFlexModeListener: TopChatRoomFlexModeListener? = null
    var chatBoxPadding: View? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view_chatroom
    override fun getAnalytic(): TopChatAnalytics = analytics

    override fun isLoadMoreEnabledByDefault(): Boolean = false

    private val newMsgObserver = Observer<Visitable<*>> { uiModel ->
        onSendAndReceiveMessage()
        onReceiveMessageEvent(uiModel)
    }

    private val srwRemovalObserver = Observer<String?> { productId ->
        if (productId != null) {
            removeSrwBubble(productId)
        } else {
            removeSrwBubble()
        }
    }

    private val deleteMsgObserver = Observer<String> { replyTime ->
        onReceiveWsEventDeleteMsg(replyTime)
    }

    /**
     * stack to keep latest change address request
     */
    private val changeAddressStack = Stack<HeaderCtaButtonAttachment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFireBase()
        registerUploadImageReceiver()
        initSmoothScroller()
    }

    private fun initSmoothScroller() {
        smoothScroller = CenterSmoothScroller(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topchat_chatroom, container, false).also {
            bindView(it)
            initUserLocation()
            initObserver()
            initComposeAreaMsg()
            initStickerView()
            initFbNewUnreadMessage()
        }
    }

    private fun initComposeAreaMsg() {
        composeMsgArea?.initLayout(this,this)
    }

    private fun getSrwFrameLayoutListener() : SrwFrameLayout.Listener {
        return object : SrwFrameLayout.Listener {
            override fun trackViewSrw() {
                analytics.eventViewSrw(shopId, session.userId)
            }

            override fun onExpandStateChanged(isExpanded: Boolean) {
                if (isExpanded) {
                    topchatViewState?.hideKeyboard()
                    chatMenu?.hideMenu()
                }
                adapter.setSrwBubbleState(isExpanded)
            }

            override fun shouldShowOnBoarding(): Boolean {
                return replyBubbleOnBoarding.hasBeenShown()
            }
        }
    }

    private fun initUserLocation() {
        context?.let {
            val userLocation = ChooseAddressUtils.getLocalizingAddressData(it) ?: return@let
            viewModel.initUserLocation(userLocation)
        }
    }

    private fun initObserver() {
        adapter.srwUiModel.observe(viewLifecycleOwner) {
            if (it == null) {
                showTemplateChatIfReady()
            } else {
                topchatViewState?.hideTemplateChat()
            }
        }
    }

    override fun updateSrwPreviewState() {
        if (shouldShowSrw()) {
            handleSrw(onNewDesign = {
                //Show SRW if only there's no reply specific message
                topchatViewState?.shouldShowSrw = (replyCompose?.isVisible != true)
            }, onOldDesign = {
                rvSrw?.renderSrwState()
                topchatViewState?.shouldShowSrw = false
            })
            topchatViewState?.hideTemplateChat()
        } else if (topchatViewState?.chatTextAreaTabLayout?.srwLayout?.isLoadingState() != true) {
            handleSrw(onNewDesign = {
                topchatViewState?.shouldShowSrw = false
            }, onOldDesign = {
                rvSrw?.hideSrw()
                topchatViewState?.shouldShowSrw = false
            })
            showTemplateChatIfReady()
        }
    }

    private fun showTemplateChatIfReady() {
        if (chatRoomFlexModeListener?.isFlexMode() == true) {
            topchatViewState?.hideTemplateChat()
        } else {
            topchatViewState?.showTemplateChatIfReady(
                adapter.isLastMessageBroadcast(), adapter.isLastMsgSrwBubble(),
                !isSeller()
            )
        }
    }

    override fun hasProductPreviewShown(): Boolean {
        return topchatViewState?.hasProductPreviewShown() ?: false
    }

    override fun hasNoSrw(): Boolean {
        return if (isSrwNewDesign()) {
            topchatViewState?.chatTextAreaTabLayout?.srwLayout?.isAllowToShow() == false
                    && topchatViewState?.chatTextAreaTabLayout?.srwLayout?.isSuccessState() == true
        } else {
            rvSrw?.isAllowToShow() == false && rvSrw?.isSuccessState() == true
        }
    }

    override fun collapseSrw() {
        rvSrw?.isExpanded = false
    }

    override fun expandSrw() {
        expandSrwPreview()
        expandSrwBubble()
    }

    private fun expandSrwPreview() {
        rvSrw?.isExpanded = true
    }

    override fun expandSrwBubble() {
        if (
            topchatViewState?.hasVisibleSendablePreview() == false &&
            chatMenu?.isVisible == false &&
            topchatViewState?.isKeyboardOpen() == false
        ) {
            adapter.expandSrwBubble()
        }
    }

    override fun showPreviewMsg(previewMsg: SendableUiModel) {
        if (!adapter.hasPreviewOnList(previewMsg.localId)) {
            adapter.addHeaderDateIfDifferent(previewMsg)
            adapter.addNewMessage(previewMsg)
            topchatViewState?.scrollToBottom()
        }
    }

    override fun clearReferredMsg() {
        replyCompose?.clearReferredComposedMsg()
        if (isSrwNewDesign()) {
            topchatViewState?.chatTextAreaTabLayout?.srwLayout?.isForceToHide = false
        }
    }

    override fun notifyPreviewRemoved(model: SendablePreview) {
        viewModel.removeAttachmentPreview(model)
        if (model is TopchatProductAttachmentPreviewUiModel && hasProductPreviewShown()) {
            if (isSrwNewDesign()) {
                topchatViewState?.showChatAreaShimmer()
                topchatViewState?.chatTextAreaTabLayout?.srwLayout?.resetChatReplyQuestion()
            }
            reloadSrw()
        }
    }

    override fun reloadCurrentAttachment() {
        viewModel.reloadCurrentAttachment()
    }

    override fun removeSrwBubble() {
        adapter.removeSrwBubble()
    }

    override fun removeSrwBubble(productId: String) {
        adapter.removeSrwBubble(productId)
    }

    override fun shouldShowSrw(): Boolean {
        return if (isSrwNewDesign()) {
            !isSeller() && hasProductPreviewShown() &&
                    topchatViewState?.chatTextAreaTabLayout?.srwLayout?.isAllowToShow() == true &&
                    viewModel.isAttachmentPreviewReady()
        } else {
            !isSeller() && hasProductPreviewShown() &&
                rvSrw?.isAllowToShow() == true && viewModel.isAttachmentPreviewReady() ||
                (rvSrw?.isLoadingState() == true && hasProductPreviewShown())
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
        composeMsgArea = view?.findViewById(R.id.reply_box)
        orderProgress = view?.findViewById(R.id.ll_transaction_progress)
        chatMenu = view?.findViewById(R.id.fl_chat_menu)
        rv = view?.findViewById(recyclerViewResourceId)
        replyCompose = view?.findViewById(R.id.trb_container)
        rvSrw = view?.findViewById(R.id.rv_srw)
        rvContainer = view?.findViewById(R.id.rv_container)
        fbNewUnreadMessage = view?.findViewById(R.id.fb_new_unread_message)
        chatBackground = view?.findViewById(R.id.iv_bg_chat)
        sendButton = view?.findViewById(R.id.send_but)
        chatBoxPadding = view?.findViewById(R.id.view_chat_box_padding)
    }

    private fun initStickerView() {
        chatMenu?.setStickerListener(this)
        chatMenu?.setVisibilityListener(object : ChatMenuView.VisibilityListener {
            override fun onShow() {
                collapseSrw()
            }

            override fun onHide() {
                expandSrw()
                expandSrwBubble()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateMessageId(messageId)
        setupBackground()
        setupViewState()
        setupArguments(savedInstanceState)
        setupAttachmentsPreview(savedInstanceState)
        hideLoading()
        setupAnalytic()
        setupBeforeReplyTime()
        loadInitialData()
        initLoadMoreListener()
        disableSendButton()
        initKeyboardListener(view)
        removeAttachmentIfNecessary(savedInstanceState)
        setupObservers()
        setupLifecycleObserver()
        initChatTextAreaLayout()
    }

    private fun setupLifecycleObserver() {
        viewLifecycleOwner.lifecycle.addObserver(viewModel)
    }

    private fun setupBackground() {
        viewModel.getBackground()
    }

    private fun setupBeforeReplyTime(replyTimeMillis: String) {
        createTime = replyTimeMillis
        setupBeforeReplyTime()
    }

    private fun setupBeforeReplyTime() {
        if (createTime.isNotEmpty()) {
            viewModel.setBeforeReplyTime(createTime)
        }
    }

    private fun initLoadMoreListener() {
        rvScrollListener = object : LoadMoreTopBottomScrollListener(rvLayoutManager) {
            override fun loadMoreTop() {
                rv?.post {
                    showTopLoading()
                }
                viewModel.loadTopChat(messageId)
            }

            override fun loadMoreDown() {
                showBottomLoading()
                viewModel.loadBottomChat(messageId)
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
        viewModel.loadAttachmentData(messageId.toLongOrZero(), chatRoom)
        renderTickerReminderIfNotYet()
    }

    private fun renderTickerReminderIfNotYet() {
        val ticker = viewModel.tickerReminder.value
        if (ticker != null && ticker is Success && isTickerNotShownYet) {
            onSuccessGetTickerReminder(ticker.data)
        }
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
        viewModel.loadAttachmentData(messageId.toLongOrZero(), chatRoom)
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
        TopChatAnalyticsKt.sourcePage = sourcePage
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
        return TopchatLinearLayoutManager(
            activity, LinearLayoutManager.VERTICAL, true
        ).also {
            rvLayoutManager = it
        }
    }

    override fun loadInitialData() {
        showLoading()
        if (messageId.isNotEmpty()) {
            viewModel.getExistingChat(messageId, true)
            viewModel.connectWebSocket()
            viewModel.getOrderProgress(messageId)
        } else {
            viewModel.getMessageId(toUserId, toShopId, source)
        }
    }

    override fun updateProductStock(
        product: ProductAttachmentUiModel,
        adapterPosition: Int,
        parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    ) {
        var id = product.parentId
        if (id == ProductAttachmentUiModel.NO_PRODUCT_ID) {
            id = product.productId
        }
        val intent = RouteManager.getIntent(
            context, ApplinkConstInternalMarketplace.RESERVED_STOCK,
            id, product.shopId
        )
        intent.putExtra(EXTRA_SOURCE, EXTRA_SOURCE_STOCK)
        viewModel.addOngoingUpdateProductStock(id, product, adapterPosition, parentMetaData)
        startActivityForResult(intent, REQUEST_UPDATE_STOCK)
    }

    override fun trackClickUpdateStock(product: ProductAttachmentUiModel) {
        analytics.trackClickUpdateStock(product)
    }

    override fun isOCCActive(): Boolean {
        /**
         * AB Test Removed because of expired rollence
         * The OCC feature will be used again in next feature (group buy)
         */
        return false
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

    private fun setupViewState() {
        topchatViewState?.attachFragmentView(this)
    }

    private fun setupArguments(savedInstanceState: Bundle?) {
        customMessage =
            getParamString(ApplinkConst.Chat.CUSTOM_MESSAGE, arguments, savedInstanceState)
        sourcePage = getParamString(ApplinkConst.Chat.SOURCE_PAGE, arguments, savedInstanceState)
        indexFromInbox = getParamInt(
            TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX,
            arguments,
            savedInstanceState
        )
        createTime =
            getParamString(ApplinkConst.Chat.SEARCH_CREATE_TIME, arguments, savedInstanceState)
        searchQuery =
            getParamString(ApplinkConst.Chat.SEARCH_PRODUCT_KEYWORD, arguments, savedInstanceState)
    }

    private fun setupAttachmentsPreview(savedInstanceState: Bundle?) {
        val isFromAnotherChat = isFromAnotherChat(savedInstanceState)
        if (!isFromAnotherChat) {
            initProductPreview(savedInstanceState)
            initInvoicePreview(savedInstanceState)
        }
        viewModel.initAttachmentPreview()
    }

    private fun onSuccessGetMessageId(messageId: String) {
        this.messageId = messageId
        viewModel.updateMessageId(messageId)
        loadInitialData()
        if (chatRoomFlexModeListener?.isFlexMode() == true) {
            chatRoomFlexModeListener?.onSuccessGetMessageId(msgId = messageId)
        }
        reloadSrw()
        viewModel.loadPendingProductPreview()
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
        viewModel.getShopFollowingStatus(shopId)
    }

    private fun setupDummyData() {
        for (dummy in UploadImageChatService.dummyMap) {
            if (dummy.messageId == messageId) {
                dummy.visitable?.let {
                    showPreviewMsg(it as SendableUiModel)
                    if (dummy.isFail) {
                        topchatViewState?.showRetryUploadImages(it as ImageUploadUiModel, true)
                    }
                }
            }
        }
    }

    private fun setupFirstTimeOnly(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        updateViewData(chatRoom)
        checkCanAttachVoucher()
        orderProgress?.renderIfExist()
        topchatViewState?.onSuccessLoadFirstTime(
            chatRoom, onToolbarClicked(), this
        )
        topchatViewState?.onSetCustomMessage(customMessage)
        viewModel.getTemplate(chatRoom.isSeller())
        viewModel.getStickerGroupList(chatRoom.isSeller())
        replyCompose?.setReplyListener(this)
        if (isSeller()) {
            setupFirstTimeForSeller()
        }
        viewModel.getTickerReminder(isSeller())
        interlocutorShopType = chatRoom.shopType
    }

    private fun setupFirstTimeForSeller() {
        viewModel.adjustInterlocutorWarehouseId(messageId)
    }

    private fun reloadSrw() {
        if (!isSeller() && messageId.isNotBlank() &&
            topchatViewState?.hasProductPreviewShown() == true
                && viewModel.isAttachmentPreviewReady()
        ) {
            val productIdCommaSeparated2 = viewModel.getProductIdPreview()
                .joinToString(separator = ",")
            viewModel.getSmartReplyWidget(messageId, productIdCommaSeparated2)
        } else if (topchatViewState?.getChatAreaShimmer()?.isVisible == true) {
            topchatViewState?.shouldShowSrw = false
        }
    }

    private fun setupFirstPage(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        adapter.setLatestHeaderDate(chatRoom.latestHeaderDate)
        renderList(chatRoom.listChat)
        updateHasNextState(chat)
        updateHasNextAfterState(chat)
        loadChatRoomSettings(chatRoom)
        viewModel.loadAttachmentData(messageId.toLongOrZero(), chatRoom)
    }

    private fun checkReplyBubbleOnBoardingFirstRender() {
        val hasShowOnBoarding = replyBubbleOnBoarding.hasBeenShown()
        if (!hasShowOnBoarding && !viewModel.isInTheMiddleOfThePage()) {
            if (isSrwNewDesign() &&
                topchatViewState?.chatTextAreaTabLayout?.visibility == View.VISIBLE
            ) {
                replyBubbleOnBoarding.showReplyBubbleOnBoarding(
                    rv, adapter, topchatViewState?.chatTextAreaTabLayout?.tabReplyBox, context
                )
            } else {
                replyBubbleOnBoarding.showReplyBubbleOnBoarding(
                    rv, adapter, composeMsgArea, context
                )
            }
        }
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

    private fun onErrorGetShopFollowingStatus() {
        topchatViewState?.isShopFollowed = false
    }

    private fun onSuccessGetShopFollowingStatus(isFollow: Boolean) {
        topchatViewState?.isShopFollowed = isFollow
        addBroadCastSpamHandler(isFollow)
    }

    private fun addBroadCastSpamHandler(isFollow: Boolean) {
        if (topchatViewState?.blockStatus?.isPromoBlocked == true ||
            isFollow || viewModel.isInTheMiddleOfThePage()
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
        if (opponentRole.toLowerCase() == ChatRoomHeaderUiModel.Companion.ROLE_USER) {
            goToProfile(opponentId)
        } else if (opponentRole.toLowerCase()
                .contains(ChatRoomHeaderUiModel.Companion.ROLE_SHOP)
        ) {
            onGoToShop()
        }
    }

    private fun goToProfile(opponentId: String) {
        RouteManager.route(activity, ApplinkConst.PROFILE.replace("{user_id}", opponentId))
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        topchatViewState?.showErrorWebSocket(isWebSocketError)
    }

    private fun loadChatRoomSettings(chatRoom: ChatroomViewModel) {
        if (chatRoom.canLoadMore) return
        viewModel.loadChatRoomSettings(messageId)
    }

    private fun onSuccessLoadChatRoomSetting(widgets: List<Visitable<TopChatTypeFactory>>) {
        adapter.addWidgetHeader(widgets)
    }

    private fun onError(throwable: Throwable) {
        hideLoading()
        view?.let {
            showSnackbarError(ErrorHandler.getErrorMessage(it.context, throwable))
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

    override fun onImageAnnouncementClicked(uiModel: ImageAnnouncementUiModel) {
        analytics.trackClickImageAnnouncement(
            uiModel.broadcastBlastId.toString(),
            uiModel.attachmentId
        )
        super.onImageAnnouncementClicked(uiModel)
    }

    override fun onRetrySendImage(element: ImageUploadUiModel) {
        context?.let {
            val bs = TopchatBottomSheetBuilder.getErrorUploadImageBs(it,
                onRetryClicked = {
                    resendImage(element)
                },
                onDeleteClicked = {
                    adapter.removePreviewMsg(element.localId)
                    UploadImageChatService.removeDummyOnList(element)
                }
            )
            bs.show(childFragmentManager, "Retry Image Upload")
        }
    }

    private fun resendImage(element: ImageUploadUiModel) {
        //change the retry value
        element.isRetry = false
        adapter.updatePreviewState(element.localId)
        viewModel.startUploadImages(element)
    }

    override fun onProductClicked(element: ProductAttachmentUiModel) {
        super.onProductClicked(element)
        context?.let {
            analytics.eventClickProductThumbnailEE(it, element, session)
        }

        analytics.trackProductAttachmentClicked()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        val chatBubble = visitable as? BaseChatUiModel
        val hasPreviewOnList = adapter.hasPreviewOnList(chatBubble?.localId)
        if (chatBubble != null && hasPreviewOnList) {
            adapter.updatePreviewUiModel(visitable, chatBubble.localId)
        } else {
            viewState?.removeDummyIfExist(visitable)
            viewState?.onReceiveMessageEvent(visitable)
        }
        topchatViewState?.scrollDownWhenInBottom()
        isMoveItemInboxToTop = true

        renderTickerReminder(chatBubble)
    }

    private fun renderTickerReminder(chatBubble: BaseChatUiModel?) {
        chatBubble?.tickerReminder?.let {
            val reminderTickerUiModel = ReminderTickerUiModel.mapToReminderTickerUiModel(it)
            if (isTickerNotShownYet) {
                onSuccessGetTickerReminder(reminderTickerUiModel)
            }
        }
    }

    override fun loadData(page: Int) {}

    override fun onImageUploadClicked(imageUrl: String, replyTime: String, isSecure: Boolean) {
        analytics.trackClickImageUpload()
        activity?.let {
            val strings: ArrayList<String> = ArrayList()
            strings.add(imageUrl)

            it.startActivity(
                ImageSecurePreviewActivity.getCallingIntent(
                    context = it,
                    imageUris = strings,
                    imageDesc = null,
                    position = 0,
                    disableDownloadButton = false,
                    isSecure = isSecure
                )
            )
        }

    }

    private fun onAttachProductClicked() {
        context?.let {
            val intent = TopChatInternalRouter.Companion.getAttachProductIntent(
                context = it,
                shopId = shopId,
                isSeller = isSeller(),
                warehouseId = viewModel.attachProductWarehouseId
            )
            startActivityForResult(intent, TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE)
        }
    }

    override fun clearEditText() {
        topchatViewState?.clearEditText()
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return TopChatTypeFactoryImpl(
            this, this, this, this,
            this, this, this, this,
            this, this, this, this,
            this, this, this, this,
            this, this, session
        )
    }

    override fun renderOrderProgress(chatOrder: ChatOrderProgress) {
        orderProgress?.render(this, chatOrder)
    }

    override fun getChatMenuView(): ChatMenuView? {
        return topchatViewState?.chatMenu
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
            viewModel.getExistingChat(messageId)
            onViewReachBottomMostChat()
        }
        if (fbNewUnreadMessage?.isVisible == false) {
            fbNewUnreadMessage?.visibility = View.VISIBLE
        }
    }

    private fun onViewReachBottomMostChat() {
        viewModel.resetUnreadMessage()
        viewModel.markAsRead()
        hideUnreadMessage()
    }

    override fun hideUnreadMessage() {
        if (fbNewUnreadMessage?.isVisible == true) {
            fbNewUnreadMessage?.visibility = View.GONE
        }
    }

    override fun onSendAndReceiveMessage() {
        adapter.removeBroadcastHandler()
        updateSrwPreviewState()
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

    override fun addDummyMessage(visitable: Visitable<*>) { }

    override fun removeDummy(visitable: Visitable<*>) {
        topchatViewState?.removeDummy(visitable)
    }

    override fun onErrorUploadImage(errorMessage: String, it: ImageUploadUiModel) {
        showSnackbarError(errorMessage)
        topchatViewState?.showRetryUploadImages(it, true)
    }

    override fun onSendButtonClicked() {
        if (viewModel.isInTheMiddleOfThePage() && isValidComposedMessage()) {
            resetItemList()
            delaySendMessage()
            viewModel.getExistingChat(messageId)
        } else {
            sendAttachmentPreviews()
            sendComposedMsg()
            onSendAndReceiveMessage()
            clearAttachmentPreviews()
            clearReferredMsg()
        }
    }

    private fun resetItemList() {
        rvScrollListener?.reset()
        adapter.reset()
        viewModel.resetChatUseCase()
        showLoading()
        topchatViewState?.hideProductPreviewLayout()
        topchatViewState?.scrollToBottom()
    }

    private fun isValidComposedMessage(): Boolean {
        val message = getComposedMessage()
        return message.isNotBlank()
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
            getComposeMessageArea()?.getComposedText() ?: ""
        }
    }

    private fun sendAttachmentPreviews(message: String? = null) {
        if (viewModel.isAttachmentPreviewReady()) {
            val composedMsg = getComposedMessage(message)
            viewModel.sendAttachments(composedMsg)
        }
    }

    private fun sendComposedMsg() {
        getComposeMessageArea()?.onSendMessage()
        val composedMsg = getComposedMessage()
        sendMessage(composedMsg)
        onSendingMessage().invoke()
    }

    private fun sendMessage(
        message: String,
        intention: String? = null
    ) {
        val referredMsg = replyCompose?.referredMsg
        handleSrw(onNewDesign = {
            if (topchatViewState?.chatTextAreaTabLayout?.isVisible == true) {
                addSrwBubbleToChat()
            }
        }, onOldDesign = {
            if (rvSrw?.isShowing() == true) {
                addSrwBubbleToChat()
            }
        })
        replyBubbleOnBoarding.dismiss()
        viewModel.sendMsg(message, intention, referredMsg)
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
            sendSrwQuestionPreview(it)
        }
        setupFirstPage(chatRoom, chat)
        delaySendMessage = ""
        delaySendSticker = null
        delaySendSrw = null
    }

    override fun onClickSticker(sticker: Sticker) {
        if (viewModel.isInTheMiddleOfThePage()) {
            resetItemList()
            delaySendSticker(sticker)
            viewModel.getExistingChat(messageId)
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
        val referredMsg = replyCompose?.referredMsg
        handleSrw(onNewDesign = {
            if (topchatViewState?.chatTextAreaTabLayout?.isVisible == true) {
                addSrwBubbleToChat()
            }
        }, onOldDesign = {
            if (rvSrw?.isShowing() == true) {
                addSrwBubbleToChat()
            }
        })
        onSendingMessage().invoke()
        sendAttachmentPreviews(sticker.intention)
        viewModel.sendSticker(sticker, referredMsg)
        clearAttachmentPreviews()
        clearReferredMsg()
    }

    private fun onSendingMessage(clearMessage: Boolean = true): () -> Unit {
        return {
            analytics.eventSendMessage()
            topchatViewState?.scrollToBottom()
            sellerReviewHelper.hasRepliedChat = true
            if (clearMessage) {
                clearEditText()
            }
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
        viewModel.sendWsStartTyping()
    }

    override fun onStopTyping() {
        viewModel.sendWsStopTyping()
    }

    override fun addTemplateString(message: String?) {
        message?.let {
            topchatViewState?.addTemplateString(message)
        }
    }

    override fun onSuccessGetTemplate(list: List<Visitable<*>>) {
        val isLastMessageBroadcast = adapter.isLastMessageBroadcast()
        val amIBuyer = !isSeller()
        chatRoomFlexModeListener?.getSeparatedTemplateChat()?.updateTemplate(list)
        if (chatRoomFlexModeListener?.isFlexMode() == true) {
            hideSeparatedChatTemplate()
        } else {
            topchatViewState?.setTemplate(list, isLastMessageBroadcast, amIBuyer)
            checkReplyBubbleOnBoardingFirstRender()
        }
    }

    override fun onErrorGetTemplate() {
        chatRoomFlexModeListener?.getSeparatedTemplateChat()?.hide()
        topchatViewState?.setTemplate(null)
    }

    override fun pickImageToUpload() {
        if (isUsingMediaPicker()) {
            pickImageWithMediaPicker()
        } else {
            pickImageWithImagePicker()
        }
    }

    private fun pickImageWithMediaPicker() {
        context?.let {
            val intent = MediaPicker.intent(it) {
                pageSource(PageSource.TopChat)
                modeType(ModeType.IMAGE_ONLY)
                singleSelectionMode()
            }
            startActivityForResult(intent, REQUEST_CODE_IMAGE_MEDIA_PICKER)
        }
    }

    private fun pickImageWithImagePicker() {
        context?.let {
            val builder = ImagePickerBuilder.getOriginalImageBuilder(it)
                .withSimpleMultipleSelection(maxPick = 1).apply {
                    maxFileSizeInKB = MAX_SIZE_IMAGE_PICKER
                }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            intent.putParamPageSource(ImagePickerPageSource.TOP_CHAT_PAGE)
            startActivityForResult(intent, REQUEST_ATTACH_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GO_TO_SETTING_TEMPLATE -> onReturnFromSettingTemplate()
            REQUEST_ATTACH_IMAGE -> onReturnFromChooseImage(resultCode, data)
            REQUEST_CODE_IMAGE_MEDIA_PICKER -> onReturnFromMediaPicker(resultCode, data)
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
            val updateProductResult = viewModel.onGoingStockUpdate[productId] ?: return
            val variantResult = getVariantResultUpdateStock(
                data, updateProductResult.product.productId
            )
            variantResult?.let {
                stockCount = variantResult.stockCount
                status = variantResult.status.name
                productName = "$productName - ${variantResult.productName}"
            }
            showToasterMsgFromUpdateStock(updateProductResult, productName, status)
            adapter.updateProductStock(updateProductResult, stockCount, status)
            viewModel.onGoingStockUpdate.remove(productId)
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
        val name = productName?.ellipsize(ELLIPSIZE_MAX_CHAR) ?: return
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
        topchatViewState?.hideAttachmentMenu()
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        val imagePathList = getImagePickerResult(data)
        handleImageToUpload(imagePathList = imagePathList)
    }

    private fun onReturnFromMediaPicker(resultCode: Int, data: Intent?) {
        topchatViewState?.hideAttachmentMenu()
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        val result = MediaPicker.result(data)
        handleImageToUpload(imagePathList = result.originalPaths)
    }

    private fun handleImageToUpload(imagePathList: List<String>) {
        processImagePathToUpload(imagePathList)?.let { model ->
            onSendAndReceiveMessage()
            viewModel.startUploadImages(model)
            topchatViewState?.scrollToBottom()
            sellerReviewHelper.hasRepliedChat = true
        }
    }

    private fun onReturnFromSettingTemplate() {
        viewModel.getTemplate(getUserSession().shopId == shopId.toString())
    }

    private fun onReturnFromReportUser(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        val result = data.getStringExtra(TopChatInternalRouter.Companion.RESULT_KEY_REPORT_USER)
        val payload =
            data.getStringExtra(TopChatInternalRouter.Companion.RESULT_KEY_PAYLOAD_REPORT_USER)
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
        topchatViewState?.shouldShowSrw = false
        val invoiceAttached = initInvoicePreview(data.extras)
        if (invoiceAttached) {
            removeSrwPreview()
            showTemplateChatIfReady()
        }
        viewModel.initAttachmentPreview()
    }

    private fun onAttachVoucherSelected(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != RESULT_OK) return
        topchatViewState?.shouldShowSrw = false
        initVoucherPreview(data.extras)
        viewModel.initAttachmentPreview()
    }

    private fun onReturnFromShopPage(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            topchatViewState?.isShopFollowed =
                data.getBooleanExtra(EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP, false)
        }
    }

    private fun onProductAttachmentSelected(data: Intent?) {
        if (data == null || !data.hasExtra(TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)) return
        val resultProducts: ArrayList<ResultProduct>? = data.getParcelableArrayListExtra(
            TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY
        )
        resultProducts?.let { products ->
            removeSrwBubble()
            removeSrwPreview()
            if (isSrwNewDesign()) {
                topchatViewState?.showChatAreaShimmer()
            }
            val productIds = products.map { it.productId }
            viewModel.loadProductPreview(productIds)
        }
    }

    private fun removeSrwPreview() {
        handleSrw(onNewDesign = {
            topchatViewState?.chatTextAreaTabLayout?.srwLayout?.hideSrw()
            topchatViewState?.chatTextAreaTabLayout?.srwLayout?.resetSrw()
            topchatViewState?.chatTextAreaTabLayout?.resetTab()
        }, onOldDesign = {
            rvSrw?.hideSrw()
            rvSrw?.resetSrw()
        })
    }

    private fun getImagePickerResult(data: Intent): List<String> {
        return ImagePickerResultExtractor.extract(data).imageUrlOrPathList
    }

    private fun processImagePathToUpload(imagePathList: List<String>): ImageUploadUiModel? {
        if (imagePathList.isEmpty()) {
            return null
        }
        val imagePath = imagePathList[0]

        if (!TextUtils.isEmpty(imagePath)) {
            val preview = generateChatViewModelWithImage(imagePath)
            replyCompose?.clearReferredComposedMsg()
            return preview
        }
        return null
    }

    private fun generateChatViewModelWithImage(imageUrl: String): ImageUploadUiModel {
        return ImageUploadUiModel.Builder()
            .withRoomMetaData(viewModel.roomMetaData)
            .withAttachmentType(AttachmentType.Companion.TYPE_IMAGE_UPLOAD)
            .withReplyTime(generateCurrentReplyTime())
            .withStartTime(SendableUiModel.generateStartTime())
            .withIsDummy(true)
            .withImageUrl(imageUrl)
            .withParentReply(replyCompose?.referredMsg)
            .withLocalId(IdentifierUtil.generateLocalId())
            .build()
    }

    private fun showDialogConfirmToAbortUpload() {
        context?.let {
            topChatRoomDialog.createAbortUploadImage(it) {
                finishActivity()
            }.show()
        }
    }

    override fun onClickBuyFromProductAttachment(element: ProductAttachmentUiModel) {
        if (element.isSupportVariant) {
            showAtcVariantHelper(element.productId, element.shopId.toString())
        } else if (isOCCActive() && element.isEligibleOCC()) {
            doOCC(element)
        } else {
            val addToCartParam = AddToCartParam.mapUiModelToParam(
                element, AddToCartParam.SOURCE_ACTION_BUY
            )
            viewModel.addProductToCart(addToCartParam)
        }
    }

    private fun onSuccessClickBuyFromProductAttachment(element: AddToCartParam) {
        analytics.trackSuccessDoBuyAndAtc(
            element, element.dataModel,
            topchatViewState?.chatRoomViewModel?.shopName ?: "",
            element.getBuyEventAction()
        )
        val intent = RouteManager.getIntent(context, ApplinkConst.CART)
        startActivity(intent)
    }

    override fun onClickATCFromProductAttachment(element: ProductAttachmentUiModel) {
        if (element.isSupportVariant) {
            showAtcVariantHelper(element.productId, element.shopId.toString())
        } else {
            val addToCartParam = AddToCartParam.mapUiModelToParam(
                element, AddToCartParam.SOURCE_ACTION_ATC
            )
            viewModel.addProductToCart(addToCartParam)
        }
    }

    private fun onSuccessClickATCFromProductAttachment(element: AddToCartParam) {
        analytics.trackSuccessDoBuyAndAtc(
            element, element.dataModel,
            topchatViewState?.chatRoomViewModel?.shopName ?: "",
            element.getAtcEventAction()
        )
        val msg = element.dataModel?.message?.getOrNull(0) ?: ""
        rvContainer?.let { view ->
            Toaster.build(
                view,
                msg,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                view.context.getString(R.string.title_topchat_see_cart)
            ) {
                analytics.eventClickSeeButtonOnAtcSuccessToaster()
                RouteManager.route(context, ApplinkConst.CART)
            }.show()
        }
    }

    private fun doOCC(element: ProductAttachmentUiModel) {
        viewModel.occProduct(getUserSession().userId, element)
    }

    private fun showAtcVariantHelper(
        productId: String,
        shopId: String
    ) {
        context?.let { ctx ->
            AtcVariantHelper.goToAtcVariant(
                context = ctx,
                productId = productId,
                pageSource = VariantPageSource.TOPCHAT_PAGESOURCE,
                isTokoNow = interlocutorShopType == SHOP_TYPE_TOKONOW,
                shopId = shopId,
                startActivitResult = { intent, requestCode ->
                    startActivityForResult(intent, requestCode)
                }
            )
        }
    }

    override fun onGoToShop() {
        val intent = RouteManager.getIntent(
            activity, ApplinkConst.SHOP.replace(
                "{shop_id}", shopId
                    .toString()
            )
        )
        startActivityForResult(intent, REQUEST_GO_TO_SHOP)
    }

    override fun followUnfollowShop(actionFollow: Boolean) {
        analytics.eventFollowUnfollowShop(actionFollow, shopId.toString())
        viewModel.followUnfollowShop(shopId.toString())
    }

    private fun onErrorFollowUnfollowShop(throwable: Throwable) {
        context?.let {
            showSnackbarError(ErrorHandler.getErrorMessage(it, throwable))
        }
    }

    private fun onSuccessFollowUnfollowShop(result: Boolean) {
        if (result) {
            val isFollow = topchatViewState?.isShopFollowed == false
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

    override fun onDeleteConversation() {
        showLoading()
        viewModel.deleteChat(messageId)
    }

    private fun onSuccessDeleteConversation() {
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
        topchatViewState?.showConfirmationBlockChat()
    }

    override fun blockChat() {
        viewModel.toggleBlockChatPromo(messageId, BlockActionType.BlockChat)
    }

    private fun onSuccessToggleBlockChat(blockStatus: Boolean, @StringRes toasterText: Int) {
        topchatViewState?.setChatBlockStatus(blockStatus)
        topchatViewState?.onCheckChatBlocked(
            opponentRole,
            opponentName,
            topchatViewState?.blockStatus ?: BlockedStatus()
        )
        context?.let {
            showToasterConfirmation(it.getString(toasterText))
        }
    }

    private fun onErrorToggleBlockChat(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToasterError(errorMessage)
    }

    override fun unBlockChat() {
        viewModel.toggleBlockChatPromo(messageId, BlockActionType.UnblockChat)
        analytics.trackClickUnblockChat(shopId)
    }

    override fun onGoToChatSetting() {
        analytics.eventClickChatSetting(shopId)
        RouteManager.route(context, ApplinkConstInternalMarketplace.CHAT_SETTING)
    }

    private fun getChatReportUrl(): String {
        var url = "${TkpdBaseURL.CHAT_REPORT_URL}$messageId"
        if (isSeller()) {
            url += "?isSeller=1"
        }
        return url
    }

    override fun onDualAnnouncementClicked(
        redirectUrl: String, attachmentId: String, blastId: String
    ) {
        analytics.trackClickImageAnnouncement(blastId, attachmentId)
        if (redirectUrl.isNotEmpty()) {
            onGoToWebView(redirectUrl, attachmentId)
        }
    }

    override fun onVoucherClicked(data: TopChatVoucherUiModel, source: String) {
        TopChatAnalyticsKt.eventVoucherThumbnailClicked(source, data.voucher.voucherId)
        if (data.isLockToProduct()) {
            goToMvcPage(data.applink)
        } else {
            goToMerchantVoucherDetail(data)
        }
    }

    override fun onVoucherSeen(data: TopChatVoucherUiModel, source: String) {
        if (seenAttachmentVoucher.add(data.voucher.voucherId.toString())) {
            TopChatAnalyticsKt.eventViewVoucher(source, data.voucher.voucherId)
        }
    }

    private fun goToMvcPage(applink: String) {
        //If seller in MA, show toaster
        if(!GlobalConfig.isSellerApp() && isSeller()) {
            view?.let {
                val text = getStringResource(R.string.topchat_mvc_not_available)
                Toaster.build(it, text).show()
            }
        //If applink is empty or wrong applink in sellerapp, return
        } else if (applink.isEmpty() || GlobalConfig.isSellerApp() &&
            !applink.contains(PREFIX_SELLER_APPLINK, ignoreCase = true)) {
            return
        } else {
            context?.let {
                val intent = RouteManager.getIntent(it, applink)
                startActivity(intent)
            }
        }
    }

    private fun goToMerchantVoucherDetail(data: TopChatVoucherUiModel) {
        activity?.let {
            val intent = MerchantVoucherDetailActivity.createIntent(
                it, data.voucher.voucherId,
                data.voucher, shopId.toString()
            )
            startActivityForResult(intent, MerchantVoucherListFragment.REQUEST_CODE_MERCHANT_DETAIL)
        }
    }

    override fun onBackPressed(): Boolean {
        if (super.onBackPressed()) return true
        if (::viewModel.isInitialized && viewModel.isUploading()) {
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
            bundle.putBoolean(
                TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP,
                isMoveItemInboxToTop
            )
            bundle.putParcelable(
                TopChatInternalRouter.Companion.RESULT_LAST_ITEM,
                topchatViewState?.getLastItem()
            )
            bundle.putString(ApplinkConst.Chat.MESSAGE_ID, messageId)
            bundle.putInt(
                TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX,
                indexFromInbox
            )
            isFavoriteShop?.let {
                bundle.putString(ApplinkConst.Chat.SHOP_FOLLOWERS_CHAT_KEY, it.toString())
            }
            intent.putExtras(bundle)
            it.setResult(RESULT_OK, intent)
            it.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterUploadImageReceiver()
    }

    override fun trackSeenProduct(element: ProductAttachmentUiModel) {
        if (seenAttachedProduct.add(element.productId)) {
            analytics.eventSeenProductAttachment(requireContext(), element, session, amISeller)
        }
    }

    override fun trackSeenBannedProduct(uiModel: BannedProductAttachmentUiModel) {
        if (seenAttachedBannedProduct.add(uiModel.productId)) {
            analytics.eventSeenBannedProductAttachment(uiModel)
        }
    }

    override fun onClickInvoiceThumbnail(url: String, id: String) {
        onGoToWebView(url, id)
    }

    override fun trackClickInvoice(uiModel: AttachInvoiceSentUiModel) {
        analytics.trackClickInvoice(uiModel)
    }

    private fun getStringArgument(key: String, savedInstanceState: Bundle?): String {
        return getParamString(key, arguments, savedInstanceState)
    }

    private fun getBooleanArgument(key: String, savedInstanceState: Bundle?): Boolean {
        return getParamBoolean(key, arguments, savedInstanceState, false)
    }

    override fun focusOnReply() {
        topchatViewState?.focusOnReply()
    }

    override fun showAttachmentPreview(attachmentPreview: ArrayList<SendablePreview>) {
        topchatViewState?.showAttachmentPreview(attachmentPreview)
        adapter.collapseSrwBubble()
        reloadSrw()
    }

    override fun onEmptyProductPreview() {
        viewModel.clearAttachmentPreview()
    }

    override fun clearAttachmentPreviews() {
        if (viewModel.isAttachmentPreviewReady()) {
            topchatViewState?.clearAttachmentPreview()
            viewModel.clearAttachmentPreview()
        }
        topchatViewState?.shouldShowSrw = false
    }

    override fun sendAnalyticAttachmentSent(attachment: SendablePreview) {
        if (attachment is InvoicePreviewUiModel) {
            analytics.invoiceAttachmentSent(attachment)
        } else if (attachment is TopchatProductAttachmentPreviewUiModel) {
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
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATTACH_INVOICE).apply {
                putExtra(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, messageId)
                putExtra(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME, opponentName)
            }
        startActivityForResult(intent, REQUEST_ATTACH_INVOICE)
    }

    override fun onClickBannedProduct(uiModel: BannedProductAttachmentUiModel) {
        analytics.eventClickBannedProduct(uiModel)
        viewModel.onClickBannedProduct(uiModel.liteUrl)
    }

    override fun redirectToBrowser(url: String) {
        if (url.isEmpty()) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun getSupportChildFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun onClickAddToWishList(product: ProductAttachmentUiModel, success: () -> Unit) {
        val productId = product.productId
        if (product.isWishListed()) {
            goToWishList()
        } else {
            analytics.eventClickAddToWishList(productId)
            requestNetworkAddToWishList(productId, success)
        }
    }

    private fun requestNetworkAddToWishList(productId: String, success: () -> Unit) {
        context?.let {
            if (WishlistV2RemoteConfigRollenceUtil.isUsingAddRemoveWishlistV2(it)) addToWishlistV2(productId, success)
            else addToWishlist(productId, success)
        }
    }

    private fun addToWishlist(productId: String, success: () -> Unit) {
        viewModel.addToWishList(productId, session.userId, object : WishListActionListener {
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
            override fun onSuccessAddWishlist(productId: String?) {
                success()
                showSuccessToastWishList(R.string.title_topchat_success_atw)
            }

            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                if (errorMessage == null) return
                view?.let {
                    Toaster.make(it, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun showSuccessToastWishList(@StringRes successMessageRes: Int) {
        view?.let {
            val successMessage = it.context.getString(successMessageRes)
            Toaster.build(
                it,
                successMessage,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL
            ).show()
        }
    }

    private fun addToWishlistV2(productId: String, success: () -> Unit) {
        viewModel.addToWishListV2(productId, session.userId, object : WishlistV2ActionListener {
            override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                view?.let { v ->
                    Toaster.build(v, ErrorHandler.getErrorMessage(context, throwable), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                }
            }

            override fun onSuccessAddWishlist(
                result: AddToWishlistV2Response.Data.WishlistAddV2,
                productId: String
            ) {
                context?.let { context ->
                    view?.let { v ->
                        AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, v)
                    }
                }
            }

            override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
            override fun onSuccessRemoveWishlist(result: DeleteWishlistV2Response.Data.WishlistRemoveV2, productId: String) { }
        })
    }

    private fun goToWishList() {
        val intent = RouteManager.getIntent(context, ApplinkConst.NEW_WISHLIST)
        startActivity(intent)
    }

    override fun onClickRemoveFromWishList(productId: String, success: () -> Unit) {
        analytics.eventClickRemoveFromWishList(productId)
        context?.let {
            if (WishlistV2RemoteConfigRollenceUtil.isUsingAddRemoveWishlistV2(it)) removeFromWishlistV2(productId)
            else removeFromWishlist(productId, success)
        }
    }

    private fun removeFromWishlist(productId: String, success: () -> Unit) {
        viewModel.removeFromWishList(productId, session.userId, object : WishListActionListener {
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

    private fun removeFromWishlistV2(productId: String) {
        viewModel.removeFromWishListV2(productId, session.userId, object : WishlistV2ActionListener {
            override fun onErrorAddWishList(throwable: Throwable, productId: String) {}
            override fun onSuccessAddWishlist(result: AddToWishlistV2Response.Data.WishlistAddV2, productId: String) {}

            override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                view?.let {
                    val errorMsg = ErrorHandler.getErrorMessage(context, throwable)
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, it)
                }
            }

            override fun onSuccessRemoveWishlist(
                result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                productId: String
            ) {
                context?.let { context ->
                    view?.let { v ->
                        AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(result, context, v)
                    }
                }
            }
        })
    }

    override fun trackClickProductThumbnail(product: ProductAttachmentUiModel) {
        analytics.eventClickProductThumbnail(product)
    }

    override fun onStickerOpened() {
        topchatViewState?.onStickerOpened()
    }

    override fun onStickerClosed() {
        topchatViewState?.onStickerClosed()
    }

    override fun getFragmentActivity(): FragmentActivity? {
        return activity
    }

    override fun getLoadedChatAttachments(): ArrayMap<String, Attachment> {
        return viewModel.attachments
    }

    override fun isSeller(): Boolean {
        return amISeller
    }

    override fun getSearchQuery(): String {
        return searchQuery
    }

    override fun requestFollowShop(element: BroadcastSpamHandlerUiModel) {
        viewModel.followUnfollowShop(
            action = ToggleFavouriteShopUseCase.Action.FOLLOW,
            shopId = shopId.toString(),
            element = element
        )
    }

    private fun onSuccessRequestFollow(element: BroadcastSpamHandlerUiModel) {
        element.stopFollowShop()
        onSuccessFollowShopFromBcHandler()
        adapter.removeBroadcastHandler(element)
        isFavoriteShop = true
    }

    private fun onErrorRequestFollow(element: BroadcastSpamHandlerUiModel, throwable: Throwable) {
        element.stopFollowShop()
        onErrorFollowShopFromBcHandler(throwable)
        adapter.updateBroadcastHandlerState(element)
    }

    override fun requestBlockPromo(element: BroadcastSpamHandlerUiModel?) {
        viewModel.toggleBlockChatPromo(messageId, BlockActionType.BlockPromo)
    }

    private fun onSuccessBlockPromo(
        response: ChatSettingsResponse,
        element: BroadcastSpamHandlerUiModel?
    ) {
        topchatViewState?.setChatPromoBlockStatus(
            true,
            response.chatBlockResponse.chatBlockStatus.validDate
        )
        element?.stopBlockPromo()
        onSuccessBlockPromoFromBcHandler(response)
        element?.let {
            adapter.removeBroadcastHandler(it)
        }
    }

    private fun onFailBlockPromo(throwable: Throwable, element: BroadcastSpamHandlerUiModel?) {
        element?.stopBlockPromo()
        onErrorBlockPromoFromBcHandler(throwable)
        element?.let {
            adapter.updateBroadcastHandlerState(it)
        }
    }

    private fun requestAllowPromo() {
        viewModel.toggleBlockChatPromo(messageId, BlockActionType.UnblockPromo)
    }

    private fun onSuccessAllowPromoFromBcHandler() {
        topchatViewState?.setChatPromoBlockStatus(false)
        addBroadCastSpamHandler(topchatViewState?.isShopFollowed ?: false)
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
        topchatViewState?.isShopFollowed = true
        context?.let {
            showToasterConfirmation(it.getString(R.string.title_success_follow_shop))
        }
    }

    private fun onSuccessUnFollowShopFromBcHandler() {
        topchatViewState?.isShopFollowed = false
        context?.let {
            showToasterConfirmation(it.getString(R.string.title_success_unfollow_shop))
        }
    }

    private fun onErrorFollowShopFromBcHandler(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToasterError(errorMessage)
    }

    private fun showToasterMsg(@StringRes messageRes: Int) {
        view?.let {
            val msg = it.context.getString(messageRes)
            showToasterMsg(msg)
        }
    }

    private fun showToasterMsg(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showToasterError(@StringRes messageRes: Int) {
        view?.let {
            val msg = it.context.getString(messageRes)
            showToasterError(msg)
        }
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    private fun showToasterConfirmation(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, "Oke")
                .show()
        }
    }

    private fun initProductPreview(savedInstanceState: Bundle?) {
        val stringProductIds =
            getStringArgument(ApplinkConst.Chat.PRODUCT_PREVIEWS, savedInstanceState)
        if (stringProductIds.isEmpty()) return
        val listType = object : TypeToken<List<String>>() {}.type
        val productIds = CommonUtil.fromJson<List<String>>(
            stringProductIds,
            listType
        )
        if (productIds.isNotEmpty()) {
            viewModel.loadProductPreview(productIds)
        }
    }

    private fun initInvoicePreview(savedInstanceState: Bundle?): Boolean {
        val id = getInvoicePreviewId(savedInstanceState)
        val invoiceCode = getStringArgument(ApplinkConst.Chat.INVOICE_CODE, savedInstanceState)
        val productName = getStringArgument(ApplinkConst.Chat.INVOICE_TITLE, savedInstanceState)
        val date = getStringArgument(ApplinkConst.Chat.INVOICE_DATE, savedInstanceState)
        val imageUrl = getStringArgument(ApplinkConst.Chat.INVOICE_IMAGE_URL, savedInstanceState)
        val invoiceUrl = getStringArgument(ApplinkConst.Chat.INVOICE_URL, savedInstanceState)
        val statusId = getStringArgument(ApplinkConst.Chat.INVOICE_STATUS_ID, savedInstanceState)
        val status = getStringArgument(ApplinkConst.Chat.INVOICE_STATUS, savedInstanceState)
        val totalPriceAmount =
            getStringArgument(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, savedInstanceState)
        val invoiceViewModel = InvoicePreviewUiModel(
            id,
            invoiceCode,
            productName,
            date,
            imageUrl,
            invoiceUrl,
            statusId.toIntOrNull() ?: InvoicePreviewUiModel.INVALID_STATUS_ID,
            status,
            totalPriceAmount
        )
        return if (invoiceViewModel.enoughRequiredData()) {
            viewModel.clearAttachmentPreview()
            viewModel.addAttachmentPreview(invoiceViewModel)
            true
        } else {
            false
        }
    }

    private fun getInvoicePreviewId(savedInstanceState: Bundle?): String {
        val id = getStringArgument(ApplinkConst.Chat.INVOICE_ID, savedInstanceState)
        return if (id.toLongOrNull() == null) {
            InvoicePreviewUiModel.INVALID_ID
        } else {
            id
        }
    }

    private fun initVoucherPreview(extras: Bundle?) {
        val stringVoucherPreview =
            getStringArgument(ApplinkConst.AttachVoucher.PARAM_VOUCHER_PREVIEW, extras)
        if (stringVoucherPreview.isEmpty()) return
        val voucherPreview =
            CommonUtil.fromJson<VoucherPreview>(stringVoucherPreview, VoucherPreview::class.java)
        val sendableVoucher = SendableVoucherPreviewUiModel(voucherPreview)
        if (!viewModel.hasEmptyAttachmentPreview()) {
            viewModel.clearAttachmentPreview()
        }
        viewModel.addAttachmentPreview(sendableVoucher)
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
            val image = intent.getParcelableExtra<ImageUploadServiceModel>(
                UploadImageChatService.IMAGE
            )
            image?.let {
                adapter.removePreviewMsg(it.localId)
            }
        }
    }

    override fun onErrorUploadImageWithService(intent: Intent) {
        if (messageId == getResultMessageId(intent)) {
            val errorMessage = intent.getStringExtra(UploadImageChatService.ERROR_MESSAGE) ?: ""
            val position = intent.getIntExtra(UploadImageChatService.RETRY_POSITION, -1)
            if (position > -1 && position < UploadImageChatService.dummyMap.size) {
                val dummyTarget = UploadImageChatService.dummyMap[position]
                onErrorUploadImage(errorMessage, dummyTarget.visitable as ImageUploadUiModel)
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
        if (viewModel.isInTheMiddleOfThePage()) {
            resetItemList()
            delaySendSrwQuestion(question)
            viewModel.getExistingChat(messageId)
        } else {
            sendSrwQuestionPreview(question)
        }
    }

    override fun onClickSrwBubbleQuestion(
        products: List<SendablePreview>, question: QuestionUiModel
    ) {
        sendSrwQuestionBubble(products, question)
    }

    override fun trackClickSrwQuestion(question: QuestionUiModel) {
        val productIds2 = viewModel.getProductIdPreview()
        val trackProductIds = productIds2.joinToString(separator = ", ")
        analytics.eventClickSrw(shopId, session.userId, trackProductIds, question)
    }


    override fun trackClickSrwBubbleQuestion(
        products: List<SendablePreview>, question: QuestionUiModel
    ) {
        val productIds = products.filterIsInstance<TopchatProductAttachmentPreviewUiModel>()
            .map { it.productId }
        val trackProductIds = productIds.joinToString(separator = ", ")
        analytics.eventClickSrw(shopId, session.userId, trackProductIds, question)
    }

    private fun delaySendSrwQuestion(question: QuestionUiModel) {
        delaySendSrw = question
    }

    private fun sendSrwQuestionPreview(question: QuestionUiModel) {
        val referredMsg = replyCompose?.referredMsg
        onSendAndReceiveMessage()
        addSrwBubbleToChat()
        onSendingMessage(false).invoke()
        replyBubbleOnBoarding.dismiss()
        sendAttachmentPreviews(question.content)
        viewModel.sendMsg(question.content, question.intent, referredMsg)
        clearAttachmentPreviews()
        clearReferredMsg()
    }

    private fun sendSrwQuestion(attachment: HeaderCtaButtonAttachment) {
        onSendAndReceiveMessage()
        onSendingMessage(false).invoke()
        val question = viewModel.generateSrwQuestionUiModel(attachment)
        viewModel.sendMsg(
            message = question.content,
            intention = question.intent,
            referredMsg = null,
            products = attachment.ctaButton.generateSendableProductPreview()
        )
    }

    private fun addSrwBubbleToChat() {
        if (chatRoomFlexModeListener?.isFlexMode() != null) {
            addSrwBubble()
        }
    }

    private fun addSrwBubble() {
        expandSrwPreview()
        val srwState = if (isSrwNewDesign()) {
            topchatViewState?.chatTextAreaTabLayout?.srwLayout?.getStateInfo()
        } else {
            rvSrw?.getStateInfo()
        }
        val previews2 = viewModel.getAttachmentsPreview().toList()
        adapter.addSrwBubbleUiModel(srwState, previews2)
    }

    private fun sendSrwQuestionBubble(
        products: List<SendablePreview>,
        question: QuestionUiModel
    ) {
        onSendAndReceiveMessage()
        viewModel.sendMsg(
            message = question.content,
            intention = question.intent,
            products = products,
            referredMsg = null
        )
    }

    private fun String.ellipsize(maxChar: Int): String {
        return if (length > maxChar) {
            "${substring(0, maxChar)}..."
        } else {
            this
        }
    }

    override fun disableSendButton(isExceedLimit: Boolean) {
        val chatSendButton = getChatSendButton()
        chatSendButton?.background =
            MethodChecker.getDrawable(context, R.drawable.bg_topchat_send_btn_disabled)
        chatSendButton?.setOnClickListener {
            if (!isExceedLimit) {
                showSnackbarError(getString(R.string.topchat_desc_empty_text_box))
            }
        }
    }

    override fun enableSendButton() {
        val chatSendButton = getChatSendButton()
        chatSendButton?.background = MethodChecker.getDrawable(context, R.drawable.bg_topchat_send_btn)
        chatSendButton?.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private fun getChatSendButton(): IconUnify? {
        return if (topchatViewState?.isChatTabVisible() == true) {
            topchatViewState?.chatTextAreaTabLayout?.getSendButton()
        } else {
            sendButton
        }
    }

    private fun getComposeMessageArea(): ComposeMessageAreaConstraintLayout? {
        return if (topchatViewState?.isChatTabVisible() == true) {
            topchatViewState?.chatTextAreaTabLayout?.getComposeMessageArea()
        } else {
            composeMsgArea
        }
    }

    private fun initKeyboardListener(view: View) {
        TopChatKeyboardHandler(view, object :
            TopChatKeyboardHandler.OnKeyBoardVisibilityChangeListener {
            override fun onKeyboardShow() {
                if (chatRoomFlexModeListener?.isFlexMode() == true) {
                    chatBoxPadding?.show()
                    showSeparatedChatTemplateIfFlex()
                }
            }

            override fun onKeyboardHide() {
                hideSeparatedChatTemplate()
            }
        })
    }

    private fun viewStateShouldShowTemplate(): Boolean {
        val isLastMsgFromBroadcastAndIamBuyer = adapter.isLastMessageBroadcast() && !isSeller()
        return !isLastMsgFromBroadcastAndIamBuyer &&
                !shouldShowSrw() &&
                !adapter.isLastMsgSrwBubble()
    }

    private fun showSeparatedChatTemplateIfFlex() {
        if (viewStateShouldShowTemplate()) {
            chatRoomFlexModeListener?.getSeparatedTemplateChat()?.show()
        } else {
            hideSeparatedChatTemplate()
        }
    }

    private fun hideSeparatedChatTemplate() {
        chatRoomFlexModeListener?.getSeparatedTemplateChat()?.hide()
        chatBoxPadding?.hide()
    }

    fun toggleTemplateChatWhenFlex(toggle: Boolean) {
        if (chatRoomFlexModeListener?.isFlexMode() == true) {
            try {
                if (!toggle) {
                    topchatViewState?.hideTemplateChat()
                } else if (toggle && viewStateShouldShowTemplate()) {
                    topchatViewState?.showTemplateChat()
                }
            } catch (ignored: Exception) {
            }
        }
    }

    private fun removeAttachmentIfNecessary(savedInstance: Bundle?) {
        val isNecessary = getBooleanArgument(Constant.CHAT_REMOVE_ATTACHMENT, savedInstance)
        if (isNecessary) {
            topchatViewState?.clearAttachmentPreview()
        }
    }

    private fun setupObservers() {
        viewModel.messageId.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetMessageId(it.data)
                is Fail -> onError(it.throwable)
            }
        }

        viewModel.shopFollowing.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.shopInfoById.result.isNullOrEmpty()) {
                        onErrorGetShopFollowingStatus()
                    } else {
                        onSuccessGetShopFollowingStatus(it.data.isFollow)
                    }
                }
                is Fail -> onErrorGetShopFollowingStatus()
            }
        }

        viewModel.followUnfollowShop.observe(viewLifecycleOwner) {
            val element = it.first
            val result = it.second
            if (element != null) {
                when (result) {
                    is Success -> onSuccessRequestFollow(element)
                    is Fail -> onErrorRequestFollow(element, result.throwable)
                }
            } else {
                when (result) {
                    is Success -> onSuccessFollowUnfollowShop(result.data)
                    is Fail -> onErrorFollowUnfollowShop(result.throwable)
                }
            }
        }

        viewModel.addToCart.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    when (it.data.source) {
                        AddToCartParam.SOURCE_ACTION_ATC -> {
                            onSuccessClickATCFromProductAttachment(it.data)
                        }
                        AddToCartParam.SOURCE_ACTION_BUY -> {
                            onSuccessClickBuyFromProductAttachment(it.data)
                        }
                    }
                }
                is Fail -> {
                    it.throwable.message?.let { msg ->
                        showToasterError(msg)
                    }
                }
            }
        }

        viewModel.seamlessLogin.observe(viewLifecycleOwner) {
            redirectToBrowser(it)
        }

        viewModel.chatRoomSetting.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val widget = filterSettingToBeShown(it.data)
                    onSuccessLoadChatRoomSetting(widget)
                }
                is Fail -> {
                    //Do nothing
                }
            }
        }

        viewModel.orderProgress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> renderOrderProgress(it.data.chatOrderProgress)
                is Fail -> {
                    //Do nothing
                }
            }
        }

        viewModel.tickerReminder.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetTickerReminder(it.data)
                else -> {}
            }
        }

        viewModel.occProduct.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    topchatViewState?.chatRoomViewModel?.let { chatData ->
                        TopChatAnalyticsKt.eventClickOCCButton(
                            it.data, chatData, getUserSession().userId
                        )
                    }
                    goToOCC()
                }
                is Fail -> onError(it.throwable)
            }
        }

        viewModel.toggleBlock.observe(viewLifecycleOwner) {
            handleToggleBlock(it)
        }

        viewModel.chatDeleteStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessDeleteConversation()
                is Fail -> onError(it.throwable)
            }
        }

        viewModel.chatBackground.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> renderBackground(it.data)
                is Fail -> {
                    //Do nothing
                }
            }
        }

        viewModel.chatAttachments.observe(viewLifecycleOwner) {
            updateAttachmentsView(it)
        }

        viewModel.chatAttachmentsPreview.observe(viewLifecycleOwner) {
            updateAttachmentsPreview(it)
        }

        viewModel.chatListGroupSticker.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val listGroupSticker = it.data.first.chatListGroupSticker.list
                    val listUpdate = it.data.second
                    getChatMenuView()?.stickerMenu?.updateStickers(listGroupSticker, listUpdate)
                    topchatViewState?.chatTextAreaTabLayout?.updateStickers(
                        listGroupSticker,
                        listUpdate
                    )
                }
                is Fail -> {
                    //Do nothing
                }
            }
        }

        viewModel.srw.observe(viewLifecycleOwner) {
            handleSrw(onNewDesign = {
                topchatViewState?.chatTextAreaTabLayout?.srwLayout?.updateStatus(it)
            }, onOldDesign = {
                rvSrw?.updateStatus(it)
            })
            updateSrwPreviewState()
        }

        viewModel.existingChat.observe(viewLifecycleOwner) {
            val result = it.first
            val isInit = it.second
            when (result) {
                is Success -> {
                    if (isInit) {
                        onSuccessGetExistingChatFirstTime(
                            result.data.chatroomViewModel, result.data.chatReplies
                        )
                    } else {
                        onSuccessResetChatToFirstPage(
                            result.data.chatroomViewModel, result.data.chatReplies
                        )
                    }
                }
                is Fail -> {
                    if (isInit) {
                        onErrorInitiateData(result.throwable)
                    } else {
                        onErrorResetChatToFirstPage(result.throwable)
                    }
                    topchatViewState?.shouldShowSrw = false
                    topchatViewState?.isAbleToReply = false
                }
            }
        }

        viewModel.topChat.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetTopChat(it.data.chatroomViewModel, it.data.chatReplies)
                is Fail -> onErrorGetTopChat(it.throwable)
            }
        }

        viewModel.bottomChat.observe(viewLifecycleOwner) {

            when (it) {
                is Success -> onSuccessGetBottomChat(it.data.chatroomViewModel, it.data.chatReplies)
                is Fail -> onErrorGetBottomChat(it.throwable)
            }
        }

        viewModel.deleteBubble.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    adapter.deleteMsg(it.data)
                    showToasterMsg(R.string.topchat_success_delete_msg_bubble)
                }
                is Fail -> showToasterError(R.string.topchat_error_delete_msg_bubble)
            }
        }

        viewModel.isWebsocketError.observe(viewLifecycleOwner) {
            showErrorWebSocket(it)
        }

        viewModel.isTyping.observe(viewLifecycleOwner) { isTyping ->
            if (isTyping) {
                onReceiveStartTypingEvent()
            } else {
                onReceiveStopTypingEvent()
            }
        }

        viewModel.msgDeleted.observeForever(deleteMsgObserver)

        viewModel.msgRead.observe(viewLifecycleOwner) { replyTime ->
            onReceiveReadEvent()
        }

        viewModel.unreadMsg.observe(viewLifecycleOwner) { totalUnread ->
            if (totalUnread > 0) {
                showUnreadMessage(totalUnread)
            } else {
                hideUnreadMessage()
            }
        }

        viewModel.newMsg.observeForever(newMsgObserver)
        viewModel.removeSrwBubble.observeForever(srwRemovalObserver)

        viewModel.previewMsg.observe(viewLifecycleOwner) { preview ->
            showPreviewMsg(preview)
        }

        viewModel.showableAttachmentPreviews.observe(viewLifecycleOwner) { attachPreview ->
            if (attachPreview.isNotEmpty()) {
                showAttachmentPreview(attachPreview)
                if (hasProductPreviewShown()) {
                    focusOnReply()
                }
            } else {
                clearAttachmentPreviews()
            }
        }

        viewModel.attachmentSent.observe(viewLifecycleOwner) { attachment ->
            sendAnalyticAttachmentSent(attachment)
        }

        viewModel.failUploadImage.observe(viewLifecycleOwner) { image ->
            topchatViewState?.showRetryUploadImages(image, true)
        }

        viewModel.errorSnackbar.observe(viewLifecycleOwner) { error ->
            showSnackbarError(error)
        }

        viewModel.uploadImageService.observe(viewLifecycleOwner) { image ->
            uploadImage(image)
        }

        viewModel.templateChat.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetTemplate(it.data)
                is Fail -> onErrorGetTemplate()
            }
        }
    }

    private fun updateAttachmentsPreview(products: ArrayMap<String, Attachment>) {
        val previousReadyState = viewModel.isAttachmentPreviewReady()
        topchatViewState?.updateProductPreviews(products)
        val afterReadyState = viewModel.isAttachmentPreviewReady()
        if (!previousReadyState && afterReadyState) {
            reloadSrw()
        } else if (!afterReadyState) {
            //when fail to get attachment, update srw preview state to hide loading & srw
            updateSrwPreviewState()
        }
    }

    override fun onDestroyView() {
        viewModel.newMsg.removeObserver(newMsgObserver)
        viewModel.removeSrwBubble.removeObserver(srwRemovalObserver)
        viewModel.msgDeleted.removeObserver(deleteMsgObserver)
        super.onDestroyView()
        replyBubbleOnBoarding.flush()
        lifecycleScope.launch(Dispatchers.IO) {
            sellerReviewHelper.saveMessageId(messageId)
        }
        Toaster.onCTAClick = View.OnClickListener { }
    }

    protected open fun uploadImage(image: ImageUploadServiceModel) {
        context?.applicationContext?.let {
            UploadImageChatService.enqueueWork(
                it, image, viewModel.roomMetaData.msgId
            )
        }
    }

    private fun handleToggleBlock(item: WrapperChatSetting) {
        when (item.blockActionType) {
            BlockActionType.BlockChat -> {
                when (item.response) {
                    is Success -> onSuccessToggleBlockChat(
                        true, R.string.title_success_block_chat
                    )
                    is Fail -> onErrorToggleBlockChat((item.response as Fail).throwable)
                }
            }
            BlockActionType.UnblockChat -> {
                when (item.response) {
                    is Success -> onSuccessToggleBlockChat(
                        false, R.string.title_success_unblock_chat
                    )
                    is Fail -> onErrorToggleBlockChat((item.response as Fail).throwable)
                }
            }
            BlockActionType.BlockPromo -> {
                when (item.response) {
                    is Success -> onSuccessBlockPromo((item.response as Success).data, item.element)
                    is Fail -> onFailBlockPromo((item.response as Fail).throwable, item.element)
                }
            }
            BlockActionType.UnblockPromo -> {
                when (item.response) {
                    is Success -> onSuccessAllowPromoFromBcHandler()
                    is Fail -> onErrorAllowPromoFromBcHandler((item.response as Fail).throwable)
                }
            }
        }
    }

    private fun onSuccessGetTickerReminder(
        data: ReminderTickerUiModel
    ) {
        if (!data.isEnable) return
        val eligiblePosition = adapter.findTickerPosition(data.replyId)
        if (eligiblePosition == RecyclerView.NO_POSITION) return
        adapter.addElement(eligiblePosition, data)
        isTickerNotShownYet = false
        if (getFirstVisibleItemPosition() == 0 && eligiblePosition == 0) {
            rv?.smoothScrollToPosition(eligiblePosition)
        }
    }

    override fun changeAddress(attachment: HeaderCtaButtonAttachment) {
        changeAddressStack.push(attachment)
        showChangeAddressBottomSheet()
    }

    protected open fun showChangeAddressBottomSheet() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(getChangeAddressListener())
        chooseAddressBottomSheet.show(childFragmentManager, "")
    }

    protected fun getChangeAddressListener(
    ): ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
        return object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
            override fun onLocalizingAddressServerDown() {

            }

            override fun onAddressDataChanged() {
                if (changeAddressStack.empty()) return
                val attachment = changeAddressStack.pop() ?: return
                val msg = context?.getString(
                    com.tokopedia.localizationchooseaddress.R.string.toaster_success_chosen_address
                ) ?: ""
                showToasterMsg(msg)
                initUserLocation()
                sendSrwQuestion(attachment)
            }

            override fun getLocalizingAddressHostSourceBottomSheet(): String {
                return "chat"
            }

            override fun onLocalizingAddressLoginSuccessBottomSheet() {

            }

            override fun onDismissChooseAddressBottomSheet() {
                if (changeAddressStack.empty()) return
                changeAddressStack.pop()
            }
        }
    }

    override fun showMsgMenu(
        msg: BaseChatUiModel, text: CharSequence, menus: List<Int>
    ) {
        replyBubbleOnBoarding.dismiss()
        val bs = TopchatBottomSheetBuilder.getLongClickBubbleMenuBs(
            context, msg, menus
        ) { itemMenu, msg ->
            TopChatAnalyticsKt.eventClickMsgMenu(itemMenu.title)
            when (itemMenu.id) {
                MENU_ID_REPLY -> replyCompose?.composeReplyData(msg, text, true)
                MENU_ID_COPY_TO_CLIPBOARD -> copyToClipboard(text)
                MENU_ID_DELETE_BUBBLE -> confirmDeleteBubble(msg)
            }
        }
        bs.show(childFragmentManager, BS_CHAT_BUBBLE_MENU)
    }

    private fun confirmDeleteBubble(msg: BaseChatUiModel) {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.topchat_title_delete_msg_bubble_confirmation))
                setDescription(it.getString(R.string.topchat_desc_delete_msg_bubble_confirmation))
                setPrimaryCTAText(it.getString(R.string.topchat_action_delete_msg_bubble_confirmation))
                setSecondaryCTAText(it.getString(R.string.cancel_bottom_sheet))
                setSecondaryCTAClickListener {
                    dismiss()
                }
                setPrimaryCTAClickListener {
                    TopChatAnalyticsKt.eventConfirmDeleteMsg(msg.replyId)
                    deleteBubble(msg)
                    dismiss()
                }
            }.show()
        }
    }

    private fun deleteBubble(msg: BaseChatUiModel) {
        val replyTimeNano = msg.replyTime ?: return
        viewModel.deleteMsg(viewModel.roomMetaData.msgId, replyTimeNano)
    }

    private fun onReceiveWsEventDeleteMsg(replyTimeNano: String) {
        adapter.deleteMsg(replyTimeNano)
    }

    override fun getCommonShopId(): String {
        return shopId
    }

    private fun copyToClipboard(text: CharSequence) {
        val clipboard = context?.getSystemService(
            CLIPBOARD_SERVICE
        ) as? ClipboardManager
        val clip = ClipData.newPlainText(CLIPBOARD_CHAT, text)
        clipboard?.setPrimaryClip(clip)
    }

    override fun getUserName(senderId: String): String {
        return viewModel.roomMetaData.userIdMap[senderId]?.name ?: ""
    }

    override fun goToBubble(parentReply: ParentReply) {
        val bubblePosition = adapter.getBubblePosition(
            parentReply.localId, parentReply.replyTime
        )
        if (bubblePosition != RecyclerView.NO_POSITION) {
            smoothScroller?.targetPosition = bubblePosition
            rvLayoutManager?.startSmoothScroll(smoothScroller)
        } else {
            resetItemList()
            setupBeforeReplyTime(parentReply.replyTimeMillisOffset)
            loadInitialData()
        }
    }

    /**
     * Show SRW Tab Design & Hide Template
     */
    override fun onCloseReplyBubble() {
        if (isSrwNewDesign()) {
            topchatViewState?.chatTextAreaTabLayout?.srwLayout?.isForceToHide = false
            if (shouldShowSrw()) {
                topchatViewState?.shouldShowSrw = true
                topchatViewState?.hideTemplateChat()
            }
        }
    }

    /**
     * Hide SRW Tab Design & Show Template
     */
    override fun onShowReplyBubble() {
        if (isSrwNewDesign() && shouldShowSrw()) {
            topchatViewState?.chatTextAreaTabLayout?.srwLayout?.isForceToHide = true
            topchatViewState?.shouldShowSrw = false
            showTemplateChatIfReady()
        }
    }

    private fun filterSettingToBeShown(result: RoomSettingResponse): List<Visitable<TopChatTypeFactory>> {
        val widgets = arrayListOf<Visitable<TopChatTypeFactory>>()

        if (result.showBanner) {
            widgets.add(result.roomBanner)
        }

        if (result.showFraudAlert) {
            widgets.add(result.fraudAlert)
        }

        return widgets
    }

    override fun trackSeenTicker(element: ReminderTickerUiModel) {
        TopChatAnalyticsKt.eventViewTicker(
            element.getTickerFeature(),
            isSeller(),
            viewModel.roomMetaData.msgId,
            element.replyId
        )
    }

    override fun onClickLinkReminderTicker(element: ReminderTickerUiModel, linkUrl: String) {
        TopChatAnalyticsKt.eventClickLinkTicker(
            element.getTickerFeature(),
            isSeller(),
            viewModel.roomMetaData.msgId,
            element.replyId
        )

        if (linkUrl.isNotEmpty()) {
            val intent = RouteManager.getIntent(context, linkUrl)
            startActivity(intent)
        }
    }

    override fun onCloseReminderTicker(element: ReminderTickerUiModel, position: Int) {
        TopChatAnalyticsKt.eventClickCloseTicker(
            element.getTickerFeature(),
            isSeller(),
            viewModel.roomMetaData.msgId,
            element.replyId
        )
        viewModel.closeTickerReminder(element, isSeller())
        adapter.removeViewHolder(element, position)
        isTickerNotShownYet = true
    }

    private fun goToOCC() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT
        )
        startActivity(intent)
    }

    override fun onClickCtaProductBundling(element: ProductBundlingUiModel) {
        if (element.isBroadcast()) {
            TopChatAnalyticsKt.eventClickCtaOnProductBundlingBroadcast(
                element.blastId,
                element.productBundling.bundleStatus.toString(),
                element.productBundling.bundleId.toString(),
                getBroadcastSenderShopId(element),
                session.userId
            )
        } else {
            TopChatAnalyticsKt.eventClickProductBundlingCta(
                element.productBundling.bundleItem?.first()?.productId?: "",
                element.productBundling.bundleId?: ""
            )
        }

        if (!element.productBundling.ctaBundling?.buttonAndroidLink.isNullOrEmpty()) {
            context?.let {
                val intent = RouteManager.getIntent(it,
                    element.productBundling.ctaBundling?.buttonAndroidLink)
                startActivity(intent)
            }
        }
    }

    override fun onSeenProductBundling(element: ProductBundlingUiModel) {
        if (seenAttachmentProductBundling.add(element.productBundling.bundleId?: "")) {
            if (element.isBroadcast()) {
                TopChatAnalyticsKt.eventViewProductBundlingBroadcast(
                    element.blastId,
                    element.productBundling.bundleStatus.toString(),
                    element.productBundling.bundleId.toString(),
                    getBroadcastSenderShopId(element),
                    session.userId
                )
            } else {
                TopChatAnalyticsKt.eventViewProductBundling(
                    element.productBundling.bundleItem?.first()?.productId?: "",
                    element.productBundling.bundleId?: ""
                )
            }
        }
    }

    override fun onClickProductBundlingImage(item: BundleItem, element: ProductBundlingUiModel) {
        if (element.isBroadcast()) {
            TopChatAnalyticsKt.eventClickProductAttachmentOnProductBundlingBroadcast(
                element.blastId,
                element.productBundling.bundleStatus.toString(),
                element.productBundling.bundleId.toString(),
                item.productId,
                getBroadcastSenderShopId(element),
                session.userId
            )
        }

        if (item.androidUrl.isNotEmpty()) {
            context?.let {
                val intent = RouteManager.getIntent(it, item.androidUrl)
                startActivity(intent)
            }
        }
    }

    private fun getBroadcastSenderShopId(element: ProductBundlingUiModel): String {
        return if (element.isSender) {
            session.shopId
        } else {
            shopId
        }
    }

    private fun isUsingMediaPicker(): Boolean {
        return abTestPlatform.getString(ROLLENCE_ENABLE_MEDIA_PICKER) == ROLLENCE_ENABLE_MEDIA_PICKER
    }

    private fun initChatTextAreaLayout() {
        handleSrw(onNewDesign = {
            topchatViewState?.chatTextAreaTabLayout?.initReplyBox(
                this,
                this,
                this,
                this
            )
            topchatViewState?.chatTextAreaTabLayout?.initSrw(
                this,
                getSrwFrameLayoutListener()
            )
        }, onOldDesign = {
            rvSrw?.initialize(this, getSrwFrameLayoutListener())
        })
    }

    private fun isFromAnotherChat(savedInstanceState: Bundle?): Boolean {
        return getBooleanArgument(IS_FROM_ANOTHER_CALL, savedInstanceState)
    }

    private fun handleSrw(onNewDesign: () -> Unit, onOldDesign: () -> Unit) {
        if (isSrwNewDesign()) {
            onNewDesign.invoke()
        } else {
            onOldDesign.invoke()
        }
    }

    private fun isSrwNewDesign(): Boolean {
        return abTestPlatform.getString(AB_TEST_NEW_SRW, AB_TEST_OLD_SRW) == AB_TEST_NEW_SRW
    }

    companion object {
        const val PARAM_RATING = "rating"
        const val PARAM_UTM_SOURCE = "utmSource"
        const val REVIEW_SOURCE_TOPCHAT = "android_topchat"
        const val BS_CHAT_BUBBLE_MENU = "CHAT_BUBBLE_MENU"
        private const val EXTRA_SOURCE_STOCK = "chat"
        private const val CLIPBOARD_CHAT = "chat message"
        private const val MAX_SIZE_IMAGE_PICKER = 20360

        private const val REQUEST_GO_TO_SHOP = 111
        private const val TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 112
        private const val REQUEST_GO_TO_SETTING_TEMPLATE = 113
        private const val REQUEST_ATTACH_INVOICE = 116
        private const val REQUEST_ATTACH_VOUCHER = 117
        private const val REQUEST_REPORT_USER = 118
        private const val REQUEST_REVIEW = 119
        private const val REQUEST_UPDATE_STOCK = 120
        private const val REQUEST_CODE_IMAGE_MEDIA_PICKER = 121

        private const val ELLIPSIZE_MAX_CHAR = 20
        private const val PREFIX_SELLER_APPLINK = "sellerapp://"

        const val AB_TEST_NEW_SRW = "srw_new_design"
        const val AB_TEST_OLD_SRW = "control_variant"
        const val ROLLENCE_ENABLE_MEDIA_PICKER = "android_chat_picker"

        fun createInstance(bundle: Bundle): BaseChatFragment {
            return TopChatRoomFragment().apply {
                arguments = bundle
            }
        }
    }
}
