package com.tokopedia.product.share.ekstensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet

/**
 * Created by yovi.putra on 21/07/22"
 * Project name: android-tokopedia-core
 **/

/**
 * this function will safely show fragments
 * before show the fragment, this function will ensure
 * that the fragment to be displayed is not in the saved state
 */
fun showImmediately(
    fragmentManager: FragmentManager,
    parent: Fragment,
    screenshotDetector: ScreenshotDetector? = null,
    fragmentFactory: () -> UniversalShareBottomSheet
) {
    // find dialog to fragment manager, if not found so invoke to fragment factory
    val tag = UniversalShareBottomSheet::class.java.simpleName
    val dialog = (fragmentManager.findFragmentByTag(tag) as? UniversalShareBottomSheet)
        ?: fragmentFactory.invoke()

    // has already been saved by its host
    if (!dialog.isStateSaved) {
        try {
            dialog.show(fragmentManager, parent, screenshotDetector)
        } catch (e: Throwable) {

        }
    }
}