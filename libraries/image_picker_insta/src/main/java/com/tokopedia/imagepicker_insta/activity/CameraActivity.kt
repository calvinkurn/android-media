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
import androidx.lifecycle.coroutineScope
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.fragment.CameraFragment
import com.tokopedia.imagepicker_insta.fragment.NoPermissionFragment
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.imagepicker_insta.mediacapture.MediaRepository
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.VideoUtil
import com.tokopedia.imagepicker_insta.util.requestCameraAndMicPermission
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.launch

class CameraActivity : PermissionActivity() {

    lateinit var parent: View
    lateinit var container: FrameLayout
    lateinit var toolbarTitle: Typography
    lateinit var toolbarIcon: AppCompatImageView
    var applinkToNavigateAfterMediaCapture: String? = null
    var videoMaxDuration:Long = VideoUtil.DEFAULT_DURATION_MAX_LIMIT
    var selectedFeedAccountId: String = ""
    val uriOfClickedMedias = arrayListOf<Uri>()
    var isOpenFrom: String = ""

    companion object {
        private const val EXTRA_SELECTED_FEED_ACCOUNT_ID = "EXTRA_SELECTED_FEED_ACCOUNT_ID"
        private const val CREATE_POST_REQUEST_CODE = 101

        fun getIntent(context: Context, applinkToNavigateAfterMediaCapture: String?, videoMaxDuration:Long, selectedFeedAccountId: String, isOpenFrom: String): Intent {
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, applinkToNavigateAfterMediaCapture)
            intent.putExtra(BundleData.VIDEO_MAX_SECONDS, videoMaxDuration)
            intent.putExtra(BundleData.SELECTED_FEED_ACCOUNT_ID, selectedFeedAccountId)
            intent.putExtra(BundleData.KEY_IS_OPEN_FROM, isOpenFrom)
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
        videoMaxDuration = intent.getLongExtra(BundleData.VIDEO_MAX_SECONDS,VideoUtil.DEFAULT_DURATION_MAX_LIMIT)
        if(videoMaxDuration ==null || videoMaxDuration == 0L){
            videoMaxDuration = VideoUtil.DEFAULT_DURATION_MAX_LIMIT
        }
        selectedFeedAccountId = intent.getStringExtra(BundleData.SELECTED_FEED_ACCOUNT_ID) ?: ""
        isOpenFrom = intent.extras?.getString(BundleData.KEY_IS_OPEN_FROM, "") ?: ""
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toolbarNavIcon: AppCompatImageView = findViewById(R.id.toolbar_nav_icon)
        toolbarNavIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setActivityResult() {
        val intent = Intent()
        intent.putExtra(BundleData.KEY_IS_OPEN_FROM, isOpenFrom)
        intent.putExtra(EXTRA_SELECTED_FEED_ACCOUNT_ID, selectedFeedAccountId)
        setResult(RESULT_OK, intent)
    }

    private fun afterMediaIsCaptured(uriList: List<Uri>) {
        if (!applinkToNavigateAfterMediaCapture.isNullOrEmpty()) {
            val finalApplink = CameraUtil.createApplinkToSendFileUris(applinkToNavigateAfterMediaCapture!!, uriList)
            val intent = RouteManager.getIntent(this, finalApplink).apply {
                putExtra(EXTRA_SELECTED_FEED_ACCOUNT_ID, selectedFeedAccountId)
                putExtra(BundleData.KEY_IS_OPEN_FROM, isOpenFrom)
            }
            startActivityForResult(intent, CREATE_POST_REQUEST_CODE)
        } else {
            finish()
        }
    }

    fun showToast(message: String, toasterType: Int) {
        Toaster.build(parent, message, Toaster.LENGTH_SHORT, toasterType).show()
    }


    fun exitActivityOnError() {
        setActivityResult()
        finish()
    }

    fun exitActivityOnSuccess(uri: Uri) {
        lifecycle.coroutineScope.launch {
            MediaRepository.mediaAdded(uri)
        }
        uriOfClickedMedias.add(0,uri)
        setActivityResult()
        afterMediaIsCaptured(arrayListOf(uri))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtil.MIC_PERMISSION_REQUEST_CODE, PermissionUtil.CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE -> {
                showFragment()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_POST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedFeedAccountId = data?.getStringExtra(EXTRA_SELECTED_FEED_ACCOUNT_ID) ?: ""
            isOpenFrom = data?.getStringExtra(BundleData.KEY_IS_OPEN_FROM) ?: ""
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
        if (!isProcessingMedia()) {
            setActivityResult()
            super.onBackPressed()
        }
    }
}
