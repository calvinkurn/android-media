package com.tokopedia.inbox.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tokopedia.inbox.R
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifyprinciples.getTypeface

class InboxBottomNavigationView : BottomNavigationView {

    private var menuView: BottomNavigationMenuView? = null
    private var buttons: Array<BottomNavigationItemView>? = null
    private var labelFontSize: Float = DEFAULT_FONT_SIZE

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        initMenuView()
        initBadgeLayout()
    }

    private fun initMenuView() {
        menuView = getField(BottomNavigationView::class.java, this, "menuView")
        buttons = getField(menuView?.javaClass, menuView, "buttons")
        disableItemShiftingMode()
        disableAnimation()
    }

    private fun initBadgeLayout() {
        val totalItems = menuView?.childCount ?: return
        for (i in 0 until totalItems) {
            val menuItem = menuView?.getChildAt(i) as? BottomNavigationItemView
            val badge = View.inflate(context, R.layout.partial_inbox_bottom_nav_badge, null)
            val layoutParam = FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
            menuItem?.addView(badge, layoutParam)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun disableItemShiftingMode() {
        val totalItems = menuView?.childCount ?: 0
        for (i in 0 until totalItems) {
            val menuItem = menuView?.getChildAt(i) as? BottomNavigationItemView
            menuItem?.setShifting(false)
        }
    }

    private fun disableAnimation() {
        val typeface = getTypeface(context, DEFAULT_TYPEFACE)
        buttons?.let {
            for (button in it) {
                getField<TextView?>(button.javaClass, button, "largeLabel")?.apply {
                    this.typeface = typeface
                    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelFontSize)
                    this.setPadding(0, 0, 0, 0)
                }
                getField<TextView?>(button.javaClass, button, "smallLabel")?.apply {
                    this.typeface = typeface
                    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelFontSize)
                }
                setField(button.javaClass, button, "shiftAmount", 0)
                setField(button.javaClass, button, "scaleUpFactor", 1)
                setField(button.javaClass, button, "scaleDownFactor", 1)
            }
        }
    }

    private fun setField(targetClass: Class<*>, instance: Any, fieldName: String, value: Any) {
        try {
            val field = targetClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field[instance] = value
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private fun <T> getField(targetClass: Class<*>?, instance: Any?, fieldName: String): T? {
        if (targetClass == null || instance == null) return null
        try {
            val field = targetClass.getDeclaredField(fieldName)
            field.isAccessible = true
            return field[instance] as T
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        const val DEFAULT_TYPEFACE = "NunitoSansExtraBold.ttf"
        val DEFAULT_FONT_SIZE = 10f.toPx()
    }
}