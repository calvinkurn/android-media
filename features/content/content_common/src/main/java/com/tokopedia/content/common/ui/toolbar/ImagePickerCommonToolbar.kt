package com.tokopedia.content.common.ui.toolbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import com.tokopedia.content.common.databinding.ImagepickerInstaComToolbarBinding
import com.tokopedia.content.common.ui.toolbar.ContentColor.LIGHT
import com.tokopedia.content.common.ui.toolbar.ContentColor.DARK
import com.tokopedia.content.common.ui.toolbar.ContentColor.TRANSPARENT
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
import com.tokopedia.unifyprinciples.R.color.Unify_Static_White

class ImagePickerCommonToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : Toolbar(context, attrs) {

    private var mOnClickListener: AccountClickListener? = null
    private var mOnBackListener: BackClickListener? = null
    private var mBinding = ImagepickerInstaComToolbarBinding.inflate(LayoutInflater.from(context), this)

    init {
        initViews()
    }

    private fun initViews() {
        setContentInsetsRelative(0, contentInsetEnd)
        setContentInsetsAbsolute(0, contentInsetRight)

        mBinding.imgComToolbarNavIcon.setOnClickListener {
            mOnBackListener?.invoke()
        }

        mBinding.imgComToolbarIcon.setOnClickListener {
            mOnClickListener?.invoke()
        }

        mBinding.textComToolbarSubtitle.setOnClickListener {
            mOnClickListener?.invoke()
        }
    }

    var icon: String = ""
        set(value) {
            field = value
            mBinding.imgComToolbarIcon.loadImageCircle(value)
            mBinding.imgComToolbarIcon.visibility = if(value.isNotEmpty()) View.VISIBLE else View.GONE
        }

    var title: String = ""
        set(value) {
            field = value
            mBinding.textComToolbarTitle.text = value
        }

    var subtitle: String = ""
        set(value) {
            field = value
            mBinding.textComToolbarSubtitle.text = value
        }

    var navIcon: Int = 0
        set(value) {
            field = value
            mBinding.imgComToolbarNavIcon.setImage(newIconId = navIcon)
        }

    fun setContentColor(contentColor: ContentColor) = with(mBinding) {
        val content: Int
        val background: Int
        when(contentColor) {
            LIGHT, TRANSPARENT -> {
                content = getColor(context, Unify_Static_White)
                background = if (contentColor == LIGHT) getColor(context, Unify_Static_Black)
                else Color.TRANSPARENT
            }
            DARK -> {
                content = getColor(context, Unify_Static_Black)
                background = getColor(context, Unify_Static_White)
            }
        }
        textComToolbarTitle.setTextColor(content)
        textComToolbarSubtitle.setTextColor(content)
        imgComToolbarNavIcon.setImage(newLightEnable = content, newDarkEnable = content)
        imgContentCreatorExpand.setImage(newLightEnable = content, newDarkEnable = content)
        toolbarParent.setBackgroundColor(background)
    }

    fun getToolbarParentView(): ConstraintLayout {
        return mBinding.toolbarParent
    }

    fun showHideExpandIcon(shouldShowExpandIcon: Boolean){
        mBinding.imgContentCreatorExpand.visibility = if(shouldShowExpandIcon) View.VISIBLE else View.GONE
    }

    fun setOnBackClickListener(listener: AccountClickListener?) {
        mOnBackListener = listener
    }

    fun setOnAccountClickListener(listener: BackClickListener?) {
        mOnClickListener = listener

        showHideExpandIcon(mOnClickListener != null)
    }
}

typealias AccountClickListener = () -> Unit
typealias BackClickListener = () -> Unit

enum class ContentColor {
    LIGHT, DARK, TRANSPARENT
}