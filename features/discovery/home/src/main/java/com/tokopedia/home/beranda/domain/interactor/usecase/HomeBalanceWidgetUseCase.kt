package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class HomeBalanceWidgetUseCase @Inject constructor(
        private val homeWalletAppRepository: HomeWalletAppRepository,
        private val homeTokopointsListRepository: HomeTokopointsListRepository,
        private val homeFlagRepository: HomeFlagRepository,
        private val userSession: UserSessionInterface,
        private val injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase
        ) {

    companion object {
        const val error_unable_to_parse_wallet = "Unable to parse wallet, wallet app list is empty"
    }

    suspend fun onGetInjectCouponTimeBased(): Result<InjectCouponTimeBased> {
        return try {
            val data = injectCouponTimeBasedUseCase.executeOnBackground().data
            Result.success(data)
        } catch (e: Exception) {
            Result.error(error = e)
        }
    }

    suspend fun onGetBalanceWidgetData(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        try {
            var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel, HomeBalanceModel.BALANCE_POSITION_FIRST, HomeBalanceModel.BALANCE_POSITION_SECOND)
            homeBalanceModel = getDataUsingWalletApp(homeBalanceModel)

            homeBalanceModel = getTokopointData(homeBalanceModel)
            return currentHeaderDataModel.copy(headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                    homeBalanceModel = homeBalanceModel,
                    isUserLogin = userSession.isLoggedIn
            ),
                    needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
            )
        } catch (e: Exception) {
            return currentHeaderDataModel
        }
    }

    private suspend fun getHomeBalanceModel(currentHeaderDataModel: HomeHeaderDataModel, vararg positionToReset: Int): HomeBalanceModel {
        return HomeBalanceModel().apply {
            val currentHomeBalanceModel = currentHeaderDataModel.headerDataModel?.homeBalanceModel
                    ?: HomeBalanceModel()
            balanceDrawerItemModels = currentHomeBalanceModel.balanceDrawerItemModels
            positionToReset.forEach {
                resetDrawerItem(it)
            }
        }
    }

    suspend fun onGetTokopointData(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel, HomeBalanceModel.BALANCE_POSITION_SECOND)
        homeBalanceModel = getTokopointData(homeBalanceModel)
        return currentHeaderDataModel.copy(
                headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel,
                        isUserLogin = userSession.isLoggedIn
                ),
                needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
        )
    }

    suspend fun onGetWalletAppData(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel, HomeBalanceModel.BALANCE_POSITION_FIRST)
        homeBalanceModel = getDataUsingWalletApp(homeBalanceModel)
        return currentHeaderDataModel.copy(
            headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                homeBalanceModel = homeBalanceModel,
                isUserLogin = userSession.isLoggedIn
            ),
            needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
        )
    }

    suspend fun onGetBalanceWidgetLoadingState(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        try {
            val homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel).apply { initBalanceModelByType() }
            return currentHeaderDataModel.copy(headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                homeBalanceModel = homeBalanceModel,
                isUserLogin = userSession.isLoggedIn
            ),
                needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
            )
        } catch (e: Exception) {
            return currentHeaderDataModel
        }
    }

    private suspend fun getTokopointData(homeBalanceModel: HomeBalanceModel): HomeBalanceModel {
        try {
            homeBalanceModel.mapBalanceData(tokopointDrawerListHomeData = homeTokopointsListRepository.getRemoteData(

            ))
        } catch (e: Exception) {
            homeBalanceModel.isTokopointsOrOvoFailed = true
            homeBalanceModel.mapErrorTokopoints()
        }
        return homeBalanceModel
    }

    private suspend fun getDataUsingWalletApp(homeBalanceModel: HomeBalanceModel): HomeBalanceModel {
        try {
            val walletAppData = homeWalletAppRepository.getRemoteData()
            walletAppData.let { walletContent ->
                if (walletContent.walletappGetBalance.balances.isNotEmpty()) {
                    homeBalanceModel.mapBalanceData(walletAppData = walletAppData)
                } else {
                    HomeServerLogger.logWarning(
                            type = HomeServerLogger.TYPE_WALLET_APP_ERROR,
                            throwable = MessageErrorException(error_unable_to_parse_wallet),
                            reason = error_unable_to_parse_wallet
                    )
                    throw IllegalStateException(error_unable_to_parse_wallet)
                }
            }
        } catch (e: Exception) {
            homeBalanceModel.isTokopointsOrOvoFailed = true
            homeBalanceModel.mapErrorWallet(isWalletApp = true)
        }
        return homeBalanceModel
    }
}