package com.tokopedia.tokopedianow.common.view

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.toPx

class ToolbarHeaderView(
    private val header: HeaderUnify?,
    private val statusBar: StatusBarView?,
    private val findSwitchThemePosition: ((recyclerView: RecyclerView, dx: Int, dy: Int) -> Float)? = null
) {

    companion object {
        private const val ICON_SIZE = 24
        private const val ICON_MARGIN = 8
    }

    var title: String = ""
    var rightIcons: List<RightIcon> = listOf()
        set(value) {
            field = value
            removeRightIcons()
            addRightIcons()
        }
    var scrollListener: RecyclerView.OnScrollListener? = null
    var onSwitchToTransparent: (() -> Unit)? = null
    var onSwitchToNormal: (() -> Unit)? = null
    var enableSwitchTheme: Boolean = true
        set(value) {
            field = value
            if (field) {
                setScrollListener()
            } else {
                resetScrollListener()
            }
        }

    fun setTheme(transparent: Boolean) {
        val isTransparent = header?.transparentMode ?: false

        when {
            transparent && !isTransparent -> setTransparentTheme()
            !transparent && isTransparent -> setNormalTheme()
        }
    }

    fun setNavButtonClickListener(onClick: () -> Unit) {
        header?.setNavigationOnClickListener {
            onClick.invoke()
        }
    }

    fun getActionItem(pos: Int): ImageView? = header?.rightIcons?.get(pos)

    fun getRightIcon(pos: Int): Int? = rightIcons.getOrNull(pos)?.iconId

    fun setRightIconsColor(@ColorRes colorId: Int) {
        header?.rightIcons?.forEach {
            it.drawable?.let { drawable ->
                val color = ContextCompat.getColor(header.context, colorId)
                applyIconUnifyColor(drawable, color)
            }
        }
    }

    fun setBackButtonColor(@ColorRes colorResId: Int) {
        header?.apply {
            val color = ContextCompat.getColor(context, colorResId)
            navigationIcon = getIconUnifyDrawable(context, IconUnify.ARROW_BACK, color)
        }
    }

    fun reset() {
        header?.apply {
            headerTitle = ""
            isShowShadow = false
            transparentMode = true
        }
        setBackButtonColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        setRightIconsColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
    }

    private fun addRightIcons() {
        rightIcons.forEach { rightIcon ->
            header?.apply {
                val icon = ContextCompat.getDrawable(context, rightIcon.iconId)
                icon?.setTint(ContextCompat.getColor(context, rightIcon.colorId))

                val mImageView = ImageView(context)
                val param = LinearLayout.LayoutParams(ICON_SIZE.toPx(), ICON_SIZE.toPx()).apply {
                    setMargins(ICON_MARGIN.toPx(), 0, 0, 0)
                }

                mImageView.layoutParams = param
                mImageView.setImageDrawable(icon)
                rightContentView.addView(mImageView)
                rightIcons?.add(mImageView)
            }
        }
    }

    private fun setTransparentTheme() {
        header?.apply {
            headerTitle = ""
            isShowShadow = false
            transparentMode = true
            setBackButtonColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            setRightIconsColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        }
    }

    private fun setNormalTheme() {
        header?.apply {
            headerTitle = this@ToolbarHeaderView.title
            isShowShadow = false
            transparentMode = false
            setBackButtonColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            setRightIconsColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        }
    }

    private fun removeRightIcons() {
        header?.rightContentView?.removeAllViews()
        header?.rightIcons?.clear()
    }

    private fun setScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val targetY = findSwitchThemePosition?.invoke(recyclerView, dx, dy).orZero()

                if (dy <= targetY) {
                    statusBar?.setTheme(transparent = true)
                    setTheme(transparent = true)
                    onSwitchToTransparent?.invoke()
                } else {
                    statusBar?.setTheme(transparent = false)
                    setTheme(transparent = false)
                    onSwitchToNormal?.invoke()
                }
            }
        }
    }

    private fun resetScrollListener() {
        scrollListener = null
    }
    
    data class RightIcon(
        @DrawableRes val iconId: Int,
        @ColorRes val colorId: Int
    )
}
