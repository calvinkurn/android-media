package com.tokopedia.autocomplete

import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery

internal fun createIntent(queryParams: String = ""): Intent {
    return Intent(InstrumentationRegistry.getInstrumentation().targetContext, AutoCompleteActivityStub::class.java).also {
        it.data = Uri.parse(ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + queryParams)
    }
}