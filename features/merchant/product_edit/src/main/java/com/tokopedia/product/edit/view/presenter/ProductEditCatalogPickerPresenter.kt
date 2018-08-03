package com.tokopedia.product.edit.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel
import com.tokopedia.product.edit.domain.interactor.FetchCatalogDataUseCase
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.view.listener.ProductEditCatalogPickerView
import rx.Subscriber
import javax.inject.Inject

class ProductEditCatalogPickerPresenter
    @Inject constructor(private val fetchCatalogDataUseCase: FetchCatalogDataUseCase)
    : BaseDaggerPresenter<ProductEditCatalogPickerView>(){

    fun getCatalog(productName: String, categoryId: Long, start: Int = 0, rows: Int = 20){
        fetchCatalogDataUseCase.unsubscribe()
        fetchCatalogDataUseCase.execute(FetchCatalogDataUseCase.createRequestParams(productName, categoryId, start, rows),
                object : Subscriber<CatalogDataModel>() {
                    override fun onNext(catalogDataModel: CatalogDataModel?) {
                        catalogDataModel?.run {
                            fun Catalog.toProductCatalog() = ProductCatalog(catalogId, catalogName, catalogImage300)
                            view?.onSuccessLoadCatalog(result.catalogs.map { it.toProductCatalog() },
                                    result.totalRecord)
                        }
                    }

                    override fun onCompleted() {}

                    override fun onError(throwable: Throwable?) {
                        view?.onErrorLoadCatalog(throwable)
                    }

                })
    }

    override fun detachView() {
        super.detachView()
        fetchCatalogDataUseCase.unsubscribe()
    }
}