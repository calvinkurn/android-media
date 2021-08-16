package com.tokopedia.review.feature.reputationhistory.view.helper

import android.view.View
import com.tokopedia.kotlin.extensions.R
import com.tokopedia.kotlin.extensions.view.loadImage
import java.util.*

/**
 * Created by normansyahputa on 3/17/17.
 */
class ReputationHeaderViewHelper(itemView: View) : GMStatHeaderViewHelper(
    itemView, true
) {
    fun setData(startDate: String, endDate: String) {
        calendarRange?.text = StringBuilder("$startDate - $endDate")
        setImageIcon()
        stopLoading()
    }

    override fun setImageIcon() {
        calendarIcon?.loadImage(CALENDAR_IMAGE_URL, R.drawable.ic_loading_placeholder)
    }

    override val maxDateCalendar: Calendar
        get() {
            val maxCalendar = Calendar.getInstance()
            maxCalendar[Calendar.HOUR_OF_DAY] = 23
            maxCalendar[Calendar.MINUTE] = 59
            maxCalendar[Calendar.SECOND] = 59
            return maxCalendar
        }

    init {
        stopLoading()
    }
}