package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.*
import javax.inject.Inject

class DigitalPDPPulsaViewModel @Inject constructor(
    val repo: DigitalPDPRepository,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _menuDetailData = MutableLiveData<RechargeNetworkResult<MenuDetailModel>>()
    val menuDetailData: LiveData<RechargeNetworkResult<MenuDetailModel>>
        get() = _menuDetailData

    private val _favoriteNumberData = MutableLiveData<RechargeNetworkResult<List<TopupBillsSeamlessFavNumberItem>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<List<TopupBillsSeamlessFavNumberItem>>>
        get() = _favoriteNumberData

    private val _catalogProductInput = MutableLiveData<Result<List<DenomWidgetModel>>>()
    val catalogProductInput: LiveData<Result<List<DenomWidgetModel>>>
        get() = _catalogProductInput

    private val _catalogPrefixSelect = MutableLiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<RechargeNetworkResult<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _errorMessage = MutableLiveData<Result<String>>()
    val errorMessage: LiveData<Result<String>>
        get() = _errorMessage

    fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false) {
        _menuDetailData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val menuDetail = repo.getMenuDetail(menuId, isLoadFromCloud)
            _menuDetailData.postValue(RechargeNetworkResult.Success(menuDetail))
        }) {
            _menuDetailData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getFavoriteNumber(categoryIds: List<String>) {
        _favoriteNumberData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val favoriteNumber = repo.getFavoriteNumber(categoryIds)
            _favoriteNumberData.postValue(RechargeNetworkResult.Success(
                favoriteNumber.seamlessFavoriteNumber.favoriteNumbers))
        }) {
            _favoriteNumberData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getPrefixOperator(menuId: Int) {
        _catalogPrefixSelect.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val operatorList = repo.getOperatorList(menuId)
            delay(DELAY_TIME)
            _catalogPrefixSelect.postValue(RechargeNetworkResult.Success(operatorList))
        }) {
            _catalogPrefixSelect.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    companion object {
        const val DELAY_TIME = 200L
    }
}