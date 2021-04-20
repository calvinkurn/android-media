package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.error.EventException
import com.tokopedia.play.broadcaster.error.SelectForbiddenException
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.EtalaseContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResult
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.view.state.NotSelectable
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.math.min

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
        private val getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
        private val userSession: UserSessionInterface,
        private val playBroadcastMapper: PlayBroadcastMapper
) : ViewModel() {

    private val channelId: String
        get() = hydraConfigStore.getChannelId()

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

    val observableEtalaseProductState: LiveData<PageResult<String>>
        get() = _observableEtalaseProductState
    private val _observableEtalaseProductState = MutableLiveData<PageResult<String>>()

    val observableSelectedProducts: LiveData<List<ProductContentUiModel>> = Transformations.map(setupDataStore.getObservableSelectedProducts()) { dataList ->
        dataList.map { ProductContentUiModel.createFromData(it, ::isProductSelected, ::isSelectable) }
    }

    val observableUploadProductEvent: LiveData<NetworkResult<Event<Unit>>>
        get() = _observableUploadProductEvent
    private val _observableUploadProductEvent = MutableLiveData<NetworkResult<Event<Unit>>>()

    val maxProductDesc: String
        get() = hydraConfigStore.getMaxProductDesc()

    private val maxProduct: Int
        get() = hydraConfigStore.getMaxProduct()

    val selectedProductList: List<ProductContentUiModel>
        get() = setupDataStore.getSelectedProducts().map { ProductContentUiModel.createFromData(it, ::isProductSelected, ::isSelectable) }

    private val etalaseMap = mutableMapOf<String, EtalaseContentUiModel>()
    private val productsMap = mutableMapOf<Long, ProductContentUiModel>()

    private val productPreviewChannel = BroadcastChannel<String>(Channel.BUFFERED)

    init {
        scope.launch { initProductPreviewChannel() }
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
            setupDataStore.selectProduct(it.extractData(), isSelected)
        }
    }

    fun loadEtalaseProductPreview(etalaseId: String) {
        scope.launch {
            productPreviewChannel.send(etalaseId)
        }
    }

    fun uploadProduct() {
        _observableUploadProductEvent.value = NetworkResult.Loading
        scope.launch {
            val result = setupDataStore.uploadSelectedProducts(channelId).map { Event(Unit) }
            _observableUploadProductEvent.value =
                    if (result is NetworkResult.Fail) NetworkResult.Fail(EventException(result.error))
                    else result
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
            try {
                val (searchedProducts, totalData) = getProductsByKeyword(keyword, page)
                updateProductMap(searchedProducts)
                _observableSearchedProducts.value = getSearchedProducts(searchedProducts, totalData)
            } catch (e: Throwable) {
                _observableSearchedProducts.value = PageResult(
                        currentValue = _observableSearchedProducts.value?.currentValue.orEmpty(),
                        state = PageResultState.Fail(e)
                )
            }
        }
    }

    fun loadEtalaseList() {
        _observableEtalase.value = PageResult.Loading(emptyList())
        scope.launch {
            try {
                val etalaseList = getEtalaseList()
                val newMap = updateEtalaseMap(etalaseList)
                broadcastNewEtalaseList(newMap)
            } catch (e: Throwable) {
                _observableEtalase.value = PageResult(emptyList(), PageResultState.Fail(e))
            }
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
                when (val etalaseProductResult = getEtalaseProductsById(etalaseId, page)) {
                    is NetworkResult.Success -> {
                        val (productList, totalData) = etalaseProductResult.data
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
                    is NetworkResult.Fail -> {
                        PageResult(
                                currentValue = etalase,
                                state = PageResultState.Fail(etalaseProductResult.error)
                        )
                    }
                    else -> throw IllegalStateException("Impossible state other than success and fail")
                }

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
                    val size = min(currentProductList.size, MAX_PRODUCT_IMAGE_COUNT)
                    for (index in 0 until size) {
                        newProductList.add(
                                if (index % 2 == 0) {
                                    currentProductList[(index + 1) / 2]
                                } else {
                                    currentProductList[index + (size - index) / 2]
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

    private suspend fun getEtalaseProductsById(etalaseId: String, page: Int): NetworkResult<Pair<List<ProductContentUiModel>, Int>> = withContext(dispatcher.io) {
        return@withContext try {
            val productList = getProductsInEtalaseUseCase.apply {
                params = GetProductsInEtalaseUseCase.createParams(
                        shopId = userSession.shopId,
                        page = page,
                        perPage = PRODUCTS_PER_PAGE,
                        etalaseId = etalaseId
                )
            }.executeOnBackground()

            NetworkResult.Success(Pair(
                    playBroadcastMapper.mapProductList(productList, ::isProductSelected, ::isSelectable),
                    productList.meta.totalHits
            ))
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
//        return@withContext NetworkResult.Success(Pair(PlayBroadcastMocker.getMockProductList(10), 10))
    }

    private suspend fun getEtalaseList() = withContext(dispatcher.io) {
        val etalaseList = getSelfEtalaseListUseCase.executeOnBackground()
        return@withContext playBroadcastMapper.mapEtalaseList(etalaseList)
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
                playBroadcastMapper.mapProductList(productList, ::isProductSelected, ::isSelectable),
                productList.meta.totalHits
        )
    }

    private suspend fun initProductPreviewChannel() = withContext(dispatcher.main) {
        productPreviewChannel.asFlow().collect {
            val result = fetchEtalaseProduct(it, 1)
            if (result.state is PageResultState.Success) {
                val etalaseMap = updateEtalaseMap(listOf(result.currentValue))
                broadcastNewEtalaseList(etalaseMap)
            } else if (result.state is PageResultState.Fail) {
                _observableEtalaseProductState.value = PageResult(result.currentValue.id, result.state)
            }
        }
    }

    companion object {

        private const val MAX_PRODUCT_IMAGE_COUNT = 4
        private const val PRODUCTS_PER_PAGE = 26
    }
}