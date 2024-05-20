package com.tokopedia.home.beranda.data.newatf.balance

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemVisitable
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home_component.util.recordCrashlytics
import com.tokopedia.home_component.widget.common.DataStatus
import com.tokopedia.home_component.widget.common.isError
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
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
@SuppressLint("PII Data Exposure")
@HomeScope
class BalanceWidgetUseCase @Inject constructor(
    private val homeDispatcher: CoroutineDispatchers,
    private val getHomeBalanceWidgetUseCase: GetHomeBalanceWidgetUseCase,
    private val homeWalletRepository: HomeWalletAppRepository,
    private val homeRewardsRepository: HomeTokopointsListRepository,
    private val balanceWidgetMapper: BalanceWidgetMapper,
    private val userSession: UserSessionInterface,
    @ApplicationContext private val context: Context
): BaseAtfRepository(), CoroutineScope {

    companion object {
        const val PARAM = "param"
        const val ERROR_UNABLE_TO_PARSE_WALLET = "Unable to parse wallet, wallet app list is empty"
        const val ERROR_NO_SUPPORTED_WALLET =
            "Unable to find wallet balance, possibly empty wallet app balance list or no supported wallet partner found in response"
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + homeDispatcher.io

    private var balanceWidgetModel = DynamicBalanceWidgetModel()

    override suspend fun getData(atfMetadata: AtfMetadata) {
        if(!userSession.isLoggedIn) {
            emitLoginWidget(atfMetadata)
            return
        }
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
            addPlaceholder(atfMetadata, balanceWidget)
            fetchFullData()
        }
    }

    fun fetchFullData() {
        if(!userSession.isLoggedIn) return
        balanceWidgetModel.balanceItems.forEachIndexed { idx, it ->
            when(it.type) {
                BalanceItemModel.GOPAY -> getWalletData()
                BalanceItemModel.REWARDS -> getRewardsData()
                BalanceItemModel.ADDRESS -> getAddressData()
            }
        }
    }

    fun refresh(contentType: BalanceItemVisitable.ContentType) {
        if(!userSession.isLoggedIn) return
        when(contentType) {
            is BalanceItemVisitable.ContentType.GoPay -> getWalletData()
            is BalanceItemVisitable.ContentType.Rewards -> getRewardsData()
            is BalanceItemVisitable.ContentType.Address -> getAddressData()
            else -> { }
        }
    }

    private suspend fun emitLoginWidget(atfMetadata: AtfMetadata) {
        balanceWidgetModel = DynamicBalanceWidgetModel(
            isLoggedIn = false
        )
        emitData(
            AtfData(
                atfMetadata = atfMetadata,
                atfStatus = AtfKey.STATUS_SUCCESS,
                isCache = false,
                atfContent = balanceWidgetModel
            )
        )
    }

    private suspend fun addPlaceholder(
        atfMetadata: AtfMetadata,
        getHomeBalanceWidgetData: GetHomeBalanceWidgetData
    ) {
        val newTypeList = getHomeBalanceWidgetData.getHomeBalanceList.balancesList.map { it.type }
        val currentTypeList = balanceWidgetModel.balanceItems.map { it.type }
        if(newTypeList == currentTypeList) return
        balanceWidgetModel = DynamicBalanceWidgetModel(
            getHomeBalanceWidgetData.getHomeBalanceList.balancesList.map {
                it.loading()
            }
        )
        emitData(AtfData(atfMetadata = atfMetadata, isCache = false, atfContent = balanceWidgetModel))
    }

    private fun getWalletData() {
        launch {
            val type = BalanceItemModel.GOPAY
            try {
                conditionalLoading(type)
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
                e.recordCrashlytics()
            }
        }
    }

    private fun getRewardsData() {
        launch {
            val type = BalanceItemModel.REWARDS
            try {
                conditionalLoading(type)
                val rewardsData = homeRewardsRepository.getRemoteData()
                rewardsData.tokopointsDrawerList.drawerList.firstOrNull()?.let {
                    val balanceItemModel = balanceWidgetMapper.mapToBalanceItemModel(it, type) ?: return@let
                    updateBalanceItem(balanceItemModel, type)
                } ?: updateState(state = DataStatus.ERROR, type = type)
            } catch (e: Exception) {
                updateState(state = DataStatus.ERROR, type = type)
                e.recordCrashlytics()
            }
        }
    }

    private fun getAddressData() {
        launch {
            val type = BalanceItemModel.ADDRESS
            val address = ChooseAddressUtils.getLocalizingAddressData(context)
            val balanceItem = balanceWidgetMapper.mapToBalanceItemModel(address, type)
            updateBalanceItem(balanceItem, type)
        }
    }

    private fun GetHomeBalanceItem.loading(): BalanceItemModel {
        return BalanceItemModel(
            state = DataStatus.LOADING,
            type = type
        )
    }

    private suspend fun conditionalLoading(type: String) {
        val currentData = getItem(type) ?: return
        if(currentData.state.isError()) {
            updateState(
                state = DataStatus.LOADING,
                type = type
            )
        }
    }

    private suspend fun initialLoading(atfMetadata: AtfMetadata) {
        if(balanceWidgetModel.balanceItems.isEmpty()) {
            emitData(
                AtfData(
                    atfMetadata = atfMetadata,
                    isCache = false,
                    atfStatus = AtfKey.STATUS_LOADING
                )
            )
        }
    }

    private suspend fun updateBalanceItem(
        model: BalanceItemModel,
        type: String
    ) {
        val atfData = flow.value.firstOrNull() ?: return

        val balanceItems = balanceWidgetModel.balanceItems.toMutableList()
        val index = getIndexOfType(type) ?: return
        balanceItems[index] = model
        balanceWidgetModel = balanceWidgetModel.copy(balanceItems = balanceItems)

        emitData(
            atfData.copy(
                atfContent = balanceWidgetModel,
                atfStatus = AtfKey.STATUS_SUCCESS
            )
        )
    }

    private suspend fun updateState(
        state: DataStatus,
        type: String
    ) {
        val atfData = flow.value.firstOrNull() ?: return

        val balanceItems = balanceWidgetModel.balanceItems.toMutableList()
        val index = getIndexOfType(type) ?: return
        balanceItems[index] = balanceItems[index].copy(state = state)
        balanceWidgetModel = balanceWidgetModel.copy(balanceItems = balanceItems)

        emitData(atfData.copy(atfContent = balanceWidgetModel))
    }

    private fun getIndexOfType(type: String): Int? {
        return balanceWidgetModel.balanceItems.indexOfFirst { it.type == type }.takeIf { it != -1 }
    }

    private fun getItem(type: String): BalanceItemModel? {
        return balanceWidgetModel.balanceItems.firstOrNull { it.type == type }
    }
}
