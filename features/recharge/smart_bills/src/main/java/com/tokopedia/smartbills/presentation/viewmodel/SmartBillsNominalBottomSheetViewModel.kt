package com.tokopedia.smartbills.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.smartbills.data.RechargeCatalogProductInput
import com.tokopedia.smartbills.data.RechargeCatalogProductInputMultiTabData
import com.tokopedia.smartbills.data.RechargeProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SmartBillsNominalBottomSheetViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers,
): BaseViewModel(dispatcher.io){

    private val mutableCatalogProduct = MutableLiveData<Result<RechargeCatalogProductInputMultiTabData>>()
    val catalogProduct: LiveData<Result<RechargeCatalogProductInputMultiTabData>>
        get() = mutableCatalogProduct

    fun getCatalogNominal(mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(SmartBillsQueries.GET_NOMINAL_TELCO,
                        RechargeCatalogProductInputMultiTabData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<RechargeCatalogProductInputMultiTabData>()

            mutableCatalogProduct.postValue(Success(data))
        }) {
            mutableCatalogProduct.postValue(Fail(it))
        }
    }

    fun createCatalogNominal(menuId: Int, platformID: Int, operator: String, clientNumber: String): Map<String, Any> {
        return mapOf(
                PARAM_MENU_ID to menuId,
                PARAM_PLATFORM_ID to platformID,
                PARAM_OPERATOR to operator,
                PARAM_CLIENT_NUMBER to clientNumber
        )
    }

    fun getProductByCategoryId(listProductAll: List<RechargeCatalogProductInput>, categoryId: String): List<RechargeProduct>? {
        val product = listProductAll.single {
            it.id == categoryId
        }

        return product.product.dataCollections.firstOrNull()?.products
    }

    companion object{
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_PLATFORM_ID = "platformID"
        const val PARAM_OPERATOR = "operator"
        const val PARAM_CLIENT_NUMBER = "clientNumber"
    }

}