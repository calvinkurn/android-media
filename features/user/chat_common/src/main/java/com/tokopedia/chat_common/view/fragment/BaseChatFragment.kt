package com.tokopedia.chat_common

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

/**
 * @author by nisie on 23/11/18.
 */
abstract class BaseChatFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
        , ImageAnnouncementListener, ChatLinkHandlerListener
        , ImageUploadListener, ProductAttachmentListener, TypingListener
        , BaseChatContract.View {

    open lateinit var viewState: BaseChatViewState

    protected var messageId: String = ""
    protected var opponentId = ""
    protected var opponentName = ""
    protected var opponentRole = ""
    protected var shopId = 0

    protected var toShopId = "0"
    protected var toUserId = "0"
    protected var source = ""


    override fun getAdapterTypeFactory(): BaseChatTypeFactoryImpl {
        return BaseChatTypeFactoryImpl(this,
                this, this, this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        return
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chatroom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewState = BaseChatViewStateImpl(view, (activity as BaseChatToolbarActivity).getToolbar(), this)
        viewState.initView()

        setupViewData(arguments, savedInstanceState)
        prepareView(view)
        prepareListener()
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    open fun prepareListener() {
        view?.findViewById<View>(R.id.send_but)?.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private fun prepareView(view: View) {
        getRecyclerView(view).setHasFixedSize(true)
    }

    private fun setupViewData(arguments: Bundle?, savedInstanceState: Bundle?) {
        messageId = getParamString(ApplinkConst.Chat.MESSAGE_ID, arguments, savedInstanceState)
        opponentId = getParamString(ApplinkConst.Chat.OPPONENT_ID, arguments, savedInstanceState)
        opponentName = getParamString(ApplinkConst.Chat.OPPONENT_NAME, arguments, savedInstanceState)
        opponentRole = getParamString(ApplinkConst.Chat.OPPONENT_ROLE, arguments, savedInstanceState)
        source = getParamString(ApplinkConst.Chat.SOURCE, arguments, savedInstanceState)
        toShopId = getParamString(ApplinkConst.Chat.TO_SHOP_ID, arguments, savedInstanceState)
        toUserId = getParamString(ApplinkConst.Chat.TO_USER_ID, arguments, savedInstanceState)
    }

    open fun getParamString(paramName: String, arguments: Bundle?,
                            savedInstanceState: Bundle?): String {
        return when {
            savedInstanceState != null
                    && savedInstanceState.getString(paramName, "").isNotEmpty()
            -> savedInstanceState.getString(paramName)
            arguments != null && arguments.getString(paramName, "").isNotEmpty()
            -> arguments.getString(paramName)
            else -> ""
        }
    }

    open fun getParamInt(paramName: String, arguments: Bundle?,
                         savedInstanceState: Bundle?): Int {
        return when {
            savedInstanceState != null -> savedInstanceState.getInt(paramName, 0)
            arguments != null -> arguments.getInt(paramName, 0)
            else -> 0
        }
    }


    override fun onImageAnnouncementClicked(viewModel: ImageAnnouncementViewModel) {
        if (!TextUtils.isEmpty(viewModel.redirectUrl)) {
            onGoToWebView(viewModel.redirectUrl, viewModel.attachmentId)
        }
    }

    override fun shouldHandleUrlManually(url: String): Boolean {
        val urlManualHandlingList = arrayOf<String>(TkpdBaseURL.BASE_CONTACT_US)
        return Arrays.asList(*urlManualHandlingList).contains(url)
    }

    override fun onGoToWebView(url: String, id: String) {
        val PARAM_URL = "PARAM_URL"
        val IS_CHAT_BOT = "IS_CHAT_BOT"

        if (url.isNotEmpty() && activity != null) {
            KeyboardHandler.DropKeyboard(activity, view)
            val uri = Uri.parse(url)

            when {
                isContactUsLink(uri) -> {
                    val intent = RouteManager.getIntent(activity, url)
                    intent.putExtra(PARAM_URL, URLGenerator.generateURLSessionLogin(
                            if (TextUtils.isEmpty(url)) TkpdBaseURL.BASE_CONTACT_US else url,
                            getUserSession().deviceId,
                            getUserSession().userId))
                    intent.putExtra(IS_CHAT_BOT, true)
                    startActivity(intent)
                }
                RouteManager.isSupportApplink(activity, url) -> RouteManager.route(activity, url)
                isBranchIOLink(url) -> handleBranchIOLinkClick(url)
                else -> {
                    val applinkRouter = activity!!.applicationContext as ApplinkRouter
                    applinkRouter.goToApplinkActivity(activity,
                            String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
                }
            }
        }
    }

    private fun isContactUsLink(uri: Uri?): Boolean {
        val CONTACT_US_PATH_SEGMENT = "toped-contact-us"
        return uri != null
                && uri.pathSegments != null
                && uri.pathSegments.contains(CONTACT_US_PATH_SEGMENT)
    }

    override fun handleBranchIOLinkClick(url: String) {
        activity?.run {
            val applinkRouter = this.applicationContext as ApplinkRouter
            var intent = applinkRouter.getApplinkIntent(activity, ApplinkConst.CONSUMER_SPLASH_SCREEN)
            intent.putExtra("branch", url)
            intent.putExtra("branch_force_new_session", true)
            startActivity(intent)
        }
    }

    override fun isBranchIOLink(url: String): Boolean {
        val BRANCH_IO_HOST = "tokopedia.link"
        val uri = Uri.parse(url)
        return uri.host != null && uri.host == BRANCH_IO_HOST
    }

    override fun onProductClicked(element: ProductAttachmentViewModel) {
        val ROLE_SHOP = "shop"

        if (!GlobalConfig.isSellerApp() || opponentRole != ROLE_SHOP) {
            activity?.run {

                var routingAppLink: String = ApplinkConst.PRODUCT_INFO
                val uriBuilder = Uri.Builder()
                uriBuilder.appendQueryParameter(ApplinkConst.Query.PDP_ID, element.productId.toString())
                        .appendQueryParameter(ApplinkConst.Query.PDP_PRICE, element.productPrice)
                        .appendQueryParameter(ApplinkConst.Query.PDP_NAME, element.productName)
                        .appendQueryParameter(ApplinkConst.Query.PDP_DATE, element.dateTimeInMilis.toString())
                        .appendQueryParameter(ApplinkConst.Query.PDP_IMAGE, element.productImage.toString())

                routingAppLink += uriBuilder.toString()
                RouteManager.route(context, routingAppLink)
            }

        } else {
            //Necessary to do it this way to prevent PDP opened in seller app
            //otherwise someone other than the owner can access PDP with topads promote page
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(element.productUrl))
            startActivity(browserIntent)
        }
    }

    override fun onRetrySendImage(element: ImageUploadViewModel) {

    }

    override fun onReceiveStartTypingEvent() {
        viewState.onShowStartTyping()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        viewState.removeMessageOnReplyBox()
        viewState.removeDummyIfExist(visitable)
        viewState.onReceiveMessageEvent(visitable)
    }

    override fun onReceiveStopTypingEvent() {
        viewState.onShowStopTyping()
    }

    override fun onReceiveReadEvent() {
        viewState.onReceiveRead()
    }

    override fun onClickBuyFromProductAttachment(element: ProductAttachmentViewModel) {
        //Please override if you use
    }

    override fun onClickATCFromProductAttachment(element: ProductAttachmentViewModel) {
        //Please override if you use
    }

    abstract fun onSendButtonClicked()

    abstract fun getUserSession(): UserSessionInterface

    open fun updateViewData(it: ChatroomViewModel) {
        this.opponentId = it.headerModel.senderId
        this.opponentName = it.headerModel.name
        this.opponentRole = it.headerModel.role
        this.shopId = it.headerModel.shopId
    }

}