package com.tokopedia.product.edit.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.TextApiUtils;
import com.tokopedia.product.edit.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.edit.common.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.product.edit.common.util.ViewUtils;
import com.tokopedia.product.edit.common.model.edit.ProductViewModel;
import com.tokopedia.product.edit.view.listener.ProductAddView;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductAddPresenterImpl<T extends ProductAddView> extends BaseDaggerPresenter<T> implements ProductAddPresenter<T> {

    private final SaveDraftProductUseCase saveDraftProductUseCase;
    private final GetShopInfoUseCase getShopInfoUseCase;
    private UserSession userSession;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                   GetShopInfoUseCase getShopInfoUseCase,
                                   UserSession userSession) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.userSession = userSession;
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
                getView().onSuccessLoadShopInfo(shopInfo.getInfo().isShopIsGoldBadge(),
                        TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsFreeReturns()),
                        TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial()));
            }
        });
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
    }
}
