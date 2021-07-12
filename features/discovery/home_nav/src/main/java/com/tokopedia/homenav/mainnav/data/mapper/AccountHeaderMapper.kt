package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.common.util.isABNewTokopoint
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.view.datamodel.AccountHeaderDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.user.session.UserSessionInterface

class AccountHeaderMapper(
        private val userSession: UserSessionInterface
) {

    fun mapToHeaderModel(userPojo: UserPojo?,
                         walletBalanceModel: WalletBalanceModel?,
                         tokopointsStatusFilteredPojo: TokopointsStatusFilteredPojo?,
                         saldoPojo: SaldoPojo?,
                         userMembershipPojo: MembershipPojo?,
                         shopInfoPojo: ShopData.ShopInfoPojo?,
                         notificationPojo: ShopData.NotificationPojo?,
                         isCache: Boolean): AccountHeaderDataModel {
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
                walletBalanceModel?.let{
                    data.setWalletData(
                            ovo = it.cashBalance,
                            point = it.pointBalance)
                }
                tokopointsStatusFilteredPojo?.tokopointsStatusFiltered?.let {
                    data.setTokopointData(it.statusFilteredData.points.externalCurrencyAmountStr, it.statusFilteredData.points.pointsAmountStr, it.statusFilteredData.points.iconImageURL)
                }
                saldoPojo?.let {
                    data.setSaldoData(
                            saldo = convertPriceValueToIdrFormat(it.saldo.buyerUsable + it.saldo.sellerUsable, false) ?: ""
                    )
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
                // extra case when tokopoint null and ab is false
                if(!isABNewTokopoint() && tokopointsStatusFilteredPojo == null && data.isTokopointExternalAmountError){
                    data.isTokopointExternalAmountError = false
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