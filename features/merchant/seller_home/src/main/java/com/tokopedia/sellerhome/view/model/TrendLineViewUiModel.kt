package com.tokopedia.sellerhome.view.model

import android.os.Parcelable
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

@Parcelize
class TrendLineViewUiModel(
        val someField: String
) : BaseSellerHomeUiModel, Parcelable {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}