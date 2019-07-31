package com.tokopedia.product.manage.item.main.base.domain;

import android.text.TextUtils;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.manage.item.common.domain.GetProductDetailUseCase;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.product.manage.item.main.base.data.exception.ImageUploadErrorException;
import com.tokopedia.product.manage.item.main.base.data.model.BasePictureViewModel;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.base.domain.mapper.ProductUploadMapper;
import com.tokopedia.product.manage.item.main.base.view.listener.ProductSubmitNotificationListener;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class SubmitProductUseCase extends UseCase<Integer> {

    private static final String NOTIFICATION_COUNT_LISTENER = "NOTIFICATION_COUNT_LISTENER";
    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final UploadProductImageUseCase uploadProductImageUseCase;
    private final SubmitRawProductUseCase submitRawProductUseCase;
    private final ProductUploadMapper productUploadMapper;

    private ProductSubmitNotificationListener notificationCountListener;

    @Inject
    public SubmitProductUseCase(
            GetShopInfoUseCase getShopInfoUseCase,
            GetProductDetailUseCase getProductDetailUseCase,
            UploadProductImageUseCase uploadProductImageUseCase,
            SubmitRawProductUseCase submitRawProductUseCase,
            ProductUploadMapper productUploadMapper) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.uploadProductImageUseCase = uploadProductImageUseCase;
        this.submitRawProductUseCase = submitRawProductUseCase;
        this.productUploadMapper = productUploadMapper;
    }

    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        ProductViewModel productViewModel = (ProductViewModel) requestParams.getObject(PRODUCT_VIEW_MODEL);
        notificationCountListener = (ProductSubmitNotificationListener) requestParams.getObject(NOTIFICATION_COUNT_LISTENER);
        return Observable.just(productViewModel)
                .flatMap((Func1<ProductViewModel, Observable<ProductViewModel>>) productFromDraft -> {
                    if (TextUtils.isEmpty(productFromDraft.getProductId())) {
                        return Observable.just(productFromDraft);
                    } else {
                        return getProductDetailUseCase.createObservable(GetProductDetailUseCase.createParams(productFromDraft.getProductId()))
                                .map(removeUnusedParam(productFromDraft));
                    }
                })
                .doOnNext(productViewModel1 -> notificationCountListener.addProgress())
                .flatMap((Func1<ProductViewModel, Observable<ProductViewModel>>) productViewModel12 -> {
                    // Replace product id to shop id if new, recommendation from power ranger team to avoid null on image path
                    if (!TextUtils.isEmpty(productViewModel12.getProductId())) {
                        return uploadProductPhoto(productViewModel12, productViewModel12.getProductId());
                    } else {
                        return getShopInfoUseCase.createObservable()
                                .flatMap((Func1<ShopModel, Observable<ProductViewModel>>) shopModel ->
                                        uploadProductPhoto(productViewModel12, shopModel.getInfo().getShopId()));
                    }
                })
                .onErrorResumeNext(throwable -> {
                    if (!(throwable instanceof SocketTimeoutException) && !(throwable instanceof UnknownHostException)) {
                        throw new ImageUploadErrorException();
                    }
                    return Observable.error(throwable);
                })
                .doOnNext(productViewModel13 -> notificationCountListener.addProgress())
                .flatMap((Func1<ProductViewModel, Observable<Integer>>) productViewModel14 ->
                        submitRawProductUseCase.createObservable(SubmitRawProductUseCase.createParams(productViewModel14)))
                .doOnNext(aBoolean -> notificationCountListener.addProgress());
    }

    private Observable<ProductViewModel> uploadProductPhoto(final ProductViewModel productViewModel, String productId) {
        return uploadProductImageUseCase.createObservable(UploadProductImageUseCase.createParams(productViewModel, productId, notificationCountListener)).map(new Func1<List<BasePictureViewModel>, ProductViewModel>() {
            @Override
            public ProductViewModel call(List<BasePictureViewModel> basePictureViewModels) {
                return productViewModel;
            }
        });
    }

    private Func1<ProductViewModel, ProductViewModel> removeUnusedParam(final ProductViewModel productFromDraft) {
        return new Func1<ProductViewModel, ProductViewModel>() {
            @Override
            public ProductViewModel call(ProductViewModel productFromServer) {
                return productUploadMapper.convertUnusedParamToNull(productFromServer, productFromDraft);
            }
        };
    }

    public static RequestParams createParams(ProductViewModel productViewModel, ProductSubmitNotificationListener notificationCountListener) {
        RequestParams params = RequestParams.create();
        params.putObject(PRODUCT_VIEW_MODEL, productViewModel);
        params.putObject(NOTIFICATION_COUNT_LISTENER, notificationCountListener);
        return params;
    }
}