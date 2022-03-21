package com.tokopedia.homenav.mainnav.view.datamodel.account

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class AccountHeaderDataModel(
    /**
     * Account holder data
     */
    val id: Int = 999,
    var loginState: Int = 0,
    var isCacheData: Boolean = false,
    var state: Int = NAV_PROFILE_STATE_LOADING,

    /**
     * Account model data
     */
    var profileDataModel: ProfileDataModel = ProfileDataModel(),
    var profileMembershipDataModel: ProfileMembershipDataModel = ProfileMembershipDataModel(),
    var profileSaldoDataModel: ProfileSaldoDataModel = ProfileSaldoDataModel(),
    var profileSellerDataModel: ProfileSellerDataModel = ProfileSellerDataModel(),
    var profileAffiliateDataModel: ProfileAffiliateDataModel = ProfileAffiliateDataModel(),
    var profileWalletAppDataModel: ProfileWalletAppDataModel = ProfileWalletAppDataModel()
) : MainNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return this == visitable
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }

    companion object {
        const val LOGIN_STATE_LOGIN = 0
        const val LOGIN_STATE_NON_LOGIN = 1
        const val STICKY_LOGIN_REMINDER_PREF = "sticky_login_reminder.pref"
        const val KEY_USER_NAME = "user_name"
        const val KEY_PROFILE_PICTURE = "profile_picture"
        const val ERROR_TEXT = "Gagal memuat, klik untuk coba lagi"

        const val ERROR_TEXT_PROFILE = "Gagal memuat profil"
        const val ERROR_TEXT_OVO = "Gagal memuat saldo Ovo"
        const val ERROR_TEXT_TOKOPOINTS = "Gagal memuat Tokopoints"
        const val ERROR_TEXT_SALDO = "Gagal memuat saldo"
        const val ERROR_TEXT_SHOP = "Gagal Memuat Toko.  %s"
        const val ERROR_TEXT_SHOP_TRY = "Coba Lagi"

        const val DEFAULT_SHOP_ID_NOT_OPEN = "-1"
        const val DEFAULT_SHOP_ID_NOT_OPEN_TEXT = "Buka Toko Gratis"

        private const val WALLET_CODE_PEMUDA = "PEMUDA"
        private const val WALLET_CODE_PEMUDA_POINTS = "PEMUDAPOINTS"

        const val NAV_PROFILE_STATE_LOADING = 98
        const val NAV_PROFILE_STATE_SUCCESS = 97
        const val NAV_PROFILE_STATE_FAILED = 96
    }

    fun setProfileData(userName: String, userImage: String, loginState: Int, isGetUserNameError: Boolean) {
        this.profileDataModel.userImage = userImage
        this.profileDataModel.userName = userName
        this.profileDataModel.isGetUserNameError = isGetUserNameError
        this.loginState = loginState
    }

    fun setSaldoData(saldo: String = "") {
        this.profileSaldoDataModel.saldo = saldo
        this.profileSaldoDataModel.isGetSaldoError = false
    }

    fun setTokopointData(amount: String, point: String, badge: String) {
        this.profileMembershipDataModel.tokopointPointAmount = point
        this.profileMembershipDataModel.tokopointExternalAmount = amount
        this.profileMembershipDataModel.tokopointBadgeUrl = badge
    }

    fun setUserBadge(badge: String) {
        this.profileMembershipDataModel.badge = badge
    }

    fun setUserShopName(
        shopName: String = "",
        shopId: String = "",
        shopOrderCount: Int,
        isError: Boolean = false,
        isLoading: Boolean = false
    ) {
        this.profileSellerDataModel.hasShop = shopId != DEFAULT_SHOP_ID_NOT_OPEN
        this.profileSellerDataModel.isGetShopError = isError
        this.profileSellerDataModel.isGetShopLoading = isLoading
        if (profileSellerDataModel.hasShop) {
            this.profileSellerDataModel.shopName = shopName
            this.profileSellerDataModel.shopId = shopId
            this.profileSellerDataModel.shopOrderCount = shopOrderCount
        } else {
            this.profileSellerDataModel.shopName = DEFAULT_SHOP_ID_NOT_OPEN_TEXT
            this.profileSellerDataModel.shopId = DEFAULT_SHOP_ID_NOT_OPEN
            this.profileSellerDataModel.shopOrderCount = 0
        }

    }

    fun setAdminData(adminRoleText: String?, canGoToSellerAccount: Boolean) {
        this.profileSellerDataModel.adminRoleText = adminRoleText
        this.profileSellerDataModel.canGoToSellerAccount = canGoToSellerAccount
    }

    fun setWalletAppData(walletAppData: WalletAppData) {
        val selectedBalance = walletAppData.walletappGetBalance.balances.getOrNull(0)
        val gopayBalance = selectedBalance?.balance?.find { it.walletCode == WALLET_CODE_PEMUDA }
        val gopayPoints =
            selectedBalance?.balance?.find { it.walletCode == WALLET_CODE_PEMUDA_POINTS }
        this.profileWalletAppDataModel.walletAppImageUrl = selectedBalance?.iconUrl ?: ""
        if (gopayBalance?.amount != 0) {
            this.profileWalletAppDataModel.gopayBalance = gopayBalance?.amountFmt ?: ""
        }
        if (gopayPoints?.amount != 0) {
            this.profileWalletAppDataModel.gopayPointsBalance = gopayPoints?.amountFmt ?: ""
        }
        this.profileWalletAppDataModel.walletAppActivationCta = selectedBalance?.globalMenuText?.id ?: ""
        this.profileWalletAppDataModel.isWalletAppLinked = selectedBalance?.isLinked ?: false
    }
}