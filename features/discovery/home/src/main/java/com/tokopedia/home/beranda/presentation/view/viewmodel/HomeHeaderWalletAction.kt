package com.tokopedia.home.beranda.presentation.view.viewmodel

import java.util.*

data class HomeHeaderWalletAction(
        var labelTitle: String = "",
        val balance: String = "",
        val redirectUrlBalance: String = "",
        val appLinkBalance: String = "",
        val typeAction: Int = 0,
        val isVisibleActionButton: Boolean = false,
        val labelActionButton: String = "",
        val appLinkActionButton: String = "",
        val isLinked: Boolean = false,
        val abTags: List<String> = ArrayList<String>(),
        val pointBalance: String = "",
        val rawPointBalance: Int = 0,
        val cashBalance: String = "",
        val rawCashBalance: Int = 0,
        val walletType: String = "",
        val isShowAnnouncement: Boolean = false,
        val isShowTopup: Boolean = false,
        val topupUrl: String = "",
        val topupLimit: Long = 0
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HomeHeaderWalletAction) return false

        if (labelTitle != other.labelTitle) return false
        if (balance != other.balance) return false
        if (redirectUrlBalance != other.redirectUrlBalance) return false
        if (appLinkBalance != other.appLinkBalance) return false
        if (typeAction != other.typeAction) return false
        if (isVisibleActionButton != other.isVisibleActionButton) return false
        if (labelActionButton != other.labelActionButton) return false
        if (appLinkActionButton != other.appLinkActionButton) return false
        if (isLinked != other.isLinked) return false
        if (abTags != other.abTags) return false
        if (pointBalance != other.pointBalance) return false
        if (rawPointBalance != other.rawPointBalance) return false
        if (cashBalance != other.cashBalance) return false
        if (rawCashBalance != other.rawCashBalance) return false
        if (walletType != other.walletType) return false
        if (isShowAnnouncement != other.isShowAnnouncement) return false
        if (isShowTopup != other.isShowTopup) return false
        if (topupUrl != other.topupUrl) return false
        if (topupLimit != other.topupLimit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = labelTitle.hashCode()
        result = HASH_CODE * result + balance.hashCode()
        result = HASH_CODE * result + redirectUrlBalance.hashCode()
        result = HASH_CODE * result + appLinkBalance.hashCode()
        result = HASH_CODE * result + typeAction
        result = HASH_CODE * result + isVisibleActionButton.hashCode()
        result = HASH_CODE * result + labelActionButton.hashCode()
        result = HASH_CODE * result + appLinkActionButton.hashCode()
        result = HASH_CODE * result + isLinked.hashCode()
        result = HASH_CODE * result + abTags.hashCode()
        result = HASH_CODE * result + pointBalance.hashCode()
        result = HASH_CODE * result + rawPointBalance
        result = HASH_CODE * result + cashBalance.hashCode()
        result = HASH_CODE * result + rawCashBalance
        result = HASH_CODE * result + walletType.hashCode()
        result = HASH_CODE * result + isShowAnnouncement.hashCode()
        result = HASH_CODE * result + isShowTopup.hashCode()
        result = HASH_CODE * result + topupUrl.hashCode()
        result = HASH_CODE * result + topupLimit.hashCode()
        return result
    }

    companion object{
        private const val HASH_CODE = 31

    }
}