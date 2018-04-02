package com.tokopedia.posapp.product.management.view.presenter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.management.domain.GetProductListManagementUseCase;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.subscriber.GetEtalaseSubscriber;
import com.tokopedia.posapp.product.management.view.subscriber.GetProductManagementSubsrciber;
import com.tokopedia.posapp.etalase.domain.GetEtalaseCacheUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * @author okasurya on 3/14/18.
 */

public class ProductManagementPresenter implements ProductManagement.Presenter {
    private static final String DEFAULT_PER_PAGE_COUNT = "20";
    private ProductManagement.View view;
    private GetEtalaseCacheUseCase getEtalaseUseCase;
    private GetProductListManagementUseCase getProductListManagementUseCase;
    private UserSession userSession;
    private int page = 0;

    @Inject
    public ProductManagementPresenter(GetProductListManagementUseCase getProductListManagementUseCase,
                                      GetEtalaseCacheUseCase getEtalaseUseCase,
                                      UserSession userSession) {
        this.getProductListManagementUseCase = getProductListManagementUseCase;
        this.getEtalaseUseCase = getEtalaseUseCase;
        this.userSession = userSession;
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
        getEtalaseUseCase.execute(RequestParams.EMPTY, new GetEtalaseSubscriber(view));
    }

    @Override
    public void load(String etalaseId) {
        getProductListManagementUseCase.execute(getRequestParam(page = 0, etalaseId), new GetProductManagementSubsrciber(view));
    }

    @Override
    public void loadMore(String etalaseId) {
        getProductListManagementUseCase.execute(getRequestParam(page++, etalaseId), new GetProductManagementSubsrciber(view));
    }

    private RequestParams getRequestParam(int pageNo, String etalaseId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductConstant.Key.SHOP_ID, userSession.getShopId());
        requestParams.putString(ProductConstant.Key.ETALASE, etalaseId);
        requestParams.putString(ProductConstant.Key.PER_PAGE, DEFAULT_PER_PAGE_COUNT);
        requestParams.putString(ProductConstant.Key.PAGE, Integer.toString(pageNo));

        return requestParams;
    }
}
