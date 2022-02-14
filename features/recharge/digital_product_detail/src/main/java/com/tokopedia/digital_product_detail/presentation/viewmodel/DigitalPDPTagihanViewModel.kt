package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogDynamicInput
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTagihanListrikRepository
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject

class DigitalPDPTagihanViewModel @Inject constructor(
    val repo: DigitalPDPTagihanListrikRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private var loadingJob: Job? = null

    var isEligibleToBuy = false

    var operatorData: CatalogOperator = CatalogOperator()

    private val _menuDetailData = MutableLiveData<RechargeNetworkResult<MenuDetailModel>>()
    val menuDetailData: LiveData<RechargeNetworkResult<MenuDetailModel>>
        get() = _menuDetailData

    private val _favoriteNumberData = MutableLiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>()
    val favoriteNumberData: LiveData<RechargeNetworkResult<List<TopupBillsPersoFavNumberItem>>>
        get() = _favoriteNumberData

    private val _catalogSelectGroup = MutableLiveData<RechargeNetworkResult<DigitalCatalogOperatorSelectGroup>>()
    val catalogSelectGroup: LiveData<RechargeNetworkResult<DigitalCatalogOperatorSelectGroup>>
        get() = _catalogSelectGroup

    private val _dynamicInput = MutableLiveData<RechargeNetworkResult<DigitalCatalogDynamicInput>>()
    val dynamicInput: LiveData<RechargeNetworkResult<DigitalCatalogDynamicInput>>
        get() = _dynamicInput

    private val _inquiry = MutableLiveData<RechargeNetworkResult<TopupBillsEnquiryData>>()
    val inquiry: LiveData<RechargeNetworkResult<TopupBillsEnquiryData>>
        get() = _inquiry

    private val _clientNumberValidatorMsg = MutableLiveData<String>()
    val clientNumberValidatorMsg: LiveData<String>
        get() = _clientNumberValidatorMsg

    fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false) {
        _menuDetailData.value = RechargeNetworkResult.Loading
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val menuDetail = repo.getMenuDetail(menuId, isLoadFromCloud)
            _menuDetailData.value = RechargeNetworkResult.Success(menuDetail)
        }) {
            _menuDetailData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun getFavoriteNumber(categoryIds: List<Int>) {
        _favoriteNumberData.value = RechargeNetworkResult.Loading
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val favoriteNumber = repo.getFavoriteNumber(categoryIds)
            _favoriteNumberData.value = RechargeNetworkResult.Success(
                favoriteNumber.persoFavoriteNumber.items)
        }) {
            _favoriteNumberData.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun getOperatorSelectGroup(menuId: Int) {
        _catalogSelectGroup.value = RechargeNetworkResult.Loading
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val data = repo.getOperatorSelectGroup(menuId)
            val operatorList = data.response.operatorGroups?.firstOrNull()?.operators
            if (!operatorList.isNullOrEmpty()){
                operatorData = operatorList.get(0)
            }
            _catalogSelectGroup.value =  RechargeNetworkResult.Success(data)
        }) {
            _catalogSelectGroup.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun getDynamicInput(menuID: Int, operator: String){
        _dynamicInput.value = RechargeNetworkResult.Loading
        viewModelScope.launchCatchError(dispatchers.main, block = {
            val data  = repo.getDynamicInput(menuID, operator)
            _dynamicInput.value = RechargeNetworkResult.Success(data)
        }){
            _dynamicInput.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun inquiry(productId: String, clientNumber: String, inputData: Map<String, String>){
        _inquiry.value = RechargeNetworkResult.Loading
        viewModelScope.launchCatchError(dispatchers.main, block = {
            var data: TopupBillsEnquiryData
            do {
                data = repo.inquiryProduct(productId, clientNumber, inputData)

                with (data.enquiry) {
                    if (status == STATUS_PENDING && retryDuration > 0) delay((retryDuration.toLong()) * MS_TO_S_DURATION)
                }
            } while (data.enquiry.status != STATUS_DONE)
            _inquiry.value = RechargeNetworkResult.Success(data)
        }){
            _inquiry.value = RechargeNetworkResult.Fail(it)
        }
    }

    fun validateClientNumber(clientNumber: String) {
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            launchCatchError(dispatchers.main, block = {
                var errorMessage = ""
//                for (validation in operatorData.rechargeCatalogPrefixSelect.validations) {
//                    val phoneIsValid = Pattern.compile(validation.rule)
//                        .matcher(clientNumber).matches()
//                    if (!phoneIsValid) {
//                        errorMessage = validation.message
//                    }
//                }
                isEligibleToBuy = errorMessage.isEmpty()
                delay(VALIDATOR_DELAY_TIME)
                _clientNumberValidatorMsg.value = errorMessage
            }){
                if (it !is CancellationException){

                }
            }
        }
    }

    companion object {
        const val DELAY_TIME = 200L
        const val VALIDATOR_DELAY_TIME = 3000L
        const val STATUS_DONE = "DONE"
        const val STATUS_PENDING = "PENDING"
        const val MS_TO_S_DURATION = 1000
    }

}