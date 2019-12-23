package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction

/**
 * @author anggaprasetiyo on 11/12/17.
 */

data class HeaderViewModel(
                      var homeHeaderWalletActionData: HomeHeaderWalletAction? = null,
                      var tokoPointDrawerData: TokopointHomeDrawerData? = null,
                      var tokopointsDrawerHomeData: TokopointsDrawer? = null,
                      var cashBackData: CashBackData? = null,
                      var isPendingTokocashChecked: Boolean = false,
                      var isWalletDataError: Boolean = false,
                      var isTokoPointDataError: Boolean = false,
                      var isUserLogin: Boolean = false) : Parcelable, HomeVisitable {

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
        if (b is HeaderViewModel) {
            return homeHeaderWalletActionData == b.homeHeaderWalletActionData &&
                    tokoPointDrawerData == b.tokoPointDrawerData &&
                    tokopointsDrawerHomeData == b.tokopointsDrawerHomeData &&
                    cashBackData == b.cashBackData
        }
        return false
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<HeaderViewModel> = object : Parcelable.Creator<HeaderViewModel> {
            override fun createFromParcel(`in`: Parcel): HeaderViewModel {
                return HeaderViewModel(`in`)
            }

            override fun newArray(size: Int): Array<HeaderViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
