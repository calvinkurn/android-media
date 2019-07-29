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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.shop.R
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel
import com.tokopedia.shop.product.view.widget.NestedWebView
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_shop_product_limited_promo.*
import javax.inject.Inject

class HomeProductFragment : BaseDaggerFragment() {


    lateinit var shopPageNestedWebView: NestedWebView
    @Inject
    lateinit var userSession: UserSessionInterface

    private var shopInfo: ShopInfo? = null

    private var isLogin: Boolean = false
    private var isBind: Boolean = false
    private var urlNeedTobBeProceed: String? = null
    lateinit var layoutLoading: View


    private var shopProductPromoViewModel: ShopProductPromoViewModel = ShopProductPromoViewModel()

    companion object {
        private const val MIN_SHOW_WEB_VIEW_PROGRESS = 100
        private const val REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101
        private const val SHOP_STATIC_URL = "shop-static"
        fun createInstance() = HomeProductFragment()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(ShopProductModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_shop_product_limited_promo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews()
        layoutLoading = view.findViewById(R.id.layout_loading)
        shopProductPromoViewModel = getHomeData(getOfficialWebViewUrl(shopInfo))
        if (isBind && isLogin == shopProductPromoViewModel.isLogin) {
            return
        }
        clearCache(shopPageNestedWebView)
        if (shopProductPromoViewModel.isLogin) {
            shopPageNestedWebView.loadAuthUrl(shopProductPromoViewModel.url,
                    shopProductPromoViewModel.userId,
                    shopProductPromoViewModel.accessToken)
        } else {
            shopPageNestedWebView.loadUrl(shopProductPromoViewModel.url)
        }

        isLogin = shopProductPromoViewModel.isLogin
        isBind = true
    }

    private fun clearCache(webView: WebView?) {
        webView?.clearCache(true)
    }

    fun setShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
    }

    fun getHomeData(contentUrl: String): ShopProductPromoViewModel {
        if (contentUrl.isNotBlank()) {
            val url = if (userSession.isLoggedIn) {
                ShopProductOfficialStoreUtils.getLogInUrl(contentUrl, userSession.deviceId, userSession.userId)
            } else contentUrl

            return ShopProductPromoViewModel(url, userSession.userId, userSession.accessToken, userSession.isLoggedIn)
        }
        return ShopProductPromoViewModel()
    }

    private fun getOfficialWebViewUrl(shopInfo: ShopInfo?): String {
        if (shopInfo == null) {
            return ""
        }
        var officialWebViewUrl = shopInfo.topContent.topUrl
        officialWebViewUrl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) officialWebViewUrl else ""
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            shopPageNestedWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            shopPageNestedWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    fun promoClicked(url: String?) {
        activity?.let {
            val urlProceed = ShopProductOfficialStoreUtils.proceedUrl(it, url, shopInfo?.shopCore?.shopID,
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
            }
            super.onProgressChanged(view, newProgress)
        }
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
        }

        override fun onLoadResource(view: WebView, url: String) {
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            finishLoading()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val uri = Uri.parse(url)

            uri.also {
                if (url.contains(SHOP_STATIC_URL)) {
                    view.loadUrl(url)
                } else if (uri.scheme == ShopProductOfficialStoreUtils.TOKOPEDIA_HOST || it.scheme.startsWith(ShopProductOfficialStoreUtils.HTTP)) {
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