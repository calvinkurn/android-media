package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance

// simple logic :
// title : check tag title, then check text title
// description : check tag description, then check text description

data class BalanceDrawerItemModel(
    val applinkContainer: String = "",
    val applinkActionText: String = "",
    val redirectUrl: String = "",
    val iconImageUrl: String? = null,
    val defaultIconRes: Int? = null,
    var balanceTitleTextAttribute: BalanceTextAttribute? = null,
    var balanceSubTitleTextAttribute: BalanceTextAttribute? = null,
    var balanceTitleTagAttribute: BalanceTagAttribute? = null,
    var balanceSubTitleTagAttribute: BalanceTagAttribute? = null,
    val drawerItemType: Int = TYPE_REWARDS,
    val mainPageTitle: String = "",
    var state: Int = STATE_LOADING,
    val trackingAttribute: String = "",
    var alternateBalanceDrawerItem: List<BalanceDrawerItemModel>? = null,
    var balanceCoachmark: BalanceCoachmark? = null,
    val reserveBalance: String = "",
    val headerTitle: String = "",
    val isSubscriberGoToPlus: Boolean = false
) {
    companion object {
        const val TYPE_REWARDS = 4

        // is not linked for any type of wallet
        const val TYPE_WALLET_APP_LINKED = 9
        const val TYPE_WALLET_APP_NOT_LINKED = 11

        // goto plus
        const val TYPE_SUBSCRIPTION = 12

        const val STATE_SUCCESS = 0
        const val STATE_LOADING = 1
        const val STATE_ERROR = 2
    }

    override fun equals(other: Any?): Boolean {
        return try {
            other as BalanceDrawerItemModel
            other.state != state &&
                other.balanceTitleTextAttribute != balanceTitleTextAttribute &&
                other.balanceSubTitleTextAttribute != balanceSubTitleTextAttribute &&
                other.iconImageUrl != iconImageUrl
        } catch (e: Exception) {
            false
        }
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun hashCode(): Int {
        var result = applinkContainer.hashCode()
        result = 31 * result + applinkActionText.hashCode()
        result = 31 * result + redirectUrl.hashCode()
        result = 31 * result + (iconImageUrl?.hashCode() ?: 0)
        result = 31 * result + (defaultIconRes ?: 0)
        result = 31 * result + (balanceTitleTextAttribute?.hashCode() ?: 0)
        result = 31 * result + (balanceSubTitleTextAttribute?.hashCode() ?: 0)
        result = 31 * result + (balanceTitleTagAttribute?.hashCode() ?: 0)
        result = 31 * result + (balanceSubTitleTagAttribute?.hashCode() ?: 0)
        result = 31 * result + drawerItemType
        result = 31 * result + mainPageTitle.hashCode()
        result = 31 * result + state
        result = 31 * result + trackingAttribute.hashCode()
        result = 31 * result + (alternateBalanceDrawerItem?.hashCode() ?: 0)
        result = 31 * result + (balanceCoachmark?.hashCode() ?: 0)
        result = 31 * result + reserveBalance.hashCode()
        result = 31 * result + headerTitle.hashCode()
        result = 31 * result + isSubscriberGoToPlus.hashCode()
        return result
    }
}
