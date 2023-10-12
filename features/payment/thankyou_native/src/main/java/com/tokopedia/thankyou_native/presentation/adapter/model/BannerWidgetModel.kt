package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory

data class BannerWidgetModel(
    val title: String = "",
    val items: List<BannerItem> = listOf()
) : Visitable<BottomContentFactory>, WidgetTag(TAG) {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val TAG = "banner"
    }
}

data class BannerItem(
    val assetUrl: String = "",
    val applink: String = "",
    val itemId: String = ""
): ImpressHolder()
