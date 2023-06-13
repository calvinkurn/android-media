package com.tokopedia.product.detail.common

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

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
    tag: String,
    fragmentFactory: () -> DialogFragment
) {
    // find dialog to fragment manager, if not found so invoke to fragment factory
    val dialog = (fragmentManager.findFragmentByTag(tag) as? DialogFragment)
        ?: fragmentFactory.invoke()

    // has already been saved by its host
    if (!dialog.isStateSaved && !dialog.isAdded) {
        runCatching {
            dialog.showNow(fragmentManager, tag)
        }.onFailure {
            runCatching {
                dialog.show(fragmentManager, tag)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}
