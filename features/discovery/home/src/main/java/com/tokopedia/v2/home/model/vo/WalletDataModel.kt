package com.tokopedia.v2.home.model.vo

import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.model.pojo.wallet.Tokopoint
import java.util.*

data class WalletDataModel(
        val walletBalance: WalletAction = WalletAction(),
        val tokopoint: TokopointAction = TokopointAction()
) : ModelViewType {
    override fun getPrimaryKey(): Int {
        return 3
    }

    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return other is WalletDataModel && walletBalance.walletType == other.walletBalance.walletType
                && walletBalance.balance == other.walletBalance.balance
                && walletBalance.visibleActionButton == other.walletBalance.visibleActionButton
                && walletBalance.pointBalance == other.walletBalance.pointBalance
                && walletBalance.linked == other.walletBalance.linked
                && walletBalance.labelActionButton == other.walletBalance.labelActionButton
                && walletBalance.cashBalance == other.walletBalance.cashBalance
                && walletBalance.status == other.walletBalance.status
                && tokopoint == other.tokopoint
    }

    data class WalletAction(
            val labelTitle: String = "",
            val balance: String = "",
            val redirectUrlBalance: String = "",
            val appLinkBalance: String = "",
            val typeAction: Int = 0,
            val visibleActionButton: Boolean = false,
            val labelActionButton: String = "",
            val appLinkActionButton: String = "",
            val linked: Boolean = false,
            val abTags: List<String> = ArrayList(),
            val pointBalance: String = "",
            val rawPointBalance: Int = 0,
            val cashBalance: String = "",
            val rawCashBalance: Int = 0,
            val walletType: String = "",
            val showAnnouncement: Boolean = false,
            val status: Resource.Status = Resource.Status.LOADING,
            val pendingTokocash: Boolean = false,
            val cashBackData: PendingCashback = PendingCashback()
    ){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as WalletAction

            if (labelTitle != other.labelTitle) return false
            if (balance != other.balance) return false
            if (redirectUrlBalance != other.redirectUrlBalance) return false
            if (appLinkBalance != other.appLinkBalance) return false
            if (typeAction != other.typeAction) return false
            if (visibleActionButton != other.visibleActionButton) return false
            if (labelActionButton != other.labelActionButton) return false
            if (appLinkActionButton != other.appLinkActionButton) return false
            if (linked != other.linked) return false
            if (abTags != other.abTags) return false
            if (pointBalance != other.pointBalance) return false
            if (rawPointBalance != other.rawPointBalance) return false
            if (cashBalance != other.cashBalance) return false
            if (rawCashBalance != other.rawCashBalance) return false
            if (walletType != other.walletType) return false
            if (showAnnouncement != other.showAnnouncement) return false

            return true
        }

        override fun hashCode(): Int {
            var result = labelTitle.hashCode()
            result = 31 * result + balance.hashCode()
            result = 31 * result + redirectUrlBalance.hashCode()
            result = 31 * result + appLinkBalance.hashCode()
            result = 31 * result + typeAction
            result = 31 * result + visibleActionButton.hashCode()
            result = 31 * result + labelActionButton.hashCode()
            result = 31 * result + appLinkActionButton.hashCode()
            result = 31 * result + linked.hashCode()
            result = 31 * result + abTags.hashCode()
            result = 31 * result + pointBalance.hashCode()
            result = 31 * result + rawPointBalance
            result = 31 * result + cashBalance.hashCode()
            result = 31 * result + rawCashBalance
            result = 31 * result + walletType.hashCode()
            result = 31 * result + showAnnouncement.hashCode()
            return result
        }
    }

    data class TokopointAction(
            val tokopoint: Tokopoint = Tokopoint(),
            val status: Resource.Status = Resource.Status.LOADING
    ){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TokopointAction

            if (tokopoint != other.tokopoint) return false
            if (status != other.status) return false

            return true
        }

        override fun hashCode(): Int {
            var result = tokopoint.hashCode()
            result = 31 * result + status.hashCode()
            return result
        }
    }
}

class WalletNonLoginDataModel : ModelViewType{
    override fun getPrimaryKey(): Int {
        return 3
    }

    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return other is WalletNonLoginDataModel
    }
}