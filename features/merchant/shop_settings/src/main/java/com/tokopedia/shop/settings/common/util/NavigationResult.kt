package com.tokopedia.shop.settings.common.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.getNavigationResult(key: String = "result") = findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(key)

fun Fragment.setNavigationResult(result: Bundle, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun Fragment.removeNavigationResult(key: String = "result") {
    findNavController().currentBackStackEntry?.savedStateHandle?.remove<Bundle>(key)
}