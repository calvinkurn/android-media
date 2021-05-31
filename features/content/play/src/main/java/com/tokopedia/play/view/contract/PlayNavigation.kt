package com.tokopedia.play.view.contract

/**
 * Created by jegul on 10/06/20
 */
interface PlayNavigation {

    fun onBackPressed(isSystemBack: Boolean)

    fun navigateToNextPage()

    fun canNavigateNextPage(): Boolean

    fun requestEnableNavigation()

    fun requestDisableNavigation()
}