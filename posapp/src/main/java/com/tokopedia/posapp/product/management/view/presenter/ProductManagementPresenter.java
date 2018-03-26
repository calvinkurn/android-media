package com.tokopedia.posapp.product.management.view.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.posapp.product.management.domain.GetProductListManagementUseCase;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.subscriber.GetProductManagementSubsrciber;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.productdetail.view.GetProductSubscriber;
import com.tokopedia.usecase.RequestParams;

import org.apache.http.client.protocol.RequestAcceptEncoding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 3/14/18.
 */

public class ProductManagementPresenter implements ProductManagement.Presenter {
    private ProductManagement.View view;
    private GetProductListManagementUseCase getProductListManagementUseCase;
    private UserSession userSession;
    private int page = 0;

    @Inject
    public ProductManagementPresenter(GetProductListManagementUseCase getProductListManagementUseCase,
                                      UserSession userSession) {
        this.getProductListManagementUseCase = getProductListManagementUseCase;
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
    public void reload(String etalaseId) {
        getProductListManagementUseCase.execute(getRequestParam(page = 0, etalaseId), new GetProductManagementSubsrciber(view));
    }

    @Override
    public void loadMore(String etalaseId) {
        getProductListManagementUseCase.execute(getRequestParam(page++, etalaseId), new GetProductManagementSubsrciber(view));
    }

    private RequestParams getRequestParam(int pageNo, String etalaseId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("shop_id", userSession.getShopId());
        requestParams.putString("etalase", etalaseId);
        requestParams.putString("perpage", "20");
        requestParams.putString("page", Integer.toString(pageNo));

        return requestParams;
    }

    private List<Visitable> getMockList() {
        List<Visitable> list = new ArrayList<>();
        list.add(mock(1));
        list.add(mock(2));
        list.add(mock(3));
        list.add(mock(4));
        list.add(mock(5));
        return list;
    }

    private ProductViewModel mock(int i) {
        ProductViewModel pvm = new ProductViewModel();
        pvm.setId(Integer.toString(i));
        pvm.setImageUrl("https://dummyimage.com/64x64/000/fff");
        pvm.setName("Samsung Galaxy J8 2017 Gold Garansi 1 Tahun TAM asdfsd " + i);
        pvm.setOnlinePrice(1000000);
        pvm.setOutletPrice(1000000);
        pvm.setShown(true);
        return pvm;
    }
}
