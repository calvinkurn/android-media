package com.tokopedia.catalog.usecase.listing

import com.tokopedia.catalog.model.raw.ProductListResponse
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.model.util.CatalogProductListMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject


class CatalogGetProductListUseCase @Inject constructor(private val categoryProductUseCase: CatalogCategoryProductUseCase,
                                                       private val topAdsProductsUseCase: CatalogTopAdsProductsUseCase) : UseCase<ProductListResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ProductListResponse> {

        val paramProductListing = RequestParams.create()
        val paramTopAd = RequestParams.create()
        paramProductListing.putString("params", requestParams?.getString(CatalogConstant.PRODUCT_PARAMS, ""))

        paramTopAd.putString("params", requestParams?.getString(CatalogConstant.TOP_ADS_PARAMS, ""))

        return Observable.zip(
                categoryProductUseCase.createObservable(paramProductListing).subscribeOn(Schedulers.io()),
                topAdsProductsUseCase.createObservable(paramTopAd).subscribeOn(Schedulers.io())
        ) { aceSearchProductResponse, topAdsResponse ->
            aceSearchProductResponse?.searchProduct?.data?.totalData = aceSearchProductResponse?.searchProduct?.header?.totalData ?: 0
            CatalogProductListMapper().transform(ProductListResponse(aceSearchProductResponse.searchProduct), topAdsResponse)
        }

    }
}