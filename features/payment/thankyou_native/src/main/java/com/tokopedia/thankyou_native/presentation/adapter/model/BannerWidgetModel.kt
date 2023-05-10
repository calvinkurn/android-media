package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory
import com.tokopedia.thankyou_native.presentation.views.widgettag.WidgetTag

data class BannerWidgetModel(
    val title: String = "",
    val items: List<BannerItem> = listOf()
) : Visitable<BottomContentFactory>, WidgetTag {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    override val tag: String = TAG

    companion object {
        const val TAG = "banner"
    }
}

data class BannerItem(
    val assetUrl: String = "",
    val applink: String = ""
)
