package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.R

class CameraButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    lateinit var progressBar: ProgressBar
    lateinit var imageCapture: AppCompatImageView

    fun getLayout() = R.layout.imagepicker_insta_camer_button

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    private fun initViews(){
        progressBar = findViewById(R.id.progress_bar_timer)
        imageCapture = findViewById(R.id.image_capture)
    }
}