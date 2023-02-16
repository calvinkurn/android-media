package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.CONTACT_US_NATIVE
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.FallbackAttachmentUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.Attachment
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.widget.AttachmentMenuRecyclerView
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.SESSION_CHANGE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_ENTRY
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_ID
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_TITLE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.CODE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.EVENT
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.FALSE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.IMAGE_URL
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.IS_ATTACHED
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.STATUS
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.STATUS_COLOR
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.USED_BY
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_FIVE
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_FOUR
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_ONE
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_THREE
import com.tokopedia.chatbot.ChatbotConstant.CsatRating.RATING_TWO
import com.tokopedia.chatbot.ChatbotConstant.ONE_SECOND_IN_MILLISECONDS
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHATBOT_ONBOARDING
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHAT_IMAGE
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHAT_VIDEO
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_CSAT
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_FEEDBACK
import com.tokopedia.chatbot.ChatbotConstant.ReplyBoxType.DYNAMIC_ATTACHMENT
import com.tokopedia.chatbot.ChatbotConstant.ReplyBoxType.REPLY_BOX_TOGGLE_VALUE
import com.tokopedia.chatbot.ChatbotConstant.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE
import com.tokopedia.chatbot.ChatbotConstant.VIDEO_URL
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_DURATION_FOR_VIDEO
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_IMAGE_COUNT
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_MEDIA_ITEM_COUNT
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_VIDEO_COUNT
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.SOURCE_ID_FOR_VIDEO_UPLOAD
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.RemoteConfigHelper
import com.tokopedia.chatbot.analytics.ChatbotAnalytics
import com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.attachinvoice.view.TransactionInvoiceBottomSheet
import com.tokopedia.chatbot.attachinvoice.view.TransactionInvoiceBottomSheetListener
import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.databinding.FragmentChatbotBinding
import com.tokopedia.chatbot.databinding.RetryUploadMediaBottomSheetLayoutBinding
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.Attributes
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.replyBox.DynamicAttachment
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.util.ChatBubbleItemDecorator
import com.tokopedia.chatbot.util.GetUserNameForReplyBubble
import com.tokopedia.chatbot.util.SmoothScroller
import com.tokopedia.chatbot.util.VideoUploadData
import com.tokopedia.chatbot.util.VideoUtil
import com.tokopedia.chatbot.util.convertMessageIdToLong
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.chatbot.view.activity.ChatBotCsatActivity
import com.tokopedia.chatbot.view.activity.ChatBotProvideRatingActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity.Companion.DEEP_LINK_URI
import com.tokopedia.chatbot.view.activity.ChatbotActivity.Companion.PAGE_SOURCE
import com.tokopedia.chatbot.view.activity.ChatbotOnboardingActivity
import com.tokopedia.chatbot.view.activity.ChatbotVideoActivity
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactoryImpl
import com.tokopedia.chatbot.view.adapter.MediaRetryBottomSheetAdapter
import com.tokopedia.chatbot.view.adapter.util.RecyclerViewScrollListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.VideoUploadListener
import com.tokopedia.chatbot.view.attachmentmenu.ChatbotImageMenu
import com.tokopedia.chatbot.view.bottomsheet.ChatbotReplyBottomSheet
import com.tokopedia.chatbot.view.bottomsheet.ChatbotReplyBottomSheetAdapter
import com.tokopedia.chatbot.view.customview.ChatbotFloatingInvoice
import com.tokopedia.chatbot.view.customview.chatroom.BigReplyBox
import com.tokopedia.chatbot.view.customview.chatroom.BigReplyBoxBottomSheet
import com.tokopedia.chatbot.view.customview.chatroom.SmallReplyBox
import com.tokopedia.chatbot.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleOnBoarding
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.chatbot.view.listener.ChatbotViewState
import com.tokopedia.chatbot.view.listener.ChatbotViewStateImpl
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.chatbot.view.uimodel.ChatbotReplyOptionsUiModel
import com.tokopedia.chatbot.view.util.CheckDynamicAttachmentValidity
import com.tokopedia.chatbot.view.util.InvoiceStatusLabelHelper
import com.tokopedia.chatbot.view.util.showToaster
import com.tokopedia.chatbot.websocket.ChatbotWebSocketException
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import java.io.InterruptedIOException
import java.util.*
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
private const val ACTION_IMPRESSION_CSAT_SMILEY_VIEW = "impression csat smiley form"
private const val ACTION_IMPRESSION_WELCOME_MESSAGE = "impression welcome message"
private const val WELCOME_MESSAGE_VALIDATION = "dengan Toped di sini"
private const val RESEND = 1
private const val DELETE = 0
private const val REPLY = 0
private const val SEE_ALL_INVOICE_TEXT = "lihat_semua_transaksi"

class ChatbotFragment :
    BaseChatFragment(),
    ChatbotContract.View,
    AttachedInvoiceSelectionListener,
    QuickReplyListener,
    ChatActionListBubbleListener,
    ChatRatingListener,
    TypingListener,
    ChatOptionListListener,
    CsatOptionListListener,
    View.OnClickListener,
    TransactionInvoiceBottomSheetListener,
    StickyActionButtonClickListener,
    VideoUploadListener,
    AttachmentMenu.AttachmentMenuListener,
    ReplyBubbleAreaMessage.Listener,
    ChatbotSendButtonListener,
    ChatbotFloatingInvoice.InvoiceListener,
    ReplyBoxClickListener,
    ChatbotReplyBottomSheetAdapter.ReplyBubbleBottomSheetListener {

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

    private var _viewBinding: FragmentChatbotBinding? = null
    private fun getBindingView() = _viewBinding!!

    lateinit var mCsatResponse: WebSocketCsatResponse
    lateinit var attribute: Attributes
    private var ticker: Ticker? = null
    private var dateIndicator: Typography? = null
    private var dateIndicatorContainer: CardView? = null
    private var csatOptionsUiModel: CsatOptionsUiModel? = null
    private var invoiceRefNum = ""
    private var replyText = ""
    private var isStickyButtonClicked = false
    private var isChatRefreshed = false
    private var isFirstPage = true
    private var isArticleEntry = false
    private var hashMap: Map<String, String> = HashMap<String, String>()
    var isAttached: Boolean = false
    private var floatingInvoice: ChatbotFloatingInvoice? = null
    private var isSendButtonActivated: Boolean = true
    private var isFloatingSendButton: Boolean = false
    private var isFloatingInvoiceCancelled: Boolean = false

    private var isConnectedToAgent: Boolean = false
    private var attachmentMenuRecyclerView: AttachmentMenuRecyclerView? = null
    private var smallReplyBox: SmallReplyBox? = null
    private var bigReplyBox: BigReplyBox? = null
    private var replyBubbleContainer: ReplyBubbleAreaMessage? = null
    private var replyBubbleEnabled: Boolean = false
    private var senderNameForReply = ""
    private var smoothScroll: SmoothScroller? = null
    private var rvScrollListener: RecyclerViewScrollListener? = null
    private var rvLayoutManager: LinearLayoutManager? = null
    private var messageCreateTime: String = ""
    private lateinit var chatbotAdapter: ChatbotAdapter
    private var isEligibleForVideoUplaod: Boolean = false
    private var guideline: Guideline? = null
    private var chatbotViewStateImpl: ChatbotViewStateImpl? = null
    private var replyBoxBottomSheetPlaceHolder: String = ""
    private var replyBoxBottomSheetTitle: String = ""
    var xForReplyBubbleOnboarding: Int = 0
    var yForReplyBubbleOnboarding: Int = 0
    var replyBubbleOnBoardingHasBeenShow: Boolean = false
    var videoUploadOnBoardingHasBeenShow: Boolean = false
    private val coachmarkHandler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var replyBubbleOnBoarding: ReplyBubbleOnBoarding

    @Inject
    lateinit var videoUploadOnBoarding: VideoUploadOnBoarding
    private var recyclerView: RecyclerView? = null
    private var isArticleDataSent: Boolean = false

    @Inject
    lateinit var getUserNameForReplyBubble: GetUserNameForReplyBubble
    private var csatRemoteConfig: Boolean = false
    private var replyBubbleBottomSheet: ChatbotReplyBottomSheet? = null

    companion object {
        private const val ONCLICK_REPLY_TIME_OFFSET_FOR_REPLY_BUBBLE = 5000
        private const val GUIDELINE_VALUE_FOR_REPLY_BUBBLE = 65
        private const val DEFAULT_GUIDELINE_VALUE_FOR_REPLY_BUBBLE = 0
        private const val Y_COORDINATE = "y-coordinate"
        private const val ZERO_POSITION = 0
        private const val BUBBLE_NOT_FOUND = -2
        private const val DELAY_TO_SHOW_COACHMARK = 1000L
        private const val COPY_TO_CLIPBOARD_LABEL = "Tokopedia-Chatbot"
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
                ((activity as Activity).application as BaseMainApplication).baseAppComponent
            )
                .chatbotModule(context?.let { ChatbotModule(it) })
                .build()

            chatbotComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun onClick(v: View?) {
        smallReplyBox?.hide()
        val id = v?.id
        if (id == getBindingView().chatbotViewHelpRate.btnInactive1.id ||
            id == getBindingView().chatbotViewHelpRate.btnInactive2.id ||
            id == getBindingView().chatbotViewHelpRate.btnInactive3.id ||
            id == getBindingView().chatbotViewHelpRate.btnInactive4.id ||
            id == getBindingView().chatbotViewHelpRate.btnInactive5.id
        ) {
            onEmojiClick(v)
        }
    }

    private fun onEmojiClick(view: View?) {
        when (view?.id) {
            getBindingView().chatbotViewHelpRate.btnInactive1.id -> {
                onClickEmoji(RATING_ONE)
            }
            getBindingView().chatbotViewHelpRate.btnInactive2.id -> {
                onClickEmoji(RATING_TWO)
            }
            getBindingView().chatbotViewHelpRate.btnInactive3.id -> {
                onClickEmoji(RATING_THREE)
            }
            getBindingView().chatbotViewHelpRate.btnInactive4.id -> {
                onClickEmoji(RATING_FOUR)
            }
            getBindingView().chatbotViewHelpRate.btnInactive5.id -> {
                onClickEmoji(RATING_FIVE)
            }
        }
    }

    override fun openCsat(csatResponse: WebSocketCsatResponse) {
        if (csatRemoteConfig) {
            openCsatNewFlow(csatResponse)
        } else {
            openCsatOldFlow(csatResponse)
        }
    }

    private fun openCsatOldFlow(csatResponse: WebSocketCsatResponse) {
        mCsatResponse = csatResponse
        if (::mCsatResponse.isInitialized) {
            getBindingView().listQuickReply.hide()
            showCsatRatingViewOldFlow()
        }
    }

    private fun openCsatNewFlow(csatResponse: WebSocketCsatResponse) {
        mCsatResponse = csatResponse
        if (::mCsatResponse.isInitialized) {
            showCsatRatingViewNewFlow()
        }
    }

    private fun showCsatRatingViewOldFlow() {
        getBindingView().chatbotViewHelpRate.txtHelpTitle.text =
            mCsatResponse.attachment?.attributes?.title
        getBindingView().chatbotViewHelpRate.layoutOfRate.show()
        chatbotAnalytics.get().eventShowView(ACTION_IMPRESSION_CSAT_SMILEY_VIEW)
        hideKeyboard()
    }

    private fun showCsatRatingViewNewFlow() {
        chatbotAnalytics.get().eventShowView(ACTION_IMPRESSION_CSAT_SMILEY_VIEW)
        hideKeyboard()
        smallReplyBox?.hide()
        onClickEmoji(RATING_FIVE)
    }

    private fun hideCsatRatingView() {
        getBindingView().chatbotViewHelpRate.layoutOfRate.hide()
    }

    private fun onClickEmoji(number: Int) {
        startActivityForResult(
            context?.let {
                ChatBotProvideRatingActivity
                    .getInstance(it, number, mCsatResponse)
            },
            REQUEST_SUBMIT_FEEDBACK
        )
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
            val uri: Uri = Uri.parse(intentData)

            isAttached = checkForIsAttachedInvoice(uri)
            hashMap = presenter.getValuesForArticleEntry(uri)
            isArticleEntry = checkForArticleEntry(uri)
        }

        _viewBinding = FragmentChatbotBinding.inflate(inflater, container, false)
        return getBindingView().root
    }

    private fun setUpFloatingInvoiceListeners() {
        floatingInvoice?.sendButtonListener = this
        floatingInvoice?.invoiceListener = this
    }

    private fun setUpBigReplyBoxListeners() {
        bigReplyBox?.replyBoxClickListener = this
        bigReplyBox?.sendButtonListener = this
    }
    private fun initSmoothScroller() {
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
                    val invoice: InvoiceLinkPojo =
                        AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(
                            attachInvoiceSingleViewModel
                        )
                    val generatedInvoice = presenter.generateInvoice(invoice, opponentId)
                    getViewState()?.onShowInvoiceToChat(generatedInvoice)
                    presenter.sendInvoiceAttachment(
                        messageId,
                        invoice,
                        generatedInvoice.startTime,
                        opponentId,
                        isArticleEntry,
                        hashMap.get(USED_BY).toBlankOrString()
                    )
                }
                if (hashMap.get(ARTICLE_ID)?.isNotEmpty() == true) {
                    val startTime = SendableUiModel.generateStartTime()
                    val msg = hashMap.get(ARTICLE_TITLE).toBlankOrString()
                    var quickReplyUiModel = QuickReplyUiModel(msg, msg, msg)

                    presenter.sendQuickReplyInvoice(
                        messageId,
                        quickReplyUiModel,
                        startTime,
                        opponentId,
                        hashMap.get(EVENT).toBlankOrString(),
                        hashMap.get(USED_BY).toBlankOrString()
                    )
                }
                enableTyping()
            } else {
                isSendButtonActivated = false

                disableSendButton()
                isFloatingSendButton = true
                val labelType = InvoiceStatusLabelHelper.getLabelType(hashMap[STATUS_COLOR])

                floatingInvoice?.setUpInvoiceData(
                    invoiceTitle = hashMap.get(CODE).toBlankOrString(),
                    invoiceIconURL = hashMap.get(IMAGE_URL).toBlankOrString(),
                    labelType = labelType,
                    labelText = hashMap.get(STATUS).toBlankOrString()
                )

                if (isFloatingSendButton) {
                    smallReplyBox?.addTextChangedListener()
                }
                floatingInvoice?.show()
            }
            isArticleDataSent(true)
        }
    }

    private fun isArticleDataSent(dataSentState: Boolean) {
        isArticleDataSent = dataSentState
    }

    private fun onSendFloatingInvoiceClicked() {
        floatingInvoice?.hide()
        smallReplyBox?.removeTextChangedListener()

        if (!isFloatingInvoiceCancelled) {
            val attachInvoiceSingleViewModel = presenter.createAttachInvoiceSingleViewModel(hashMap)
            val invoice: InvoiceLinkPojo =
                AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(
                    attachInvoiceSingleViewModel
                )
            val generatedInvoice = presenter.generateInvoice(invoice, opponentId)
            //         getViewState()?.onShowInvoiceToChat(generatedInvoice)
            presenter.sendInvoiceAttachment(
                messageId,
                invoice,
                generatedInvoice.startTime,
                opponentId,
                isArticleEntry,
                hashMap.get(USED_BY).toBlankOrString()
            )
        }

        val startTime = SendableUiModel.generateStartTime()
        val msg = smallReplyBox?.getMessage() ?: ""
        var quickReplyUiModel = QuickReplyUiModel(msg, msg, msg)

        presenter.sendQuickReplyInvoice(
            messageId,
            quickReplyUiModel,
            startTime,
            opponentId,
            hashMap.get(EVENT).toString(),
            hashMap.get(USED_BY).toString()
        )
        smallReplyBox?.clearChatText()
        isFloatingSendButton = false
    }

    private fun setChatBackground() {
        activity?.window?.setBackgroundDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.layered_chatbot_background) })
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
            this,
            this,
            getUserSession()
        )
    }

    private fun setDateIndicator(): (String) -> Unit = {
        if (it.isNotEmpty()) {
            dateIndicator?.text = it
            dateIndicatorContainer?.show()
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
        floatingInvoice = getBindingView().floatingInvoice

        ticker = getBindingView().chatbotTicker
        dateIndicator = getBindingView().dateIndicator
        dateIndicatorContainer = getBindingView().dateIndicatorContainer
        attachmentMenuRecyclerView = getBindingView().rvAttachmentMenu

        smallReplyBox = getBindingView().smallReplyBox
        bigReplyBox = getBindingView().bigReplyBox
        guideline = smallReplyBox?.getGuidelineForReplyBubble()

        smallReplyBox?.bindCommentTextBackground()
        replyBubbleContainer = smallReplyBox?.getReplyBubbleContainer()
        smallReplyBox?.listener = this

        setUpBigReplyBoxListeners()
        replyBubbleOnBoardingHasBeenShow = replyBubbleOnBoarding.hasBeenShown()
        videoUploadOnBoardingHasBeenShow = videoUploadOnBoarding.hasBeenShown()

        setUpFloatingInvoiceListeners()

        recyclerView = getRecyclerView(view)
        isFloatingInvoiceCancelled = false
        setChatBackground()
        initSmoothScroller()
        getRecyclerView(view)?.addItemDecoration(ChatBubbleItemDecorator(setDateIndicator()))
        chatbotAdapter = adapter as ChatbotAdapter

        attachListenersForRating()

        super.onViewCreated(view, savedInstanceState)
        viewState?.initView()
        showTopLoading()
        val pageSource = getParamString(PAGE_SOURCE, arguments, savedInstanceState)
        presenter.setPageSource(pageSource)
        presenter.checkForSession(messageId)
        presenter.checkUploadVideoEligibility(messageId)
        remoteConfigForCsatExperiment()
        showTicker()

        initRecyclerViewListener()
        setupBeforeReplyTime()

        if (savedInstanceState != null) {
            this.attribute = savedInstanceState.getParcelable(this.CSAT_ATTRIBUTES) ?: Attributes()
        }
    }

    private fun attachListenersForRating() {
        getBindingView().chatbotViewHelpRate.btnInactive1.setOnClickListener(this@ChatbotFragment)
        getBindingView().chatbotViewHelpRate.btnInactive2.setOnClickListener(this@ChatbotFragment)
        getBindingView().chatbotViewHelpRate.btnInactive3.setOnClickListener(this@ChatbotFragment)
        getBindingView().chatbotViewHelpRate.btnInactive4.setOnClickListener(this@ChatbotFragment)
        getBindingView().chatbotViewHelpRate.btnInactive5.setOnClickListener(this@ChatbotFragment)
    }

    private fun goToOnboardingActivity() {
        val hasBeenShownVideoUploadOnBoarding = videoUploadOnBoarding.hasBeenShown()
        val hasBeenShownReplyBubbleOnboarding = replyBubbleOnBoarding.hasBeenShown()

        if (hasBeenShownReplyBubbleOnboarding && hasBeenShownVideoUploadOnBoarding) {
            return
        }

        val intent = Intent(activity, ChatbotOnboardingActivity::class.java)
        intent.putExtra(Y_COORDINATE, yForReplyBubbleOnboarding)
        startActivityForResult(intent, REQUEST_CODE_CHATBOT_ONBOARDING)
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
        handleReplyBox(false)
    }

    override fun enableTyping() {
        handleReplyBox(true)
        swipeToRefresh.setMargin(0, 0, 0, 0)
    }

    private fun handleReplyBox(toShowSmallReplyBox: Boolean) {
        if (toShowSmallReplyBox) {
            getBindingView().addCommentArea.show()
            smallReplyBox?.show()
            bigReplyBox?.hide()
        } else {
            smallReplyBox?.hide()
            bigReplyBox?.show()
        }
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
        ).also {
            chatbotViewStateImpl = it
        }
    }

    private fun pickVideoFromDevice() {
        activity?.let {
            val intent = context?.let { context ->
                MediaPicker.intentWithGalleryFirst(context) {
                    pageSource(PageSource.ChatBot)
                    modeType(ModeType.VIDEO_ONLY)
                    multipleSelectionMode()
                    maxMediaItem(MAX_MEDIA_ITEM_COUNT)
                    maxVideoItem(MAX_VIDEO_COUNT)
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
                    maxMediaItem(MAX_IMAGE_COUNT)
                    multipleSelectionMode()
                }
            }
            startActivityForResult(intent, REQUEST_CODE_CHAT_IMAGE)
        }
    }

    private fun showTicker() {
        presenter.showTickerData(messageId)
    }

    override fun onSuccessGetTickerData(tickerData: TickerData) {
        if (!tickerData.items.isNullOrEmpty()) {
            ticker?.show()
            if (tickerData.items.size > 1) {
                showMultiTicker(tickerData)
            } else if (tickerData.items.size == 1) {
                showSingleTicker(tickerData)
            }
        }
    }

    private fun showSingleTicker(tickerData: TickerData) {
        ticker?.tickerTitle = tickerData.items?.get(0)?.title
        ticker?.setHtmlDescription(tickerData.items?.get(0)?.text ?: "")
        ticker?.tickerType = getTickerType(tickerData.type ?: "")
        ticker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                navigateToWebView(linkUrl.toString())
            }

            override fun onDismiss() {
            }
        })
    }

    private fun showMultiTicker(tickerData: TickerData) {
        val mockData = arrayListOf<com.tokopedia.unifycomponents.ticker.TickerData>()

        tickerData.items?.forEach {
            mockData.add(
                com.tokopedia.unifycomponents.ticker.TickerData(
                    it?.title,
                    it?.text ?: "",
                    getTickerType(tickerData.type ?: "")
                )
            )
        }

        val adapter = TickerPagerAdapter(activity, mockData)
        ticker?.addPagerView(adapter, mockData)
        adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                navigateToWebView(linkUrl.toString())
            }
        })
    }

    private fun navigateToWebView(linkUrl: String) {
        RouteManager.route(
            context,
            String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, linkUrl)
        )
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
        if (!isChatRefreshed && isFirstPage) {
            hideSnackBarRetry()
            presenter.getExistingChat(messageId, onShowErrorPage(), onSuccessGetExistingChatFirstTime(), onGetChatRatingListMessageError)
            swipeToRefresh.isRefreshing = true
            isChatRefreshed = true
        } else {
            swipeToRefresh.isRefreshing = false
            swipeToRefresh.isEnabled = false
            swipeToRefresh.setOnRefreshListener(null)
        }
    }

    override fun getSwipeRefreshLayoutResourceId() = 0

    override fun getRecyclerViewResourceId(): Int {
        return getBindingView().recyclerView.id
    }

    override fun loadInitialData() {
        getViewState()?.clearChatOnLoadChatHistory()
        showTopLoading()
        presenter.getExistingChat(messageId, onShowErrorPage(), onSuccessGetExistingChatFirstTime(), onGetChatRatingListMessageError)
    }

    private fun onSuccessGetExistingChatFirstTime(): (ChatroomViewModel, ChatReplies) -> Unit {
        return { chatroomViewModel, chatReplies ->
            processDynamicAttachmentFromHistoryForContentCode100(chatroomViewModel)
            processDynamicAttachmentFromHistoryForContentCode101(chatroomViewModel)
            val list = filterChatList(chatroomViewModel)

            updateViewData(chatroomViewModel)
            renderList(list)
            getViewState()?.onSuccessLoadFirstTime(chatroomViewModel)

            updateHasNextState(chatReplies)
            updateHasNextAfterState(chatReplies)
            enableLoadMore()
            replyBubbleContainer?.setReplyListener(this)
        }
    }

    /**
     * Check the first item of the list , if it is of Attachment type 34 and content code 100,
     * then need to check the whole list- to show/hide bigReplyBox,
     * condition is - check whether after receiving that attachment user has not changed anything
     * If the user has sent any message after that attachment, then return immediately else process
     * that item
     * */
    private fun processDynamicAttachmentFromHistoryForContentCode100(chatroom: ChatroomViewModel) {
        chatroom.listChat.forEach {
            if (it is MessageUiModel && it.isSender) {
                return
            }

            if (it !is FallbackAttachmentUiModel) {
                return@forEach
            }

            if (it.attachmentType != DYNAMIC_ATTACHMENT) {
                return@forEach
            }

            if (it.attachment is Attachment) {
                val attachment = it.attachment as Attachment

                try {
                    val dynamicAttachmentContents =
                        Gson().fromJson(attachment.attributes, DynamicAttachment::class.java)

                    val replyBoxAttribute =
                        dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

                    val state = presenter.validateHistoryForAttachment34(replyBoxAttribute)

                    if (state) {
                        return
                    }
                } catch (e: JsonSyntaxException) {
                    return@forEach
                }
            }
        }
    }

    /**
     * Check the first item of the list , if it is of Attachment type 34 and content code 101,
     * then need to check the whole list, whether to show/hide the reply box
     * but only take action for the first element with type 34
     * content code 101 , then return from the method, ,
     * */
    private fun processDynamicAttachmentFromHistoryForContentCode101(chatroom: ChatroomViewModel) {
        chatroom.listChat.forEach {
            if (it !is FallbackAttachmentUiModel) {
                return
            }
            if (it.attachmentType != DYNAMIC_ATTACHMENT) {
                return
            }

            if (it.attachment is Attachment) {
                val attachment = it.attachment as Attachment

                try {
                    val dynamicAttachmentContents =
                        Gson().fromJson(attachment.attributes, DynamicAttachment::class.java)

                    val replyBoxAttribute =
                        dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

                    var state = false
                    if (replyBoxAttribute?.contentCode == REPLY_BOX_TOGGLE_VALUE) {
                        state = presenter.validateHistoryForAttachment34(replyBoxAttribute)
                    }

                    if (state) {
                        return
                    }
                } catch (e: JsonSyntaxException) {
                    return
                }
            }
        }
    }

    private fun onSuccessResetChatToFirstPage(): (ChatroomViewModel, ChatReplies) -> Unit {
        return { chatroomViewModel, chatReplies ->
            val list = chatroomViewModel.listChat.filter {
                !(
                    (it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                        (it is MessageUiModel && it.message.isEmpty())
                    )
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
            Toaster.build(requireView(), it, Snackbar.LENGTH_LONG, TYPE_ERROR)
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            if (view != null) {
                Toaster.build(
                    requireView(),
                    ErrorHandler.getErrorMessage(requireView().context, it),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                )
            }
        }
    }

    private fun onShowErrorPage(): (Throwable) -> Unit {
        return {
            if (view != null) {
                setShowingErrorLayout(it)
            }
        }
    }

    private fun setErrorLayoutForServer() {
        getBindingView().layoutErrorGlobal.run {
            visible()
            getBindingView().homeGlobalError.run {
                setType(SERVER_ERROR)
                errorAction.text = context.getString(R.string.chatbot_back_to_tokopedia_care)
                errorSecondaryAction.hide()
                setActionClickListener {
                    val intent = RouteManager.getIntent(requireView().context, CONTACT_US_NATIVE)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setErrorLayoutForNetwork() {
        getBindingView().layoutErrorGlobal.run {
            visible()
            getBindingView().homeGlobalError.run {
                setType(NO_CONNECTION)
                errorAction.text = context.getString(R.string.chatbot_retry_get_data)
                errorSecondaryAction.show()
                errorSecondaryAction.text = context.getString(R.string.chatbot_to_wifi_setting)
                setSecondaryActionClickListener {
                    goToSettingConnection()
                }
                setActionClickListener {
                    loadInitialData()
                    hideErrorLayoutForNetwork()
                }
            }
        }
    }

    private fun goToSettingConnection(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            }
        }catch (e : Exception){ }
    }

    private fun hideErrorLayoutForNetwork() {
        getBindingView().layoutErrorGlobal.gone()
        getBindingView().layoutChatRoom.show()
    }

    private fun setShowingErrorLayout(throwable: Throwable){
        when(throwable){
            is InterruptedIOException, is ChatbotWebSocketException -> {
                if(chatbotAdapter.data.isEmpty()){
                    setErrorLayoutForNetwork()
                } else {
                    hideErrorLayoutForNetwork()
                }
            }

            else -> {
                setErrorLayoutForServer()
            }
        }
    }

    override fun onError(throwable: Throwable) {
        if (view != null) {
            Toaster.build(
                requireView(),
                ErrorHandler.getErrorMessage(requireView().context, throwable),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
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
        if (visitable is MessageUiModel && visitable.attachmentType != SESSION_CHANGE) {
            getViewState()?.hideInvoiceList()
            getViewState()?.hideHelpfullOptions()
        }
    }

    private fun manageActionBubble(visitable: Visitable<*>) {
        when {
            (visitable is MessageUiModel && (visitable.attachmentType != SESSION_CHANGE || visitable.attachmentType != DYNAMIC_ATTACHMENT)) -> hideActionBubble()
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
            is QuickReplyListUiModel -> getViewState()?.onReceiveQuickReplyEvent(visitable)
            is ChatActionSelectionBubbleUiModel -> getViewState()?.onReceiveQuickReplyEventWithActionButton(visitable)
            is ChatRatingUiModel -> getViewState()?.onReceiveQuickReplyEventWithChatRating(visitable)
            else -> super.onReceiveMessageEvent(visitable)
        }
    }

    override fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo) {
        val generatedInvoice = presenter.generateInvoice(invoiceLinkPojo, opponentId)
        getViewState()?.removeInvoiceCarousel()
        hideActionBubble()
        presenter.sendInvoiceAttachment(
            messageId,
            invoiceLinkPojo,
            generatedInvoice.startTime,
            opponentId,
            isArticleEntry,
            hashMap.get(USED_BY).toBlankOrString()
        )
        enableTyping()
    }

    private fun attachInvoiceRetrieved(selectedInvoice: InvoiceLinkPojo) {
        val generatedInvoice = presenter.generateInvoice(selectedInvoice, "")
        getViewState()?.onShowInvoiceToChat(generatedInvoice)
        presenter.sendInvoiceAttachment(
            messageId,
            selectedInvoice,
            generatedInvoice.startTime,
            opponentId,
            isArticleEntry,
            hashMap.get(USED_BY).toBlankOrString()
        )
    }

    private fun showSearchInvoiceScreen() {
        activity?.let {
            val bottomSheetUnify = TransactionInvoiceBottomSheet.newInstance(it, messageId.convertMessageIdToLong(), this)
            bottomSheetUnify.clearContentPadding = true
            bottomSheetUnify.show(childFragmentManager, "")
        }
    }

    override fun onQuickReplyClicked(model: QuickReplyUiModel) {
        chatbotAnalytics.get().eventClick(ACTION_QUICK_REPLY_BUTTON_CLICKED)
        presenter.sendQuickReply(messageId, model, SendableUiModel.generateStartTime(), opponentId)
        getViewState()?.hideQuickReplyOnClick()
        hideCsatRatingView()
    }

    override fun onImageUploadClicked(imageUrl: String, replyTime: String, isSecure: Boolean) {
        activity?.let {
            val strings: ArrayList<String> = ArrayList()
            strings.add(imageUrl)
            it.startActivity(
                ImagePreviewActivity.getCallingIntent(
                    it,
                    strings,
                    null,
                    0
                )
            )
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
            REQUEST_CODE_CHAT_VIDEO -> onPickedAttachVideo(resultCode, data)
            REQUEST_SUBMIT_FEEDBACK -> if (resultCode == Activity.RESULT_OK) submitRating(data)
            REQUEST_SUBMIT_CSAT -> submitCsat(resultCode, data)
            REQUEST_CODE_CHATBOT_ONBOARDING -> backFromOnboardingActivity()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun backFromOnboardingActivity() {
        smoothScrollToPosition(ZERO_POSITION)
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
        presenter.submitChatCsat(messageId, input)
    }

    override fun onSuccessSubmitChatCsat(msg: String) {
        view?.let {
            csatOptionsUiModel?.let { it -> getViewState()?.hideCsatOptionList(it) }
            Toaster.build(it, msg, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, SNACK_BAR_TEXT_OK).show()
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

        presenter.submitCsatRating(messageId, input)
        getBindingView().listQuickReply.show()
    }

    private fun getFilters(data: Intent?, reasonList: List<String?>?): String? {
        try {
            val selectedOption = data?.getStringExtra(SELECTED_ITEMS)?.split(";")
            var filters = ""
            if (!selectedOption.isNullOrEmpty()) {
                for (filter in selectedOption) {
                    if (filter.isNotEmpty()) {
                        filters += reasonList?.get(filter.toIntOrZero()) + ","
                    }
                }
                return filters.substring(0, filters.length - 1)
            }
            return ""
        } catch (e: Exception) {
            return ""
        }
    }

    override fun onSuccessSubmitCsatRating(msg: String) {
        hideCsatRatingView()
        view?.let {
            Toaster.showNormalWithAction(it, msg, Snackbar.LENGTH_LONG, SNACK_BAR_TEXT_OK, View.OnClickListener { })
        }
    }

    private fun onPickedAttachImage(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        presenter.checkUploadSecure(messageId, data)
    }

    private fun onPickedAttachVideo(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        uploadVideo(data)
    }

    private fun uploadVideo(data: Intent) {
        val paths = MediaPicker.result(data)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            presenter.filterMediaUploadJobs(paths.originalPaths)
            val list = mutableListOf<VideoUploadData>()
            paths.originalPaths.forEach {
                list.add(VideoUploadData(it, messageId, SendableUiModel.generateStartTime()))
                processVideoPathToUpload(it)?.let { videoUploadUiModel ->
                    getViewState()?.onVideoUpload(videoUploadUiModel)
                }
                sendAnalyticsForVideoUpload(it)
            }
            presenter.updateMediaUris(list)
        }
    }

    override fun onVideoUploadChangeView(uiModel: VideoUploadUiModel) {
        getViewState()?.onVideoUpload(uiModel)
    }

    override fun setBigReplyBoxTitle(text: String, placeholder: String) {
        handleReplyBox(false)
        bigReplyBox?.setText(text)
        replyBoxBottomSheetPlaceHolder = placeholder
        replyBoxBottomSheetTitle = text
    }

    override fun hideReplyBox() {
        bigReplyBox?.hide()
        smallReplyBox?.hide()
    }

    override fun showErrorLayout(throwable: Throwable) {
        setShowingErrorLayout(throwable)
    }

    private fun sendAnalyticsForVideoUpload(videoFilePath: String) {
        val videoFile = File(videoFilePath)
        val extension = VideoUtil.findVideoExtension(videoFile)
        val videoSize = VideoUtil.findVideoSize(videoFile)
        chatbotAnalytics.get().eventOnVideoUpload(videoFilePath, extension, videoSize)
    }

    private fun processVideoPathToUpload(path: String): VideoUploadUiModel? {
        val totalLength = VideoUtil.retrieveVideoLength(context, path)

        if (!TextUtils.isEmpty(path)) {
            return generateChatUiModelWithVideo(path, totalLength)
        }

        return null
    }

    private fun generateChatUiModelWithVideo(video: String, totalLength: Long): VideoUploadUiModel {
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

    private fun onErrorImageUpload(): (Throwable, ImageUploadUiModel) -> Unit {
        return { throwable, image ->
            if (view != null) {
                Toaster.make(requireView(), ErrorHandler.getErrorMessage(requireView().context, throwable), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
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
                    ChatbotInternalRouter.Companion.TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY
                ) ?: SelectedInvoice()
                attachInvoiceRetrieved(AttachInvoiceMapper.convertInvoiceToDomainInvoiceModel(selectedInvoice))
            }
        }
        enableTyping()
    }

    override fun prepareListener() {
        smallReplyBox?.getSmallReplyBoxSendButton()?.setOnClickListener {
            if (isSendButtonActivated) {
                if (isFloatingSendButton) {
                    onSendFloatingInvoiceClicked()
                } else {
                    onSendButtonClicked()
                }
            } else {
                Toaster.make(
                    it,
                    context?.resources?.getString(R.string.chatbot_float_invoice_input_length_zero).toBlankOrString(),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
                )
            }
        }
    }

    override fun onSendButtonClicked() {
        chatbotAnalytics.get().eventClick(ACTION_REPLY_BUTTON_CLICKED)
        val sendMessage = smallReplyBox?.getMessage() ?: ""
        val startTime = SendableUiModel.generateStartTime()

        presenter.sendMessage(
            messageId,
            sendMessage,
            startTime,
            opponentId,
            replyBubbleContainer?.referredMsg,
            onSendingMessage(sendMessage, startTime, replyBubbleContainer?.referredMsg)
        )

        visibilityReplyBubble(false)
        smallReplyBox?.clearChatText()
    }

    private fun onSendingMessage(sendMessage: String, startTime: String, parentReply: ParentReply?): () -> Unit {
        return {
            if (rvScrollListener?.hasNextAfterPage == true) {
                resetData()
                showTopLoading()
                presenter.getExistingChat(messageId, onShowErrorPage(), onSuccessResetChatToFirstPage(), onGetChatRatingListMessageError)
            } else {
                getViewState()?.onSendingMessage(
                    messageId,
                    getUserSession().userId,
                    getUserSession()
                        .name,
                    sendMessage,
                    startTime,
                    parentReply
                )
                getViewState()?.scrollToBottom()
            }
        }
    }

    override fun onChatActionBalloonSelected(selected: ChatActionBubbleUiModel, model: ChatActionSelectionBubbleUiModel) {
        chatbotAnalytics.get().eventClick(ACTION_ACTION_BUBBLE_CLICKED)
        if (selected.action.equals(SEE_ALL_INVOICE_TEXT, true)) {
            showSearchInvoiceScreen()
        } else {
            getViewState()?.hideActionBubble(model)
            presenter.sendActionBubble(messageId, selected, SendableUiModel.generateStartTime(), opponentId)
            enableTyping()
        }
    }

    override fun onClickRating(element: ChatRatingUiModel, rating: Int) {
        sendEvent(rating)
        presenter.sendRating(messageId, rating, element)
    }

    private fun sendEvent(rating: Int) {
        if (rating == ChatRatingUiModel.RATING_GOOD) {
            chatbotAnalytics.get().eventClick(ACTION_THUMBS_UP_BUTTON_CLICKED)
        } else {
            chatbotAnalytics.get().eventClick(ACTION_THUMBS_DOWN_BUTTON_CLICKED)
        }
    }

    private fun onSuccessSendRating(rating: Int, element: ChatRatingUiModel): (SendRatingPojo) ->
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
                val isTargetDomainTokopedia = uri.host != null &&
                    uri.host!!.endsWith("tokopedia.com")
                val isTargetTkpMeAndNotRedirect = TextUtils.equals(uri.host, BASE_DOMAIN_SHORTENED) &&
                    !TextUtils.equals(uri.encodedPath, "/r")
                val isNeedAuthToken = isTargetDomainTokopedia || isTargetTkpMeAndNotRedirect

                val urlWithSession = URLGenerator.generateURLSessionLogin(
                    url,
                    session.deviceId,
                    session.userId
                )
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
            Toaster.make(it, context?.resources?.getString(R.string.undersize_image).toBlankOrString(), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun onUploadOversizedImage() {
        view?.let {
            Toaster.make(it, context?.resources?.getString(R.string.oversize_image).toBlankOrString(), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun showSnackbarError(stringId: Int) {
        view?.let {
            Toaster.make(it, context?.resources?.getString(stringId).toBlankOrString(), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    override fun clearChatText() {
        smallReplyBox?.clearChatText()
    }

    /**
     * To handle url manually to webview. In this case, to contact us.
     */
    override fun shouldHandleUrlManually(url: String): Boolean {
        return true
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            true
        ).also {
            rvLayoutManager = it
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::mCsatResponse.isInitialized) {
            outState.putParcelable(CSAT_ATTRIBUTES, mCsatResponse.attachment?.attributes)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        replyBubbleOnBoarding.flush()
        videoUploadOnBoarding.flush()
        presenter.detachView()
    }

    override fun createAttachmentMenus(): List<AttachmentMenu> {
        var list = mutableListOf<AttachmentMenu>()
        if (isConnectedToAgent && isEligibleForVideoUplaod) {
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
        chatbotAnalytics?.get()?.eventOnVideoPick()
    }

    override fun showErrorToast(it: Throwable) {
        view?.let { mView -> Toaster.showErrorWithAction(mView, it.message.toString(), Snackbar.LENGTH_LONG, SNACK_BAR_TEXT_OK, View.OnClickListener { }) }
    }

    override fun onReceiveChatSepratorEvent(chatSepratorUiModel: ChatSepratorUiModel, quickReplyList: List<QuickReplyUiModel>) {
        getViewState()?.showLiveChatSeprator(chatSepratorUiModel)
        getViewState()?.showLiveChatQuickReply(quickReplyList)
    }

    override fun updateToolbar(profileName: String?, profileImage: String?, badgeImage: ToolbarAttributes.BadgeImage?) {
        if (activity is ChatbotActivity) {
            (activity as ChatbotActivity).upadateToolbar(profileName, profileImage, badgeImage)
        }
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        getViewState()?.showErrorWebSocket(isWebSocketError)
    }

    override fun chatOptionListSelected(selected: ChatOptionListUiModel, model: HelpFullQuestionsUiModel?) {
        model?.let { getViewState()?.hideOptionList(it) }
        sendOptionListSelectedMessage(selected.text ?: "")

        selected.value.let { presenter.hitGqlforOptionList(messageId, it, model) }
    }

    private fun sendOptionListSelectedMessage(selectedMessage: String) {
        val sendMessage = selectedMessage
        val startTime = SendableUiModel.generateStartTime()
        presenter.sendMessage(
            messageId,
            sendMessage,
            startTime,
            opponentId,
            onSendingMessage(sendMessage, startTime, null)
        )
    }

    override fun csatOptionListSelected(selected: ChatOptionListUiModel, model: CsatOptionsUiModel?) {
        csatOptionsUiModel = model
        startActivityForResult(
            context?.let {
                ChatBotCsatActivity
                    .getInstance(it, selected.value, model)
            },
            REQUEST_SUBMIT_CSAT
        )
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

    private fun handleImageResendBottomSheet(element: ImageUploadUiModel, bottomSheetPage: BottomSheetUnify) {
        removeDummy(element)
        getViewState()?.onImageUpload(element)
        presenter.uploadImageSecureUpload(element, messageId, opponentId, onErrorImageUpload(), element.imageUrl, context)
        bottomSheetPage.dismiss()
    }

    private fun handleImageDeleteBottomSheet(element: ImageUploadUiModel, bottomSheetPage: BottomSheetUnify) {
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

    private fun handleVideoResendBottomSheet(element: VideoUploadUiModel, bottomSheetPage: BottomSheetUnify) {
        removeDummy(element)
        bottomSheetPage.dismiss()
        element.isRetry = false
        getViewState()?.onVideoUpload(element)
        presenter.updateMediaUris(listOf(VideoUploadData(element.videoUrl, messageId, SendableUiModel.generateStartTime())))
    }

    private fun handleVideoDeleteBottomSheet(element: VideoUploadUiModel, bottomSheetPage: BottomSheetUnify) {
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

    private fun onErrorVideoUpload(): (String, VideoUploadUiModel) -> Unit {
        return { errorMsg, video ->
            if (view != null) {
                Toaster.build(requireView(), errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
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
        presenter.checkLinkForRedirection(
            messageId,
            invoiceRefNum,
            onGetSuccessResponse = {
                if (it.isNotEmpty()) {
                    onGoToWebView(it, it)
                }
            },
            setStickyButtonStatus = { isResoListNotEmpty ->
                if (!isResoListNotEmpty) this.isStickyButtonClicked = true
            },
            onError = {
            }
        )
    }

    override fun onResume() {
        super.onResume()
        sendReplyTextForResolutionComponent()
    }

    private fun sendReplyTextForResolutionComponent() {
        if (isStickyButtonClicked) {
            this.isStickyButtonClicked = false

            presenter.checkLinkForRedirection(
                messageId,
                invoiceRefNum,
                onGetSuccessResponse = {},
                setStickyButtonStatus = { isResoListNotEmpty ->
                    if (isResoListNotEmpty) {
                        val startTime = SendableUiModel.generateStartTime()
                        presenter.sendMessage(
                            messageId,
                            replyText,
                            startTime,
                            opponentId,
                            onSendingMessage(replyText, startTime, null)
                        )
                    }
                },
                onError = {
                }
            )
        }
    }

    override fun onBottomSheetDismissListener(data: Intent) {
        onSelectedInvoiceResult(Activity.RESULT_OK, data)
    }

    override fun transactionNotFoundClick() {
        val selected = presenter.getActionBubbleforNoTrasaction()
        presenter.sendActionBubble(messageId, selected, SendableUiModel.generateStartTime(), opponentId)
        enableTyping()
    }

    override fun getUserName(): String {
        return senderNameForReply
    }

    override fun showReplyOption(messageUiModel: MessageUiModel, messageBubble: TextView?) {
        activity?.let {
            replyBubbleBottomSheet = ChatbotReplyBottomSheet(messageUiModel, this, replyBubbleEnabled)
            replyBubbleBottomSheet?.setOnMenuClickListener { menu ->
                onClickReplyMenuListener(menu, messageUiModel, messageBubble)
            }
            replyBubbleBottomSheet?.show(
                childFragmentManager,
                context?.resources?.getString(R.string.chatbot_reply_bubble_bottomsheet_retry)
            )
        }
    }

    private fun onClickReplyMenuListener(
        menu: ChatbotReplyOptionsUiModel,
        messageUiModel: MessageUiModel,
        messageBubble: TextView?
    ) {
        replyBubbleBottomSheet?.dismiss()
        when (menu) {
            is ChatbotReplyOptionsUiModel.Reply -> {
                sendReplyToSpecificChat(messageUiModel)
            }
            is ChatbotReplyOptionsUiModel.CopyToClipboard -> {
                copyToClipBoard(messageBubble)
            }
        }
    }

    private fun sendReplyToSpecificChat(messageUiModel: MessageUiModel) {
        senderNameForReply = messageUiModel.from
        setGuidelineForReplyBubble(true)
        replyBubbleContainer?.composeReplyData(
            messageUiModel,
            "",
            true,
            getUserNameForReplyBubble.getUserName(messageUiModel)
        )
    }

    private fun copyToClipBoard(messageBubble: TextView?) {
        activity?.let {
            val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(
                COPY_TO_CLIPBOARD_LABEL,
                messageBubble?.text.toString()
            )
            clipboard.setPrimaryClip(clip)
            _viewBinding?.smallReplyBox?.showToaster(
                context?.resources?.getString(
                    R.string.chatbot_bottomsheet_copy_success_toaster
                ).toBlankOrString()
            )
        }
    }

    override fun goToBubble(parentReply: ParentReply) {
        val bubblePosition = chatbotAdapter.getBubblePosition(
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

    override fun resetGuidelineForReplyBubble() {
        setGuidelineForReplyBubble(false)
    }

    private fun loadDataOnClick(replyTime: String) {
        showTopLoading()
        presenter.getTopChat(messageId, onSuccessGetTopChatData(replyTime = replyTime, fromOnClick = true), onErrorGetTopChat(), onGetChatRatingListMessageError)
    }

    private fun onReplyBottomSheetItemClicked(bottomSheetPage: BottomSheetUnify, messageUiModel: MessageUiModel): (position: Int) -> Unit {
        return {
            when (it) {
                REPLY -> {
                    senderNameForReply = messageUiModel.from
                    setGuidelineForReplyBubble(true)
                    replyBubbleContainer?.composeReplyData(
                        messageUiModel,
                        "",
                        true,
                        getUserNameForReplyBubble.getUserName(messageUiModel)
                    )
                    bottomSheetPage.dismiss()
                }
            }
        }
    }

    private fun setGuidelineForReplyBubble(toSet: Boolean) {
        if (toSet) {
            val params = guideline?.layoutParams as ConstraintLayout.LayoutParams
            params.guideBegin = context?.dpToPx(GUIDELINE_VALUE_FOR_REPLY_BUBBLE)?.toInt()
                ?: DEFAULT_GUIDELINE_VALUE_FOR_REPLY_BUBBLE
            guideline?.layoutParams = params
        } else {
            val params = guideline?.layoutParams as ConstraintLayout.LayoutParams
            params.guideBegin = DEFAULT_GUIDELINE_VALUE_FOR_REPLY_BUBBLE
            guideline?.layoutParams = params
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getBindingView().smallReplyBox.commentEditText?.windowToken, 0)
    }

    private fun checkReplyBubbleOnboardingStatus() {
        hideKeyboard()
        coachmarkHandler.postDelayed({
            if (!replyBubbleOnBoarding.hasBeenShown()) {
                val position = getPositionToAnchorReplyBubbleCoachmark()
                if (position == BUBBLE_NOT_FOUND) {
                    return@postDelayed
                } else if (position != RecyclerView.NO_POSITION) {
                    smoothScrollToPosition(position)
                }
            } else {
                goToOnboardingActivity()
            }
        }, DELAY_TO_SHOW_COACHMARK)
    }

    private fun getPositionToAnchorReplyBubbleCoachmark(): Int {
        return chatbotAdapter.getMostRecentTokopediaCareMessage()
    }

    override fun visibilityReplyBubble(state: Boolean) {
        if (!state) {
            replyBubbleContainer?.referredMsg = null
            setGuidelineForReplyBubble(false)
            replyBubbleContainer?.hide()
        } else {
            setGuidelineForReplyBubble(true)
            replyBubbleContainer?.show()
        }
    }

    override fun onSuccessSendRating(
        pojo: SendRatingPojo,
        rating: Int,
        element: ChatRatingUiModel
    ) {
        (activity as Activity).run {
            (viewState as ChatbotViewState).onSuccessSendRating(pojo, rating, element, this)
        }
    }

    private fun initRecyclerViewListener() {
        rvScrollListener = object : RecyclerViewScrollListener((recyclerView?.layoutManager as LinearLayoutManager)) {
            override fun loadMoreTop() {
                showTopLoading()
                presenter.getTopChat(
                    messageId,
                    onSuccessGetTopChatData(),
                    onErrorGetTopChat(),
                    onGetChatRatingListMessageError
                )
            }

            override fun loadMoreDown() {
                showBottomLoading()
                presenter.getBottomChat(
                    messageId,
                    onSuccessGetBottomChatData(),
                    onErrorGetBottomChat(),
                    onGetChatRatingListMessageError
                )
            }

            override fun scrollDone() {
                if (!isConnectedToAgent) {
                    return
                }
                if (videoUploadOnBoardingHasBeenShow && replyBubbleOnBoardingHasBeenShow) {
                    return
                }

                getPositionToShowCoachmark()
            }
        }.also {
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun getPositionToShowCoachmark() {
        val position = getPositionToAnchorReplyBubbleCoachmark()

        val firstPosition =
            ((recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
        val location = IntArray(2)
        recyclerView?.getChildAt(position - firstPosition)
            ?.getLocationOnScreen(location)

        xForReplyBubbleOnboarding = location[0]
        yForReplyBubbleOnboarding = location[1]
        goToOnboardingActivity()
    }

    private fun onErrorGetBottomChat(): (Throwable) -> Unit = {
        chatbotAdapter.hideBottomLoading()
        onError()
        rvScrollListener?.finishBottomLoadingState()
    }

    private fun onErrorGetTopChat(): (Throwable) -> Unit = {
        hideTopLoading()
        onError()
        rvScrollListener?.finishTopLoadingState()
    }

    private fun hideTopLoading() {
        hideLoading()
    }

    private fun filterChatList(chatroom: ChatroomViewModel): List<Visitable<*>> {
        return chatroom.listChat.filter {
            !(
                (it is FallbackAttachmentUiModel && it.message.isEmpty()) ||
                    (it is MessageUiModel && it.message.isEmpty()) || checkForDynamicAttachment(it)
                )
        }
    }

    /**
     * We are using Dynamic Attachment with content_codes like 100,101 [As of now]. In future more will
     * get added. If the user doesn't have the updated version to receive new content_code, we will
     * show message to update the app
     * */
    private fun checkForDynamicAttachment(visitable: Visitable<*>): Boolean {
        if (visitable !is FallbackAttachmentUiModel) {
            return false
        }
        if (visitable.attachmentType != DYNAMIC_ATTACHMENT) {
            return false
        }

        if (visitable.attachment is Attachment) {
            val attachment = visitable.attachment as Attachment

            try {
                val dynamicAttachmentContents =
                    Gson().fromJson(attachment.attributes, DynamicAttachment::class.java)

                val replyBoxAttribute =
                    dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

                if (!CheckDynamicAttachmentValidity.checkValidity(replyBoxAttribute?.contentCode)) {
                    return false
                }
            } catch (e: JsonSyntaxException) {
                return true
            }
        }
        return true
    }

    private fun onSuccessGetTopChatData(replyTime: String = "", fromOnClick: Boolean = false): (ChatroomViewModel, ChatReplies) -> Unit {
        return { chatroom, chatReplies ->
            val list = filterChatList(chatroom)
            if (list.isNotEmpty()) {
                val filteredList = getViewState()?.clearDuplicate(list)
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
            } else {
                presenter.getExistingChat(
                    messageId,
                    onShowErrorPage(),
                    onSuccessGetExistingChatFirstTime(),
                    onGetChatRatingListMessageError
                )
            }
        }
    }

    private fun onSuccessGetBottomChatData(): (ChatroomViewModel, ChatReplies) -> Unit {
        return { chatroom, chatReplies ->
            val list = filterChatList(chatroom)
            if (list.isNotEmpty()) {
                val filteredList = getViewState()?.clearDuplicate(list)
                rvScrollListener?.finishBottomLoadingState()
                if (filteredList?.isNotEmpty() == true) {
                    renderBottomList(filteredList)
                } else {
                    presenter.getBottomChat(
                        messageId,
                        onSuccessGetBottomChatData(),
                        onErrorGetBottomChat(),
                        onGetChatRatingListMessageError
                    )
                }
                updateHasNextAfterState(chatReplies)
            } else {
                if (rvScrollListener?.hasNextAfterPage == true) {
                    presenter.getBottomChat(
                        messageId,
                        onSuccessGetBottomChatData(),
                        onErrorGetBottomChat(),
                        onGetChatRatingListMessageError
                    )
                } else {
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
        chatbotAdapter.hideBottomLoading()
        if (listChat.isNotEmpty()) {
            chatbotAdapter.addBottomData(listChat)
        }
    }

    private fun renderTopList(listChat: List<Visitable<*>>) {
        chatbotAdapter.hideTopLoading()
        if (listChat.isNotEmpty()) {
            chatbotAdapter.addTopData(listChat)
        }
    }

    private fun resetData() {
        rvScrollListener?.reset()
        presenter.clearGetChatUseCase()
        chatbotAdapter.reset()
        showTopLoading()
    }

    override fun onRetrySendVideo(element: VideoUploadUiModel) {
        createRetryMediaUploadBottomSheet(element)
    }

    override fun onVideoUploadCancelClicked(video: VideoUploadUiModel) {
        presenter.cancelVideoUpload(video.videoUrl!!, SOURCE_ID_FOR_VIDEO_UPLOAD, onError())
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
            RetryUploadMediaBottomSheetLayoutBinding.inflate(LayoutInflater.from(context)).apply {
                setUpMediaRetryBottomSheet(this, element, bottomSheetPage)
            }

        bottomSheetPage.apply {
            if (element is ImageUploadUiModel) {
                setTitle(
                    this@ChatbotFragment.context?.getString(R.string.chatbot_retry_image_upload_bottom_sheet_title)
                        ?: ""
                )
            } else {
                setTitle(
                    this@ChatbotFragment.context?.getString(R.string.chatbot_retry_video_upload_bottom_sheet_title)
                        ?: ""
                )
            }
            showCloseIcon = false
            setChild(viewBottomSheetPage.root)
            showKnob = true
        }
        fragmentManager?.let {
            bottomSheetPage.show(it, "retry media bottom sheet")
        }
    }

    private fun setUpMediaRetryBottomSheet(view: RetryUploadMediaBottomSheetLayoutBinding, element: SendableUiModel, bottomSheetPage: BottomSheetUnify) {
        val rvPages = view.rvImageUploadOption
        rvPages.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter =
            MediaRetryBottomSheetAdapter(onBottomSheetItemClicked(element, bottomSheetPage))
        rvPages.adapter = adapter
        adapter.setList(
            listOf<String>(
                context?.getString(R.string.chatbot_delete)
                    ?: "",
                context?.getString(R.string.chatbot_resend) ?: ""
            )
        )
    }

    override fun sessionChangeStateHandler(state: Boolean) {
        isConnectedToAgent = state
        replyBubbleEnabled = state
        if (state) {
            checkReplyBubbleOnboardingStatus()
            createAttachmentMenus()
        }
    }

    override fun videoUploadEligibilityHandler(state: Boolean) {
        isEligibleForVideoUplaod = state
    }

    override fun disableSendButton() {
        isSendButtonActivated = false
        smallReplyBox?.sendButton?.setImageResource(R.drawable.ic_chatbot_send_deactivated)
    }

    override fun enableSendButton() {
        isSendButtonActivated = true
        smallReplyBox?.sendButton?.setImageResource(R.drawable.ic_chatbot_send)
    }

    override fun isInvoiceRemoved(isRemoved: Boolean) {
        isFloatingInvoiceCancelled = isRemoved

        smallReplyBox?.removeTextChangedListener()
    }

    override fun onAttachmentMenuClicked() {
        attachmentMenuRecyclerView?.toggle()
        createAttachmentMenus()
    }

    override fun goToBigReplyBoxBottomSheet() {
        activity?.let {
            val bottomSheetUnify = BigReplyBoxBottomSheet
                .newInstance(it, replyBoxBottomSheetPlaceHolder, replyBoxBottomSheetTitle)
            BigReplyBoxBottomSheet.replyBoxClickListener = this
            bottomSheetUnify.clearContentPadding = true
            bottomSheetUnify.show(childFragmentManager, "")
        }
    }

    override fun getMessageContentFromBottomSheet(msg: String) {
        val startTime = SendableUiModel.generateStartTime()

        enableTyping()

        presenter.sendMessage(
            messageId,
            msg,
            startTime,
            opponentId,
            replyBubbleContainer?.referredMsg,
            onSendingMessage(msg, startTime, replyBubbleContainer?.referredMsg)
        )
    }

    private fun remoteConfigForCsatExperiment() {
        csatRemoteConfig = context?.let { RemoteConfigHelper.isRemoteConfigForCsat(it) } ?: false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        videoUploadOnBoarding.flush()
        replyBubbleOnBoarding.flush()
        coachmarkHandler.removeCallbacksAndMessages(null)
    }

    override fun onClickMessageReply(messageUiModel: MessageUiModel) {
        senderNameForReply = messageUiModel.from
        setGuidelineForReplyBubble(true)
        replyBubbleContainer?.composeReplyData(
            messageUiModel,
            "",
            true,
            getUserNameForReplyBubble.getUserName(messageUiModel)
        )
        replyBubbleBottomSheet?.dismiss()
    }
}
