package com.tokopedia.product.manage.item.main.draft.view.presenter;

import com.tokopedia.product.manage.item.main.add.view.presenter.ProductAddPresenterImpl;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.draft.domain.FetchDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.SaveDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.view.listener.ProductDraftView;
import com.tokopedia.product.manage.item.variant.domain.FetchProductVariantByCatUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.user.session.UserSessionInterface;

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
                                     UserSessionInterface userSession,
                                     FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase,
                                     FetchDraftProductUseCase fetchDraftProductUseCase) {
        super(saveDraftProductUseCase, getShopInfoUseCase, userSession, fetchProductVariantByCatUseCase);
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
