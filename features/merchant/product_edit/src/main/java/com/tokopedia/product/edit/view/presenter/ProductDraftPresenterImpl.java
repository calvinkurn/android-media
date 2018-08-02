package com.tokopedia.product.edit.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.product.edit.common.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.product.edit.common.domain.interactor.FetchProductVariantByCatUseCase;
import com.tokopedia.product.edit.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.edit.common.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.product.edit.common.model.edit.ProductViewModel;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftPresenterImpl extends ProductAddPresenterImpl<ProductDraftView> implements ProductDraftPresenter{

    private FetchDraftProductUseCase fetchDraftProductUseCase;

    @Inject
    public ProductDraftPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                     GetShopInfoUseCase getShopInfoUseCase,
                                     UserSession userSession,
                                     FetchDraftProductUseCase fetchDraftProductUseCase) {
        super(saveDraftProductUseCase, getShopInfoUseCase, userSession);
        this.fetchDraftProductUseCase = fetchDraftProductUseCase;
    }

    public void fetchDraftData(long draftProductId) {
        fetchDraftProductUseCase.execute(FetchDraftProductUseCase.createRequestParams(draftProductId), getSubsriberFetchDraft());
    }

    public Subscriber<ProductViewModel> getSubsriberFetchDraft() {
        return new Subscriber<ProductViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorLoadProduct(e);
            }

            @Override
            public void onNext(ProductViewModel productViewModel) {
                checkViewAttached();
                getView().onSuccessLoadProduct(productViewModel);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchDraftProductUseCase.unsubscribe();
    }
}
