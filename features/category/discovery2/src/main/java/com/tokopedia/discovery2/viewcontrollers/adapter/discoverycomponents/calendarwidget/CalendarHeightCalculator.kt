package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.content.Context
import com.tokopedia.discovery2.Constant.Calendar.CAROUSEL
import com.tokopedia.discovery2.Constant.Calendar.DOUBLE
import com.tokopedia.discovery2.Constant.Calendar.TRIPLE
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend fun List<DataItem>?.getMaxHeightForCarouselView(context: Context?, coroutineDispatcher: CoroutineDispatcher, calendarLayout : String?): Int {
    if (this == null || context == null) return 0

    return withContext(coroutineDispatcher) {
        val calendarCardHeightList = mutableListOf<Int>()

        forEach { dataItem ->
            val calendarDate = getCalendarDateHeight(context)
            val calendarTitleImage = getCalendarTitleImageHeight(context, dataItem.titleLogoUrl, dataItem.title)
            val calendarTitle = getCalendarTitleHeight(context, dataItem.title)
            val calendarDescription = getCalendarDescriptionHeight(context)
            val calendarButton = getCalendarButtonHeight(context, dataItem.buttonApplink)
            val calendarImage = getCalendarImageHeight(context, dataItem.imageUrl, calendarLayout)
            val padding = context.resources.getDimensionPixelSize(R.dimen.dp_4)

            calendarCardHeightList.add(
                        calendarDate +
                        calendarTitleImage +
                        calendarTitle +
                        calendarDescription +
                        calendarImage +
                        calendarButton +
                        padding
            )
        }

        calendarCardHeightList.maxOrNull()?.toInt() ?: 0
    }
}
private fun getCalendarDateHeight(context: Context): Int {
    return context.resources.getDimensionPixelSize(R.dimen.dp_32)
}

private fun getCalendarTitleImageHeight(context: Context, titleLogoUrl: String?, title: String?): Int {
    return if (!titleLogoUrl.isNullOrEmpty() && title.isNullOrEmpty())
            context.resources.getDimensionPixelSize(R.dimen.dp_80)
    else 0
}
private fun getCalendarTitleHeight(context: Context, title: String?): Int {
    return if (!title.isNullOrEmpty())
        context.resources.getDimensionPixelSize(R.dimen.dp_64)
    else 0
}

private fun getCalendarDescriptionHeight(context: Context): Int {
    return context.resources.getDimensionPixelSize(R.dimen.dp_36)
}

fun getCalendarButtonHeight(context: Context, buttonApplink: String?): Int {
    return if (!buttonApplink.isNullOrEmpty())
        context.resources.getDimensionPixelSize(R.dimen.dp_24)
    else 0
}

fun getCalendarImageHeight(context: Context, imageUrl: String?, calendarLayout: String?): Int {
    return if (!imageUrl.isNullOrEmpty()) {
        when(calendarLayout) {
            DOUBLE -> context.resources.getDimensionPixelSize(R.dimen.dp_100)
            TRIPLE -> context.resources.getDimensionPixelSize(R.dimen.dp_96)
            CAROUSEL -> context.resources.getDimensionPixelSize(R.dimen.dp_100)
            else -> 0
        }
    }
    else 0
}
