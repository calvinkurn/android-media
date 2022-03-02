package com.tokopedia.discovery2.repository.mycoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.mycoupon.MyCouponResponse
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest
import javax.inject.Inject

open class MyCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), MyCouponRepository {

    override suspend fun getCouponData(myCouponsRequest: MyCouponsRequest): MyCouponResponse {
        return getGQLData(getGQLString(R.raw.my_coupon_gql), MyCouponResponse::class.java, setParams(myCouponsRequest))
    }

    fun setParams(myCouponsRequest: MyCouponsRequest): Map<String,Any>{
        val params: Map<String, Any>?
        params = mapOf(
                PARAM_SERVICE_ID to myCouponsRequest.serviceID,
                PARAM_CATEGORY_ID to myCouponsRequest.categoryID,
                PARAM_CATEGORY_ID_COUPON to myCouponsRequest.categoryIDCoupon,
                PARAM_PAGE to myCouponsRequest.page,
                PARAM_LIMIT to myCouponsRequest.limit,
                PARAM_INCLUDE_EXTRA_INFO to myCouponsRequest.includeExtraInfo,
                PARAM_API_VERSION to myCouponsRequest.apiVersion,
                PARAM_IS_GET_PROMO_INFO to myCouponsRequest.isGetPromoInfo,
                PARAM_SOURCE to myCouponsRequest.source,
                PARAM_CLIENT_ID to myCouponsRequest.clientID,
                PARAM_CATALOG_SLUGS to myCouponsRequest.catalogSlugs
        )

        return params
    }

    companion object{
        private const val PARAM_SERVICE_ID = "serviceID"
        private const val PARAM_CATEGORY_ID = "categoryID"
        private const val PARAM_CATEGORY_ID_COUPON = "categoryIDCoupon"
        private const val PARAM_PAGE = "page"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_INCLUDE_EXTRA_INFO = "includeExtraInfo"
        private const val PARAM_API_VERSION = "apiVersion"
        private const val PARAM_IS_GET_PROMO_INFO = "isGetPromoInfo"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_CLIENT_ID = "clientID"
        private const val PARAM_CATALOG_SLUGS = "catalogSlugs"

    }
}


