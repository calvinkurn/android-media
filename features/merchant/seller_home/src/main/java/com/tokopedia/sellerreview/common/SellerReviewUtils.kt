package com.tokopedia.sellerreview.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

    fun getConnectionStatus(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo // Deprecated in 29
            activeNetwork != null && activeNetwork.isConnectedOrConnecting // // Deprecated in 28
        }
    }
}