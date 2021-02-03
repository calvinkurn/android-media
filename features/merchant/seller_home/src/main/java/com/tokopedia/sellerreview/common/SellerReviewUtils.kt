package com.tokopedia.sellerreview.common

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import java.util.*

/**
 * Created By @ilhamsuaib on 03/02/21
 */

object SellerReviewUtils {

    fun saveFlagHasSubmittedReview(context: Context?) {
        context?.let {
            val localCacheHandler = LocalCacheHandler(it, TkpdCache.SellerInAppReview.PREFERENCE_NAME)
            localCacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_HAS_SUBMITTED_REVIEW, true)
            localCacheHandler.applyEditor()
        }
    }

    fun saveFlagHasOpenedReviewApp(context: Context?) {
        context?.let {
            val localCacheHandler = LocalCacheHandler(it, TkpdCache.SellerInAppReview.PREFERENCE_NAME)
            localCacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_HAS_OPENED_REVIEW, true)
            localCacheHandler.putLong(TkpdCache.SellerInAppReview.KEY_LAST_REVIEW_ASKED, Date().time)
            localCacheHandler.applyEditor()
        }
    }
}