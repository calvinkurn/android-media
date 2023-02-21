package com.tokopedia.discovery.common.microinteraction.navtoolbar

import android.content.Context
import androidx.fragment.app.Fragment
import com.tokopedia.discovery.common.microinteraction.SearchBarMicroInteractionProvider

private class NavToolbarMicroInteractionProvider(
    contextProvider: (() -> Context?),
): SearchBarMicroInteractionProvider<NavToolbarMicroInteraction>(contextProvider) {

    override fun create(context: Context) = NavToolbarMicroInteraction()
}

fun Fragment.navToolbarMicroInteraction(): Lazy<NavToolbarMicroInteraction?> =
    NavToolbarMicroInteractionProvider { context }
