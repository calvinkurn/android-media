package com.tokopedia.play.broadcaster.view.contract

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by mzennis on 29/05/20.
 */
interface PlayBaseCoordinator {

    fun<T: Fragment> navigateToFragment(
            fragmentClass: Class<out T>,
            extras: Bundle = Bundle.EMPTY,
            sharedElements: List<View> = emptyList(),
            onFragment: (T) -> Unit = {}
    )

}