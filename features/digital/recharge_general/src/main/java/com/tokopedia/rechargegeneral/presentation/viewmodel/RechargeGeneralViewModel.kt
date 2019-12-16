package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.rechargegeneral.domain.GetProductUseCase
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductData
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeGeneralViewModel  @Inject constructor(
        private val getProductUseCase: GetProductUseCase,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val operatorCluster = MutableLiveData<Result<RechargeGeneralOperatorCluster>>()
    val productList = MutableLiveData<Result<RechargeGeneralProductData>>()

    lateinit var operatorClusterQuery: String
    lateinit var productListQuery: String

//    fun getOperatorCluster(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false, operatorId: Int? = null) {
//        if (::operatorClusterQuery.isInitialized && ::productListQuery.isInitialized) {
//            launch {
//                operatorCluster.value = withContext(Dispatchers.Default) {
//                    getDigitalProductUseCase.getOperatorData(operatorClusterQuery, mapParams, isLoadFromCloud)
//                }
//
//                    // Retrieve products based on operatorId (if available) or first operator in response
//                    if (operatorCluster.value is Success) {
//                        val selectedOperator: Int? = operatorId ?: getFirstOperatorId((operatorCluster.value as Success).data)
//                        if (selectedOperator != null && mapParams.containsKey(PARAM_MENU_ID)) {
//                            val productListParams = createParams(mapParams[PARAM_MENU_ID] as Int, selectedOperator)
//                            productList.value = getDigitalProductUseCase.getProductList(productListQuery, productListParams, isLoadFromCloud)
//                        } else {
//                            productList.value = Fail(MessageErrorException("No default operator provided"))
//                    }
//                }
//            }
//        }
//    }

    fun getOperatorCluster(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        if (::operatorClusterQuery.isInitialized) {
            launch {
                withContext(Dispatchers.Default) {
                    operatorCluster.postValue(getProductUseCase.getOperatorData(operatorClusterQuery, mapParams, isLoadFromCloud))
                }
            }
        }
    }

    fun getProductList(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        if (::productListQuery.isInitialized) {
            launch {
                withContext(Dispatchers.Default) {
                    productList.postValue(getProductUseCase.getProductList(productListQuery, mapParams, isLoadFromCloud))
                }
            }
        }
    }

    fun createParams(menuID: Int, operator: Int? = null): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params[PARAM_MENU_ID] = menuID
        operator?.let { opr -> params[PARAM_OPERATOR] = opr.toString() }
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
    }
}
