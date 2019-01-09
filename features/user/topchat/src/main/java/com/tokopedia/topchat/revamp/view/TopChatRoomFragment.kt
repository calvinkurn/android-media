package com.tokopedia.topchat.revamp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.attachproduct.analytics.AttachProductAnalytics
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.topchat.R
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.revamp.di.DaggerChatComponent
import com.tokopedia.topchat.revamp.di.DaggerTopChatRoomComponent
import com.tokopedia.topchat.revamp.listener.TopChatContract
import com.tokopedia.topchat.revamp.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.revamp.view.listener.ImagePickerListener
import com.tokopedia.topchat.revamp.view.listener.SendButtonListener
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author : Steven 29/11/18
 */

class TopChatRoomFragment : BaseChatFragment(), TopChatContract.View
        , TypingListener, SendButtonListener, ImagePickerListener, ChatTemplateListener {

    @Inject
    lateinit var presenter: TopChatRoomPresenter

    @Inject
    lateinit var topChatRoomDialog: TopChatRoomDialog

    @Inject
    lateinit var analytics: TopChatAnalytics

    @Inject
    lateinit var session: UserSessionInterface

    private lateinit var alertDialog: Dialog

    val TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 112

    private lateinit var actionBox: View

    override fun getContext(): Context? {
        return activity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initView(view)
        loadInitialData()
    }

    override fun loadInitialData() {
        presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChatFirstTime())
        presenter.connectWebSocket(messageId)
    }


    private fun onSuccessGetExistingChatFirstTime(): (ChatroomViewModel) -> Unit {
        return {
            updateViewData(it)
            setCanLoadMore(it)
            getViewState().onSuccessLoadFirstTime(it)
            presenter.getTemplate()
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            showSnackbarError(ErrorHandler.getErrorMessage(view!!.context, it))
        }
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        //TODO create analytic class
//        TrackingUtils.sendGTMEvent(
//                new EventTracking(
//                        "clickInboxChat",
//                        "inbox-chat",
//                        "click on thumbnail",
//                        viewModel.getBlastId() + " - " + viewModel.getAttachmentId()
//                ).getEvent()
//        );
        super.onImageAnnouncementClicked(viewModel)
    }

    override fun onRetrySendImage(element: ImageUploadViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    }

    companion object {

        private const val POST_ID = "{post_id}"
        fun createInstance(bundle: Bundle): BaseChatFragment {
            val fragment = TopChatRoomFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun loadData(page: Int) {
        return
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chatroom, container, false)
    }

    fun initView(view: View?) {
        view?.let {
            super.viewState = TopChatViewStateImpl(
                    it,
                    presenter,
                    this,
                    this,
                    this,
                    this,
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

            hideLoading()
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

    override fun initInjector() {

        if (activity != null && (activity as Activity).application != null) {
            val chatRoomComponent = DaggerTopChatRoomComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()
            val chatComponent = DaggerChatComponent.builder().topChatRoomComponent(chatRoomComponent).build()
            chatComponent.inject(this)
            presenter.attachView(this)
        }
    }
//
//    override fun disableAction() {
//        getViewState().setActionable(false)
//    }
//
//    override fun showSnackbarError(string: Unit) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    override fun addDummyMessage(visitable: Visitable<*>) {
        getViewState().addMessage(visitable)
        getViewState().scrollDownWhenInBottom()
    }

    override fun removeDummy(visitable: Visitable<*>) {
        getViewState().removeDummy(visitable)
    }

    override fun onErrorUploadImage(errorMessage: String) {
        showSnackbarError(errorMessage)
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
            ToasterError.make(view, stringResource).show()
        }
    }

    private fun setCanLoadMore(chatroomViewModel: ChatroomViewModel) {
        if (chatroomViewModel.canLoadMore) {
            enableLoadMore()
        } else {
            disableLoadMore()
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun addTemplateString(message: String?) {
        message?.let {
            getViewState().addTemplateString(message)
        }
    }

    override fun goToSettingTemplate() {
        val intent = TemplateChatActivity.createInstance(context)
        activity?.run {
            startActivityForResult(intent, 100)
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
            val intent = ImagePickerActivity.getIntent(getContext(), builder)
            startActivityForResult(intent, TopChatRoomActivity.REQUEST_CODE_CHAT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> {
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

            TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE -> onProductAttachmentSelected(resultCode, data)
        }
    }

    private fun onProductAttachmentSelected(resultCode: Int, data: Intent?) {
        if (data == null)
            return

        if (!data.hasExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY))
            return

        val resultProducts: ArrayList<ResultProduct> = data.getParcelableArrayListExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
        attachProductRetrieved(resultProducts)
    }

    fun attachProductRetrieved(resultProducts: java.util.ArrayList<ResultProduct>) {

        analytics.trackSendProductAttachment()

        val msgId = arguments!!.getString(InboxMessageConstant.PARAM_MESSAGE_ID)
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
            val temp = generateChatViewModelWithImage(imagePath)
            return temp
        }
        return null
    }

    fun generateChatViewModelWithImage(imageUrl: String): ImageUploadViewModel {
        return ImageUploadViewModel(
                messageId,
                arguments!!.getString(BaseChatToolbarActivity.Companion.PARAM_SENDER_ID),
                (System.currentTimeMillis() / 1000).toString(),
                imageUrl,
                SendableViewModel.generateStartTime()
        )
    }

    fun showDialogConfirmToAbortUpload() {
        context?.run {
            topChatRoomDialog.createAbortUploadImage(
                    this, alertDialog,
                    View.OnClickListener {
                        activity?.onBackPressed()
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

    override fun onBackPressedEvent() {
        if (presenter.isUploading()) {
            showDialogConfirmToAbortUpload()
        } else {
            activity?.finish()
        }
    }
}