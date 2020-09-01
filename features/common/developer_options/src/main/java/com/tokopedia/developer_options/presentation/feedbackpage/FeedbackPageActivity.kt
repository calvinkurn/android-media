package com.tokopedia.developer_options.presentation.feedbackpage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.screenshot_observer.Screenshot

class FeedbackPageActivity : BaseSimpleActivity() {

    private lateinit var screenshot: Screenshot

    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun getNewFragment(): Fragment? {
        return FeedbackPageFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
    companion object {

        private const val PERMISSIONS_REQUEST_CODE = 5111
    }
}