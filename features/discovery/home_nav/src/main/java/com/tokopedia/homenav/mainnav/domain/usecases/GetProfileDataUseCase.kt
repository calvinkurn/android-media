package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.saldo.SaldoPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetProfileDataUseCase @Inject constructor(
    private val accountHeaderMapper: AccountHeaderMapper,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getSaldoUseCase: GetSaldoUseCase,
    private val getUserMembershipUseCase: GetUserMembershipUseCase,
    private val getTokopointStatusFiltered: GetTokopointStatusFiltered,
    private val getShopInfoUseCase: GetShopInfoUseCase,
    private val getWalletEligibilityUseCase: GetWalletEligibilityUseCase,
    private val getWalletAppBalanceUseCase: GetWalletAppBalanceUseCase
) : UseCase<AccountHeaderDataModel>() {


    override suspend fun executeOnBackground(): AccountHeaderDataModel {
        getUserInfoUseCase.setStrategyCloudThenCache()
        getShopInfoUseCase.setStrategyCloudThenCache()
        getUserMembershipUseCase.setStrategyCloudThenCache()

        return withContext(coroutineContext) {
            var userInfoData: UserPojo? = null
            var saldoData: SaldoPojo? = null
            var userMembershipData: MembershipPojo? = null
            var shopData: ShopData? = null
            var tokopoint: TokopointsStatusFilteredPojo? = null
            var isEligibleForWalletApp: Boolean = false
            var walletAppData: WalletAppData? = null
            var isWalletAppError: Boolean = false
            var isSaldoError: Boolean = false
            var isShopDataError: Boolean = false
            var isGetTokopointError: Boolean = false

            val getUserInfoCall = async {
                getUserInfoUseCase.executeOnBackground()
            }
            val getTokopointCall = async {
                getTokopointStatusFiltered.executeOnBackground()
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
            userInfoData =
                (getUserInfoCall.await().takeIf { it is Success } as? Success<UserPojo>)?.data

            val saldoJob = getSaldoCall.await()
            if (saldoJob is Success) {
                saldoData = (saldoJob as? Success<SaldoPojo>)?.data
            } else if (saldoJob is Fail) {
                isSaldoError = true
            }

            userMembershipData = (getUserMembershipCall.await()
                .takeIf { it is Success } as? Success<MembershipPojo>)?.data

            val tokopointJob = getTokopointCall.await()
            if (tokopointJob is Success) {
                tokopoint = (tokopointJob as? Success<TokopointsStatusFilteredPojo>)?.data
            } else if (tokopointJob is Fail) {
                isGetTokopointError = true
            }

            isEligibleForWalletApp = try {
                getWalletEligibilityUseCase.executeOnBackground().isGoPointsEligible
            } catch (e: Exception) {
                false
            }

            if (isEligibleForWalletApp) {
                walletAppData = try {
                    tokopoint = null
                    isWalletAppError = false
                    getWalletAppBalanceUseCase.executeOnBackground()
                } catch (e: Exception) {
                    isWalletAppError = true
                    null
                }
            }

            val shopJob = getShopInfoCall.await()
            if (shopJob is Success) {
                shopData = (shopJob as? Success<ShopData>)?.data
            } else if (shopJob is Fail) {
                isShopDataError = true
            }

            accountHeaderMapper.mapToHeaderModel(
                userInfoData,
                tokopoint,
                saldoData,
                userMembershipData,
                shopData?.userShopInfo,
                shopData?.notifications,
                false,
                walletAppData = walletAppData,
                isWalletAppError = isWalletAppError,
                isEligibleForWalletApp = isEligibleForWalletApp,
                isSaldoError = isSaldoError,
                isShopDataError = isShopDataError,
                isGetTokopointsError = isGetTokopointError
            )
        }
    }
}