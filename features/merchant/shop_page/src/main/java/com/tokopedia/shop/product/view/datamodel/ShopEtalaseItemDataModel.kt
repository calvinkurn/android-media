package com.tokopedia.shop.product.view.datamodel

import android.os.Parcelable
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseRules
import kotlinx.android.parcel.Parcelize

/**
 * Created by normansyahputa on 2/28/18.
 */

@Parcelize
data class ShopEtalaseItemDataModel(
        val etalaseId: String = "",
        val alias: String = "",
        val etalaseName: String = "",
        @ShopEtalaseTypeDef val type: Int = ShopEtalaseTypeDef.ETALASE_DEFAULT,
        val etalaseBadge: String = "",
        val etalaseCount: Long = 0,
        val highlighted: Boolean = false,
        val etalaseRules: List<ShopEtalaseRules> = listOf()
) : Parcelable
