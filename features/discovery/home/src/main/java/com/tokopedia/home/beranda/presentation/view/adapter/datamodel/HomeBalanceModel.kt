package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

data class HomeBalanceModel (
        val balanceDrawerItemModels: Map<Int, BalanceDrawerItemModel> = hashMapOf(),
        val balanceType: Int = TYPE_STATE_1
) : HomeVisitable {
    companion object {
        // State 1: Ovo, Coupon, Bebas Ongkir
        const val TYPE_STATE_1 = 1

        // State 2: Tokopoints, Ovo, Bebas Ongkir
        const val TYPE_STATE_2 = 2

        // State 3: Tokopoints, Coupon, Bebas Ongkir
        const val TYPE_STATE_3 = 3

        private const val HASH_CODE = 39
    }
    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun setTrackingData(trackingData: Map<String, Any>) {

    }

    override fun getTrackingData(): Map<String, Any>? {
        return null
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return null
    }

    override fun setTrackingDataForCombination(`object`: List<Any>) {

    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {

    }

    override fun isCache(): Boolean {
        return false
    }

    override fun visitableId(): String {
        return "HomeBalanceModel"
    }

    override fun equalsWith(b: Any?): Boolean {
        return equals(b)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeBalanceModel

        if (balanceDrawerItemModels != other.balanceDrawerItemModels) return false

        return true
    }

    override fun hashCode(): Int {
        var result = balanceDrawerItemModels?.hashCode() ?: 0
        return result
    }
}

// free ongkir, tokopoint, coupon

//simple logic :
//title : check tag title, then check text title
//description : check tag description, then check text description
data class BalanceDrawerItemModel(
        val applink: String = "",
        val iconImageUrl: String = "",
        val defaultIconRes: Int? = null,
        val balanceTitleTextAttribute: BalanceTextAttribute? = null,
        val balanceSubTitleTextAttribute: BalanceTextAttribute? = null,
        val balanceTitleTagAttribute: BalanceTagAttribute? = null,
        val balanceSubTitleTagAttribute: BalanceTagAttribute? = null,
        val drawerItemType: Int = TYPE_TOKOPOINT
) {
    companion object {
        const val TYPE_TOKOPOINT = 1

        const val TYPE_FREE_ONGKIR = 2

        const val TYPE_COUPON = 3
    }
}

data class BalanceTextAttribute(
        val colour: String = "",
        val text: String = "",
        val isBold: Boolean = false
)

data class BalanceTagAttribute(
        val text: String = "",
        val backgroundColour: String = ""
)