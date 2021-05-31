package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel

import android.os.Bundle
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction

/**
 * @author Lukas on 18/05/2020.
 */

data class HeaderDataModel(
      val homeHeaderWalletActionData: HomeHeaderWalletAction? = null,
      val tokoPointDrawerData: TokopointHomeDrawerData? = null,
      val tokopointsDrawerHomeData: TokopointsDrawer? = null,
      val tokopointsDrawerBBOHomeData: TokopointsDrawer? = null,
      val cashBackData: CashBackData? = null,
      var isPendingTokocashChecked: Boolean = false,
      val isWalletDataError: Boolean = false,
      val isTokoPointDataError: Boolean = false,
      var isUserLogin: Boolean = false,
      var homeBalanceModel: HomeBalanceModel = HomeBalanceModel()
) : HomeVisitable {

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
        if (tokopointsDrawerBBOHomeData != other.tokopointsDrawerBBOHomeData) return false
        if (cashBackData != other.cashBackData) return false
        if (isPendingTokocashChecked != other.isPendingTokocashChecked) return false
        if (isWalletDataError != other.isWalletDataError) return false
        if (isTokoPointDataError != other.isTokoPointDataError) return false
        if (isUserLogin != other.isUserLogin) return false
        if (homeBalanceModel != other.homeBalanceModel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = homeHeaderWalletActionData?.hashCode() ?: 0
        result = HASH_CODE * result + (tokoPointDrawerData?.hashCode() ?: 0)
        result = HASH_CODE * result + (tokopointsDrawerHomeData?.hashCode() ?: 0)
        result = HASH_CODE * result + (tokopointsDrawerBBOHomeData?.hashCode() ?: 0)
        result = HASH_CODE * result + (cashBackData?.hashCode() ?: 0)
        result = HASH_CODE * result + isPendingTokocashChecked.hashCode()
        result = HASH_CODE * result + isWalletDataError.hashCode()
        result = HASH_CODE * result + isTokoPointDataError.hashCode()
        result = HASH_CODE * result + isUserLogin.hashCode()
        result = HASH_CODE * result + homeBalanceModel.hashCode()
        return result
    }

    companion object {
        private const val HASH_CODE = 31
    }
}
