package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction

/**
 * @author anggaprasetiyo on 11/12/17.
 */

data class HeaderDataModel(
                      var homeHeaderWalletActionData: HomeHeaderWalletAction? = null,
                      var tokoPointDrawerData: TokopointHomeDrawerData? = null,
                      var tokopointsDrawerHomeData: TokopointsDrawer? = null,
                      var cashBackData: CashBackData? = null,
                      var isPendingTokocashChecked: Boolean = false,
                      var isWalletDataError: Boolean = false,
                      var isTokoPointDataError: Boolean = false,
                      var isUserLogin: Boolean = false
) : Parcelable, HomeVisitable {

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    protected constructor(`in`: Parcel) : this() {
        homeHeaderWalletActionData = `in`.readParcelable(HomeHeaderWalletAction::class.java.classLoader)
        tokoPointDrawerData = `in`.readParcelable(TokopointsDrawer::class.java.classLoader)
        tokopointsDrawerHomeData = `in`.readParcelable(TokopointsDrawerHomeData::class.java.classLoader)
        cashBackData = `in`.readParcelable(CashBackData::class.java.classLoader)
        isPendingTokocashChecked = `in`.readByte().toInt() != 0
        isWalletDataError = `in`.readByte().toInt() != 0
        isTokoPointDataError = `in`.readByte().toInt() != 0
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeParcelable(homeHeaderWalletActionData, i)
        parcel.writeParcelable(tokoPointDrawerData, i)
        parcel.writeParcelable(tokopointsDrawerHomeData, i)
        parcel.writeParcelable(cashBackData, i)
        parcel.writeByte((if (isPendingTokocashChecked) 1 else 0).toByte())
        parcel.writeByte((if (isWalletDataError) 1 else 0).toByte())
        parcel.writeByte((if (isTokoPointDataError) 1 else 0).toByte())
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
        return "ovoSection"
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

        other as HeaderDataModel

        if (homeHeaderWalletActionData != other.homeHeaderWalletActionData) return false
        if (tokoPointDrawerData != other.tokoPointDrawerData) return false
        if (tokopointsDrawerHomeData != other.tokopointsDrawerHomeData) return false
        if (cashBackData != other.cashBackData) return false
        if (isPendingTokocashChecked != other.isPendingTokocashChecked) return false
        if (isWalletDataError != other.isWalletDataError) return false
        if (isTokoPointDataError != other.isTokoPointDataError) return false
        if (isUserLogin != other.isUserLogin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = homeHeaderWalletActionData?.hashCode() ?: 0
        result = HASH_CODE * result + (tokoPointDrawerData?.hashCode() ?: 0)
        result = HASH_CODE * result + (tokopointsDrawerHomeData?.hashCode() ?: 0)
        result = HASH_CODE * result + (cashBackData?.hashCode() ?: 0)
        result = HASH_CODE * result + isPendingTokocashChecked.hashCode()
        result = HASH_CODE * result + isWalletDataError.hashCode()
        result = HASH_CODE * result + isTokoPointDataError.hashCode()
        result = HASH_CODE * result + isUserLogin.hashCode()
        return result
    }

    companion object {
        private const val HASH_CODE = 31
        @JvmField
        val CREATOR: Parcelable.Creator<HeaderDataModel> = object : Parcelable.Creator<HeaderDataModel> {
            override fun createFromParcel(`in`: Parcel): HeaderDataModel {
                return HeaderDataModel(`in`)
            }

            override fun newArray(size: Int): Array<HeaderDataModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
