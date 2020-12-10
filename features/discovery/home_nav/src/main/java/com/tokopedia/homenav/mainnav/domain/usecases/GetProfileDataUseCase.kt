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

class GetProfileDataUseCase @Inject constructor(
        private val accountHeaderMapper: AccountHeaderMapper,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getOvoUseCase: GetCoroutineWalletBalanceUseCase,
        private val getSaldoUseCase: GetSaldoUseCase,
        private val getUserMembershipUseCase: GetUserMembershipUseCase,
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val userSession: UserSessionInterface,
        @ApplicationContext private val context: Context
): UseCase<AccountHeaderViewModel>() {

    override suspend fun executeOnBackground(): AccountHeaderViewModel {
        return withContext(coroutineContext){

            val getUserInfoCall = async {
                getUserInfoUseCase.executeOnBackground()
            }

            val userInfoData = (getUserInfoCall.await().takeIf { it is Success } as? Success<UserPojo>)?.data

            var ovoData: WalletBalanceModel? = null
            var saldoData: SaldoPojo? = null
            var userMembershipData: MembershipPojo? = null
            var shopData: ShopInfoPojo? = null

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

            accountHeaderMapper.mapToHeaderModel(
                    userInfoData,
                    ovoData,
                    saldoData,
                    userMembershipData,
                    shopData
            )
        }
    }
}