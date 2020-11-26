package com.tokopedia.shop.settings.address.data

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.settings.address.view.adapter.ShopLocationTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopLocationViewModel(
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
) : Visitable<ShopLocationTypeFactory>, Parcelable {
    override fun type(typeFactory: ShopLocationTypeFactory) = typeFactory.type(this)
}