package com.tokopedia.posapp.outlet.view.presenter;

import com.tokopedia.posapp.outlet.domain.usecase.GetOutletUseCase;
import com.tokopedia.posapp.outlet.view.GetOutletSubscriber;
import com.tokopedia.posapp.outlet.view.Outlet;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

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

    @Inject
    public OutletPresenter(GetOutletUseCase outletUseCase) {
        this.getOutletUseCase = outletUseCase;
    }

    @Override
    public void attachView(Outlet.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getOutlet(String query) {
        view.startLoading();
        view.clearOutletData();

        getOutletUseCase.execute(RequestParams.EMPTY, new GetOutletSubscriber(view));
    }

    @Override
    public void setHasNextPage(String uriNext) {
//        pagingHandler.setHasNext(PagingHandler.CheckHasNext(uriNext));
//        if(pagingHandler.CheckNextPage()) {
//            PagingHandler.PagingHandlerModel pagingHandlerModel = new PagingHandler.PagingHandlerModel();
//            pagingHandlerModel.setUriNext(uriNext);
//            pagingHandler.setPagingHandlerModel(pagingHandlerModel);
//        }
    }

    @Override
    public void getNextOutlet(String query) {
//        try {
//            if (pagingHandler.CheckNextPage()) {
//                RequestParams params = AuthUtil.generateRequestParamsNetwork(context);
//                params.putString(PARAM_PAGE, pagingHandler.getNextPage() + "");
//                params.putString(PARAM_QUERY, query);
//                params.putString(PARAM_ORDER_BY, ORDER_BY_ADDRESS_NAME);
//                getOutletUseCase.execute(params, new GetOutletSubscriber(view()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
