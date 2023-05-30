package com.tokopedia.catalog_library.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.usecase.CatalogProductsUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogProductsBaseViewModel @Inject constructor(
    private val catalogProductsUseCase: CatalogProductsUseCase
) : ViewModel() {

    private val _catalogProductsLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogProductsLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> =
        _catalogProductsLiveData

    private val _shimmerLiveData = MutableLiveData<Boolean>()
    val shimmerLiveData: LiveData<Boolean> =
        _shimmerLiveData

    private var sourceScreen: String = ""

    fun getCatalogListData(
        source: String,
        categoryId: String,
        sortType: Int,
        rows: Int,
        page: Int = 1,
        brandId: String = ""
    ) {
        sourceScreen = source
        addProductShimmer()
        catalogProductsUseCase.cancelJobs()
        catalogProductsUseCase.getCatalogProductsData(
            ::onAvailableCatalogListData,
            ::onFailHomeData,
            categoryId,
            sortType,
            rows,
            page,
            brandId
        )
    }

    private fun addProductShimmer() {
        _shimmerLiveData.postValue(true)
    }

    private fun onAvailableCatalogListData(
        catalogListResponse: CatalogListResponse,
        page: Int = 1
    ) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isNullOrEmpty()) {
            onFailHomeData(IllegalStateException("No Catalog List Response Data"))
        } else {
            catalogListResponse.let {
                _catalogProductsLiveData.postValue(Success(mapCatalogProductData(it, page)))
            }
        }
    }

    private fun onFailHomeData(throwable: Throwable) {
        _catalogProductsLiveData.postValue(Fail(throwable))
    }

    private fun mapCatalogProductData(
        data: CatalogListResponse,
        page: Int = 1
    ): CatalogLibraryDataModel {
        return CatalogLibraryDataModel(
            getProductsVisitableList(
                data.catalogGetList.categoryName,
                data.catalogGetList.catalogsProduct,
                page
            )
        )
    }

    private fun getProductsVisitableList(
        categoryName: String,
        catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>,
        page: Int = 1
    ): ArrayList<BaseCatalogLibraryDM> {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()
        val title = if (categoryName.isBlank()) {
            CatalogLibraryConstant.CATALOG_HOME_PRODUCT_TITLE
        } else {
            "Semua katalog ${categoryName.lowercase()}"
        }
        if (sourceScreen != SOURCE_CATEGORY_BRAND_LANDING_PAGE && page == 1) {
            val productHeaderModel = CatalogContainerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_PRODUCT_HEADER,
                CatalogLibraryConstant.CATALOG_CONTAINER_PRODUCT_HEADER,
                title,
                null,
                marginForTitle = Margin(32, 16, 12, 16)
            )
            visitableList.add(productHeaderModel)
        }
        catalogsProduct.forEach { product ->
            visitableList.add(
                CatalogProductDM(
                    CatalogLibraryConstant.CATALOG_PRODUCT,
                    "${CatalogLibraryConstant.CATALOG_PRODUCT}_${product.id}",
                    product,
                    sourceScreen,
                    categoryName
                )
            )
        }
        return visitableList
    }
}
