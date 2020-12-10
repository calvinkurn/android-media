package com.tokopedia.homenav.mainnav.domain.usecases

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetProfileDataCacheUseCase @Inject constructor(
        private val accountHeaderMapper: AccountHeaderMapper,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getUserMembershipUseCase: GetUserMembershipUseCase,
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val userSession: UserSessionInterface,
        @ApplicationContext private val context: Context
): UseCase<AccountHeaderViewModel>() {

    override suspend fun executeOnBackground(): AccountHeaderViewModel {
        getUserInfoUseCase.setStrategyCache()
        getUserMembershipUseCase.setStrategyCache()
        getShopInfoUseCase.setStrategyCache()
        return withContext(coroutineContext){

            var userInfoData: UserPojo? = null
            var userMembershipData: MembershipPojo? = null
            var shopData: ShopInfoPojo? = null

            if (getLoginState() == AccountHeaderViewModel.LOGIN_STATE_LOGIN) {
                val getUserInfoCall = async {
                    getUserInfoUseCase.executeOnBackground()
                }
                val getUserMembershipCall = async {
                    getUserMembershipUseCase.executeOnBackground()
                }
                val getShopInfoCall = async {
                    getShopInfoUseCase.executeOnBackground()

                }
                userInfoData = (getUserInfoCall.await().takeIf { it is Success } as? Success<UserPojo>)?.data
                userMembershipData = (getUserMembershipCall.await().takeIf { it is Success } as? Success<MembershipPojo>)?.data
                shopData = (getShopInfoCall.await().takeIf { it is Success } as? Success<ShopInfoPojo>)?.data
            }

            accountHeaderMapper.mapToHeaderModel(
                    userInfoData,
                    null,
                    null,
                    userMembershipData,
                    shopData,
                    true
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