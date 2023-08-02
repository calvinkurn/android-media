package com.tokopedia.logisticaddaddress.domain.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 17/11/17.
 */
@Parcelize
data class Address(
    var districtId: Long = 0,
    var districtName: String = "",
    var cityId: Long = 0,
    var cityName: String = "",
    var provinceId: Long = 0,
    var provinceName: String = "",
    var zipCodes: List<String> = arrayListOf()
) : Parcelable, Visitable<DistrictTypeFactory?> {

    override fun type(typeFactory: DistrictTypeFactory?): Int {
        return typeFactory?.type(this)!!
    }

    override fun describeContents(): Int {
        return 0
    }
}
