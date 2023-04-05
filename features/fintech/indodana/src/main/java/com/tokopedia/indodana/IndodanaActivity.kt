package com.tokopedia.indodana

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Pair
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.indodana.whitelabelsdk.camera.CameraController
import com.indodana.whitelabelsdk.camera.WhitelabelCameraView
import com.indodana.whitelabelsdk.webview.jsinterface.WhitelabelCameraJsInterface
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class IndodanaActivity : AppCompatActivity() {

    private lateinit var webview: WebView
    private lateinit var cameraview: WhitelabelCameraView
    private var cameraController: CameraController? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indodana)

        webview = findViewById(R.id.standardWebview)
        cameraview = findViewById(R.id.whitelabelCameraView)

        val webSettings: WebSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return false
            }
        }

        val url = intent.data?.getQueryParameter("url")
            ?: "https://sandbox01.indodana.com/borrower/credit-limit/apply/sign-in"

        webview.loadUrl(url)

        webview.addJavascriptInterface(
            WhitelabelCameraJsInterface(
            WhitelabelCameraJsInterface.OpenCamera { docType: String?, lang: String?, _: String? ->
                this.takePictureFromCamera(docType, lang)
            }), "CameraPicker"
        )

        requestPermission()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS
                    ),
                    0
                )
            }
        }
    }

    //Handler to display camera view
    private fun takePictureFromCamera(docType: String?, lang: String?) {
        runOnUiThread {
            if (cameraController == null) {
                cameraController = CameraController(
                    this,
                    this,
                    getRealScreenSize(),
                    cameraview,
                    docType
                ) { docsTyp, imageBase64 ->
                    finishTakePicture(docsTyp, imageBase64)
                }
            }
            cameraview.initialize(docType, lang, "", {
                cameraController?.takePicture()
            }, { toggled: Boolean ->
                cameraController?.flashToggled(toggled)
            }, {
                dismissTakeImageView()
            })
            cameraview.visibility = View.VISIBLE
            cameraController?.startTakingPicture()
        }
    }

    //Handler to inject image back to webview
    private fun finishTakePicture(docType: String?, imageBase64: String?) {
        if (imageBase64 != null && docType != null) {
            val script = String.format(
                "var event = new CustomEvent('cameraTriggered'," +
                    "{ detail: {document: '%s', image: 'data:image/jpeg;base64,%s'}});" +
                    "window.dispatchEvent(event);",
                docType, imageBase64
            )
            executeJs(script)
            dismissTakeImageView()
        }
    }

    private fun executeJs(script: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.evaluateJavascript(
                script
            ) { value: String -> Log.i("executeJS", "result: $value") }
        } else {
            try {
                webview.loadUrl("javascript:" + URLEncoder.encode(script, "UTF-8"))
            } catch (ex: UnsupportedEncodingException) {
                ex.printStackTrace()
            }
        }
    }

    //set the cameraview to invisible after usage
    private fun dismissTakeImageView() {
        cameraController?.onStop()
        cameraview.visibility = View.INVISIBLE
        cameraview.onStop()
        cameraController = null
    }

    //used to calculate image cropping
    private fun getRealScreenSize(): Pair<Int, Int>? {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val realWidth = displayMetrics.widthPixels
        val realHeight = displayMetrics.heightPixels
        return Pair(realWidth, realHeight)
    }

    /****Additional Code to enable camera SDK*****/
    override fun onResume() {
        super.onResume()
        webview.onResume()
        cameraController?.startTakingPicture()
    }

    override fun onPause() {
        webview.onPause()
        cameraController?.onPause()
        super.onPause()
    }
}
