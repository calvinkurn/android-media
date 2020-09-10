package com.tokopedia.product.addedit.common.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.findNavController

object NavigationController {
    fun navigate(fragment: Fragment, directions: NavDirections) {
        try {
            val navController = findNavController(fragment)
            navController.navigate(directions)
        } catch (e: IllegalArgumentException) {
            AddEditProductErrorHandler.logExceptionToCrashlytics(e.fillInStackTrace())
        }
    }
}