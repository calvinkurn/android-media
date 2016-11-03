package com.tokopedia.tkpd.selling.presenter;

import android.content.Intent;
import android.view.View;

import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.List;

/**
 * Created by Toped10 on 7/28/2016.
 */
public interface ShippingView extends BaseView {

    /**
     * call when onCreate event
     */
    void initHandlerAndAdapter();

    boolean getUserVisible();

    void setRefreshPullEnabled(boolean b);

    void disableFilter();

    PagingHandler getPaging();

    String getSearchInvoice();

    int getDeadline();

    void enableFilter();

    void removeLoading();

    void setRefreshing(boolean b);

    void removeRetry();

    void finishRefresh();

    void notifyDataSetChanged(List<ShippingImpl.Model> modelList);

    void showProgressDialog();

    void hideProfressDialog();

    boolean isRefreshing();

    void addLoadingFooter();

    View getRootView();

    void addRetry();

    void initRefreshView();

    void setListener();

    void setAdapter();

    void initView();

    String getSelectedShipping();

    void showProgress();

    void hideFilter();

    void clearMultiSelector();

    void moveToDetailResult(Intent intent, int i);
}
