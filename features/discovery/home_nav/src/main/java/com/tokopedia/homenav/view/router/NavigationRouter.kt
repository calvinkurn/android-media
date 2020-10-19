package com.tokopedia.homenav.view.router

import android.view.View
import androidx.navigation.Navigation
import com.tokopedia.homenav.R

object NavigationRouter {

    val PAGE_MAIN_NAV = 0
    val PAGE_CATEGORY = 1


    object MainNavRouter {
        private val NAVIGATION_MAIN_TO_CATEGORY = R.id.action_fragmentMain_to_fragmentCategory
        fun navigateTo(view: View, page: Int) {
            navigatePage(view, when (page) {
                PAGE_CATEGORY -> NAVIGATION_MAIN_TO_CATEGORY
                else -> 0
            })
        }
    }

    private fun navigatePage(view: View, transition: Int) {
        Navigation.findNavController(view).navigate(transition)
    }
}