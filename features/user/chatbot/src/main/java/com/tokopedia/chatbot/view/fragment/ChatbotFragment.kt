package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.data.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.AttachInvoiceMapper
import com.tokopedia.chatbot.domain.InvoiceLinkPojo
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.customview.ChatbotViewState
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import javax.inject.Inject

/**
 * @author by nisie on 23/11/18.
 */
class ChatbotFragment : BaseChatFragment(), ChatbotContract.View,
        AttachedInvoiceSelectionListener, QuickReplyListener {

    val TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114

    @Inject
    lateinit var presenter: ChatbotPresenter

    private lateinit var chatbotViewState: ChatbotViewState

    override fun initInjector() {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        mapMessageToList(visitable)
        checkHideQuickReply(visitable)
    }

    private fun checkHideQuickReply(visitable: Visitable<*>) {
        if (visitable is BaseChatViewModel
                && TextUtils.isEmpty(visitable.attachmentId)
                && chatbotViewState.hasQuickReply()
                && !isMyMessage(visitable.fromUid)) {
            chatbotViewState.hideQuickReply()
        }
    }

    private fun isMyMessage(fromUid: String?): Boolean {
        return fromUid != null && userSession.userId == fromUid
    }

    private fun mapMessageToList(visitable: Visitable<*>) {
        when (visitable) {
            is QuickReplyListViewModel -> chatbotViewState.showQuickReply()
            else -> super.onReceiveMessageEvent(visitable)
        }
    }

    override fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo) {
        val generatedInvoice = generateInvoice(invoiceLinkPojo)
        adapter.addElement(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, invoiceLinkPojo, generatedInvoice.startTime)
        chatbotViewState.scrollToBottom()
    }

    override fun showSearchInvoiceScreen() {
        activity?.run {
            val intent = ChatbotInternalRouter.Companion.getAttachInvoiceIntent(this,
                    userSession.userId,
                    messageId.toInt())
            startActivityForResult(intent, TOKOPEDIA_ATTACH_INVOICE_REQ_CODE)
        }

    }

    override fun onQuickReplyClicked(quickReplyListViewModel: QuickReplyListViewModel,
                                     model: QuickReplyViewModel) {

        presenter.sendQuickReply(messageId, model, SendableViewModel.generateStartTime())

    }

    private fun generateInvoice(selectedInvoice: InvoiceLinkPojo): AttachInvoiceSentViewModel {
        val invoiceLinkAttributePojo = selectedInvoice.attributes
        return AttachInvoiceSentViewModel(
                senderId,
                userSession.name,
                invoiceLinkAttributePojo.title,
                invoiceLinkAttributePojo.description,
                invoiceLinkAttributePojo.imageUrl,
                invoiceLinkAttributePojo.totalAmount,
                SendableViewModel.generateStartTime()
        )
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

    private fun attachInvoiceRetrieved(selectedInvoice: InvoiceLinkPojo) {
        val generatedInvoice = generateInvoice(selectedInvoice)
        adapter.addElement(generatedInvoice)
        presenter.sendInvoiceAttachment(messageId, selectedInvoice, generatedInvoice.startTime)
        chatbotViewState.scrollToBottom()
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
                            userSession.deviceId,
                            userSession.userId))
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
}
