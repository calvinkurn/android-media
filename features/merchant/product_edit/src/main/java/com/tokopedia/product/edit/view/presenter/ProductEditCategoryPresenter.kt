package com.tokopedia.product.edit.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase
import com.tokopedia.product.edit.common.util.ViewUtils
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel
import com.tokopedia.product.edit.domain.interactor.FetchCatalogDataUseCase
import com.tokopedia.product.edit.domain.interactor.GetCategoryRecommUseCase
import com.tokopedia.product.edit.domain.model.CategoryRecommDomainModel
import com.tokopedia.product.edit.view.listener.ProductEditCategoryView
import com.tokopedia.product.edit.view.mapper.CategoryRecommDomainToViewMapper
import rx.Subscriber
import rx.subjects.BehaviorSubject
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductEditCategoryPresenter
    @Inject constructor(private val getCategoryRecommUseCase: GetCategoryRecommUseCase,
                        private val fetchCategoryDisplayUseCase : FetchCategoryDisplayUseCase,
                        private val fetchCatalogDataUseCase: FetchCatalogDataUseCase):
        BaseDaggerPresenter<ProductEditCategoryView>(){

    var categoryId = -1L

    private val onProductName: BehaviorSubject<String> = BehaviorSubject.create()
    private val subscriptionProductName = onProductName.debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { productName ->
                getCategoryRecommendation(productName)
                fetchCatalogData(productName, categoryId, 0, 1)
            }!!

    fun getCategoryRecommendation(productName: String, limitRow: Int = 3){
        getCategoryRecommUseCase.execute(GetCategoryRecommUseCase.createRequestParams(productName, limitRow),
                object : Subscriber<CategoryRecommDomainModel>() {
                    override fun onNext(categoryRecommDomainModel: CategoryRecommDomainModel?) {
                        view?.onSuccessLoadRecommendationCategory(
                                CategoryRecommDomainToViewMapper.mapDomainView(categoryRecommDomainModel)
                                        .productCategoryPrediction)
                    }

                    override fun onCompleted() {}

                    override fun onError(throwable: Throwable?) {
                        view?.onErrorLoadRecommendationCategory(throwable)
                    }
                })
    }

     fun fetchCategory(categoryId: Long){
         val requestParam = FetchCategoryDisplayUseCase.generateParam(categoryId)
         fetchCategoryDisplayUseCase.execute(requestParam, FetchCategoryDisplaySubscriber())
     }

    fun fetchCatalogData(keyword: String, departmentId: Long, start: Int, rows: Int) {
        fetchCatalogDataUseCase.execute(
                FetchCatalogDataUseCase.createRequestParams(keyword, departmentId, start, rows),
                object : Subscriber<CatalogDataModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if (!isViewAttached) {
                            return
                        }
                        view.onErrorLoadCatalog(ViewUtils.getErrorMessage(e))
                    }

                    override fun onNext(catalogDataModel: CatalogDataModel) {
                        view.onSuccessLoadCatalog(keyword, departmentId, catalogDataModel.result.catalogs)
                    }
                })
    }

    fun onProductNameChange(productName: String){
        onProductName.onNext(productName)
    }

    private inner class FetchCategoryDisplaySubscriber : Subscriber<List<String>>() {
        override fun onCompleted() {

        }

        override fun onError(e: Throwable) {
            if (!isViewAttached) {
                return
            }
            checkViewAttached()
        }

        override fun onNext(strings: List<String>) {
            checkViewAttached()
            view.populateCategory(strings)
        }
    }

    override fun detachView() {
        super.detachView()
        getCategoryRecommUseCase.unsubscribe()
        fetchCategoryDisplayUseCase.unsubscribe()
        subscriptionProductName.unsubscribe()
    }
}