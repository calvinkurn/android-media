package com.tokopedia.recommendation_widget_common.ext

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

/**
 * Created by Lukas on 2/24/21.
 */
fun LocalCacheModel.toQueryParam(queryParam: String): String{
    val addressId = "user_addressId=" + this.address_id
    val cityId = "user_cityId=" + this.city_id
    val districtId = "user_districtId=" + this.district_id
    val lat = "user_lat=" + this.lat
    val long = "user_long=" + this.long
    val postCode = "user_postCode=" + this.postal_code
    val warehouse_ids = "warehouse_ids=" + this.warehouse_id
    return (if(queryParam.isEmpty()) "" else "$queryParam&") +
            "%s&%s&%s&%s&%s&%s&%s".format(addressId, cityId, districtId, lat, long, postCode, warehouse_ids)
}