package com.tokopedia.play.broadcaster.view.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.error.SelectForbiddenException
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.EtalaseContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.ui.model.result.PageResult
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.view.state.NotSelectable
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.min

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
        private val getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
        private val userSession: UserSessionInterface
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val observableEtalase: LiveData<PageResult<List<EtalaseContentUiModel>>>
        get() = _observableEtalase
    private val _observableEtalase = MutableLiveData<PageResult<List<EtalaseContentUiModel>>>()

    val observableSearchedProducts: LiveData<PageResult<List<ProductContentUiModel>>>
        get() = _observableSearchedProducts
    private val _observableSearchedProducts = MutableLiveData<PageResult<List<ProductContentUiModel>>>()

    val observableSelectedEtalase: LiveData<PageResult<EtalaseContentUiModel>>
        get() = _observableSelectedEtalase
    private val _observableSelectedEtalase = MutableLiveData<PageResult<EtalaseContentUiModel>>()

    val observableSelectedProducts: LiveData<List<ProductContentUiModel>>
        get() = setupDataStore.getObservableSelectedProducts()

    val observableUploadProductEvent: LiveData<NetworkResult<Event<Unit>>>
        get() = _observableUploadProductEvent
    private val _observableUploadProductEvent = MutableLiveData<NetworkResult<Event<Unit>>>()

    val maxProduct = PlayBroadcastMocker.getMaxSelectedProduct()

    val selectedProductList: List<ProductContentUiModel>
        get() = setupDataStore.getSelectedProducts()

    var coverImageUri: Uri? = null
    var coverImageUrl: String = ""
    var liveTitle: String = ""

    private val etalaseMap = mutableMapOf<String, EtalaseContentUiModel>()
    private val productsMap = mutableMapOf<Long, ProductContentUiModel>()

    init {
        loadEtalaseList()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }

    fun loadEtalaseProducts(etalaseId: String, page: Int) {
        val currentValue = _observableSelectedEtalase.value?.currentValue
        val etalase = etalaseMap[etalaseId]
        _observableSelectedEtalase.value = PageResult.Loading(
                if (page == 1 || currentValue == null) EtalaseContentUiModel.Empty(name = etalase?.name.orEmpty())
                else currentValue
        )

        scope.launch {
            _observableSelectedEtalase.value = fetchEtalaseProduct(etalaseId, page)
        }
    }

    fun selectProduct(productId: Long, isSelected: Boolean) {
        productsMap[productId]?.let {
            setupDataStore.selectProduct(it, isSelected)
        }
    }

    fun loadEtalaseProductPreview(etalaseId: String) {
        scope.launch {
            fetchEtalaseProduct(etalaseId, 1)
            broadcastNewEtalaseList(etalaseMap)
        }
    }

    fun uploadProduct(channelId: String) {
        _observableUploadProductEvent.value = NetworkResult.Loading
//        scope.launch {
//            val result = setupDataStore.uploadSelectedProducts(channelId)
//                if (result is NetworkResult.Success) _observableUploadProductEvent.value = NetworkResult.Success(Event(Unit))
//                else if (result is NetworkResult.Fail) _observableUploadProductEvent.value = result
//        }
        //TODO("Remove Mock Behavior")
        scope.launch {
            delay(3500)
            _observableUploadProductEvent.value = NetworkResult.Success(Event(Unit))
        }
    }

    /**
     * Search
     */
    fun searchProductsByKeyword(keyword: String, page: Int) {
        val currentValue = _observableSearchedProducts.value?.currentValue.orEmpty()
        _observableSearchedProducts.value = PageResult.Loading(
                if (page == 1) emptyList() else currentValue
        )
        scope.launch {
            val (searchedProducts, totalData) = getProductsByKeyword(keyword, page)
            updateProductMap(searchedProducts)
            _observableSearchedProducts.value = getSearchedProducts(searchedProducts, totalData)
        }
    }

    private fun loadEtalaseList() {
        _observableEtalase.value = PageResult.Loading(emptyList())
        scope.launch {
            val etalaseList = getEtalaseList()
            val newMap = updateEtalaseMap(etalaseList)
            broadcastNewEtalaseList(newMap)
        }
    }

    private fun isProductSelected(productId: Long): Boolean {
        return setupDataStore.isProductSelected(productId)
    }

    private fun isSelectable(shouldSelect: Boolean): SelectableState {
        if (_observableUploadProductEvent.value is NetworkResult.Loading) return NotSelectable(SelectForbiddenException("Product is uploading"))

        return if (shouldSelect) {
            if (setupDataStore.getTotalSelectedProduct() < maxProduct) Selectable
            else NotSelectable(SelectForbiddenException("Oops, kamu sudah memilih $maxProduct produk"))
        } else Selectable
    }

    private suspend fun fetchEtalaseProduct(etalaseId: String, page: Int): PageResult<EtalaseContentUiModel> = withContext(dispatcher.computation) {
        val etalase = etalaseMap[etalaseId]
        return@withContext if (etalase != null) {
            val productListInMap = etalase.productMap[page]
            if (productListInMap != null) {
                //if product map already retrieved

                PageResult(
                        currentValue = etalase.copy(
                                productMap = etalase.productMap.filterKeys { it <= page }.toMutableMap()
                        ),
                        state = PageResultState.Success(etalase.stillHasProduct)
                )

            } else {
                //if not yet retrieved
                val (productList, totalData) = getEtalaseProductsById(etalaseId, page)
                etalase.productMap[page] = productList.map {
                    it.copy(transitionName = "$etalaseId - ${it.id}")
                }
                launch { updateProductMap(productList) }

                val stillHasNextPage = etalase.productMap.values.size < totalData

                PageResult(
                        currentValue = etalase.copy(stillHasProduct = stillHasNextPage),
                        state = PageResultState.Success(stillHasNextPage)
                )
            }
        } else {
            PageResult(
                    currentValue = EtalaseContentUiModel.Empty(),
                    state = PageResultState.Fail(IllegalStateException("Etalase not found"))
            )
        }
    }

    private suspend fun broadcastNewEtalaseList(etalaseMap: Map<String, EtalaseContentUiModel>) {
        val etalaseList = withContext(dispatcher.computation) {
            etalaseMap.values.map { etalase ->

                val currentProductList = etalase.productMap[1]
                if (currentProductList != null) {
                    val newProductList = mutableListOf<ProductContentUiModel>()
                    for (index in 0 until min(currentProductList.size, MAX_PRODUCT_IMAGE_COUNT)) {
                        newProductList.add(
                                if (index % 2 == 0) {
                                    currentProductList[(index + 1) / 2]
                                } else {
                                    currentProductList[index + (MAX_PRODUCT_IMAGE_COUNT - index) / 2]
                                }
                        )
                    }

                    etalase.copy(
                            productMap = mutableMapOf(
                                    1 to newProductList
                            )
                    )
                } else etalase.copy(
                        productMap = mutableMapOf()
                )
            }
        }

        _observableEtalase.value = PageResult(
                currentValue = etalaseList,
                state = PageResultState.Success(false)
        )
    }

    private suspend fun getSearchedProducts(productList: List<ProductContentUiModel>, totalData: Int): PageResult<List<ProductContentUiModel>> = withContext(dispatcher.computation) {
        val prevList = _observableSearchedProducts.value?.currentValue.orEmpty()
        val appendedList = prevList + productList
        return@withContext PageResult(
                currentValue = appendedList,
                state = PageResultState.Success(appendedList.size < totalData)
        )
    }

    private suspend fun updateEtalaseMap(newEtalaseList: List<EtalaseContentUiModel>) = withContext(dispatcher.computation) {
        newEtalaseList.forEach {
            val etalase = etalaseMap[it.id]
            if (etalase == null) etalaseMap[it.id] = it
        }
        return@withContext etalaseMap
    }

    private suspend fun updateProductMap(newProductList: List<ProductContentUiModel>) = withContext(dispatcher.computation) {
        newProductList.associateByTo(productsMap) { it.id }
    }

    private suspend fun getEtalaseProductsById(etalaseId: String, page: Int) = withContext(dispatcher.io) {
        val productList = getProductsInEtalaseUseCase.apply {
            params = GetProductsInEtalaseUseCase.createParams(
                    shopId = userSession.shopId,
                    page = page,
                    perPage = PRODUCTS_PER_PAGE,
                    etalaseId = etalaseId
            )
        }.executeOnBackground()

        return@withContext Pair(
                PlayBroadcastUiMapper.mapProductList(productList, ::isProductSelected, ::isSelectable),
                productList.totalData
        )
    }

    private suspend fun getEtalaseList() = withContext(dispatcher.io) {
        val etalaseList = getSelfEtalaseListUseCase.executeOnBackground()
        return@withContext PlayBroadcastUiMapper.mapEtalaseList(etalaseList)
    }

    private suspend fun getSearchSuggestions(keyword: String) = withContext(dispatcher.io) {
        return@withContext if (keyword.isEmpty()) emptyList() else {
            val suggestionList = getProductsInEtalaseUseCase.apply {
                params = GetProductsInEtalaseUseCase.createParams(
                        shopId = userSession.shopId,
                        page = 1,
                        perPage = SEARCH_SUGGESTIONS_PER_PAGE,
                        keyword = keyword
                )
            }.executeOnBackground()

            PlayBroadcastUiMapper.mapSearchSuggestionList(keyword, suggestionList)
        }
    }

    private suspend fun getProductsByKeyword(keyword: String, page: Int) = withContext(dispatcher.io) {
        val productList = getProductsInEtalaseUseCase.apply {
            params = GetProductsInEtalaseUseCase.createParams(
                    shopId = userSession.shopId,
                    page = page,
                    perPage = PRODUCTS_PER_PAGE,
                    keyword = keyword
            )
        }.executeOnBackground()

        return@withContext Pair(
                PlayBroadcastUiMapper.mapProductList(productList, ::isProductSelected, ::isSelectable),
                productList.totalData
        )
    }

    companion object {

        private const val MAX_PRODUCT_IMAGE_COUNT = 4
        private const val PRODUCTS_PER_PAGE = 26
        private const val SEARCH_SUGGESTIONS_PER_PAGE = 30
    }
}