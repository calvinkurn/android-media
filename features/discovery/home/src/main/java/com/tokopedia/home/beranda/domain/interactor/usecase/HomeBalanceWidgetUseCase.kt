package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.PendingCashbackModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

class HomeBalanceWidgetUseCase @Inject constructor(
        private val homeWalletAppRepository: HomeWalletAppRepository,
        private val homeWalletEligibilityRepository: HomeWalletEligibilityRepository,
        private val homeOvoWalletRepository: HomeOvoWalletRepository,
        private val homePendingCashbackRepository: HomePendingCashbackRepository,
        private val homeTokopointsListRepository: HomeTokopointsListRepository,
        private val homeFlagRepository: HomeFlagRepository,
        private val userSession: UserSessionInterface
        ) {

    companion object {
        const val error_unable_to_parse_wallet = "Unable to parse wallet, wallet app list is empty"
    }

    var currentEligibility: Boolean? = null
    private suspend fun isGopayEligible(): Boolean {
        if (currentEligibility == null) {
            currentEligibility = try {
                homeWalletEligibilityRepository.getRemoteData().isGoPointsEligible
            } catch (e: Exception) {
                false
            }
        }
        return currentEligibility!!
    }

    private suspend fun walletAppAbTestCondition(
            isGopayEligible: Boolean = false,
            isUsingWalletApp: suspend () -> Unit,
            isUsingOldWallet: suspend () -> Unit
    ) {
        if (isGopayEligible) {
            isUsingWalletApp.invoke()
        } else {
            isUsingOldWallet.invoke()
        }
    }

    suspend fun onGetBalanceWidgetData(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(isGopayEligible(), currentHeaderDataModel)

        walletAppAbTestCondition(
                isGopayEligible = isGopayEligible(),
                isUsingWalletApp = {
                    homeBalanceModel = getDataUsingWalletApp(homeBalanceModel)
                },
                isUsingOldWallet = {
                    homeBalanceModel = getDataUsingWalletOvo(homeBalanceModel)
                }
        )

        homeBalanceModel = getTokopointData(homeBalanceModel)
        return currentHeaderDataModel.copy(headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                homeBalanceModel = homeBalanceModel,
                isUserLogin = userSession.isLoggedIn
        ),
                needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
        )
    }

    private suspend fun getHomeBalanceModel(isGopayEligible: Boolean, currentHeaderDataModel: HomeHeaderDataModel): HomeBalanceModel {
        return HomeBalanceModel(
                isGopayEligible = isGopayEligible
        ).apply {
            val currentHomeBalanceModel = currentHeaderDataModel.headerDataModel?.homeBalanceModel
                    ?: HomeBalanceModel()
            balanceDrawerItemModels = currentHomeBalanceModel.balanceDrawerItemModels
            balanceType = when(homeFlagRepository.getCachedData().homeFlag.getFlagValue(HomeFlag.TYPE.HAS_TOKOPOINTS)) {
                1 -> {
                    HomeBalanceModel.TYPE_STATE_1
                }
                2 -> {
                    HomeBalanceModel.TYPE_STATE_2
                }
                3 -> {
                    HomeBalanceModel.TYPE_STATE_3
                }
                else -> {
                    HomeBalanceModel.TYPE_STATE_4
                }
            }
            initBalanceModelByType()
        }
    }

    suspend fun onGetWalletData(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(isGopayEligible(), currentHeaderDataModel)
        walletAppAbTestCondition(
                isGopayEligible = isGopayEligible(),
                isUsingWalletApp = {
                    homeBalanceModel = getDataUsingWalletApp(homeBalanceModel)
                },
                isUsingOldWallet = {
                    homeBalanceModel = getDataUsingWalletOvo(homeBalanceModel)
                }
        )
        return currentHeaderDataModel.copy(
                headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel,
                        isUserLogin = userSession.isLoggedIn
                ),
                needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
        )
    }

    suspend fun onGetTokopointData(currentHeaderDataModel: HomeHeaderDataModel): HomeHeaderDataModel {
        if (!userSession.isLoggedIn) return currentHeaderDataModel

        var homeBalanceModel = getHomeBalanceModel(isGopayEligible(), currentHeaderDataModel)
        homeBalanceModel = getTokopointData(homeBalanceModel)
        return currentHeaderDataModel.copy(
                headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel,
                        isUserLogin = userSession.isLoggedIn
                ),
                needToShowUserWallet = homeFlagRepository.getCachedData().homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
        )
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

    private suspend fun getDataUsingWalletOvo(homeBalanceModel: HomeBalanceModel): HomeBalanceModel {
        try {
            val walletData = homeOvoWalletRepository.getRemoteData()
            walletData.let {
                if (!walletData.isLinked) {
                    try {
                        val pendingCashback = homePendingCashbackRepository.getRemoteData()
                        pendingCashback.let { pendingData ->
                            homeBalanceModel.mapBalanceData(
                                    homeHeaderWalletAction = walletData.copy(cashBalance = pendingData.amountText),
                                    pendingCashBackData = PendingCashbackModel(
                                            pendingCashback = pendingData,
                                            labelActionButton = walletData.labelActionButton,
                                            labelTitle = walletData.labelTitle,
                                            walletType = walletData.walletType
                                    )
                            )
                        }
                    } catch (e: Exception) {
                        homeBalanceModel.isTokopointsOrOvoFailed = true
                        homeBalanceModel.mapErrorWallet(isWalletApp = false)
                    }
                } else {
                    homeBalanceModel.mapBalanceData(homeHeaderWalletAction = walletData)
                }
            }
        } catch (e: Exception) {
            homeBalanceModel.isTokopointsOrOvoFailed = true
            homeBalanceModel.mapErrorWallet(isWalletApp = false)
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