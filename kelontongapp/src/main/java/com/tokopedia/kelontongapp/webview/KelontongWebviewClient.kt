package com.tokopedia.kelontongapp.webview

import android.Manifest.permission.*
import android.annotation.TargetApi
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import android.content.Context
import android.content.SharedPreferences
import android.webkit.WebResourceError
import com.appsflyer.AppsFlyerLib
import com.tokopedia.kelontongapp.*
import org.json.JSONObject
import org.json.JSONArray
import java.lang.Exception

/**
 * Created by meta on 12/10/18.
 */
class KelontongWebviewClient(private val activity: Activity) : WebViewClient() {
    private val sharedPref: SharedPreferences = activity.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        val uri = Uri.parse(url)
        return handleUri(view, uri)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val uri = request.url
        return handleUri(view, uri)
    }

    private fun handleUri(view: WebView, uri: Uri): Boolean {
        return if (uri.scheme.startsWith(APPSFLYER_URL_SCHEME)) {
            handleAppsFlyer(uri)
            false
        } else if (uri.scheme.startsWith(MOENGAGE_URL_SCHEME)) {
            handleMoengage(uri)
            false
        } else {
            view.loadUrl(uri.toString())
            true
        }
    }

//    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
//        super.onReceivedError(view, request, error)
//        if (activity is KelontongMainActivity) {
//            activity.onReceivedErrorView()
//        }
//    }

    fun checkPermission(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(activity, CAMERA)
        val resultWrite = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE)
        return resultCamera == PackageManager.PERMISSION_GRANTED
                && resultWrite == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(activity, arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (!(writeAccepted && cameraAccepted))
                    Toast.makeText(activity, "Mohon untuk memberikan izin akses kamera dan menulis file.", Toast.LENGTH_SHORT).show()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (activity.shouldShowRequestPermissionRationale(CAMERA)) {
                        showMessageOKCancel("Mohon untuk memberikan izin akses kamera dan menulis file.", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                requestPermission()
                            }
                        })
                        return
                    }
                }
            }
        }
    }

    private var progressInterface: ProgressInterface? = null

    fun setProgressInterface(progressInterface: ProgressInterface) {
        this.progressInterface = progressInterface;
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)
        if (progressInterface != null)
            progressInterface!!.onPageVisible()
    }

    interface ProgressInterface {
        fun onPageVisible()
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Batal", null)
                .create()
                .show()
    }

    companion object {

        private val PERMISSION_REQUEST_CODE = 2312
    }

    private fun handleMoengage(uri: Uri) {
        // user loginn
        // MoEHelper.getInstance(getApplicationContext()).setUniqueId(UNIQUE_ID);
        // user logout
        // MoEHelper.getInstance(getApplicationContext()).logoutUser();
        // set tracking user attribute
        // helper = MoEHelper.getInstance(this);
        // Helper method to set User uniqueId. Can be String,int,long,float,double
//        helper.setUniqueId("abc@moengage.com");
//        // If you have first and last name separately
//        helper.setFirstName("Aaron");
//        helper.setLastName("Finch");
//        // If you have full name
//        helper.setFullName("Aaron Finch");
//        helper.setBirthDate("01/01/1990");
//        helper.setUserLocation(40.77,73.98);
//        helper.setEmail("abc@moengage.com");
//        helper.setGender("Male");
//        //Helper method to set mobile number
//        helper.setNumber("12345612345");

        // MoEHelper.getInstance(mCurrentContext).setUserAttribute("locality", "SF");
        // https://docs.moengage.com/docs/identifying-user

        // tracking event
//        PayloadBuilder builder = new PayloadBuilder();
//        builder.putAttrInt("quantity", 2)
//                .putAttrString("product", "iPhone")
//                .putAttrDate("purchaseDate", new Date())
//                .putAttrDouble("price", 5999.99)
//                .putAttrString("currency", "dollar");
//        MoEHelper.getInstance(mCurrentContext).trackEvent("Purchase", builder.build());
        // https://docs.moengage.com/docs/track-event
    }

    private fun handleAppsFlyer(uri: Uri) {
        if(uri.host == APPSFLYER_SET_USER) {
            if(uri.getQueryParameter(ID) != null) {
                with (sharedPref.edit()) {
                    putString(USER_ID, uri.getQueryParameter(ID))
                    apply()
                }
                AppsFlyerLib.getInstance().setCustomerUserId(uri.getQueryParameter(ID))
            }
        } else {
            var eventName: String? = uri.getQueryParameter(EVENT_NAME)
            val eventValue: MutableMap<String, Any> = HashMap()

            val event: JSONObject
            val keys: JSONArray
            try {
                event = JSONObject(uri.getQueryParameter(EVENT_VALUE))
                keys = event.names()
                for (i in 0 until keys.length()) {
                    eventValue[keys.getString(i)] = event.getString(keys.getString(i))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            AppsFlyerLib.getInstance().trackEvent(activity.applicationContext, eventName, eventValue)
        }
    }
}
