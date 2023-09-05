@file:SuppressLint("NewApi", "DeprecatedMethod")
package com.tokopedia.contactus.inboxtickets.view.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.annotation.ChecksSdkIntAtLeast
import java.util.ArrayList

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
val isAboveAndroidT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

inline fun <reified T : Parcelable> Intent?.parcelableArrayListExtra(name: String): ArrayList<T>? {
    return if (isAboveAndroidT) {
        this?.getParcelableArrayListExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        this?.getParcelableArrayListExtra(name)
    }
}
