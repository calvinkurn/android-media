package com.tokopedia.loginregister.shopcreation.domain.param

/**
 * Created by Ade Fulki on 2020-02-06.
 * ade.hadian@tokopedia.com
 */

data class ShopInfoParam @JvmOverloads constructor(
        val shopID: Int = 0
) {

    fun toMap(): Map<String, Any> = mapOf(
            PARAM_SHOP_ID to shopID
    )

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
    }
}