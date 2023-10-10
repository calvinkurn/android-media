package com.tokopedia.home_account.fundsAndInvestment

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.AssetConfig
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

                setUpVerticalList(response.data.assetConfigVertical)
                setUpHorizontalList(response.data.assetConfigHorizontal)

                _uiState.postValue(
                    FundsAndInvestmentResult.Recomposition(
                        listVertical = currentListVertical,
                        listHorizontal = currentListHorizontal
                    )
                )
            },
            onError = {
                _uiState.postValue(FundsAndInvestmentResult.Failed(it))
            }
        )
    }

    private suspend fun setUpVerticalList(listVertical: List<AssetConfig>) {
        if (listVertical.isNotEmpty()) {
            listVertical.forEach {
                val title = it.getTitle()
                currentListVertical.add(
                    getBalanceAndPoint(
                        walletId = it.id,
                        hideTitleText = it.hideTitle,
                        titleText = title,
                        isVertical = true
                    )
                )
            }
        }
    }

    private suspend fun setUpHorizontalList(listHorizontal: List<AssetConfig>) {
        if (listHorizontal.isNotEmpty()) {
            listHorizontal.forEach {
                currentListHorizontal.add(
                    getBalanceAndPoint(
                        walletId = it.id,
                        hideTitleText = it.hideTitle,
                        titleText = it.title,
                        isVertical = false
                    )
                )
            }
        }
    }

    fun refreshItem(item: WalletUiModel) {
        val index = getIndex(item = item, verticalList = currentListVertical, horizontalList = currentListHorizontal)
        var result = setItemLoadingState(item = item, index = index, verticalList = currentListVertical, horizontalList = currentListHorizontal)

        changeItem(index, result)

        //trigger recomposition state item to loading
        _uiState.value = FundsAndInvestmentResult.Recomposition(
            listVertical = currentListVertical,
            listHorizontal = currentListHorizontal
        )

        launchCatchError(context = dispatcher.io,
            block = {
                result = getBalanceAndPoint(
                    walletId = item.id,
                    hideTitleText = item.hideTitle,
                    titleText = item.title,
                    isVertical = item.isVertical
                )

                changeItem(index, result)

                //trigger recomposition item to new data
                _uiState.postValue(
                    FundsAndInvestmentResult.Recomposition(
                        listVertical = currentListVertical,
                        listHorizontal = currentListHorizontal
                    )
                )
            },
            onError = {
                result = WalletUiModel(
                    id = item.id,
                    title = item.title,
                    isVertical = item.isVertical,
                    hideTitle = item.hideTitle,
                    isFailed = true
                )

                changeItem(index, result)

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

    private fun changeItem(index: Int, newItem: WalletUiModel) {
        if (newItem.isVertical) {
            currentListVertical[index] = newItem
        } else {
            currentListHorizontal[index] = newItem
        }
    }

    private suspend fun getBalanceAndPoint(walletId: String, hideTitleText: Boolean, titleText: String, isVertical: Boolean): WalletUiModel {
        try {
            val item: WalletappGetAccountBalance = when (walletId) {
                AccountConstants.WALLET.TOKOPOINT -> {
                    val result = getTokoPointsBalanceAndPointUseCase(Unit)
                    result.data.apply {
                        title = titleText
                        hideTitle = hideTitleText
                    }
                    result.data
                }
                AccountConstants.WALLET.SALDO -> {
                    val result = getSaldoBalanceUseCase(Unit)
                    result.data.apply {
                        hideTitle = true
                    }
                    result.data
                }
                AccountConstants.WALLET.CO_BRAND_CC -> {
                    val result = getCoBrandCCBalanceAndPointUseCase(Unit)
                    result.data.apply {
                        titleAsset = titleText
                        hideTitle = hideTitleText
                    }
                    result.data
                }
                else -> {
                    getOtherBalanceAndPoint(walletId, hideTitleText)
                }
            }
            return setBalanceAndPointValue(item, titleText, isVertical)
        } catch (e: Exception) {
            return WalletUiModel(
                id = walletId,
                title = titleText,
                isVertical = isVertical,
                hideTitle = hideTitleText,
                isFailed = true
            )
        }
    }

    private suspend fun getOtherBalanceAndPoint(walletId: String, hideTitleText: Boolean): WalletappGetAccountBalance {
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

        val response = getBalanceAndPointUseCase(partnerCode).data.apply {
            hideTitle = hideTitleText
        }

        return response
    }

    private fun setBalanceAndPointValue(data: WalletappGetAccountBalance, titleText: String, isVertical: Boolean): WalletUiModel {
        val wallet = UiModelMapper.getWalletUiModel(data).apply {
            title = titleText
            this.isVertical = isVertical
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
