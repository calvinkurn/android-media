package com.tokopedia.chat_common

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chat_common.view.BaseChatViewState
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by nisie on 23/11/18.
 */
abstract class BaseChatFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
        , ImageAnnouncementListener, ChatLinkHandlerListener
        , ImageUploadListener, ProductAttachmentListener
        , BaseChatContract.View {

    @Inject
    private lateinit var presenter: BaseChatPresenter

    @Inject
    protected lateinit var userSession: UserSessionInterface

    protected lateinit var baseChatViewState: BaseChatViewState

    protected var messageId: String = ""
    protected var senderId = ""
    protected var title = ""
    protected var senderRole = ""

    override fun getAdapterTypeFactory(): BaseChatTypeFactoryImpl {
        return BaseChatTypeFactoryImpl(this,
                this, this, this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        return
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

        presenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view).setHasFixedSize(true)
        setupViewData(arguments, savedInstanceState)
        presenter.connectWebSocket(messageId)
    }

    private fun setupViewData(arguments: Bundle?, savedInstanceState: Bundle?) {
        messageId = when {
            savedInstanceState != null
                    && savedInstanceState.getString("message_id", "").isNotEmpty()
            -> savedInstanceState.getString("message_id")
            arguments != null && arguments.getString("message_id", "").isNotEmpty()
            -> arguments.getString("message_id")
            else -> ""
        }

        senderId = when {
            savedInstanceState != null
                    && savedInstanceState.getString("sender_id", "").isNotEmpty()
            -> savedInstanceState.getString("sender_id")
            arguments != null && arguments.getString("sender_id", "").isNotEmpty()
            -> arguments.getString("sender_id")
            else -> ""
        }
    }

    override fun loadData(page: Int) {
        return
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
                            userSession.deviceId,
                            userSession.userId))
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

    override fun onImageUploadClicked(imageUrl: String, replyTime: String) {
        val strings: ArrayList<String> = ArrayList()
        strings.add(imageUrl)

        activity?.run {
            val intent = RouteManager.getIntent(this, ApplinkConst.IMAGE_PREVIEW);
            val bundle = Bundle()
            bundle.putStringArrayList(ApplinkConst.Query.IMAGE_PREVIEW_FILELOC, strings)
            bundle.putStringArrayList(ApplinkConst.Query.IMAGE_PREVIEW_IMAGE_DESC, ArrayList())
            bundle.putInt(ApplinkConst.Query.IMAGE_PREVIEW_IMG_POSITION, 0)
            bundle.putString(ApplinkConst.Query.IMAGE_PREVIEW_TITLE, title)
            bundle.putString(ApplinkConst.Query.IMAGE_PREVIEW_SUBTITLE, replyTime)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onProductClicked(element: ProductAttachmentViewModel) {
        val ROLE_SHOP = "shop"

        if (!GlobalConfig.isSellerApp() || senderRole != ROLE_SHOP) {
            activity?.run{

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessGetChat(model: ArrayList<Visitable<*>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun developmentView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onReceiveStartTypingEvent() {
        baseChatViewState.recipientTyping()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        baseChatViewState.addMessage(visitable)
    }

    override fun onReceiveStopTypingEvent() {
        baseChatViewState.recipientStopTyping()
    }

    override fun onReceiveReadEvent() {
        return
    }

}