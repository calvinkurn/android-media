package com.tokopedia.topchat.revamp.view

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachproduct.analytics.AttachProductAnalytics
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
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.InboxChatConstant.PARCEL
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.common.TopChatRouter
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.revamp.di.DaggerChatComponent
import com.tokopedia.topchat.revamp.listener.TopChatContract
import com.tokopedia.topchat.revamp.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.revamp.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.revamp.view.adapter.TopChatTypeFactoryImpl
import com.tokopedia.topchat.revamp.view.listener.*
import com.tokopedia.user.session.UserSessionInterface

import javax.inject.Inject


/**
 * @author : Steven 29/11/18
 */

class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View
        , TypingListener, SendButtonListener, ImagePickerListener, ChatTemplateListener,
        HeaderMenuListener, DualAnnouncementListener, SecurityInfoListener {

    @Inject
    lateinit var presenter: TopChatRoomPresenter

    @Inject
    lateinit var topChatRoomDialog: TopChatRoomDialog

    @Inject
    lateinit var analytics: TopChatAnalytics

    @Inject
    lateinit var session: UserSessionInterface

    private lateinit var alertDialog: Dialog
    private lateinit var customMessage: String
    var indexFromInbox = -1

    val REQUEST_GO_TO_SHOP = 111
    val TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 112
    val REQUEST_GO_TO_SETTING_TEMPLATE = 113
    val REQUEST_GO_TO_SETTING_CHAT = 114


    private lateinit var actionBox: View

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
                    onError(),
                    onSuccessGetExistingChatFirstTime(),
                    onChatIsBlocked())
            presenter.connectWebSocket(messageId)
        } else {
            presenter.getMessageId(toUserId.toString(),
                    toShopId.toString(),
                    source,
                    onError(),
                    onSuccessGetMessageId())
        }
    }


    private fun onChatIsBlocked(): (ChatroomViewModel) -> Unit {
        return {
            getViewState().showChatBlocked(it.blockedStatus, it.headerModel.role, it.headerModel
                    .name, onUnblockChatClicked())
        }
    }

    private fun onUnblockChatClicked(): () -> Unit {
        return {
            analytics.trackClickUnblockChat()
            presenter.unblockChat(messageId, onError(), onSuccessUnblockChat())
        }
    }

    private fun onSuccessUnblockChat(): (BlockedStatus) -> Unit {
        return {
            getViewState().removeChatBlocked()
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
            presenter.connectWebSocket(messageId)
            updateViewData(it)
            renderList(it.listChat, it.canLoadMore)
            getViewState().onSuccessLoadFirstTime(it, onToolbarClicked(), this, alertDialog)
            presenter.getTemplate()

            activity?.run {
                val data = Intent()
                data.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)

                if (indexFromInbox != -1) {
                    data.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, indexFromInbox)
                } else {
                    data.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MUST_REFRESH, true)
                }
                setResult(TopChatInternalRouter.Companion.CHAT_READ_RESULT_CODE, data)
            }
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
            goToShop(shopId)
        }
    }

    private fun goToShop(shopId: Int) {
        RouteManager.route(activity, ApplinkConst.SHOP.replace("{shop_id}", shopId.toString()))
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

    override fun getScreenName(): String {
        return TopChatAnalytics.SCREEN_CHAT_ROOM
    }

    override fun getUserSession(): UserSessionInterface {
        return session
    }


    private fun getViewState(): TopChatViewStateImpl {
        return viewState as TopChatViewStateImpl
    }

    override fun developmentView() {
        val dummyList = arrayListOf<Visitable<*>>()

        dummyList.add(MessageViewModel("1", "1960918", "lawan", "User", "", "", "213123123", "213123123", true, false, false, "hi1"))
        dummyList.add(MessageViewModel("2", "7977933", "lawan", "User", "", "", "213123124", "213123123", true, false, true, "hi2"))
        dummyList.add(MessageViewModel("3", "1960918", "lawan", "User", "", "", "213123125", "213123123", true, false, false, "hi3"))
        dummyList.add(MessageViewModel("4", "7977933", "lawan", "User", "", "", "213123126", "213123123", true, false, true, "hi4"))
        dummyList.add(MessageViewModel("5", "1960918", "lawan", "User", "", "", "213123127", "213123123", true, false, false, "hi5"))
        dummyList.add(MessageViewModel("6", "7977933", "lawan", "User", "", "", "213123128", "213123123", true, false, true, "hi6"))
        dummyList.add(MessageViewModel("11", "1960918", "lawan", "User", "", "", "213123123", "213123123", true, false, false, "hi11"))
        dummyList.add(MessageViewModel("21", "7977933", "lawan", "User", "", "", "213123124", "213123123", true, false, true, "hi21"))
        dummyList.add(MessageViewModel("31", "1960918", "lawan", "User", "", "", "213123125", "213123123", true, false, false, "hi31"))
        dummyList.add(MessageViewModel("41", "7977933", "lawan", "User", "", "", "213123126", "213123123", true, false, true, "hi41"))
        dummyList.add(MessageViewModel("51", "1960918", "lawan", "User", "", "", "213123127", "213123123", true, false, false, "hi51"))
        dummyList.add(MessageViewModel("61", "7977933", "lawan", "User", "", "", "213123128", "213123123", true, false, true, "hi61"))

        getViewState().addList(dummyList)
        getViewState().hideLoading()
        getViewState().developmentView()
    }

    override fun onImageAnnouncementClicked(viewModel: ImageAnnouncementViewModel) {
        analytics.trackClickImageAnnouncement(viewModel.blastId.toString(), viewModel.attachmentId)
        super.onImageAnnouncementClicked(viewModel)
    }

    override fun onRetrySendImage(element: ImageUploadViewModel) {
        val bottomSheetBuilder = CheckedBottomSheetBuilder(activity).setMode(BottomSheetBuilder.MODE_LIST)
        bottomSheetBuilder.addItem(InboxMessageConstant.RESEND, com.tokopedia.topchat.R.string.resend, null)
        bottomSheetBuilder.addItem(InboxMessageConstant.DELETE, com.tokopedia.topchat.R.string.delete, null)
        var bottomSheetDialog = bottomSheetBuilder.expandOnStart(true).setItemClickListener(BottomSheetItemClickListener {
            when (it.itemId) {
                InboxMessageConstant.RESEND -> {
                    presenter.startUploadImages(element)
                    removeDummy(element)
                }
                InboxMessageConstant.DELETE -> {
                    removeDummy(element)
                }
            }
        }).createDialog().show()
    }

    override fun onProductClicked(element: ProductAttachmentViewModel) {
        super.onProductClicked(element)
        if (activity!!.applicationContext is AbstractionRouter) {
            val abstractionRouter = activity!!
                    .applicationContext as AbstractionRouter
            abstractionRouter.analyticTracker.sendEventTracking(
                    AttachProductAnalytics.getEventClickChatAttachedProductImage().event
            )
        }
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        super.onReceiveMessageEvent(visitable)
        getViewState().scrollDownWhenInBottom()
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
                    (activity as BaseChatToolbarActivity).getToolbar()
            )

            if (!::alertDialog.isInitialized) {
                alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
            }

            getViewState().onSetCustomMessage(customMessage)

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

//
//    override fun disableAction() {
//        getViewState().setActionable(false)
//    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return TopChatRoomAdapter(TopChatTypeFactoryImpl(
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
        getViewState().onSendingMessage(messageId, getUserSession().userId, getUserSession()
                .name, sendMessage, startTime)
        presenter.sendMessage(messageId, sendMessage, startTime, opponentId)
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
        presenter.sendMessage(messageId, message, generateStartTime, "")
    }

    override fun addTemplateString(message: String?) {
        message?.let {
            getViewState().addTemplateString(message)
        }
    }

    override fun goToSettingTemplate() {
        val intent = TemplateChatActivity.createInstance(context)
        activity?.run {
            startActivityForResult(intent, REQUEST_GO_TO_SETTING_TEMPLATE)
            overridePendingTransition(com.tokopedia.topchat.R.anim.pull_up, android.R.anim.fade_out)
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
                    //                    chatViewState.addMessage(it)
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
                    it.getBooleanExtra(TopChatInternalRouter.Companion
                            .RESULT_CHAT_SETTING_IS_BLOCKED, false),
                    it.getBooleanExtra(TopChatInternalRouter.Companion
                            .RESULT_CHAT_SETTING_IS_PROMO_BLOCKED, false),
                    it.getStringExtra(TopChatInternalRouter.Companion
                            .RESULT_CHAT_SETTING_BLOCKED_UNTIL)
            )

            when (resultCode) {
                TopChatInternalRouter.Companion.RESULT_CODE_CHAT_SETTINGS_DISABLED -> {
                    getViewState().showChatBlocked(blockedStatus, opponentRole, opponentName, onUnblockChatClicked())
                }
                TopChatInternalRouter.Companion.RESULT_CODE_CHAT_SETTINGS_ENABLED -> {
                    getViewState().removeChatBlocked()
                }
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
                arguments!!.getString(ApplinkConst.Chat.OPPONENT_ID),
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
        Log.d("NIS", "go to Buy")
    }

    override fun onClickATCFromProductAttachment(element: ProductAttachmentViewModel) {
        presenter.addProductToCart(element, onError(), onSuccessAddToCart())
    }

    private fun onSuccessAddToCart(): () -> Unit {
        return {
            ToasterNormal.make(view, getString(R.string.success_add_to_cart), ToasterNormal
                    .LENGTH_SHORT).show()
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
                    blockedStatus.blockedUntil)
            startActivityForResult(intent, REQUEST_GO_TO_SETTING_CHAT)
        }

        analytics.trackOpenChatSetting()

    }

    override fun onGoToDetailOpponentFromMenu() {
        analytics.trackGoToDetailFromMenu()
        goToDetailOpponent()
    }

    override fun onDualAnnouncementClicked(redirectUrl: String, attachmentId: String, blastId: Int) {
        analytics.trackClickImageAnnouncement(blastId.toString(), attachmentId)
        if (redirectUrl.isNotEmpty()) {
            onGoToWebView(redirectUrl, attachmentId)
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

    fun finishActivity() {
        activity?.let {
            var intent = Intent()
            var bundle = Bundle()
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