package com.tokopedia.developer_options.utils

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache

/**
 * Created By @ilhamsuaib on 04/02/21
 */

class SellerInAppReview {

    companion object {
        @JvmStatic
        fun setSellerAppReviewDebuggingEnabled(context: Context, boolean: Boolean) {
            val cacheHandler = LocalCacheHandler(context, TkpdCache.SellerInAppReview.PREFERENCE_NAME)
            cacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_IS_ALLOW_APP_REVIEW_DEBUGGING, boolean)
            cacheHandler.applyEditor()
        }

        @JvmStatic
        fun getSellerAppReviewDebuggingEnabled(context: Context): Boolean {
            val cacheHandler = LocalCacheHandler(context, TkpdCache.SellerInAppReview.PREFERENCE_NAME)
            return cacheHandler.getBoolean(TkpdCache.SellerInAppReview.KEY_IS_ALLOW_APP_REVIEW_DEBUGGING, false)
        }
    }
}