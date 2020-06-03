package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.error.SelectForbiddenException
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcasterUiMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.state.NotSelectable
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.min

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) private val mainDispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.COMPUTATION) private val computationDispatcher: CoroutineDispatcher,
        private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
        private val getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
        private val userSession: UserSessionInterface
): ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + mainDispatcher)

    val observableEtalase: LiveData<List<PlayEtalaseUiModel>>
        get() = _observableEtalase
    private val _observableEtalase = MutableLiveData<List<PlayEtalaseUiModel>>()

    val observableSearchedProducts: LiveData<PageResult<List<ProductContentUiModel>>>
        get() = _observableSearchedProducts
    private val _observableSearchedProducts = MutableLiveData<PageResult<List<ProductContentUiModel>>>()

    val observableSelectedEtalase: LiveData<PageResult<PlayEtalaseUiModel>>
        get() = _observableSelectedEtalase
    private val _observableSelectedEtalase = MutableLiveData<PageResult<PlayEtalaseUiModel>>()

    val observableSelectedProducts: LiveData<List<ProductContentUiModel>>
        get() = _observableSelectedProducts
    private val _observableSelectedProducts = MutableLiveData<List<ProductContentUiModel>>()

    val observableSuggestionList: LiveData<List<SearchSuggestionUiModel>>
        get() = _observableSuggestionList
    private val _observableSuggestionList = MutableLiveData<List<SearchSuggestionUiModel>>()

    val maxProduct = PlayBroadcastMocker.getMaxSelectedProduct()

    private val searchChannel: Channel<String> = Channel()

    val selectedProductList: List<ProductContentUiModel>
        get() = observableSelectedProducts.value.orEmpty()

    private val etalaseMap = mutableMapOf<Long, PlayEtalaseUiModel>()
    private val productsMap = mutableMapOf<Long, ProductContentUiModel>()
    private val selectedProductIdList = mutableListOf<Long>()

    init {
        _observableSelectedProducts.value = emptyList()
        scope.launch { initSearchChannel() }
        loadEtalaseList()
    }

    override fun onCleared() {
        super.onCleared()
        searchChannel.cancel()
        job.cancelChildren()
    }

    fun loadEtalaseProducts(etalaseId: Long, page: Int) {
        val currentValue = _observableSelectedEtalase.value?.currentValue
        val etalase = etalaseMap[etalaseId]
        _observableSelectedEtalase.value = PageResult.Loading(
                if (page == 1 || currentValue == null) PlayEtalaseUiModel.Empty(name = etalase?.name.orEmpty())
                else currentValue
        )

        scope.launch {
            _observableSelectedEtalase.value = fetchEtalaseProduct(etalaseId, page)
        }
    }

    fun selectProduct(productId: Long, isSelected: Boolean) {
        if (isSelected) selectedProductIdList.add(productId)
        else selectedProductIdList.remove(productId)

        updateSelectedProducts()
    }

    fun loadEtalaseProductPreview(etalaseId: Long) {
        scope.launch {
            fetchEtalaseProduct(etalaseId, 1)
            broadcastNewEtalaseList(etalaseMap)
        }
    }

    /**
     * Search
     */
    fun loadSuggestionsFromKeyword(keyword: String) {
        if (keyword.isEmpty()) _observableSuggestionList.value = emptyList()
        else searchChannel.offer(keyword)
    }

    fun searchProductsByKeyword(keyword: String, page: Int) {
        val currentValue = _observableSearchedProducts.value?.currentValue.orEmpty()
        _observableSearchedProducts.value = PageResult.Loading(
                if (page == 1) emptyList() else currentValue
        )
        scope.launch {
            val (searchedProducts, totalData) = getProductsByKeyword(keyword, page)
            _observableSearchedProducts.value = getSearchedProducts(searchedProducts, totalData)
        }
    }

    fun loadEtalaseList() {
        scope.launch {
            val etalaseList = getEtalaseList()
            val newMap = updateEtalaseMap(etalaseList)
            broadcastNewEtalaseList(newMap)
        }
    }

    private fun isProductSelected(productId: Long): Boolean {
        return selectedProductIdList.contains(productId)
    }

    private fun isSelectable(): SelectableState {
        return if (selectedProductIdList.size < maxProduct) Selectable
        else NotSelectable(SelectForbiddenException("Oops, kamu sudah memilih $maxProduct produk"))
    }

    private fun updateSelectedProducts() {
        _observableSelectedProducts.value = selectedProductIdList.flatMap {
            val product = productsMap[it]
            if (product != null) listOf(product) else emptyList()
        }
    }

    private suspend fun initSearchChannel() = withContext(mainDispatcher) {
        searchChannel.consumeAsFlow().debounce(500).collect {
            val searchSuggestions = getSearchSuggestions(it)
            _observableSuggestionList.value = searchSuggestions
        }
    }

    private suspend fun fetchEtalaseProduct(etalaseId: Long, page: Int): PageResult<PlayEtalaseUiModel> = withContext(computationDispatcher) {
        val etalase = etalaseMap[etalaseId]
        return@withContext if (etalase != null) {
            val productListInMap = etalase.productMap[page]
            if (productListInMap != null) {
                //if product map already retrieved

                PageResult(
                        currentValue = etalase.copy(
                                productMap = etalase.productMap.filterKeys { it <= page }.toMutableMap()
                        ),
                        state = ResultState.Success(etalase.stillHasProduct)
                )

            } else {
                //if not yet retrieved
                val (productList, totalData) = getEtalaseProductsById(etalaseId, page)
                updateEtalaseMap(etalaseId, productList, page)
                updateProductMap(productList)

                val newProductMap = etalase.productMap.filterKeys { it <= page }.toMutableMap().also {
                    it[page] = productList
                }

                val stillHasNextPage = newProductMap.values.size < totalData

                PageResult(
                        currentValue = etalase.copy(
                                productMap = newProductMap,
                                stillHasProduct = stillHasNextPage
                        ),
                        state = ResultState.Success(stillHasNextPage)
                )
            }
        } else {
            PageResult(
                    currentValue = PlayEtalaseUiModel.Empty(),
                    state = ResultState.Fail(IllegalStateException("Etalase not found"))
            )
        }
    }

    private suspend fun broadcastNewEtalaseList(etalaseMap: Map<Long, PlayEtalaseUiModel>) {
        _observableEtalase.value = withContext(computationDispatcher) {
            etalaseMap.values.map { etalase ->

                val currentProductList = etalase.productMap[1]
                val newProductList = mutableListOf<ProductContentUiModel>()
                for (index in 0 until min((currentProductList?.size ?: 0), MAX_PRODUCT_IMAGE_COUNT)) {
                    newProductList.add(
                            if (index % 2 == 0) {
                                currentProductList!![(index + 1) / 2]
                            } else {
                                currentProductList!![index + (MAX_PRODUCT_IMAGE_COUNT - index) / 2]
                            }
                    )
                }

                etalase.copy(
                        productMap = mutableMapOf(
                                1 to newProductList
                        )
                )
            }
        }
    }

    private suspend fun getSearchedProducts(productList: List<ProductContentUiModel>, totalData: Int): PageResult<List<ProductContentUiModel>> = withContext(computationDispatcher) {
        val prevList = _observableSearchedProducts.value?.currentValue.orEmpty()
        val appendedList = prevList + productList
        return@withContext PageResult(
                currentValue = appendedList,
                state = ResultState.Success(appendedList.size < totalData)
        )
    }

    private suspend fun updateEtalaseMap(newEtalaseList: List<PlayEtalaseUiModel>) = withContext(computationDispatcher) {
        newEtalaseList.forEach {
            val etalase = etalaseMap[it.id]
            if (etalase == null) etalaseMap[it.id] = it
        }
        return@withContext etalaseMap
    }

    private suspend fun updateEtalaseMap(etalaseId: Long, productList: List<ProductContentUiModel>, page: Int) = withContext(computationDispatcher) {
        etalaseMap[etalaseId]?.productMap?.put(page, productList)
    }

    private suspend fun updateProductMap(newProductList: List<ProductContentUiModel>) = withContext(computationDispatcher) {
        newProductList.associateByTo(productsMap) { it.id }
    }

    private suspend fun getEtalaseProductsById(etalaseId: Long, page: Int) = withContext(ioDispatcher) {
        val productList = getProductsInEtalaseUseCase.apply {
            params = GetProductsInEtalaseUseCase.createParams(
                    shopId = userSession.shopId,
                    page = page,
                    perPage = PRODUCTS_PER_PAGE,
                    etalaseId = etalaseId.toString()
            )
        }.executeOnBackground()

        return@withContext Pair(
                PlayBroadcasterUiMapper.mapProductList(productList, ::isProductSelected, ::isSelectable),
                productList.totalData
        )
    }

    private suspend fun getEtalaseList() = withContext(ioDispatcher) {
        val etalaseList = getSelfEtalaseListUseCase.executeOnBackground()
        return@withContext PlayBroadcasterUiMapper.mapEtalaseList(etalaseList)
    }

    private suspend fun getSearchSuggestions(keyword: String) = withContext(ioDispatcher) {
        return@withContext if (keyword.isEmpty()) emptyList() else PlayBroadcastMocker.getMockSearchSuggestions(keyword)
    }

    private suspend fun getProductsByKeyword(keyword: String, page: Int) = withContext(ioDispatcher) {
        val productList = getProductsInEtalaseUseCase.apply {
            params = GetProductsInEtalaseUseCase.createParams(
                    shopId = userSession.shopId,
                    page = page,
                    perPage = PRODUCTS_PER_PAGE,
                    keyword = keyword
            )
        }.executeOnBackground()

        return@withContext Pair(
                PlayBroadcasterUiMapper.mapProductList(productList, ::isProductSelected, ::isSelectable),
                productList.totalData
        )
    }

    companion object {

        private const val MAX_PRODUCT_IMAGE_COUNT = 4
        private const val PRODUCTS_PER_PAGE = 26
    }
}