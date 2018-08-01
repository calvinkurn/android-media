package com.tokopedia.posapp.outlet.view.presenter;

import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.usecase.DeleteAllCartUseCase;
import com.tokopedia.posapp.outlet.domain.usecase.GetOutletUseCase;
import com.tokopedia.posapp.outlet.domain.usecase.SelectOutletUseCase;
import com.tokopedia.posapp.outlet.view.GetOutletSubscriber;
import com.tokopedia.posapp.outlet.view.Outlet;
import com.tokopedia.posapp.outlet.view.viewmodel.OutletItemViewModel;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletPresenter implements Outlet.Presenter {
    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    private static final String ORDER_BY_ADDRESS_NAME = "2";
    private static final String FIRST_PAGE = "1";

    private Outlet.View view;
    private GetOutletUseCase getOutletUseCase;
    private SelectOutletUseCase selectOutletUseCase;
    private DeleteAllCartUseCase deleteAllCartUseCase;

    @Inject
    public OutletPresenter(GetOutletUseCase outletUseCase,
                           SelectOutletUseCase selectOutletUseCase,
                           DeleteAllCartUseCase deleteAllCartUseCase) {
        this.getOutletUseCase = outletUseCase;
        this.selectOutletUseCase = selectOutletUseCase;
        this.deleteAllCartUseCase = deleteAllCartUseCase;
    }

    @Override
    public void attachView(Outlet.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        getOutletUseCase.unsubscribe();
        selectOutletUseCase.unsubscribe();
        deleteAllCartUseCase.unsubscribe();
    }

    @Override
    public void getOutlet(String query) {
        view.startLoading();
        view.clearOutletData();
        getOutletUseCase.execute(RequestParams.create(), new GetOutletSubscriber(view));
        deleteAllCartUseCase.execute(RequestParams.create(), new Subscriber<ATCStatusDomain>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ATCStatusDomain atcStatusDomain) {
            }
        });
    }

    @Override
    public void setHasNextPage(String uriNext) {

    }

    @Override
    public void getNextOutlet(String query) {

    }

    @Override
    public void selectOutlet(OutletItemViewModel outlet) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductConstant.Key.OUTLET_ID, outlet.getOutletId());
        requestParams.putString(ProductConstant.Key.OUTLET_NAME, outlet.getOutletName());
        selectOutletUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.onErrorSelectOutlet();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                view.onOutletSelected();
            }
        });
    }
}
