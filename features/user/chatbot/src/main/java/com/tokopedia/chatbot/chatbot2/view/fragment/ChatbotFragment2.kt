package com.tokopedia.chatbot.chatbot2.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
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
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.VideoMenu
import com.tokopedia.chat_common.view.fragment.BaseChatFragment
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.widget.AttachmentMenuRecyclerView
import com.tokopedia.chatbot.ChatbotConstant
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
import com.tokopedia.chatbot.ChatbotConstant.DynamicAttachment.DYNAMIC_ATTACHMENT
import com.tokopedia.chatbot.ChatbotConstant.DynamicAttachment.DYNAMIC_REPLY_CSAT_NO
import com.tokopedia.chatbot.ChatbotConstant.DynamicAttachment.DYNAMIC_REPLY_CSAT_YES
import com.tokopedia.chatbot.ChatbotConstant.DynamicAttachment.REPLY_BOX_TOGGLE_VALUE
import com.tokopedia.chatbot.ChatbotConstant.ONE_SECOND_IN_MILLISECONDS
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHATBOT_ONBOARDING
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHAT_IMAGE
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_CODE_CHAT_VIDEO
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_CSAT
import com.tokopedia.chatbot.ChatbotConstant.REQUEST_SUBMIT_FEEDBACK
import com.tokopedia.chatbot.ChatbotConstant.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE
import com.tokopedia.chatbot.ChatbotConstant.VIDEO_URL
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_DURATION_FOR_VIDEO
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_IMAGE_COUNT
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_MEDIA_ITEM_COUNT
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.MAX_VIDEO_COUNT
import com.tokopedia.chatbot.ChatbotConstant.VideoUpload.SOURCE_ID_FOR_VIDEO_UPLOAD
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.TransactionInvoiceBottomSheet
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.TransactionInvoiceBottomSheetListener
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse.Attributes
import com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.chatbot2.data.dynamicAttachment.DynamicAttachment
import com.tokopedia.chatbot.chatbot2.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.chatbot2.di.ChatbotModule
import com.tokopedia.chatbot.chatbot2.di.DaggerChatbotComponent
import com.tokopedia.chatbot.chatbot2.domain.video.VideoUploadData
import com.tokopedia.chatbot.chatbot2.util.convertMessageIdToLong
import com.tokopedia.chatbot.chatbot2.view.activity.ChatBotCsatActivity
import com.tokopedia.chatbot.chatbot2.view.activity.ChatBotProvideRatingActivity
import com.tokopedia.chatbot.chatbot2.view.activity.ChatbotVideoActivity
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotTypeFactoryImpl
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotOwocListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.DynamicStickyButtonListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.VideoUploadListener
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.BigReplyBoxBottomSheet
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.BigReplyBoxBottomSheet.Companion.MINIMUM_NUMBER_OF_WORDS
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.ChatbotMediaRetryBottomSheet
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.ChatbotRejectReasonsBottomSheet
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.ChatbotReplyBottomSheet
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter.ChatbotReplyBottomSheetAdapter
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.listener.ChatbotRejectReasonsChipListener
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.replybox.BigReplyBox
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.replybox.SmallReplyBox
import com.tokopedia.chatbot.chatbot2.view.customview.floatinginvoice.ChatbotFloatingInvoice
import com.tokopedia.chatbot.chatbot2.view.customview.reply.ReplyBubbleOnBoarding
import com.tokopedia.chatbot.chatbot2.view.listener.ChatbotContract
import com.tokopedia.chatbot.chatbot2.view.listener.ChatbotViewState
import com.tokopedia.chatbot.chatbot2.view.listener.ChatbotViewStateImpl
import com.tokopedia.chatbot.chatbot2.view.listener.RecyclerViewScrollListener
import com.tokopedia.chatbot.chatbot2.view.listener.SmoothScroller
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicAttachmentTextUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.chatbot2.view.util.CheckDynamicAttachmentValidity
import com.tokopedia.chatbot.chatbot2.view.util.decorator.ChatBubbleItemDecorator
import com.tokopedia.chatbot.chatbot2.view.util.helper.GetUserNameForReplyBubble
import com.tokopedia.chatbot.chatbot2.view.util.helper.InvoiceStatusLabelHelper
import com.tokopedia.chatbot.chatbot2.view.util.helper.createAttachInvoiceSingleViewModel
import com.tokopedia.chatbot.chatbot2.view.util.helper.generateInvoice
import com.tokopedia.chatbot.chatbot2.view.util.helper.getActionBubbleforNoTrasaction
import com.tokopedia.chatbot.chatbot2.view.util.helper.getValuesForArticleEntry
import com.tokopedia.chatbot.chatbot2.view.util.helper.validateImageAttachment
import com.tokopedia.chatbot.chatbot2.view.util.showToaster
import com.tokopedia.chatbot.chatbot2.view.util.showToasterError
import com.tokopedia.chatbot.chatbot2.view.util.video.VideoUtil
import com.tokopedia.chatbot.chatbot2.view.viewmodel.ChatbotViewModel
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.BigReplyBoxState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatDataState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatRatingListState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotArticleEntryState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotChatSeparatorState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotDynamicAttachmentMediaButtonState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotImageUploadFailureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotOpenCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotRejectReasonsState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSendChatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketErrorState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketReceiveEvent
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitChatCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitCsatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotUpdateToolbarState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotVideoUploadFailureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.CheckUploadSecureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.GetTickerDataState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TopBotNewSessionState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ValidImageAttachment
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.databinding.FragmentChatbot2Binding
import com.tokopedia.chatbot.util.logNewRelicMessageIdError
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.activity.ChatbotActivity.Companion.DEEP_LINK_URI
import com.tokopedia.chatbot.view.activity.ChatbotActivity.Companion.PAGE_SOURCE
import com.tokopedia.chatbot.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.chatbot.view.uimodel.ChatbotReplyOptionsUiModel
import com.tokopedia.chatbot.view.util.OnboardingVideoDismissListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 23/11/18.
 */

class ChatbotFragment2 :
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
    ChatbotReplyBottomSheetAdapter.ReplyBubbleBottomSheetListener,
    com.tokopedia.chatbot.chatbot2.view.listener.ChatbotSendButtonListener,
    com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener.ReplyBoxClickListener,
    ChatbotRejectReasonsBottomSheet.ChatbotRejectReasonsListener,
    DynamicStickyButtonListener,
    ChatbotRejectReasonsChipListener,
    ChatbotOwocListener {

    @Inject
    lateinit var session: UserSessionInterface

    @Inject
    lateinit var chatbotAnalytics: dagger.Lazy<ChatbotAnalytics>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ChatbotViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(ChatbotViewModel::class.java)
    }

    private var _viewBinding: FragmentChatbot2Binding? = null
    private fun getBindingView() = _viewBinding!!

    var mCsatResponse: WebSocketCsatResponse? = null
    var attribute: Attributes? = null
    private var ticker: Ticker? = null
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
    private var chatbotAdapter: ChatbotAdapter? = null
    private var isEligibleForVideoUpload: Boolean = false
    private var guideline: Guideline? = null
    var yForReplyBubbleOnboarding: Int = 0
    private var chatbotViewStateImpl: ChatbotViewStateImpl? = null
    private var replyBoxBottomSheetPlaceHolder: String = ""
    private var replyBoxBottomSheetTitle: String = ""
    var replyBubbleOnBoardingHasBeenShow: Boolean = false
    var videoUploadOnBoardingHasBeenShow: Boolean = false
    private val coachmarkHandler = Handler(Looper.getMainLooper())
    private var imageData: Intent? = null
    private var chatRatingUiModel: ChatRatingUiModel? = null
    private var rating = 0
    private var replyTime = ""
    private var isGetChatFromOnClick = false
    private var replyBubbleBottomSheet: ChatbotReplyBottomSheet? = null
    private var mediaRetryBottomSheet: ChatbotMediaRetryBottomSheet? = null
    private var bigReplyBoxPlaceHolder: String = ""

    // Used for resetting the usecase when user replies to message from not page 1
    private var messageSentNotFromFirstPage = false

    @Inject
    lateinit var replyBubbleOnBoarding: ReplyBubbleOnBoarding
    var isReplyCoachMarkShowing = false

    @Inject
    lateinit var videoUploadOnBoarding: VideoUploadOnBoarding
    var isVideoCoachMarkShowing = false
    private var recyclerView: RecyclerView? = null
    private var isArticleDataSent: Boolean = false

    @Inject
    lateinit var getUserNameForReplyBubble: GetUserNameForReplyBubble

    private var pageSource: String = ""
    private var showAddAttachmentMenu: Boolean = true
    private var showUploadImageButton: Boolean = true
    private var showUploadVideoButton: Boolean = false
    private var isDismissClickOnRejectReasons: Boolean = false

    private var bigReplyBoxBottomSheet: BigReplyBoxBottomSheet? = null
    private var dynamicAttachmentRejectReasons: DynamicAttachmentRejectReasons? = null
    private var reasonsBottomSheet: ChatbotRejectReasonsBottomSheet? = null
    private var rejectReasonsText: String = ""

    val selectedList =
        mutableListOf<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>()

    companion object {
        private const val ONCLICK_REPLY_TIME_OFFSET_FOR_REPLY_BUBBLE = 5000
        private const val GUIDELINE_VALUE_FOR_REPLY_BUBBLE = 65
        private const val DEFAULT_GUIDELINE_VALUE_FOR_REPLY_BUBBLE = 0
        private const val BUBBLE_NOT_FOUND = -2
        private const val DELAY_TO_SHOW_COACHMARK = 1000L
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
        private const val SEE_ALL_INVOICE_TEXT = "lihat_semua_transaksi"
        private const val SNACK_BAR_TEXT_OK = "OK"
        private const val BOT_OTHER_REASON_TEXT = "bot_other_reason"
        private const val SELECTED_ITEMS = "selected_items"
        private const val EMOJI_STATE = "emoji_state"
        private const val TIME_STAMP = "time_stamp"
        private const val CSAT_ATTRIBUTES = "csat_attribute"
        private const val TICKER_TYPE_ANNOUNCEMENT = "announcement"
        private const val TICKER_TYPE_WARNING = "warning"
        private const val COPY_TO_CLIPBOARD_LABEL = "Tokopedia-Chatbot"
        private const val ZERO_RATIO = 0F
        private const val ZERO_POSITION = 0
        private const val REPLY_BOX_SIZE = 150
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
                ((activity as Activity).application as BaseMainApplication).baseAppComponent
            )
                .chatbotModule(context?.let { ChatbotModule(it) })
                .build()

            chatbotComponent.inject(this)
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
        if (csatResponse.attachment?.attributes?.isSimplifyCsat.orFalse()) {
            openCsatNewFlow(csatResponse)
        } else {
            openCsatOldFlow(csatResponse)
        }
    }

    private fun openCsatOldFlow(csatResponse: WebSocketCsatResponse) {
        mCsatResponse = csatResponse
        mCsatResponse.let {
            getBindingView().listQuickReply.hide()
            showCsatRatingViewOldFlow()
        }
    }

    private fun openCsatNewFlow(csatResponse: WebSocketCsatResponse) {
        mCsatResponse = csatResponse
        mCsatResponse.let {
            showCsatRatingViewNewFlow()
        }
    }

    private fun showCsatRatingViewOldFlow() {
        getBindingView().chatbotViewHelpRate.txtHelpTitle.text =
            mCsatResponse?.attachment?.attributes?.title
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

    private fun onClickEmoji(number: Long) {
        startActivityForResult(
            context?.let {
                mCsatResponse?.let { it1 ->
                    ChatBotProvideRatingActivity
                        .getInstance(it, number, it1)
                }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bundle = this.arguments
        if (bundle != null) {
            val intentData = bundle.getString(DEEP_LINK_URI, "")
            val uri: Uri = Uri.parse(intentData)

            isAttached = checkForIsAttachedInvoice(uri)
            hashMap = getValuesForArticleEntry(uri)
            isArticleEntry = checkForArticleEntry(uri)
        }

        _viewBinding = FragmentChatbot2Binding.inflate(inflater, container, false)
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

    private fun sendInvoiceAttachment(
        invoiceLinkPojo: InvoiceLinkPojo,
        startTime: String
    ) {
        viewModel.sendInvoiceAttachment(
            messageId,
            invoiceLinkPojo,
            startTime,
            opponentId,
            isArticleEntry,
            hashMap.get(USED_BY).toBlankOrString()
        )
    }

    override fun sendInvoiceForArticle() {
        if (isArticleEntry && !isArticleDataSent) {
            if (!isAttached) {
                if (hashMap.get(CODE)?.isNotEmpty() == true) {
                    val attachInvoiceSingleViewModel = createAttachInvoiceSingleViewModel(hashMap)
                    val invoice: InvoiceLinkPojo =
                        AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(
                            attachInvoiceSingleViewModel
                        )
                    val generatedInvoice =
                        generateInvoice(invoice, opponentId, getUserSession().name)
                    getViewState()?.onShowInvoiceToChat(generatedInvoice)
                    sendInvoiceAttachment(invoice, generatedInvoice.startTime)
                }
                if (hashMap.get(ARTICLE_ID)?.isNotEmpty() == true) {
                    val startTime = SendableUiModel.generateStartTime()
                    val msg = hashMap.get(ARTICLE_TITLE).toBlankOrString()
                    val quickReplyUiModel = QuickReplyUiModel(msg, msg, msg)

                    viewModel.sendQuickReplyInvoice(
                        messageId,
                        quickReplyUiModel,
                        startTime,
                        opponentId,
                        hashMap[EVENT].toBlankOrString(),
                        hashMap[USED_BY].toBlankOrString(),
                        false
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
            val attachInvoiceSingleViewModel = createAttachInvoiceSingleViewModel(hashMap)
            val invoice: InvoiceLinkPojo =
                AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(
                    attachInvoiceSingleViewModel
                )
            val generatedInvoice = generateInvoice(invoice, opponentId, getUserSession().name)
            getViewState()?.onShowInvoiceToChat(generatedInvoice)
            sendInvoiceAttachment(
                invoice,
                generatedInvoice.startTime
            )
        }

        val startTime = SendableUiModel.generateStartTime()
        val msg = smallReplyBox?.getMessage() ?: ""
        val quickReplyUiModel = QuickReplyUiModel(msg, msg, msg)

        viewModel.sendQuickReplyInvoice(
            messageId,
            quickReplyUiModel,
            startTime,
            opponentId,
            hashMap.get(EVENT).toString(),
            hashMap.get(USED_BY).toString(),
            false
        )
        smallReplyBox?.clearChatText()
        isFloatingSendButton = false
    }

    private fun setChatBackground() {
        activity?.window?.setBackgroundDrawable(
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.layered_chatbot_background
                )
            }
        )
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
            this,
            this,
            getUserSession()
        )
    }

    fun setDateIndicator(): (String) -> Unit = {
        if (it.isNotEmpty() && it != context?.resources?.getString(
                R.string.chatbot_placeholder_date
            ).toBlankOrString()
        ) {
            getBindingView().dateIndicator.text = it
            getBindingView().dateIndicatorContainer.show()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        if (adapterTypeFactory !is ChatbotTypeFactoryImpl) {
            throw IllegalStateException(
                "getAdapterTypeFactory() must return ChatbotTypeFactoryImpl"
            )
        }
        val typeFactory = adapterTypeFactory as ChatbotTypeFactoryImpl
        return ChatbotAdapter(typeFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        floatingInvoice = getBindingView().floatingInvoice

        ticker = getBindingView().chatbotTicker
        attachmentMenuRecyclerView = getBindingView().rvAttachmentMenu

        smallReplyBox = getBindingView().smallReplyBox
        bigReplyBox = getBindingView().bigReplyBox
        guideline = smallReplyBox?.guideline
        smallReplyBox?.replyBoxClickListener = this

        replyBubbleContainer = smallReplyBox?.replyBubbleContainer

        setUpBigReplyBoxListeners()
        replyBubbleContainer = smallReplyBox?.replyBubbleContainer
        replyBubbleOnBoardingHasBeenShow = replyBubbleOnBoarding.hasBeenShown()
        videoUploadOnBoardingHasBeenShow = videoUploadOnBoarding.hasBeenShown()
        setUpFloatingInvoiceListeners()

        smallReplyBox?.listener = this

        recyclerView = getRecyclerView(view)
        isFloatingInvoiceCancelled = false
        setChatBackground()
        initSmoothScroller()
        getRecyclerView(view)?.addItemDecoration(ChatBubbleItemDecorator(setDateIndicator()))
        chatbotAdapter = adapter as ChatbotAdapter

        attachListenersForRating()

        super.onViewCreated(view, savedInstanceState)
        viewState?.initView()

        startObservingViewModels()

        pageSource = getParamString(PAGE_SOURCE, arguments, savedInstanceState)
        val isChatbotActive =
            getParamBoolean(ChatbotActivity.IS_CHATBOT_ACTIVE, arguments, savedInstanceState, true)
        checkIsChatbotServerActive(isChatbotActive)
        handlingForMessageIdValidity(messageId)
        viewModel.setPageSourceValue(pageSource)

        viewModel.checkForSession(messageId)
        viewModel.showTickerData(messageId)

        initRecyclerViewListener()
        setupBeforeReplyTime()
        createAttachmentMenus()

        if (savedInstanceState != null) {
            this.attribute = savedInstanceState.getParcelable(CSAT_ATTRIBUTES) ?: Attributes()
        }
    }

    private fun setErrorLayoutForServer() {
        getBindingView().layoutErrorGlobal.run {
            visible()
            getBindingView().homeGlobalError.run {
                setType(GlobalError.SERVER_ERROR)
                errorAction.text = context.getString(R.string.chatbot_back_to_tokopedia_care)
                errorSecondaryAction.hide()
                setActionClickListener {
                    val intent = RouteManager.getIntent(
                        requireView().context,
                        ApplinkConst.CONTACT_US_NATIVE
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }

    private fun handlingForMessageIdValidity(messageId: String) {
        try {
            val id = messageId.toLong()
            if (id == 0L) {
                throw NumberFormatException()
            }
        } catch (e: NumberFormatException) {
            setErrorLayoutForServer()
            logNewRelicMessageIdError(
                messageId,
                pageSource
            )
        }
    }

    private fun attachListenersForRating() {
        getBindingView().chatbotViewHelpRate.btnInactive1.setOnClickListener(this@ChatbotFragment2)
        getBindingView().chatbotViewHelpRate.btnInactive2.setOnClickListener(this@ChatbotFragment2)
        getBindingView().chatbotViewHelpRate.btnInactive3.setOnClickListener(this@ChatbotFragment2)
        getBindingView().chatbotViewHelpRate.btnInactive4.setOnClickListener(this@ChatbotFragment2)
        getBindingView().chatbotViewHelpRate.btnInactive5.setOnClickListener(this@ChatbotFragment2)
    }

    private fun startObservingViewModels() {
        viewModel.topBotNewSession.observe(viewLifecycleOwner) {
            when (it) {
                is TopBotNewSessionState.SuccessTopBotNewSession -> {
                    handleTopBotNewSessionSuccess(it.data)
                }

                is TopBotNewSessionState.HandleFailureNewSession -> {
                    handleTopBotNewSessionFailure()
                }
            }
        }

        viewModel.checkUploadSecure.observe(viewLifecycleOwner) {
            when (it) {
                is CheckUploadSecureState.SuccessCheckUploadSecure -> {
                    handleCheckUploadSecureSuccess()
                }

                is CheckUploadSecureState.HandleFailureCheckUploadSecure -> {
                    // Using same error method as we have same logic
                    handleTopBotNewSessionFailure()
                }
            }
        }

        viewModel.tickerData.observe(viewLifecycleOwner) {
            when (it) {
                is GetTickerDataState.SuccessTickerData -> {
                    onSuccessGetTickerData(it.data)
                }

                is GetTickerDataState.HandleTickerDataFailure -> {
                    onError(it.error)
                }
            }
        }

        viewModel.submitCsatRating.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotSubmitCsatRatingState.SuccessChatbotSubmtiCsatRating -> {
                    onSuccessSubmitCsatRating(it.data)
                }

                is ChatbotSubmitCsatRatingState.HandleFailureChatbotSubmitCsatRating -> {
                    onError(it.error)
                }
            }
        }

        viewModel.submitChatCsat.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotSubmitChatCsatState.HandleChatbotSubmitChatCsatSuccess -> {
                    onSuccessSubmitChatCsat(it.message)
                }

                is ChatbotSubmitChatCsatState.FailureChatbotSubmitChatCsat -> {
                    onError(it.error)
                }
            }
        }

        viewModel.stickyActionClick.observe(viewLifecycleOwner) {
            this.isStickyButtonClicked = !it
        }

        viewModel.stickyActionGoToWebView.observe(viewLifecycleOwner) {
            onGoToWebView(it, it)
        }

        viewModel.toSendTextMessageForResoComponent.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.sendMessage(
                    messageId,
                    replyText,
                    SendableUiModel.generateStartTime(),
                    opponentId,
                    replyBubbleContainer?.referredMsg,
                    onSendingMessage(replyText, SendableUiModel.generateStartTime(), null)
                )
            }
        }

        viewModel.sendChatRating.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotSendChatRatingState.HandleSuccessChatbotSendChatRating -> {
                    onSuccessSendRating(it.data)
                }

                is ChatbotSendChatRatingState.HandleErrorSendChatRating -> {
                    onError(it.throwable)
                }
            }
        }

        viewModel.existingChatData.observe(viewLifecycleOwner) {
            when (it) {
                is ChatDataState.SuccessChatDataState -> {
                    if (messageSentNotFromFirstPage) {
                        onSuccessResetChatToFirstPage(it.chatReplies, it.chatroomViewModel)
                    } else {
                        onSuccessGetExistingChatFirstTime(it.chatReplies, it.chatroomViewModel)
                    }
                }

                is ChatDataState.HandleFailureChatData -> {
                    onError(it.throwable)
                }
            }
        }

        viewModel.topChatData.observe(viewLifecycleOwner) {
            when (it) {
                is ChatDataState.SuccessChatDataState -> {
                    onSuccessGetTopChatData(it.chatReplies, it.chatroomViewModel)
                }

                is ChatDataState.HandleFailureChatData -> {
                    onErrorGetTopChat()
                }
            }
        }

        viewModel.bottomChatData.observe(viewLifecycleOwner) {
            when (it) {
                is ChatDataState.SuccessChatDataState -> {
                    onSuccessGetBottomChatData(it.chatReplies, it.chatroomViewModel)
                }

                is ChatDataState.HandleFailureChatData -> {
                    onErrorGetBottomChat()
                }
            }
        }

        viewModel.ratingListMessageError.observe(viewLifecycleOwner) {
            when (it) {
                is ChatRatingListState.HandleFailureChatRatingList -> {
                    getBindingView().footer.showToaster(it.errorMessage)
                }
            }
        }

        viewModel.receiverModeAgent.observe(viewLifecycleOwner) {
            sessionChangeStateHandler(it)
        }

        viewModel.updateToolbar.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotUpdateToolbarState.UpdateToolbar -> updateToolbar(
                    it.profileName,
                    it.profileImage,
                    it.badgeImage
                )
            }
        }
        viewModel.openCsat.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotOpenCsatState.ShowCsat -> openCsat(it.response)
            }
        }

        viewModel.chatSeparator.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotChatSeparatorState.ChatSeparator -> onReceiveChatSepratorEvent(
                    it.chatSepratorUiModel,
                    it.quickReplyList
                )
            }
        }

        viewModel.isArticleEntryToSendData.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotArticleEntryState.ToSendArticleUponEntry -> sendInvoiceForArticle()
            }
        }

        viewModel.socketConnectionError.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotSocketErrorState.SocketConnectionError -> showErrorWebSocket(true)
                is ChatbotSocketErrorState.SocketConnectionSuccessful -> showErrorWebSocket(false)
            }
        }

        viewModel.socketReceiveMessageEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotSocketReceiveEvent.StartTypingEvent -> {
                    onReceiveStartTypingEvent()
                }

                is ChatbotSocketReceiveEvent.EndTypingEvent -> {
                    onReceiveStopTypingEvent()
                }

                is ChatbotSocketReceiveEvent.ReadEvent -> {
                    onReceiveReadEvent()
                }

                is ChatbotSocketReceiveEvent.ReplyMessageEvent -> {
                    onReceiveMessageEvent(it.visitable)
                }
            }
        }

        viewModel.videoUploadFailure.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotVideoUploadFailureState.ChatbotVideoUploadFailure -> {
                    onErrorVideoUpload(it.uiModel, it.message)
                }
            }
        }

        viewModel.imageUploadFailureData.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotImageUploadFailureState.ImageUploadErrorBody -> onErrorImageUpload(
                    it.imageUploadUiModel,
                    it.throwable
                )
            }
        }

        viewModel.smallReplyBoxDisabled.observe(viewLifecycleOwner) {
            if (it) {
                hideReplyBox()
            } else {
                enableTyping()
            }
        }

        viewModel.bigReplyBoxState.observe(viewLifecycleOwner) {
            when (it) {
                is BigReplyBoxState.BigReplyBoxContents -> {
                    setBigReplyBoxTitle(it.title, it.placeHolder)
                }
            }
        }

        viewModel.dynamicAttachmentMediaUploadState.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotDynamicAttachmentMediaButtonState.OnReceiveMediaButtonAttachment -> {
                    handleAddAttachmentButtonViewState(it.toShowAddAttachmentButton)
                    handleImageUploadButtonViewState(it.toShowImageUploadButton)
                    handleVideoUploadButtonViewState(it.toShowVideoUploadButton)
                }
            }
        }

        viewModel.dynamicAttachmentRejectReasonState.observe(viewLifecycleOwner) {
            when (it) {
                is ChatbotRejectReasonsState.ChatbotRejectReasonData -> {
                    getViewState()?.handleQuickReplyFromDynamicAttachment(true, it.rejectReasons)
                    dynamicAttachmentRejectReasons = it.rejectReasons
                    hideKeyboard()
                }
            }
        }

        viewModel.dynamicAttachmentNewSlowMode.observe(viewLifecycleOwner) {
            setupSlowModeSendButton(it.isUsingSlowMode, it.slowModeDurationInSeconds)
        }

        viewModel.sendWebsocketMessageEvent.observe(viewLifecycleOwner) {
            if (it) {
                slowDownSendButton()
            }
        }

        viewModel.applink.observe(viewLifecycleOwner) { applink ->
            context?.let { context ->
                RouteManager.route(context, applink)
            }
        }
    }

    private fun handleAddAttachmentButtonViewState(toShow: Boolean) {
        showAddAttachmentMenu = toShow
        getBindingView().smallReplyBox.addAttachmentMenu?.showWithCondition(
            showAddAttachmentMenu
        )
        bigReplyBox?.handleAddAttachmentButton(toShow)
        bigReplyBoxBottomSheet?.hideAddAttachmentButton(toShow)
        if (showAddAttachmentMenu) {
            getBindingView().smallReplyBox.commentEditText?.editText?.apply {
                if (!isConnectedToAgent) {
                    setImageUploadDataInReplyBox()
                } else {
                    setDefaultDataInReplyBox()
                }
            }
        }
    }

    fun handleImageUploadButtonViewState(toShow: Boolean) {
        showUploadImageButton = toShow
    }

    fun handleVideoUploadButtonViewState(toShow: Boolean) {
        showUploadVideoButton = toShow
        createAttachmentMenus()
    }

    private fun EditText.setImageUploadDataInReplyBox() {
        this.apply {
            hint = context?.resources?.getString(R.string.chatbot_edit_text_hint)
                .toBlankOrString()
            isEnabled = false
        }
    }

    private fun EditText.setDefaultDataInReplyBox() {
        this.apply {
            hint =
                context?.resources?.getString(R.string.cb_bot_reply_text).toBlankOrString()
            isEnabled = true
        }
    }

    private fun handleCheckUploadSecureSuccess() {
        imageData?.let {
            uploadUsingSecureUpload(it)
        }
    }

    private fun handleTopBotNewSessionSuccess(
        topBotResponse: TopBotNewSessionResponse.TopBotGetNewSession
    ) {
        val isNewSession = topBotResponse.isNewSession
        handleNewSession(isNewSession)
        handleReplyBox(topBotResponse)
    }

    private fun handleTopBotNewSessionFailure() {
        loadChatHistory()
        enableTyping()
    }

    private fun handleNewSession(isNewSession: Boolean) {
        if (isNewSession) startNewSession() else loadChatHistory()
    }

    private fun showOnBoardCoachMark() {
        val hasBeenShownVideoUploadOnBoarding = videoUploadOnBoarding.hasBeenShown()
        val hasBeenShownReplyBubbleOnBoarding = replyBubbleOnBoarding.hasBeenShown()

        if (hasBeenShownReplyBubbleOnBoarding && hasBeenShownVideoUploadOnBoarding) {
            return
        }

        if (!hasBeenShownReplyBubbleOnBoarding) {
            val ratioY = calculateRatiosForGuideline()
            setUpPositionReplyBubbleGuideline(ratioY)
            isReplyCoachMarkShowing = true
            showReplayCoachMark()
        } else if (!hasBeenShownReplyBubbleOnBoarding) {
            checkVideoUploadOnboardingStatus()
        }
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun loadChatHistory() {
        loadInitialData()
        viewModel.connectWebSocket(messageId)
    }

    override fun startNewSession() {
        viewModel.connectWebSocket(messageId)
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

    private fun handleReplyBox(topBotResponse: TopBotNewSessionResponse.TopBotGetNewSession) {
        val isTypingBlocked = topBotResponse.isTypingBlocked
        handleReplyBox(!isTypingBlocked)

        setupSlowModeSendButton(topBotResponse.isSlowMode, topBotResponse.slowModeDurationInSeconds)
    }

    private fun setupSlowModeSendButton(isSlowMode: Boolean, slowModeDurationInSeconds: Int) {
//        smallReplyBox?.sendButton?.apply {
//            isSlowModeEnabled = isSlowMode
//            slowModeDurationInSecond = slowModeDurationInSeconds
//        }
//        bigReplyBox?.sendButton?.apply {
//            isSlowModeEnabled = isSlowMode
//            slowModeDurationInSecond = slowModeDurationInSeconds
//        }
        smallReplyBox?.sendButton?.apply {
            isSlowModeEnabled = true
            slowModeDurationInSecond = 5
        }
        bigReplyBox?.sendButton?.apply {
            isSlowModeEnabled = true
            slowModeDurationInSecond = 5
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

    override fun onSuccessGetTickerData(
        tickerData: com.tokopedia.chatbot.chatbot2.data.tickerData.TickerData
    ) {
        if (!tickerData.items.isNullOrEmpty()) {
            getBindingView().chatbotTicker.show()
            if (tickerData.items.size > 1) {
                showMultiTicker(tickerData)
            } else if (tickerData.items.size == 1) {
                showSingleTicker(tickerData)
            }
        }
    }

    private fun showSingleTicker(
        tickerData: com.tokopedia.chatbot.chatbot2.data.tickerData.TickerData
    ) {
        getBindingView().chatbotTicker.let {
            it.tickerTitle = tickerData.items?.get(0)?.title
            it.setHtmlDescription(tickerData.items?.get(0)?.text ?: "")
            it.tickerType = getTickerType(tickerData.type ?: "")
            it.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    navigateToWebView(linkUrl.toString())
                }

                override fun onDismiss() {
                }
            })
        }
    }

    private fun showMultiTicker(
        tickerData: com.tokopedia.chatbot.chatbot2.data.tickerData.TickerData
    ) {
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
        getBindingView().chatbotTicker.addPagerView(adapter, mockData)
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
            refreshFirstPage()
        } else {
            swipeToRefresh.isRefreshing = false
            swipeToRefresh.isEnabled = false
            swipeToRefresh.setOnRefreshListener(null)
        }
    }

    private fun refreshFirstPage() {
        hideSnackBarRetry()
        getViewState()?.clearChatOnLoadChatHistory()
        viewModel.getExistingChat(messageId)
        swipeToRefresh.isRefreshing = true
        isChatRefreshed = true
    }

    override fun getSwipeRefreshLayoutResourceId() = 0

    override fun getRecyclerViewResourceId(): Int {
        return getBindingView().recyclerView.id
    }

    override fun loadInitialData() {
        getViewState()?.clearChatOnLoadChatHistory()
        showTopLoading()
        viewModel.getExistingChat(messageId)
    }

    private fun onSuccessGetExistingChatFirstTime(
        chatReplies: ChatReplies,
        chatroomViewModel: ChatroomViewModel
    ) {
        processDynamicAttachmentFromHistoryForContentCode100(chatroomViewModel)
        processDynamicAttachmentFromHistoryForContentCode101(chatroomViewModel)
        processDynamicAttachmentFromHistoryForContentCode107(chatroomViewModel)
        val list = filterChatList(chatroomViewModel)

        updateViewData(chatroomViewModel)
        renderList(list)
        getViewState()?.onSuccessLoadFirstTime(chatroomViewModel)

        updateHasNextState(chatReplies)
        updateHasNextAfterState(chatReplies)
        enableLoadMore()
        replyBubbleContainer?.setReplyListener(this)
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
                        dynamicAttachmentContents?.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes

                    val state = viewModel.validateHistoryForAttachment34(replyBoxAttribute)

                    if (state) {
                        return
                    }
                } catch (e: JsonSyntaxException) {
                    FirebaseCrashlytics.getInstance().recordException(e)
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
                        dynamicAttachmentContents?.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes

                    var state = false
                    if (replyBoxAttribute?.contentCode == REPLY_BOX_TOGGLE_VALUE) {
                        state = viewModel.validateHistoryForAttachment34(replyBoxAttribute)
                    }

                    if (state) {
                        return
                    }
                } catch (e: JsonSyntaxException) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    return
                }
            }
        }
    }

    private fun processDynamicAttachmentFromHistoryForContentCode107(chatroom: ChatroomViewModel) {
        val uiModel = chatroom.listChat.getOrElse(0) { return }
        if (uiModel !is DynamicAttachmentTextUiModel) {
            return
        }
        if (uiModel.attachmentType != DYNAMIC_ATTACHMENT) {
            return
        }
        uiModel.rejectReasons?.let { data -> viewModel.handleDynamicAttachmentRejectReasons(data) }
    }

    private fun onSuccessResetChatToFirstPage(
        chatReplies: ChatReplies,
        chatroomViewModel: ChatroomViewModel
    ) {
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

    override fun onError(throwable: Throwable) {
        getBindingView().footer.showToasterError(
            ErrorHandler.getErrorMessage(requireView().context, throwable)
        )
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
            visitable is com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel && visitable.isSender -> hideActionBubble()
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
            is ChatActionSelectionBubbleUiModel -> getViewState()?.onReceiveQuickReplyEventWithActionButton(
                visitable
            )

            is ChatRatingUiModel -> getViewState()?.onReceiveQuickReplyEventWithChatRating(
                visitable
            )

            else -> super.onReceiveMessageEvent(visitable)
        }
    }

    override fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo) {
        val generatedInvoice = generateInvoice(invoiceLinkPojo, opponentId, getUserSession().name)
        getViewState()?.removeInvoiceCarousel()
        hideActionBubble()
        getViewState()?.onShowInvoiceToChat(generatedInvoice)
        sendInvoiceAttachment(invoiceLinkPojo, generatedInvoice.startTime)
        enableTyping()
    }

    private fun attachInvoiceRetrieved(selectedInvoice: InvoiceLinkPojo) {
        val generatedInvoice = generateInvoice(selectedInvoice, "", getUserSession().name)
        getViewState()?.onShowInvoiceToChat(generatedInvoice)
        sendInvoiceAttachment(selectedInvoice, generatedInvoice.startTime)
    }

    private fun showSearchInvoiceScreen() {
        activity?.let {
            val bottomSheetUnify = TransactionInvoiceBottomSheet.newInstance(
                it,
                messageId.convertMessageIdToLong(),
                this
            )
            bottomSheetUnify.clearContentPadding = true
            bottomSheetUnify.show(childFragmentManager, "")
        }
    }

    override fun onQuickReplyClicked(model: QuickReplyUiModel, isFromDynamicAttachment: Boolean) {
        if (isFromDynamicAttachment) {
            if (model.action == DYNAMIC_REPLY_CSAT_YES) {
                processDynamicAttachmentButtonAction(model)
            } else if (model.action == DYNAMIC_REPLY_CSAT_NO) {
                openRejectReasonsBottomSheet()
            }
        } else {
            handleQuickReply(model)
        }
    }

    private fun processDynamicAttachmentButtonAction(model: QuickReplyUiModel) {
        getViewState()?.hideQuickReplyOnClick()
        selectedList.clear()
        reasonsBottomSheet?.clearChipList()
        rejectReasonsText = ""
        viewModel.sendDynamicAttachment108ForAcknowledgement(
            messageId,
            opponentId,
            model,
            SendableUiModel.generateStartTime(),
            Int.ZERO,
            isDismissClickOnRejectReasons
        )
        reasonsBottomSheet?.updateSendButtonStatus(false)
        isDismissClickOnRejectReasons = false
    }

    private fun handleQuickReply(model: QuickReplyUiModel) {
        chatbotAnalytics.get().eventClick(ACTION_QUICK_REPLY_BUTTON_CLICKED)
        viewModel.sendQuickReply(
            messageId,
            model,
            SendableUiModel.generateStartTime(),
            opponentId,
            false
        )
        getViewState()?.hideQuickReplyOnClick()
        hideCsatRatingView()
    }

    private fun openRejectReasonsBottomSheet() {
        dynamicAttachmentRejectReasons?.let {
            if (reasonsBottomSheet == null) {
                reasonsBottomSheet = ChatbotRejectReasonsBottomSheet.newInstance(
                    it,
                    selectedList,
                    rejectReasonsText
                )
            }
        }
        reasonsBottomSheet?.setUpListener(this)
        reasonsBottomSheet?.setUpChipClickListener(this)
        reasonsBottomSheet?.setText(rejectReasonsText)
        reasonsBottomSheet?.updateSendButtonStatus(false)
        reasonsBottomSheet?.show(childFragmentManager, "")
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
        viewModel.cancelImageUpload()
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
            rating = data.extras?.getLong(EMOJI_STATE) ?: 0
            reasonCode = data.getStringExtra(SELECTED_ITEMS) ?: ""
        }
        viewModel.submitChatCsat(messageId, input)
    }

    override fun onSuccessSubmitChatCsat(msg: String) {
        view?.let {
            csatOptionsUiModel?.let {
                getViewState()?.hideCsatOptionList(it)
            }
            getBindingView().footer.showToaster(
                msg,
                SNACK_BAR_TEXT_OK
            )
        }
    }

    private fun submitRating(data: Intent?) {
        val csatAttributes: Attributes?

        getViewState()?.scrollToBottom()
        csatAttributes = if (mCsatResponse == null) {
            attribute
        } else {
            mCsatResponse?.attachment?.attributes
        }

        val reasonList = csatAttributes?.reasons
        val input = com.tokopedia.chatbot.chatbot2.data.csatRating.csatInput.InputItem()
        input.chatbotSessionId = csatAttributes?.chatbotSessionId
        input.livechatSessionId = csatAttributes?.livechatSessionId
        input.reason = getFilters(data, reasonList)
        input.otherReason = data?.getStringExtra(BOT_OTHER_REASON_TEXT) ?: ""
        input.score = data?.extras?.getLong(EMOJI_STATE) ?: 0
        input.timestamp = data?.getStringExtra(TIME_STAMP)
        input.triggerRuleType = csatAttributes?.triggerRuleType

        viewModel.submitCsatRating(messageId, input)
        getBindingView().listQuickReply.show()
    }

    private fun getFilters(data: Intent?, reasonList: List<String?>?): String {
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
            FirebaseCrashlytics.getInstance().recordException(e)
            return ""
        }
    }

    override fun onSuccessSubmitCsatRating(msg: String) {
        hideCsatRatingView()
        getBindingView().footer.showToaster(msg, SNACK_BAR_TEXT_OK)
        getBindingView().listQuickReply.show()
    }

    private fun onPickedAttachImage(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        imageData = data
        viewModel.checkUploadSecure(messageId)
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
            viewModel.filterMediaUploadJobs(paths.originalPaths)
            val list = mutableListOf<VideoUploadData>()
            paths.originalPaths.forEach {
                processVideoPathToUpload(it)?.let { videoUploadUiModel ->
                    list.add(
                        VideoUploadData(
                            it,
                            messageId,
                            SendableUiModel.generateStartTime(),
                            videoUploadUiModel
                        )
                    )
                    getViewState()?.onVideoUpload(videoUploadUiModel)
                }
                sendAnalyticsForVideoUpload(it)
            }
            viewModel.updateMediaUris(list)
        }
    }

    override fun onVideoUploadChangeView(uiModel: VideoUploadUiModel) {
        getViewState()?.onVideoUpload(uiModel)
    }

    override fun setBigReplyBoxTitle(text: String, placeholder: String) {
        handleReplyBox(false)
        bigReplyBoxPlaceHolder = placeholder
        bigReplyBox?.setText(bigReplyBoxPlaceHolder)
        bigReplyBox?.shouldShowAddAttachmentButton(showAddAttachmentMenu)
        replyBoxBottomSheetPlaceHolder = placeholder
        replyBoxBottomSheetTitle = text
    }

    override fun hideReplyBox() {
        bigReplyBox?.hide()
        smallReplyBox?.hide()
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
                viewModel.uploadImageSecureUpload(
                    imageUploadUiModel,
                    messageId,
                    path
                )
            }
        }
        getBindingView().smallReplyBox.commentEditText?.editText?.apply {
            setDefaultDataInReplyBox()
        }
    }

    private fun onErrorImageUpload(imageUploadUiModel: ImageUploadUiModel, throwable: Throwable) {
        getBindingView().footer.showToasterError(
            ErrorHandler.getErrorMessage(requireView().context, throwable)
        )
        getViewState()?.showRetryUploadImages(imageUploadUiModel, true)
    }

    private fun processImagePathToUpload(path: String): ImageUploadUiModel? {
        val imageAttachmentValid = validateImageAttachment(
            path,
            ChatbotConstant.ImageUpload.MAX_FILE_SIZE_UPLOAD_SECURE
        )

        if (imageAttachmentValid == ValidImageAttachment.OverSizedImage) {
            onUploadOversizedImage()
            return null
        }

        if (imageAttachmentValid == ValidImageAttachment.UnderSizedImage) {
            onUploadUndersizedImage()
            return null
        }

        if (imageAttachmentValid != ValidImageAttachment.CorrectImage) {
            return null
        }

        if (!TextUtils.isEmpty(path)) {
            return generateChatViewModelWithImage(path)
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
                    com.tokopedia.chatbot.chatbot2.view.ChatbotInternalRouter.Companion.TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY
                ) ?: SelectedInvoice()
                attachInvoiceRetrieved(
                    AttachInvoiceMapper.convertInvoiceToDomainInvoiceModel(
                        selectedInvoice
                    )
                )
            }
        }
        enableTyping()
    }

    override fun prepareListener() {
        smallReplyBox?.sendButton?.setOnClickListener {
            if (isSendButtonActivated) {
                if (isFloatingSendButton) {
                    onSendFloatingInvoiceClicked()
                } else {
                    if (smallReplyBox?.sendButton?.isSlowModeRunning == false) {
                        onSendButtonClicked()
                    }
                }
            } else {
                getBindingView().footer.showToaster(
                    context?.resources?.getString(R.string.chatbot_float_invoice_input_length_zero)
                        .toBlankOrString()
                )
            }
        }
    }

    override fun onSendButtonClicked() {
        chatbotAnalytics.get().eventClick(ACTION_REPLY_BUTTON_CLICKED)
        val sendMessage = smallReplyBox?.getMessage() ?: ""
        val startTime = SendableUiModel.generateStartTime()

        sendTextMessage(
            sendMessage,
            startTime,
            replyBubbleContainer?.referredMsg,
            onSendingMessage(sendMessage, startTime, replyBubbleContainer?.referredMsg)
        )

        visibilityReplyBubble(false)
        smallReplyBox?.clearChatText()
    }

    private fun sendTextMessage(
        messageText: String,
        startTime: String,
        parentReply: ParentReply? = null,
        onSendingMessage: () -> Unit
    ) {
        viewModel.sendMessage(
            messageId,
            messageText,
            startTime,
            opponentId,
            parentReply,
            onSendingMessage
        )
    }

    private fun onSendingMessage(
        sendMessage: String,
        startTime: String,
        parentReply: ParentReply?
    ): () -> Unit {
        return {
            if (rvScrollListener?.hasNextAfterPage == true) {
                resetData()
                showTopLoading()
                messageSentNotFromFirstPage = true
                viewModel.getExistingChat(messageId)
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

    private fun slowDownSendButton() {
        if (smallReplyBox?.isVisible == true) {
            smallReplyBox?.sendButton?.startSlowDown()
        } else if (bigReplyBox?.isVisible == true) {
            bigReplyBox?.sendButton?.startSlowDown()
        }
    }

    override fun onChatActionBalloonSelected(
        selected: ChatActionBubbleUiModel,
        model: ChatActionSelectionBubbleUiModel
    ) {
        chatbotAnalytics.get().eventClick(ACTION_ACTION_BUBBLE_CLICKED)
        if (selected.action.equals(SEE_ALL_INVOICE_TEXT, true)) {
            showSearchInvoiceScreen()
        } else {
            getViewState()?.hideActionBubble(model)
            viewModel.sendActionBubble(
                messageId,
                selected,
                SendableUiModel.generateStartTime(),
                opponentId,
                model.isTypingBlocked ?: false
            )
            handleIsTypingBlocked(model.isTypingBlocked)
        }
    }

    private fun handleIsTypingBlocked(isTypingBlocked: Boolean?) {
        if (isTypingBlocked == true) {
            hideReplyBox()
        } else {
            enableTyping()
        }
    }

    override fun onClickRating(element: ChatRatingUiModel, rating: Int) {
        sendEvent(rating)
        chatRatingUiModel = element
        this.rating = rating
        viewModel.sendRating(messageId, rating, element)
    }

    private fun sendEvent(rating: Int) {
        if (rating == ChatRatingUiModel.RATING_GOOD) {
            chatbotAnalytics.get().eventClick(ACTION_THUMBS_UP_BUTTON_CLICKED)
        } else {
            chatbotAnalytics.get().eventClick(ACTION_THUMBS_DOWN_BUTTON_CLICKED)
        }
    }

    private fun onSuccessSendRating(rating: Int, element: ChatRatingUiModel): (
        com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo
    ) ->
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
                val isTargetTkpMeAndNotRedirect =
                    TextUtils.equals(uri.host, BASE_DOMAIN_SHORTENED) &&
                        !TextUtils.equals(uri.encodedPath, "/r")
                val isNeedAuthToken = isTargetDomainTokopedia || isTargetTkpMeAndNotRedirect

                val urlWithSession = URLGenerator.generateURLSessionLogin(
                    url,
                    session.deviceId,
                    session.userId
                )
                val applinkWebview =
                    String.format(
                        Locale.getDefault(),
                        "%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        urlWithSession
                    )
                if (isNeedAuthToken && RouteManager.isSupportApplink(activity, applinkWebview)) {
                    RouteManager.route(activity, applinkWebview)
                } else if (isBranchIOLink(url)) {
                    viewModel.extractBranchLink(url)
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
        getBindingView().footer.showToasterError(
            context?.resources?.getString(R.string.undersize_image).toBlankOrString()
        )
    }

    override fun onUploadOversizedImage() {
        getBindingView().footer.showToasterError(
            context?.resources?.getString(R.string.oversize_image).toBlankOrString()
        )
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
        mCsatResponse?.let {
            outState.putParcelable(CSAT_ATTRIBUTES, mCsatResponse?.attachment?.attributes)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        replyBubbleOnBoarding.flush()
        videoUploadOnBoarding.flush()
    }

    override fun createAttachmentMenus(): List<AttachmentMenu> {
        var list = mutableListOf<AttachmentMenu>()
        attachmentMenuRecyclerView?.apply {
            removeChatbotImageAttachmentMenu()
            removeVideoAttachmentMenu()
            if (showUploadImageButton) {
                list.add(com.tokopedia.chat_common.domain.pojo.attachmentmenu.ChatbotImageMenu())
                addChatbotImageAttachmentMenu()
            }
            if (showUploadVideoButton) {
                list.add(VideoMenu())
                addVideoAttachmentMenu()
            }
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
        chatbotAnalytics.get()?.eventOnVideoPick()
    }

    override fun onReceiveChatSepratorEvent(
        chatSepratorUiModel: ChatSepratorUiModel,
        quickReplyList: List<QuickReplyUiModel>
    ) {
        getViewState()?.showLiveChatSeprator(chatSepratorUiModel)
        getViewState()?.showLiveChatQuickReply(quickReplyList)
    }

    override fun updateToolbar(
        profileName: String?,
        profileImage: String?,
        badgeImage: ToolbarAttributes.BadgeImage?
    ) {
        if (activity is ChatbotActivity) {
            (activity as ChatbotActivity).upadateToolbar(profileName, profileImage, badgeImage)
        }
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        getViewState()?.showErrorWebSocket(isWebSocketError)
    }

    override fun chatOptionListSelected(
        selected: ChatOptionListUiModel,
        model: HelpFullQuestionsUiModel?
    ) {
        model?.let { getViewState()?.hideOptionList(it) }
        sendOptionListSelectedMessage(selected.text)

        selected.value.let {
            viewModel.hitGqlforOptionList(messageId, it, model)
        }
    }

    private fun sendOptionListSelectedMessage(selectedMessage: String) {
        val startTime = SendableUiModel.generateStartTime()
        sendTextMessage(
            selectedMessage,
            startTime,
            null,
            onSendingMessage(selectedMessage, startTime, null)
        )
    }

    override fun csatOptionListSelected(
        selected: ChatOptionListUiModel,
        model: CsatOptionsUiModel?
    ) {
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
        element: SendableUiModel
    ): (Int) -> Unit {
        return {
            if (element is ImageUploadUiModel) {
                when (it) {
                    RESEND -> handleImageResendBottomSheet(element)
                    DELETE -> handleImageDeleteBottomSheet(element)
                }
            } else if (element is VideoUploadUiModel) {
                when (it) {
                    RESEND -> handleVideoResendBottomSheet(element)
                    DELETE -> handleVideoDeleteBottomSheet(element)
                }
            }
        }
    }

    private fun handleImageResendBottomSheet(element: ImageUploadUiModel) {
        removeDummy(element)
        getViewState()?.onImageUpload(element)
        viewModel.uploadImageSecureUpload(
            element,
            messageId,
            element.imageUrl
        )
        if (mediaRetryBottomSheet?.isVisible == true) {
            mediaRetryBottomSheet?.dismiss()
        }
    }

    private fun handleImageDeleteBottomSheet(element: ImageUploadUiModel) {
        removeDummy(element)
        if (mediaRetryBottomSheet?.isVisible == true) {
            mediaRetryBottomSheet?.dismiss()
        }
        getBindingView().footer.showToaster(
            context?.resources?.getString(R.string.chatbot_your_picture_has_been_deleted)
                .toBlankOrString()
        )
    }

    private fun handleVideoResendBottomSheet(
        element: VideoUploadUiModel
    ) {
        removeDummy(element)
        if (mediaRetryBottomSheet?.isVisible == true) {
            mediaRetryBottomSheet?.dismiss()
        }
        element.isRetry = false
        getViewState()?.onVideoUpload(element)
        viewModel.updateMediaUris(
            listOf(
                VideoUploadData(
                    element.videoUrl,
                    messageId,
                    SendableUiModel.generateStartTime(),
                    element
                )
            )
        )
    }

    private fun handleVideoDeleteBottomSheet(element: VideoUploadUiModel) {
        removeDummy(element)
        if (mediaRetryBottomSheet?.isVisible == true) {
            mediaRetryBottomSheet?.dismiss()
        }
        getBindingView().footer.showToaster(
            context?.resources?.getString(R.string.chatbot_your_video_has_been_deleted)
                .toBlankOrString()
        )
    }

    private fun onErrorVideoUpload(uiModel: VideoUploadUiModel, errorMsg: String) {
        getBindingView().footer.showToasterError(
            errorMsg
        )
        getViewState()?.showRetryUploadVideos(uiModel)
    }

    override fun removeDummy(visitable: Visitable<*>) {
        getViewState()?.removeDummy(visitable)
    }

    override fun onStickyActionButtonClicked(invoiceRefNum: String, replyText: String) {
        this.invoiceRefNum = invoiceRefNum
        this.replyText = replyText
        viewModel.checkLinkForRedirectionForStickyActionClick(messageId, invoiceRefNum)
    }

    override fun onResume() {
        super.onResume()
        sendReplyTextForResolutionComponent()
    }

    private fun sendReplyTextForResolutionComponent() {
        if (isStickyButtonClicked) {
            this.isStickyButtonClicked = false
            viewModel.checkToSendMessageForResoLink(
                messageId,
                invoiceRefNum
            )
        }
    }

    override fun onBottomSheetDismissListener(data: Intent) {
        onSelectedInvoiceResult(Activity.RESULT_OK, data)
    }

    override fun transactionNotFoundClick() {
        val selected = getActionBubbleforNoTrasaction(view)
        viewModel.sendActionBubble(
            messageId,
            selected,
            SendableUiModel.generateStartTime(),
            opponentId,
            false
        )
        enableTyping()
    }

    override fun getUserName(): String {
        return senderNameForReply
    }

    override fun showReplyOption(messageUiModel: MessageUiModel, messageBubble: TextView?) {
        activity?.let {
            replyBubbleBottomSheet =
                ChatbotReplyBottomSheet.newInstance(replyBubbleEnabled)
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
        val bubblePosition = chatbotAdapter?.getBubblePosition(
            parentReply.replyTime
        )
        if (bubblePosition != RecyclerView.NO_POSITION) {
            if (bubblePosition != null) {
                smoothScrollToPosition(bubblePosition)
            }
        } else {
            resetData()
            setupBeforeReplyTime(
                ((parentReply.replyTimeMillisOffset).toLongOrZero() + ONCLICK_REPLY_TIME_OFFSET_FOR_REPLY_BUBBLE).toString()
            )
            isGetChatFromOnClick = true
            replyTime = parentReply.replyTime
            loadDataOnClick()
        }
    }

    override fun resetGuidelineForReplyBubble() {
        setGuidelineForReplyBubble(false)
    }

    private fun loadDataOnClick() {
        showTopLoading()
        viewModel.getTopChat(messageId)
    }

    private fun setGuidelineForReplyBubble(toSet: Boolean) {
        if (toSet) {
            val params = guideline?.layoutParams as ConstraintLayout.LayoutParams
            params.guideBegin = context?.dpToPx(GUIDELINE_VALUE_FOR_REPLY_BUBBLE)?.toInt()
                ?: DEFAULT_GUIDELINE_VALUE_FOR_REPLY_BUBBLE
            guideline?.layoutParams = params

            smallReplyBox?.commentEditText?.apply {
                setMargin(
                    top = REPLY_BOX_SIZE,
                    left = this.marginLeft,
                    right = this.marginRight,
                    bottom = this.marginBottom
                )
            }
        } else {
            val params = guideline?.layoutParams as ConstraintLayout.LayoutParams
            params.guideBegin = DEFAULT_GUIDELINE_VALUE_FOR_REPLY_BUBBLE
            guideline?.layoutParams = params

            smallReplyBox?.commentEditText?.apply {
                setMargin(
                    top = ZERO_POSITION,
                    left = this.marginLeft,
                    right = this.marginRight,
                    bottom = this.marginBottom
                )
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getBindingView().smallReplyBox.commentEditText?.windowToken, 0)
    }

    private fun checkCoachMarkStatus() {
        hideKeyboard()
        val isHasBeenShownCoachMark = replyBubbleOnBoarding.hasBeenShown()
        coachmarkHandler.postDelayed({
            if (!isHasBeenShownCoachMark) {
                val position = getPositionToAnchorReplyBubbleCoachmark()
                if (position == BUBBLE_NOT_FOUND) {
                    return@postDelayed
                } else if (position != RecyclerView.NO_POSITION) {
                    smoothScrollToPosition(position)
                }
            } else {
                showOnBoardCoachMark()
            }
        }, DELAY_TO_SHOW_COACHMARK)
    }

    private fun getPositionToAnchorReplyBubbleCoachmark(): Int {
        return chatbotAdapter?.getMostRecentTokopediaCareMessage() ?: BUBBLE_NOT_FOUND
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
        pojo: com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo
    ) {
        (activity as Activity).run {
            chatRatingUiModel?.let {
                (viewState as ChatbotViewState).onSuccessSendRating(
                    pojo,
                    rating,
                    it,
                    this
                )
            }
        }
    }

    private fun initRecyclerViewListener() {
        rvScrollListener = object :
            RecyclerViewScrollListener((recyclerView?.layoutManager as LinearLayoutManager)) {
            override fun loadMoreTop() {
                showTopLoading()
                viewModel.getTopChat(messageId)
            }

            override fun loadMoreDown() {
                showBottomLoading()
                viewModel.getBottomChat(messageId)
            }

            override fun scrollDone() {
                if (!isConnectedToAgent) {
                    return
                }
                if (videoUploadOnBoardingHasBeenShow && replyBubbleOnBoardingHasBeenShow) {
                    return
                }

                getPositionToShowCoachMark()
            }
        }.also {
            recyclerView?.addOnScrollListener(it)
        }
    }

    private fun getPositionToShowCoachMark() {
        val position = getPositionToAnchorReplyBubbleCoachmark()

        val firstPosition =
            ((recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
        val location = IntArray(2)
        recyclerView?.getChildAt(position - firstPosition)
            ?.getLocationOnScreen(location)

        yForReplyBubbleOnboarding = location[1]
        showOnBoardCoachMark()
    }

    private fun onErrorGetBottomChat() {
        chatbotAdapter?.hideBottomLoading()
        rvScrollListener?.finishBottomLoadingState()
    }

    private fun onErrorGetTopChat() {
        hideTopLoading()
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
                    dynamicAttachmentContents?.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes

                if (!CheckDynamicAttachmentValidity.checkValidity(replyBoxAttribute?.contentCode)) {
                    return false
                }
            } catch (e: JsonSyntaxException) {
                FirebaseCrashlytics.getInstance().recordException(e)
                return true
            }
        }
        return true
    }

    private fun onSuccessGetTopChatData(
        chatReplies: ChatReplies,
        chatroomViewModel: ChatroomViewModel
    ) {
        val list = filterChatList(chatroomViewModel)
        if (list.isNotEmpty()) {
            val filteredList = getViewState()?.clearDuplicate(list)
            updateHasNextState(chatReplies)
            rvScrollListener?.finishTopLoadingState()
            filteredList?.let { renderList ->
                renderTopList(renderList)
                if (isGetChatFromOnClick) {
                    val bubblePosition = chatbotAdapter?.getBubblePosition(
                        replyTime
                    )
                    if (bubblePosition != RecyclerView.NO_POSITION) {
                        if (bubblePosition != null) {
                            smoothScrollToPosition(bubblePosition)
                        }
                    }
                }
            }
            if (isGetChatFromOnClick && replyTime.isNotEmpty()) {
                replyTime = ""
                isGetChatFromOnClick = false
                updateHasNextAfterState(chatReplies)
            }
        } else {
            viewModel.getExistingChat(messageId)
        }
    }

    private fun onSuccessGetBottomChatData(
        chatReplies: ChatReplies,
        chatroomViewModel: ChatroomViewModel
    ) {
        val list = filterChatList(chatroomViewModel)
        if (list.isNotEmpty()) {
            val filteredList = getViewState()?.clearDuplicate(list)
            rvScrollListener?.finishBottomLoadingState()
            if (filteredList?.isNotEmpty() == true) {
                renderBottomList(filteredList)
            } else {
                viewModel.getBottomChat(messageId)
            }
            updateHasNextAfterState(chatReplies)
        } else {
            if (rvScrollListener?.hasNextAfterPage == true) {
                viewModel.getBottomChat(messageId)
            } else {
                chatbotAdapter?.hideBottomLoading()
                rvScrollListener?.finishBottomLoadingState()
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
        chatbotAdapter?.showTopLoading()
    }

    private fun showBottomLoading() {
        chatbotAdapter?.showBottomLoading()
    }

    private fun setupBeforeReplyTime(replyTimeMillis: String) {
        messageCreateTime = replyTimeMillis
        setupBeforeReplyTime()
    }

    private fun setupBeforeReplyTime() {
        if (messageCreateTime.isNotEmpty()) {
            viewModel.setBeforeReplyTime(messageCreateTime)
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

    private fun resetData() {
        rvScrollListener?.reset()
        viewModel.clearGetChatUseCase()
        chatbotAdapter?.reset()
        showTopLoading()
    }

    override fun onRetrySendVideo(element: VideoUploadUiModel) {
        createRetryMediaUploadBottomSheet(element)
    }

    override fun onVideoUploadCancelClicked(video: VideoUploadUiModel) {
        viewModel.cancelVideoUpload(video.videoUrl!!, SOURCE_ID_FOR_VIDEO_UPLOAD)
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
        mediaRetryBottomSheet = ChatbotMediaRetryBottomSheet.newInstance(
            element,
            onBottomSheetItemClicked(element)
        )
        activity?.let {
            mediaRetryBottomSheet?.show(
                childFragmentManager,
                context?.resources?.getString(R.string.chatbot_reply_bubble_bottomsheet_retry)
            )
        }
    }

    private fun sessionChangeStateHandler(state: Boolean) {
        isConnectedToAgent = state
        replyBubbleEnabled = state
        if (state) {
            checkCoachMarkStatus()
            handleAddAttachmentButtonViewState(showAddAttachmentMenu)
        }
    }

    override fun videoUploadEligibilityHandler(state: Boolean) {
        isEligibleForVideoUpload = state
    }

    override fun disableSendButton() {
        isSendButtonActivated = false
        smallReplyBox?.sendButton?.disableSendButton()
    }

    override fun enableSendButton() {
        isSendButtonActivated = true
        smallReplyBox?.sendButton?.enableSendButton()
    }

    override fun isInvoiceRemoved(isRemoved: Boolean) {
        isFloatingInvoiceCancelled = isRemoved
        smallReplyBox?.removeTextChangedListener()
    }

    override fun onAttachmentMenuClicked() {
        attachmentMenuRecyclerView?.toggle()
        createAttachmentMenus()
    }

    override fun goToBigReplyBoxBottomSheet(isError: Boolean) {
        activity?.let {
            if (bigReplyBoxBottomSheet == null) {
                bigReplyBoxBottomSheet = BigReplyBoxBottomSheet
                    .newInstance(
                        replyBoxBottomSheetPlaceHolder,
                        replyBoxBottomSheetTitle,
                        showAddAttachmentMenu,
                        ""
                    )
            }
            bigReplyBoxBottomSheet?.setErrorStatus(isError)
            BigReplyBoxBottomSheet.replyBoxClickListener = this
            bigReplyBoxBottomSheet?.clearContentPadding = true
            if (bigReplyBox?.sendButton?.isSlowModeRunning == false) {
                bigReplyBoxBottomSheet?.show(childFragmentManager, "")
            }
        }
    }

    override fun getMessageContentFromBottomSheet(msg: String) {
        val startTime = SendableUiModel.generateStartTime()
        if (msg == bigReplyBoxPlaceHolder) {
            return
        }
        enableTyping()
        hideKeyboard()
        viewModel.sendMessage(
            messageId,
            msg,
            startTime,
            opponentId,
            replyBubbleContainer?.referredMsg,
            onSendingMessage(msg, startTime, replyBubbleContainer?.referredMsg)
        )
        bigReplyBoxBottomSheet?.clearMessageText()
        enableSendButton()
    }

    override fun dismissBigReplyBoxBottomSheet(message: String, wordLength: Int) {
        if (message.isEmpty()) {
            bigReplyBox?.setText(bigReplyBoxPlaceHolder)
            return
        }
        bigReplyBox?.setText(message)
        if (wordLength >= MINIMUM_NUMBER_OF_WORDS) {
            bigReplyBox?.enableSendButton()
            enableSendButton()
        } else {
            bigReplyBox?.disableSendButton()
        }
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        videoUploadOnBoarding.flush()
        replyBubbleOnBoarding.flush()
        coachmarkHandler.removeCallbacksAndMessages(null)
        smallReplyBox?.sendButton?.cancelSlowDown()
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

    private fun checkVideoUploadOnboardingStatus() {
        setHandlingViewHelperVideoCoachMark()
        showCoachMarkOfVideoReplay()
    }

    private fun setHandlingViewHelperVideoCoachMark() {
        getBindingView().coachMarkVideoHelper.apply {
            bringToFront()
            show()
            isClickable = true
            setOnClickListener {
                videoUploadOnBoarding.dismiss()
            }
        }
    }

    private fun showCoachMarkOfVideoReplay() {
        isVideoCoachMarkShowing = true
        videoUploadOnBoarding.onboardingDismissListener = object : OnboardingVideoDismissListener {
            override fun dismissVideoUploadOnBoarding() {
                isVideoCoachMarkShowing = false
                getBindingView().coachMarkVideoHelper.gone()
                smoothScrollToPosition(ZERO_POSITION)
            }
        }
        if (smallReplyBox?.isVisible.orFalse()) {
            getBindingView().smallReplyBox.showCoachMark(videoUploadOnBoarding)
        }
    }

    /**
     * Getting the x and y coordinates from ChatbotFragment , calculate the ratioY and align the
     * guideline accordingly. The guideline will change the position of the reply_bubble_holder view
     * which is used to show the pointer on the coachmark
     * */
    private fun calculateRatiosForGuideline(): Float {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val givenY = yForReplyBubbleOnboarding
        return (givenY.toFloat() / height.toFloat())
    }

    private fun setUpPositionReplyBubbleGuideline(ratioY: Float) {
        if (ratioY == ZERO_RATIO) {
            return
        }
        val params =
            getBindingView().guidelineReplyBubble.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = ratioY
        getBindingView().guidelineReplyBubble.layoutParams = params
    }

    private fun showReplayCoachMark() {
        setHandlingViewHelperReplayCoachMark()
        setReplayBubbleCoachMarkListener()
        replyBubbleOnBoarding.showReplyBubbleOnBoarding(
            getBindingView().replyBubbleHolder,
            context
        )
    }

    private fun setReplayBubbleCoachMarkListener() {
        replyBubbleOnBoarding.onboardingDismissListener =
            object : com.tokopedia.chatbot.chatbot2.view.util.OnboardingReplayDismissListener {
                override fun dismissReplyBubbleOnBoarding() {
                    isReplyCoachMarkShowing = false
                    checkVideoUploadOnboardingStatus()
                }
            }
    }

    private fun setHandlingViewHelperReplayCoachMark() {
        val coachMarkLineHelper = getBindingView().coachMarkReplayHelper
        val coachMarkHelper = getBindingView().frameCoachMarkReplay
        coachMarkLineHelper.apply {
            show()
        }
        coachMarkHelper.apply {
            bringToFront()
            show()
            isClickable = true
            setOnClickListener {
                coachMarkLineHelper.gone()
                gone()
                replyBubbleOnBoarding.dismiss()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return if (isReplyCoachMarkShowing) {
            replyBubbleOnBoarding.dismiss()
            true
        } else if (isVideoCoachMarkShowing) {
            videoUploadOnBoarding.dismiss()
            true
        } else {
            super.onBackPressed()
        }
    }

    override fun onButtonActionClicked(bubble: ChatActionBubbleUiModel) {
        val startTime = SendableUiModel.generateStartTime()
        viewModel.sendDynamicAttachmentText(
            messageId,
            bubble,
            startTime,
            opponentId
        )
        getViewState()?.removeDynamicStickyButton()
        getViewState()?.scrollToBottom()
    }

    private fun checkIsChatbotServerActive(isChatbotActive: Boolean) {
        if (!isChatbotActive) {
            setErrorLayoutForServer()
        }
    }

    override fun submitRejectReasonsViaSocket(
        selectedReasons: List<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>,
        reasonText: String,
        helpfulQuestion: DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion?
    ) {
        getViewState()?.hideQuickReplyOnClick()
        hideKeyboard()
        selectedList.clear()
        reasonsBottomSheet?.clearChipList()
        rejectReasonsText = ""
        val list = mutableListOf<Long>()
        selectedReasons.forEach {
            list.add(it.code)
        }
        viewModel.sendDynamicAttachment108(
            list,
            reasonText,
            messageId,
            opponentId,
            SendableUiModel.generateStartTime(),
            helpfulQuestion,
            Int.ONE,
            isDismissClickOnRejectReasons
        )
        isDismissClickOnRejectReasons = false
        reasonsBottomSheet?.updateSendButtonStatus(false)
    }

    override fun isDismissClicked(isDismissClickOnRejectReasons: Boolean, text: String) {
        this.isDismissClickOnRejectReasons = isDismissClickOnRejectReasons
        this.rejectReasonsText = text
    }

    override fun onChipClick(count: Int) {
        reasonsBottomSheet?.checkChipCounter(count)
    }

    override fun onReceiveOwocInvoiceList() {
        hideKeyboard()
    }
}
