package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.unifyprinciples.Typography

class ToggleCountLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    lateinit var tvCount: Typography
    lateinit var circleImage: ToggleImageView
    private var isMultiCheck = false

    @DrawableRes
    var drawableCircleCheck: Int = R.drawable.imagepicker_insta_ic_check_circle

    @DrawableRes
    var drawableCircleFilled: Int = R.drawable.imagepicker_insta_circle_filled


    fun getLayout() = R.layout.imagepicker_insta_toggle_count_layout

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    private fun initViews() {
        tvCount = findViewById(R.id.tv_count)
        circleImage = findViewById(R.id.circle_image)

        circleImage.scaleType = ImageView.ScaleType.FIT_XY
        setMultiCheckEnable(false)
    }

    fun setMultiCheckEnable(isMultiCheck: Boolean) {
        this.isMultiCheck = isMultiCheck

        if (isMultiCheck) {
            circleImage.onDrawableId = drawableCircleFilled
        } else {
            circleImage.onDrawableId = drawableCircleCheck
        }
    }

    fun setCount(count: Int) {
        tvCount.text = count.toString()
        if(!isMultiCheck || count == 0){
            tvCount.visibility = View.INVISIBLE
        }else{
            tvCount.visibility = View.VISIBLE
        }
    }

    fun toggle(isChecked: Boolean) {
        circleImage.toggle(isChecked)

        if (!isChecked) {
            setCount(0)
        }
    }
}