package com.tokopedia.chat_common

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.URLUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.VoucherMenu
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chat_common.view.fragment.BaseChatActivityListener
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.user.session.UserSessionInterface
import java.net.URLEncoder
import java.util.*

/**
 * @author by nisie on 23/11/18.
 */
abstract class BaseChatFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
        , ImageAnnouncementListener, ChatLinkHandlerListener
        , ImageUploadListener, ProductAttachmentListener, TypingListener
        , BaseChatContract.View
        , BaseChatActivityListener
        , AttachmentMenu.AttachmentMenuListener {

    open lateinit var viewState: BaseChatViewState

    protected var messageId: String = ""
    protected var opponentId = ""
    protected var opponentName = ""
    protected var opponentRole = ""
    protected var shopId: Long = 0L
    protected var toShopId = "0"
    protected var toUserId = "0"
    protected var source = ""
    protected var amISeller = false

    abstract fun onCreateViewState(view: View): BaseChatViewState
    abstract fun onSendButtonClicked()
    abstract fun getUserSession(): UserSessionInterface

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewState(view)
        setupViewData(arguments, savedInstanceState)
        prepareView(view)
        prepareListener()
    }

    private fun setupViewState(view: View?) {
        view?.let {
            viewState = onCreateViewState(it)
        }
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    open fun prepareListener() { }

    private fun prepareView(view: View) {
        getRecyclerView(view)?.setHasFixedSize(true)
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
            -> savedInstanceState.getString(paramName, "")
            arguments != null && arguments.getString(paramName, "").isNotEmpty()
            -> arguments.getString(paramName, "")
            else -> ""
        }
    }

    open fun getParamInt(paramName: String, arguments: Bundle?,
                         savedInstanceState: Bundle?): Int {
        return when {
            savedInstanceState != null -> savedInstanceState.getInt(paramName, -1)
            arguments != null -> arguments.getInt(paramName, -1)
            else -> -1
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
                isBranchIOLink(url) -> handleBranchIOLinkClick(url)
                RouteManager.isSupportApplink(activity, url) && !URLUtil.isNetworkUrl(url) -> RouteManager.route(activity, url)
                else -> {
                    val encodedUrl = URLEncoder.encode(url, "UTF-8")
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl))
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
        if (!GlobalConfig.isSellerApp()) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.CONSUMER_SPLASH_SCREEN)
            intent.putExtra("branch", url)
            intent.putExtra("branch_force_new_session", true)
            startActivity(intent)
        } else {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    private fun openWebview(url: String) {
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
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
                RouteManager.route(this, ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                        element.productId.toString())
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

    open fun updateViewData(it: ChatroomViewModel) {
        this.opponentId = it.headerModel.senderId
        this.opponentName = it.headerModel.name
        this.opponentRole = it.headerModel.role
        this.shopId = it.headerModel.shopId
        this.amISeller = it.isSeller()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewState.clear()
    }

    override fun onBackPressed(): Boolean {
        if (viewState.isAttachmentMenuVisible()) {
            viewState.hideAttachmentMenu()
            return true
        }
        return false
    }

    override fun createAttachmentMenus(): List<AttachmentMenu> {
        return emptyList()
    }

    override fun onClickAttachProduct(menu: AttachmentMenu) {}

    override fun onClickAttachImage(menu: AttachmentMenu) {}

    override fun onClickAttachInvoice(menu: AttachmentMenu) {}

    override fun onClickAttachVoucher(voucherMenu: VoucherMenu) {}

    override fun onClickBannedProduct(viewModel: BannedProductAttachmentViewModel) {}

    override fun trackSeenProduct(element: ProductAttachmentViewModel) {}

    override fun trackSeenBannedProduct(viewModel: BannedProductAttachmentViewModel) {}

    override fun onClickAddToWishList(product: ProductAttachmentViewModel, success: () -> Unit) {}

    override fun onClickRemoveFromWishList(productId: String, success: () -> Unit) {}

    override fun trackClickProductThumbnail(product: ProductAttachmentViewModel) {}

    override fun onItemClicked(t: Visitable<*>?) {}
}