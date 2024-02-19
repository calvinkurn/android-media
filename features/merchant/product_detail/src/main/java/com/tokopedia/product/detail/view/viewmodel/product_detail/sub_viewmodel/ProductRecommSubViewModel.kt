package com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.config.GlobalConfig
import com.tokopedia.library.subviewmodel.SubViewModel
import com.tokopedia.library.subviewmodel.extension.launch
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.usecase.GetProductRecommendationUseCase
import com.tokopedia.product.detail.view.util.ProductRecommendationMapper
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.detail.view.viewmodel.product_detail.IProductRecommSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ProductRecommendationEvent
import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ViewState
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

class ProductRecommSubViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getRecommendationUseCase: dagger.Lazy<GetRecommendationUseCase>,
    private val getProductRecommendationUseCase: dagger.Lazy<GetProductRecommendationUseCase>
) : SubViewModel(), IProductRecommSubViewModel {
    private var alreadyHitRecom: MutableList<String> = mutableListOf()

    private val _loadViewToView = MutableLiveData<Result<RecommendationWidget>>()
    override val loadViewToView: LiveData<Result<RecommendationWidget>>
        get() = _loadViewToView

    private val _verticalRecommendation = MutableLiveData<Result<RecommendationWidget>>()
    override val verticalRecommendation: LiveData<Result<RecommendationWidget>>
        get() = _verticalRecommendation

    private val _statusFilterTopAdsProduct = MutableLiveData<Result<Boolean>>()
    override val statusFilterTopAdsProduct: LiveData<Result<Boolean>>
        get() = _statusFilterTopAdsProduct

    private val _filterTopAdsProduct = MutableLiveData<ProductRecommendationDataModel>()
    override val filterTopAdsProduct: LiveData<ProductRecommendationDataModel>
        get() = _filterTopAdsProduct

    private val _loadTopAdsProduct = MutableLiveData<Result<RecommendationWidget>>()
    override val loadTopAdsProduct: LiveData<Result<RecommendationWidget>>
        get() = _loadTopAdsProduct

    private val _recomPageName = MutableSharedFlow<ProductRecommendationEvent.LoadRecommendation>()
    private val _refreshPage = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    override val resultData by lazy {
        _recomPageName
            .buffer()
            .flatMapMerge {
                getRecommendation(
                    pageName = it.pageName,
                    productId = it.productId,
                    isTokoNow = it.isTokoNow,
                    miniCart = it.miniCart,
                    queryParam = it.queryParam,
                    thematicId = it.thematicId
                )
            }
            .scan(mutableListOf<ProductRecommUiState>()) { accumulator, value ->
                if (_refreshPage.value) {
                    accumulator.clear()
                }

                accumulator.add(value)
                _refreshPage.emit(false)
                accumulator
            }
            .debounce(150)
            .map {
                val filteredData = it.filterNot {
                    it.alreadyCollected
                }
                filteredData.toMutableList()
            }
            .map {
                val alreadyCollectedData = it.map {
                    it.copy(alreadyCollected = true)
                }
                alreadyCollectedData.toMutableList()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = mutableListOf()
            )
    }

    override fun onEvent(event: ProductRecommendationEvent) {
        when (event) {
            is ProductRecommendationEvent.LoadRecommendation -> {
                viewModelScope.launch {
                    _recomPageName.emit(event)
                }
            }

            is ProductRecommendationEvent.RefreshRecommendation -> {
                viewModelScope.launch {
                    _refreshPage.emit(true)
                }
            }
        }
    }

    override fun loadViewToView(
        pageName: String,
        productId: String,
        isTokoNow: Boolean,
        queryParam: String,
        thematicId: String
    ) {
        if (GlobalConfig.isSellerApp()) return

        if (!alreadyHitRecom.contains(pageName)) {
            alreadyHitRecom.add(pageName)
        } else {
            return
        }

        launch {
            runCatching {
                val response = getRecommendationUseCase.get().getData(
                    GetRecommendationRequestParam(
                        pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                        pageName = pageName,
                        productIds = arrayListOf(productId),
                        isTokonow = isTokoNow,
                        queryParam = queryParam,
                        criteriaThematicIDs = listOf(thematicId),
                        hasNewProductCardEnabled = true
                    )
                )

                _loadViewToView.value =
                    if (!response.firstOrNull()?.recommendationItemList.isNullOrEmpty()) {
                        Success(response.first())
                    } else {
                        alreadyHitRecom.remove(pageName)
                        Fail(MessageErrorException())
                    }
            }.onFailure {
                alreadyHitRecom.remove(pageName)
                _loadViewToView.value = Throwable(pageName, it).asFail()
            }
        }
    }

    override fun getVerticalRecommendationData(
        pageName: String,
        page: Int?,
        productId: String?,
        queryParam: String,
        thematicId: String
    ) {
        val nonNullPage = page ?: ProductDetailConstant.DEFAULT_PAGE_NUMBER
        val nonNullProductId = productId.orEmpty()

        launch {
            runCatching {
                val requestParams = GetRecommendationRequestParam(
                    pageNumber = nonNullPage,
                    pageName = pageName,
                    productIds = arrayListOf(nonNullProductId),
                    queryParam = queryParam,
                    criteriaThematicIDs = listOf(thematicId),
                    hasNewProductCardEnabled = true
                )
                val recommendationResponse = getRecommendationUseCase.get().getData(requestParams)
                val dataResponse = recommendationResponse.firstOrNull()
                if (dataResponse == null) {
                    _verticalRecommendation.value = Fail(Throwable())
                } else {
                    _verticalRecommendation.value = dataResponse.asSuccess()
                }
            }.onFailure {
                _verticalRecommendation.value = Fail(it)
            }
        }
    }

    override fun recommendationChipClicked(
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip,
        productId: String
    ) {
        launch {
            runCatching {
                if (!GlobalConfig.isSellerApp()) {
                    val requestParams = GetRecommendationRequestParam(
                        pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                        pageName = recommendationDataModel.recomWidgetData?.pageName ?: "",
                        queryParam = if (annotationChip.recommendationFilterChip.isActivated) annotationChip.recommendationFilterChip.value else "",
                        productIds = arrayListOf(productId)
                    )
                    val recommendationResponse =
                        getRecommendationUseCase.get().getData(requestParams)
                    val updatedData = if (recommendationResponse.isNotEmpty() &&
                        recommendationResponse.first().recommendationItemList.isNotEmpty()
                    ) {
                        recommendationResponse.first()
                    } else {
                        null
                    }

                    updateFilterTopadsProduct(
                        updatedData,
                        recommendationDataModel,
                        annotationChip
                    )
                }
            }.onFailure {
                updateFilterTopadsProduct(
                    null,
                    recommendationDataModel,
                    annotationChip
                )
                _statusFilterTopAdsProduct.postValue(it.asFail())
            }
        }
    }

    private fun updateFilterTopadsProduct(
        updatedData: RecommendationWidget?,
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip
    ) {
        _filterTopAdsProduct.postValue(
            recommendationDataModel.copy(
                recomWidgetData = updatedData ?: recommendationDataModel.recomWidgetData,
                filterData = ProductRecommendationMapper.selectOrDeselectAnnotationChip(
                    filterData = recommendationDataModel.filterData,
                    name = annotationChip.recommendationFilterChip.name,
                    isActivated = annotationChip.recommendationFilterChip.isActivated
                )
            )
        )
    }

    private fun getRecommendation(
        pageName: String,
        productId: String,
        isTokoNow: Boolean,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?,
        queryParam: String,
        thematicId: String
    ) = flow {
        runCatching {
            val response = getProductRecommendationUseCase.get().executeOnBackground(
                GetProductRecommendationUseCase.createParams(
                    productId = productId,
                    pageName = pageName,
                    isTokoNow = isTokoNow,
                    miniCartData = miniCart,
                    queryParam = queryParam,
                    thematicId = thematicId
                )
            )

            emit(
                ProductRecommUiState(
                    data = ViewState.RenderSuccess(response),
                    pageName = pageName
                )
            )
        }.onFailure {
            emit(
                ProductRecommUiState(
                    data = ViewState.RenderFailure(Throwable(pageName)),
                    pageName = pageName
                )
            )
        }
    }.flowOn(dispatcher.io)

    override fun loadRecommendation(
        pageName: String,
        productId: String,
        isTokoNow: Boolean,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?,
        queryParam: String,
        thematicId: String
    ) {
        if (GlobalConfig.isSellerApp()) {
            return
        }

        if (!alreadyHitRecom.contains(pageName)) {
            alreadyHitRecom.add(pageName)
        } else {
            return
        }

        launch {
            runCatching {
                val response = getProductRecommendationUseCase.get().executeOnBackground(
                    GetProductRecommendationUseCase.createParams(
                        productId = productId,
                        pageName = pageName,
                        isTokoNow = isTokoNow,
                        miniCartData = miniCart,
                        queryParam = queryParam,
                        thematicId = thematicId
                    )
                )
                _loadTopAdsProduct.value = response.asSuccess()
            }.onFailure {
                _loadTopAdsProduct.value = Throwable(pageName).asFail()
            }
        }
    }

    override fun onResetAlreadyRecomHit() {
        alreadyHitRecom = mutableListOf()
    }
}
