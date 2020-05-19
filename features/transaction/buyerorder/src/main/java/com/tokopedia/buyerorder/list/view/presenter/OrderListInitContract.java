package com.tokopedia.buyerorder.list.view.presenter;

import android.content.Context;

import java.util.List;

public class OrderListInitContract {
     public interface Presenter {

         void getInitData();

         void destroyView();

         void getTickerInfo();
     }

    public interface View {
        Context getAppContext();

        void removeProgressBarView();

        void showErrorNetwork(String message);

        void renderTabs(List<OrderLabelList> orderLabelList);

        void updateTicker(TickerResponse tickerInfo);
    }
}
