package com.tokopedia.home.beranda.domain.interactor.usecase

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.tokopedia.home.beranda.data.model.SubscriptionsData
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.GetHomeBalanceWidgetRepository
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
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
        private val userSession: UserSessionInterface,
        private val injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase,
        private val getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository
        ) {

    companion object {
        const val error_unable_to_parse_wallet = "Unable to parse wallet, wallet app list is empty"
        const val ERROR_UNABLE_TO_PARSE_BALANCE_WIDGET = "Unable to parse balance widget"
        const val ERROR_UNABLE_TO_PARSE_BALANCE_WIDGET_SUBSCRIPTION = "Unable to parse balance widget subscription data"
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

    suspend fun onGetBalanceWidgetData(): HomeHeaderDataModel {
        val homeHeaderDataModel = HomeHeaderDataModel()
        if (!userSession.isLoggedIn) return homeHeaderDataModel

        try {
            val getHomeBalanceWidget = getHomeBalanceWidgetRepository.getRemoteData()
            if (getHomeBalanceWidget.getHomeBalanceList.error.isNotBlank()) {
                throw MessageErrorException(getHomeBalanceWidget.getHomeBalanceList.error)
            } else {
                homeHeaderDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.clear()
                var homeBalanceModel = getHomeBalanceModel(homeHeaderDataModel)
                var indexDataRegistered = 0
                getHomeBalanceWidget.getHomeBalanceList.balancesList.forEach { getHomeBalanceItem ->
                    when (getHomeBalanceItem.type) {
                        BALANCE_TYPE_GOPAY -> {
                            val placeHolderLoadingGopay = BalanceDrawerItemModel(
                                drawerItemType = TYPE_WALLET_APP_LINKED,
                                headerTitle = getHomeBalanceItem.title
                            )
                            homeBalanceModel.balanceDrawerItemModels.add(placeHolderLoadingGopay)
                            indexDataRegistered++
                        }
                        BALANCE_TYPE_REWARDS -> {
                            val placeHolderLoadingRewards = BalanceDrawerItemModel(
                                drawerItemType = TYPE_REWARDS,
                                headerTitle = getHomeBalanceItem.title
                            )
                            homeBalanceModel.balanceDrawerItemModels.add(placeHolderLoadingRewards)
                            indexDataRegistered++
                        }
                        BALANCE_TYPE_SUBSCRIPTIONS -> {
                            homeBalanceModel = getSubscriptionsData(
                                homeBalanceModel,
                                getHomeBalanceItem.title,
                                getHomeBalanceItem.data
                            )
                            homeBalanceModel.balancePositionSubscriptions = indexDataRegistered
                            indexDataRegistered++
                        }
                    }
                }
                indexDataRegistered = 0

                return homeHeaderDataModel.copy(
                    headerDataModel = homeHeaderDataModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel.copy(status = HomeBalanceModel.STATUS_SUCCESS),
                        isUserLogin = userSession.isLoggedIn
                    )
                )
            }
        } catch (e: Exception) {
            HomeServerLogger.logWarning(
                type = HomeServerLogger.TYPE_BALANCE_WIDGET_ERROR,
                throwable = MessageErrorException(e.localizedMessage),
                reason = ERROR_UNABLE_TO_PARSE_BALANCE_WIDGET
            )
            homeHeaderDataModel.headerDataModel?.homeBalanceModel?.status = HomeBalanceModel.STATUS_ERROR
            homeHeaderDataModel.headerDataModel?.isUserLogin = userSession.isLoggedIn
            return homeHeaderDataModel
        }
    }

    private fun getHomeBalanceModel(currentHeaderDataModel: HomeHeaderDataModel): HomeBalanceModel {
        return HomeBalanceModel().apply {
            val currentHomeBalanceModel = currentHeaderDataModel.headerDataModel?.homeBalanceModel
                    ?: HomeBalanceModel()
            balanceDrawerItemModels = currentHomeBalanceModel.balanceDrawerItemModels
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
                    homeBalanceModel = homeBalanceModel.copy(status = HomeBalanceModel.STATUS_SUCCESS,
                        balancePositionSubscriptions = currentHeaderDataModel.headerDataModel?.homeBalanceModel?.balancePositionSubscriptions
                            ?: DEFAULT_BALANCE_POSITION
                    ),
                        isUserLogin = userSession.isLoggedIn,
                )
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
                homeBalanceModel = homeBalanceModel.copy(status = HomeBalanceModel.STATUS_SUCCESS,
                    balancePositionSubscriptions = currentHeaderDataModel.headerDataModel?.homeBalanceModel?.balancePositionSubscriptions
                        ?: DEFAULT_BALANCE_POSITION
                ),
                isUserLogin = userSession.isLoggedIn
            )
        )
    }

    fun onGetBalanceWidgetLoadingState(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel
        return currentHeaderDataModel.copy(
            headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                isUserLogin = userSession.isLoggedIn,
                homeBalanceModel = HomeBalanceModel(balancePositionSubscriptions = currentHeaderDataModel.headerDataModel?.homeBalanceModel?.balancePositionSubscriptions?: DEFAULT_BALANCE_POSITION)
            )
        )
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
            HomeServerLogger.logWarning(
                type = HomeServerLogger.TYPE_SUBSCRIPTION_ERROR,
                throwable = MessageErrorException(e.localizedMessage),
                reason = ERROR_UNABLE_TO_PARSE_BALANCE_WIDGET_SUBSCRIPTION
            )
            throw JsonParseException(e)
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
            homeBalanceModel.mapErrorWallet(headerTitle, position)
        }
        return homeBalanceModel
    }
}
