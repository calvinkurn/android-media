package com.tokopedia.indodana

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.indodana.whitelabelsdk.camera.CameraController
import com.indodana.whitelabelsdk.camera.WhitelabelCameraView
import com.indodana.whitelabelsdk.webview.jsinterface.WhitelabelCameraJsInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity
import com.tokopedia.homecredit.view.fragment.HomeCreditKTPFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
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
            ?: "https://csb-ioo7i.netlify.app/"
//            ?: "https://sandbox01.indodana.com/borrower/credit-limit/apply/sign-in?campaignid=%7Btokopedia%7C%7Ccustomized%7D&formVersion=V4&network=tokopedia&phoneNumber=%7BphoneNumber%7D&uiVersion=V2&utm_campaign=%7Btokopedia%7C%7Ccustomized%7D&utm_medium=webform&utm_source=tokopedia"

        webview.loadUrl(url)

        webview.addJavascriptInterface(
            WhitelabelCameraJsInterface(
                WhitelabelCameraJsInterface.OpenCamera { docType: String?, lang: String?, _: String? ->
                    this.takePictureFromCamera(docType, lang)
                }
            ),
            "CameraPicker"
        )

//        webview.addJavascriptInterface(
//            WebAppInterface { this.takePictureFromCamera("", "") },
//            "CameraPicker"
//        )

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

    // Handler to display camera view
    private fun takePictureFromCamera(docType: String?, lang: String?) {
        val applink = if (docType == HomeCreditKTPFragment.TYPE) {
            ApplinkConst.HOME_CREDIT_KTP_WITHOUT_TYPE
        } else {
            ApplinkConst.HOME_CREDIT_SELFIE_WITHOUT_TYPE
        }

        val intent = RouteManager.getIntent(this, applink)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
//        runOnUiThread {
//            if (cameraController == null) {
//                cameraController = CameraController(
//                    this,
//                    this,
//                    getRealScreenSize(),
//                    cameraview,
//                    docType
//                ) { docsTyp, imageBase64 ->
//                    finishTakePicture(docsTyp, imageBase64)
//                }
//            }
//            cameraview.initialize(docType, lang, "", {
//                cameraController?.takePicture()
//            }, { toggled: Boolean ->
//                cameraController?.flashToggled(toggled)
//            }, {
//                dismissTakeImageView()
//            })
//            cameraview.visibility = View.VISIBLE
//            cameraController?.startTakingPicture()
//        }
    }

    // Handler to inject image back to webview
    private fun finishTakePicture(docType: String?, imageBase64: String?) {
        if (imageBase64 != null && docType != null) {
            val script = String.format(
                "var event = new CustomEvent('cameraTriggered'," +
                    "{ detail: {document: '%s', image: 'data:image/jpeg;base64,%s'}});" +
                    "window.dispatchEvent(event);",
                docType,
                imageBase64
            )
            executeJs(script)
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

    @SuppressLint("PII Data Exposure")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageFilePath = data?.getStringExtra(HomeCreditRegisterActivity.HCI_KTP_IMAGE_PATH)
            val type = data?.getStringExtra(HomeCreditRegisterActivity.HCI_TYPE)

            val imageBase64 = getBase64FromImagePath(imageFilePath ?: "")?.replace("\n", "")

            finishTakePicture(type, imageBase64)
        }
    }

    private fun getBase64FromImagePath(imagePath: String): String? {
        try {
            val file = File(imagePath)
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(1024)
            val outputStream = ByteArrayOutputStream()
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            val imageBytes = outputStream.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private const val REQUEST_CODE_IMAGE = 123
    }
}

class WebAppInterface(private val openCamera: () -> Unit) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun takePicture(json: String) {
        val a = json
        openCamera.invoke()
    }
}
