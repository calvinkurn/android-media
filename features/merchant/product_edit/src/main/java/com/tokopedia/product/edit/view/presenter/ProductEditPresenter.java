package com.tokopedia.product.edit.view.presenter;

import com.tokopedia.product.common.domain.interactor.FetchProductVariantByCatUseCase;
import com.tokopedia.product.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.common.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.product.common.model.edit.ProductViewModel;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase;

import javax.inject.Inject;

import rx.Subscriber;
/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenter extends ProductAddPresenterImpl<ProductEditView> {
    private final GetProductDetailUseCase getProductDetailUseCase;

    @Inject
    public ProductEditPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                GetCategoryRecommUseCase getCategoryRecommUseCase,
                                ProductScoringUseCase productScoringUseCase,
                                AddProductShopInfoUseCase addProductShopInfoUseCase,
                                GetProductDetailUseCase getProductDetailUseCase,
                                FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase,
                                FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase) {
        super(saveDraftProductUseCase,
                fetchCatalogDataUseCase,
                getCategoryRecommUseCase,
                productScoringUseCase,
                addProductShopInfoUseCase,
                fetchCategoryDisplayUseCase,
                fetchProductVariantByCatUseCase);
        this.getProductDetailUseCase = getProductDetailUseCase;
    }

    public void fetchEditProductData(String productId) {
        getProductDetailUseCase.execute(GetProductDetailUseCase.createParams(productId),
                getFetchEditProductFormSubscriber());
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductDetailUseCase.unsubscribe();
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

}
