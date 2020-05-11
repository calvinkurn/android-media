package com.tokopedia.discovery2.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.data.DataItem


class Utils {
    
    companion object {
        const val TIME_ZONE = "Asia/Jakarta"
        const val TIMER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
        const val DEFAULT_BANNER_WIDTH = 800
        const val DEFAULT_BANNER_HEIGHT = 150
        const val BANNER_SUBSCRIPTION_DEFAULT_STATUS = -1


        fun extractDimension(url: String?, dimension: String = "height"): Int? {
            val uri = Uri.parse(url)
            return uri?.getQueryParameter(dimension)?.toInt()
        }
    }
}