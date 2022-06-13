package com.tokopedia.smartbills.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.RechargeAddBillsProductTrackData
import com.tokopedia.common.topupbills.data.RechargeSBMAddBillRequest
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryQuery
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.smartbills.data.RechargeAddBillsData
import com.tokopedia.smartbills.data.RechargeCatalogProductInput
import com.tokopedia.smartbills.data.RechargeCatalogProductInputMultiTabData
import com.tokopedia.smartbills.data.RechargeProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SmartBillsAddTelcoViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers,
        private val rechargeCatalogPrefixSelectUseCase: RechargeCatalogPrefixSelectUseCase
): BaseViewModel(dispatcher.io){

    private val mutableInputNumberNotFound = MutableLiveData<String>()
    val inputNumberNotFound: LiveData<String>
        get() = mutableInputNumberNotFound

    private val mutableCatalogPrefixSelect = MutableLiveData<Result<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<Result<TelcoCatalogPrefixSelect>>
        get() = mutableCatalogPrefixSelect

    private val mutableSelectedOperator = MutableLiveData<RechargePrefix>()
    val selectedOperator: LiveData<RechargePrefix>
        get() = mutableSelectedOperator

    private val mutableInquiryData = MutableLiveData<Result<TopupBillsEnquiryData>>()
    val inquiryData: LiveData<Result<TopupBillsEnquiryData>>
        get() = mutableInquiryData

    private val mutableRechargeAddBills= MutableLiveData<Result<RechargeAddBillsData>>()
    val rechargeAddBills: LiveData<Result<RechargeAddBillsData>>
        get() = mutableRechargeAddBills

    private val mutableCatalogProduct = MutableLiveData<Result<RechargeCatalogProductInputMultiTabData>>()
    val catalogProduct: LiveData<Result<RechargeCatalogProductInputMultiTabData>>
        get() = mutableCatalogProduct


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

    fun getSelectedOperator(inputNumber: String, errorNotFound: String){
        if (inputNumber.isEmpty() || inputNumber.length <= NUMBER_MIN_CHECK_VALUE
                || inputNumber.length > NUMBER_MAX_CHECK_VALUE) return
        try {
            catalogPrefixSelect.value.let {
                if (it is Success) {
                    val operatorSelected = it.data.rechargeCatalogPrefixSelect.prefixes.single {
                        inputNumber.startsWith(it.value)
                    }
                    mutableSelectedOperator.postValue(operatorSelected)
                } else {
                    mutableCatalogPrefixSelect.postValue(it)
                }
            }
        } catch (e: Throwable) {
            mutableInputNumberNotFound.postValue(errorNotFound)
        }
    }

    fun getInquiryData(mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(CommonTopupBillsGqlQuery.rechargeInquiry,
                    TopupBillsEnquiryData::class.java, mapParam)
            var data: TopupBillsEnquiryData
            do {
                data = withContext(dispatcher.io) {
                    graphqlRepository.response(listOf(graphqlRequest))
                }.getSuccessData()

                with (data.enquiry) {
                    if (status == TopupBillsViewModel.STATUS_PENDING && retryDuration > 0) delay((retryDuration.toLong()) * 1000)
                }
            } while (data.enquiry.status != TopupBillsViewModel.STATUS_DONE)

            mutableInquiryData.postValue(Success(data))
        }) {
            mutableInquiryData.postValue(Fail(it))
        }
    }

    fun addBill(mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(CommonTopupBillsGqlQuery.ADD_BILL_QUERY,
                        RechargeAddBillsData::class.java, mapParam)
                graphqlRepository.response(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<RechargeAddBillsData>()

            mutableRechargeAddBills.postValue(Success(data))
        }) {
            mutableRechargeAddBills.postValue(Fail(it))
        }
    }

    fun getCatalogNominal(isRequestNominal: Boolean, catalogProductInput: RechargeCatalogProductInputMultiTabData, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            if(isRequestNominal || (catalogProduct.value is Fail) ||
                    catalogProductInput.multitabData.productInputs.isNullOrEmpty()) {
                val data = withContext(dispatcher.io) {
                    val graphqlRequest = GraphqlRequest(SmartBillsQueries.GET_NOMINAL_TELCO,
                            RechargeCatalogProductInputMultiTabData::class.java, mapParam)
                    graphqlRepository.response(listOf(graphqlRequest),
                            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
                }.getSuccessData<RechargeCatalogProductInputMultiTabData>()

                mutableCatalogProduct.postValue(Success(data))
            } else {
                mutableCatalogProduct.postValue(Success(catalogProductInput))
            }
        }) {
            mutableCatalogProduct.postValue(Fail(it))
        }
    }

    fun createMenuDetailAddTelcoParams(menuId: Int): Map<String, Any> {
        return mapOf(PARAM_MENU_ID to menuId)
    }

    fun createInquiryParam(productId: String, clientNumber: String): Map<String, Any>{
        val enquiryParams = mutableListOf<TopupBillsEnquiryQuery>()
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_SOURCE_TYPE, ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_DEVICE_ID, ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_PRODUCT_ID, productId))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_CLIENT_NUMBER, clientNumber))

        return mapOf(PARAM_FIELDS to enquiryParams)
    }

    fun createAddBillsParam(addBillRequest: RechargeSBMAddBillRequest): Map<String, Any> {
        return mapOf(PARAM_ADD_REQUEST to addBillRequest)
    }

    fun createCatalogNominal(menuId: Int, platformID: Int, operator: String): Map<String, Any> {
        return mapOf(
                PARAM_MENU_ID to menuId,
                PARAM_PLATFORM_ID to platformID,
                PARAM_OPERATOR to operator
        )
    }

    fun getProductByCategoryId(listProductAll: List<RechargeCatalogProductInput>, categoryName: String): List<RechargeProduct>? {
        val mutableListRechargeProduct = mutableListOf<RechargeProduct>()

        val listDataCollection = listProductAll.single {
            it.label == categoryName
        }.product.dataCollections

        for (dataCollection in listDataCollection){
            mutableListRechargeProduct.addAll(dataCollection.products)
        }

        return mutableListRechargeProduct
    }

    fun getProductTracker(productInputs: List<RechargeProduct>, categoryName: String, operatorName: String): List<RechargeAddBillsProductTrackData>{
        return productInputs.mapIndexed{ index, product ->
            RechargeAddBillsProductTrackData(
                    index,
                    operatorName,
                    categoryName,
                    product.id,
                    product.attributes.desc,
                    "",
                    product.attributes.pricePlain.toString()
            )
        }
    }


    companion object{
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_CATEGORY_ID = "categoryID"
        const val PARAM_PLATFORM_ID = "platformID"
        const val PARAM_OPERATOR = "operator"
        const val PARAM_CLIENT_NUMBER = "clientNumber"

        const val PARAM_FIELDS = "fields"
        const val PARAM_ADD_REQUEST = "addRequest"

        const val ENQUIRY_PARAM_DEVICE_ID = "device_id"
        const val ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE = "5"
        const val ENQUIRY_PARAM_SOURCE_TYPE = "source_type"
        const val ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE = "c20ad4d76fe977"
        const val ENQUIRY_PARAM_PRODUCT_ID = "product_id"
        const val ENQUIRY_PARAM_CLIENT_NUMBER = "client_number"

        const val NUMBER_MAX_CHECK_VALUE = 14
        const val NUMBER_MIN_CHECK_VALUE = 4
        const val NUMBER_MIN_VALUE = 10
    }

}