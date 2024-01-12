package com.tokopedia.tokopedianow.home.presentation.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
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

    fun setColor(@DrawableRes borderDrawable: Int, @ColorRes imageColorResId: Int) {
        binding.apply {
            innerCircle.setImageResource(imageColorResId)
            imageBorder.setImageDrawable(ContextCompat.getDrawable(context, borderDrawable))
        }
    }
}
