package com.tokopedia.homenav.mainnav.domain.usecases

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.common.util.isABNewTokopoint
import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.view.datamodel.AccountHeaderDataModel
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetProfileDataUseCase @Inject constructor(
        private val accountHeaderMapper: AccountHeaderMapper,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getOvoUseCase: GetCoroutineWalletBalanceUseCase,
        private val getSaldoUseCase: GetSaldoUseCase,
        private val getUserMembershipUseCase: GetUserMembershipUseCase,
        private val getTokopointStatusFiltered: GetTokopointStatusFiltered,
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val userSession: UserSessionInterface,
        @ApplicationContext private val context: Context
): UseCase<AccountHeaderDataModel>() {


    override suspend fun executeOnBackground(): AccountHeaderDataModel {
        getUserInfoUseCase.setStrategyCloudThenCache()
        getShopInfoUseCase.setStrategyCloudThenCache()

        return withContext(coroutineContext){
            var userInfoData: UserPojo? = null

            var ovoData: WalletBalanceModel? = null
            var saldoData: SaldoPojo? = null
            var userMembershipData: MembershipPojo? = null
            var shopData: ShopData? = null
            var tokopoint: TokopointsStatusFilteredPojo? = null

            val getUserInfoCall = async {
                getUserInfoUseCase.executeOnBackground()
            }
            val getTokopointCall = async {
                getTokopointStatusFiltered.executeOnBackground()
            }
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
            userInfoData = (getUserInfoCall.await().takeIf { it is Success } as? Success<UserPojo>)?.data

            userMembershipData = (getUserMembershipCall.await().takeIf { it is Success } as? Success<MembershipPojo>)?.data

            if(isABNewTokopoint()) tokopoint =
                    (getTokopointCall.await().takeIf { it is Success } as? Success<TokopointsStatusFilteredPojo>)?.data


            // check if tokopoint = 0 or null then follow old flow (fetch saldo)
            if(tokopoint?.tokopointsStatusFiltered?.statusFilteredData?.points?.pointsAmount.toZeroIfNull().isZero() && tokopoint?.tokopointsStatusFiltered?.statusFilteredData?.points?.externalCurrencyAmount.toZeroIfNull().isZero()){
                ovoData = (getOvoCall.await().takeIf { it is Success } as? Success<WalletBalanceModel>)?.data
                saldoData = (getSaldoCall.await().takeIf { it is Success } as? Success<SaldoPojo>)?.data
                tokopoint = null
            }

            shopData = (getShopInfoCall.await().takeIf { it is Success } as? Success<ShopData>)?.data

            accountHeaderMapper.mapToHeaderModel(
                    userInfoData,
                    ovoData,
                    tokopoint,
                    saldoData,
                    userMembershipData,
                    shopData?.userShopInfo,
                    shopData?.notifications,
                    false
            )
        }
    }

    private fun getLoginState(): Int {
        return when {
            userSession.isLoggedIn -> AccountHeaderDataModel.LOGIN_STATE_LOGIN
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