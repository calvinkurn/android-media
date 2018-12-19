package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.SendableViewModel
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
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.listener.ChatbotViewState
import com.tokopedia.chatbot.view.listener.ChatbotViewStateImpl
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.design.component.ToasterError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 23/11/18.
 */
class ChatbotFragment : BaseChatFragment(), ChatbotContract.View,
        AttachedInvoiceSelectionListener, QuickReplyListener, ChatActionListBubbleListener, ChatRatingListener {

    val TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114

    @Inject
    lateinit var presenter: ChatbotPresenter

    @Inject
    lateinit var session: UserSessionInterface

    lateinit var replyEditText : EditText

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.viewState = ChatbotViewStateImpl(view, session, this, this,
                this, this, this,
                this, this, (activity as BaseChatToolbarActivity).getToolbar())
        viewState.initView()
        loadInitialData()
    }

    override fun loadInitialData() {
        presenter.getExistingChat(messageId, onError(), onSuccessGetExistingChat())
        presenter.connectWebSocket(messageId)

    }

    private fun onSuccessGetExistingChat(): (ChatroomViewModel) -> Unit {

        return {
            setCanLoadMore(it)
            getViewState().onSuccessLoadFirstTime(it)
        }
    }

    private fun setCanLoadMore(chatroomViewModel: ChatroomViewModel) {
        if (chatroomViewModel.canLoadMore) {
            enableLoadMore()
        } else {
            disableLoadMore()
        }
    }


    private fun onError(): (Throwable) -> Unit {
        return {
            if (view != null) {
                ToasterError.make(view, ErrorHandler.getErrorMessage(view!!.context, it)).show()
            }
        }
    }

    override fun loadData(page: Int) {

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
        val generatedInvoice = presenter.generateInvoice(invoiceLinkPojo, senderId)
        getViewState().onShowInvoiceToChat(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, invoiceLinkPojo, generatedInvoice.startTime)
    }

    private fun attachInvoiceRetrieved(selectedInvoice: InvoiceLinkPojo) {
        val generatedInvoice = presenter.generateInvoice(selectedInvoice, "")
        getViewState().onShowInvoiceToChat(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, selectedInvoice, generatedInvoice.startTime)
    }

    override fun showSearchInvoiceScreen() {
        activity?.run {
            val intent = ChatbotInternalRouter.Companion.getAttachInvoiceIntent(this,
                    session.userId,
                    messageId.toInt())
            startActivityForResult(intent, TOKOPEDIA_ATTACH_INVOICE_REQ_CODE)
        }
    }

    override fun onQuickReplyClicked(quickReplyListViewModel: QuickReplyListViewModel,
                                     model: QuickReplyViewModel) {

        presenter.sendQuickReply(messageId, model, SendableViewModel.generateStartTime())

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOKOPEDIA_ATTACH_INVOICE_REQ_CODE -> onSelectedInvoiceResult(resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
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

                if (isNeedAuthToken && RouteManager.isSupportApplink(activity, String.format
                        ("%s?url=%s", ApplinkConst.WEBVIEW, url))) {

                    RouteManager.route(activity, URLGenerator.generateURLSessionLogin(url,
                            session.deviceId,
                            session.userId))
                } else {
                    super.onGoToWebView(url, id)
                }

            }
        }
    }

    /**
     * To handle url manually to webview. In this case, to contact us.
     */
    override fun shouldHandleUrlManually(url: String): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSendButtonClicked() {
        val sendMessage = replyEditText.text.toString()

        getViewState().onSendingMessage(messageId, getUserSession().userId, getUserSession()
                .name, sendMessage)
        presenter.sendMessage(messageId, sendMessage)
    }

    override fun onChatActionBalloonSelected(selected: ChatActionBubbleViewModel, model: ChatActionSelectionBubbleViewModel) {
        presenter.sendActionBubble()
    }

    override fun onClickRating(element: ChatRatingViewModel, rating: Int) {
        (viewState as ChatbotViewState).onSendRating(element, rating)
        presenter.sendRating(rating, onError(), onSuccessSendRating())
    }

    private fun onSuccessSendRating(): () -> Unit {
        return {
            (activity as Activity).run {
                //            (viewState as ChatbotViewState).onSuccessSendRating(element, rating, this,
//                    onClickReasonRating())
            }
        }
    }

    fun onClickReasonRating() {
        presenter.sendReasonRating()
    }
}
