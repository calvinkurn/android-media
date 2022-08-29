package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam

class AccountHeaderMapper(
        private val userSession: UserSessionInterface
) {

    fun mapToHeaderModel(userPojo: UserPojo?,
                         tokopointsStatusFilteredPojo: TokopointsStatusFilteredPojo?,
                         saldoPojo: SaldoPojo?,
                         userMembershipPojo: MembershipPojo?,
                         shopInfoPojo: ShopData.ShopInfoPojo?,
                         notificationPojo: ShopData.NotificationPojo?,
                         affiliatePojo: AffiliateUserDetailData?,
                         isCache: Boolean,
                         walletAppData: WalletAppData? = null,
                         isWalletAppError: Boolean = false,
                         isEligibleForWalletApp: Boolean = false,
                         isSaldoError: Boolean = false,
                         isShopDataError: Boolean = false,
                         isGetTokopointsError: Boolean = false,
                         isAffiliateError: Boolean = false,
                         tokopediaPlusParam: TokopediaPlusParam?,
                         tokopediaPlusError: Throwable? = null
    ): AccountHeaderDataModel {
        var accountModel = AccountHeaderDataModel()

        when (val loginState = getLoginState()) {
            AccountHeaderDataModel.LOGIN_STATE_LOGIN -> {
                val data = AccountHeaderDataModel()
                if(userPojo == null) {
                    data.setProfileData(
                        userName = "",
                        userImage = "",
                        loginState = loginState,
                        isGetUserNameError = true
                    )
                }
                else {
                    userPojo.let {
                        data.setProfileData(
                            userName = userPojo.profile.name,
                            userImage = userPojo.profile.profilePicture,
                            loginState = loginState,
                            isGetUserNameError = data.profileDataModel.isGetUserNameError
                        )
                }
                }
                tokopointsStatusFilteredPojo?.tokopointsStatusFiltered?.let {
                    data.setTokopointData(it.statusFilteredData.points.externalCurrencyAmountStr, it.statusFilteredData.points.pointsAmountStr, it.statusFilteredData.points.iconImageURL)
                }
                walletAppData?.let {
                    data.setWalletAppData(it)
                }
                saldoPojo?.let {
                    val totalSaldo = it.saldo.buyerUsable + it.saldo.sellerUsable
                    val saldoValue = convertPriceValueToIdrFormat(totalSaldo, false) ?: ""
                    data.setSaldoData(
                        saldo = saldoValue
                    )
                }
                userMembershipPojo?.let {
                    data.setUserBadge(
                            badge = it.tokopoints.status.tier.imageUrl
                    )
                }
                shopInfoPojo?.let {
                    it.info
                    data.setUserShopName(
                            shopName = it.info.shopName,
                            shopId =  it.info.shopId,
                            shopOrderCount = getTotalOrderCount(notificationPojo),
                            isError = false,
                            isLoading = false
                    )
                }

                affiliatePojo?.let {
                    data.setAffiliate(
                        isRegistered = it.affiliateUserDetail.isRegistered,
                        affiliateName = it.affiliateUserDetail.title,
                        affiliateAppLink = it.affiliateUserDetail.redirection.android,
                        isLoading = false
                    )
                }

                tokopediaPlusParam?.let {
                    data.setTokopediaPlus(
                        tokopediaPlusParam = it,
                        isLoading = false,
                        error = null
                    )
                }

                data.profileWalletAppDataModel.isWalletAppFailed = isWalletAppError
                data.profileWalletAppDataModel.isEligibleForWalletApp = isEligibleForWalletApp
                data.profileSaldoDataModel.isGetSaldoError = isSaldoError
                data.profileSellerDataModel.isGetShopError = isShopDataError
                data.profileAffiliateDataModel.isGetAffiliateError = isAffiliateError
                data.profileMembershipDataModel.isGetUserMembershipError = isGetTokopointsError
                data.tokopediaPlusDataModel.isGetTokopediaPlusLoading = false
                data.tokopediaPlusDataModel.tokopediaPlusError = tokopediaPlusError
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