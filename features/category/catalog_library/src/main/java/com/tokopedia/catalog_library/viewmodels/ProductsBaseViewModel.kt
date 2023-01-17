package com.tokopedia.catalog_library.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogContainerDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogProductDataModel
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.usecase.CatalogProductsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductsBaseViewModel @Inject constructor(
    private val catalogProductsUseCase: CatalogProductsUseCase
) : ViewModel() {

    private val _catalogProductsLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogProductsLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> =
        _catalogProductsLiveData

    private val _shimmerLiveData = MutableLiveData<Boolean>()
    val shimmerLiveData: LiveData<Boolean> =
        _shimmerLiveData

    fun getCatalogListData(
        categoryIdentifier: String,
        sortType: Int,
        rows: Int,
        page: Int = 1
    ) {
        addProductShimmer()
        catalogProductsUseCase.cancelJobs()
        catalogProductsUseCase.getCatalogProductsData(
            ::onAvailableCatalogListData,
            ::onFailHomeData,
            categoryIdentifier,
            sortType,
            rows,
            page
        )
    }

    private fun addProductShimmer() {
        _shimmerLiveData.postValue(true)
    }

    private fun onAvailableCatalogListData(
        catalogListResponse: CatalogListResponse, page: Int = 1
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
                data.catalogGetList.catalogsProduct,
                page
            )
        )
    }

    private fun getProductsVisitableList(
        catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>,
        page: Int = 1
    ): ArrayList<BaseCatalogLibraryDataModel> {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        if (page == 1) {
            val productHeaderModel = CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_PRODUCT_HEADER,
                CatalogLibraryConstant.CATALOG_CONTAINER_PRODUCT_HEADER,
                CatalogLibraryConstant.CATALOG_HOME_PRODUCT_TITLE, null
            )
            visitableList.add(productHeaderModel)
        }
        catalogsProduct.forEach { product ->
            visitableList.add(
                CatalogProductDataModel(
                    CatalogLibraryConstant.CATALOG_PRODUCT,
                    "${CatalogLibraryConstant.CATALOG_PRODUCT}_${product.id}",
                    product
                )
            )
        }
        return visitableList
    }
}
