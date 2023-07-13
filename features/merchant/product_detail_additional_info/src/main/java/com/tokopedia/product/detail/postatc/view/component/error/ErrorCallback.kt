package com.tokopedia.product.detail.postatc.view.component.error

import com.tokopedia.product.detail.postatc.base.BaseCallbackImpl
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet

interface ErrorCallback {
    fun refreshPage()
}

class ErrorCallbackImpl(
    fragment: PostAtcBottomSheet
) : BaseCallbackImpl(fragment), ErrorCallback {
    override fun refreshPage() = with(fragmentRef.get()) {
        if (this == null) return
        initData()
    }
}
