package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.gamification.R


class TapTapOverView : FrameLayout {

    lateinit var image: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView
    lateinit var tvDescription: AppCompatTextView
    lateinit var btnAction: AppCompatButton

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(com.tokopedia.gamification.R.layout.view_tap_tap_over, this, true)
        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDesc)
        btnAction = findViewById(R.id.btnAction)
    }
}