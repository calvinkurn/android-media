package com.tokopedia.play.broadcaster.view.custom

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionUtil


/**
 * Created by mzennis on 02/06/20.
 */
class PlayRequestPermissionView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val tvCheckCamera: AppCompatTextView
    private val tvCheckMic: AppCompatTextView
    private val ivClose: AppCompatImageView

    init {
        val view = View.inflate(context, R.layout.view_play_request_permission, this)
        tvCheckCamera = view.findViewById(R.id.tv_permit_check_camera)
        tvCheckMic = view.findViewById(R.id.tv_permit_check_mic)
        ivClose = view.findViewById(R.id.iv_permit_close)
        setupView(view)
    }

    private fun setupView(view: View?) {
        tvCheckCamera.setOnClickListener {
            goToDeviceSetting()
        }

        tvCheckMic.setOnClickListener {
            goToDeviceSetting()
        }

        ivClose.setOnClickListener {
            (context as? Activity)?.finish()
        }
    }

    private fun goToDeviceSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        (context as? Activity)?.startActivityForResult(intent, PlayPermissionUtil.PLAY_REQUEST_PERMISSION_CODE)
    }

    fun setPermissionGranted(permissions: List<String>) {
        permissions.forEach {
            if (it == Manifest.permission.CAMERA) {
                setPermissionGranted(tvCheckCamera)
            } else if (it == Manifest.permission.RECORD_AUDIO) {
                setPermissionGranted(tvCheckMic)
            }
        }
    }

    private fun setPermissionGranted(textView: AppCompatTextView) {
        textView.text = ""
        textView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_play_check_green,
                0
        )
    }
}