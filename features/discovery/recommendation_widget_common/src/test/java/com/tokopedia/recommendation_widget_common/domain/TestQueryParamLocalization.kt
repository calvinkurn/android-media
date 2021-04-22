package com.tokopedia.recommendation_widget_common.domain

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import org.junit.Test

/**
 * Created by Lukas on 3/4/21.
 */
class TestQueryParamLocalization {

    @Test
    fun testEmptyQueryParam(){
        val queryParam = ""
        val expected = "user_addressId=1&user_cityId=2&user_districtId=3&user_lat=4&user_long=5&user_postCode=6"
        val localCache = LocalCacheModel(
                address_id = "1",
                label = "label",
                city_id = "2",
                district_id = "3",
                lat = "4",
                long = "5",
                postal_code = "6"
        )
        assert(localCache.toQueryParam(queryParam) == expected)
    }
    @Test
    fun testNonEmptyQueryParam(){
        val queryParam = "productIds=123123"
        val expected = "productIds=123123&user_addressId=1&user_cityId=2&user_districtId=3&user_lat=4&user_long=5&user_postCode=6"
        val localCache = LocalCacheModel(
                address_id = "1",
                label = "label",
                city_id = "2",
                district_id = "3",
                lat = "4",
                long = "5",
                postal_code = "6"
        )
        assert(localCache.toQueryParam(queryParam) == expected)
    }
}