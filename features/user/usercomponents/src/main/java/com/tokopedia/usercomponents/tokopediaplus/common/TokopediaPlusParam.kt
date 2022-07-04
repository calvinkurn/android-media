package com.tokopedia.usercomponents.tokopediaplus.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

class TokopediaPlusParam (
    val pageSource: String,
    val viewModeStrStoreOwner: ViewModelStoreOwner,
    val lifecycleOwner: LifecycleOwner,
)