package com.tokopedia.play.broadcaster.view.contract

import androidx.fragment.app.Fragment

/**
 * Created by jegul on 26/05/20
 */
interface PlayBroadcastCoordinator {

    fun navigateToFragment(fragment: Fragment)

    fun setupTitle(title: String)
}