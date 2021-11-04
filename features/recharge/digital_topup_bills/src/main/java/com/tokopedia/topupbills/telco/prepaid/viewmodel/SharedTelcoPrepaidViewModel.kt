package com.tokopedia.topupbills.telco.prepaid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInput
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInputMultiTab
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 20/05/19.
 */
class SharedTelcoPrepaidViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.io) {

    private val _expandView = MutableLiveData<Boolean>()
    val expandView: LiveData<Boolean>
        get() = _expandView

    private val _productCatalogItem = MutableLiveData<TelcoProduct>()
    val productCatalogItem: LiveData<TelcoProduct>
        get() = _productCatalogItem

    private val _productAutoCheckout = MutableLiveData<TelcoProduct>()
    val productAutoCheckout: LiveData<TelcoProduct>
        get() = _productAutoCheckout

    private val _showTotalPrice = MutableLiveData<Boolean>()
    val showTotalPrice: LiveData<Boolean>
        get() = _showTotalPrice

    private val _productList = MutableLiveData<Result<List<TelcoCatalogProductInput>>>()
    val productList: LiveData<Result<List<TelcoCatalogProductInput>>>
        get() = _productList

    private val _selectedCategoryViewPager = MutableLiveData<String>()
    val selectedCategoryViewPager: LiveData<String>
        get() = _selectedCategoryViewPager

    private val _selectedFilter = MutableLiveData<ArrayList<HashMap<String, Any>>>()
    val selectedFilter: LiveData<ArrayList<HashMap<String, Any>>>
        get() = _selectedFilter

    private val _loadingProductList = MutableLiveData<Boolean>()
    val loadingProductList: LiveData<Boolean>
        get() = _loadingProductList

    private val _positionScrollItem = MutableLiveData<Int>()
    val positionScrollItem: LiveData<Int>
        get() = _positionScrollItem

    private val _inputWidgetFocus = MutableLiveData<Boolean>()
    val inputWidgetFocus: LiveData<Boolean>
        get() = _inputWidgetFocus

    private val _selectedProductById = MutableLiveData<String>()
    val selectedProductById: LiveData<String>
        get() = _selectedProductById

    private val _resetSelectedProduct = MutableLiveData<Boolean>()
    val resetSelectedProduct: LiveData<Boolean>
        get() = _resetSelectedProduct

    private var getCatalogProductListJob: Job? = null

    fun setProductCatalogSelected(productCatalogItem: TelcoProduct) {
        _productCatalogItem.postValue(productCatalogItem)
    }

    fun setProductAutoCheckout(product: TelcoProduct) {
        _productAutoCheckout.postValue(product)
    }

    fun setPositionScrollToItem(position: Int) {
        _positionScrollItem.postValue(position)
    }

    fun setVisibilityTotalPrice(show: Boolean) {
        _showTotalPrice.postValue(show)
    }

    fun setSelectedCategoryViewPager(categoryName: String) {
        _selectedCategoryViewPager.postValue(categoryName)
    }

    fun setSelectedFilter(filter: ArrayList<HashMap<String, Any>>) {
        _selectedFilter.postValue(filter)
    }

    fun setInputWidgetFocus(isFocus: Boolean) {
        _inputWidgetFocus.postValue(isFocus)
    }

    fun clearCatalogProductList() {
        _productList.value = Success(emptyList())
    }

    fun setSelectedProductById(productId: String) {
        _selectedProductById.postValue(productId)
    }

    fun resetSelectedProduct() {
        _resetSelectedProduct.postValue(true)
    }

    fun getCatalogProductList(rawQuery: String, menuId: Int, operatorId: String,
                              filterData: ArrayList<HashMap<String, Any>>?,
                              autoSelectProductId: Int = 0, clientNumber: String,
                              isDelayed: Boolean = false
    ) {
        getCatalogProductListJob?.cancel()
        getCatalogProductListJob = CoroutineScope(coroutineContext).launch {
            launchCatchError(block = {
                if (isDelayed) {
                    delay(PRODUCT_LIST_DELAY_TIME)
                }
                _loadingProductList.postValue(true)
                val mapParam = HashMap<String, Any>()
                mapParam[KEY_MENU_ID] = menuId
                mapParam[KEY_OPERATOR_ID] = operatorId
                mapParam[KEY_CLIENT_NUMBER] = arrayListOf(clientNumber)
                if (filterData != null && filterData.size > 0) {
                    mapParam[KEY_FILTER_DATA] = filterData
                }

                val data = withContext(dispatcher.io) {
                    val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogProductInputMultiTab::class.java, mapParam)
                    graphqlRepository.response(listOf(graphqlRequest))
                }.getSuccessData<TelcoCatalogProductInputMultiTab>()

                _loadingProductList.postValue(false)
                if (data.rechargeCatalogProductDataData.productInputList.isEmpty()) {
                    _productList.postValue(Fail(MessageErrorException()))
                } else {
                    _productList.postValue(Success(data.rechargeCatalogProductDataData.productInputList))
                    setSelectedProductById(autoSelectProductId.toString())
                }
            }){
                _loadingProductList.postValue(false)
                _productList.postValue(Fail(it))
            }
        }
    }

    fun setProductListShimmer(isShow: Boolean) {
        if (_loadingProductList.value != isShow)
            _loadingProductList.postValue(isShow)
    }

    fun setExpandInputNumberView(expand: Boolean) {
        launch {
            delay(DELAY_TIME)
            _expandView.postValue(expand)
        }
    }

    companion object {
        const val KEY_MENU_ID = "menuID"
        const val KEY_OPERATOR_ID = "operatorID"
        const val KEY_FILTER_DATA = "filterData"
        const val KEY_CLIENT_NUMBER = "clientNumber"

        const val EXP_TIME = 10
        const val DELAY_TIME: Long = 100
        const val PRODUCT_LIST_DELAY_TIME: Long = 250
    }
}