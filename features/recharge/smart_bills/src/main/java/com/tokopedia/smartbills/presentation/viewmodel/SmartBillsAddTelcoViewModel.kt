package com.tokopedia.smartbills.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SmartBillsAddTelcoViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers,
        private val rechargeCatalogPrefixSelectUseCase: RechargeCatalogPrefixSelectUseCase
): BaseViewModel(dispatcher.io){

    private val mutableListTicker = MutableLiveData<Result<List<TopupBillsTicker>>>()
    val listTicker: LiveData<Result<List<TopupBillsTicker>>>
        get() = mutableListTicker

    private val mutableCatalogPrefixSelect = MutableLiveData<Result<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<Result<TelcoCatalogPrefixSelect>>
        get() = mutableCatalogPrefixSelect

    private val mutableSelectedOperator = MutableLiveData<RechargePrefix>()
    val selectedOperator: LiveData<RechargePrefix>
        get() = mutableSelectedOperator

    fun getMenuDetailAddTelco(mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(CommonTopupBillsGqlQuery.catalogMenuDetail,
                        TelcoCatalogMenuDetailData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<TelcoCatalogMenuDetailData>()

            mutableListTicker.postValue(Success(data.catalogMenuDetailData.tickers))
        }) {
            mutableListTicker.postValue(Fail(it))
        }
    }

    fun getPrefixAddTelco(menuId: Int){
        rechargeCatalogPrefixSelectUseCase.execute(
                RechargeCatalogPrefixSelectUseCase.createParams(menuId),
                {
                    mutableCatalogPrefixSelect.value = Success(it)
                },
                {
                    //on fail get prefix
                    mutableCatalogPrefixSelect.value = Fail(it)
                }
        )
    }

    fun getSelectedOperator(inputNumber: String){
        if (inputNumber.isEmpty() || inputNumber.length <= NUMBER_MIN_CHECK_VALUE
                || inputNumber.length > NUMBER_MAX_CHECK_VALUE) return
        try {
            if (catalogPrefixSelect.value is Success) {
                val operatorSelected = (catalogPrefixSelect.value as Success).data.rechargeCatalogPrefixSelect.prefixes.single {
                    inputNumber.startsWith(it.value)
                }
                mutableSelectedOperator.value = operatorSelected
            } else if (catalogPrefixSelect.value is Fail) {
                mutableCatalogPrefixSelect.value = catalogPrefixSelect.value as Fail
            }
        } catch (e: Throwable) {

        }
    }

    fun createMenuDetailAddTelcoParams(menuId: Int): Map<String, Any> {
        return mapOf(PARAM_MENU_ID to menuId)
    }

    companion object{
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_CATEGORY_ID = "categoryID"

        const val NUMBER_MAX_CHECK_VALUE = 14
        const val NUMBER_MIN_CHECK_VALUE = 4
        const val NUMBER_MIN_VALUE = 10
    }

}