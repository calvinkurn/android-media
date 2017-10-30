package com.tokopedia.posapp.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.posapp.domain.usecase.GetOutletUseCase;
import com.tokopedia.posapp.domain.usecase.GetShopUseCase;
import com.tokopedia.posapp.view.Outlet;
import com.tokopedia.posapp.view.subscriber.GetOutletSubscriber;

import javax.inject.Inject;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletPresenter extends BaseDaggerPresenter<Outlet.View>
        implements Outlet.Presenter {
    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    private static final String ORDER_BY_ADDRESS_NAME = "2";
    private static final String FIRST_PAGE =  "1";

    private Context context;
    private GetOutletUseCase getOutletUseCase;

    private PagingHandler pagingHandler;

    @Inject
    public OutletPresenter(@ApplicationContext Context context, GetOutletUseCase outletUseCase) {
        this.context = context;
        this.getOutletUseCase = outletUseCase;
        pagingHandler = new PagingHandler();
    }

    @Override
    public void attachView(Outlet.View view) {
        super.attachView(view);
    }

    @Override
    public void getOutlet(String query) {
        getView().startLoading();
        getView().clearOutletData();

        RequestParams params = AuthUtil.generateRequestParamsNetwork(context);
        params.putString(PARAM_PAGE, FIRST_PAGE);
        params.putString(PARAM_QUERY, query);
        params.putString(PARAM_ORDER_BY, ORDER_BY_ADDRESS_NAME);

        getOutletUseCase.execute(params, new GetOutletSubscriber(getView()));
    }

    @Override
    public void setHasNextPage(String uriNext) {
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(uriNext));
        if(pagingHandler.CheckNextPage()) {
            PagingHandler.PagingHandlerModel pagingHandlerModel = new PagingHandler.PagingHandlerModel();
            pagingHandlerModel.setUriNext(uriNext);
            pagingHandler.setPagingHandlerModel(pagingHandlerModel);
        }
    }

    @Override
    public void getNextOutlet(String query) {
        try {
            if (pagingHandler.CheckNextPage()) {
                RequestParams params = AuthUtil.generateRequestParamsNetwork(context);
                params.putString(PARAM_PAGE, pagingHandler.getNextPage() + "");
                params.putString(PARAM_QUERY, query);
                params.putString(PARAM_ORDER_BY, ORDER_BY_ADDRESS_NAME);

                getOutletUseCase.execute(params, new GetOutletSubscriber(getView()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
