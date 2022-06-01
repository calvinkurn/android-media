package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.ImageMenu
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_ENTRY
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_ID
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_TITLE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.CODE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.EVENT
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.FALSE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.IMAGE_URL
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.IS_ATTACHED
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.STATUS
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.STATUS_ID
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.USED_BY
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_FIVE
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_FOUR
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_ONE
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_THREE
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_TWO
import com.tokopedia.chatbot.ChatbotConstant.ONE_SECOND_IN_MILLISECONDS
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHAT_IMAGE
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_CSAT
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_FEEDBACK
import com.tokopedia.chatbot.ChatbotConstant.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics.Companion.chatbotAnalytics
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.attachinvoice.view.TransactionInvoiceBottomSheet
import com.tokopedia.chatbot.attachinvoice.view.TransactionInvoiceBottomSheetListener
import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.Attributes
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.util.ChatBubbleItemDecorator
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.chatbot.view.activity.ChatBotCsatActivity
import com.tokopedia.chatbot.view.activity.ChatBotProvideRatingActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity.Companion.DEEP_LINK_URI
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactoryImpl
import com.tokopedia.chatbot.view.adapter.ImageRetryBottomSheetAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.*
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.listener.ChatbotViewState
import com.tokopedia.chatbot.view.listener.ChatbotViewStateImpl
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.imagepicker.common.*
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.chatbot_layout_rating.view.*
import kotlinx.android.synthetic.main.fragment_chatbot.*
import javax.inject.Inject

/**
 * @author by nisie on 23/11/18.
 */
private const val ACTION_CSAT_SMILEY_BUTTON_CLICKED = "click csat smiley button"
private const val ACTION_QUICK_REPLY_BUTTON_CLICKED = "click quick reply button"
private const val ACTION_REPLY_BUTTON_CLICKED = "click reply"
private const val ACTION_ACTION_BUBBLE_CLICKED = "click action button"
private const val ACTION_THUMBS_UP_BUTTON_CLICKED = "click thumbs up button"
private const val ACTION_THUMBS_DOWN_BUTTON_CLICKED = "click thumbs down button"
private const val ACTION_THUMBS_DOWN_REASON_BUTTON_CLICKED = "click thumbs down reason button"
private const val ACTION_IMPRESSION_CSAT_SMILEY_VIEW = "impression csat smiley form"
private const val ACTION_IMPRESSION_WELCOME_MESSAGE = "impression welcome message"
private const val WELCOME_MESSAGE_VALIDATION = "dengan Toped di sini"
private const val FIRST_PAGE = 1
private const val RESEND = 1
private const val DELETE = 0
private const val SEE_ALL_INVOICE_TEXT = "lihat_semua_transaksi"

class ChatbotFragment : BaseChatFragment(), ChatbotContract.View,
        AttachedInvoiceSelectionListener, QuickReplyListener,
        ChatActionListBubbleListener, ChatRatingListener,
        TypingListener, ChatOptionListListener, CsatOptionListListener,
        View.OnClickListener, TransactionInvoiceBottomSheetListener, StickyActionButtonClickListener{

    override fun clearChatText() {
        replyEditText.setText("")
    }
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
    lateinit var replyEditTextContainer: LinearLayout

    lateinit var mCsatResponse: WebSocketCsatResponse
    lateinit var attribute: Attributes
    private var isBackAllowed = true
    private lateinit var ticker: Ticker
    private lateinit var dateIndicator: Typography
    private lateinit var dateIndicatorContainer: CardView
    private var csatOptionsViewModel: CsatOptionsViewModel? = null
    private var invoiceRefNum = ""
    private var replyText = ""
    private var isStickyButtonClicked = false
    private var isChatRefreshed = false
    private var isFirstPage = true
    private var isArticleEntry = false
    private var hashMap: Map<String,String> = HashMap<String,String>()
    var isAttached : Boolean = false
    private lateinit var invoiceLabel: Label
    private lateinit var invoiceName : Typography
    private lateinit var invoiceImage : ImageView
    private lateinit var invoiceCancel : ImageView
    private lateinit var sendButton : ImageView
    private var isSendButtonActivated : Boolean = true
    private var isFloatingSendButton: Boolean = false
    private var isFloatingInvoiceCancelled : Boolean = false

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
            R.id.btn_inactive_1 -> {
                onClickEmoji(RATING_ONE)
            }
            R.id.btn_inactive_2 -> {
                onClickEmoji(RATING_TWO)
            }
            R.id.btn_inactive_3 -> {
                onClickEmoji(RATING_THREE)
            }
            R.id.btn_inactive_4 -> {
                onClickEmoji(RATING_FOUR)
            }
            R.id.btn_inactive_5 -> {
                onClickEmoji(RATING_FIVE)
            }
        }
    }

    override fun openCsat(csatResponse: WebSocketCsatResponse) {
        mCsatResponse = csatResponse
        if (::mCsatResponse.isInitialized) {
            list_quick_reply.hide()
            showCsatRatingView()
        }
    }

    private fun showCsatRatingView() {
        chatbotAnalytics.eventShowView(ACTION_IMPRESSION_CSAT_SMILEY_VIEW)
        chatbot_view_help_rate.txt_help_title.setText(mCsatResponse.attachment?.attributes?.title)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(new_comment.getWindowToken(), 0)
        reply_box.hide()
        chatbot_view_help_rate.show()
    }

    private fun hideCsatRatingView() {
        enableTyping()
        chatbot_view_help_rate.hide()
    }

    private fun onClickEmoji(number: Int) {
        startActivityForResult(context?.let {
            ChatBotProvideRatingActivity
                    .getInstance(it, number, mCsatResponse)
        }, REQUEST_SUBMIT_FEEDBACK)
        chatbotAnalytics.eventClick(ACTION_CSAT_SMILEY_BUTTON_CLICKED, number.toString())
    }

    override fun getUserSession(): UserSessionInterface {
        return session
    }

    override fun getScreenName(): String {
        return ""
    }

    lateinit var textWatcher : TextWatcher

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bundle = this.arguments
        if (bundle != null) {
            val intentData = bundle.getString(DEEP_LINK_URI, "")
            var uri: Uri = Uri.parse(intentData)

            isAttached = checkForIsAttachedInvoice(uri)
            hashMap = presenter.getValuesForArticleEntry(uri)
            isArticleEntry = checkForArticleEntry(uri)

        }

        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)
        replyEditText = view.findViewById(R.id.new_comment)
        replyEditTextContainer = view.findViewById(R.id.new_comment_container)
        bindReplyTextBackground()
        ticker = view.findViewById(R.id.chatbot_ticker)
        dateIndicator = view.findViewById(R.id.dateIndicator)
        dateIndicatorContainer = view.findViewById(R.id.dateIndicatorContainer)

        invoiceLabel = view.findViewById(R.id.tv_status)
        invoiceName = view.findViewById(R.id.tv_invoice_name)
        invoiceImage = view.findViewById(R.id.iv_thumbnail)
        invoiceCancel = view.findViewById(R.id.iv_cross)
        sendButton = view.findViewById(R.id.send_but)

        isFloatingInvoiceCancelled = false
        setChatBackground()
        getRecyclerView(view)?.addItemDecoration(ChatBubbleItemDecorator(setDateIndicator()))
        return view
    }

    private fun checkForArticleEntry(uri: Uri): Boolean {
        if (uri.getQueryParameter(USED_BY) != null && uri.getQueryParameter(USED_BY) == ARTICLE_ENTRY) {
            return true
        }
        return false
    }

    private fun checkForIsAttachedInvoice(uri: Uri): Boolean {
        if (uri.getQueryParameter(IS_ATTACHED) != null) {
            return uri.getQueryParameter(IS_ATTACHED) != FALSE
        }
        return false
    }

    override fun sendInvoiceForArticle() {
        if (isArticleEntry) {
            if (!isAttached) {

                if (hashMap.get(CODE)?.isNotEmpty() == true) {
                    val attachInvoiceSingleViewModel = presenter.createAttachInvoiceSingleViewModel(hashMap)
                    var invoice: InvoiceLinkPojo =
                        AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(
                            attachInvoiceSingleViewModel
                        )
                    val generatedInvoice = presenter.generateInvoice(invoice, opponentId)
                    getViewState()?.onShowInvoiceToChat(generatedInvoice)
                    presenter.sendInvoiceAttachment(
                        messageId, invoice, generatedInvoice.startTime,
                        opponentId, isArticleEntry,hashMap.get(USED_BY).toBlankOrString()
                    )
                }
                if (hashMap.get(ARTICLE_ID)?.isNotEmpty() == true) {
                    val startTime = SendableUiModel.generateStartTime()
                    val msg = hashMap.get(ARTICLE_TITLE).toBlankOrString()
                    var quickReplyViewModel = QuickReplyViewModel(msg, msg, msg)

                    presenter.sendQuickReplyInvoice(
                        messageId,
                        quickReplyViewModel,
                        startTime,
                        opponentId,
                        hashMap.get(EVENT).toBlankOrString(),
                        hashMap.get(USED_BY).toBlankOrString()
                    )
                }
                enableTyping()
            } else {

                isSendButtonActivated = false
                isFloatingSendButton = true
                sendButton.setImageResource(R.drawable.ic_chatbot_send_deactivated)

                invoiceLabel.text = hashMap.get(STATUS).toBlankOrString()
                val labelType = getLabelType(hashMap.get(STATUS_ID).toIntOrZero())
                invoiceLabel?.setLabelType(labelType)


                invoiceName.setText(hashMap.get(CODE).toBlankOrString())
                if (hashMap.get(IMAGE_URL)?.isNotEmpty() == true)
                    ImageHandler.loadImage(
                        context,
                        invoiceImage,
                        hashMap.get(IMAGE_URL)!!.toBlankOrString(),
                        R.drawable.ic_retry_image_send
                    )
                invoiceCancel.setOnClickListener {
                    float_chat_item.visibility = View.GONE
                    isAttached = false
                    isFloatingInvoiceCancelled = true
                }
                if (isFloatingSendButton) {

                    textWatcher = object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            if (replyEditText.text.toString().isEmpty()) {
                                sendButton.setImageResource(R.drawable.ic_chatbot_send_deactivated)
                                isSendButtonActivated = false
                            }
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            if (replyEditText.text.toString().isNotEmpty()) {
                                sendButton.setImageResource(R.drawable.ic_chatbot_send)
                                isSendButtonActivated = true
                            }
                        }
                    }
                    replyEditText.addTextChangedListener(textWatcher)

                }

                float_chat_item.show()

            }
        }

    }

    private fun onSendFloatingInvoiceClicked() {

        float_chat_item.hide()
        isSendButtonActivated = true
        sendButton.setImageResource(R.drawable.ic_chatbot_send)
        replyEditText.removeTextChangedListener(textWatcher)

        if(!isFloatingInvoiceCancelled) {

            val attachInvoiceSingleViewModel = presenter.createAttachInvoiceSingleViewModel(hashMap)
            var invoice: InvoiceLinkPojo =
                AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(
                    attachInvoiceSingleViewModel
                )
            val generatedInvoice = presenter.generateInvoice(invoice, opponentId)
            getViewState()?.onShowInvoiceToChat(generatedInvoice)
            presenter.sendInvoiceAttachment(
                messageId, invoice, generatedInvoice.startTime,
                opponentId, isArticleEntry, hashMap.get(USED_BY).toBlankOrString()
            )
        }

        val startTime = SendableUiModel.generateStartTime()
        val msg = replyEditText.text.toString()
        var quickReplyViewModel = QuickReplyViewModel(msg, msg, msg)

        presenter.sendQuickReplyInvoice(
            messageId,
            quickReplyViewModel,
            startTime,
            opponentId,
            hashMap.get(EVENT).toString(),
            hashMap.get(USED_BY).toString()
        )
        emptyReplyEditText()
        isFloatingSendButton = false
    }

    private fun emptyReplyEditText(){
        replyEditText.setText("")
    }

    private fun getLabelType(statusId: Int?): Int {
        if (statusId == null) return Label.GENERAL_DARK_GREY
        return when (OrderStatusCode.MAP[statusId]) {
            OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
            OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_DARK_GREY
        }
    }


    private fun setChatBackground() {
        activity?.window?.setBackgroundDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.layered_chatbot_background) })
    }

    private fun bindReplyTextBackground() {
        val replyEditTextBg = ViewUtil.generateBackgroundWithShadow(
                replyEditTextContainer,
                com.tokopedia.unifyprinciples.R.color.Unify_N0,
                R.dimen.dp_chatbot_20,
                R.dimen.dp_chatbot_20,
                R.dimen.dp_chatbot_20,
                R.dimen.dp_chatbot_20,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
                R.dimen.dp_chatbot_2,
                R.dimen.dp_chatbot_1,
                Gravity.CENTER
        )
        val paddingStart = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).toInt()
        val paddingEnd = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8).toInt()
        val paddingTop = resources.getDimension(R.dimen.dp_chatbot_11).toInt()
        val paddingBottom = resources.getDimension(R.dimen.dp_chatbot_10).toInt()
        replyEditTextContainer.background = replyEditTextBg
        replyEditTextContainer.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ChatbotTypeFactoryImpl(
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                getUserSession()
        )
    }

    fun setDateIndicator() :(String) ->Unit ={
        if (it.isNotEmpty()){
            dateIndicator.text = it
            dateIndicatorContainer.show()
        }
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
        viewState?.initView()
        presenter.checkForSession(messageId)
        showTicker()

        if (savedInstanceState != null)
            this.attribute = savedInstanceState.getParcelable(this.CSAT_ATTRIBUTES) ?: Attributes()

    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun loadChatHistory() {
        loadInitialData()
        presenter.connectWebSocket(messageId)
    }

    override fun startNewSession() {
        presenter.connectWebSocket(messageId)
    }

    override fun blockTyping() {
        getViewState()?.handleReplyBox(false)
    }

    override fun enableTyping() {
        getViewState()?.handleReplyBox(true)
        swipeToRefresh.setMargin(0, 0, 0, 0)
    }

    override fun onCreateViewState(view: View): BaseChatViewState {
        return ChatbotViewStateImpl(
                view,
                session,
                this,
                this,
                this,
                (activity as BaseChatToolbarActivity).getToolbar(),
                adapter,
                onChatMenuButtonClicked
        )
    }

    val onChatMenuButtonClicked: () -> Unit = {
        activity?.let {
            val builder = ImagePickerBuilder.getOriginalImageBuilder(it)
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            intent.putParamPageSource(ImagePickerPageSource.CHAT_BOT_PAGE)
            startActivityForResult(intent, REQUEST_CODE_CHAT_IMAGE)
        }
    }

    private fun showTicker() {
        presenter.showTickerData(onError(), onSuccesGetTickerData())
    }

    private fun onSuccesGetTickerData(): (TickerData) -> Unit {
        return {
            if (!it.items.isNullOrEmpty()) {
                ticker.show()
                if (it.items.size > 1) {
                    showMultiTicker(it)
                } else if (it.items.size == 1) {
                    showSingleTicker(it)
                }
            }
        }
    }

    private fun showSingleTicker(tickerData: TickerData) {
        ticker.tickerTitle = tickerData.items?.get(0)?.title
        ticker.setHtmlDescription(tickerData.items?.get(0)?.text ?: "")
        ticker.tickerType = getTickerType(tickerData.type ?: "")
        ticker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(view?.context, linkUrl.toString())
            }

            override fun onDismiss() {
            }

        })
    }

    private fun showMultiTicker(tickerData: TickerData) {
        val mockData = arrayListOf<com.tokopedia.unifycomponents.ticker.TickerData>()

        tickerData.items?.forEach {
            mockData.add(com.tokopedia.unifycomponents.ticker.TickerData(it?.title,
                    it?.text ?: "",
                    getTickerType(tickerData.type ?: "")))
        }

        val adapter = TickerPagerAdapter(activity, mockData)
        ticker.addPagerView(adapter, mockData)
        adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                RouteManager.route(view?.context, linkUrl.toString())
            }
        })
    }

    private fun getTickerType(tickerType: String): Int {
        return when (tickerType) {
            TICKER_TYPE_ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.swipe_refresh_layout)
    }

    override fun onSwipeRefresh() {
        if (!isChatRefreshed && isFirstPage){
            hideSnackBarRetry()
            loadData(FIRST_PAGE)
            swipeToRefresh.isRefreshing = true
            isChatRefreshed = true
        } else{
            swipeToRefresh.isRefreshing = false
            swipeToRefresh.isEnabled = false
            swipeToRefresh.setOnRefreshListener(null)
        }

    }

    override fun getSwipeRefreshLayoutResourceId() = 0

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun loadInitialData() {
        getViewState()?.clearChatOnLoadChatHistory()
        showLoading()
        presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChatFirstTime(), onGetChatRatingListMessageError)
    }

    private fun onSuccessGetExistingChatFirstTime(): (ChatroomViewModel) -> Unit {
        return {
            val list = it.listChat.filter {
                !((it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                        (it is MessageUiModel && it.message.isEmpty()))
            }

            updateViewData(it)
            renderList(list, it.canLoadMore)
            getViewState()?.onSuccessLoadFirstTime(it)
            checkShowLoading(it.canLoadMore)
            enableLoadMore()
        }
    }

    private fun onSuccessGetPreviousChat(page: Int): (ChatroomViewModel) -> Unit {
        return {
            val list = it.listChat.filter {
                !((it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                        (it is MessageUiModel && it.message.isEmpty()))
            }
            if (page == FIRST_PAGE) isFirstPage = false
            if (list.isNotEmpty()){
                val filteredList= getViewState()?.clearDuplicate(list)
                if (filteredList?.isEmpty() == true) loadData(page + FIRST_PAGE)
                filteredList?.let { renderList ->
                    renderList(renderList, it.canLoadMore)
                }
                checkShowLoading(it.canLoadMore)
                enableLoadMore()
            }
        }
    }

    fun checkShowLoading(canLoadMore: Boolean) {
        if (canLoadMore) super.showLoading()
    }

    private val onGetChatRatingListMessageError: (String) -> Unit = {
        if (view != null) {
            Toaster.make(view!!, it, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            if (view != null) {
                Toaster.make(view!!, ErrorHandler.getErrorMessage(view!!.context, it), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
            }
        }
    }

    override fun loadData(page: Int) {
        presenter.loadPrevious(messageId, page, onError(), onSuccessGetPreviousChat(page), onGetChatRatingListMessageError)
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        sendEventForWelcomeMessage(visitable)
        manageActionBubble(visitable)
        managePreviousStateOfBubble(visitable)
        mapMessageToList(visitable)
        getViewState()?.hideEmptyMessage(visitable)
        getViewState()?.onCheckToHideQuickReply(visitable)
    }

    private fun managePreviousStateOfBubble(visitable: Visitable<*>) {
        if(visitable is MessageUiModel && visitable.isSender){
            getViewState()?.hideInvoiceList()
            getViewState()?.hideHelpfullOptions()
        }
    }

    private fun manageActionBubble(visitable: Visitable<*>) {
        when {
            visitable is MessageUiModel && visitable.isSender -> hideActionBubble()
            visitable is AttachInvoiceSentUiModel && visitable.isSender -> hideActionBubble()
        }
    }

    private fun hideActionBubble() {
        getViewState()?.hideActionBubbleOnSenderMsg()
    }

    private fun sendEventForWelcomeMessage(visitable: Visitable<*>) {
        if (visitable is BaseChatUiModel && visitable.message.contains(WELCOME_MESSAGE_VALIDATION)) {
            chatbotAnalytics.eventShowView(ACTION_IMPRESSION_WELCOME_MESSAGE)
        }
    }

    private fun getViewState(): ChatbotViewState? {
        return viewState as? ChatbotViewState
    }

    private fun mapMessageToList(visitable: Visitable<*>) {
        when (visitable) {
            is QuickReplyListViewModel -> getViewState()?.onReceiveQuickReplyEvent(visitable)
            is ChatActionSelectionBubbleViewModel -> getViewState()?.onReceiveQuickReplyEventWithActionButton(visitable)
            is ChatRatingViewModel -> getViewState()?.onReceiveQuickReplyEventWithChatRating(visitable)
            else -> super.onReceiveMessageEvent(visitable)
        }
    }

    override fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo) {
        val generatedInvoice = presenter.generateInvoice(invoiceLinkPojo, opponentId)
        getViewState()?.onShowInvoiceToChat(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, invoiceLinkPojo, generatedInvoice.startTime,
                opponentId,isArticleEntry,hashMap.get(USED_BY).toBlankOrString())
        enableTyping()
    }

    private fun attachInvoiceRetrieved(selectedInvoice: InvoiceLinkPojo) {
        val generatedInvoice = presenter.generateInvoice(selectedInvoice, "")
        getViewState()?.onShowInvoiceToChat(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, selectedInvoice, generatedInvoice.startTime,
                opponentId,isArticleEntry,hashMap.get(USED_BY).toBlankOrString())
    }

    fun showSearchInvoiceScreen() {
        activity?.let {
            val bottomSheetUnify = TransactionInvoiceBottomSheet.newInstance(it, messageId.toIntOrZero(), this)
            bottomSheetUnify.clearContentPadding = true
            bottomSheetUnify.show(childFragmentManager, "")
        }
    }

    override fun onQuickReplyClicked(model: QuickReplyViewModel) {
        chatbotAnalytics.eventClick(ACTION_QUICK_REPLY_BUTTON_CLICKED)
        presenter.sendQuickReply(messageId, model, SendableUiModel.generateStartTime(), opponentId)
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

    override fun onImageUploadCancelClicked(image: ImageUploadUiModel) {
        presenter.cancelImageUpload()
        getViewState()?.showRetryUploadImages(image, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOKOPEDIA_ATTACH_INVOICE_REQ_CODE -> onSelectedInvoiceResult(resultCode, data)
            REQUEST_CODE_CHAT_IMAGE -> onPickedAttachImage(resultCode, data)
            REQUEST_SUBMIT_FEEDBACK -> if (resultCode == Activity.RESULT_OK) submitRating(data)
            REQUEST_SUBMIT_CSAT -> submitCsat(resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun submitCsat(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) return
        val input = ChipSubmitChatCsatInput()
        with(input) {
            messageID = messageId
            caseID = data.getStringExtra(ChatBotCsatActivity.CASE_ID) ?: ""
            caseChatID = data.getStringExtra(ChatBotCsatActivity.CASE_CHAT_ID) ?: ""
            rating = data.extras?.getInt(EMOJI_STATE) ?: 0
            reasonCode = data.getStringExtra(SELECTED_ITEMS) ?: ""

        }
        presenter.submitChatCsat(input, onsubmitingChatCsatSuccess, onError())
    }

    private val onsubmitingChatCsatSuccess: (String) -> Unit = { message ->
        view?.let {
            csatOptionsViewModel?.let { it -> getViewState()?.hideCsatOptionList(it) }
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, SNACK_BAR_TEXT_OK)
        }
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
                if(filter.isNotEmpty())
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
            list_quick_reply.show()
        }
    }

    private fun onPickedAttachImage(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        presenter.checkUploadSecure(messageId, data)

    }

    override fun uploadUsingSecureUpload(data: Intent) {
        val path = ImagePickerResultExtractor.extract(data).imageUrlOrPathList.getOrNull(0)
        processImagePathToUpload(data)?.let {
            getViewState()?.onImageUpload(it)
            presenter.uploadImageSecureUpload(it, messageId, opponentId, onErrorImageUpload(), path, context)
        }
    }

    override fun uploadUsingOldMechanism(data: Intent) {
        processImagePathToUpload(data)?.let {
            getViewState()?.onImageUpload(it)
            presenter.uploadImages(it, messageId, opponentId, onErrorImageUpload())
        }
    }

    private fun onErrorImageUpload(): (Throwable, ImageUploadUiModel) -> Unit {
        return { throwable, image ->
            if (view != null) {
                Toaster.make(view!!, ErrorHandler.getErrorMessage(view!!.context, throwable), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                getViewState()?.showRetryUploadImages(image, true)
            }
        }
    }

    private fun processImagePathToUpload(data: Intent): ImageUploadUiModel? {

        val imagePathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
        if (imagePathList.size <= 0) {
            return null
        }
        val imagePath = imagePathList[0]

        if (!TextUtils.isEmpty(imagePath)) {
            val temp = generateChatViewModelWithImage(imagePath)
            return temp
        }
        return null
    }

    fun generateChatViewModelWithImage(imageUrl: String): ImageUploadUiModel {
        return ImageUploadUiModel.Builder()
            .withMsgId(messageId)
            .withFromUid(opponentId)
            .withAttachmentId((System.currentTimeMillis() / ONE_SECOND_IN_MILLISECONDS).toString())
            .withAttachmentType(AttachmentType.Companion.TYPE_IMAGE_UPLOAD)
            .withReplyTime(SendableUiModel.SENDING_TEXT)
            .withStartTime(SendableUiModel.generateStartTime())
            .withIsDummy(true)
            .withImageUrl(imageUrl)
            .build()
    }

    private fun onSelectedInvoiceResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.run {
                val selectedInvoice = data.getParcelableExtra(
                        ChatbotInternalRouter.Companion.TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY) ?: SelectedInvoice()
                attachInvoiceRetrieved(AttachInvoiceMapper.convertInvoiceToDomainInvoiceModel(selectedInvoice))
            }
        }
        enableTyping()
    }

    override fun prepareListener() {
        sendButton.setOnClickListener {
            if (isSendButtonActivated) {
                if (isFloatingSendButton) {
                    onSendFloatingInvoiceClicked()
                } else {
                    onSendButtonClicked()
                }
            } else
                Toaster.make(
                    it,
                    getString(R.string.chatbot_float_invoice_input_length_zero),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
                )
        }
    }

    override fun onSendButtonClicked() {
        chatbotAnalytics.eventClick(ACTION_REPLY_BUTTON_CLICKED)
        val sendMessage = replyEditText.text.toString()
        val startTime = SendableUiModel.generateStartTime()
        presenter.sendMessage(messageId, sendMessage, startTime, opponentId,
                onSendingMessage(sendMessage, startTime))
    }

    private fun onSendingMessage(sendMessage: String, startTime: String): () -> Unit {
        return {
            getViewState()?.onSendingMessage(messageId, getUserSession().userId, getUserSession()
                    .name, sendMessage, startTime)
            getViewState()?.scrollToBottom()
        }
    }

    override fun onChatActionBalloonSelected(selected: ChatActionBubbleViewModel, model: ChatActionSelectionBubbleViewModel) {
        chatbotAnalytics.eventClick(ACTION_ACTION_BUBBLE_CLICKED)
        if (selected.action.equals(SEE_ALL_INVOICE_TEXT, true)) {
            showSearchInvoiceScreen()
        } else {
            getViewState()?.hideActionBubble(model)
            presenter.sendActionBubble(messageId, selected, SendableUiModel.generateStartTime(), opponentId)
            enableTyping()
        }
    }

    override fun onClickRating(element: ChatRatingViewModel, rating: Int) {
        sendEvent(rating)
        presenter.sendRating(messageId, rating, element.replyTimeNano.toString(), onError(),
                onSuccessSendRating(rating, element))
    }

    private fun sendEvent(rating: Int) {
        if (rating == ChatRatingViewModel.RATING_GOOD) {
            chatbotAnalytics.eventClick(ACTION_THUMBS_UP_BUTTON_CLICKED)
        } else {
            chatbotAnalytics.eventClick(ACTION_THUMBS_DOWN_BUTTON_CLICKED)
        }
    }

    private fun onSuccessSendRating(rating: Int, element: ChatRatingViewModel): (SendRatingPojo) ->
    Unit {
        return {
            (activity as Activity).run {
                (viewState as ChatbotViewState).onSuccessSendRating(it, rating, element, this)
            }
        }
    }

    override fun onGoToWebView(url: String, id: String) {
        val BASE_DOMAIN_SHORTENED = "tkp.me"

        if (url.isNotEmpty()) {
            KeyboardHandler.DropKeyboard(activity, view)
            val uri = Uri.parse(url)
            if (uri?.scheme != null) {
                val isTargetDomainTokopedia = uri.host != null
                        && uri.host!!.endsWith("tokopedia.com")
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
        view?.let {
            Toaster.make(it, getString(R.string.undersize_image), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun onUploadOversizedImage() {
        view?.let {
            Toaster.make(it, getString(R.string.oversize_image), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun showSnackbarError(stringId: Int) {
        view?.let {
            Toaster.make(it, getString(stringId), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    /**
     * To handle url manually to webview. In this case, to contact us.
     */
    override fun shouldHandleUrlManually(url: String): Boolean {
        return true
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                isFirstPage = false
                showLoading()
                loadData(page + FIRST_PAGE)
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

    override fun createAttachmentMenus(): List<AttachmentMenu> {
        return listOf(
                ImageMenu()
        )
    }

    override fun showErrorToast(it: Throwable) {
        view?.let { mView -> Toaster.showErrorWithAction(mView, it.message.toString(), Snackbar.LENGTH_LONG, SNACK_BAR_TEXT_OK, View.OnClickListener { }) }
    }

    override fun onReceiveConnectionEvent(connectionDividerViewModel: ConnectionDividerViewModel, quickReplyList: List<QuickReplyViewModel>) {
        getViewState()?.showDividerViewOnConnection(connectionDividerViewModel)
        getViewState()?.showLiveChatQuickReply(quickReplyList)
    }

    override fun onReceiveChatSepratorEvent(chatSepratorViewModel: ChatSepratorViewModel, quickReplyList: List<QuickReplyViewModel>) {
        getViewState()?.showLiveChatSeprator(chatSepratorViewModel)
        getViewState()?.showLiveChatQuickReply(quickReplyList)
    }

    override fun isBackAllowed(isBackAllowed: Boolean) {
        this.isBackAllowed = isBackAllowed
    }

    override fun onClickLeaveQueue() {
        presenter.OnClickLeaveQueue()
    }

    override fun updateToolbar(profileName: String?, profileImage: String?, badgeImage: ToolbarAttributes.BadgeImage?) {
        if (activity is ChatbotActivity) {
            (activity as ChatbotActivity).upadateToolbar(profileName, profileImage, badgeImage)
        }
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        getViewState()?.showErrorWebSocket(isWebSocketError)
    }

    override fun chatOptionListSelected(selected: ChatOptionListViewModel, model: HelpFullQuestionsViewModel?) {
        model?.let { getViewState()?.hideOptionList(it) }
        sendOptionListSelectedMessage(selected.text ?: "")

        selected.value.let { presenter.hitGqlforOptionList(it, model) }
    }

    private fun sendOptionListSelectedMessage(selectedMessage: String) {
        val sendMessage = selectedMessage
        val startTime = SendableUiModel.generateStartTime()
        presenter.sendMessage(messageId, sendMessage, startTime, opponentId,
                onSendingMessage(sendMessage, startTime))
    }

    override fun csatOptionListSelected(selected: ChatOptionListViewModel, model: CsatOptionsViewModel?) {
        csatOptionsViewModel = model
        startActivityForResult(context?.let {
            ChatBotCsatActivity
                    .getInstance(it, selected.value, model)
        }, REQUEST_SUBMIT_CSAT)
    }

    override fun onRetrySendImage(element: ImageUploadUiModel) {
        val bottomSheetPage = BottomSheetUnify()
        val viewBottomSheetPage = View.inflate(context, R.layout.retry_upload_image_bottom_sheet_layout, null).apply {
            val rvPages = findViewById<RecyclerView>(R.id.rv_image_upload_option)
            rvPages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val adapter = ImageRetryBottomSheetAdapter(onBottomSheetItemClicked(element, bottomSheetPage))
            rvPages.adapter = adapter
            adapter.setList(listOf<String>(context?.getString(R.string.chatbot_delete)
                    ?: "", context?.getString(R.string.chatbot_resend) ?: ""))

        }

        bottomSheetPage.apply {
            setTitle(this@ChatbotFragment.context?.getString(R.string.chatbot_retry_image_upload_bottom_sheet_title)
                    ?: "")
            showCloseIcon = false
            setChild(viewBottomSheetPage)
            showKnob = true
        }
        fragmentManager?.let {
            bottomSheetPage.show(it, "retry image bottom sheet")
        }

    }

    fun onBottomSheetItemClicked(element: ImageUploadUiModel, bottomSheetPage: BottomSheetUnify): (Int) -> Unit {
        return {
            when (it) {
                RESEND -> {
                    removeDummy(element)
                    getViewState()?.onImageUpload(element)
                    presenter.uploadImages(element, messageId, opponentId, onErrorImageUpload())
                    bottomSheetPage.dismiss()
                }
                DELETE -> {
                    removeDummy(element)
                    bottomSheetPage.dismiss()
                    view?.let {
                        Toaster.make(it, context?.getString(R.string.chatbot_your_picture_has_been_deleted)
                                ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL)
                    }
                }
            }
        }
    }


    override fun removeDummy(visitable: Visitable<*>) {
        getViewState()?.removeDummy(visitable)
    }

    override fun onStickyActionButtonClicked(invoiceRefNum: String, replyText: String) {
        this.invoiceRefNum = invoiceRefNum
        this.replyText = replyText
        presenter.checkLinkForRedirection(invoiceRefNum,
                onGetSuccessResponse = {
                    if (it.isNotEmpty()){
                        onGoToWebView(it, it)}
                },
                setStickyButtonStatus = { isResoListNotEmpty->
                    if (!isResoListNotEmpty) this.isStickyButtonClicked = true
                },
                onError = {

                })
    }


    override fun onResume() {
        super.onResume()
        sendReplyTextForResolutionComponent()
    }

    private fun sendReplyTextForResolutionComponent() {
        if (isStickyButtonClicked) {
            this.isStickyButtonClicked = false
            presenter.checkLinkForRedirection(invoiceRefNum,
                    onGetSuccessResponse = {},
                    setStickyButtonStatus = { isResoListNotEmpty ->
                        if (isResoListNotEmpty) {
                            val startTime = SendableUiModel.generateStartTime()
                            presenter.sendMessage(messageId, replyText, startTime, opponentId,
                                    onSendingMessage(replyText, startTime))
                        }
                    },
                    onError = {

                    })
        }

    }

    override fun onBackPressed(): Boolean {
        if (!isBackAllowed) {
            presenter.OnClickLeaveQueue()
            (activity as? ChatbotActivity)?.finish()
            return true
        }
        return super.onBackPressed()
    }

    override fun onBottomSheetDismissListener(data: Intent) {
        onSelectedInvoiceResult(Activity.RESULT_OK, data)
    }

    override fun transactionNotFoundClick() {
        val selected = presenter.getActionBubbleforNoTrasaction()
        presenter.sendActionBubble(messageId, selected, SendableUiModel.generateStartTime(), opponentId)
    }
}

