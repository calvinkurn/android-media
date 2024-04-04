package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home.R as homeR

object BalanceUtil {
    fun getBalanceItemWidth(context: Context, totalItems: Int): Int {
        val screenWidth = DeviceScreenInfo.getScreenWidth(context)
        val dividerCount = totalItems - 1
        val totalDividerWidth = dividerCount * context.resources.getDimensionPixelSize(homeR.dimen.balance_divider_width)
        val outerPadding = 2 * context.resources.getDimensionPixelSize(homeR.dimen.balance_outer_margin)
        val totalContentWidth = screenWidth - totalDividerWidth - outerPadding
        return totalContentWidth / totalItems
    }

    fun getBalanceTextWidth(context: Context): Int {
        val containerMaxWidth = context.resources.getDimensionPixelSize(homeR.dimen.balance_item_max_width)
        val leftPadding = context.resources.getDimensionPixelSize(homeR.dimen.balance_inner_left_padding)
        val rightPadding = context.resources.getDimensionPixelSize(homeR.dimen.balance_atf3_inner_right_padding)
        val imageToTextPadding = context.resources.getDimensionPixelSize(homeR.dimen.balance_inner_content_padding)
        val paddings = leftPadding + rightPadding + imageToTextPadding
        val image = context.resources.getDimensionPixelSize(homeR.dimen.balance_atf3_image_size)
        return containerMaxWidth - paddings - image
    }
}
