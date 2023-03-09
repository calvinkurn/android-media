package com.tokopedia.media.picker.utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
val isAboveAndroidT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

@SuppressLint("NewApi", "DeprecatedMethod")
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? {
    return if (isAboveAndroidT) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key) as? T
    }
}
