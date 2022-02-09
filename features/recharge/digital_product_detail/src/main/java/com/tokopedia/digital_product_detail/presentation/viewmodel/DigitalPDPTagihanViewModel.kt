package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTagihanListrikRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPTagihanViewModel @Inject constructor(
    val repo: DigitalPDPTagihanListrikRepository,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private var loadingJob: Job? = null

    var isEligibleToBuy = false

    var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(
        RechargeCatalogPrefixSelect()
    )

    private val _menuDetailData = MutableLiveData<RechargeNetworkResult<MenuDetailModel>>()
    val menuDetailData: LiveData<RechargeNetworkResult<MenuDetailModel>>
        get() = _menuDetailData

    private val _favoriteNumberData = MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _favoriteNumberData

    private val _catalogPrefixSelect = MutableLiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _clientNumberValidatorMsg = MutableLiveData<String>()
    val clientNumberValidatorMsg: LiveData<String>
        get() = _clientNumberValidatorMsg

    fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false) {
        _menuDetailData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(block = {
            val menuDetail = repo.getMenuDetail(menuId, isLoadFromCloud)
            _menuDetailData.postValue(RechargeNetworkResult.Success(menuDetail))
        }) {
            _menuDetailData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getFavoriteNumber(categoryIds: List<Int>) {
        _favoriteNumberData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val favoriteNumber = repo.getFavoriteNumber(categoryIds)
            _favoriteNumberData.postValue(RechargeNetworkResult.Success(
                favoriteNumber.persoFavoriteNumber.items))
        }) {
            _favoriteNumberData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getPrefixOperator(menuId: Int) {
        _catalogPrefixSelect.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            operatorData = repo.getOperatorList(menuId)
            delay(DigitalPDPPulsaViewModel.DELAY_TIME)
            _catalogPrefixSelect.postValue(RechargeNetworkResult.Success(operatorData))
        }) {
            _catalogPrefixSelect.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun validateClientNumber(clientNumber: String) {
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            launchCatchError(block = {
                var errorMessage = ""
                for (validation in operatorData.rechargeCatalogPrefixSelect.validations) {
                    val phoneIsValid = Pattern.compile(validation.rule)
                        .matcher(clientNumber).matches()
                    if (!phoneIsValid) {
                        errorMessage = validation.message
                    }
                }
                isEligibleToBuy = errorMessage.isEmpty()
                delay(DigitalPDPPulsaViewModel.VALIDATOR_DELAY_TIME)
                _clientNumberValidatorMsg.postValue(errorMessage)
            }){
                if (it !is CancellationException){

                }
            }
        }
    }

}