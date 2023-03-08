package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.util.toDpInt

object BalanceAtf2Util {
    private val DIVIDER_WIDTH = 1f.toDpInt()

    fun getBalanceItemWidth(context: Context, totalItems: Int) : Int {
        val screenWidth = DeviceScreenInfo.getScreenWidth(context)
        val dividerCount = totalItems - 1
        val totalDividerWidth = dividerCount * DIVIDER_WIDTH
        val outerPadding = 2 * context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_atf2_outer_margin)
        val totalContentWidth = screenWidth - totalDividerWidth - outerPadding
        return totalContentWidth / totalItems
    }
}
