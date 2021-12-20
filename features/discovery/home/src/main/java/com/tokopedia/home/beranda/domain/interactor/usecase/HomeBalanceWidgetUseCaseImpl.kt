package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeOvoWalletRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePendingCashbackRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletEligibilityRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.PendingCashbackModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class HomeBalanceWidgetUseCaseImpl @Inject constructor(
        private val homeWalletAppRepository: HomeWalletAppRepository,
        private val homeWalletEligibilityRepository: HomeWalletEligibilityRepository,
        private val homeOvoWalletRepository: HomeOvoWalletRepository,
        private val homePendingCashbackRepository: HomePendingCashbackRepository,
        private val userSession: UserSessionInterface
        ): HomeBalanceWidgetUseCase {

    companion object {
        const val error_unable_to_parse_wallet = "Unable to parse wallet, wallet app list is empty"
    }

    var homeHeaderDataModel = HomeHeaderOvoDataModel()

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

    override suspend fun onGetWalletData(): HeaderDataModel {
        val headerDataModel = HeaderDataModel()
        if (!userSession.isLoggedIn) return headerDataModel

        val isGopayEligible = homeWalletEligibilityRepository.getRemoteData().isGoPointsEligible
        var homeBalanceModel = HomeBalanceModel(
                isGopayEligible = isGopayEligible
        ).apply {
            val currentHomeBalanceModel = homeHeaderDataModel.headerDataModel?.homeBalanceModel?: HomeBalanceModel()
            balanceDrawerItemModels = currentHomeBalanceModel.balanceDrawerItemModels
            balanceType = currentHomeBalanceModel.balanceType
            initBalanceModelByType()
        }

        walletAppAbTestCondition(
                isGopayEligible = isGopayEligible,
                isUsingWalletApp = {
                    homeBalanceModel = getDataUsingWalletApp(homeBalanceModel)
                },
                isUsingOldWallet = {
                    homeBalanceModel = getDataUsingWalletOvo(homeBalanceModel)
                }
        )

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

    override suspend fun onGetTokopointData() {
        TODO("Not yet implemented")
    }

    override suspend fun onGetTokocashData() {
        TODO("Not yet implemented")
    }

    override suspend fun onGetTokocashPendingBalance() {
        TODO("Not yet implemented")
    }
}