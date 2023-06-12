package com.tokopedia.catalog_library.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse
import com.tokopedia.catalog_library.usecase.CatalogBrandsPopularWithCatalogsUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogPopularBrandsViewModel @Inject constructor(
    private val catalogBrandsPopularWithCatalogsUseCase: CatalogBrandsPopularWithCatalogsUseCase
) : ViewModel() {

    private val _brandsWithCatalogsLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val brandsWithCatalogsLiveData: LiveData<Result<CatalogLibraryDataModel>> =
        _brandsWithCatalogsLiveData

    fun getBrandsWithCatalogs() {
        catalogBrandsPopularWithCatalogsUseCase.cancelJobs()
        catalogBrandsPopularWithCatalogsUseCase.getBrandPopularWithCatalogs(
            ::onAvailablePopularBrands,
            ::onFailHomeData
        )
    }

    private fun onAvailablePopularBrands(brandsPopularResponse: CatalogBrandsPopularResponse) {
        if (brandsPopularResponse.catalogGetBrandPopular.brands.isEmpty()) {
            onFailHomeData(IllegalStateException("No Brands Response Data"))
        } else {
            brandsPopularResponse.let {
                _brandsWithCatalogsLiveData.postValue(Success(mapPopularBrands(it)))
            }
        }
    }

    private fun mapPopularBrands(
        data: CatalogBrandsPopularResponse
    ): CatalogLibraryDataModel {
        return CatalogLibraryDataModel(
            getVisitableList(
                data.catalogGetBrandPopular.brands
            )
        )
    }

    private fun getVisitableList(
        brands: ArrayList<CatalogBrandsPopularResponse.CatalogGetBrandPopular.Brands>
    ): ArrayList<BaseCatalogLibraryDM> {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()

        brands.forEach { brand ->
            visitableList.add(
                CatalogPopularBrandsListDM(
                    CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS_WITH_CATALOGS,
                    "${CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS_WITH_CATALOGS}_${brand.id}",
                    brand
                )
            )
        }
        return visitableList
    }

    private fun onFailHomeData(throwable: Throwable) {
        _brandsWithCatalogsLiveData.postValue(Fail(throwable))
    }
}
