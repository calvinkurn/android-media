package com.tokopedia.home_component.productcardgridcarousel.viewHolder.calculator

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.SpecialReleaseTimerCopyGenerator
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel
import com.tokopedia.home_component.util.DateHelper
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*

@SuppressLint("ResourcePackage")
suspend fun List<CarouselSpecialReleaseDataModel>?.calculateHeight(
    context: Context?, coroutineDispatcher: CoroutineDispatcher
): Int? {
    if (this == null || context == null) return 0

    val cardHeightList = mutableListOf<Int>()

    val cardPaddingBottom =
        context.resources.getDimensionPixelSize(R.dimen.special_release_padding_bottom)
    val productImageHeight =
        context.resources.getDimensionPixelSize(R.dimen.special_release_shop_product_height)
    val shopImageHeight =
        context.resources.getDimensionPixelSize(R.dimen.special_release_shop_image_offset_height)
    val shopNameHeight =
        context.resources.getDimensionPixelSize(R.dimen.special_release_shop_name_height)
    val tagHeight =
        context.resources.getDimensionPixelSize(R.dimen.special_release_tag_height)
    val priceHeight =
        context.resources.getDimensionPixelSize(R.dimen.special_release_price_height)
    val timerHeight =
        context.resources.getDimensionPixelSize(R.dimen.special_release_timer_height)

    forEach { specialReleaseModel ->
        var overallHeight = cardPaddingBottom + productImageHeight + shopImageHeight

        if (specialReleaseModel.grid.shop.shopName.isNotEmpty()) {
            overallHeight+=shopNameHeight
        }

        if (specialReleaseModel.grid.benefit.value.isNotEmpty()) {
            overallHeight+=tagHeight
        }

        if (specialReleaseModel.grid.price.isNotEmpty()) {
            overallHeight+=priceHeight
        }

        if (specialReleaseModel.grid.expiredTime.isNotEmpty()
            || !timerCalculationIsEmpty(
                specialReleaseModel.grid.expiredTime,
                specialReleaseModel.channel.channelConfig.serverTimeOffset)) {
            overallHeight+=timerHeight
        }
        cardHeightList.add(overallHeight)
    }
    return cardHeightList.maxOrNull()?.toInt()
}

private fun timerCalculationIsEmpty(expiredTime: String, serverTimeOffset: Long): Boolean {
    val expiredTimeDate = DateHelper.getExpiredTime(expiredTime)
    return if (expiredTime.isNotEmpty() && !DateHelper.isExpired(serverTimeOffset, expiredTimeDate)) {
        val timerCopy = SpecialReleaseTimerCopyGenerator.getCopy(
            expiredTimeDate = expiredTimeDate,
            currentTimeDate = Date(),
            offset = serverTimeOffset
        )
        timerCopy.isEmpty()
    } else {
        true
    }
}