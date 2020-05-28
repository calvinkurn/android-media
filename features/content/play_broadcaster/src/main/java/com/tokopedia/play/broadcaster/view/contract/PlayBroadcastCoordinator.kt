package com.tokopedia.play.broadcaster.view.contract

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by jegul on 26/05/20
 */
interface PlayBroadcastCoordinator {

    fun navigateToFragment(fragmentClass: Class<out Fragment>, extras: Bundle = Bundle.EMPTY, recordBreadcrumbs: Boolean = true)

    fun setupTitle(title: String)
}