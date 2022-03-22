package com.tokopedia.imagepicker_insta.common.ui.toolbar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.iconunify.IconUnify
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker_insta.common.R
import com.tokopedia.unifyprinciples.Typography

class ImagePickerCommonToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.Toolbar(context, attrs) {

    lateinit var toolbarIcon: AppCompatImageView
    lateinit var toolbarTitle: Typography
    lateinit var toolbarSubtitle: Typography
    lateinit var toolbarNavIcon: AppCompatImageView
    lateinit var toolbarExpandIcon: IconUnify
    lateinit var toolbarParent: ConstraintLayout

    fun getLayout() = R.layout.imagepicker_insta_com_toolbar

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    private fun initViews() {
        setContentInsetsRelative(0,contentInsetEnd)
        setContentInsetsAbsolute(0,contentInsetRight)

        background = ColorDrawable(MethodChecker.getColor(context,R.color.Unify_Static_White))
        toolbarIcon  = findViewById(R.id.img_com_toolbar_icon)
        toolbarTitle = findViewById(R.id.img_com_toolbar_title)
        toolbarSubtitle = findViewById(R.id.img_com_toolbar_subtitle)
        toolbarNavIcon  = findViewById(R.id.img_com_toolbar_nav_icon)
        toolbarParent  = findViewById(R.id.toolbar_parent)
        toolbarExpandIcon  = findViewById(R.id.content_creator_expand_icon)

    }
}