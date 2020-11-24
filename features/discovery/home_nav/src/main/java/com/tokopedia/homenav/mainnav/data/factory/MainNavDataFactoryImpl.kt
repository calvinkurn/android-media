package com.tokopedia.homenav.mainnav.data.factory

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.common.util.convertPriceValueToIdrFormat
import com.tokopedia.homenav.mainnav.data.mapper.toVisitable
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.user.session.UserSessionInterface

class MainNavDataFactoryImpl(
        private val context: Context,
        private val userSession: UserSessionInterface
): MainNavDataFactory {

    private var visitableList: MutableList<Visitable<*>> = mutableListOf()

    override fun buildVisitableList(): MainNavDataFactory {
        visitableList.clear()
        return this
    }

    override fun addProfileSection(userPojo: UserPojo?,
                                   walletBalanceModel: WalletBalanceModel?,
                                   saldoPojo: SaldoPojo?,
                                   membershipPojo: MembershipPojo?,
                                   shopInfoPojo: ShopInfoPojo?): MainNavDataFactory {
        when (val loginState = getLoginState()) {
            AccountHeaderViewModel.LOGIN_STATE_LOGIN -> {
                val data = AccountHeaderViewModel()
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
                membershipPojo?.let {
                    data.setUserBadge(
                            badge = it.tokopoints.status.tier.imageUrl
                    )
                }
                shopInfoPojo?.let {
                    data.setUserShopName(
                            shopName = it.info.shopName,
                            shopId =  it.info.shopId
                    )
                }

                visitableList.add(data)
            }
            AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS,
            AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN -> {
                visitableList.add(AccountHeaderViewModel(loginState = loginState))
            }
        }
        return this
    }

    override fun addSeparatorSection(): MainNavDataFactory {
        visitableList.add(SeparatorViewModel())
        return this
    }

    override fun addBUListSection(categoryData: List<DynamicHomeIconEntity.Category>?): MainNavDataFactory {
        categoryData?.toVisitable()?.let { visitableList.addAll(it) }
        return this
    }

    override fun build(): List<Visitable<*>> {
        return visitableList
    }

    private fun getLoginState(): Int {
        return when {
            userSession.isLoggedIn -> AccountHeaderViewModel.LOGIN_STATE_LOGIN
            haveUserLogoutData() -> AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS
            else -> AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN
        }
    }

    private fun haveUserLogoutData(): Boolean {
        val name = getSharedPreference().getString(AccountHeaderViewModel.KEY_USER_NAME, "") ?: ""
        return name.isNotEmpty()
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(AccountHeaderViewModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }

}