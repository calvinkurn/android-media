package com.tokopedia.report.view.util.extensions

import androidx.fragment.app.Fragment

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

fun Fragment.argsString(key: String, default: String = "") = lazy {
    arguments?.getString(key, default) ?: default
}