package com.tokopedia.deals.search.model.visitor

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.search.ui.typefactory.DealsSearchTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoucherModel (
        var voucherId: String = "",
        var voucherName: String = "",
        var merchantName: String = "",
        var voucherThumbnail: String = "",
        var realPrice: String = "0",
        var mrp: String = "0",
        var discountText: String = "",
        var appUrl: String = "",
        var position: Int = 0
): Parcelable, Visitable<DealsSearchTypeFactory> {

    override fun type(typeFactoryDeals: DealsSearchTypeFactory): Int {
        return typeFactoryDeals.type(this)
    }

}