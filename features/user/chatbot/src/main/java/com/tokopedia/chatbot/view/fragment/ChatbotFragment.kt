package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.chat_common.view.adapter.viewholder.factory.ChatMenuFactory
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.Attributes
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.chatbot.view.activity.ChatBotProvideRatingActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactoryImpl
import com.tokopedia.chatbot.view.adapter.viewholder.factory.ChatBotChatMenuFactory
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.listener.ChatbotViewState
import com.tokopedia.chatbot.view.listener.ChatbotViewStateImpl
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.chatbot_layout_rating.view.*
import kotlinx.android.synthetic.main.fragment_chatbot.*
import javax.inject.Inject

/**
 * @author by nisie on 23/11/18.
 */
class ChatbotFragment : BaseChatFragment(), ChatbotContract.View,
        AttachedInvoiceSelectionListener, QuickReplyListener,
        ChatActionListBubbleListener, ChatRatingListener, TypingListener, View.OnClickListener,ChatbotActivity.OnBackPressed {

    override fun clearChatText() {
        replyEditText.setText("")
    }

    val TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114
    val REQUEST_CODE_CHAT_IMAGE = 115
    val REQUEST_SUBMIT_FEEDBACK = 909
    val SNACK_BAR_TEXT_OK = "OK"
    val BOT_OTHER_REASON_TEXT = "bot_other_reason"
    val SELECTED_ITEMS = "selected_items"
    val EMOJI_STATE = "emoji_state"
    val CSAT_ATTRIBUTES = "csat_attribute"
    private val TICKER_TYPE_ANNOUNCEMENT = "announcement"
    private val TICKER_TYPE_WARNING = "warning"

    @Inject
    lateinit var presenter: ChatbotPresenter

    @Inject
    lateinit var session: UserSessionInterface

    lateinit var replyEditText: EditText

    lateinit var mCsatResponse: WebSocketCsatResponse
    lateinit var attribute: Attributes
    private var isBackAllowed = true
    private lateinit var ticker:Ticker

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .chatbotModule(context?.let { ChatbotModule(it) })
                    .build()

            chatbotComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun onClick(v: View?) {
        reply_box.hide()
        val id = v?.id
        if (id == R.id.btn_inactive_1 || id == R.id.btn_inactive_2 || id == R.id.btn_inactive_3
                || id == R.id.btn_inactive_4 || id == R.id.btn_inactive_5) {
            onEmojiClick(v)
        }
    }

    private fun onEmojiClick(view: View?) {
        when (view?.id) {
            R.id.btn_inactive_1 -> onClickEmoji(1)
            R.id.btn_inactive_2 -> onClickEmoji(2)
            R.id.btn_inactive_3 -> onClickEmoji(3)
            R.id.btn_inactive_4 -> onClickEmoji(4)
            R.id.btn_inactive_5 -> onClickEmoji(5)
        }
    }

    override fun openCsat(csatResponse: WebSocketCsatResponse) {
        mCsatResponse = csatResponse
        if(::mCsatResponse.isInitialized){
            list_quick_reply.hide()
            showCsatRatingView()
        }
    }

    private fun showCsatRatingView() {
        chatbot_view_help_rate.txt_help_title.setText(mCsatResponse.attachment?.attributes?.title)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(new_comment.getWindowToken(), 0)
        reply_box.hide()
        chatbot_view_help_rate.show()
    }

    private fun hideCsatRatingView() {
        reply_box.show()
        chatbot_view_help_rate.hide()
    }

    private fun onClickEmoji(number: Int) {
        startActivityForResult(context?.let {
            ChatBotProvideRatingActivity
                .getInstance(it, number,mCsatResponse)
        }, REQUEST_SUBMIT_FEEDBACK)
    }

    override fun getUserSession(): UserSessionInterface {
        return session
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)
        replyEditText = view.findViewById(R.id.new_comment)
        ticker = view.findViewById(R.id.chatbot_ticker)
        return view
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ChatbotTypeFactoryImpl(
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
        if (adapterTypeFactory !is ChatbotTypeFactoryImpl) {
            throw IllegalStateException("getAdapterTypeFactory() must return ChatbotTypeFactoryImpl")
        }
        val typeFactory = adapterTypeFactory as ChatbotTypeFactoryImpl
        return ChatbotAdapter(typeFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chatbot_view_help_rate.btn_inactive_1.setOnClickListener(this@ChatbotFragment)
        chatbot_view_help_rate.btn_inactive_2.setOnClickListener(this@ChatbotFragment)
        chatbot_view_help_rate.btn_inactive_3.setOnClickListener(this@ChatbotFragment)
        chatbot_view_help_rate.btn_inactive_4.setOnClickListener(this@ChatbotFragment)
        chatbot_view_help_rate.btn_inactive_5.setOnClickListener(this@ChatbotFragment)

        super.onViewCreated(view, savedInstanceState)
        super.viewState = ChatbotViewStateImpl(
                view,
                session,
                this,
                this,
                this,
                (activity as BaseChatToolbarActivity).getToolbar(),
                adapter
        )
        viewState.initView()
        loadInitialData()
        showTicker()

        if (savedInstanceState != null)
            this.attribute = savedInstanceState.getParcelable(this.CSAT_ATTRIBUTES) ?: Attributes()

    }

    private fun showTicker() {
        presenter.showTickerData(onError(),onSuccesGetTickerData())
    }

    private fun onSuccesGetTickerData(): (TickerData) -> Unit {
        return {
            if (!it.items.isNullOrEmpty()) {
                ticker.show()
                if (it.items.size > 1) {
                    showMultiTicker(it)
                } else {
                    showSingleTicker(it)
                }
            }
        }
    }

    private fun showSingleTicker(tickerData: TickerData) {
        ticker.tickerTitle = tickerData.items?.get(0)?.title
        ticker.setTextDescription(tickerData.items?.get(0)?.text?:"")
        ticker.tickerType = getTickerType(tickerData.type ?: "")
    }

    private fun showMultiTicker(tickerData: TickerData) {
        val mockData = arrayListOf<com.tokopedia.unifycomponents.ticker.TickerData>()

        tickerData.items?.forEach {
            mockData.add(com.tokopedia.unifycomponents.ticker.TickerData(it?.title,
                    it?.text?:"",
                    getTickerType(tickerData.type ?: "")))
        }

        val adapter = TickerPagerAdapter(activity, mockData)
        ticker.addPagerView(adapter, mockData)
    }

    private fun getTickerType(tickerType: String): Int {
        return when (tickerType) {
            TICKER_TYPE_ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    override fun getSwipeRefreshLayoutResourceId() = 0

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    private fun onAttachImageClicked() {
        activity?.let {
            val builder = ImagePickerBuilder(it.getString(com.tokopedia.imagepicker.R.string.choose_image),
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

    override fun loadInitialData() {
        showLoading()
        presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChatFirstTime())
        presenter.connectWebSocket(messageId)
    }

    private fun onSuccessGetExistingChatFirstTime(): (ChatroomViewModel) -> Unit {
        return {

            val list = it.listChat.filter {
                !(it is FallbackAttachmentViewModel && it.message.isEmpty())
            }

            updateViewData(it)
            renderList(list, it.canLoadMore)
            getViewState().onSuccessLoadFirstTime(it)
            checkShowLoading(it.canLoadMore)
            presenter.sendReadEvent(messageId)
        }
    }

    private fun onSuccessGetPreviousChat(): (ChatroomViewModel) -> Unit {
        return {
            val list = it.listChat.filter {
                !(it is FallbackAttachmentViewModel && it.message.isEmpty())
            }
            renderList(list, it.canLoadMore)
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
            it.startActivity(ImagePreviewActivity.getCallingIntent(it,
                    strings,
                    null, 0))
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOKOPEDIA_ATTACH_INVOICE_REQ_CODE -> onSelectedInvoiceResult(resultCode, data)
            REQUEST_CODE_CHAT_IMAGE -> onPickedAttachImage(resultCode, data)
            REQUEST_SUBMIT_FEEDBACK -> if (resultCode == Activity.RESULT_OK) submitRating(data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun submitRating(data: Intent?) {

        var csatAttributes: Attributes?

        if (!(::mCsatResponse.isInitialized)) {
            csatAttributes = attribute
        } else {
            csatAttributes = mCsatResponse.attachment?.attributes
        }

        val reasonList = csatAttributes?.reasons
        val input = InputItem()
        input.chatbotSessionId = csatAttributes?.chatbotSessionId
        input.livechatSessionId = csatAttributes?.livechatSessionId
        input.reason = getFilters(data, reasonList)
        input.otherReason = data?.getStringExtra(BOT_OTHER_REASON_TEXT) ?: ""
        input.score = data?.extras?.getInt(EMOJI_STATE) ?: 0
        input.timestamp = data?.getStringExtra("time_stamp")
        input.triggerRuleType = csatAttributes?.triggerRuleType

        presenter.submitCsatRating(input, onError(),
                onSuccessSubmitCsatRating())
    }

    private fun getFilters(data: Intent?, reasonList: List<String?>?): String? {
        val selectedOption = data?.getStringExtra(SELECTED_ITEMS)?.split(";")
        var filters = ""
        if (!selectedOption.isNullOrEmpty()) {
            for (filter in selectedOption) {
                filters += reasonList?.get(filter.toInt()) + ","
            }
            return filters.substring(0, filters.length - 1)
        }
        return ""
    }

    private fun onSuccessSubmitCsatRating(): (String) -> Unit {
        hideCsatRatingView()
        return { str ->
            view?.let {
                Toaster.showNormalWithAction(it, str, Snackbar.LENGTH_LONG, SNACK_BAR_TEXT_OK, View.OnClickListener { })
            }
        }
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

    override fun prepareListener() {
        view?.findViewById<View>(R.id.send_but)?.setOnClickListener {
            onSendButtonClicked()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::mCsatResponse.isInitialized)
            outState.putParcelable(CSAT_ATTRIBUTES, mCsatResponse.attachment?.attributes)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onClickImagePicker() {
        onAttachImageClicked()
    }

    override fun createChatMenuFactory(): ChatMenuFactory {
        return ChatBotChatMenuFactory()
    }


    override fun showErrorToast(it: Throwable) {
        view?.let { mView -> Toaster.showErrorWithAction(mView, it.message.toString(), Snackbar.LENGTH_LONG, SNACK_BAR_TEXT_OK, View.OnClickListener { }) }
    }

    override fun onReceiveConnectionEvent(connectionDividerViewModel: ConnectionDividerViewModel) {
        getViewState().showDividerViewOnConnection(connectionDividerViewModel)
    }

    override fun isBackAllowed(isBackAllowed: Boolean) {
        this.isBackAllowed = isBackAllowed
    }

    override fun onClickLeaveQueue() {
        presenter.OnClickLeaveQueue()
    }

    override fun updateToolbar(profileName: String?, profileImage: String?) {
        if (activity is ChatbotActivity){
            (activity as ChatbotActivity).upadateToolbar(profileName,profileImage)
        }
    }

    override fun onBackPressed() {
        if(!isBackAllowed){
            val dialog = Dialog(context as Activity, Dialog.Type.PROMINANCE)
            dialog.setTitle(context?.getString(R.string.cb_bot_leave_the_queue))
            dialog.setDesc(context?.getString(R.string.cb_bot_leave_the_queue_desc_one))
            dialog.setBtnOk(context?.getString(R.string.cb_bot_ok_text))
            dialog.setBtnCancel(context?.getString(R.string.cb_bot_cancel_text))
            dialog.setOnOkClickListener{
                presenter.OnClickLeaveQueue()
                (activity as ChatbotActivity).finish()

            }
            dialog.setOnCancelClickListener{
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
        }else{
            (activity as ChatbotActivity).finish()
        }
    }
}
