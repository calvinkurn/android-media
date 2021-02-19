package com.tokopedia.catalog.usecase.listing

import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.common_category.usecase.mapper.ProductListMapper
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
        paramProductListing.putString("params", requestParams?.getString("product_params", ""))

        paramTopAd.putString("params", requestParams?.getString("top_params", ""))

        return Observable.zip(
                categoryProductUseCase.createObservable(paramProductListing).subscribeOn(Schedulers.io()),
                topAdsProductsUseCase.createObservable(paramTopAd).subscribeOn(Schedulers.io())
        ) { aceSearchProductResponse, topAdsResponse ->
            ProductListMapper().transform(ProductListResponse(aceSearchProductResponse.aceSearchProduct?.searchData), topAdsResponse)

        }

    }
}