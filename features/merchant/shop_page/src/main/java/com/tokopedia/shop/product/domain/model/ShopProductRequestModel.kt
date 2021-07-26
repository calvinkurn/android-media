package com.tokopedia.shop.product.domain.model

import android.text.TextUtils
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.constant.ShopParamApiConstant
import java.util.*

/**
 * Created by nathan on 2/15/18.
 */
class ShopProductRequestModel(val shopId: String, // Additional param for query attribute
                              val isShopClosed: Boolean, val isOfficialStore: Boolean, val page: Int, var isUseAce: Boolean,
                              val perPage: Int) {
    var keyword: String? = null
    private var etalaseId = ""
    var orderBy = 0
    var wholesale = 0

    fun getEtalaseId(): String {
        return etalaseId
    }

    fun setEtalaseId(etalaseId: String?) {
        if (etalaseId == null) {
            this.etalaseId = ""
        } else {
            this.etalaseId = etalaseId
        }
    }

    val hashMap: HashMap<String, String?>
        get() {
            val hashMap = HashMap<String, String?>()
            if (!TextUtils.isEmpty(shopId)) {
                hashMap[ShopCommonParamApiConstant.SHOP_ID] = shopId
            }
            if (!TextUtils.isEmpty(keyword)) {
                hashMap[ShopParamApiConstant.KEYWORD] = keyword
            }
            if (!TextUtils.isEmpty(etalaseId)) {
                hashMap[ShopParamApiConstant.ETALASE_ID] = etalaseId
            }
            hashMap[ShopParamApiConstant.PAGE] = page.toString()
            if (orderBy > 0) {
                hashMap[ShopParamApiConstant.ORDER_BY] = orderBy.toString()
            }
            if (perPage > 0) {
                hashMap[ShopParamApiConstant.PER_PAGE] = perPage.toString()
            }
            if (wholesale > 0) {
                hashMap[ShopParamApiConstant.WHOLESALE] = wholesale.toString()
            }
            return hashMap
        }

    fun useTome(): Boolean {
        return isShopClosed || !isUseAce
    }
}