package com.tokopedia.groupchat.room.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.webview.TkpdWebView
import com.tokopedia.abstraction.base.view.webview.TkpdWebViewClient
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.groupchat.room.di.PlayComponent
import com.tokopedia.groupchat.room.di.DaggerPlayComponent

import com.tokopedia.groupchat.GroupChatModuleRouter
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 12/02/19.
 */
class PlayWebviewFragment : BaseDaggerFragment(), View.OnKeyListener {

    private var url: String = ""
    private var hasTitlebar: Boolean = false
    private var gcToken: String = ""
    private var doubleTapExit = false
    private val EXIT_DELAY_MILLIS = 2000
    private val REQUEST_CODE_LOGIN = 123
    private val PARAM_HEADER_GC_TOKEN: String = "gc-token"

    //Chrome Client
    val ATTACH_FILE_REQUEST = 1
    private var callbackBeforeL: ValueCallback<Uri>? = null
    var callbackAfterL: ValueCallback<Array<Uri>>? = null

    lateinit var webview: TkpdWebView
    lateinit var webviewClient: TkpdWebViewClient
    lateinit var progressBar: ProgressBar

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        fun createInstance(bundle: Bundle): PlayWebviewFragment {
            val fragment = PlayWebviewFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun createInstance(url: String): PlayWebviewFragment {
            val fragment = PlayWebviewFragment()
            val bundle = Bundle()
            bundle.putBoolean(ApplinkConst.Play.PARAM_HAS_TITLEBAR, false)
            bundle.putString(ApplinkConst.Play.PARAM_URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
       return GroupChatAnalytics.SCREEN_PLAY_WEBVIEW_FULL
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val playComponent = DaggerPlayComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()
            playComponent.inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(
                R.layout.fragment_webview, container, false
        )
        initWebview(view)
        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebview(view: View) {
        CookieManager.getInstance().setAcceptCookie(true)
        webview = view.findViewById(R.id.webview)
        progressBar = view.findViewById(R.id.progress_bar)
        progressBar.isIndeterminate = true
        webview.setOnKeyListener(this)
        webview.settings.javaScriptEnabled = true
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.settings.domStorageEnabled = true
        webview.webViewClient = getWebviewClient()
        webview.webChromeClient = getWebviewChromeClient()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run{
            if (!hasTitlebar
                    && this is AppCompatActivity) {
                (activity as AppCompatActivity).supportActionBar?.hide()
            }
        }

        loadWebview()
    }

    private fun loadWebview() {
        webview.loadAuthUrl(url, userSession.userId, userSession.accessToken, getHeaderPlay())
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        activity?.run {
            hasTitlebar = getParamBoolean(ApplinkConst.Play.PARAM_HAS_TITLEBAR, arguments,
                    savedInstanceState, true)

            url = getParamString(ApplinkConst.Play.PARAM_URL, arguments,
                    savedInstanceState, "")

            if (url.isBlank())
                finish()

            gcToken = getGCToken()
        }
    }

    private fun getGCToken(): String {
        activity?.run{
            val LOGIN_SESSION = "LOGIN_SESSION"
            val KEY_GC_TOKEN = "gc_token"

            val sharedPrefs = applicationContext.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE)
            return sharedPrefs.getString(KEY_GC_TOKEN, "")
        }

        return ""
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (::webview.isInitialized && event?.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> if (webview.canGoBack()) {
                    webview.goBack()
                    return true
                } else if (!hasTitlebar) {
                    doubleTapExit()
                    return true
                }
            }
        }
        return false
    }

    private fun doubleTapExit() {
        activity?.run {
            if (doubleTapExit) {
                finish()
            } else {
                doubleTapExit = true
                val exitMessage = "Tekan sekali lagi untuk keluar"
                Toast.makeText(this, exitMessage, Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ doubleTapExit = false }, EXIT_DELAY_MILLIS.toLong())
            }
        }

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

//            override fun onReceivedTitle(view: WebView, title: String) {
//                super.onReceivedTitle(view, title)
//                if (activity is AppCompatActivity && (activity as AppCompatActivity).supportActionBar != null) {
//                    val decodedUrl = Uri.decode(url).toLowerCase()
//
//                    if (!TextUtils.isEmpty(title)
//                            && Uri.parse(title).scheme == null
//                            && isKolUrl(decodedUrl)) {
//                        (activity as AppCompatActivity).supportActionBar!!.setTitle(
//                                title
//                        )
//                    } else {
//                        (activity as AppCompatActivity).supportActionBar!!.setTitle(
//                                getString(R.string.title_activity_deep_link)
//                        )
//                    }
//                }
//            }
        }
    }

    private fun getWebviewClient(): WebViewClient? {
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val PARAM_WEBVIEW_BACK = "tokopedia://back"

                activity?.run {
                    if (url.equals(PARAM_WEBVIEW_BACK, ignoreCase = true)
                            && !isTaskRoot) run {
                        finish()
                        return true
                    } else if (url.equals(PARAM_WEBVIEW_BACK, ignoreCase = true)
                            && isTaskRoot) run {
                        openHomePage()
                        return true
                    } else if (url.equals(ApplinkConst.LOGIN)) {
                        val intent = RouteManager.getIntent(this, url)
                        startActivityForResult(intent, REQUEST_CODE_LOGIN)
                        return true
                    } else if (RouteManager.isSupportApplink(this, url)) {
                        RouteManager.route(this, url)
                        return true
                    } else {
                        return false
                    }
                }
                return false
            }
        }
    }

    private fun openHomePage() {
        activity?.run {
            startActivity((applicationContext as GroupChatModuleRouter).getHomeIntent(this))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       if(requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK){
           loadWebview()
       }else {
           super.onActivityResult(requestCode, resultCode, data)
       }
    }

    private fun getHeaderPlay(): HashMap<String, String> {
        val header = HashMap<String,String>()
        header[PARAM_HEADER_GC_TOKEN] = gcToken
        return header
    }

}