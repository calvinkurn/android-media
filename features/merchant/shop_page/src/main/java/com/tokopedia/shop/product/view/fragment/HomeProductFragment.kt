package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils
import com.tokopedia.shop.product.view.datamodel.ShopProductPromoUiModel
import com.tokopedia.shop.product.view.widget.NestedWebView
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_shop_product_limited_promo.*
import timber.log.Timber
import javax.inject.Inject

class HomeProductFragment : BaseDaggerFragment() {


    lateinit var shopPageNestedWebView: NestedWebView
    @Inject
    lateinit var userSession: UserSessionInterface

    private var isLogin: Boolean = false
    private var isBind: Boolean = false
    private var urlNeedTobBeProceed: String? = null
    lateinit var layoutLoading: View
    private var shopId: String = ""

    private var shopProductPromoUiModel: ShopProductPromoUiModel = ShopProductPromoUiModel()

    companion object {
        private const val MIN_SHOW_WEB_VIEW_PROGRESS = 80
        private const val REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101
        private const val SHOP_STATIC_URL = "shop-static"
        private const val SHOP_ID = "SHOP_ID"
        private const val SHOP_TOP_CONTENT_URL = "SHOP_TOP_CONTENT_URL"
        fun createInstance(shopId: String, topContentWebViewUrl: String) : Fragment {

            return HomeProductFragment().apply {
                val bundle = Bundle()
                bundle.putString(SHOP_ID, shopId)
                bundle.putString(SHOP_TOP_CONTENT_URL, topContentWebViewUrl)
                arguments = bundle
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerShopProductComponent
                    .builder()
                    .shopProductModule(ShopProductModule())
                    .shopComponent(ShopComponentHelper().getComponent(it.application, it))
                    .build()
                    .inject(this)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_shop_product_limited_promo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopId = arguments?.getString(SHOP_ID, "") ?: ""
        findViews()
        layoutLoading = view.findViewById(R.id.layout_loading)
        shopProductPromoUiModel = getHomeData(getOfficialWebViewUrl())
        if (isBind && isLogin == shopProductPromoUiModel.isLogin) {
            return
        }
        clearCache(shopPageNestedWebView)
        if (shopProductPromoUiModel.isLogin) {
            shopPageNestedWebView.loadAuthUrl(shopProductPromoUiModel.url.orEmpty(), userSession)
        } else {
            shopPageNestedWebView.loadUrl(shopProductPromoUiModel.url)
        }

        isLogin = shopProductPromoUiModel.isLogin
        isBind = true
    }

    override fun onDestroy() {
        isBind = false
        super.onDestroy()
    }

    private fun clearCache(webView: WebView?) {
        webView?.clearCache(true)
    }

    private fun getHomeData(contentUrl: String): ShopProductPromoUiModel {
        if (contentUrl.isNotBlank()) {
            val url = if (userSession.isLoggedIn) {
                ShopProductOfficialStoreUtils.getLogInUrl(contentUrl, userSession.deviceId, userSession.userId)
            } else contentUrl

            return ShopProductPromoUiModel(url, userSession.userId, userSession.accessToken, userSession.isLoggedIn)
        }
        return ShopProductPromoUiModel()
    }

    private fun getOfficialWebViewUrl(): String {
        var officialWebViewUrl = arguments?.getString(SHOP_TOP_CONTENT_URL, "") ?: ""
        officialWebViewUrl = if (TextApiUtils.isTextEmpty(officialWebViewUrl)) "" else officialWebViewUrl
        return officialWebViewUrl
    }

    private fun findViews() {
        shopPageNestedWebView = web_view
        shopPageNestedWebView.webViewClient = OfficialStoreWebViewClient()
        shopPageNestedWebView.webChromeClient = OfficialStoreWebChromeClient()

        val webSettings = shopPageNestedWebView.settings
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
        webSettings.builtInZoomControls = false
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        optimizeWebView()
        CookieManager.getInstance().setAcceptCookie(true)
    }

    private fun optimizeWebView() {
        shopPageNestedWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    fun promoClicked(url: String?) {
        activity?.let {
            val urlProceed = ShopProductOfficialStoreUtils.proceedUrl(it, url.orEmpty(), shopId,
                    userSession.isLoggedIn,
                    userSession.deviceId,
                    userSession.userId)
            // Need to login
            if (!urlProceed) {
                urlNeedTobBeProceed = url
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW)
            }
        }
    }

    private inner class OfficialStoreWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (newProgress >= MIN_SHOW_WEB_VIEW_PROGRESS) {
                view.visibility = View.VISIBLE
                finishLoading()
                stopPerformanceMonitoring()
            }
            super.onProgressChanged(view, newProgress)
        }

    }

    private fun stopPerformanceMonitoring(){
        (activity as? ShopPageActivity)?.stopShopHomeWebViewTabPerformanceMonitoring()
    }

    private inner class OfficialStoreWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLoading()
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            handler.cancel()
            finishLoading()
            stopPerformanceMonitoring()
        }

        override fun onLoadResource(view: WebView, url: String) {
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            finishLoading()
            ServerLogger.log(Priority.P1, "WEBVIEW_ERROR", mapOf("type" to failingUrl,
                    "error_code" to errorCode.toString(), "desc" to description))
            stopPerformanceMonitoring()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val uri = Uri.parse(url)

            uri.also {
                if (url.contains(SHOP_STATIC_URL)) {
                    view.loadUrl(url)
                } else if (uri.scheme == ShopProductOfficialStoreUtils.TOKOPEDIA_HOST || it.scheme?.startsWith(ShopProductOfficialStoreUtils.HTTP) == true) {
                    promoClicked(url)
                }
            }

            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW -> if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(urlNeedTobBeProceed)) {
                promoClicked(urlNeedTobBeProceed)
            }
        }
    }

    private fun showLoading() {
        layoutLoading.visibility = View.VISIBLE
        shopPageNestedWebView.visibility = View.GONE
    }

    private fun finishLoading() {
        layoutLoading.visibility = View.GONE
        shopPageNestedWebView.visibility = View.VISIBLE
    }


}