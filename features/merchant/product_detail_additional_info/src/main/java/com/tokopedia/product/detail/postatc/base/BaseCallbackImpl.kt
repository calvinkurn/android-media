package com.tokopedia.product.detail.postatc.base

import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import java.lang.ref.WeakReference

open class BaseCallbackImpl(fragment: PostAtcBottomSheet) {
    protected val fragmentRef: WeakReference<PostAtcBottomSheet> = WeakReference(fragment)
    protected val fragment: PostAtcBottomSheet?
        get() = fragmentRef.get()
}
