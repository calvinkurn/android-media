package com.tokopedia.home_account.ui.fundsAndInvestment

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

class FundsAndInvestmentViewModel @Inject constructor(
    private val getCentralizedUserAssetConfigUseCase: GetCentralizedUserAssetConfigUseCase,
    private val getTokoPointsBalanceAndPointUseCase: GetTokopointsBalanceAndPointUseCase,
    private val getSaldoBalanceUseCase: GetSaldoBalanceUseCase,
    private val getCoBrandCCBalanceAndPointUseCase: GetCoBrandCCBalanceAndPointUseCase,
    private val getBalanceAndPointUseCase: GetBalanceAndPointUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _uiState = MutableLiveData<FundsAndInvestmentResult>()
    val uiState : LiveData<FundsAndInvestmentResult> get() = _uiState

    init {
        getCentralizedUserAssetConfig(false)
    }

    fun getCentralizedUserAssetConfig(isRefreshData: Boolean) {
        _uiState.value = FundsAndInvestmentResult.Loading(isRefreshData)

        launchCatchError(
            block = {
                val response = getCentralizedUserAssetConfigUseCase(ASSET_PAGE)
                val currentListVertical = mutableListOf<WalletUiModel>()
                val currentListHorizontal = mutableListOf<WalletUiModel>()

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
                _uiState.value =
                    FundsAndInvestmentResult.Content(
                        listVertical = currentListVertical,
                        listHorizontal = currentListHorizontal
                    )

                setUpVerticalList(currentListVertical)
                setUpHorizontalList(currentListHorizontal)
            },
            onError = {
                _uiState.value = FundsAndInvestmentResult.Failed
            }
        )
    }

    private fun setUpVerticalList(currentListVertical: List<WalletUiModel>) {
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

    private fun setUpHorizontalList(currentListHorizontal: List<WalletUiModel>) {
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
        when (val state = _uiState.value) {
            is FundsAndInvestmentResult.Content -> {
                loadRefreshItem(item, state)
            }
            else -> {}
        }
    }

    private fun loadRefreshItem(item: WalletUiModel, state: FundsAndInvestmentResult.Content) {
        val currentListVertical = state.listVertical.toMutableList()
        val currentListHorizontal = state.listHorizontal.toMutableList()

        val index = getIndexFromId(item = item, verticalList = currentListVertical, horizontalList = currentListHorizontal)
        val result = setItemLoadingState(item = item, index = index, verticalList = currentListVertical, horizontalList = currentListHorizontal)

        val listData = changeItem(index, result)

        //trigger recomposition state item to loading
        _uiState.value = FundsAndInvestmentResult.Content(
            listVertical = listData.first,
            listHorizontal = listData.second
        )

        getBalanceAndPoint(
            index = index,
            walletId = item.id,
            hideTitleText = item.hideTitle,
            titleText = item.title,
            isVertical = item.isVertical
        )
    }

    private fun changeItem(index: Int, newItem: WalletUiModel): Pair<List<WalletUiModel>, List<WalletUiModel>> {
        var listVertical = mutableListOf<WalletUiModel>()
        var listHorizontal = mutableListOf<WalletUiModel>()
        when (val state = _uiState.value) {
            is FundsAndInvestmentResult.Content -> {
                listVertical = state.listVertical.toMutableList()
                listHorizontal = state.listHorizontal.toMutableList()
                if (newItem.isVertical) {
                    listVertical[index] = newItem
                } else {
                    listHorizontal[index] = newItem
                }
            }
            else -> {}
        }

        return Pair(listVertical, listHorizontal)
    }

    private fun getBalanceAndPoint(index: Int, walletId: String, hideTitleText: Boolean, titleText: String, isVertical: Boolean) {
        launchCatchError(
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
                val listData = changeItem(index, newData)

                //trigger recomposition state item to new data
                _uiState.value =
                    FundsAndInvestmentResult.Content(
                        listVertical = listData.first,
                        listHorizontal = listData.second
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
                val listData = changeItem(index, newData)

                //trigger recomposition state item to failed
                _uiState.value =
                    FundsAndInvestmentResult.Content(
                        listVertical = listData.first,
                        listHorizontal = listData.second
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
        const val GOPAY_PARTNER_CODE = "PEMUDA"
        const val GOPAYLATER_PARTNER_CODE = "PEMUDAPAYLATER"
        const val GOPAYLATERCICIL_PARTNER_CODE = "PEMUDACICIL"
        const val OVO_PARTNER_CODE = "OVO"
        const val ASSET_PAGE = "asset_page"
    }

}

sealed interface FundsAndInvestmentResult {
    data class Loading(
        val isRefreshData: Boolean
    ) : FundsAndInvestmentResult
    data class Content(
        val listVertical: List<WalletUiModel>,
        val listHorizontal: List<WalletUiModel>
    ) : FundsAndInvestmentResult
    object Failed : FundsAndInvestmentResult
}
