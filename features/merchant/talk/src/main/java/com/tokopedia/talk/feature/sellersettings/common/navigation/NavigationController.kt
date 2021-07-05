package com.tokopedia.talk.feature.sellersettings.common.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController

object NavigationController {
    fun navigate(fragment: Fragment, directions: NavDirections) {
        try {
            val navController = findNavController(fragment)
            navController.navigate(directions)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    internal fun Fragment.getNavigationResult(key: String = "result") = findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(key)

    internal fun Fragment.setNavigationResult(result: Bundle, key: String = "result") {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    }

    internal fun Fragment.removeNavigationResult(key: String = "result") {
        findNavController().currentBackStackEntry?.savedStateHandle?.remove<Bundle>(key)
    }
}