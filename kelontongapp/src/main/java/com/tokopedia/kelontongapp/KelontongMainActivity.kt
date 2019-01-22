package com.tokopedia.kelontongapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import com.appsflyer.AppsFlyerLib
import com.tokopedia.kelontongapp.firebase.Preference
import com.tokopedia.kelontongapp.helper.ConnectionManager
import com.tokopedia.kelontongapp.webview.FilePickerInterface
import com.tokopedia.kelontongapp.webview.KelontongWebChromeClient
import com.tokopedia.kelontongapp.webview.KelontongWebview
import com.tokopedia.kelontongapp.webview.KelontongWebviewClient
import java.util.*

/**
 * Created by meta on 02/10/18.
 */
class KelontongMainActivity : AppCompatActivity(), FilePickerInterface {

    private var BASE_URL = KelontongBaseUrl.BASE_URL

    private lateinit var webViewChromeClient: KelontongWebChromeClient
    private lateinit var webviewClient: KelontongWebviewClient
    private lateinit var webView: KelontongWebview
    private lateinit var progressBar: ProgressBar

    private var doubleTapExit = false

    private val headers = HashMap<String, String>()

    private val isHome: Boolean
        get() = webView.url.equals(BASE_URL, ignoreCase = true)

    private val isMitraUrl: Boolean
        get() = webView.url.contains(KelontongBaseUrl.TOKOPEDIA_URL, ignoreCase = true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showAlertDialog()

        loadWebViewPage()
    }

    private fun loadWebViewPage() {
        if (ConnectionManager.isNetworkConnected(this)) {
            setContentView(R.layout.activity_main_kelontong)

            initializeWebview()
        } else {
            noInternetConnection()
        }
    }

    private fun noInternetConnection() {
        setContentView(R.layout.activity_no_internet)
        findViewById<TextView>(R.id.tv_msg_medium).visibility = View.VISIBLE
        val btnTryAgain = findViewById<Button>(R.id.btn_retry)
        btnTryAgain.setOnClickListener { loadWebViewPage() }
    }

    private fun initializeWebview() {
        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progressbar)

        webViewChromeClient = KelontongWebChromeClient(this, this)
        webviewClient = KelontongWebviewClient(this)

        headers[X_REQUESTED_WITH] = ""
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webviewClient.setProgressInterface(object: KelontongWebviewClient.ProgressInterface {
            override fun onPageVisible() {
                progressBar.visibility = View.GONE
            }
        })

        webViewChromeClient.setWebviewListener(object : KelontongWebChromeClient.WebviewListener {
            override fun onComplete() {
                progressBar.visibility = View.GONE
                if (Preference.isFirstTime(this@KelontongMainActivity)) {
                    Preference.saveFirstTime(this@KelontongMainActivity)
                }
            }
        })

        webView.webChromeClient = webViewChromeClient
        webView.webViewClient = webviewClient
        webView.settings.allowFileAccess = true
        webView.settings.pluginState = WebSettings.PluginState.ON

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = 0
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }

        var userAgent = System.getProperty("http.agent")
        userAgent += String.format(" %s-%s %s", ANDROID, BuildConfig.VERSION_NAME, MOBILE)
        webView.settings.userAgentString = userAgent

        val fcmToken = Preference.getFcmToken(this)
        val cookieGcmId = "$GCM_ID=$fcmToken"
        val cookieAfId = "$AF_ID=${AppsFlyerLib.getInstance().getAppsFlyerUID(this)}"

        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setCookie(KelontongBaseUrl.COOKIE_URL, cookieGcmId)
        cookieManager.setCookie(KelontongBaseUrl.COOKIE_URL, cookieAfId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        } else {
            CookieManager.getInstance().setAcceptCookie(true)
        }

        if (Preference.isFirstTime(this)) {
            requestPermission()
        }

        if (BuildConfig.DEBUG) {
            debugModeAlert()
        } else {
            loadHome()
        }
    }

    private fun loadHome() {
        webView.loadUrl(BASE_URL, headers)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!webviewClient.checkPermission()) {
                webviewClient.requestPermission()
            }
        }
    }

    private fun showAlertDialog() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
            builder.setTitle(R.string.dialog_info)
            builder.setMessage(R.string.dialog_msg)
            builder.setPositiveButton(R.string.dialog_yes, null)
            builder.setNegativeButton(R.string.dialog_no) { _, _ ->
                val url = BASE_URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
            builder.show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (isHome) {
                        onBackPressed()
                    } else if (webView.canGoBack()) {
                        if (!isMitraUrl) {
                            webView.clearHistory()
                            loadHome()
                        } else {
                            webView.goBack()
                        }
                    }
                    return true
                }
            }

        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            webViewChromeClient.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            webviewClient.onRequestPermissionsResult(requestCode, permissions, grantResults)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KelontongWebChromeClient.ATTACH_FILE_REQUEST && webViewChromeClient != null) {
            webViewChromeClient.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        doubleTapExit()
    }

    private fun doubleTapExit() {
        if (doubleTapExit) {
            this.finish()
        } else {
            doubleTapExit = true
            Toast.makeText(this, R.string.msg_exit, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleTapExit = false }, EXIT_DELAY_MILLIS.toLong())
        }
    }

    private fun debugModeAlert() {
        val alert =  AlertDialog.Builder(this)
        val edittext = EditText(this)
        edittext.setText(KelontongBaseUrl.BASE_URL)
        edittext.maxLines = 1

        val layout = FrameLayout(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layout.setPaddingRelative(45,15,45,0)
        }
        alert.setTitle(title)
        layout.addView(edittext)
        alert.setView(layout)
        alert.setPositiveButton(getString(R.string.dialog_yes)) {
            _, _ ->
            run {
                val url = edittext.text.toString()
                BASE_URL = url
            }
        }
        alert.setNegativeButton(getString(R.string.dialog_no)) {
            dialog, _ ->
            run {
                dialog.dismiss()
            }
        }

        alert.show()
    }

    companion object {

        fun start(context: Context): Intent {
            return Intent(context, KelontongMainActivity::class.java)
        }
    }
}
