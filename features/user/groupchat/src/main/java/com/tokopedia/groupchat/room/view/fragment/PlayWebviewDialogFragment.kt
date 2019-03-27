package com.tokopedia.groupchat.room.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import com.tokopedia.abstraction.base.view.webview.TkpdWebView
import com.tokopedia.abstraction.base.view.webview.TkpdWebViewClient
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.groupchat.GroupChatModuleRouter
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.common.data.GroupChatUrl
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 18/02/19.
 */
class PlayWebviewDialogFragment : BottomSheetDialogFragment(), View.OnKeyListener {

    private var url: String = ""
    private var gcToken: String = ""
    private val REQUEST_CODE_LOGIN = 123
    private val PARAM_HEADER_GC_TOKEN: String = "X-User-Token"
    private var isBottomSheetCloseable: Boolean = true

    //Chrome Client
    val ATTACH_FILE_REQUEST = 1
    private var callbackBeforeL: ValueCallback<Uri>? = null
    var callbackAfterL: ValueCallback<Array<Uri>>? = null

    lateinit var webview: TkpdWebView
    lateinit var progressBar: ProgressBar
    lateinit var userSession: UserSessionInterface
    lateinit var errorView: View
    lateinit var errorImage: ImageView
    lateinit var retryButton: View
    private var behavior: BottomSheetBehavior<FrameLayout?>? = null

    lateinit var closeButton: View
    companion object {

        fun createInstance(bundle: Bundle): PlayWebviewDialogFragment {
            val fragment = PlayWebviewDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
        fun createInstance(url: String): PlayWebviewDialogFragment {
            val fragment = PlayWebviewDialogFragment()
            val bundle = Bundle()
            bundle.putBoolean(ApplinkConst.Play.PARAM_HAS_TITLEBAR, false)
            bundle.putString(ApplinkConst.Play.PARAM_URL, url)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
        //TODO use dagger?
        userSession = UserSession(context)
        setupViewModel(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val temp = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        temp.setOnShowListener { dialog ->
            var finalDialog = dialog as BottomSheetDialog
            val bottomSheet = finalDialog.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            var behavior = BottomSheetBehavior.from(bottomSheet)
            this.behavior = behavior
            behavior.peekHeight = 600
            activity?.windowManager?.defaultDisplay?.let {
                var displayMetrics = DisplayMetrics()
                it.getMetrics(displayMetrics)
                behavior.peekHeight = displayMetrics.heightPixels * 9 / 16
            }

            isBottomSheetCloseable = true
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (!isBottomSheetCloseable && newState == BottomSheetBehavior
                                    .STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED;
                    }

                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        finalDialog.cancel()
                    }
                }
            })
        }

        return temp
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run {

            if (url.isNullOrEmpty()) {
                url = getParamString(ApplinkConst.Play.PARAM_URL, arguments,
                        savedInstanceState, "")
            }
            if (url.isBlank())
                finish()

            gcToken = userSession.gcToken
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_webview, container, false)
        errorView = view.findViewById(R.id.error_layout)
        errorImage = view.findViewById(R.id.error_image)
        retryButton = view.findViewById(R.id.retry_button)
        closeButton = view.findViewById(R.id.header)
        initWebview(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWebview()
        ImageHandler.LoadImage(errorImage, GroupChatUrl.ERROR_WEBVIEW_IMAGE_URL)
        retryButton.setOnClickListener {
            webview.show()
            errorView.hide()
            loadWebview()
        }
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun loadWebview() {
        webview.loadAuthUrl(url, userSession.userId, userSession.accessToken, getHeaderPlay())
    }

    fun setUrl(url: String) {
        this.url = url
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebview(view: View) {
        CookieManager.getInstance().setAcceptCookie(true)
        if(GlobalConfig.isAllowDebuggingTools())
            WebView.setWebContentsDebuggingEnabled(true)

        webview = view.findViewById(R.id.webview)
        progressBar = view.findViewById(R.id.progress_bar)
        progressBar.isIndeterminate = true
        webview.setOnKeyListener(this)
//        webview.addJavascriptInterface(WebViewResizer(), "WebViewResizer")
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.settings.domStorageEnabled = true
        webview.settings.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webview.settings.mediaPlaybackRequiresUserGesture = false
        }
        webview.webViewClient = getWebviewClient()
        webview.webChromeClient = getWebviewChromeClient()
        webview.setWebviewScrollListener(object : TkpdWebView.WebviewScrollListener {
            override fun onTopReached() {
                isBottomSheetCloseable = true

            }

            override fun onEndReached() {
                isBottomSheetCloseable = false

            }

            override fun onHasScrolled() {
                isBottomSheetCloseable = false
            }
        })

    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (::webview.isInitialized && event?.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> if (webview.canGoBack()) {
                    webview.goBack()
                    return true
                }
            }
        }
        return false
    }

    private fun getWebviewChromeClient(): WebChromeClient? {
        return object : WebChromeClient() {

            //For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                callbackBeforeL = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST)
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                callbackBeforeL = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"), ATTACH_FILE_REQUEST)
            }

            //For Android 4.1+
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
                callbackBeforeL = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST)
            }

            override fun onShowFileChooser(webView: WebView?,
                                           filePathCallback: ValueCallback<Array<Uri>>?,
                                           fileChooserParams: FileChooserParams?): Boolean {
                if (callbackAfterL != null) {
                    callbackAfterL!!.onReceiveValue(null)
                }
                callbackAfterL = filePathCallback

                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "*/*"
                val intentArray = arrayOfNulls<Intent>(0)

                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, ATTACH_FILE_REQUEST)
                return true
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                //  progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
                super.onProgressChanged(view, newProgress)
            }

        }
    }

    private fun getWebviewClient(): TkpdWebViewClient {
        return object : TkpdWebViewClient() {

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)

                webview.hide()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var text= error?.errorCode.toString() +" "+ error?.description
                    errorView.findViewById<TextView>(R.id.error_subtitle).text = text
                }
                errorView.show()

            }

            override fun onOverrideUrl(url: Uri?): Boolean {
                if (url == null) return true
                val requestUrl = url.toString()
                return onOverrideUrl(requestUrl)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
//                webview.loadUrl("javascript:window.WebViewResizer.processHeight(document.querySelector('body').offsetHeight);")
                super.onPageFinished(view, url)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                Toast.makeText(view?.context, "ssl "+ error.toString(), Toast.LENGTH_LONG).show()
                super.onReceivedSslError(view, handler, error)
            }
        }
    }

    private fun onOverrideUrl(requestUrl: String): Boolean {
        val webview_back = "tokopedia://back"

        activity?.run {
            if (requestUrl.equals(webview_back, true)
                    && !isTaskRoot) run {
                finish()
                return true
            } else if (requestUrl.equals(webview_back, true)
                    && isTaskRoot) run {
                openHomePage()
                return true
            } else if (requestUrl.equals(ApplinkConst.LOGIN, true)) {
                val intent = RouteManager.getIntent(this, requestUrl)
                startActivityForResult(intent, REQUEST_CODE_LOGIN)
                return true
            } else if (RouteManager.isSupportApplink(this, requestUrl)) {
                RouteManager.route(this, requestUrl)
                return true
            } else {
                return false
            }
        }
        return false
    }

    private fun openHomePage() {
        activity?.run {
            startActivity((applicationContext as GroupChatModuleRouter).getHomeIntent(this))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            loadWebview()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getHeaderPlay(): HashMap<String, String> {
        val header = HashMap<String, String>()
        header[PARAM_HEADER_GC_TOKEN] = gcToken
        return header
    }


    private inner class WebViewResizer {
        @JavascriptInterface
        fun processHeight(height: Float) {
            var webViewHeight = (height * resources.displayMetrics.density)
            var marginTop = (10 * resources.displayMetrics.density)
        }
    }
}
