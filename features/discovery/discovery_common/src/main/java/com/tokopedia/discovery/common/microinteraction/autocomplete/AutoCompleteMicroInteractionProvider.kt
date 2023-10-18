package com.tokopedia.discovery.common.microinteraction.autocomplete

import android.app.Activity
import android.content.Context
import android.view.View
import com.tokopedia.discovery.common.microinteraction.SearchBarMicroInteractionProvider

private class AutoCompleteMicroInteractionProvider(
    contextProvider: () -> Context?,
): SearchBarMicroInteractionProvider<AutoCompleteMicroInteraction>(contextProvider) {

    override fun create(context: Context): AutoCompleteMicroInteraction =
        AutoCompleteMicroInteraction(context)
}

fun Activity.autoCompleteMicroInteraction(): Lazy<AutoCompleteMicroInteraction?> =
    AutoCompleteMicroInteractionProvider { this }
