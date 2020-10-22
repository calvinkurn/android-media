package com.tokopedia.inbox.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tokopedia.inbox.R

class InboxBottomNavigationView : BottomNavigationView {

    private var menuView: BottomNavigationMenuView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        initMenuView()
        initBadgeLayout()
    }

    private fun initMenuView() {
        menuView = getField(BottomNavigationView::class.java, this, "menuView")
        disableItemShiftingMode()
    }

    private fun initBadgeLayout() {
        val totalItems = menuView?.childCount ?: 0
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

    private fun <T> getField(targetClass: Class<*>, instance: Any, fieldName: String): T? {
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
}