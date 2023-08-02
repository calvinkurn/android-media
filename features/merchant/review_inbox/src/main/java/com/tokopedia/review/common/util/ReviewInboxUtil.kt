package com.tokopedia.review.common.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.unifycomponents.BottomSheetUnify

object ReviewInboxUtil {

    fun routeToWebview(context: Context, bottomSheet: BottomSheetUnify?, url: String): Boolean {
        val webviewUrl = String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
        bottomSheet?.dismiss()
        return RouteManager.route(context, webviewUrl)
    }
}

fun getReviewStar(ratingCount: Int): Int {
    return when (ratingCount) {
        ReviewInboxConstants.RATING_1 -> {
            com.tokopedia.reviewcommon.R.drawable.review_ic_rating_star_one
        }
        ReviewInboxConstants.RATING_2 -> {
            com.tokopedia.reviewcommon.R.drawable.review_ic_rating_star_two
        }
        ReviewInboxConstants.RATING_3 -> {
            com.tokopedia.reviewcommon.R.drawable.review_ic_rating_star_three
        }
        ReviewInboxConstants.RATING_4 -> {
            com.tokopedia.reviewcommon.R.drawable.review_ic_rating_star_four
        }
        ReviewInboxConstants.RATING_5 -> {
            com.tokopedia.reviewcommon.R.drawable.review_ic_rating_star_five
        }
        else -> {
            com.tokopedia.reviewcommon.R.drawable.review_ic_rating_star_zero
        }
    }
}
