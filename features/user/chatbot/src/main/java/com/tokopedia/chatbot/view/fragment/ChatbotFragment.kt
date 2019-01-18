package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.ChatbotRouter
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactoryImpl
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.listener.ChatbotViewState
import com.tokopedia.chatbot.view.listener.ChatbotViewStateImpl
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 23/11/18.
 */
class ChatbotFragment : BaseChatFragment(), ChatbotContract.View,
        AttachedInvoiceSelectionListener, QuickReplyListener,
        ChatActionListBubbleListener, ChatRatingListener, TypingListener {

    val TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114
    val REQUEST_CODE_CHAT_IMAGE = 115

    @Inject
    lateinit var presenter: ChatbotPresenter

    @Inject
    lateinit var session: UserSessionInterface

    lateinit var replyEditText: EditText

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()

            chatbotComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun getUserSession(): UserSessionInterface {
        return session
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chatroom, container, false)
        replyEditText = view.findViewById(R.id.new_comment)
        return view
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return ChatbotAdapter(ChatbotTypeFactoryImpl(this,
                this, this, this,
                this, this, this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.viewState = ChatbotViewStateImpl(view, session, this,
                this, onAttachImageClicked(),
                (activity as BaseChatToolbarActivity).getToolbar(), adapter)
        viewState.initView()
        loadInitialData()
    }

    private fun onAttachImageClicked(): () -> Unit {
        return {
            activity?.let {
                val builder = ImagePickerBuilder(it.getString(R.string.choose_image),
                        intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY,
                                ImagePickerTabTypeDef.TYPE_CAMERA),
                        GalleryType.IMAGE_ONLY,
                        ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                        ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                        null,
                        true,
                        null,
                        null)
                val intent = ImagePickerActivity.getIntent(it, builder)
                startActivityForResult(intent, REQUEST_CODE_CHAT_IMAGE)
            }
        }
    }

    override fun loadInitialData() {
        showLoading()
        presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChatFirstTime())
        presenter.connectWebSocket(messageId)
    }

    private fun onSuccessGetExistingChatFirstTime(): (ChatroomViewModel) -> Unit {
        return {
            updateViewData(it)
            renderList(it.listChat, it.canLoadMore)
            getViewState().onSuccessLoadFirstTime(it)
            checkShowLoading(it.canLoadMore)
            presenter.sendReadEvent(messageId)
        }
    }

    private fun onSuccessGetPreviousChat(): (ChatroomViewModel) -> Unit {
        return {
            renderList(it.listChat, it.canLoadMore)
            checkShowLoading(it.canLoadMore)
        }
    }

    fun checkShowLoading(canLoadMore: Boolean) {
        if (canLoadMore) super.showLoading()
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            if (view != null) {
                ToasterError.make(view, ErrorHandler.getErrorMessage(view!!.context, it)).show()
            }
        }
    }

    override fun loadData(page: Int) {
        presenter.loadPrevious(messageId, page, onError(), onSuccessGetPreviousChat())
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        mapMessageToList(visitable)
        getViewState().onCheckToHideQuickReply(visitable)
    }

    private fun getViewState(): ChatbotViewState {
        return viewState as ChatbotViewState
    }

    private fun mapMessageToList(visitable: Visitable<*>) {
        when (visitable) {
            is QuickReplyListViewModel -> getViewState().onReceiveQuickReplyEvent(visitable)
            else -> super.onReceiveMessageEvent(visitable)
        }
    }

    override fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo) {
        val generatedInvoice = presenter.generateInvoice(invoiceLinkPojo, opponentId)
        getViewState().onShowInvoiceToChat(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, invoiceLinkPojo, generatedInvoice.startTime,
                opponentId)
    }

    private fun attachInvoiceRetrieved(selectedInvoice: InvoiceLinkPojo) {
        val generatedInvoice = presenter.generateInvoice(selectedInvoice, "")
        getViewState().onShowInvoiceToChat(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, selectedInvoice, generatedInvoice.startTime,
                opponentId)
    }

    override fun showSearchInvoiceScreen() {
        activity?.let {
            val intent = ChatbotInternalRouter.Companion.getAttachInvoiceIntent(it,
                    session.userId,
                    messageId.toInt())
            startActivityForResult(intent, TOKOPEDIA_ATTACH_INVOICE_REQ_CODE)
        }
    }

    override fun onQuickReplyClicked(quickReplyListViewModel: QuickReplyListViewModel,
                                     model: QuickReplyViewModel) {

        presenter.sendQuickReply(messageId, model, SendableViewModel.generateStartTime(), opponentId)

    }

    override fun onImageUploadClicked(imageUrl: String, replyTime: String) {

        activity?.let {

            val strings: ArrayList<String> = ArrayList()
            strings.add(imageUrl)
            val chatbotRouter = (it.applicationContext as ChatbotRouter)

            chatbotRouter.openImagePreviewFromChat(it, strings, ArrayList(),
                    opponentName, replyTime)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOKOPEDIA_ATTACH_INVOICE_REQ_CODE -> onSelectedInvoiceResult(resultCode, data)
            REQUEST_CODE_CHAT_IMAGE -> onPickedAttachImage(resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onPickedAttachImage(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        processImagePathToUpload(data)?.let {
            getViewState().onImageUpload(it)
            presenter.uploadImages(it, messageId, opponentId, onError())
        }
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
                opponentId,
                (System.currentTimeMillis() / 1000).toString(),
                imageUrl,
                SendableViewModel.generateStartTime()
        )
    }

    private fun onSelectedInvoiceResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.run {
                val selectedInvoice = data.getParcelableExtra<SelectedInvoice>(
                        ChatbotInternalRouter.Companion.TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY)
                attachInvoiceRetrieved(AttachInvoiceMapper.convertInvoiceToDomainInvoiceModel(selectedInvoice))
            }
        }
    }

    override fun onSendButtonClicked() {
        val sendMessage = replyEditText.text.toString()
        val startTime = SendableViewModel.generateStartTime()
        presenter.sendMessage(messageId, sendMessage, startTime, opponentId,
                onSendingMessage(sendMessage, startTime))
    }

    private fun onSendingMessage(sendMessage: String, startTime: String): () -> Unit {
        return {
            getViewState().onSendingMessage(messageId, getUserSession().userId, getUserSession()
                    .name, sendMessage, startTime)
            getViewState().scrollToBottom()
        }
    }

    override fun onChatActionBalloonSelected(selected: ChatActionBubbleViewModel, model: ChatActionSelectionBubbleViewModel) {
        presenter.sendActionBubble(messageId, selected, SendableViewModel.generateStartTime(), opponentId)
    }

    override fun onClickRating(element: ChatRatingViewModel, rating: Int) {
        presenter.sendRating(messageId, rating, element.replyTimeNano.toString(), onError(),
                onSuccessSendRating(rating, element))
    }

    private fun onSuccessSendRating(rating: Int, element: ChatRatingViewModel): (SendRatingPojo) ->
    Unit {
        return {
            (activity as Activity).run {
                (viewState as ChatbotViewState).onSuccessSendRating(it, rating, element, this,
                        onClickReasonRating(element.replyTimeNano.toString()))
            }
        }
    }

    private fun onClickReasonRating(timestamp: String): (String) -> Unit {
        return {
            (viewState as ChatbotViewState).onClickReasonRating()
            presenter.sendReasonRating(messageId, it, timestamp, onError(),
                    onSuccessSendReasonRating())
        }
    }

    private fun onSuccessSendReasonRating(): (String) -> Unit {
        return {
            ToasterNormal.make(view, it, ToasterNormal.LENGTH_LONG).show()
        }
    }

    override fun onGoToWebView(url: String, id: String) {
        val BASE_DOMAIN_SHORTENED = "tkp.me"

        if (url.isNotEmpty()) {
            KeyboardHandler.DropKeyboard(activity, view)
            val uri = Uri.parse(url)
            if (uri?.scheme != null) {
                val isTargetDomainTokopedia = uri.host != null
                        && uri.host.endsWith("tokopedia.com")
                val isTargetTkpMeAndNotRedirect = TextUtils.equals(uri.host, BASE_DOMAIN_SHORTENED)
                        && !TextUtils.equals(uri.encodedPath, "/r")
                val isNeedAuthToken = isTargetDomainTokopedia || isTargetTkpMeAndNotRedirect

                val urlWithSession = URLGenerator.generateURLSessionLogin(url,
                        session.deviceId,
                        session.userId)
                val applinkWebview = String.format("%s?url=%s", ApplinkConst.WEBVIEW, urlWithSession)
                if (isNeedAuthToken && RouteManager.isSupportApplink(activity, applinkWebview)) {
                    RouteManager.route(activity, applinkWebview)
                } else {
                    super.onGoToWebView(url, id)
                }

            }
        }
    }

    override fun onStartTyping() {

    }

    override fun onStopTyping() {

    }

    override fun onUploadUndersizedImage() {
        ToasterError.make(view, getString(R.string.undersize_image)).show()
    }

    override fun onUploadOversizedImage() {
        ToasterError.make(view, getString(R.string.oversize_image)).show()
    }

    override fun showSnackbarError(stringId: Int) {
        if (view != null) {
            ToasterError.make(view, getString(stringId), Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * To handle url manually to webview. In this case, to contact us.
     */
    override fun shouldHandleUrlManually(url: String): Boolean {
        return true
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
