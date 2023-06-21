package com.tokopedia.imagepicker_insta.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.util.VideoUtil

@SuppressLint("ClickableViewAccessibility")
class CameraButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    lateinit var progressBar: ProgressBar
    lateinit var imageCapture: AppCompatImageView
    var cameraButtonListener: CameraButtonListener? = null
    var maxDurationToRecord = VideoUtil.DEFAULT_DURATION_MAX_LIMIT

    fun getLayout() = R.layout.imagepicker_insta_camer_button

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        progressBar = findViewById(R.id.progress_bar_timer)
        imageCapture = findViewById(R.id.image_capture)
        addTouchListener()
    }

    fun addTouchListener() {
        imageCapture.setOnClickListener {
            cameraButtonListener?.onClick()
        }
    }
}

interface CameraButtonListener {
    fun onClick()
}
