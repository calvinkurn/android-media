package com.tokopedia.home.beranda.data.mapper

import com.google.gson.Gson
import com.tokopedia.home.beranda.data.model.RedeemCouponRedirection

object ClaimCouponMapper {

    /**
     * Claim coupon mutation returns a jsonMetaData which the entry point url for redirection.
     *
     * This mapper will parse the jsonMetaData and extracting out the url and appLink (if exist).
     * The extraction will be executed once the coupon claimed.
     *
     * @sample:
     * "{"url": "https://staging.tokopedia.com/christin-os-1/voucher/38317?page_source=pdp",
     * "app_link": "tokopedia://shop/6550307/voucher/38317?page_source=pdp"}"
     *
     * @return: [RedeemCouponRedirection]
     */
    fun extractMetaData(json: String?): RedeemCouponRedirection {
        if (json == null) return RedeemCouponRedirection("", "")

        return try {
            return Gson().fromJson(json, RedeemCouponRedirection::class.java)
        } catch (_: Throwable) {
            RedeemCouponRedirection("", "")
        }
    }
}
