package com.tokopedia.buyerorder.list.view.presenter;

import android.content.Context;

import com.tokopedia.buyerorder.list.data.OrderLabelList;
import com.tokopedia.buyerorder.list.data.ticker.TickerResponse;

import java.util.List;

public class OrderListInitContract {
     public interface Presenter {

         void getInitData(String orderCategory);

         void destroyView();

         void getTickerInfo();
     }

    public interface View {
        Context getAppContext();

        void removeProgressBarView();

        void showErrorNetwork(String message);

        void renderTabs(List<OrderLabelList> orderLabelList, String orderCategory);

        void updateTicker(TickerResponse tickerInfo);
    }
}
