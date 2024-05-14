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
import com.tokopedia.home.beranda.domain.interactor.GetHomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemVisitable
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
    private val getHomeBalanceWidgetUseCase: GetHomeBalanceWidgetUseCase,
    private val homeWalletRepository: HomeWalletAppRepository,
    private val homeRewardsRepository: HomeTokopointsListRepository,
    private val userSession: UserSessionInterface,
    private val balanceWidgetMapper: BalanceWidgetMapper,
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
        initialLoading(atfMetadata)

        val param = Bundle().apply {
            putString(
                PARAM,
                atfMetadata.param
            )
        }

        val balanceWidget = getHomeBalanceWidgetUseCase.getRemoteData(param)
        if (balanceWidget.getHomeBalanceList.error.isNotBlank()) {
            throw MessageErrorException(balanceWidget.getHomeBalanceList.error)
        } else {
            addPlaceholder(balanceWidget)
            fetchFullData()
        }
    }

    fun refresh(contentType: BalanceItemVisitable.ContentType) {
        when(contentType) {
            is BalanceItemVisitable.ContentType.GoPay -> getWalletData()
            is BalanceItemVisitable.ContentType.Rewards -> getRewardsData()
            else -> { }
        }
    }

    private fun addPlaceholder(getHomeBalanceWidgetData: GetHomeBalanceWidgetData) {
        balanceWidgetModel = DynamicBalanceWidgetModel(
            getHomeBalanceWidgetData.getHomeBalanceList.balancesList.map {
                it.loading()
            }
        )
    }

    fun fetchFullData() {
        balanceWidgetModel.balanceItems.forEachIndexed { idx, it ->
            when(it.type) {
                BalanceItemModel.GOPAY -> getWalletData()
                BalanceItemModel.REWARDS -> getRewardsData()
            }
        }
    }

    fun getWalletData() {
        launch {
            val type = BalanceItemModel.GOPAY
            try {
                updateState(DataStatus.LOADING, type)
                val walletAppData = homeWalletRepository.getRemoteData()
                if (walletAppData.walletappGetBalance.balances.isNotEmpty()) {
                    val balanceItemModel = walletAppData.walletappGetBalance.balances.firstOrNull()?.let {
                        balanceWidgetMapper.mapToBalanceItemModel(it, type)
                    }.ifNull {
                        HomeServerLogger.logWarning(
                            type = HomeServerLogger.TYPE_WALLET_APP_ERROR,
                            throwable = MessageErrorException(ERROR_NO_SUPPORTED_WALLET),
                            reason = ERROR_NO_SUPPORTED_WALLET
                        )
                        throw IllegalStateException(ERROR_NO_SUPPORTED_WALLET)
                    }
                    updateBalanceItem(balanceItemModel, type)
                } else {
                    HomeServerLogger.logWarning(
                        type = HomeServerLogger.TYPE_WALLET_APP_ERROR,
                        throwable = MessageErrorException(ERROR_UNABLE_TO_PARSE_WALLET),
                        reason = ERROR_UNABLE_TO_PARSE_WALLET
                    )
                    throw IllegalStateException(ERROR_UNABLE_TO_PARSE_WALLET)
                }
            } catch (e: Exception) {
                updateState(state = DataStatus.ERROR, type = type)
            }
        }
    }

    fun getRewardsData() {
        launch {
            val type = BalanceItemModel.REWARDS
            try {
                updateState(DataStatus.LOADING, type)
                val rewardsData = homeRewardsRepository.getRemoteData()
                rewardsData.tokopointsDrawerList.drawerList.firstOrNull()?.let {
                    val balanceItemModel = balanceWidgetMapper.mapToBalanceItemModel(it, type)
                    updateBalanceItem(balanceItemModel, type)
                } ?: updateState(state = DataStatus.ERROR, type = type)
            } catch (e: Exception) {
                updateState(state = DataStatus.ERROR, type = type)
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun GetHomeBalanceItem.loading(): BalanceItemModel {
        return BalanceItemModel(
            state = DataStatus.LOADING,
            type = type
        )
    }

    private suspend fun initialLoading(atfMetadata: AtfMetadata) {
        emitData(
            AtfData(
                atfMetadata = atfMetadata,
                isCache = false,
                atfStatus = AtfKey.STATUS_LOADING
            )
        )
    }

    private suspend fun updateBalanceItem(
        model: BalanceItemModel,
        type: String
    ) {
        val atfData = flow.value.firstOrNull() ?: return

        val balanceItems = balanceWidgetModel.balanceItems.toMutableList()
        val index = balanceItems.indexOfFirst { it.type == type }
        if(index == -1) return
        balanceItems[index] = model
        balanceWidgetModel = balanceWidgetModel.copy(balanceItems)

        emitData(atfData.copy(atfContent = balanceWidgetModel))
    }

    private suspend fun updateState(
        state: DataStatus,
        type: String
    ) {
        val atfData = flow.value.firstOrNull() ?: return

        val balanceItems = balanceWidgetModel.balanceItems.toMutableList()
        val index = balanceItems.indexOfFirst { it.type == type }
        if(index == -1) return
        balanceItems[index] = balanceItems[index].copy(state = state)
        balanceWidgetModel = balanceWidgetModel.copy(balanceItems = balanceItems)

        emitData(atfData.copy(atfContent = balanceWidgetModel))
    }
}
