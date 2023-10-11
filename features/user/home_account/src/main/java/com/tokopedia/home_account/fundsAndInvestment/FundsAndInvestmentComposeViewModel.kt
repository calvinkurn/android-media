package com.tokopedia.home_account.fundsAndInvestment

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.domain.usecase.GetBalanceAndPointUseCase
import com.tokopedia.home_account.domain.usecase.GetCentralizedUserAssetConfigUseCase
import com.tokopedia.home_account.domain.usecase.GetCoBrandCCBalanceAndPointUseCase
import com.tokopedia.home_account.domain.usecase.GetSaldoBalanceUseCase
import com.tokopedia.home_account.domain.usecase.GetTokopointsBalanceAndPointUseCase
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.home_account.view.mapper.UiModelMapper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class FundsAndInvestmentComposeViewModel @Inject constructor(
    private val getCentralizedUserAssetConfigUseCase: GetCentralizedUserAssetConfigUseCase,
    private val getTokoPointsBalanceAndPointUseCase: GetTokopointsBalanceAndPointUseCase,
    private val getSaldoBalanceUseCase: GetSaldoBalanceUseCase,
    private val getCoBrandCCBalanceAndPointUseCase: GetCoBrandCCBalanceAndPointUseCase,
    private val getBalanceAndPointUseCase: GetBalanceAndPointUseCase,
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val currentListVertical = mutableStateListOf<WalletUiModel>()
    private val currentListHorizontal = mutableStateListOf<WalletUiModel>()

    private val _uiState = MutableLiveData<FundsAndInvestmentResult>()
    val uiState : LiveData<FundsAndInvestmentResult> get() = _uiState

    init {
        getCentralizedUserAssetConfig(isRefreshData = false)
    }

    fun getCentralizedUserAssetConfig(isRefreshData: Boolean) {
        _uiState.value = FundsAndInvestmentResult.Loading(isRefreshData = isRefreshData)

        launchCatchError(context = dispatcher.io,
            block = {
                val response = getCentralizedUserAssetConfigUseCase(ASSET_PAGE)

                currentListVertical.clear()
                currentListHorizontal.clear()

                response.data.assetConfigVertical.forEach {
                    currentListVertical.add(
                        WalletUiModel(
                            id = it.id,
                            title = it.getTitle(),
                            hideTitle = it.hideTitle,
                            isLoading = true,
                            isVertical = true
                        )
                    )
                }
                response.data.assetConfigHorizontal.forEach {
                    currentListHorizontal.add(
                        WalletUiModel(
                            id = it.id,
                            title = it.getTitle(),
                            hideTitle = it.hideTitle,
                            isLoading = true,
                            isVertical = false
                        )
                    )
                }

                //trigger recomposition state all item to loading
                _uiState.postValue(
                    FundsAndInvestmentResult.Recomposition(
                        listVertical = currentListVertical,
                        listHorizontal = currentListHorizontal
                    )
                )

                setUpVerticalList()
                setUpHorizontalList()
            },
            onError = {
                _uiState.postValue(FundsAndInvestmentResult.Failed(it))
            }
        )
    }

    private fun setUpVerticalList() {
        if (currentListVertical.isNotEmpty()) {
            currentListVertical.forEachIndexed { index, assetConfig ->
                getBalanceAndPoint(
                    index = index,
                    walletId = assetConfig.id,
                    hideTitleText = assetConfig.hideTitle,
                    titleText = assetConfig.title,
                    isVertical = true
                )
            }
        }
    }

    private fun setUpHorizontalList() {
        if (currentListHorizontal.isNotEmpty()) {
            currentListHorizontal.forEachIndexed { index, assetConfig ->
                getBalanceAndPoint(
                    index = index,
                    walletId = assetConfig.id,
                    hideTitleText = assetConfig.hideTitle,
                    titleText = assetConfig.title,
                    isVertical = false
                )
            }
        }
    }

    fun refreshItem(item: WalletUiModel) {
        val index = getIndex(item = item, verticalList = currentListVertical, horizontalList = currentListHorizontal)
        val result = setItemLoadingState(item = item, index = index, verticalList = currentListVertical, horizontalList = currentListHorizontal)

        changeItem(index, result)

        //trigger recomposition state item to loading
        _uiState.value = FundsAndInvestmentResult.Recomposition(
            listVertical = currentListVertical,
            listHorizontal = currentListHorizontal
        )

        getBalanceAndPoint(
            index = index,
            walletId = item.id,
            hideTitleText = item.hideTitle,
            titleText = item.title,
            isVertical = item.isVertical
        )
    }

    private fun changeItem(index: Int, newItem: WalletUiModel) {
        if (newItem.isVertical) {
            currentListVertical[index] = newItem
        } else {
            currentListHorizontal[index] = newItem
        }
    }

    private fun getBalanceAndPoint(index: Int, walletId: String, hideTitleText: Boolean, titleText: String, isVertical: Boolean) {
        launchCatchError(context = dispatcher.io,
            block = {
                val item: WalletappGetAccountBalance = when (walletId) {
                    AccountConstants.WALLET.TOKOPOINT -> {
                        getTokoPointsBalanceAndPointUseCase(Unit).data
                    }
                    AccountConstants.WALLET.SALDO -> {
                        getSaldoBalanceUseCase(Unit).data.apply {
                            hideTitle = true
                        }
                    }
                    AccountConstants.WALLET.CO_BRAND_CC -> {
                        getCoBrandCCBalanceAndPointUseCase(Unit).data
                    }
                    else -> {
                        getOtherBalanceAndPoint(walletId)
                    }
                }
                val newData = setBalanceAndPointValue(item, titleText, hideTitleText, isVertical)
                changeItem(index, newData)

                //trigger recomposition state item to new data
                _uiState.postValue(
                    FundsAndInvestmentResult.Recomposition(
                        listVertical = currentListVertical,
                        listHorizontal = currentListHorizontal
                    )
                )
            },
            onError = {
                val newData = WalletUiModel(
                    id = walletId,
                    title = titleText,
                    isVertical = isVertical,
                    hideTitle = hideTitleText,
                    isFailed = true
                )
                changeItem(index, newData)

                //trigger recomposition state item to failed
                _uiState.postValue(
                    FundsAndInvestmentResult.Recomposition(
                        listVertical = currentListVertical,
                        listHorizontal = currentListHorizontal
                    )
                )
            }
        )
    }

    private suspend fun getOtherBalanceAndPoint(walletId: String): WalletappGetAccountBalance {
        val partnerCode = when (walletId) {
            AccountConstants.WALLET.GOPAY -> {
                GOPAY_PARTNER_CODE
            }
            AccountConstants.WALLET.GOPAYLATER -> {
                GOPAYLATER_PARTNER_CODE
            }
            AccountConstants.WALLET.GOPAYLATERCICIL -> {
                GOPAYLATERCICIL_PARTNER_CODE
            }
            AccountConstants.WALLET.OVO -> {
                OVO_PARTNER_CODE
            }
            else -> ""
        }

        return getBalanceAndPointUseCase(partnerCode).data
    }

    private fun setBalanceAndPointValue(data: WalletappGetAccountBalance, titleText: String, hideTitle: Boolean, isVertical: Boolean): WalletUiModel {
        val wallet = UiModelMapper.getWalletUiModel(data).apply {
            title = titleText
            this.isVertical = isVertical
            this.hideTitle = hideTitle
        }

        return wallet
    }

    companion object {
        private const val GOPAY_PARTNER_CODE = "PEMUDA"
        private const val GOPAYLATER_PARTNER_CODE = "PEMUDAPAYLATER"
        private const val GOPAYLATERCICIL_PARTNER_CODE = "PEMUDACICIL"
        private const val OVO_PARTNER_CODE = "OVO"
        private const val ASSET_PAGE = "asset_page"
    }

}

sealed class FundsAndInvestmentResult {
    data class Loading(
        val isRefreshData: Boolean
    ) : FundsAndInvestmentResult()
    data class Recomposition(
        val listVertical: List<WalletUiModel>,
        val listHorizontal: List<WalletUiModel>
    ) : FundsAndInvestmentResult()
    data class Failed(val throwable: Throwable) : FundsAndInvestmentResult()
}
