package com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

class ProductRecommSubViewModel @Inject constructor(
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

    override fun loadViewToView(
        pageName: String,
        productId: String,
        isTokoNow: Boolean
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
                        isTokonow = isTokoNow
                    )
                )

                _loadViewToView.value = if (response.isNotEmpty()) {
                    Success(response.first())
                } else {
                    Fail(MessageErrorException())
                }
            }.onFailure {
                alreadyHitRecom.remove(pageName)
                _loadViewToView.value = Throwable(pageName, it).asFail()
            }
        }
    }

    override fun getVerticalRecommendationData(pageName: String, page: Int?, productId: String?) {
        val nonNullPage = page ?: ProductDetailConstant.DEFAULT_PAGE_NUMBER
        val nonNullProductId = productId.orEmpty()

        launch {
            runCatching {
                val requestParams = GetRecommendationRequestParam(
                    pageNumber = nonNullPage,
                    pageName = pageName,
                    productIds = arrayListOf(nonNullProductId)
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
