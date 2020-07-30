package com.tokopedia.product.manage.item.category.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase
import com.tokopedia.product.manage.item.catalog.domain.FetchCatalogDataUseCase
import com.tokopedia.product.manage.item.common.util.ViewUtils
import com.tokopedia.product.manage.item.catalog.view.listener.ProductEditCategoryView
import com.tokopedia.product.manage.item.category.domain.GetCategoryRecommendationUseCase
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.CatalogDataModel
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommendationdata.Category
import rx.Subscriber
import rx.subjects.BehaviorSubject
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductEditCategoryPresenter
    @Inject constructor(private val getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase,
                        private val fetchCategoryDisplayUseCase : FetchCategoryDisplayUseCase,
                        private val fetchCatalogDataUseCase: FetchCatalogDataUseCase):
        BaseDaggerPresenter<ProductEditCategoryView>(){

    var categoryId = -1L

    private val onProductName: BehaviorSubject<String> = BehaviorSubject.create()
    private val subscriptionProductName = onProductName.debounce(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { productName ->
                getCategoryRecommendation(productName)
                fetchCatalogData(productName, categoryId, 0, 1)
            }!!

    fun getCategoryRecommendation(productName: String){
        getCategoryRecommendationUseCase.execute(GetCategoryRecommendationUseCase.createRequestParams(productName),
                object : Subscriber<List<Category>>() {

                    override fun onNext(productCategoryList: List<Category>?) {
                        if (productCategoryList != null) view?.onSuccessLoadRecommendationCategory(productCategoryList)
                    }

                    override fun onCompleted() {}

                    override fun onError(throwable: Throwable?) {
                        if(!isViewAttached){
                            return
                        }
                        view?.onErrorLoadRecommendationCategory(throwable)
                    }
                })
    }

     fun fetchCategory(categoryId: Long){
         val requestParam = FetchCategoryDisplayUseCase.generateParam(categoryId)
         fetchCategoryDisplayUseCase.execute(requestParam, FetchCategoryDisplaySubscriber(categoryId))
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

    private inner class FetchCategoryDisplaySubscriber(categoryId: Long) : Subscriber<List<String>>() {
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
            view.populateCategory(strings, categoryId)
        }
    }

    override fun detachView() {
        super.detachView()
        getCategoryRecommendationUseCase.unsubscribe()
        fetchCategoryDisplayUseCase.unsubscribe()
        subscriptionProductName.unsubscribe()
    }
}