package com.tokopedia.gopay_kyc.presentation.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayKtpFragment
import kotlinx.android.synthetic.main.activity_gopay_ktp_layout.*

class GoPayCameraKtpActivity : BaseSimpleActivity() {

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        //setupOldToolbar()
        if (!allPermissionsGranted())
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CAMERA_PERMISSIONS)

    }

    //override fun getLayoutRes() = R.layout.activity_gopay_ktp_layout

    //override fun getParentViewResourceID(): Int = R.id.kycFrameLayout

    override fun getNewFragment() = GoPayKtpFragment.newInstance()
    override fun getScreenName() = null

    private fun setupOldToolbar() {
        ktpHeader.isShowBackButton = true
        toolbar = ktpHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        ktpHeader.title = UPGRADE_GOPAY_TITLE
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


    companion object {
        const val UPGRADE_GOPAY_TITLE = "Ambil Foto KTP"
        const val REQUEST_CAMERA_PERMISSIONS = 932
        fun getIntent(context: Context): Intent {
            return Intent(context, GoPayCameraKtpActivity::class.java)
        }
    }

}