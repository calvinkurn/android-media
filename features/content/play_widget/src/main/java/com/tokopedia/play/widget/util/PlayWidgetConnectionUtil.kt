package com.tokopedia.play.widget.util

import android.content.Context
import android.net.ConnectivityManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by jegul on 31/08/21
 */
class PlayWidgetConnectionUtil @Inject constructor(
    @ApplicationContext context: Context
) {

    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isEligibleForHeavyDataUsage(): Boolean {
        return !connectivityManager.isActiveNetworkMetered
    }
}