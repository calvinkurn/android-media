package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.AssetImageView

class ToogleViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var maskImageView:AppCompatImageView
    lateinit var toggleImageView:ToggleImageView
    lateinit var assetImageView:AssetImageView


}