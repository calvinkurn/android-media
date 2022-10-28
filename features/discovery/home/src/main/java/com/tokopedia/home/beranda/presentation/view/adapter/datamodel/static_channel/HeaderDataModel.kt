package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel

import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
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
) {
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
}
