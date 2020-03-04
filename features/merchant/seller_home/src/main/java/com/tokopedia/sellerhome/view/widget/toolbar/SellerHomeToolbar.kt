package com.tokopedia.sellerhome.view.widget.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.tokopedia.sellerhome.R

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class SellerHomeToolbar(context: Context?, attrs: AttributeSet?) : Toolbar(context, attrs) {

    private var onNotifClickListener: () -> Unit = {}
    private val notificationMenuId = R.id.menu_sah_notification

    init {
        inflateMenu(R.menu.sah_menu_toolbar_notification)

        setOnMenuItemClickListener {
            if (it.itemId == notificationMenuId)
                onNotifClickListener()
            return@setOnMenuItemClickListener true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 12f
        }
    }

    fun setOnNotificationClickListener(action: () -> Unit) {
        this.onNotifClickListener = action
    }

    fun showBadge() {
        val icon = getIcon()
        val badge = getBadgeDrawable()

        badge.showBadge()
        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_group_count, badge)
    }

    fun hideBadge() {
        val icon = getIcon()
        val badge = getBadgeDrawable()

        badge.removeBadge()
        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_group_count, badge)
    }

    private fun getIcon(): LayerDrawable? {
        val menuIcon = menu.findItem(notificationMenuId).icon
        val icon: LayerDrawable? = if (menuIcon is LayerDrawable) menuIcon else null
        return icon
    }

    private fun getBadgeDrawable(): CountDrawable {
        val icon = getIcon()

        // Reuse drawable if exist
        val reuse: Drawable? = icon?.findDrawableByLayerId(R.id.ic_group_count)
        return if (reuse != null && reuse is CountDrawable) {
            reuse
        } else {
            CountDrawable(context)
        }
    }
}