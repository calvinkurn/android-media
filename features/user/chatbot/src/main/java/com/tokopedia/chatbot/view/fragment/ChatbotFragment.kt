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
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.FallbackAttachmentUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.ImageMenu
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.SESSION_CHANGE
import com.tokopedia.chat_common.view.widget.AttachmentMenuRecyclerView
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
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHAT_VIDEO
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_CSAT
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_FEEDBACK
import com.tokopedia.chatbot.ChatbotConstant.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE
import com.tokopedia.chatbot.ChatbotConstant.VIDEO_URL
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_DURATION_FOR_VIDEO
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_MEDIA_COUNT
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.SOURCE_ID_FOR_VIDEO_UPLOAD
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics
import com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
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
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.Attributes
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.util.ChatBubbleItemDecorator
import com.tokopedia.chatbot.util.VideoUtil
import com.tokopedia.chatbot.util.GetUserNameForReplyBubble
import com.tokopedia.chatbot.util.SmoothScroller
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.util.convertMessageIdToLong
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.chatbot.view.activity.ChatBotCsatActivity
import com.tokopedia.chatbot.view.activity.ChatBotProvideRatingActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity.Companion.DEEP_LINK_URI
import com.tokopedia.chatbot.view.activity.ChatbotVideoActivity
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactoryImpl
import com.tokopedia.chatbot.view.adapter.MediaRetryBottomSheetAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.VideoUploadListener
import com.tokopedia.chatbot.view.attachmentmenu.ChatbotImageMenu
import com.tokopedia.chatbot.view.adapter.ImageRetryBottomSheetAdapter
import com.tokopedia.chatbot.view.adapter.ReplyBubbleBottomSheetAdapter
import com.tokopedia.chatbot.view.adapter.util.RecyclerViewScrollListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleOnBoarding
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.listener.ChatbotViewState
import com.tokopedia.chatbot.view.listener.ChatbotViewStateImpl
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
import kotlinx.android.synthetic.main.compose_message_area.*
import kotlinx.android.synthetic.main.fragment_chatbot.*
import java.io.File
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
private const val RESEND = 1
private const val DELETE = 0
private const val REPLY = 0
private const val SEE_ALL_INVOICE_TEXT = "lihat_semua_transaksi"

class ChatbotFragment : BaseChatFragment(), ChatbotContract.View,
    AttachedInvoiceSelectionListener, QuickReplyListener,
    ChatActionListBubbleListener, ChatRatingListener,
    TypingListener, ChatOptionListListener, CsatOptionListListener,
    View.OnClickListener, TransactionInvoiceBottomSheetListener, StickyActionButtonClickListener,
    VideoUploadListener, AttachmentMenu.AttachmentMenuListener {
        AttachedInvoiceSelectionListener, QuickReplyListener,
        ChatActionListBubbleListener, ChatRatingListener,
        TypingListener, ChatOptionListListener, CsatOptionListListener,
        View.OnClickListener, TransactionInvoiceBottomSheetListener, StickyActionButtonClickListener
        , ReplyBubbleAreaMessage.Listener{

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

    @Inject
    lateinit var chatbotAnalytics: dagger.Lazy<ChatbotAnalytics>

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
    lateinit var textWatcher : TextWatcher
    private var isConnectedToAgent : Boolean = false
    private lateinit var attachmentMenuRecyclerView : AttachmentMenuRecyclerView
    private lateinit var replyBubbleContainer : ReplyBubbleAreaMessage
    private var replyBubbleEnabled : Boolean = false
    private var senderNameForReply = ""
    private var smoothScroll : SmoothScroller? = null
    private var rvScrollListener : RecyclerViewScrollListener? = null
    private var rvLayoutManager : LinearLayoutManager? = null
    private var messageCreateTime : String = ""
    private lateinit var chatbotAdapter: ChatbotAdapter

    @Inject
    lateinit var replyBubbleOnBoarding : ReplyBubbleOnBoarding
    private var recyclerView : RecyclerView? = null
    private var isArticleDataSent : Boolean = false

    @Inject
    lateinit var getUserNameForReplyBubble : GetUserNameForReplyBubble

    companion object {
        private const val ONCLICK_REPLY_TIME_OFFSET_FOR_REPLY_BUBBLE = 5000
    }

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
        chatbotAnalytics.get().eventShowView(ACTION_IMPRESSION_CSAT_SMILEY_VIEW)
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
        chatbotAnalytics.get().eventClick(ACTION_CSAT_SMILEY_BUTTON_CLICKED, number.toString())
    }

    override fun getUserSession(): UserSessionInterface {
        return session
    }

    override fun getScreenName(): String {
        return ""
    }

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
        replyBubbleContainer = view.findViewById(R.id.reply_bubble_container)
        bindReplyTextBackground()
        ticker = view.findViewById(R.id.chatbot_ticker)
        dateIndicator = view.findViewById(R.id.dateIndicator)
        dateIndicatorContainer = view.findViewById(R.id.dateIndicatorContainer)

        invoiceLabel = view.findViewById(R.id.tv_status)
        invoiceName = view.findViewById(R.id.tv_invoice_name)
        invoiceImage = view.findViewById(R.id.iv_thumbnail)
        invoiceCancel = view.findViewById(R.id.iv_cross)
        sendButton = view.findViewById(R.id.send_but)

        attachmentMenuRecyclerView = view.findViewById(R.id.rv_attachment_menu)

        recyclerView = getRecyclerView(view)
        isFloatingInvoiceCancelled = false
        setChatBackground()
        initSmoothScroller()
        getRecyclerView(view)?.addItemDecoration(ChatBubbleItemDecorator(setDateIndicator()))

        chatbotAdapter = adapter as ChatbotAdapter
        return view
    }

    private fun initSmoothScroller(){
        smoothScroll = SmoothScroller(context)
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
        if (isArticleEntry && !isArticleDataSent) {
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
            isArticleDataSent(true)
        }

    }

    private fun isArticleDataSent(dataSentState: Boolean) {
        isArticleDataSent = dataSentState
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
                getUserSession(),
                this
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

        initRecyclerViewListener()
        setupBeforeReplyTime()

        if (savedInstanceState != null)
            this.attribute = savedInstanceState.getParcelable(this.CSAT_ATTRIBUTES) ?: Attributes()

    }
    private  val TAG = "ChatbotFragment2"

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
                sendAnalytics = { impressionType ->
                    chatbotAnalytics.get().eventShowView(impressionType)
            }
        )
    }

    private fun pickVideoFromDevice(){

        activity?.let {
            val intent = context?.let { context ->
                MediaPicker.intentWithGalleryFirst(context) {
                    pageSource(PageSource.ChatBot)
                    modeType(ModeType.VIDEO_ONLY)
                    multipleSelectionMode()
                    maxVideoItem(MAX_MEDIA_COUNT)
                    maxVideoDuration(MAX_DURATION_FOR_VIDEO)
                }
            }
            startActivityForResult(intent, REQUEST_CODE_CHAT_VIDEO)
        }
    }

    private fun pickImageFromDevice() {
        activity?.let {
            val intent = context?.let { context ->
                MediaPicker.intentWithGalleryFirst(context) {
                    pageSource(PageSource.ChatBot)
                    modeType(ModeType.IMAGE_ONLY)
                    multipleSelectionMode()
                }
            }
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
            presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChatFirstTime(), onGetChatRatingListMessageError)
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
        showTopLoading()
        presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChatFirstTime(), onGetChatRatingListMessageError)
    }

    private fun onSuccessGetExistingChatFirstTime(): (ChatroomViewModel, ChatReplies) -> Unit {
        return { chatroomViewModel, chatReplies ->
            val list = chatroomViewModel.listChat.filter {
                !((it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                        (it is MessageUiModel && it.message.isEmpty()))
            }

            updateViewData(chatroomViewModel)
            renderList(list)
            getViewState()?.onSuccessLoadFirstTime(chatroomViewModel)

            updateHasNextState(chatReplies)
            updateHasNextAfterState(chatReplies)
            enableLoadMore()
            checkReplyBubbleOnboardingStatus()
            replyBubbleContainer?.setReplyListener(this)
        }
    }

    private fun onSuccessResetChatToFirstPage(): (ChatroomViewModel, ChatReplies) -> Unit {
        return { chatroomViewModel, chatReplies ->
            val list = chatroomViewModel.listChat.filter {
                !((it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                        (it is MessageUiModel && it.message.isEmpty()))
            }
            if (list.isNotEmpty()) {
                val filteredList = getViewState()?.clearDuplicate(list)
                filteredList?.let { renderList ->
                    renderList(renderList)
                }
                getViewState()?.scrollToBottom()
                updateHasNextState(chatReplies)
                updateHasNextAfterState(chatReplies)
                enableLoadMore()
                replyBubbleContainer?.setReplyListener(this)
            }

        }
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
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        sendEventForWelcomeMessage(visitable)
        manageActionBubble(visitable)
        managePreviousStateOfBubble(visitable)
        manageVideoBubble()
        mapMessageToList(visitable)
        getViewState()?.hideEmptyMessage(visitable)
        getViewState()?.onCheckToHideQuickReply(visitable)
    }

    private fun manageVideoBubble() {
        getViewState()?.hideDummyVideoAttachment()
    }

    private fun managePreviousStateOfBubble(visitable: Visitable<*>) {
        if(visitable is MessageUiModel && visitable.attachmentType != SESSION_CHANGE){
            getViewState()?.hideInvoiceList()
            getViewState()?.hideHelpfullOptions()
        }
    }

    private fun manageActionBubble(visitable: Visitable<*>) {
        when {
            (visitable is MessageUiModel && visitable.attachmentType != SESSION_CHANGE) -> hideActionBubble()
            visitable is AttachInvoiceSentUiModel && visitable.isSender -> hideActionBubble()
        }
    }

    private fun hideActionBubble() {
        getViewState()?.hideActionBubbleOnSenderMsg()
    }

    private fun sendEventForWelcomeMessage(visitable: Visitable<*>) {
        if (visitable is BaseChatUiModel && visitable.message.contains(WELCOME_MESSAGE_VALIDATION)) {
            chatbotAnalytics.get().eventShowView(ACTION_IMPRESSION_WELCOME_MESSAGE)
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
            val bottomSheetUnify = TransactionInvoiceBottomSheet.newInstance(it, messageId.convertMessageIdToLong(), this)
            bottomSheetUnify.clearContentPadding = true
            bottomSheetUnify.show(childFragmentManager, "")
        }
    }

    override fun onQuickReplyClicked(model: QuickReplyViewModel) {
        chatbotAnalytics.get().eventClick(ACTION_QUICK_REPLY_BUTTON_CLICKED)
        presenter.sendQuickReply(messageId, model, SendableUiModel.generateStartTime(), opponentId)
        getViewState()?.hideQuickReplyOnClick()
    }

    override fun onImageUploadClicked(imageUrl: String, replyTime: String, isSecure: Boolean) {

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
            REQUEST_CODE_CHAT_VIDEO -> onPickedAttachVideo(resultCode,data)
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

        getViewState()?.scrollToBottom()

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
        try {
            val selectedOption = data?.getStringExtra(SELECTED_ITEMS)?.split(";")
            var filters = ""
            if (!selectedOption.isNullOrEmpty()) {
                for (filter in selectedOption) {
                    if (filter.isNotEmpty())
                        filters += reasonList?.get(filter.toIntOrZero()) + ","
                }
                return filters.substring(0, filters.length - 1)
            }
            return ""
        } catch (e : Exception) {
            return ""
        }
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

    private fun onPickedAttachVideo(resultCode: Int, data: Intent?){
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        uploadVideo(data)
    }

    private fun uploadVideo(data: Intent) {
        val paths = MediaPicker.result(data)
        paths.originalPaths.forEach { path ->
            processVideoPathToUpload(path)?.let { videoUploadUiModel ->
                getViewState()?.onVideoUpload(videoUploadUiModel)
                sendAnalyticsForVideoUpload(path)
                presenter.uploadVideo(
                    videoUploadUiModel,
                    SOURCE_ID_FOR_VIDEO_UPLOAD,
                    SendableUiModel.generateStartTime(),
                    messageId,
                    onErrorVideoUpload()
                )
            }
        }
    }

    //TODO clear doubts here - do we need to release it
    private fun sendAnalyticsForVideoUpload(videoFilePath : String) {
        val videoFile = File(videoFilePath)
        val extension = VideoUtil.findVideoExtension(videoFile)
        val videoSize = VideoUtil.findVideoSize(videoFile)
        chatbotAnalytics.get().eventOnVideoUpload(videoFilePath, extension, videoSize)
    }

    private fun processVideoPathToUpload(path: String): VideoUploadUiModel? {
        val totalLength = VideoUtil.retrieveVideoLength(context, path)

        if (!TextUtils.isEmpty(path)) {
            return generateChatViewModelWithVideo(path, totalLength)
        }

        return null

    }

    private fun generateChatViewModelWithVideo(video: String, totalLength: Long): VideoUploadUiModel {
        return VideoUploadUiModel.Builder().withMsgId(messageId)
            .withFromUid(opponentId)
            .withAttachmentId((System.currentTimeMillis() / ONE_SECOND_IN_MILLISECONDS).toString())
            .withAttachmentType(AttachmentType.Companion.TYPE_IMAGE_UPLOAD)
            .withReplyTime(SendableUiModel.SENDING_TEXT)
            .withStartTime(SendableUiModel.generateStartTime())
            .withVideoUrl(video)
            .withIsDummy(true)
            .withLength(totalLength)
            .build()

    }

    override fun uploadUsingSecureUpload(data: Intent) {
        val paths = MediaPicker.result(data)
        paths.originalPaths.forEach { path ->
            processImagePathToUpload(path)?.let { imageUploadUiModel ->
                getViewState()?.onImageUpload(imageUploadUiModel)
                presenter.uploadImageSecureUpload(imageUploadUiModel, messageId, opponentId, onErrorImageUpload(), path, context)
            }

        }
    }

    override fun uploadUsingOldMechanism(data: Intent) {
        val paths = MediaPicker.result(data)
        paths.originalPaths.forEach { path ->
            processImagePathToUpload(path)?.let { imageUploadUiModel ->
                getViewState()?.onImageUpload(imageUploadUiModel)
                presenter.uploadImages(
                    imageUploadUiModel,
                    messageId,
                    opponentId,
                    onErrorImageUpload()
                )
            }

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

    private fun processImagePathToUpload(path: String): ImageUploadUiModel? {
        if (!TextUtils.isEmpty(path)) {
            val imageUiModel = generateChatViewModelWithImage(path)
            return imageUiModel
        }
        return null
    }

    private fun generateChatViewModelWithImage(imageUrl: String): ImageUploadUiModel {
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
        chatbotAnalytics.get().eventClick(ACTION_REPLY_BUTTON_CLICKED)
        val sendMessage = replyEditText.text.toString()
        val startTime = SendableUiModel.generateStartTime()

        presenter.sendMessage(
            messageId,
            sendMessage,
            startTime,
            opponentId,
            replyBubbleContainer.referredMsg,
            onSendingMessage(sendMessage, startTime, replyBubbleContainer?.referredMsg)
        )

        replyBubbleOnBoarding.dismiss()
        visibilityReplyBubble(false)
        clearChatText()
    }


    private fun onSendingMessage(sendMessage: String, startTime: String,parentReply: ParentReply?): () -> Unit {
        return {
            if (rvScrollListener?.hasNextAfterPage == true) {
                resetData()
                showTopLoading()
                presenter.getExistingChat(messageId, onError(), onSuccessResetChatToFirstPage(), onGetChatRatingListMessageError)
            } else {
                getViewState()?.onSendingMessage(
                    messageId, getUserSession().userId, getUserSession()
                        .name, sendMessage, startTime, parentReply
                )
                getViewState()?.scrollToBottom()
            }
        }
    }

    override fun onChatActionBalloonSelected(selected: ChatActionBubbleViewModel, model: ChatActionSelectionBubbleViewModel) {
        chatbotAnalytics.get().eventClick(ACTION_ACTION_BUBBLE_CLICKED)
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
            chatbotAnalytics.get().eventClick(ACTION_THUMBS_UP_BUTTON_CLICKED)
        } else {
            chatbotAnalytics.get().eventClick(ACTION_THUMBS_DOWN_BUTTON_CLICKED)
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

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(
            activity, LinearLayoutManager.VERTICAL, true
        ).also {
            rvLayoutManager = it
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::mCsatResponse.isInitialized)
            outState.putParcelable(CSAT_ATTRIBUTES, mCsatResponse.attachment?.attributes)
    }

    override fun onDestroy() {
        super.onDestroy()
        replyBubbleOnBoarding.flush()
        presenter.detachView()
    }

    override fun createAttachmentMenus(): List<AttachmentMenu> {
        var list = mutableListOf<AttachmentMenu>()
        if(isConnectedToAgent){
            attachmentMenuRecyclerView?.addVideoAttachmentMenu()
        } else {
            list.add(ChatbotImageMenu())
        }
        return list

    }

    override fun onClickAttachImage(menu: AttachmentMenu) {
        super.onClickAttachImage(menu)
        pickImageFromDevice()
    }

    override fun onClickAttachVideo(menu: AttachmentMenu) {
        super.onClickAttachVideo(menu)
        pickVideoFromDevice()
        chatbotAnalytics?.get().eventOnVideoPick()
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
                onSendingMessage(sendMessage, startTime,null))
    }

    override fun csatOptionListSelected(selected: ChatOptionListViewModel, model: CsatOptionsViewModel?) {
        csatOptionsViewModel = model
        startActivityForResult(context?.let {
            ChatBotCsatActivity
                    .getInstance(it, selected.value, model)
        }, REQUEST_SUBMIT_CSAT)
    }

    override fun onRetrySendImage(element: ImageUploadUiModel) {
        createRetryMediaUploadBottomSheet(element)
    }

    private fun onBottomSheetItemClicked(
        element: SendableUiModel,
        bottomSheetPage: BottomSheetUnify
    ): (Int) -> Unit {
        return {
            if (element is ImageUploadUiModel) {
                when (it) {
                    RESEND -> handleImageResendBottomSheet(element, bottomSheetPage)
                    DELETE -> handleImageDeleteBottomSheet(element, bottomSheetPage)
                }
            } else if (element is VideoUploadUiModel) {
                when (it) {
                    RESEND -> handleVideoResendBottomSheet(element, bottomSheetPage)
                    DELETE -> handleVideoDeleteBottomSheet(element, bottomSheetPage)
                }
            }
        }
    }

    private fun handleImageResendBottomSheet(element: ImageUploadUiModel,bottomSheetPage: BottomSheetUnify) {
        removeDummy(element)
        getViewState()?.onImageUpload(element)
        presenter.uploadImages(element, messageId, opponentId, onErrorImageUpload())
        bottomSheetPage.dismiss()
    }

    private fun handleImageDeleteBottomSheet(element: ImageUploadUiModel,bottomSheetPage: BottomSheetUnify) {
        removeDummy(element)
        bottomSheetPage.dismiss()
        view?.let {
            Toaster.make(
                it,
                context?.getString(R.string.chatbot_your_picture_has_been_deleted)
                    ?: "",
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL
            )
        }
    }

    private fun handleVideoResendBottomSheet(element: VideoUploadUiModel,bottomSheetPage: BottomSheetUnify) {
        removeDummy(element)
        bottomSheetPage.dismiss()
        element.isRetry = false
        getViewState()?.onVideoUpload(element)
        presenter.uploadVideo(element, SOURCE_ID_FOR_VIDEO_UPLOAD, SendableUiModel.generateStartTime(), messageId, onErrorVideoUpload())
    }

    private fun handleVideoDeleteBottomSheet(element: VideoUploadUiModel,bottomSheetPage: BottomSheetUnify) {
        removeDummy(element)
        bottomSheetPage.dismiss()
        view?.let {
            Toaster.make(
                it,
                context?.getString(R.string.chatbot_your_video_has_been_deleted)
                    ?: "",
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL
            )
        }
    }

    private fun onErrorVideoUpload(): (String,VideoUploadUiModel) -> Unit {
        return { errorMsg,video ->
            if (view != null) {
                Toaster.build(view!!, errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                getViewState()?.showRetryUploadVideos(video)
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
                                    onSendingMessage(replyText, startTime,null))
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
        getViewState()?.handleReplyBox(true)
    }

    override fun getUserName(): String {
        return senderNameForReply
    }

    override fun showReplyOption(messageUiModel: MessageUiModel) {
        if (replyBubbleEnabled) {
            val bottomSheetPage = BottomSheetUnify()
            val viewBottomSheetPage = initBottomSheetForReply(bottomSheetPage, messageUiModel)

            bottomSheetPage.apply {
                setTitle(this@ChatbotFragment.context?.getString(R.string.chatbot_reply_bubble_bottomsheet_title)?: "")
                showCloseIcon = true
                setChild(viewBottomSheetPage)
                showKnob = false
            }

            fragmentManager?.let {
                bottomSheetPage.show(it, getString(R.string.chatbot_reply_bubble_bottomsheet_retry))
            }
        }
    }

    private fun initBottomSheetForReply(
        bottomSheetPage: BottomSheetUnify,
        messageUiModel: MessageUiModel
    ) : View {
        return View.inflate(context, R.layout.reply_bubble_bottom_sheet_layout, null).apply {
            val rvPages = findViewById<RecyclerView>(R.id.rv_reply_bubble)
            rvPages.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val adapter =
                ReplyBubbleBottomSheetAdapter(onReplyBottomSheetItemClicked(bottomSheetPage,messageUiModel))
            rvPages.adapter = adapter
        }
    }

    override fun goToBubble(parentReply: ParentReply) {
        val bubblePosition = chatbotAdapter.getBubblePosition (
            parentReply.replyTime
        )
        if (bubblePosition != RecyclerView.NO_POSITION) {
            smoothScrollToPosition(bubblePosition)
        } else {
            resetData()
            setupBeforeReplyTime(((parentReply.replyTimeMillisOffset).toLongOrZero() + ONCLICK_REPLY_TIME_OFFSET_FOR_REPLY_BUBBLE).toString())
            loadDataOnClick(parentReply.replyTime)
        }
    }

    private fun loadDataOnClick(replyTime : String){
        showTopLoading()
        presenter.getTopChat(messageId, onSuccessGetTopChatData(replyTime = replyTime, fromOnClick = true), onErrorGetTopChat(), onGetChatRatingListMessageError)
    }

    private fun onReplyBottomSheetItemClicked(bottomSheetPage: BottomSheetUnify,messageUiModel: MessageUiModel): (position: Int) -> Unit {
        return {
            when (it) {
                REPLY -> {
                    replyBubbleOnBoarding.dismiss()
                    senderNameForReply = messageUiModel.from
                    replyBubbleContainer.composeReplyData(messageUiModel,"",true, getUserNameForReplyBubble.getUserName(messageUiModel))
                    bottomSheetPage.dismiss()
                }
            }
        }
    }


    override fun replyBubbleStateHandler(state: Boolean) {
        replyBubbleEnabled = state
        checkReplyBubbleOnboardingStatus()
    }

    private fun checkReplyBubbleOnboardingStatus() {
        val hasBeenShown = replyBubbleOnBoarding.hasBeenShown()
        if (!replyBubbleEnabled)
            return
        recyclerView?.let {
            if (!hasBeenShown){
                replyBubbleOnBoarding.showReplyBubbleOnBoarding(it,
                    chatbotAdapter,
                    reply_box, context)
            }
        }
    }

    override fun visibilityReplyBubble(state: Boolean) {
        if (!state) {
            replyBubbleContainer?.referredMsg = null
            replyBubbleContainer.hide()
        }else{
            replyBubbleContainer.show()
        }
    }

    private fun initRecyclerViewListener() {
        rvScrollListener = object : RecyclerViewScrollListener((recyclerView?.layoutManager as LinearLayoutManager)) {
            override fun loadMoreTop() {
                showTopLoading()
                presenter.getTopChat(messageId, onSuccessGetTopChatData(), onErrorGetTopChat(), onGetChatRatingListMessageError)
            }

            override fun loadMoreDown() {
               showBottomLoading()
               presenter.getBottomChat(messageId, onSuccessGetBottomChatData(), onErrorGetBottomChat(), onGetChatRatingListMessageError)
            }
        }.also {
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun onErrorGetBottomChat() : (Throwable) -> Unit =  {
        chatbotAdapter.hideBottomLoading()
        onError()
        rvScrollListener?.finishBottomLoadingState()
    }

    private fun onErrorGetTopChat() : (Throwable) -> Unit = {
        hideTopLoading()
        onError()
        rvScrollListener?.finishTopLoadingState()
    }

    private fun hideTopLoading() {
        hideLoading()
    }

    private fun onSuccessGetTopChatData(replyTime: String = "", fromOnClick : Boolean = false): (ChatroomViewModel,ChatReplies) -> Unit {
        return { chatroom , chatReplies ->
            val list = chatroom.listChat.filter {
                !((it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                        (it is MessageUiModel && it.message.isEmpty()))
            }
            if (list.isNotEmpty()){
                val filteredList= getViewState()?.clearDuplicate(list)
                updateHasNextState(chatReplies)
                rvScrollListener?.finishTopLoadingState()
                filteredList?.let { renderList ->
                    renderTopList(renderList)
                    if (fromOnClick) {
                        val bubblePosition = chatbotAdapter.getBubblePosition(
                            replyTime
                        )
                        if (bubblePosition != RecyclerView.NO_POSITION) {
                            smoothScrollToPosition(bubblePosition)
                        }

                    }
                }
                if (fromOnClick && replyTime.isNotEmpty()) {
                    updateHasNextAfterState(chatReplies)
                }
            }else{
                presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChatFirstTime(), onGetChatRatingListMessageError)
            }
        }
    }

    private fun onSuccessGetBottomChatData(replyTime: String = "", fromOnClick: Boolean = false): (ChatroomViewModel,ChatReplies) -> Unit {
        return { chatroom , chatReplies ->
            val list = chatroom.listChat.filter {
                !((it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                        (it is MessageUiModel && it.message.isEmpty()))
            }
            if (list.isNotEmpty()){
                val filteredList= getViewState()?.clearDuplicate(list)
                rvScrollListener?.finishBottomLoadingState()
                if (filteredList?.isNotEmpty()==true) {
                    renderBottomList(filteredList)
                } else{
                    presenter.getBottomChat(messageId, onSuccessGetBottomChatData(), onErrorGetBottomChat(), onGetChatRatingListMessageError)
                }
                updateHasNextAfterState(chatReplies)
            }
            else{
                if(rvScrollListener?.hasNextAfterPage==true)
                    presenter.getBottomChat(messageId, onSuccessGetBottomChatData(), onErrorGetBottomChat(), onGetChatRatingListMessageError)
                else {
                    chatbotAdapter.hideBottomLoading()
                    rvScrollListener?.finishBottomLoadingState()
                }
            }
        }
    }

    private fun smoothScrollToPosition(bubblePosition: Int) {
        smoothScroll?.targetPosition = bubblePosition
        (recyclerView?.layoutManager as LinearLayoutManager).startSmoothScroll(
            smoothScroll
        )
    }

    private fun showTopLoading() {
        chatbotAdapter.showTopLoading()
    }

    private fun showBottomLoading() {
        chatbotAdapter.showBottomLoading()
    }

    private fun setupBeforeReplyTime(replyTimeMillis: String) {
        messageCreateTime = replyTimeMillis
        setupBeforeReplyTime()
    }

    private fun setupBeforeReplyTime() {
        if (messageCreateTime.isNotEmpty()) {
            presenter.setBeforeReplyTime(messageCreateTime)
        }
    }

    private fun updateHasNextState(chat: ChatReplies) {
        val hasNext = chat.hasNext
        rvScrollListener?.updateHasNextState(chat)
        if (hasNext) {
            showTopLoading()
        }
    }

    private fun updateHasNextAfterState(chat: ChatReplies) {
        val hasNextAfter = chat.hasNextAfter
        rvScrollListener?.updateHasNextAfterState(chat)
        if (hasNextAfter) {
            showBottomLoading()
        }
    }

    private fun renderBottomList(listChat: List<Visitable<*>>) {
        chatbotAdapter?.hideBottomLoading()
        if (listChat.isNotEmpty()) {
            chatbotAdapter?.addBottomData(listChat)
        }
    }

    private fun renderTopList(listChat: List<Visitable<*>>) {
        chatbotAdapter?.hideTopLoading()
        if (listChat.isNotEmpty()) {
            chatbotAdapter?.addTopData(listChat)
        }
    }

    private fun resetData(){
        rvScrollListener?.reset()
        presenter.clearGetChatUseCase()
        chatbotAdapter.reset()
        showTopLoading()
    }


    override fun onRetrySendVideo(element: VideoUploadUiModel) {
        createRetryMediaUploadBottomSheet(element)
    }

    override fun onVideoUploadCancelClicked(video: VideoUploadUiModel) {
        presenter.cancelVideoUpload(video.videoUrl!!,SOURCE_ID_FOR_VIDEO_UPLOAD, onError())
        getViewState()?.showRetryUploadVideos(video)
    }

    override fun onUploadedVideoClicked(videoUrl: String) {
        val intent = Intent(activity, ChatbotVideoActivity::class.java)
        intent.putExtra(
            VIDEO_URL,
            videoUrl
        )
        startActivity(intent)
    }

    private fun createRetryMediaUploadBottomSheet(element: SendableUiModel) {
        val bottomSheetPage = BottomSheetUnify()
        val viewBottomSheetPage =
            View.inflate(context, R.layout.retry_upload_media_bottom_sheet_layout, null).apply {
                setUpMediaRetryBottomSheet(this, element, bottomSheetPage)
            }

        bottomSheetPage.apply {
            if (element is ImageUploadUiModel)
                setTitle(
                    this@ChatbotFragment.context?.getString(R.string.chatbot_retry_image_upload_bottom_sheet_title)
                        ?: ""
                )
            else
                setTitle(
                    this@ChatbotFragment.context?.getString(R.string.chatbot_retry_video_upload_bottom_sheet_title)
                        ?: ""
                )
            showCloseIcon = false
            setChild(viewBottomSheetPage)
            showKnob = true
        }
        fragmentManager?.let {
            bottomSheetPage.show(it, "retry media bottom sheet")
        }
    }

    private fun setUpMediaRetryBottomSheet(view: View, element: SendableUiModel, bottomSheetPage: BottomSheetUnify) {
        val rvPages = view.findViewById<RecyclerView>(R.id.rv_image_upload_option)
        rvPages.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter =
            MediaRetryBottomSheetAdapter(onBottomSheetItemClicked(element, bottomSheetPage))
        rvPages.adapter = adapter
        adapter.setList(
            listOf<String>(
                context?.getString(R.string.chatbot_delete)
                    ?: "", context?.getString(R.string.chatbot_resend) ?: ""
            )
        )
    }

    override fun sessionChangeStateHandler(state: Boolean) {
        isConnectedToAgent = state
        createAttachmentMenus()
    }
}

