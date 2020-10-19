package com.tokopedia.homenav.view.router

import android.view.View
import androidx.navigation.Navigation
import com.tokopedia.homenav.R

object NavigationRouter {

    val NAVIGATION_MAIN_TO_CATEGORY = R.id.action_fragmentMain_to_fragmentCategory

    fun navigateToCategoryPageFromMainNav(view: View) {
        Navigation.findNavController(view).navigate(NAVIGATION_MAIN_TO_CATEGORY)
    }
}