package com.tokopedia.review.common.util

import android.content.Context
import android.util.TypedValue
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.*

object ReviewUtil {

    fun convertMapObjectToString(map: HashMap<String, Any>): HashMap<String, String>? {
        val newMap = HashMap<String, String>()
        for ((key, value) in map) {
            newMap[key] = value.toString()
        }
        return newMap
    }

    fun DptoPx(context: Context, dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    }

    fun routeToWebview(context: Context, bottomSheet: BottomSheetUnify?, url: String): Boolean {
        val webviewUrl = String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
        bottomSheet?.dismiss()
        return RouteManager.route(context, webviewUrl)
    }
}

fun getReviewStar(ratingCount: Int): Int {
    return when (ratingCount) {
        1 -> {
            R.drawable.review_ic_rating_star_one
        }
        2 -> {
            R.drawable.review_ic_rating_star_two
        }
        3 -> {
            R.drawable.review_ic_rating_star_three
        }
        4 -> {
            R.drawable.review_ic_rating_star_four
        }
        5 -> {
            R.drawable.review_ic_rating_star_five
        }
        else -> {
            R.drawable.review_ic_rating_star_zero
        }
    }
}
