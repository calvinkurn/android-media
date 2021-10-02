package com.tokopedia.imagepicker_insta.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.fragment.CameraFragment
import com.tokopedia.imagepicker_insta.fragment.NoPermissionFragment
import com.tokopedia.imagepicker_insta.common.BundleData
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.requestCameraAndMicPermission
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class CameraActivity : PermissionActivity() {

    lateinit var parent: View
    lateinit var container: FrameLayout
    lateinit var toolbarTitle: Typography
    lateinit var toolbarIcon: AppCompatImageView
    var applinkToNavigateAfterMediaCapture: String? = null
    val uriOfClickedMedias = arrayListOf<Uri>()

    companion object {

        val REQUEST_CODE = 213
        fun getIntent(context: Context, applinkToNavigateAfterMediaCapture: String?): Intent {
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, applinkToNavigateAfterMediaCapture)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readIntentData()
        setContentView(R.layout.imagepicker_insta_camera_activity)
        parent = findViewById(R.id.parent)
        container = findViewById(R.id.frag_container)
        toolbarIcon = findViewById(R.id.toolbar_nav_icon)
        toolbarTitle = findViewById(R.id.toolbar_title)
        showFragment(requestPerm = true)
        setToolbar()
    }

    override fun onResume() {
        super.onResume()
        if (!supportFragmentManager.fragments.isNullOrEmpty()) {
            if (!PermissionUtil.hasCameraAndMicPermission(this)) {
                showPermissionFragment()
            } else {
                if (supportFragmentManager.fragments.first() is NoPermissionFragment) {
                    showCameraFragment()
                }
            }
        }

    }

    private fun showFragment(requestPerm: Boolean = false) {
        if (PermissionUtil.hasCameraAndMicPermission(this)) {
            showCameraFragment()
        } else {
            showPermissionFragment()
            if (requestPerm) {
                requestCameraAndMicPermission()
            }
        }
    }

    private fun showCameraFragment() {
        supportFragmentManager.beginTransaction()
            .replace(container.id, CameraFragment())
            .commit()

        toolbarIcon.setColorFilter(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White), PorterDuff.Mode.SRC_IN)
        toolbarTitle.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
    }

    private fun showPermissionFragment() {
        supportFragmentManager.beginTransaction()
            .replace(container.id, NoPermissionFragment())
            .commit()

        toolbarIcon.setColorFilter(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), PorterDuff.Mode.SRC_IN)
        toolbarTitle.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black))
    }

    private fun readIntentData() {
        applinkToNavigateAfterMediaCapture = intent.getStringExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE)
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toolbarNavIcon: AppCompatImageView = findViewById(R.id.toolbar_nav_icon)
        toolbarNavIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun afterMediaIsCaptured(uriList: List<Uri>) {
        if (!applinkToNavigateAfterMediaCapture.isNullOrEmpty()) {
            val finalApplink = CameraUtil.createApplinkToSendFileUris(applinkToNavigateAfterMediaCapture!!, uriList)
            RouteManager.route(this, finalApplink)
        } else {
            finish()
        }
    }

    fun showToast(message: String, toasterType: Int) {
        Toaster.build(parent, message, Toaster.LENGTH_SHORT, toasterType).show()
    }


    fun exitActivityOnError() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun exitActivityOnSuccess(uri: Uri) {
        uriOfClickedMedias.add(0,uri)
        setResult(Activity.RESULT_OK, CameraUtil.getIntentfromFileUris(uriOfClickedMedias))
        afterMediaIsCaptured(ArrayList(uri))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtil.MIC_PERMISSION_REQUEST_CODE, PermissionUtil.CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE -> {
                showFragment()
            }
        }
    }

    private fun isProcessingMedia(): Boolean {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            if (supportFragmentManager.fragments.first() is CameraFragment) {
                val camFragment = supportFragmentManager.fragments.first() as CameraFragment
                if (camFragment.loader.visibility == View.VISIBLE) {
                    return true
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (!isProcessingMedia())
            super.onBackPressed()

    }
}