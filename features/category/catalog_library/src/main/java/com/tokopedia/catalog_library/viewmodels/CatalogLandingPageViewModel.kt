package com.tokopedia.catalog_library.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_MOST_VIRAL
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_TOP_FIVE
import com.tokopedia.catalog_library.usecase.CatalogListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogLandingPageViewModel @Inject constructor(
    private val catalogTopFiveUseCase: CatalogListUseCase,
    private val catalogMostViralUseCase: CatalogListUseCase,
    private val catalogProductListUseCase: CatalogListUseCase
) : ViewModel() {

    private val _catalogLandingPageLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogLandingPageLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> = _catalogLandingPageLiveData

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDataModel>()

    fun getCatalogTopFiveData(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        catalogTopFiveUseCase.cancelJobs()
        catalogTopFiveUseCase.getCatalogListData(
            ::onAvailableCatalogTopFiveData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    fun getCatalogMostViralData(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        catalogMostViralUseCase.cancelJobs()
        catalogMostViralUseCase.getCatalogListData(
            ::onAvailableCatalogMostViralData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    fun getCatalogListData(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        catalogProductListUseCase.cancelJobs()
        catalogProductListUseCase.getCatalogListData(
            ::onAvailableCatalogListData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    private fun onAvailableCatalogTopFiveData(categoryIdentifier: String, catalogListResponse: CatalogListResponse) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogTopFiveData(it)))
            }
        }
    }

    private fun onAvailableCatalogMostViralData(categoryIdentifier: String, catalogListResponse: CatalogListResponse) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogMostViralData(it)))
            }
        }
    }

    private fun onAvailableCatalogListData(categoryIdentifier: String, catalogListResponse: CatalogListResponse) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogProductData(it)))
            }
        }
    }

    private fun onFailLandingPageData(throwable: Throwable) {
        _catalogLandingPageLiveData.postValue(Fail(throwable))
    }

    private fun mapCatalogTopFiveData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogTopFiveDataModel =
            CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                "Top 5 category terlaris di toped",
                getTopFiveVisitableList(data.catalogGetList.catalogsProduct)
            )
        listOfComponents.add(catalogTopFiveDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getTopFiveVisitableList(catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>): ArrayList<BaseCatalogLibraryDataModel> {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        catalogsProduct.forEachIndexed { index, catalogTopFive ->
            catalogTopFive.rank = (index + 1)
            visitableList.add(
                CatalogTopFiveDataModel(
                    CATALOG_TOP_FIVE,
                    CATALOG_TOP_FIVE,
                    catalogTopFive
                )
            )
        }
        return visitableList
    }

    private fun mapCatalogMostViralData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogMostViralDataModel =
            CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                "",
                getMostViralVisitableList(data.catalogGetList.catalogsProduct)
            )
        listOfComponents.add(catalogMostViralDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getMostViralVisitableList(catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>): ArrayList<BaseCatalogLibraryDataModel> {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        catalogsProduct.forEach {
            visitableList.add(CatalogMostViralDataModel(CATALOG_MOST_VIRAL, CATALOG_MOST_VIRAL, it))
        }
        return visitableList
    }

    private fun mapCatalogProductData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogProductDataModel =
            CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CatalogLibraryConstant.CATALOG_PRODUCT,
                "Katalog lainnya buat inspirasimu",
                getProductsVisitableList(data.catalogGetList.catalogsProduct),
                RecyclerView.VERTICAL
            )

        listOfComponents.add(catalogProductDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getProductsVisitableList(catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>): ArrayList<BaseCatalogLibraryDataModel> {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        catalogsProduct.forEachIndexed { index, product ->
            product.rank = (index + 1)
            visitableList.add(
                CatalogProductDataModel(
                    CatalogLibraryConstant.CATALOG_PRODUCT,
                    CatalogLibraryConstant.CATALOG_PRODUCT,
                    product
                )
            )
        }
        return visitableList
    }
}
