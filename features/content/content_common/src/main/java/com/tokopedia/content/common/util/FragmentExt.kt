package com.tokopedia.content.common.util

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * Created By : Jonathan Darwin on September 14, 2022
 */
inline fun <reified T : Fragment> Fragment.getParentFragmentByInstance(): T? {
    var parent = parentFragment

    while (parent != null) {
        if (parent is T) return parent

        parent = parent.parentFragment
    }

    return null
}

@SuppressLint("DeprecatedMethod")
inline fun <reified T : Serializable> Bundle.getAsSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as T
    }
}
