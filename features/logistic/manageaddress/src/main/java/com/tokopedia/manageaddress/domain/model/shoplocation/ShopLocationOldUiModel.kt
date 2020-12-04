package com.tokopedia.manageaddress.domain.model.shoplocation

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.adapter.ShopLocationOldTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopLocationOldUiModel(
        val id: String = "",
        var name: String = "",
        var address: String = "",
        var districtId: Int = -1,
        var districtName: String = "",
        var cityId: Int = -1,
        var cityName: String = "",
        var stateId: Int = -1,
        var stateName: String = "",
        var postalCode: Int = -1,
        var email: String = "",
        var phone: String = "",
        var fax: String = ""
) : Visitable<ShopLocationOldTypeFactory>, Parcelable {
    override fun type(typeFactory: ShopLocationOldTypeFactory) = typeFactory.type(this)
}