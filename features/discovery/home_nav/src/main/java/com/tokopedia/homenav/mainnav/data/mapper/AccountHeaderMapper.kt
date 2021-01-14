package com.tokopedia.homenav.mainnav.data.mapper

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.view.datamodel.AccountHeaderDataModel
import com.tokopedia.user.session.UserSessionInterface

class AccountHeaderMapper (
        private val context: Context,
        private val userSession: UserSessionInterface
) {

    fun mapToHeaderModel(userPojo: UserPojo?,
                         walletBalanceModel: WalletBalanceModel?,
                         saldoPojo: SaldoPojo?,
                         userMembershipPojo: MembershipPojo?,
                         shopInfoPojo: ShopInfoPojo?,
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
                            isError = false,
                            isLoading = false
                    )
                }
                data.isCacheData = isCache
                accountModel = data

            }
            AccountHeaderDataModel.LOGIN_STATE_LOGIN_AS,
            AccountHeaderDataModel.LOGIN_STATE_NON_LOGIN -> {
                accountModel = AccountHeaderDataModel(loginState = loginState)
            }
        }
        return accountModel
    }

    private fun getLoginState(): Int {
        return when {
            userSession.isLoggedIn -> AccountHeaderDataModel.LOGIN_STATE_LOGIN
            haveUserLogoutData() -> AccountHeaderDataModel.LOGIN_STATE_LOGIN_AS
            else -> AccountHeaderDataModel.LOGIN_STATE_NON_LOGIN
        }
    }

    private fun haveUserLogoutData(): Boolean {
        val name = getSharedPreference().getString(AccountHeaderDataModel.KEY_USER_NAME, "") ?: ""
        return name.isNotEmpty()
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(AccountHeaderDataModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }
}