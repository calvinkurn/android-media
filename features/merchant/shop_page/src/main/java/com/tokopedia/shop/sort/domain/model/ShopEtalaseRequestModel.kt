package com.tokopedia.shop.sort.domain.model

import android.text.TextUtils
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import java.util.*

/**
 * Created by normansyahputa on 2/28/18.
 */
class ShopEtalaseRequestModel {
    var shopId: String? = null
    var shopDomain: String? = null
    val hashMap: HashMap<String, String?>
        get() {
            val hashMap = HashMap<String, String?>()
            if (!TextUtils.isEmpty(shopId)) {
                hashMap[ShopCommonParamApiConstant.SHOP_ID] = shopId
            }
            if (!TextUtils.isEmpty(shopDomain)) {
                hashMap[ShopCommonParamApiConstant.SHOP_DOMAIN] = shopDomain
            }
            hashMap[ShopCommonParamApiConstant.SHOW_ALL] = SHOW_ALL
            return hashMap
        }

    companion object {
        private const val SHOW_ALL = "1"
    }
}