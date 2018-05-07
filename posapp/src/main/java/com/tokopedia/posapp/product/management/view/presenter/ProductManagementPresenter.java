package com.tokopedia.posapp.product.management.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.management.data.pojo.request.EditProductRequest;
import com.tokopedia.posapp.product.management.data.pojo.request.ProductPriceRequest;
import com.tokopedia.posapp.product.management.domain.EditProductUseCase;
import com.tokopedia.posapp.product.management.domain.GetProductListManagementUseCase;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.subscriber.EditProductStatusSubscriber;
import com.tokopedia.posapp.product.management.view.subscriber.GetProductManagementSubscriber;
import com.tokopedia.posapp.product.management.view.subscriber.LoadMoreProductManagementSubscriber;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.productdetail.view.Product;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 3/14/18.
 */

public class ProductManagementPresenter implements ProductManagement.Presenter {
    private static final String DEFAULT_PER_PAGE_COUNT = "20";
    private ProductManagement.View view;
    private GetProductListManagementUseCase getProductListManagementUseCase;
    private EditProductUseCase editProductUseCase;
    private UserSession userSession;
    private PosSessionHandler posSession;
    private int page = 1;

    @Inject
    public ProductManagementPresenter(GetProductListManagementUseCase getProductListManagementUseCase,
                                      EditProductUseCase editProductUseCase,
                                      UserSession userSession,
                                      PosSessionHandler posSession) {
        this.getProductListManagementUseCase = getProductListManagementUseCase;
        this.editProductUseCase = editProductUseCase;
        this.userSession = userSession;
        this.posSession = posSession;
    }

    @Override
    public void attachView(ProductManagement.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void reload() {
        getProductListManagementUseCase.execute(getRequestParam(page = 1), new GetProductManagementSubscriber(view));
    }

    @Override
    public void loadMore() {
        getProductListManagementUseCase.execute(getRequestParam(++page), new LoadMoreProductManagementSubscriber(view));
    }

    @Override
    public void editStatus(ProductViewModel productViewModel, boolean status, int position) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductConstant.Key.OUTLET_ID, posSession.getOutletId());

        requestParams.putObject(ProductConstant.Key.EDIT_PRODUCT_REQUEST, getRequestModel(productViewModel, status));

        editProductUseCase.execute(requestParams, new EditProductStatusSubscriber(view, position, productViewModel));
    }

    private RequestParams getRequestParam(int pageNo) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductConstant.Key.SHOP_ID, userSession.getShopId());
        requestParams.putString(ProductConstant.Key.PER_PAGE, DEFAULT_PER_PAGE_COUNT);
        requestParams.putString(ProductConstant.Key.PAGE, Integer.toString(pageNo));
        requestParams.putString(ProductConstant.Key.OUTLET_ID, posSession.getOutletId());

        return requestParams;
    }

    private EditProductRequest getRequestModel(ProductViewModel productViewModel, boolean status) {
        EditProductRequest editProductRequest = new EditProductRequest();

        ProductPriceRequest productPriceRequest = new ProductPriceRequest();
        if(status) productPriceRequest.setStatus(ProductConstant.Status.LOCAL_PRICE_SHOW);
        else productPriceRequest.setStatus(ProductConstant.Status.ONLINE_PRICE_HIDDEN);

        if(productViewModel.getOutletPriceUnformatted() == 0) {
            productPriceRequest.setPrice(0);
        } else {
            productPriceRequest.setPrice((int) productViewModel.getOutletPriceUnformatted());
        }

        productPriceRequest.setProductId(Long.parseLong(productViewModel.getId()));
        productPriceRequest.setStock(1);
        productPriceRequest.setEtalaseId(productViewModel.getEtalaseId());

        List<ProductPriceRequest> priceRequestList = new ArrayList<>();
        priceRequestList.add(productPriceRequest);

        editProductRequest.setShopId(Long.parseLong(userSession.getShopId()));
        editProductRequest.setProductPrice(priceRequestList);
        editProductRequest.setOutletId(Long.parseLong(posSession.getOutletId()));

        return editProductRequest;
    }
}
