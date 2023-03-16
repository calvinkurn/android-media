package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo

object BalanceAtf2Util {
    fun getBalanceItemWidth(context: Context, totalItems: Int): Int {
        val screenWidth = DeviceScreenInfo.getScreenWidth(context)
        val dividerCount = totalItems - 1
        val totalDividerWidth = dividerCount * context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_divider_width)
        val outerPadding = 2 * context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_atf2_outer_margin)
        val totalContentWidth = screenWidth - totalDividerWidth - outerPadding
        return totalContentWidth / totalItems
    }

    fun getBalanceTextWidth(context: Context, isBalance: Boolean): Int {
        val containerMaxWidth = if (isBalance) {
            context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_atf2_item_max_width_balance)
        } else {
            context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_atf2_item_max_width_other)
        }
        val paddings = context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_inner_left_padding) +
            context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_inner_right_padding) +
            context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_inner_content_padding)
        val image = context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_image_size)
        return containerMaxWidth - paddings - image
    }
}
