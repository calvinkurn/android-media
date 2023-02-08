package com.tokopedia.home.beranda.domain.interactor.usecase

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.tokopedia.home.beranda.data.model.SubscriptionsData
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.GetHomeBalanceWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel.Companion.DEFAULT_BALANCE_POSITION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderAtf2DataModel
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class HomeBalanceWidgetAtf2UseCase @Inject constructor(
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

    suspend fun onGetBalanceWidgetData(previousHeaderDataModel: HomeHeaderAtf2DataModel? = null): HomeHeaderAtf2DataModel {
        val homeHeaderAtf2DataModel = HomeHeaderAtf2DataModel()
        if (!userSession.isLoggedIn) return homeHeaderAtf2DataModel

        try {
            val getHomeBalanceWidget = getHomeBalanceWidgetRepository.getRemoteData()
            if (getHomeBalanceWidget.getHomeBalanceList.error.isNotBlank()) {
                throw MessageErrorException(getHomeBalanceWidget.getHomeBalanceList.error)
            } else {
                homeHeaderAtf2DataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.clear()
                var homeBalanceModel = getHomeBalanceModel(homeHeaderAtf2DataModel)
                var indexDataRegistered = 0

                getHomeBalanceWidget.getHomeBalanceList.balancesList.forEach { getHomeBalanceItem ->
                    when (getHomeBalanceItem.type) {
                        BALANCE_TYPE_GOPAY -> {
                            val placeHolderGopay = BalanceDrawerItemModel(
                                drawerItemType = TYPE_WALLET_APP_LINKED,
                                headerTitle = getHomeBalanceItem.title
                            )
                            homeBalanceModel.balanceDrawerItemModels.add(placeHolderGopay)
                            indexDataRegistered++
                        }
                        BALANCE_TYPE_REWARDS -> {
                            val placeHolderRewards = BalanceDrawerItemModel(
                                drawerItemType = TYPE_REWARDS,
                                headerTitle = getHomeBalanceItem.title
                            )
                            homeBalanceModel.balanceDrawerItemModels.add(placeHolderRewards)
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

                if (previousHeaderDataModel != null) {
                    homeBalanceModel.balanceDrawerItemModels.forEachIndexed { index, it ->
                        if (it.state == BalanceDrawerItemModel.STATE_LOADING) {
                            if (it.drawerItemType == TYPE_WALLET_APP_LINKED) {
                                homeBalanceModel =
                                    getDataUsingWalletApp(homeBalanceModel, it.headerTitle, index)
                            } else if (it.drawerItemType == TYPE_REWARDS) {
                                homeBalanceModel =
                                    getTokopointData(homeBalanceModel, it.headerTitle, index)
                            }
                        }
                    }
                }
                return homeHeaderAtf2DataModel.copy(
                    headerDataModel = homeHeaderAtf2DataModel.headerDataModel?.copy(
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
            homeHeaderAtf2DataModel.headerDataModel?.homeBalanceModel?.status = HomeBalanceModel.STATUS_ERROR
            homeHeaderAtf2DataModel.headerDataModel?.isUserLogin = userSession.isLoggedIn
            return homeHeaderAtf2DataModel
        }
    }

    private fun getHomeBalanceModel(currentHeaderDataModel: HomeHeaderAtf2DataModel): HomeBalanceModel {
        return HomeBalanceModel().apply {
            val currentHomeBalanceModel = currentHeaderDataModel.headerDataModel?.homeBalanceModel
                ?: HomeBalanceModel()
            balanceDrawerItemModels = currentHomeBalanceModel.balanceDrawerItemModels
        }
    }

    suspend fun onGetTokopointData(
        currentHeaderDataModel: HomeHeaderAtf2DataModel,
        position: Int,
        headerTitle: String
    ): HomeHeaderAtf2DataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel)
        homeBalanceModel = getTokopointData(homeBalanceModel, headerTitle, position)
        return currentHeaderDataModel.copy(
            headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                homeBalanceModel = homeBalanceModel.copy(
                    status = HomeBalanceModel.STATUS_SUCCESS,
                    balancePositionSubscriptions = currentHeaderDataModel.headerDataModel?.homeBalanceModel?.balancePositionSubscriptions
                        ?: DEFAULT_BALANCE_POSITION
                ),
                isUserLogin = userSession.isLoggedIn
            )
        )
    }

    suspend fun onGetWalletAppData(
        currentHeaderDataModel: HomeHeaderAtf2DataModel,
        position: Int,
        headerTitle: String
    ): HomeHeaderAtf2DataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(currentHeaderDataModel)
        homeBalanceModel = getDataUsingWalletApp(homeBalanceModel, headerTitle, position)
        return currentHeaderDataModel.copy(
            headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                homeBalanceModel = homeBalanceModel.copy(
                    status = HomeBalanceModel.STATUS_SUCCESS,
                    balancePositionSubscriptions = currentHeaderDataModel.headerDataModel?.homeBalanceModel?.balancePositionSubscriptions
                        ?: DEFAULT_BALANCE_POSITION
                ),
                isUserLogin = userSession.isLoggedIn
            )
        )
    }

    fun onGetBalanceWidgetLoadingState(currentHeaderDataModel: HomeHeaderAtf2DataModel): HomeHeaderAtf2DataModel {
        return if (!userSession.isLoggedIn) {
            currentHeaderDataModel
        } else if (currentHeaderDataModel.headerDataModel?.homeBalanceModel?.status == HomeBalanceModel.STATUS_LOADING) {
            currentHeaderDataModel.copy(
                headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                    isUserLogin = userSession.isLoggedIn,
                    homeBalanceModel = HomeBalanceModel(
                        balancePositionSubscriptions = currentHeaderDataModel.headerDataModel?.homeBalanceModel?.balancePositionSubscriptions
                            ?: DEFAULT_BALANCE_POSITION
                    )
                )
            )
        } else {
            currentHeaderDataModel.copy(
                headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                    homeBalanceModel = currentHeaderDataModel.headerDataModel?.homeBalanceModel?.copy(
                        status = HomeBalanceModel.STATUS_LOADING
                    ) ?: HomeBalanceModel()
                )
            )
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
