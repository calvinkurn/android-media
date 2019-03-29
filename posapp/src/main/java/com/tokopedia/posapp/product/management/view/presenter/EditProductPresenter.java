package com.tokopedia.posapp.product.management.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.management.data.pojo.request.EditProductRequest;
import com.tokopedia.posapp.product.management.data.pojo.request.ProductPriceRequest;
import com.tokopedia.posapp.product.management.domain.EditProductUseCase;
import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.subscriber.EditProductSubscriber;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 3/14/18.
 */

public class EditProductPresenter implements EditProduct.Presenter {
    private EditProduct.View view;
    private EditProductUseCase editProductUseCase;
    private PosSessionHandler posSessionHandler;
    private UserSession userSession;

    @Inject
    public EditProductPresenter(EditProductUseCase editProductUseCase,
                                PosSessionHandler posSessionHandler,
                                UserSession userSession) {
        this.editProductUseCase = editProductUseCase;
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
    public void save(ProductViewModel productViewModel, String price, int position) {
        double localPrice = getPriceValue(price);
        if(localPrice != 0) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(ProductConstant.Key.OUTLET_ID, posSessionHandler.getOutletId());

            requestParams.putObject(ProductConstant.Key.EDIT_PRODUCT_REQUEST, getRequestModel(productViewModel, localPrice));

            editProductUseCase.execute(requestParams, new EditProductSubscriber(view, price, productViewModel, position));
        }
    }

    private EditProductRequest getRequestModel(ProductViewModel productViewModel, double localPrice) {
        EditProductRequest editProductRequest = new EditProductRequest();

        ProductPriceRequest productPriceRequest = new ProductPriceRequest();
        productPriceRequest.setPrice((int) localPrice);
        productPriceRequest.setProductId(Long.parseLong(productViewModel.getId()));
        productPriceRequest.setStatus(productViewModel.getStatus());
        productPriceRequest.setStock(1);
        productPriceRequest.setEtalaseId(productViewModel.getEtalaseId());

        List<ProductPriceRequest> priceRequestList = new ArrayList<>();
        priceRequestList.add(productPriceRequest);

        editProductRequest.setShopId(Long.parseLong(userSession.getShopId()));
        editProductRequest.setProductPrice(priceRequestList);
        editProductRequest.setOutletId(Long.parseLong(posSessionHandler.getOutletId()));

        return editProductRequest;
    }

    private double getPriceValue(String price) {
        try {
            if(!TextUtils.isEmpty(price)) {
                return Double.parseDouble(price.replace(".", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
