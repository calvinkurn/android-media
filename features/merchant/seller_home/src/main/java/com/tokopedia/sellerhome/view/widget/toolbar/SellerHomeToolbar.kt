package com.tokopedia.sellerhome.view.widget.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sellerhome.R

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class SellerHomeToolbar(context: Context?, attrs: AttributeSet?) : Toolbar(context, attrs) {

    private val notificationMenuId = R.id.menu_sah_notification
    private val addProductMenuId = R.id.menu_sah_add_product
    private val chatSettingsMenuId = R.id.menu_sah_chat_settings

    init {
        inflateMenu(R.menu.sah_menu_toolbar_notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = context?.dpToPx(8) ?: 12f
        }
    }

    fun showNotificationActionMenu(onClick: () -> Unit) {
        menu.findItem(notificationMenuId)?.isVisible = true
        menu.findItem(addProductMenuId)?.isVisible = false
        menu.findItem(chatSettingsMenuId)?.isVisible = false
        setOnMenuItemClickListener {
            if (it.itemId == notificationMenuId)
                onClick()
            return@setOnMenuItemClickListener true
        }
    }

    fun showAddProductActionMenu(onClick: () -> Unit) {
        menu.findItem(notificationMenuId)?.isVisible = false
        menu.findItem(addProductMenuId)?.isVisible = true
        menu.findItem(chatSettingsMenuId)?.isVisible = false
        setOnMenuItemClickListener {
            if (it.itemId == addProductMenuId)
                onClick()
            return@setOnMenuItemClickListener true
        }
    }

    fun showChatSettingsActionMenu(onClick: () -> Unit) {
        menu.findItem(notificationMenuId)?.isVisible = false
        menu.findItem(addProductMenuId)?.isVisible = false
        menu.findItem(chatSettingsMenuId)?.isVisible = true
        setOnMenuItemClickListener {
            if (it.itemId == chatSettingsMenuId)
                onClick()
            return@setOnMenuItemClickListener true
        }
    }

    fun hideAllActionMenu() {
        menu.findItem(notificationMenuId)?.isVisible = false
        menu.findItem(addProductMenuId)?.isVisible = false
        menu.findItem(chatSettingsMenuId)?.isVisible = false
    }

    fun showBadge() {
        val icon = getIcon()
        val badge = getBadgeDrawable()

        badge.showBadge()
        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_dot, badge)
    }

    fun removeBadge() {
        val icon = getIcon()
        val badge = getBadgeDrawable()

        badge.removeBadge()
        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_dot, badge)
    }

    private fun getIcon(): LayerDrawable? {
        val menuIcon = menu.findItem(notificationMenuId).icon
        val icon: LayerDrawable? = if (menuIcon is LayerDrawable) menuIcon else null
        return icon
    }

    private fun getBadgeDrawable(): NotificationDotBadge {
        val icon = getIcon()

        // Reuse drawable if exist
        val reuse: Drawable? = icon?.findDrawableByLayerId(R.id.ic_dot)
        return if (reuse != null && reuse is NotificationDotBadge) {
            reuse
        } else {
            NotificationDotBadge(context)
        }
    }
}