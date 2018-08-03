package com.tokopedia.product.edit.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel
import com.tokopedia.product.edit.domain.interactor.FetchCatalogDataUseCase
import com.tokopedia.product.edit.domain.interactor.GetCategoryRecommUseCase
import com.tokopedia.product.edit.domain.model.CategoryRecommDomainModel
import com.tokopedia.product.edit.view.listener.ProductEditCategoryView
import com.tokopedia.product.edit.view.mapper.CategoryRecommDomainToViewMapper
import rx.Subscriber
import javax.inject.Inject

class ProductEditCategoryPresenter
    @Inject constructor(private val getCategoryRecommUseCase: GetCategoryRecommUseCase):
        BaseDaggerPresenter<ProductEditCategoryView>(){

    fun getCategotyRecommendation(productName: String, limitRow: Int = 3){
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

    override fun detachView() {
        super.detachView()
        getCategoryRecommUseCase.unsubscribe()
    }
}