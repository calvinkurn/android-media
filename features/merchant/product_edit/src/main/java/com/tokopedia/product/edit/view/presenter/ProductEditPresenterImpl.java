package com.tokopedia.product.edit.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.product.edit.common.domain.interactor.FetchProductVariantByCatUseCase;
import com.tokopedia.product.edit.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.edit.common.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.product.edit.common.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.edit.common.util.ViewUtils;
import com.tokopedia.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.product.edit.common.model.edit.ProductViewModel;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase;
import com.tokopedia.product.edit.domain.model.CategoryRecommDomainModel;
import com.tokopedia.product.edit.view.mapper.CategoryRecommDomainToViewMapper;
import com.tokopedia.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenterImpl extends ProductAddPresenterImpl<ProductEditView> implements ProductEditPresenter {

    private final GetProductDetailUseCase getProductDetailUseCase;

    @Inject
    public ProductEditPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                    GetShopInfoUseCase getShopInfoUseCase,
                                    UserSession userSession,
                                    GetProductDetailUseCase getProductDetailUseCase) {
        super(saveDraftProductUseCase, getShopInfoUseCase, userSession);
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
