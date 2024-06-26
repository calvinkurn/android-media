package com.tokopedia.content.product.picker.seller.model.etalase

import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef

/**
 * Created by jegul on 03/06/20
 */
sealed class EtalaseType {

    object User : EtalaseType()
    data class Group(val fMenu: String) : EtalaseType()
    object Unknown : EtalaseType()

    companion object {
        fun getByType(type: Int, etalaseId: String): EtalaseType {
            return when (type) {
                1 -> User
                ShopEtalaseTypeDef.ETALASE_DEFAULT -> Group(getFMenuByEtalaseId(etalaseId))
                else -> Unknown
            }
        }

        private fun getFMenuByEtalaseId(etalaseId: String): String {
            return when (etalaseId) {
                "2" -> ""
                else -> ""
            }
        }
    }
}
