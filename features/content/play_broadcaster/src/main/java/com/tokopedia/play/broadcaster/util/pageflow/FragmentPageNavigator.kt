package com.tokopedia.play.broadcaster.util.pageflow

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play_common.util.extension.commit
import com.tokopedia.play_common.util.extension.compatTransitionName

/**
 * Created by jegul on 26/04/21
 */
class FragmentPageNavigator(
        private val fragmentManager: FragmentManager
) {

    fun navigate(
            @IdRes container: Int,
            fragmentClass: Class<out Fragment>,
            extras: Bundle,
            sharedElements: List<View>
    ) {
        fragmentManager.commit {
            sharedElements.forEach {
                val transitionName = it.compatTransitionName
                if (transitionName != null) addSharedElement(it, transitionName)
            }

            if (sharedElements.isNotEmpty()) setReorderingAllowed(true)

            replace(
                    container,
                    fragmentClass,
                    extras
            )

            addToBackStack(fragmentClass.name)
        }
    }
}