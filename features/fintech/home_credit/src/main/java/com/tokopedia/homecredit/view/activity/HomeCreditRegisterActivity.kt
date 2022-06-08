package com.tokopedia.homecredit.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.homecredit.di.component.DaggerHomeCreditComponent
import com.tokopedia.homecredit.di.component.HomeCreditComponent
import com.tokopedia.homecredit.view.fragment.HomeCreditKTPFragment
import com.tokopedia.homecredit.view.fragment.HomeCreditSelfieFragment
import java.util.*

open class HomeCreditRegisterActivity : BaseSimpleActivity(), HasComponent<HomeCreditComponent?> {
    private var permissionsToRequest: MutableList<String>? = null
    private var isPermissionGotDenied = false
    private var showKtp = false
    private var homeCreditComponent: HomeCreditComponent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionsToRequest != null && grantResults.size == permissionsToRequest!!.size) {
            var grantCount = 0
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionGotDenied = true
                    break
                }
                grantCount++
            }
            if (grantCount == grantResults.size) {
                isPermissionGotDenied = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGotDenied) {
            finish()
            return
        }
        val permissions: Array<String> = arrayOf(Manifest.permission.CAMERA)
        permissionsToRequest = ArrayList()
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                (permissionsToRequest as ArrayList<String>).add(permission)
            }
        }
        if ((permissionsToRequest as ArrayList<String>).isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                (permissionsToRequest as ArrayList<String>).toTypedArray(),
                REQUEST_CAMERA_PERMISSIONS
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun getNewFragment(): Fragment? {
        val intent = intent
        if (intent != null) {
            val uri = intent.data
            showKtp = "true" == uri!!.getQueryParameter(SHOW_KTP)
        }
        return if (showKtp) {
            HomeCreditKTPFragment.createInstance()
        } else {
            HomeCreditSelfieFragment.createInstance()
        }
    }

    override fun getComponent(): HomeCreditComponent {
        if (homeCreditComponent == null) initInjector()
        return homeCreditComponent!!
    }

    private fun initInjector() {
        homeCreditComponent = DaggerHomeCreditComponent.builder().baseAppComponent(
            (applicationContext as BaseMainApplication).baseAppComponent
        ).build()
    }

    companion object {
        const val HCI_KTP_IMAGE_PATH = "ktp_image_path"
        private const val REQUEST_CAMERA_PERMISSIONS = 932
        private const val SHOW_KTP = "show_ktp"
    }
}