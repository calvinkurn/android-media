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
import com.tokopedia.play.broadcaster.ui.model.PlayEtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
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

    val observableSearchedProducts: LiveData<List<ProductUiModel>>
        get() = _observableSearchedProducts
    private val _observableSearchedProducts = MutableLiveData<List<ProductUiModel>>()

    val observableSelectedEtalase: LiveData<PlayEtalaseUiModel>
        get() = _observableSelectedEtalase
    private val _observableSelectedEtalase = MutableLiveData<PlayEtalaseUiModel>()

    val observableSelectedProducts: LiveData<List<ProductUiModel>>
        get() = _observableSelectedProducts
    private val _observableSelectedProducts = MutableLiveData<List<ProductUiModel>>()

    val observableSuggestionList: LiveData<List<SearchSuggestionUiModel>>
        get() = _observableSuggestionList
    private val _observableSuggestionList = MutableLiveData<List<SearchSuggestionUiModel>>()

    val maxProduct = PlayBroadcastMocker.getMaxSelectedProduct()

    private val searchChannel: Channel<String> = Channel()

    val selectedProductList: List<ProductUiModel>
        get() = observableSelectedProducts.value.orEmpty()

    private val etalaseMap = mutableMapOf<Long, PlayEtalaseUiModel>()
    private val productsMap = mutableMapOf<Long, ProductUiModel>()
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

    /**
     * Etalase
     */
    fun setSelectedEtalase(etalaseId: Long) {
        val selectedEtalase = etalaseMap[etalaseId]
        if (selectedEtalase != null) _observableSelectedEtalase.value = selectedEtalase
    }

    fun loadCurrentEtalaseProducts(page: Int) {
        scope.launch {
            val selectedEtalase = _observableSelectedEtalase.value
            if (selectedEtalase != null) {
                val productList = getEtalaseProductsById(selectedEtalase.id, page)
                val newEtalase = updateEtalaseMap(selectedEtalase, productList)
                _observableSelectedEtalase.value = newEtalase
            }
        }
    }

    fun selectProduct(productId: Long, isSelected: Boolean) {
        if (isSelected) selectedProductIdList.add(productId)
        else selectedProductIdList.remove(productId)

        updateSelectedProducts()
    }

    fun loadEtalaseProductPreview(etalaseId: Long) {
        scope.launch {
            val productList = etalaseMap[etalaseId]?.productList.orEmpty()
            if (productList.isEmpty()) {
                val newProducts = getEtalaseProductsById(etalaseId, 1)
                val etalase = etalaseMap[etalaseId]
                if (etalase != null) etalase.productList += newProducts

                launch { updateProductMap(newProducts) }

                broadcastNewEtalaseList(etalaseMap)
            }
            updateCurrentSelectedEtalase(etalaseId)
        }
    }

    /**
     * Search
     */
    fun loadSuggestionsFromKeyword(keyword: String) {
        if (keyword.isEmpty()) _observableSuggestionList.value = emptyList()
        else searchChannel.offer(keyword)
    }

    fun searchProductsByKeyword(keyword: String) {
        scope.launch {
            val searchedProducts = getProductsByKeyword(keyword)
            broadcastNewSearchedProducts(searchedProducts)
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

    private fun updateCurrentSelectedEtalase(etalaseId: Long) {
        if (_observableSelectedEtalase.value?.id == etalaseId)
            _observableSelectedEtalase.value = etalaseMap[etalaseId]
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

    private suspend fun broadcastNewEtalaseList(etalaseMap: Map<Long, PlayEtalaseUiModel>) {
        _observableEtalase.value = withContext(computationDispatcher) {
            etalaseMap.values.map { etalase ->
                etalase.copy(
                        productList = etalase.productList.take(MAX_PRODUCT_IMAGE_COUNT)
                )
            }
        }
    }

    private fun broadcastNewSearchedProducts(productList: List<ProductUiModel>) {
        _observableSearchedProducts.value = productList
    }

    private suspend fun updateEtalaseMap(newEtalaseList: List<PlayEtalaseUiModel>) = withContext(computationDispatcher) {
        newEtalaseList.forEach {
            val etalase = etalaseMap[it.id]
            if (etalase == null) etalaseMap[it.id] = it
        }
        return@withContext etalaseMap
    }

    private suspend fun updateEtalaseMap(currentEtalase: PlayEtalaseUiModel, productList: List<ProductUiModel>): PlayEtalaseUiModel = withContext(computationDispatcher) {
        val newEtalase = currentEtalase.copy(productList = (currentEtalase.productList + productList).distinctBy { it.id })
        etalaseMap[currentEtalase.id] = newEtalase
        newEtalase
    }

    private suspend fun updateProductMap(newProductList: List<ProductUiModel>) = withContext(computationDispatcher) {
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

        return@withContext PlayBroadcasterUiMapper.mapProductList(productList, ::isProductSelected, ::isSelectable)
    }

    private suspend fun getEtalaseList() = withContext(ioDispatcher) {
        val etalaseList = getSelfEtalaseListUseCase.executeOnBackground()
        return@withContext PlayBroadcasterUiMapper.mapEtalaseList(etalaseList)
    }

    private suspend fun getSearchSuggestions(keyword: String) = withContext(ioDispatcher) {
        return@withContext if (keyword.isEmpty()) emptyList() else PlayBroadcastMocker.getMockSearchSuggestions(keyword)
    }

    private suspend fun getProductsByKeyword(keyword: String) = withContext(ioDispatcher) {
        return@withContext PlayBroadcastMocker.getMockProductList(keyword.length).map {
            it.copy(isSelectedHandler = ::isProductSelected, isSelectable = ::isSelectable)
        }
    }

    companion object {

        private const val MAX_PRODUCT_IMAGE_COUNT = 4
        private const val PRODUCTS_PER_PAGE = 4
    }
}