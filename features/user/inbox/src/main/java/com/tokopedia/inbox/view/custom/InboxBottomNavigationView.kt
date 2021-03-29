package com.tokopedia.inbox.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inbox.view.binder.BadgeCounterBinder
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.getTypeface

class InboxBottomNavigationView : BottomNavigationView {

    private var menuView: BottomNavigationMenuView? = null
    private var buttons: Array<BottomNavigationItemView>? = null
    private var labelFontSize: Float = DEFAULT_FONT_SIZE
    private var badges: Array<Typography?> = Array(3) { null }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        initField()
        initMenuView()
        initBadgeLayout()
    }

    fun setBadgeCount(@InboxFragmentType typePosition: Int, badgeCount: Int?) {
        val badge = getBadgeAt(typePosition)
        BadgeCounterBinder.bindBadgeCounter(badge, badgeCount)
    }

    fun hasBadge(@InboxFragmentType typePosition: Int): Boolean? {
        val badge = getBadgeAt(typePosition)
        return badge?.text?.isNotEmpty()
    }

    fun setSelectedPage(@InboxFragmentType page: Int) {
        val pageId = when (page) {
            InboxFragmentType.NOTIFICATION -> R.id.menu_inbox_notification
            InboxFragmentType.CHAT -> R.id.menu_inbox_chat
            InboxFragmentType.DISCUSSION -> R.id.menu_inbox_discussion
            else -> throw UnsupportedOperationException("Unsupported fragment type")
        }
        selectedItemId = pageId
    }

    private fun initMenuView() {
        disableItemShiftingMode()
        disableAnimation()
    }

    private fun initField() {
        menuView = getField(BottomNavigationView::class.java, this, "menuView")
        buttons = getField(menuView?.javaClass, menuView, "buttons")
    }

    private fun initBadgeLayout() {
        val totalItems = menuView?.childCount ?: return
        for (i in 0 until totalItems) {
            val menuItem = getMenuViewAt(i)
            val badge = View.inflate(context, R.layout.partial_inbox_bottom_nav_badge, null)
            val layoutParam = LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
            menuItem?.addView(badge, layoutParam)
            badges[i] = badge.findViewById(R.id.unread_counter)
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
        buttons?.let {
            for (button in it) {
                getField<TextView?>(button.javaClass, button, "largeLabel")?.apply {
                    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelFontSize)
                    this.setPadding(0, 0, 0, 0)
                }
                getField<TextView?>(button.javaClass, button, "smallLabel")?.apply {
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

    private fun getMenuViewAt(position: Int): BottomNavigationItemView? {
        return menuView?.getChildAt(position) as? BottomNavigationItemView
    }

    private fun getBadgeAt(typePosition: Int): Typography? {
        return badges.getOrNull(typePosition)
    }

    companion object {
        val DEFAULT_FONT_SIZE = 12f.toPx()
    }
}