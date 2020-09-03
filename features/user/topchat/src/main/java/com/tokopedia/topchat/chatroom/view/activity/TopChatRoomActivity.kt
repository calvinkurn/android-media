package com.tokopedia.topchat.chatroom.view.activity

import android.accounts.NetworkErrorException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.MODE_DEFAULT_GET_CHAT
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull
import com.tokopedia.product.manage.common.feature.list.model.ProductViewModel
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.chatroom.view.viewmodel.QuickEditTopChatViewModel
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_base_simple.*
import javax.inject.Inject

class TopChatRoomActivity : BaseChatToolbarActivity(), ProductManageQuickEditStockFragment.OnFinishedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val mViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(QuickEditTopChatViewModel::class.java)
    }

    override fun getScreenName(): String {
        return "/${TopChatAnalytics.Category.CHAT_DETAIL}"
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)

        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        return TopChatRoomFragment.createInstance(bundle)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        useLightNotificationBar()
        initInjector()
        initWindowBackground()
        initTopchatToolbar()
        observeEditStock()
    }

    private fun initInjector() {
        val chatComponent = DaggerChatComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .chatRoomContextModule(ChatRoomContextModule(this))
                .build()
        chatComponent.inject(this)
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(Color.WHITE)
    }

    private fun initTopchatToolbar() {
        supportActionBar?.setBackgroundDrawable(null)
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    override fun setupToolbar() {
        super.setupToolbar()
        decreaseToolbarElevation()
    }

    override fun onFinishEditStock(modifiedProduct: ProductViewModel) {
        //No op. This should not be run if it is from top chat page
    }

    override fun onFinishEditStock(productId: String, productName: String, productStatus: ProductStatus, stock: Int) {
        mViewModel.editStock(userSession.shopId, productId, stock, productName, productStatus)
    }

    private fun decreaseToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val pathSegments = it.pathSegments
            when {
                pathSegments.contains(ApplinkConst.Chat.PATH_ASK_SELLER) -> {
                    val toShopId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    val shopName = it.getQueryParameter(ApplinkConst.Chat.OPPONENT_NAME).toEmptyStringIfNull()
                    val customMessage = it.getQueryParameter(ApplinkConst.Chat.CUSTOM_MESSAGE).toEmptyStringIfNull()
                    val avatar = it.getQueryParameter(ApplinkConst.Chat.AVATAR).toEmptyStringIfNull()
                    val source = it.getQueryParameter(ApplinkConst.Chat.SOURCE) ?: "deeplink"
                    intent.putExtra(ApplinkConst.Chat.SOURCE, source)

                    val model = ChatRoomHeaderViewModel()
                    model.name = shopName
                    model.label = LABEL_SELLER
                    model.senderId = toShopId
                    model.role = ROLE_SELLER
                    model.mode = MODE_DEFAULT_GET_CHAT
                    model.keyword = ""
                    model.image = avatar
                    intent.putExtra(ApplinkConst.Chat.PARAM_HEADER, model)
                    intent.putExtra(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
                    intent.putExtra(RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, "")
                    intent.putExtra(ApplinkConst.Chat.TO_SHOP_ID, toShopId)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, toShopId)
                }
                pathSegments.contains(ApplinkConst.Chat.PATH_ASK_BUYER) -> {
                    val toUserId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    val shopName = it.getQueryParameter(ApplinkConst.Chat.OPPONENT_NAME).toEmptyStringIfNull()
                    val customMessage = it.getQueryParameter(ApplinkConst.Chat.CUSTOM_MESSAGE).toEmptyStringIfNull()
                    val avatar = it.getQueryParameter(ApplinkConst.Chat.AVATAR).toEmptyStringIfNull()
                    val source = it.getQueryParameter(ApplinkConst.Chat.SOURCE) ?: "deeplink"
                    intent.putExtra(ApplinkConst.Chat.SOURCE, source)

                    val model = ChatRoomHeaderViewModel()
                    model.name = shopName
                    model.label = LABEL_USER
                    model.senderId = toUserId
                    model.role = ROLE_USER
                    model.mode = MODE_DEFAULT_GET_CHAT
                    model.keyword = ""
                    model.image = avatar
                    intent.putExtra(ApplinkConst.Chat.PARAM_HEADER, model)
                    intent.putExtra(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
                    intent.putExtra(RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, "")
                    intent.putExtra(ApplinkConst.Chat.TO_USER_ID, toUserId)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, toUserId)
                }
                else -> {
                    val messageId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
                }
            }
        }
    }

    private fun observeEditStock() {
        mViewModel.editStockResult.observe(this, Observer {
            when(it) {
                is Success -> showSuccessStockEditToaster(it.data.productName)
                is Fail -> showErrorStockEditToaster(it.throwable as EditStockResult)
            }
        })
    }

    private fun showSuccessStockEditToaster(productName: String) {
        Toaster.make(parent_view, getString(
                R.string.product_manage_quick_edit_stock_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
    }

    private fun showErrorStockEditToaster(editStockResult: EditStockResult) {
        val message = if(editStockResult.error is NetworkErrorException) {
            getString(editStockResult.error?.message.toIntOrZero())
        } else {
            editStockResult.error?.message
        }
        message?.let {
            val retryMessage = getString(R.string.product_manage_snack_bar_retry)
            showErrorToast(it, retryMessage) {
                mViewModel.editStock(userSession.shopId, editStockResult.productId, editStockResult.stock, editStockResult.productName, editStockResult.status)
            }
        }
    }

    private fun showErrorToast(
            message: String = getString(R.string.product_manage_snack_bar_fail),
            actionLabel: String = getString(com.tokopedia.abstraction.R.string.close),
            listener: () -> Unit = {}
    ) {
        parent_view?.let {
            val onClickActionLabel = View.OnClickListener { listener.invoke() }
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionLabel, onClickActionLabel)
        }
    }

    private fun handleNewIntent(intent: Intent?) {
        intent?.data?.run {
            when {
                pathSegments.contains(ApplinkConst.Chat.PATH_STOCK) -> handleStockQuickEditUri()
                else -> {}
            }
        }
    }

    private fun Uri.handleStockQuickEditUri() {
        getQueryParameter(ApplinkConst.Chat.QUICKEDIT_PRODUCT_ID)?.let { productId ->
            getQueryParameter(ApplinkConst.Chat.QUICKEDIT_PRODUCT_NAME)?.let { productName ->
                getQueryParameter(ApplinkConst.Chat.QUICKEDIT_PRODUCT_STATUS)?.let { productStatus ->
                    getQueryParameter(ApplinkConst.Chat.QUICKEDIT_STOCK)?.toIntOrNull()?.let { productStock ->
                        getQueryParameter(ApplinkConst.Chat.QUICKEDIT_HAS_RESERVED)?.toBoolean()?.let { hasReservedStock ->
                            openStockQuickEdit(productId, productName, productStatus, productStock, hasReservedStock)
                        }
                    }
                }
            }
        }
    }

    private fun openStockQuickEdit(productId: String,
                                   productName: String,
                                   productStatus: String,
                                   productStock: Int,
                                   hasReservedStock: Boolean) {
        if (hasReservedStock) {
            RouteManager.route(this, ApplinkConstInternalMarketplace.RESERVED_STOCK, productId, userSession.shopId)
        } else {
            ProductManageQuickEditStockFragment.createInstance(
                    productId,
                    productName,
                    productStatus,
                    productStock,
                    this).let { bottomSheet ->
                bottomSheet.show(supportFragmentManager, QUICKEDIT_STOCK_TAG)
            }
        }
    }

    companion object {
        val REQUEST_CODE_CHAT_IMAGE = 2325
        val LABEL_USER = "Pengguna"
        val LABEL_SELLER = "Penjual"
        val ROLE_SELLER = "shop"
        val ROLE_USER = "user"

        private const val QUICKEDIT_STOCK_TAG = "quickedit_stock"
    }

}
