package com.tokopedia.shop.settings.common.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController

object NavigationController {
    fun navigate(fragment: Fragment, directions: NavDirections) {
        try {
            val navController = findNavController(fragment)
            navController.navigate(directions)
        } catch (e: IllegalArgumentException) {
            ShopSettingsErrorHandler.logMessage(e.message ?: "")
            ShopSettingsErrorHandler.logExceptionToCrashlytics(e.fillInStackTrace())
        }
    }
}
