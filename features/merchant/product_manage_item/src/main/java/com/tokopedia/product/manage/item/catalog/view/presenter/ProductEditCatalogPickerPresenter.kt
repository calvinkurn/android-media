package com.tokopedia.product.manage.item.catalog.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.product.manage.item.catalog.domain.FetchCatalogDataUseCase
import com.tokopedia.product.manage.item.catalog.view.listener.ProductEditCatalogPickerView
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.CatalogDataModel
import rx.Subscriber
import javax.inject.Inject

class ProductEditCatalogPickerPresenter
    @Inject constructor(private val fetchCatalogDataUseCase: FetchCatalogDataUseCase)
    : BaseDaggerPresenter<ProductEditCatalogPickerView>(){

    fun getCatalog(productName: String, categoryId: Long, page: Int = 0, rows: Int = 20){
        val start = (page - 1) * rows
        fetchCatalogDataUseCase.unsubscribe()
        fetchCatalogDataUseCase.execute(FetchCatalogDataUseCase.createRequestParams(productName, categoryId, start, rows),
                object : Subscriber<CatalogDataModel>() {
                    override fun onNext(catalogDataModel: CatalogDataModel?) {
                        catalogDataModel?.run {
                            fun Catalog.toProductCatalog() = ProductCatalog(catalogId, catalogName, catalogImage300)
                            view?.onSuccessLoadCatalog(result.catalogs.map { it.toProductCatalog() },
                                    start+rows < result.totalRecord)
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