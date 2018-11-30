package com.tokopedia.kelontongapp.helper

import android.content.Context
import android.net.ConnectivityManager

import java.net.InetAddress

/**
 * Created by meta on 23/10/18.
 */
object ConnectionManager {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}
