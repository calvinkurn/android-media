package com.tokopedia.product.manage.item.main.add.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.TextApiUtils;
import com.tokopedia.product.manage.item.common.util.ViewUtils;
import com.tokopedia.product.manage.item.main.add.view.listener.ProductAddView;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.draft.domain.SaveDraftProductUseCase;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.variant.domain.FetchProductVariantByCatUseCase;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductAddPresenterImpl<T extends ProductAddView> extends BaseDaggerPresenter<T> implements ProductAddPresenter<T> {

    private final SaveDraftProductUseCase saveDraftProductUseCase;
    private final GetShopInfoUseCase getShopInfoUseCase;
    protected final FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase;
    private UserSessionInterface userSession;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                   GetShopInfoUseCase getShopInfoUseCase,
                                   UserSessionInterface userSession,
                                   FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.userSession = userSession;
        this.fetchProductVariantByCatUseCase = fetchProductVariantByCatUseCase;
    }

    @Override
    public void saveDraft(ProductViewModel viewModel, boolean isUploading) {
        saveDraftProductUseCase.execute(generateRequestParamAddDraft(viewModel, isUploading),
                new SaveDraftSubscriber(isUploading));
    }

    @Override
    public void getShopInfo() {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(userSession.getShopId()), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorLoadShopInfo(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessLoadShopInfo(shopInfo.getOwner().isIsGoldMerchant(),
                        TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsFreeReturns()),
                        TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial()));
            }
        });
    }

    @Override
    public void fetchProductVariantByCat(long categoryId) {
        com.tokopedia.core.base.domain.RequestParams requestParam = FetchProductVariantByCatUseCase.generateParam(categoryId);
        fetchProductVariantByCatUseCase.execute(requestParam, getProductVariantSubscriber());
    }

    private Subscriber<List<ProductVariantByCatModel>> getProductVariantSubscriber() {
        return new Subscriber<List<ProductVariantByCatModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorGetProductVariantByCat(e);
            }

            @Override
            public void onNext(List<ProductVariantByCatModel> s) {
                getView().onSuccessGetProductVariantCat(s);
            }
        };
    }


    private class SaveDraftSubscriber extends Subscriber<Long> {

        boolean isUploading;
        SaveDraftSubscriber(boolean isUploading) {
            this.isUploading = isUploading;
        }
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (!isViewAttached()) {
                return;
            }
            if (isUploading) {
                getView().onErrorStoreProductToDraftWhenUpload(ViewUtils.getErrorMessage(e));
            } else {
                getView().onErrorStoreProductToDraftWhenBackPressed(ViewUtils.getErrorMessage(e));
            }
        }

        @Override
        public void onNext(Long productId) {
            checkViewAttached();
            getView().onSuccessStoreProductToDraft(productId, isUploading);
        }
    }

    private RequestParams generateRequestParamAddDraft(ProductViewModel viewModel, boolean isUploading) {
        return SaveDraftProductUseCase.generateUploadProductParam(viewModel, getProductDraftId(),
                isUploading);
    }

    private long getProductDraftId(){
        return getView().getProductDraftId();
    }

    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
        saveDraftProductUseCase.unsubscribe();
        fetchProductVariantByCatUseCase.unsubscribe();
    }
}
