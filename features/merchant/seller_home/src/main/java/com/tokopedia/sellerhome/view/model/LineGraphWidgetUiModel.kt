package com.tokopedia.sellerhome.view.model

import android.os.Parcelable
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

@Parcelize
class LineGraphWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val url: String,
        override val appLink: String
) : BaseWidgetUiModel, Parcelable {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}