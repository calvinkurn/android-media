package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.error.SelectForbiddenException
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.view.state.NotSelectable
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.play.broadcaster.view.uimodel.PlayEtalaseUiModel
import com.tokopedia.play.broadcaster.view.uimodel.ProductUiModel
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) mainDispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.COMPUTATION) private val computationDispatcher: CoroutineDispatcher
): ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + mainDispatcher)

    val observableEtalase: LiveData<List<PlayEtalaseUiModel>>
        get() = _observableEtalase
    private val _observableEtalase = MutableLiveData<List<PlayEtalaseUiModel>>()

    val observableSelectedEtalase: LiveData<PlayEtalaseUiModel>
        get() = _observableSelectedEtalase
    private val _observableSelectedEtalase = MutableLiveData<PlayEtalaseUiModel>()

    val observableSelectedProductIds: LiveData<List<Long>>
        get() = _observableSelectedProductIds
    private val _observableSelectedProductIds = MutableLiveData<List<Long>>()

    private val etalaseMap = mutableMapOf<Long, PlayEtalaseUiModel>()
    private val selectedProductIdList = mutableListOf<Long>()

    val maxProduct = PlayBroadcastMocker.getMaxSelectedProduct()

    init {
        fetchEtalaseList()
    }

    fun setSelectedEtalase(etalaseId: Long) {
        val selectedEtalase = _observableEtalase.value?.firstOrNull { it.id == etalaseId }
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

        _observableSelectedProductIds.value = selectedProductIdList
    }

    private fun fetchEtalaseList() {
        scope.launch {
            val etalaseList = getEtalaseList()
            launch { updateEtalaseMap(etalaseList) }

            _observableEtalase.value = etalaseList.map {
                it.copy(productList = it.productList.take(4))
            }
        }
    }

    private fun isProductSelected(productId: Long): Boolean {
        return selectedProductIdList.contains(productId)
    }

    private fun isSelectable(): SelectableState {
        return if (selectedProductIdList.size < maxProduct) Selectable
        else NotSelectable(SelectForbiddenException("Oops, kamu sudah memilih $maxProduct produk"))
    }

    private suspend fun updateEtalaseMap(newEtalaseList: List<PlayEtalaseUiModel>) = withContext(computationDispatcher) {
        newEtalaseList.associateByTo(etalaseMap) { it.id }
    }

    private suspend fun updateEtalaseMap(currentEtalase: PlayEtalaseUiModel, productList: List<ProductUiModel>): PlayEtalaseUiModel = withContext(computationDispatcher) {
        val newEtalase = currentEtalase.copy(productList = (currentEtalase.productList + productList).distinctBy { it.id })
        etalaseMap[currentEtalase.id] = newEtalase
        newEtalase
    }

    private suspend fun getEtalaseProductsById(etalaseId: Long, page: Int) = withContext(ioDispatcher) {
        return@withContext PlayBroadcastMocker.getMockProductList(10).map {
            it.copy(isSelectedHandler = ::isProductSelected, isSelectable = ::isSelectable)
        }
    }

    private suspend fun getEtalaseList() = withContext(ioDispatcher) {
        return@withContext PlayBroadcastMocker.getMockEtalaseList().map { etalase ->
            etalase.copy(productList = etalase.productList.map { product ->
                product.copy(isSelectedHandler = ::isProductSelected, isSelectable = ::isSelectable)
            })
        }
    }
}