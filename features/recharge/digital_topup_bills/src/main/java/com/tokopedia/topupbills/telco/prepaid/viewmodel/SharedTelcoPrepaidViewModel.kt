package com.tokopedia.topupbills.telco.prepaid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 20/05/19.
 */
class SharedTelcoPrepaidViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _expandView = MutableLiveData<Boolean>()
    val expandView: LiveData<Boolean>
        get() = _expandView

    private val _productCatalogItem = MutableLiveData<TelcoProduct>()
    val productCatalogItem: LiveData<TelcoProduct>
        get() = _productCatalogItem

    private val _productAutoCheckout = MutableLiveData<TelcoProduct>()
    val productAutoCheckout: LiveData<TelcoProduct>
        get() = _productAutoCheckout

    private val _favNumberSelected = MutableLiveData<TopupBillsFavNumberItem>()
    val favNumberSelected: LiveData<TopupBillsFavNumberItem>
        get() = _favNumberSelected

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

    fun setProductCatalogSelected(productCatalogItem: TelcoProduct) {
        _productCatalogItem.postValue(productCatalogItem)
    }

    fun setProductAutoCheckout(product: TelcoProduct) {
        _productAutoCheckout.postValue(product)
    }

    fun setPositionScrollToItem(position: Int) {
        _positionScrollItem.postValue(position)
    }

    fun setFavNumberSelected(favNumber: TopupBillsFavNumberItem) {
        _favNumberSelected.postValue(favNumber)
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

    // cache in 10 minutes
    fun getCatalogProductList(rawQuery: String, menuId: Int, operatorId: String,
                              filterData: ArrayList<HashMap<String, Any>>?, autoSelectProductId: Int = 0) {
        launchCatchError(block = {
            _loadingProductList.postValue(true)
            val mapParam = HashMap<String, Any>()
            mapParam[KEY_MENU_ID] = menuId
            mapParam[KEY_OPERATOR_ID] = operatorId
            if (filterData != null && filterData.size > 0) {
                mapParam[KEY_FILTER_DATA] = filterData
            }

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogProductInputMultiTab::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXP_TIME).build())
            }.getSuccessData<TelcoCatalogProductInputMultiTab>()

            _loadingProductList.postValue(false)
            if (data.rechargeCatalogProductDataData.productInputList.isEmpty()) {
                _productList.postValue(Fail(MessageErrorException()))
            } else {
                _productList.postValue(Success(data.rechargeCatalogProductDataData.productInputList))
                setFavNumberSelected(TopupBillsFavNumberItem(productId = autoSelectProductId.toString()))
            }
        }) {
            _loadingProductList.postValue(false)
            _productList.postValue(Fail(it))
        }
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

        const val EXP_TIME = 10
        const val DELAY_TIME: Long = 100
    }
}