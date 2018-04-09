package com.tokopedia.posapp.product.management.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.management.data.pojo.EditProductRequest;
import com.tokopedia.posapp.product.management.data.pojo.ProductPriceRequest;
import com.tokopedia.posapp.product.management.domain.EditProductLocalPriceUseCase;
import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author okasurya on 3/14/18.
 */

public class EditProductPresenter implements EditProduct.Presenter {
    private EditProduct.View view;
    private EditProductLocalPriceUseCase editProductLocalPriceUseCase;
    private PosSessionHandler posSessionHandler;
    private UserSession userSession;

    @Inject
    public EditProductPresenter(EditProductLocalPriceUseCase editProductLocalPriceUseCase,
                                PosSessionHandler posSessionHandler,
                                UserSession userSession) {
        this.editProductLocalPriceUseCase = editProductLocalPriceUseCase;
        this.posSessionHandler = posSessionHandler;
        this.userSession = userSession;
    }

    @Override
    public void attachView(EditProduct.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void save(ProductViewModel productViewModel, String price) {
        double localPrice = getPriceValue(price);
        if(localPrice != 0) {
            RequestParams requestParams = RequestParams.EMPTY;
            requestParams.putString(ProductConstant.Key.OUTLET_ID, posSessionHandler.getOutletId());

            EditProductRequest editProductRequest = new EditProductRequest();
            editProductRequest.setOutletId(Long.parseLong(posSessionHandler.getOutletId()));
            editProductRequest.setShopId(Long.parseLong(userSession.getShopId()));

            ProductPriceRequest productPriceRequest = new ProductPriceRequest();
            productPriceRequest.setPrice(localPrice);
            productPriceRequest.setProductId(Long.parseLong(productViewModel.getId()));
            productPriceRequest.setStatus(productViewModel.getStatus());
            List<ProductPriceRequest> priceRequestList = new ArrayList<>();
            priceRequestList.add(productPriceRequest);
            editProductRequest.setProductPrice(priceRequestList);

            requestParams.putObject(ProductConstant.Key.EDIT_PRODUCT_REQUEST, editProductRequest);

            editProductLocalPriceUseCase.execute(requestParams, new Subscriber<DataStatus>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    view.onErrorSave(e);
                }

                @Override
                public void onNext(DataStatus dataStatus) {
                    if(dataStatus.isOk()) view.onSuccessSave();
                    else view.onErrorSave(new RuntimeException(dataStatus.getMessage()));
                }
            });
        }
    }

    private double getPriceValue(String price) {
        try {
            if(!TextUtils.isEmpty(price)) {
                return Double.parseDouble(price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
