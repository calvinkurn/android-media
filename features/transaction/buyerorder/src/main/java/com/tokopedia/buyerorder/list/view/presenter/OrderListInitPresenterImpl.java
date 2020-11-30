package com.tokopedia.buyerorder.list.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.list.data.TabData;
import com.tokopedia.buyerorder.list.data.ticker.Input;
import com.tokopedia.buyerorder.list.data.ticker.TickerResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import timber.log.Timber;

public class OrderListInitPresenterImpl extends BaseDaggerPresenter<OrderListInitContract.View>  implements OrderListInitContract.Presenter {
    OrderListInitContract.View view;
    GraphqlUseCase initUseCase;
    GraphqlUseCase tickerUseCase;
    public OrderListInitPresenterImpl(OrderListInitContract.View view, GraphqlUseCase initUseCase) {
        this.view = view;
        this.initUseCase = initUseCase;
        this.tickerUseCase = initUseCase;
    }


    @Override
    public void getInitData(String query, String orderCategory) {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(query, TabData.class, false);
        initUseCase.addRequest(graphqlRequest);

        initUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e.toString());
                if (view != null) {
                    view.removeProgressBarView();
                    view.showErrorNetwork(e.toString());
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (view != null) {
                    view.removeProgressBarView();
                    if (response != null) {
                        TabData data = response.getData(TabData.class);
                        if (data != null && !data.getOrderLabelList().isEmpty()) {
                            view.renderTabs(data.getOrderLabelList(), orderCategory);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void destroyView() {
        initUseCase.unsubscribe();
        if (tickerUseCase != null)
            tickerUseCase.unsubscribe();
    }

    @Override
    public void getTickerInfo(Context context) {
        if (getView() != null) {
            Map<String, Object> requestInfo = new HashMap<>();
            Input input = new Input();
            UserSession userSession = new UserSession(context);
            input.setRequest_by("buyer");
            input.setUser_id(userSession.getUserId());
            input.setClient("mobile");

            requestInfo.put("input", input);
            tickerUseCase = new GraphqlUseCase();
            GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.tickerinfo), TickerResponse.class, requestInfo, false);
            tickerUseCase.addRequest(graphqlRequest);
            tickerUseCase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (view != null) {
                        e.printStackTrace();
                        view.showErrorNetwork(e.toString());
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    // Show ticker
                    if (graphqlResponse != null) {
                        TickerResponse tickerResponse = graphqlResponse.getData(TickerResponse.class);
                        if (view != null) {
                            view.updateTicker(tickerResponse);
                        }
                    }
                }
            });
        }
    }
}
