package com.tokopedia.play.broadcaster.view.fragment

import android.Manifest
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
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.util.permission.PermissionHelper
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.Companion.RESULT_PERMISSION_CODE
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import javax.inject.Inject


/**
 * Created by mzennis on 10/07/20.
 */
class PlayPermissionFragment @Inject constructor():  PlayBaseBroadcastFragment() {

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    private lateinit var ivClose: AppCompatImageView
    private var tvCheckCamera: AppCompatTextView? = null
    private var tvCheckMic: AppCompatTextView? = null

    private lateinit var permissionHelper: PermissionHelper

    override fun getScreenName(): String = "Play Permission page"

    override fun onStart() {
        super.onStart()
        analytic.openPermissionScreen()
    }

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
        broadcastCoordinator.setupTitle("")
        tvCheckCamera?.setOnClickListener { goToDeviceSetting() }
        tvCheckMic?.setOnClickListener { goToDeviceSetting() }
        ivClose.setOnClickListener { activity?.finish() }

        configurePermission()
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

    private fun goToDeviceSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        activity?.startActivityForResult(intent, RESULT_PERMISSION_CODE)
    }

    private fun configurePermission() {
        if (permissionHelper.isPermissionGranted(Manifest.permission.CAMERA)) setPermissionGranted(Manifest.permission.CAMERA)
        if (permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO)) setPermissionGranted(Manifest.permission.RECORD_AUDIO)
    }

    private fun setPermissionGranted(permission: String) {
        if (permission == Manifest.permission.CAMERA) {
            setPermissionGranted(tvCheckCamera)
        } else if (permission == Manifest.permission.RECORD_AUDIO) {
            setPermissionGranted(tvCheckMic)
        }
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

}