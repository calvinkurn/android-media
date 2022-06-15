package com.tokopedia.imagepicker_insta.common.ui.toolbar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker_insta.common.R
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.unifyprinciples.Typography

class ImagePickerCommonToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.Toolbar(context, attrs) {

    lateinit var toolbarIcon: AppCompatImageView
    lateinit var toolbarTitle: Typography
    lateinit var toolbarSubtitle: Typography
    lateinit var toolbarNavIcon: AppCompatImageView

    fun getLayout() = R.layout.imagepicker_insta_com_toolbar

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    private fun initViews() {
        setContentInsetsRelative(0,contentInsetEnd)
        setContentInsetsAbsolute(0,contentInsetRight)

        background = ColorDrawable(MethodChecker.getColor(context,R.color.Unify_Background))
        toolbarIcon  = findViewById(R.id.img_com_toolbar_icon)
        toolbarTitle = findViewById(R.id.img_com_toolbar_title)
        toolbarSubtitle = findViewById(R.id.img_com_toolbar_subtitle)
        toolbarNavIcon  = findViewById(R.id.img_com_toolbar_nav_icon)
    }
}