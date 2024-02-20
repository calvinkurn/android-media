package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.kotlin.util.lazyThreadSafetyNone

internal fun <Callback : BaseComponentCallback<*>> callback(
    creation: () -> Callback
): Lazy<Callback> = lazyThreadSafetyNone {
    creation.invoke()
}
