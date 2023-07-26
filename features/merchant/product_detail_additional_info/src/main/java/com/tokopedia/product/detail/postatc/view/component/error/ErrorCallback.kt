package com.tokopedia.product.detail.postatc.view.component.error

import com.tokopedia.product.detail.postatc.base.PostAtcBottomSheetDelegate
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet

interface ErrorCallback {
    fun refreshPage()
}

class ErrorCallbackImpl(
    fragment: PostAtcBottomSheet
) : ErrorCallback, PostAtcBottomSheetDelegate by fragment {
    override fun refreshPage() = initData()
}
