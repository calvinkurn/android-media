package com.tokopedia.play.broadcaster.setup.product.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.CampaignAndEtalaseUiModel
import com.tokopedia.play.broadcaster.setup.product.model.ProductSaveStateUiModel
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupConfig
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductSummaryUiState
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.ui.model.PagingType
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play_common.util.extension.combine
import com.tokopedia.play_common.util.extension.switch
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductSetupViewModel @AssistedInject constructor(
    @Assisted productSectionList: List<ProductTagSectionUiModel>,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted isEligibleForPin: Boolean,
    private val repo: PlayBroadcastRepository,
    private val configStore: HydraConfigStore,
    userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            productSectionList: List<ProductTagSectionUiModel>,
            savedStateHandle: SavedStateHandle,
            isEligibleForPin: Boolean,
        ): PlayBroProductSetupViewModel
    }

    init {
        if (!savedStateHandle.hasProductSections()) {
            savedStateHandle.setProductSections(productSectionList)
        }
        savedStateHandle.setEligiblePinStatus(isEligibleForPin)
    }

    val channelId: String
        get() = configStore.getChannelId()

    val maxProduct: Int
        get() = configStore.getMaxProduct()

    val isEligibleForPin: Boolean
        get() = savedStateHandle.isEligibleForPin()

    private val _campaignAndEtalase = MutableStateFlow(CampaignAndEtalaseUiModel.Empty)
    private val _selectedProductList = MutableStateFlow(
        savedStateHandle.getProductSections().flatMap { it.products }
    )
    private val _focusedProductList = MutableStateFlow(ProductListPaging.Empty)
    private val _saveState = MutableStateFlow(ProductSaveStateUiModel.Empty)
    private val _productTagSectionList = MutableStateFlow(savedStateHandle.getProductSections())
    private val _productTagSummary = MutableStateFlow<ProductTagSummaryUiModel>(ProductTagSummaryUiModel.Success)
    private val _loadParam = MutableStateFlow(ProductListPaging.Param.Empty)
    private val _config = MutableStateFlow(
        ProductSetupConfig(shopName = userSession.shopName, maxProduct = configStore.getMaxProduct())
    )

    private val searchQuery = MutableStateFlow("")

    private var getProductListJob: Job? = null

    private val _uiEvent = MutableSharedFlow<PlayBroProductChooserEvent>(extraBufferCapacity = 5)

    val uiEvent: SharedFlow<PlayBroProductChooserEvent>
        get() = _uiEvent

    val uiState: StateFlow<ProductChooserUiState> = combine(
        _campaignAndEtalase,
        _focusedProductList,
        _selectedProductList,
        _loadParam,
        _saveState,
        _config,
    ) { campaignAndEtalase, focusedProductList, selectedProductList, loadParam, saveState,
        config ->
        ProductChooserUiState(
            campaignAndEtalase = campaignAndEtalase,
            focusedProductList = focusedProductList,
            selectedProductList = selectedProductList,
            loadParam = loadParam,
            saveState = saveState,
            config = config,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(STOP_TIME_OUT_MILLIS),
        ProductChooserUiState.Empty,
    )

    val summaryUiState = combine(
        _productTagSectionList,
        _productTagSummary,
    ) { productTagSectionList, productTagSummary ->
        PlayBroProductSummaryUiState(
            productTagSectionList = productTagSectionList,
            productCount = productTagSectionList.sumOf { it.products.size },
            productTagSummary = productTagSummary
        )
    }

    val selectedProducts: List<ProductUiModel>
        get() = _selectedProductList.value

    init {
        getCampaignAndEtalaseList()

        viewModelScope.launch {
            searchQuery.debounce(DEBOUNCE_TIME_OUT_MILLIS)
                .collectLatest { query ->
                    _loadParam.update {
                        it.copy(keyword = query)
                    }
                }
        }

        viewModelScope.launch {
            _loadParam.collectLatest {
                handleLoadProductList(it, true)
            }
        }

        viewModelScope.launch {
            _selectedProductList.collectLatest { products ->
                _saveState.update {
                    it.copy(
                        canSave = products.isNotEmpty()
                    )
                }
            }
        }

        viewModelScope.launch {
            _productTagSectionList.collectLatest { sections ->
                savedStateHandle.setProductSections(sections)
            }
        }
    }

    fun submitAction(action: ProductSetupAction) {
        when (action) {
            is ProductSetupAction.SetSort -> handleSetSort(action.sort)
            is ProductSetupAction.SelectEtalase -> handleSelectEtalase(action.etalase)
            is ProductSetupAction.SelectCampaign -> handleSelectCampaign(action.campaign)
            is ProductSetupAction.ToggleSelectProduct -> handleSelectProduct(action.product)
            is ProductSetupAction.SetProducts -> handleSetProducts(action.products)
            is ProductSetupAction.LoadProductList -> handleLoadProductList(
                param = _loadParam.value,
                resetList = false,
            )
            is ProductSetupAction.SearchProduct -> handleSearchProduct(action.keyword)
            ProductSetupAction.SaveProducts -> handleSaveProducts()
            ProductSetupAction.RetryFetchProducts -> handleRetryFetchProducts()
            is ProductSetupAction.DeleteSelectedProduct -> handleDeleteProduct(action.product)
            is ProductSetupAction.ClickPinProduct -> handleClickPin(action.product)
        }
    }

    private fun getCampaignAndEtalaseList() {
        _campaignAndEtalase.update {
            it.copy(state = NetworkState.Loading)
        }
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val campaignListDeferred = asyncCatchError(block = {
                repo.getCampaignList()
            }) { emptyList() }
            val etalaseListDeferred = asyncCatchError(block = {
                repo.getEtalaseList()
            }) { emptyList() }

            _campaignAndEtalase.update {
                it.copy(
                    campaignList = campaignListDeferred.await().orEmpty(),
                    etalaseList = etalaseListDeferred.await().orEmpty(),
                    state = NetworkState.Success,
                )
            }
        }) {
            _campaignAndEtalase.update {
                it.copy(state = NetworkState.Failed)
            }
        }
    }


    private fun handleSetSort(sort: SortUiModel) {
        _loadParam.update {
            it.copy(sort = sort)
        }
    }

    private fun handleSearchProduct(keyword: String) {
        searchQuery.value = keyword
    }

    private fun handleSelectEtalase(etalase: EtalaseUiModel) {
        _loadParam.update {
            it.copy(
                etalase = SelectedEtalaseModel.Etalase(etalase),
            )
        }
    }

    private fun handleSelectCampaign(campaign: CampaignUiModel) {
        _loadParam.update {
            it.copy(
                etalase = SelectedEtalaseModel.Campaign(campaign),
                sort = null,
                keyword = "",
            )
        }
    }

    private fun handleSelectProduct(product: ProductUiModel) = whenProductsNotSaving {
        if (product.stock <= 0) return@whenProductsNotSaving

        _selectedProductList.update { products ->
            val hasProduct = products.any { it.id == product.id }

            if (!hasProduct && configStore.getMaxProduct() > products.size) {
                products + product
            } else {
                products.filterNot { it.id == product.id }
            }
        }
    }

    private fun handleSetProducts(products: List<ProductUiModel>) = whenProductsNotSaving {
        val productsSize = min(configStore.getMaxProduct(), products.size)
        _selectedProductList.value = products.subList(0, productsSize).filterNot { it.stock <= 0 }
    }

    private fun handleLoadProductList(
        param: ProductListPaging.Param,
        resetList: Boolean,
    ) {
        if (!resetList && when (val result = _focusedProductList.value.resultState) {
            PageResultState.Loading -> true
            is PageResultState.Success -> !result.hasNextPage
            else -> false
        }) return

        _focusedProductList.update {
            it.copy(
                productList = if (resetList) emptyList() else it.productList,
                resultState = PageResultState.Loading,
            )
        }
        getProductListJob = viewModelScope.launchCatchError(dispatchers.io, block = {
            val pagingType = _focusedProductList.value.pagingType
            when (val selectedEtalase = _loadParam.value.etalase) {
                is SelectedEtalaseModel.Campaign -> {
                    val page = when {
                        resetList -> 1
                        pagingType is PagingType.Page -> pagingType.page + 1
                        else -> 1
                    }

                    val pagedProductList = repo.getProductsInCampaign(
                        campaignId = selectedEtalase.campaign.id,
                        page = page
                    )

                    _focusedProductList.update {
                        it.copy(
                            productList = it.productList + pagedProductList.dataList,
                            resultState = PageResultState.Success(pagedProductList.hasNextPage),
                            pagingType = PagingType.Page(page),
                        )
                    }
                }
                is SelectedEtalaseModel.Etalase,
                SelectedEtalaseModel.None -> {
                    val cursor = when {
                        resetList -> ""
                        pagingType is PagingType.Cursor -> pagingType.cursor
                        else -> ""
                    }

                    val pagedProductList = repo.getProductsInEtalase(
                        etalaseId = if (selectedEtalase is SelectedEtalaseModel.Etalase) {
                            selectedEtalase.etalase.id
                        } else "",
                        cursor = cursor,
                        keyword = param.keyword,
                        sort = param.sort ?: SortUiModel.supportedSortList.first(),
                    )

                    _focusedProductList.update {
                        it.copy(
                            productList = it.productList + pagedProductList.dataList,
                            resultState = PageResultState.Success(pagedProductList.hasNextPage),
                            pagingType = PagingType.Cursor(pagedProductList.cursor),
                        )
                    }
                }
            }
        }) { err ->
            _focusedProductList.update {
                it.copy(
                    resultState = PageResultState.Fail(err),
                )
            }
        }

        getProductListJob?.invokeOnCompletion { cause ->
            if (cause is CancellationException) {
                _focusedProductList.update {
                    it.copy(resultState = PageResultState.Fail(cause))
                }
            }
        }
    }

    private fun handleSaveProducts() {
        val currentState = _saveState.getAndUpdate {
            it.copy(isLoading = true)
        }
        if (currentState.isLoading) return

        viewModelScope.launchCatchError(dispatchers.io, block = {
            repo.setProductTags(
                channelId = configStore.getChannelId(),
                productIds = _selectedProductList.value.map(ProductUiModel::id),
            )

            getProductTagSummary()

            _uiEvent.emit(PlayBroProductChooserEvent.SaveProductSuccess)
        }) {
            _uiEvent.emit(PlayBroProductChooserEvent.ShowError(it))
        }.apply {
            invokeOnCompletion {
                _saveState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private fun handleRetryFetchProducts() {
        handleLoadProductList(_loadParam.value, resetList = false)
    }

    /** Product Summary */
    private fun handleDeleteProduct(product: ProductUiModel) {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _productTagSummary.value = ProductTagSummaryUiModel.Loading

            val productIds = _productTagSectionList.value.flatMap { section ->
                section.products.mapNotNull { if (it.id != product.id) it.id else null }
            }
            repo.setProductTags(channelId, productIds)

            _uiEvent.emit(PlayBroProductChooserEvent.DeleteProductSuccess(1))

            getProductTagSummary()
        }) {
            _productTagSummary.value = ProductTagSummaryUiModel.Unknown
            _uiEvent.emit(
                PlayBroProductChooserEvent.DeleteProductError(it) {
                    submitAction(ProductSetupAction.DeleteSelectedProduct(product))
                }
            )
        }
    }

    private suspend fun getProductTagSummary() {
        val response = repo.getProductTagSummarySection(channelId)

        _productTagSectionList.value = response
        _selectedProductList.value = response.flatMap { it.products }
        _productTagSummary.value = ProductTagSummaryUiModel.Success
    }

    /**
     * Util
     */
    private fun <T: Any> whenProductsNotSaving(fn: () -> T) {
        if (_saveState.value.isLoading) return
        fn()
    }

    private fun SavedStateHandle.getProductSections(): List<ProductTagSectionUiModel> {
        return savedStateHandle.get<List<ProductTagSectionUiModel>>(KEY_PRODUCT_SECTIONS).orEmpty()
    }

    private fun SavedStateHandle.setProductSections(
        productSectionList: List<ProductTagSectionUiModel>
    ) {
        savedStateHandle[KEY_PRODUCT_SECTIONS] = productSectionList
    }

    private fun SavedStateHandle.setEligiblePinStatus(
        isEligibleForPin: Boolean
    ) {
        savedStateHandle[KEY_ELIGIBLE_PIN] = isEligibleForPin
    }

    private fun SavedStateHandle.hasProductSections(): Boolean {
        return savedStateHandle.contains(KEY_PRODUCT_SECTIONS)
    }

    private fun SavedStateHandle.isEligibleForPin(): Boolean {
        return savedStateHandle[KEY_ELIGIBLE_PIN] ?: true
    }

    private fun handleClickPin(product: ProductUiModel){
        viewModelScope.launchCatchError(block = {
            product.updatePinProduct(isLoading = true, needToReset = true)
            val result = repo.setPinProduct(channelId, product)
            if(result)
                product.updatePinProduct(isLoading = false)
        }){
            product.updatePinProduct(isLoading = false, needToReset = true)
            _uiEvent.emit(PlayBroProductChooserEvent.FailPinUnPinProduct(it, product.pinStatus.isPinned))
        }
    }

    /**
     * hacky way needToReset -> find better approach
     */
    private fun ProductUiModel.updatePinProduct(isLoading: Boolean = false, needToReset: Boolean = false) {
        _productTagSectionList.update { sectionList ->
            sectionList.map { sectionUiModel ->
                sectionUiModel.copy(campaignStatus = sectionUiModel.campaignStatus, products =
                sectionUiModel.products.map { prod ->
                    if(prod.id == this.id)
                        prod.copy(pinStatus = this.pinStatus.copy(isLoading = isLoading, isPinned =
                            if(!needToReset) this.pinStatus.isPinned.switch()
                            else this.pinStatus.isPinned))
                    else prod.copy(pinStatus = prod.pinStatus.copy(isPinned = prod.pinStatus.isPinned && needToReset))
                })
            }
        }
    }

    companion object {
        private const val KEY_PRODUCT_SECTIONS = "product_sections"
        private const val KEY_ELIGIBLE_PIN = "eligible_pin_status"

        private const val STOP_TIME_OUT_MILLIS = 5000L
        private const val DEBOUNCE_TIME_OUT_MILLIS = 300L
    }
}
