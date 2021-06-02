package com.tokopedia.entertainment.home.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

object NavigationEventController {
    fun navigate(fragment: Fragment, directions: NavDirections) {
        try {
            val navController = NavHostFragment.findNavController(fragment)
            navController.navigate(directions)
        } catch (e: IllegalArgumentException) {

        }
    }
}