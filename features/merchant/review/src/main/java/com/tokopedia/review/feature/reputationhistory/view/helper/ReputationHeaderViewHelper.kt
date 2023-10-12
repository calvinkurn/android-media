package com.tokopedia.review.feature.reputationhistory.view.helper

import android.view.View
import com.tokopedia.media.loader.loadImage

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
        calendarIcon?.loadImage(CALENDAR_IMAGE_URL) {
            setErrorDrawable(com.tokopedia.utils.R.drawable.ic_loading_placeholder)
        }
    }

    init {
        stopLoading()
    }
}