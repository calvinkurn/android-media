package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class HeadlineAdsWidgetModel(
    val cpmModel: CpmModel
) : Visitable<BottomContentFactory>, WidgetTag(TAG) {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val TAG = "shopads"
    }
}
