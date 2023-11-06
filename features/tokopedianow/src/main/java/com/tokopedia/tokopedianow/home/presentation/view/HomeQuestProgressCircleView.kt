package com.tokopedia.tokopedianow.home.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuestProgressBarCircleViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class HomeQuestProgressCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    private val binding = LayoutTokopedianowQuestProgressBarCircleViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setColor(@ColorRes strokeColorResId: Int, @ColorRes imageColorResId: Int) {
        binding.apply {
            imageCircle.setStrokeColorResource(strokeColorResId)
            imageCircle.loadImage(imageColorResId)
        }
    }
}
