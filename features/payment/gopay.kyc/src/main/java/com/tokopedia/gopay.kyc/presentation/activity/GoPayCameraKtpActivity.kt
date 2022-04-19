package com.tokopedia.gopay.kyc.presentation.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gopay.kyc.di.DaggerGoPayKycComponent
import com.tokopedia.gopay.kyc.di.GoPayKycComponent
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayKtpFragment
import com.tokopedia.gopay.kyc.presentation.fragment.GoPaySelfieKtpFragment

class GoPayCameraKtpActivity : BaseSimpleActivity(), HasComponent<GoPayKycComponent> {

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.CAMERA)

    private val kycComponent: GoPayKycComponent by lazy { initInjector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        if (!allPermissionsGranted())
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CAMERA_PERMISSIONS)

    }

    override fun getNewFragment(): Fragment {
        return if (intent.hasExtra(IS_SELFIE) && intent.getBooleanExtra(IS_SELFIE, false)) {
            GoPaySelfieKtpFragment.newInstance()
        } else
            GoPayKtpFragment.newInstance()
    }

    override fun getScreenName() = null

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

    override fun getComponent() = kycComponent

    private fun initInjector() =
        DaggerGoPayKycComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()


    companion object {
        const val REQUEST_CAMERA_PERMISSIONS = 932
        const val IS_SELFIE = "isSelfie"
        const val KTP_IMAGE_PATH = "ktp_image_path"
        const val SELFIE_KTP_IMAGE_PATH = "selfie_ktp_image_path"
        fun getIntent(context: Context, isSelfieCamera: Boolean): Intent {
            return Intent(context, GoPayCameraKtpActivity::class.java).putExtra(
                IS_SELFIE,
                isSelfieCamera
            )
        }
    }
}