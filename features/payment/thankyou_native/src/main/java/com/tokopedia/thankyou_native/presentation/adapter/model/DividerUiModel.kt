package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory

class DividerUiModel: Visitable<BottomContentFactory> {
    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }
}
