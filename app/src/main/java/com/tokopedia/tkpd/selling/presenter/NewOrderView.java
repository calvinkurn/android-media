package com.tokopedia.tkpd.selling.presenter;

import android.app.Fragment;
import android.content.Intent;
import android.view.View;

import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.List;

/**
 * Created by Toped10 on 7/18/2016.
 */
public interface NewOrderView extends BaseView {
    void initHandlerAndAdapter();

    boolean getUserVisible();

    void initView();

    void setRefreshPullEnable(boolean b);

    void disableFilter();

    String getQuery();

    int getDeadlineSelectionPos();

    void notifyDataSetChanged(List<OrderShippingList> listDatas);

    void finishRefresh();

    void removeRetry();

    void removeLoading();

    void enableFilter();

    View getRootView();

    void setRefreshing(boolean b);

    void addLoadingFooter();

    void initListener();

    void hideFilterView();

    boolean getRefreshing();

    void resetPage();

    PagingHandler getPaging();

    void addRetry();

    void moveToDetailResult(Intent intent, int code);
}
