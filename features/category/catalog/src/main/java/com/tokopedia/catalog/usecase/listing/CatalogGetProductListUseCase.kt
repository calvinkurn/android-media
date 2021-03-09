package com.tokopedia.catalog.usecase.listing

import com.tokopedia.catalog.model.raw.ProductListResponse
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.model.util.CatalogProductListMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject


class CatalogGetProductListUseCase @Inject constructor(private val categoryProductUseCase: CatalogCategoryProductUseCase) : UseCase<ProductListResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ProductListResponse> {

        val paramProductListing = RequestParams.create()
        paramProductListing.putString("params", requestParams?.getString(CatalogConstant.PRODUCT_PARAMS, ""))

        return categoryProductUseCase.createObservable(paramProductListing).flatMap { aceSearchProductResponse ->
            aceSearchProductResponse?.searchProduct?.data?.totalData = aceSearchProductResponse?.searchProduct?.header?.totalData ?: 0
            Observable.just(CatalogProductListMapper().transform(aceSearchProductResponse))
        }
    }
}