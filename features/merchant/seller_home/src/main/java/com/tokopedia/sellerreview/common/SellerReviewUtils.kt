package com.tokopedia.sellerreview.common

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import java.util.*

/**
 * Created By @ilhamsuaib on 03/02/21
 */

object SellerReviewUtils {

    fun saveFlagHasOpenedReviewApp(context: Context?, userId: String) {
        context?.let {
            val localCacheHandler = LocalCacheHandler(it, TkpdCache.SellerInAppReview.PREFERENCE_NAME)
            localCacheHandler.putBoolean(getUniqueKey(TkpdCache.SellerInAppReview.KEY_HAS_OPENED_REVIEW, userId), true)
            localCacheHandler.putLong(getUniqueKey(TkpdCache.SellerInAppReview.KEY_LAST_REVIEW_ASKED, userId), Date().time)
            localCacheHandler.applyEditor()
        }
    }

    fun getUniqueKey(key: String, userId: String): String = key + userId
}