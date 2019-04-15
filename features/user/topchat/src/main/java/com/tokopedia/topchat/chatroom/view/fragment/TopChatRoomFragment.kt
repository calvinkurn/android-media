package com.tokopedia.topchat.chatroom.view.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.*
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
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactoryImpl
import com.tokopedia.topchat.chatroom.view.customview.TopChatRoomDialog
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewStateImpl
import com.tokopedia.topchat.chatroom.view.listener.*
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.InboxChatConstant.PARCEL
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.TopChatRouter
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author : Steven 29/11/18
 */

class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View
        , TypingListener, SendButtonListener, ImagePickerListener, ChatTemplateListener,
        HeaderMenuListener, DualAnnouncementListener, SecurityInfoListener,
        TopChatVoucherListener {

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        customMessage = getParamString(ApplinkConst.Chat.CUSTOM_MESSAGE, arguments, savedInstanceState)
        indexFromInbox = getParamInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, arguments, savedInstanceState)
        initView(view)
        loadInitialData()
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
            presenter.getTemplate()

            activity?.run {
                val data = Intent()
                data.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)

                if (indexFromInbox != -1) {
                    data.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, indexFromInbox)
                } else {
                    data.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MUST_REFRESH, true)
                }
            }

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
                    element.category, element.variant)

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
                    onAttachProductClicked(),
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

        activity?.let {
            val strings: ArrayList<String> = ArrayList()
            strings.add(imageUrl)
            val topChatRouter = (it.applicationContext as TopChatRouter)

            topChatRouter.openImagePreviewFromChat(it, strings, ArrayList(),
                    opponentName, replyTime)
        }

    }

    private fun onAttachProductClicked(): () -> Unit {
        return {
            val intent = TopChatInternalRouter.Companion.getAttachProductIntent(activity as Activity,
                    shopId.toString(),
                    "",
                    getUserSession().shopId == shopId.toString())
            startActivityForResult(intent, TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE)
        }
    }

    override fun clearEditText() {
        getViewState().clearEditText()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return TopChatRoomAdapter(TopChatTypeFactoryImpl(
                this,
                this,
                this,
                this,
                this,
                this,
                this))
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
            getViewState().onSendingMessage(messageId, getUserSession().userId, getUserSession()
                    .name, sendMessage, startTime)
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
        getViewState().minimizeTools()
    }

    override fun onStopTyping() {
        presenter.stopTyping()
    }

    override fun onSendClicked(message: String, generateStartTime: String) {
        presenter.sendMessage(messageId, message, generateStartTime, "", onSendingMessage(
                message, generateStartTime
        ))
    }

    override fun addTemplateString(message: String?) {
        message?.let {
            getViewState().addTemplateString(message)
        }
    }

    override fun goToSettingTemplate() {
        val intent = TemplateChatActivity.createInstance(context)
        activity?.let {
            startActivityForResult(intent, REQUEST_GO_TO_SETTING_TEMPLATE)
            it.overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out)
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
                presenter.getTemplate()
            }

            TopChatRoomActivity.REQUEST_CODE_CHAT_IMAGE -> {
                if (resultCode != Activity.RESULT_OK || data == null) {
                    return
                }
                processImagePathToUpload(data)?.let {
                    presenter.startUploadImages(it)
                }
            }

            TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE -> onProductAttachmentSelected(data)

            REQUEST_GO_TO_SHOP -> onReturnFromShopPage(resultCode, data)

            REQUEST_GO_TO_SETTING_CHAT -> onReturnFromChatSetting(resultCode, data)
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
        attachProductRetrieved(resultProducts)
    }

    private fun attachProductRetrieved(resultProducts: java.util.ArrayList<ResultProduct>) {

        analytics.trackSendProductAttachment()

        for (result in resultProducts) {
            val item = generateProductChatViewModel(result)
            getViewState().onSendProductAttachment(item)
            presenter.sendProductAttachment(messageId, result, item.startTime, opponentId)
        }
    }

    private fun generateProductChatViewModel(product: ResultProduct): ProductAttachmentViewModel {
        return ProductAttachmentViewModel(
                getUserSession().userId,
                product.productId,
                product.name,
                product.price,
                product.productUrl,
                product.productImageThumbnail,
                SendableViewModel.generateStartTime(),
                false)
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
            presenter.addProductToCart(router, element, onError(), onSuccessBuyFromProdAttachment(),
                    element.shopId)
        }
    }

    override fun onClickATCFromProductAttachment(element: ProductAttachmentViewModel) {
        activity?.let {
            val router = (it.application as TopChatRouter)
            presenter.addProductToCart(router, element, onError(), onSuccessAddToCart(),
                    element.shopId)
        }
    }

    private fun onSuccessAddToCart(): (addToCartResult: AddToCartResult) -> Unit {
        return {
            showSnackbarAddToCart(it)
        }
    }

    private fun showSnackbarAddToCart(it: AddToCartResult) {
        if(it.isSuccess) {
            ToasterNormal.make(view, it.message, ToasterNormal.LENGTH_LONG).show()
        }else {
            ToasterError.make(view, it.message, ToasterNormal.LENGTH_LONG).show()
        }
    }

    private fun onSuccessBuyFromProdAttachment(): (addToCartResult: AddToCartResult) -> Unit {
        return {
            showSnackbarAddToCart(it)
            activity?.startActivity((activity!!.application as TopChatRouter)
                    .getCartIntent(activity))
        }
    }

    override fun onGoToShop() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP.replace("{shop_id}", shopId
                .toString()))
        startActivityForResult(intent, REQUEST_GO_TO_SHOP)
    }

    override fun onDeleteConversation() {
        showLoading()
        presenter.deleteChat(messageId, onError(), onSuccessDeleteConversation())
    }

    private fun onSuccessDeleteConversation(): () -> Unit {
        return {
            hideLoading()
            activity?.run {
                val data = Intent()
                data.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)

                if (indexFromInbox != -1) {
                    data.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, indexFromInbox)
                } else {
                    data.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MUST_REFRESH, true)
                }
                setResult(TopChatInternalRouter.Companion.CHAT_DELETED_RESULT_CODE, data)
                finish()
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

    override fun onDualAnnouncementClicked(redirectUrl: String, attachmentId: String, blastId: Int) {
        analytics.trackClickImageAnnouncement(blastId.toString(), attachmentId)
        if (redirectUrl.isNotEmpty()) {
            onGoToWebView(redirectUrl, attachmentId)
        }
    }

    override fun onVoucherCopyClicked(voucherCode: String, messageId: String, replyId: String, blastId: String, attachmentId: String, replyTime: String?, fromUid: String?) {
        analytics.eventVoucherCopyClicked(voucherCode)
        presenter.copyVoucherCode(fromUid, replyId, blastId, attachmentId, replyTime)
        activity?.run{
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

    override fun onBackPressedEvent() {
        if (presenter.isUploading()) {
            showDialogConfirmToAbortUpload()
        } else {
            finishActivity()
        }
    }

    private fun finishActivity() {
        activity?.let {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putBoolean(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP, isMoveItemInboxToTop)
            bundle.putParcelable(PARCEL, getViewState().getLastItem())
            intent.putExtras(bundle)
            it.setResult(RESULT_OK, intent)
            it.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
