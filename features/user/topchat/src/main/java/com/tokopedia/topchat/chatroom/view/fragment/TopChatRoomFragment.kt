package com.tokopedia.topchat.chatroom.view.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.ImageMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.ProductMenu
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactoryImpl
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AttachedInvoiceViewHolder.InvoiceThumbnailListener
import com.tokopedia.topchat.chatroom.view.customview.TopChatRoomDialog
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewState
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewStateImpl
import com.tokopedia.topchat.chatroom.view.listener.*
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.TopChatRouter
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseSimpleWebViewActivity
import javax.inject.Inject

/**
 * @author : Steven 29/11/18
 */

class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View
        , TypingListener, SendButtonListener, ImagePickerListener, ChatTemplateListener,
        HeaderMenuListener, DualAnnouncementListener, SecurityInfoListener,
        TopChatVoucherListener, InvoiceThumbnailListener {

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
    var indexFromInbox = -1
    var isMoveItemInboxToTop = false

    val REQUEST_GO_TO_SHOP = 111
    val TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 112
    val REQUEST_GO_TO_SETTING_TEMPLATE = 113
    val REQUEST_GO_TO_SETTING_CHAT = 114
    val REQUEST_GO_TO_NORMAL_CHECKOUT = 115

    private var seenAttachedProduct = HashSet<Int>()
    private var seenAttachedBannedProduct = HashSet<Int>()

    protected var remoteConfig: RemoteConfig? = null

    companion object {

        private const val POST_ID = "{post_id}"
        fun createInstance(bundle: Bundle): BaseChatFragment {
            val fragment = TopChatRoomFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun getContext(): Context? {
        return activity
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val chatComponent = DaggerChatComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()
            chatComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fpm = PerformanceMonitoring.start(TopChatAnalytics.FPM_DETAIL_CHAT)
        remoteConfig = FirebaseRemoteConfigImpl(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        customMessage = getParamString(ApplinkConst.Chat.CUSTOM_MESSAGE, arguments, savedInstanceState)
        indexFromInbox = getParamInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, arguments, savedInstanceState)
        initView(view)
        loadInitialData()
        presenter.initProductPreview(savedInstanceState)
        presenter.initInvoicePreview(savedInstanceState)
        presenter.initAttachmentPreview()
    }

    override fun loadInitialData() {
        if (messageId.isNotEmpty()) {
            presenter.getExistingChat(messageId,
                    onErrorInitiateData(),
                    onSuccessGetExistingChatFirstTime())
            presenter.connectWebSocket(messageId)
        } else {
            presenter.getMessageId(toUserId,
                    toShopId,
                    source,
                    onError(),
                    onSuccessGetMessageId())
        }
    }

    private fun onUnblockChatClicked(): () -> Unit {
        return {
            analytics.trackClickUnblockChat(shopId)
            presenter.unblockChat(messageId, opponentRole, onError(), onSuccessUnblockChat())
        }
    }

    private fun onSuccessUnblockChat(): (BlockedStatus) -> Unit {
        return {
            ToasterNormal.make(view, String.format(getString(R.string.chat_unblocked_text),
                    opponentName), ToasterNormal.LENGTH_SHORT).show()
            getViewState().removeChatBlocked(it)
        }
    }

    private fun onSuccessGetMessageId(): (String) -> Unit {
        return {
            this.messageId = it
            loadInitialData()
        }
    }

    private fun onSuccessGetExistingChatFirstTime(): (ChatroomViewModel) -> Unit {
        return {
            updateViewData(it)
            presenter.connectWebSocket(messageId)
            presenter.getShopFollowingStatus(shopId, onErrorGetShopFollowingStatus(),
                    onSuccessGetShopFollowingStatus())

            renderList(it.listChat, it.canLoadMore)
            getViewState().onSuccessLoadFirstTime(it, onToolbarClicked(), this, alertDialog, onUnblockChatClicked())
            getViewState().onSetCustomMessage(customMessage)
            presenter.getTemplate(getUserSession().shopId == shopId.toString())

            fpm.stopTrace()
        }
    }

    private fun onErrorGetShopFollowingStatus(): (Throwable) -> Unit {
        return {
            getViewState().isShopFollowed = false
        }
    }

    private fun onSuccessGetShopFollowingStatus(): (Boolean) -> Unit {
        return {
            getViewState().isShopFollowed = it
        }
    }

    private fun onToolbarClicked(): () -> Unit {
        return {

            analytics.trackHeaderClicked()

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

    override fun showErrorWebSocket(b: Boolean) {
        getViewState().showErrorWebSocket(b)
    }

    private fun onSuccessGetPreviousChat(): (ChatroomViewModel) -> Unit {
        return {
            renderList(it.listChat, it.canLoadMore)
            checkShowLoading(it.canLoadMore)
        }
    }

    private fun checkShowLoading(canLoadMore: Boolean) {
        if (canLoadMore) super.showLoading()
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            hideLoading()
            showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, it))
        }
    }

    private fun onErrorInitiateData(): (Throwable) -> Unit {
        return {
            hideLoading()
            showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, it))
            fpm.stopTrace()
        }
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
        bottomSheetBuilder.addItem(InboxMessageConstant.RESEND, com.tokopedia.topchat.R.string.resend, null)
        bottomSheetBuilder.addItem(InboxMessageConstant.DELETE, com.tokopedia.topchat.R.string.delete, null)
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

        analytics.eventClickProductThumbnailEE(element.blastId,
                element.productId.toString(),
                element.productName,
                element.priceInt,
                element.category, element.variants.toString())

        analytics.trackProductAttachmentClicked()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        super.onReceiveMessageEvent(visitable)
        getViewState().scrollDownWhenInBottom()
        isMoveItemInboxToTop = true
    }

    override fun loadData(page: Int) {
        presenter.loadPreviousChat(messageId, page, onError(), onSuccessGetPreviousChat())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chatroom, container, false)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    fun initView(view: View?) {
        view?.let {
            super.viewState = TopChatViewStateImpl(
                    it,
                    this,
                    this,
                    this,
                    this,
                    this,
                    (activity as BaseChatToolbarActivity).getToolbar(),
                    analytics
            )

            if (!::alertDialog.isInitialized) {
                alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
            }

            hideLoading()
        }
    }

    override fun onImageUploadClicked(imageUrl: String, replyTime: String) {
        analytics.trackClickImageUpload()
        activity?.let {
            val strings: ArrayList<String> = ArrayList()
            strings.add(imageUrl)
            val topChatRouter = (it.applicationContext as TopChatRouter)

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
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        if (adapterTypeFactory !is TopChatTypeFactoryImpl) {
            throw IllegalStateException("getAdapterTypeFactory() must return TopChatTypeFactoryImpl")
        }
        val typeFactory = adapterTypeFactory as TopChatTypeFactoryImpl
        return TopChatRoomAdapter(typeFactory)
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
        val sendMessage = view?.findViewById<EditText>(R.id.new_comment)?.text.toString()
        val startTime = SendableViewModel.generateStartTime()

        presenter.sendMessage(messageId, sendMessage, startTime, opponentId, onSendingMessage
        (sendMessage, startTime))
    }

    private fun onSendingMessage(sendMessage: String, startTime: String): () -> Unit {
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
        if (view != null) {
            ToasterError.make(view, stringResource, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onStartTyping() {
        presenter.startTyping()
    }

    override fun onStopTyping() {
        presenter.stopTyping()
    }

    override fun onSendClicked(message: String, generateStartTime: String) {
        presenter.sendAttachmentsAndMessage(
                messageId,
                message,
                generateStartTime,
                "",
                onSendingMessage(message, generateStartTime)
        )
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
            val builder = ImagePickerBuilder(it.getString(com.tokopedia.topchat.R.string.choose_image),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA), GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, null, true, null, null)
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
                if (resultCode != Activity.RESULT_OK || data == null) {
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
        }
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
            val cartIntent = (activity!!.application as TopChatRouter).getCartIntent(activity)
            analytics.eventClickSeeButtonOnAtcSuccessToaster()
            activity?.startActivity(cartIntent)
        }
    }

    private fun onReturnFromChatSetting(resultCode: Int, data: Intent?) {
        data?.let {
            val blockedStatus = BlockedStatus(
                    it.getBooleanExtra(TopChatInternalRouter.Companion.RESULT_CHAT_SETTING_IS_BLOCKED, false),
                    it.getBooleanExtra(TopChatInternalRouter.Companion.RESULT_CHAT_SETTING_IS_PROMO_BLOCKED, false),
                    it.getStringExtra(TopChatInternalRouter.Companion.RESULT_CHAT_SETTING_BLOCKED_UNTIL)
            )

            if (resultCode == Activity.RESULT_OK) {
                getViewState().onCheckChatBlocked(opponentRole, opponentName,
                        blockedStatus, onUnblockChatClicked())
            }

        }

    }

    private fun onReturnFromShopPage(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            getViewState().isShopFollowed = data.getBooleanExtra(TopChatRouter
                    .EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP, false)
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
        activity?.let {
            val router = (it.application as TopChatRouter)
            (viewState as TopChatViewState)?.sendAnalyticsClickBuyNow(element)
            var shopId = this.shopId
            if (shopId == 0) {
                shopId = element.shopId
            }
            presenter.addProductToCart(router, element, onError(), onSuccessBuyFromProdAttachment(), shopId)
        }
    }

    override fun onClickATCFromProductAttachment(element: ProductAttachmentViewModel) {
        (viewState as TopChatViewState).sendAnalyticsClickATC(element)
        val atcPageIntent = presenter.getAtcPageIntent(context, element)
        startActivityForResult(atcPageIntent, REQUEST_GO_TO_NORMAL_CHECKOUT)
    }

    private fun showSnackbarAddToCart(it: AddToCartDataModel) {
        if (it.status.equals(AddToCartDataModel.STATUS_OK, true) && it.data.success == 1) {
            ToasterNormal.make(view, it.data.message[0], ToasterNormal.LENGTH_LONG).show()
        } else {
            ToasterError.make(view, it.errorMessage[0], ToasterNormal.LENGTH_LONG).show()
        }
    }

    private fun onSuccessBuyFromProdAttachment(): (addToCartResult: AddToCartDataModel) -> Unit {
        return {
            showSnackbarAddToCart(it)
            if (it.status.equals(AddToCartDataModel.STATUS_OK, true) && it.data.success == 1) {
                activity?.startActivity((activity!!.application as TopChatRouter)
                        .getCartIntent(activity))
            }
        }
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
                getViewState().isShopFollowed = !getViewState().isShopFollowed
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
                    blockedStatus.isPromoBlocked,
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
            val intent = BaseSimpleWebViewActivity.getStartIntent(it, reportUrl)
            startActivity(intent)
        }
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

    override fun onGoToSecurityInfo(url: String) {
        if (url.isNotEmpty()) {
            onGoToWebView(url, "")
        }
    }

    override fun onBackPressed(): Boolean {
        if (presenter.isUploading()) {
            showDialogConfirmToAbortUpload()
            return true
        }

        return super.onBackPressed()
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
    }

    override fun trackSeenProduct(element: ProductAttachmentViewModel) {
        if (seenAttachedProduct.add(element.productId)) {
            analytics.eventSeenProductAttachment(element)
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

    override fun notifyAttachmentsSent() {
        getViewState().clearAttachmentPreview()
    }

    override fun getShopName(): String {
        return opponentName
    }

    override fun sendAnalyticAttachmentSent(attachment: SendablePreview) {
        if (attachment is InvoicePreviewViewModel) {
            analytics.invoiceAttachmentSent(attachment)
        } else if (attachment is SendableProductPreview) {
            analytics.trackSendProductAttachment()
        }
    }

    override fun createAttachmentMenus(): List<AttachmentMenu> {
        return listOf(
                ProductMenu(),
                ImageMenu()
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

    override fun onClickBannedProduct(viewModel: BannedProductAttachmentViewModel) {
        analytics.eventClickBannedProduct(viewModel)
        presenter.onClickBannedProduct(viewModel.liteUrl)
    }

    override fun redirectToBrowser(url: String) {
        if (url.isEmpty()) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
