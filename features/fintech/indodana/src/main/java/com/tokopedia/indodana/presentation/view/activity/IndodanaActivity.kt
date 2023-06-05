package com.tokopedia.indodana.presentation.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity
import com.tokopedia.homecredit.view.fragment.HomeCreditCameraV2Fragment
import com.tokopedia.indodana.R
import com.tokopedia.indodana.presentation.view.fragment.PartnerKycFragment
import org.json.JSONObject
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class IndodanaActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val url = intent.data?.getQueryParameter("url")
            ?: "https://sandbox01.indodana.com/borrower/credit-limit/apply/sign-in?campaignid=%7Btokopedia%7C%7Ccustomized%7D&formVersion=V4&network=tokopedia&phoneNumber=%7BphoneNumber%7D&uiVersion=V2&utm_campaign=%7Btokopedia%7C%7Ccustomized%7D&utm_medium=webform&utm_source=tokopedia"
        return PartnerKycFragment.create(url)
    }
}
