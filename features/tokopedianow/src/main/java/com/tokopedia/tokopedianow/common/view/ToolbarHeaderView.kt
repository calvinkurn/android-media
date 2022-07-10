package com.tokopedia.tokopedianow.common.view

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.kotlin.extensions.view.orZero

class ToolbarHeaderView(
    private val header: HeaderUnify?,
    private val statusBar: StatusBarView?,
    private val findSwitchThemePosition: ((recyclerView: RecyclerView, dx: Int, dy: Int) -> Float)? = null
) {

    var title: String = ""
    var icons: List<Int> = listOf()
        set(value) {
            field = value
            value.forEach {
                header?.addRightIcon(it)
            }
        }
    var scrollListener: RecyclerView.OnScrollListener? = null
    var onSwitchToTransparent: (() -> Unit)? = null
    var onSwitchToNormal: (() -> Unit)? = null
    var enableSwitchTheme: Boolean = true

    init {
        if (enableSwitchTheme) {
            createScrollListener()
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

    fun getActionItem(pos: Int): View? = header?.rightIcons?.get(pos)

    private fun setTransparentTheme() {
        header?.apply {
            headerTitle = ""
            isShowShadow = false
            transparentMode = true
            setIconsColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        }
    }

    private fun setNormalTheme() {
        header?.apply {
            headerTitle = this@ToolbarHeaderView.title
            isShowShadow = false
            transparentMode = false
            setIconsColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        }
    }

    private fun setIconsColor(@ColorRes colorId: Int) {
        header?.rightIcons?.forEach {
            it.drawable?.let { drawable ->
                val color = ContextCompat.getColor(header.context, colorId)
                applyIconUnifyColor(drawable, color)
            }
        }
    }

    private fun createScrollListener() {
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
}