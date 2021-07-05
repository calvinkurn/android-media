package com.tokopedia.play.broadcaster.view.fragment

import android.Manifest.permission
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.permission.PermissionHelper
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.Companion.RESULT_PERMISSION_CODE
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import javax.inject.Inject


/**
 * Created by mzennis on 10/07/20.
 */
class PlayPermissionFragment @Inject constructor():  PlayBaseBroadcastFragment() {

    private lateinit var ivClose: AppCompatImageView
    private var tvCheckCamera: AppCompatTextView? = null
    private var tvCheckMic: AppCompatTextView? = null

    private lateinit var permissionHelper: PermissionHelper

    override fun getScreenName(): String = "Play Permission page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionHelper = PermissionHelperImpl(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView()
        setupInsets(view)
    }

    override fun onResume() {
        super.onResume()
        configurePermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)) return
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun initView(view: View) {
        with(view) {
            tvCheckCamera = findViewById(R.id.tv_permit_check_camera)
            tvCheckMic = findViewById(R.id.tv_permit_check_mic)
            ivClose = findViewById(R.id.iv_permit_close)
        }
    }

    private fun setupView() {
        tvCheckCamera?.setOnClickListener { requestPermission(permission.CAMERA, CAMERA_PERMISSION_CODE) }
        tvCheckMic?.setOnClickListener {requestPermission(permission.RECORD_AUDIO, MICROPHONE_PERMISSION_CODE) }
        ivClose.setOnClickListener { activity?.finish() }
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        permissionHelper.requestPermissionFullFlow(permission, requestCode, object : PermissionResultListener {
            override fun onRequestPermissionResult(): PermissionStatusHandler {
                return {
                    if (!isAllGranted()) {
                        if (!shouldShowRequestPermissionRationale(permission)) goToDeviceSetting()
                    } else {
                        (activity as? PlayBroadcastActivity)?.checkAllPermission()
                    }
                }
            }

            override fun onShouldShowRequestPermissionRationale(permissions: Array<String>, requestCode: Int): Boolean {
                return false
            }
        })
    }

    private fun setupInsets(view: View) {
        ivClose.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun configurePermission() {
        if (permissionHelper.isPermissionGranted(permission.CAMERA)) {
            (activity as? PlayBroadcastActivity)?.startPreview()
            setPermissionGranted(tvCheckCamera)
        }
        if (permissionHelper.isPermissionGranted(permission.RECORD_AUDIO))
            setPermissionGranted(tvCheckMic)
    }

    private fun setPermissionGranted(textView: AppCompatTextView?) {
        textView?.text = ""
        textView?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_play_check_green,
                0
        )
        textView?.setOnClickListener(null)
    }

    private fun goToDeviceSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        activity?.startActivityForResult(intent, RESULT_PERMISSION_CODE)
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 3297
        private const val MICROPHONE_PERMISSION_CODE = 3296
    }

}