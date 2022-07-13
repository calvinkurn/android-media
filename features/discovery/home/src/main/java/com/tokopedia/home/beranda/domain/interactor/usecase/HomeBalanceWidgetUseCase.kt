package com.tokopedia.home.beranda.domain.interactor.usecase

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.home.beranda.data.model.SubscriptionsData
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel.Companion.DEFAULT_BALANCE_POSITION
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
        private val injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase,
        private val getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository
        ) {

    companion object {
        const val error_unable_to_parse_wallet = "Unable to parse wallet, wallet app list is empty"
        private const val BALANCE_TYPE_GOPAY = "gopay"
        private const val BALANCE_TYPE_REWARDS = "rewards"
        private const val BALANCE_TYPE_SUBSCRIPTIONS = "subscription"
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
            val getHomeBalanceWidget = getHomeBalanceWidgetRepository.getRemoteData()
            currentHeaderDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.clear()
            var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel)
            var counter = 0
            getHomeBalanceWidget.getHomeBalanceList.balancesList.forEach {
                when (it.type) {
                    BALANCE_TYPE_GOPAY -> {
                        homeBalanceModel = getDataUsingWalletApp(homeBalanceModel, it.title)
                        homeBalanceModel.balancePositionGopay = counter
                    }
                    BALANCE_TYPE_REWARDS -> {
                        homeBalanceModel = getTokopointData(homeBalanceModel, it.title)
                        homeBalanceModel.balancePositionRewards = counter
                    }
                    BALANCE_TYPE_SUBSCRIPTIONS -> {
                        homeBalanceModel = getSubscriptionsData(homeBalanceModel, it.title, it.data)
                        homeBalanceModel.balancePositionSubscriptions = counter
                    }
                }
                counter++
            }

            return currentHeaderDataModel.copy(
                headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                    homeBalanceModel = homeBalanceModel.copy(status = HomeBalanceModel.STATUS_SUCCESS),
                    isUserLogin = userSession.isLoggedIn
                ),
                needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)
            )
        } catch (e: Exception) {
            currentHeaderDataModel.headerDataModel?.homeBalanceModel?.status = HomeBalanceModel.STATUS_ERROR
            currentHeaderDataModel.headerDataModel?.isUserLogin = userSession.isLoggedIn
            return currentHeaderDataModel
        }
    }

    private fun getHomeBalanceModel(currentHeaderDataModel: HomeHeaderDataModel): HomeBalanceModel {
        return HomeBalanceModel().apply {
            val currentHomeBalanceModel = currentHeaderDataModel.headerDataModel?.homeBalanceModel
                    ?: HomeBalanceModel()
            balanceDrawerItemModels = currentHomeBalanceModel.balanceDrawerItemModels
//            positionToReset.forEach {
//                resetDrawerItem(it)
//            }
        }
    }

    suspend fun onGetTokopointData(
        currentHeaderDataModel: HomeHeaderDataModel,
        position: Int,
        headerTitle: String
    ): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel)
        homeBalanceModel = getTokopointData(homeBalanceModel, headerTitle, position)
        return currentHeaderDataModel.copy(
                headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel.copy(status = HomeBalanceModel.STATUS_SUCCESS),
                        isUserLogin = userSession.isLoggedIn
                ),
                needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
        )
    }

    suspend fun onGetWalletAppData(
        currentHeaderDataModel: HomeHeaderDataModel,
        position: Int,
        headerTitle: String
    ): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel)
        homeBalanceModel = getDataUsingWalletApp(homeBalanceModel, headerTitle, position)
        return currentHeaderDataModel.copy(
            headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                homeBalanceModel = homeBalanceModel.copy(status = HomeBalanceModel.STATUS_SUCCESS),
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

    private suspend fun getTokopointData(
        homeBalanceModel: HomeBalanceModel,
        headerTitle: String,
        position: Int = DEFAULT_BALANCE_POSITION
    ): HomeBalanceModel {
        try {
            homeBalanceModel.mapBalanceData(
                tokopointDrawerListHomeData = homeTokopointsListRepository.getRemoteData(),
                headerTitle = headerTitle,
                position = position
            )
        } catch (e: Exception) {
            homeBalanceModel.isTokopointsOrOvoFailed = true
            homeBalanceModel.mapErrorTokopoints(headerTitle, position)
        }
        return homeBalanceModel
    }

    private fun getSubscriptionsData(
        homeBalanceModel: HomeBalanceModel,
        headerTitle: String,
        subscriptions: String
    ): HomeBalanceModel {
        try {
            val subscriptionsData = Gson().fromJson(subscriptions, SubscriptionsData::class.java)
            homeBalanceModel.mapBalanceData(subscriptionsData = subscriptionsData, headerTitle = headerTitle)
        } catch (e: Exception) {
            homeBalanceModel.mapErrorTokopoints(headerTitle)
        }
        return homeBalanceModel
    }

    private suspend fun getDataUsingWalletApp(
        homeBalanceModel: HomeBalanceModel,
        headerTitle: String,
        position: Int = DEFAULT_BALANCE_POSITION
    ): HomeBalanceModel {
        try {
            val walletAppData = homeWalletAppRepository.getRemoteData()
            walletAppData.let { walletContent ->
                if (walletContent.walletappGetBalance.balances.isNotEmpty()) {
                    homeBalanceModel.mapBalanceData(
                        walletAppData = walletAppData,
                        headerTitle = headerTitle,
                        position = position
                    )
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
            homeBalanceModel.mapErrorWallet(headerTitle, position)
        }
        return homeBalanceModel
    }
}