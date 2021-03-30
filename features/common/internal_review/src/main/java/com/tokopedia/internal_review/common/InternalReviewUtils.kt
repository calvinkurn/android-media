package com.tokopedia.internal_review.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import java.util.*

/**
 * Created By @ilhamsuaib on 03/02/21
 */

object InternalReviewUtils {

    fun saveFlagHasOpenedReviewApp(context: Context?, userId: String) {
        context?.let {
            val localCacheHandler = LocalCacheHandler(it, Const.SharedPrefKey.PREFERENCE_NAME)
            localCacheHandler.putBoolean(getUniqueKey(Const.SharedPrefKey.KEY_HAS_OPENED_REVIEW, userId), true)
            localCacheHandler.putLong(getUniqueKey(Const.SharedPrefKey.KEY_LAST_REVIEW_ASKED, userId), Date().time)
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

    fun dismissSoftKeyboard(fragment: Fragment) {
        fragment.activity?.run {
            fragment.view?.rootView?.windowToken?.let {
                val imm: InputMethodManager? = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(fragment.view?.rootView?.windowToken, 0)
            }
        }
    }
}