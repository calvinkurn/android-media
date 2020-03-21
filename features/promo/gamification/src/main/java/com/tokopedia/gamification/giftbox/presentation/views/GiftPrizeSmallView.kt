package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.gamification.R
import com.tokopedia.utils.image.ImageUtils


class GiftPrizeSmallView : FrameLayout {
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: AppCompatTextView

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

    fun getLayout() = R.layout.view_gift_benefits_small

    fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
    }

    fun setData(imageUrl: String, textList: List<String>?) {
        ImageUtils.loadImage(image, imageUrl)
        if (!textList.isNullOrEmpty()) {
            tvTitle.text = textList[0]
        }
    }
}