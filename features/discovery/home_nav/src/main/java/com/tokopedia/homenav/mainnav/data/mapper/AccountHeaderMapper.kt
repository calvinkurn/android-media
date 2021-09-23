package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.user.session.UserSessionInterface

class AccountHeaderMapper(
        private val userSession: UserSessionInterface
) {

    fun mapToHeaderModel(userPojo: UserPojo?,
                         tokopointsStatusFilteredPojo: TokopointsStatusFilteredPojo?,
                         saldoPojo: SaldoPojo?,
                         userMembershipPojo: MembershipPojo?,
                         shopInfoPojo: ShopData.ShopInfoPojo?,
                         notificationPojo: ShopData.NotificationPojo?,
                         isCache: Boolean,
                         walletAppData: WalletAppData? = null,
                         isWalletAppError: Boolean = false,
                         isEligibleForWalletApp: Boolean = false,
                         isSaldoError: Boolean = false,
                         isShopDataError: Boolean = false
    ): AccountHeaderDataModel {
        var accountModel = AccountHeaderDataModel()

        when (val loginState = getLoginState()) {
            AccountHeaderDataModel.LOGIN_STATE_LOGIN -> {
                val data = AccountHeaderDataModel()
                userPojo?.let {
                    data.setProfileData(
                            userName = userPojo.profile.name,
                            userImage = userPojo.profile.profilePicture,
                            loginState = loginState
                    )
                }
                tokopointsStatusFilteredPojo?.tokopointsStatusFiltered?.let {
                    data.setTokopointData(it.statusFilteredData.points.externalCurrencyAmountStr, it.statusFilteredData.points.pointsAmountStr, it.statusFilteredData.points.iconImageURL)
                }
                saldoPojo?.let {
                    val totalSaldo = it.saldo.buyerUsable + it.saldo.sellerUsable
                    if (totalSaldo > 0) {
                        val saldoValue = convertPriceValueToIdrFormat(totalSaldo, false) ?: ""
                        data.setSaldoData(
                            saldo =saldoValue
                        )
                    }
                }
                userMembershipPojo?.let {
                    data.setUserBadge(
                            badge = it.tokopoints.status.tier.imageUrl
                    )
                }
                shopInfoPojo?.let {
                    data.setUserShopName(
                            shopName = it.info.shopName,
                            shopId =  it.info.shopId,
                            shopOrderCount = getTotalOrderCount(notificationPojo),
                            isError = false,
                            isLoading = false
                    )
                }
                walletAppData?.let {
                    data.setWalletAppData(it)
                }
                data.profileWalletAppDataModel.isWalletAppFailed = isWalletAppError
                data.profileWalletAppDataModel.isEligibleForWalletApp = isEligibleForWalletApp
                data.profileSaldoDataModel.isGetSaldoError = isSaldoError
                data.profileSellerDataModel.isGetShopError = isShopDataError
                // extra case when tokopoint null and ab is false
                if(data.profileMembershipDataModel.isTokopointExternalAmountError){
                    data.profileMembershipDataModel.isTokopointExternalAmountError = false
                }
                data.isCacheData = isCache
                accountModel = data
            }
            AccountHeaderDataModel.LOGIN_STATE_NON_LOGIN -> {
                accountModel = AccountHeaderDataModel(loginState = loginState)
            }
        }
        return accountModel
    }

    private fun getTotalOrderCount(notificationPojo: ShopData.NotificationPojo?): Int {
        return notificationPojo?.let {
            it.sellerOrderStatus.newOrderCount
                    .plus(it.sellerOrderStatus.readyToShipOrderCount)
                    .plus(it.sellerOrderStatus.inResolution)
        }.orZero()
    }

    private fun getLoginState(): Int {
        return when {
            userSession.isLoggedIn -> AccountHeaderDataModel.LOGIN_STATE_LOGIN
            else -> AccountHeaderDataModel.LOGIN_STATE_NON_LOGIN
        }
    }
}