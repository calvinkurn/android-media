package com.tokopedia.home.beranda.data.newatf.balance

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.model.GetHomeBalanceItem
import com.tokopedia.home.beranda.data.model.GetHomeBalanceWidgetData
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.BaseAtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.GetHomeBalanceWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home_component.widget.common.DataStatus
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Frenzel
 */
@HomeScope
class BalanceWidgetUseCase @Inject constructor(
    private val homeDispatcher: CoroutineDispatchers,
    private val getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository,
    private val homeWalletRepository: HomeWalletAppRepository,
    private val homeRewardsRepository: HomeTokopointsListRepository,
    private val userSession: UserSessionInterface,
): BaseAtfRepository(), CoroutineScope {

    companion object {
        const val PARAM = "param"
        const val ERROR_UNABLE_TO_PARSE_WALLET = "Unable to parse wallet, wallet app list is empty"
        const val ERROR_UNABLE_TO_PARSE_BALANCE_WIDGET = "Unable to parse balance widget"
        const val ERROR_NO_SUPPORTED_WALLET =
            "Unable to find wallet balance, possibly empty wallet app balance list or no supported wallet partner found in response"
        private const val ERROR_TITLE = "Gagal memuat"
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + homeDispatcher.io

    private var balanceWidgetModel = DynamicBalanceWidgetModel()

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        emitInitialLoading(atfMetadata)

        val param = Bundle().apply {
            putString(
                PARAM,
                atfMetadata.param
            )
        }

        val balanceWidget = getHomeBalanceWidgetRepository.getRemoteData(param)
        if (balanceWidget.getHomeBalanceList.error.isNotBlank()) {
            throw MessageErrorException(balanceWidget.getHomeBalanceList.error)
        } else {
            addPlaceholder(atfMetadata, balanceWidget)
            conditionalFetchBalance(atfMetadata)
        }
    }

    private suspend fun emitInitialLoading(atfMetadata: AtfMetadata) {
        emitData(
            AtfData(
                atfMetadata = atfMetadata,
                isCache = false
            )
        )
    }

    private fun conditionalFetchBalance(atfMetadata: AtfMetadata) {
        balanceWidgetModel.balanceItems.forEachIndexed { idx, it ->
            launch {
                when (it) {
                    is BalanceWalletModel -> {
                        it.initialLoading(idx, atfMetadata)
                        getWalletData().updateBalanceWidgetModel(atfMetadata)
                    }
                    is BalanceRewardsModel -> {
                        it.initialLoading(idx, atfMetadata)
                        getRewardsData().updateBalanceWidgetModel(atfMetadata)
                    }

                    else -> { }
                }
            }
        }
    }

    private suspend fun addPlaceholder(
        atfMetadata: AtfMetadata,
        getHomeBalanceWidgetData: GetHomeBalanceWidgetData
    ) {
        val balanceItems = mutableListOf<BalanceItemModel>()
        getHomeBalanceWidgetData.getHomeBalanceList.balancesList.forEachIndexed { idx, balance ->
            val model = balance.initialLoading()
            model?.let { balanceItems.add(it) }
        }
        balanceWidgetModel = DynamicBalanceWidgetModel(balanceItems)
        emitData(
            AtfData(
                atfMetadata = atfMetadata,
                atfContent = balanceWidgetModel,
                atfStatus = AtfKey.STATUS_LOADING,
                isCache = false
            )
        )
    }

    private suspend fun getWalletData(): BalanceItemModel {
        return try {
            val walletAppData = homeWalletRepository.getRemoteData()
            if (walletAppData.walletappGetBalance.balances.isNotEmpty()) {
                walletAppData.walletappGetBalance.balances.firstOrNull()?.let {
                    BalanceWalletModel(
                        data = it,
                        state = DataStatus.SUCCESS,
                    )
                }.ifNull {
                    HomeServerLogger.logWarning(
                        type = HomeServerLogger.TYPE_WALLET_APP_ERROR,
                        throwable = MessageErrorException(ERROR_NO_SUPPORTED_WALLET),
                        reason = ERROR_NO_SUPPORTED_WALLET
                    )
                    throw IllegalStateException(ERROR_NO_SUPPORTED_WALLET)
                }
            } else {
                HomeServerLogger.logWarning(
                    type = HomeServerLogger.TYPE_WALLET_APP_ERROR,
                    throwable = MessageErrorException(ERROR_UNABLE_TO_PARSE_WALLET),
                    reason = ERROR_UNABLE_TO_PARSE_WALLET
                )
                throw IllegalStateException(ERROR_UNABLE_TO_PARSE_WALLET)
            }
        } catch (e: Exception) {
            updateState(state = DataStatus.ERROR, type = BalanceItemModel.GOPAY)
        }
    }

    private suspend fun getRewardsData(): BalanceItemModel {
        return try {
            val rewardsData = homeRewardsRepository.getRemoteData()
            rewardsData.tokopointsDrawerList.drawerList.firstOrNull()?.let {
                BalanceRewardsModel(
                    data = it,
                    state = DataStatus.SUCCESS
                )
            }.ifNull {
                updateState(state = DataStatus.ERROR, type = BalanceItemModel.REWARDS)
            }
        } catch (e: Exception) {
            updateState(state = DataStatus.ERROR, type = BalanceItemModel.REWARDS)
        }
    }

    private suspend fun BalanceItemModel.updateBalanceWidgetModel(
        atfMetadata: AtfMetadata
    ) {
        val balanceItems = balanceWidgetModel.balanceItems.toMutableList()
        val index = balanceItems.indexOfFirst { it == this }
        balanceItems[index] = this
        balanceWidgetModel = balanceWidgetModel.copy(
            balanceItems = balanceItems
        )
        emitData(
            AtfData(
                atfMetadata = atfMetadata,
                atfContent = balanceWidgetModel,
                atfStatus = AtfKey.STATUS_SUCCESS,
                isCache = false
            )
        )
    }

    private fun GetHomeBalanceItem.initialLoading(): BalanceItemModel? {
        return when(type) {
            BalanceItemModel.GOPAY -> {
                BalanceWalletModel(state = DataStatus.LOADING)
            }
            BalanceItemModel.REWARDS -> {
                BalanceRewardsModel(state = DataStatus.LOADING)
            }
            else -> null
        }
    }

    private suspend fun BalanceItemModel.initialLoading(index: Int, atfMetadata: AtfMetadata) {
        val balanceItems = balanceWidgetModel.balanceItems.toMutableList()
        balanceItems[index] = updateState(DataStatus.LOADING, type)
        balanceWidgetModel = balanceWidgetModel.copy(
            balanceItems = balanceItems
        )
        emitData(
            AtfData(
                atfMetadata = atfMetadata,
                atfContent = balanceWidgetModel,
                atfStatus = AtfKey.STATUS_SUCCESS,
                isCache = false
            )
        )
    }

    private fun updateState(
        state: DataStatus,
        type: String
    ): BalanceItemModel {
        return when(type) {
            BalanceItemModel.GOPAY -> {
                BalanceWalletModel(state = state)
            }
            BalanceItemModel.REWARDS -> {
                BalanceRewardsModel(state = state)
            }

            else -> object : BalanceItemModel {
                override val state: DataStatus
                    get() = state

            }
        }
    }
}
