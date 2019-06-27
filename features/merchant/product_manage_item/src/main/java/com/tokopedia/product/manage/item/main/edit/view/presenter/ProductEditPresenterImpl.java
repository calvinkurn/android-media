package com.tokopedia.product.manage.item.main.edit.view.presenter;

import com.tokopedia.product.manage.item.common.domain.GetProductDetailUseCase;
import com.tokopedia.product.manage.item.main.add.view.presenter.ProductAddPresenterImpl;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.draft.domain.SaveDraftProductUseCase;
import com.tokopedia.product.manage.item.main.edit.view.listener.ProductEditView;
import com.tokopedia.product.manage.item.variant.domain.FetchProductVariantByCatUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenterImpl extends ProductAddPresenterImpl<ProductEditView> implements ProductEditPresenter {

    private final GetProductDetailUseCase getProductDetailUseCase;

    @Inject
    public ProductEditPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                    GetShopInfoUseCase getShopInfoUseCase,
                                    UserSessionInterface userSession,
                                    FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase,
                                    GetProductDetailUseCase getProductDetailUseCase) {
        super(saveDraftProductUseCase, getShopInfoUseCase, userSession, fetchProductVariantByCatUseCase);
        this.getProductDetailUseCase = getProductDetailUseCase;
    }


    @Override
    public void fetchEditProductData(String productId) {
        getProductDetailUseCase.execute(GetProductDetailUseCase.createParams(productId),
                getFetchEditProductFormSubscriber());
    }

    private Subscriber<ProductViewModel> getFetchEditProductFormSubscriber() {
        return new Subscriber<ProductViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorFetchEditProduct(e);
            }

            @Override
            public void onNext(ProductViewModel productViewModel) {
                getView().onSuccessLoadProduct(productViewModel);
            }
        };
    }


    @Override
    public void detachView() {
        super.detachView();
        getProductDetailUseCase.unsubscribe();
    }
}
