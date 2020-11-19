package com.tokopedia.homenav.mainnav.domain.usecases

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetMainNavDataUseCase @Inject constructor(
        private val mainNavMapper: MainNavMapper,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getOvoUseCase: GetCoroutineWalletBalanceUseCase,
        private val getSaldoUseCase: GetSaldoUseCase,
        private val getUserMembershipUseCase: GetUserMembershipUseCase,
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val getCategoryGroupUseCase: GetCategoryGroupUseCase,
        private val userSession: UserSessionInterface,
        @ApplicationContext private val context: Context
): UseCase<MainNavigationDataModel>() {

    override suspend fun executeOnBackground(): MainNavigationDataModel {
        return withContext(coroutineContext){

            val getUserInfoCall = async {
                getUserInfoUseCase.executeOnBackground()
            }
            val getCategoryCall = async {
                getCategoryGroupUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
                getCategoryGroupUseCase.executeOnBackground()
            }

            val userInfoData = (getUserInfoCall.await().takeIf { it is Success } as? Success<UserPojo>)?.data
            val categoryData = (getCategoryCall.await().takeIf { it is Success } as? Success<List<DynamicHomeIconEntity.Category>>)?.data

            var ovoData: WalletBalanceModel? = null
            var saldoData: SaldoPojo? = null
            var userMembershipData: MembershipPojo? = null
            var shopData: ShopInfoPojo? = null

            if (getLoginState() == AccountHeaderViewModel.LOGIN_STATE_LOGIN) {

                val getOvoCall = async {
                    getOvoUseCase.executeOnBackground()
                }
                val getSaldoCall = async {
                    getSaldoUseCase.executeOnBackground()
                }
                val getUserMembershipCall = async {
                    getUserMembershipUseCase.executeOnBackground()
                }
                val getShopInfoCall = async {
                    getShopInfoUseCase.executeOnBackground()

                }
                ovoData = (getOvoCall.await().takeIf { it is Success } as? Success<WalletBalanceModel>)?.data
                saldoData = (getSaldoCall.await().takeIf { it is Success } as? Success<SaldoPojo>)?.data
                userMembershipData = (getUserMembershipCall.await().takeIf { it is Success } as? Success<MembershipPojo>)?.data
                shopData = (getShopInfoCall.await().takeIf { it is Success } as? Success<ShopInfoPojo>)?.data
            }

            mainNavMapper.mapMainNavData(
                    userInfoData,
                    ovoData,
                    saldoData,
                    userMembershipData,
                    shopData,
                    categoryData
            )
        }
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