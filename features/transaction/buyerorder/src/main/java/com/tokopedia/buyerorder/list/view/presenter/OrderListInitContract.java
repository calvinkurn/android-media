package com.tokopedia.buyerorder.list.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.buyerorder.list.data.OrderLabelList;
import com.tokopedia.buyerorder.list.data.ticker.TickerResponse;

import java.util.List;

public class OrderListInitContract {
     public interface Presenter {

         void getInitData(String query, String orderCategory);

         void destroyView();

         void getTickerInfo(Context context);
     }

    public interface View extends CustomerView {
        Context getActivity();

        void removeProgressBarView();

        void showErrorNetwork(String message);

        void renderTabs(List<OrderLabelList> orderLabelList, String orderCategory);

        void updateTicker(TickerResponse tickerInfo);
    }
}
