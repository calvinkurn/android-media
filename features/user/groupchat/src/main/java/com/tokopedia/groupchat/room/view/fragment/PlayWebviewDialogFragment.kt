package com.tokopedia.groupchat.room.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.view.webview.TkpdWebView
import com.tokopedia.abstraction.base.view.webview.TkpdWebViewClient
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.common.data.GroupChatUrl
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_webview.*

/**
 * @author by nisie on 18/02/19.
 */
class PlayWebviewDialogFragment : BottomSheetDialogFragment(), View.OnKeyListener {

    private var url: String = ""
    private var isBottomSheetCloseable: Boolean = true

    private var callbackBeforeL: ValueCallback<Uri>? = null
    private var callbackAfterL: ValueCallback<Array<Uri>>? = null
    private lateinit var behavior: BottomSheetBehavior<FrameLayout?>

    lateinit var webview: TkpdWebView
    lateinit var progressBar: ProgressBar
    private val userSession: UserSessionInterface by lazy {
        UserSession(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
        setupViewModel(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val webViewDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        webViewDialog.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            this.behavior = behavior

            behavior.peekHeight = FIRST_STATE_HEIGHT

            activity?.windowManager?.defaultDisplay?.let {
                val displayMetrics = DisplayMetrics()
                it.getMetrics(displayMetrics)
                behavior.peekHeight = displayMetrics.heightPixels * 9 / 16
            }

            isBottomSheetCloseable = true

            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val newHeight = (behavior.peekHeight + (bottomSheet.height - behavior.peekHeight) * slideOffset)
                    setWebviewContentHeight(newHeight.toInt())
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (!isBottomSheetCloseable && newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }

                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetDialog.cancel()
                    }
                }
            })
        }

        return webViewDialog
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run {
            if (url.isEmpty()) {
                url = getParamString(ApplinkConst.Play.PARAM_URL, arguments, savedInstanceState, "")
            } else if (url.isBlank()) {
                finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_webview, container, false)
        initWebView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWebView()

        ImageHandler.LoadImage(errorImage, GroupChatUrl.ERROR_WEBVIEW_IMAGE_URL)
        retryButton.setOnClickListener {
            webview.show()
            errorView.hide()
            loadWebView()
        }
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun loadWebView() {
        if(shouldRedirectToSeamless(url)){
            url = URLGenerator.generateURLSessionLogin(url, userSession.deviceId, userSession.userId)
        }
        webview.loadAuthUrl(url, userSession.userId, userSession.accessToken, getHeaderPlay())
    }

    private fun shouldRedirectToSeamless(url: String): Boolean {
        return !url.contains("tokopedia.com/play", true)
                && !url.contains("js.tokopedia.com/seamless", true)
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun setWebviewContentHeight(height: Int) {
        val resizableLayoutParams = webview.layoutParams
        resizableLayoutParams.height = height
        webview.layoutParams = resizableLayoutParams
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(view: View) {
        CookieManager.getInstance().setAcceptCookie(true)

        webview = view.findViewById(R.id.webview)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.isIndeterminate = true
        webview.setOnKeyListener(this)
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.settings.domStorageEnabled = true
        webview.settings.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webview.settings.mediaPlaybackRequiresUserGesture = false
        }
        webview.webViewClient = getWebViewClient()
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
        if (event?.action == KeyEvent.ACTION_DOWN) {
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
                    progressBar?.visibility = View.GONE
                }
                super.onProgressChanged(view, newProgress)
            }
        }
    }

    private fun getWebViewClient(): TkpdWebViewClient {
        return object : TkpdWebViewClient() {

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                var showError = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GlobalConfig.isAllowDebuggingTools()) {
                    var text= error?.errorCode.toString() +" "+ error?.description
                    errorView.findViewById<TextView>(R.id.error_subtitle).text = text
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && error?.errorCode == WebViewClient.ERROR_CONNECT) {
                    showError = false
                }
                if(showError) {
                    webview.hide()
                    errorView.show()
                }
            }

            override fun onOverrideUrl(url: Uri?): Boolean {
                if (url == null) return true
                val requestUrl = url.toString()
                return onOverrideUrl(requestUrl)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                setWebviewContentHeight(behavior.peekHeight)
                super.onPageFinished(view, url)
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
            startActivity(RouteManager.getIntent(this, ApplinkConst.HOME))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            loadWebView()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getHeaderPlay(): HashMap<String, String> {
        val header = HashMap<String, String>()
        header[PARAM_HEADER_GC_TOKEN] = userSession.gcToken
        return header
    }


    companion object {
        private const val REQUEST_CODE_LOGIN = 123
        private const val PARAM_HEADER_GC_TOKEN: String = "X-User-Token"
        private const val FIRST_STATE_HEIGHT = 600

        //Chrome Client
        private const val ATTACH_FILE_REQUEST = 1

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

}
