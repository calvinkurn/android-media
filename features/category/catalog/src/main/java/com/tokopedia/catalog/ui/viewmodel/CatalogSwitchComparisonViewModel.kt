package com.tokopedia.catalog.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.catalog.domain.CatalogComparisonProductUseCase
import com.tokopedia.catalog.ui.mapper.map
import com.tokopedia.catalog.ui.model.CatalogComparisonProductsUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatalogSwitchComparisonViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val catalogDetailUseCase: CatalogDetailUseCase,
    private val catalogComparisonProductUseCase: CatalogComparisonProductUseCase
) : BaseViewModel(dispatchers.main) {

    private val _errorsToasterGetCatalogListing = MutableLiveData<Throwable>()
    val errorsToasterGetCatalogListing: LiveData<Throwable>
        get() = _errorsToasterGetCatalogListing

    private val _errorsToasterGetInitComparison = MutableLiveData<Throwable>()
    val errorsToasterGetInitComparison: LiveData<Throwable>
        get() = _errorsToasterGetInitComparison

    private val _comparisonUiModel = MutableLiveData<CatalogComparisonProductsUiModel?>()
    val comparisonUiModel: LiveData<CatalogComparisonProductsUiModel?>
        get() = _comparisonUiModel

    private val _catalogListingUiModel = MutableLiveData<CatalogComparisonProductsUiModel?>()
    val catalogListingUiModel: LiveData<CatalogComparisonProductsUiModel?>
        get() = _catalogListingUiModel

    fun getComparisonProducts(
        catalogId: String,
        brand: String,
        categoryId: String,
        limit: Int,
        page: Int,
        name: String
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = catalogComparisonProductUseCase.getCatalogComparisonProducts(
                    catalogId,
                    brand,
                    categoryId,
                    limit.toString(),
                    page.toString(),
                    name
                )
                _catalogListingUiModel.postValue(result.map())
            },
            onError = {
                _errorsToasterGetCatalogListing.postValue(it)
            }
        )
    }

    fun loadAllDataInOnePage(
        catalogId: String,
        brand: String,
        categoryId: String,
        limit: Int,
        compareCatalogId: List<String>,
        name: String
    ) {
        launchCatchError(
            block = {
                val response = withContext(dispatchers.io) {
                    val result = async {
                        catalogDetailUseCase.getCatalogDetailV4Comparison(
                            catalogId,
                            compareCatalogId
                        )
                    }

                    val resultComparisonProducts = async {
                        catalogComparisonProductUseCase.getCatalogComparisonProducts(
                            catalogId,
                            brand,
                            categoryId,
                            limit.toString(),
                            Int.ONE.toString(),
                            name
                        )
                    }

                    result.await()?.map() to resultComparisonProducts.await().map()
                }

                _comparisonUiModel.postValue(response.first)
                _catalogListingUiModel.postValue(response.second)
            },
            onError = {
                _errorsToasterGetInitComparison.postValue(it)
            }
        )
    }
}
