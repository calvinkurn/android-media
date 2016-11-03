package com.tokopedia.tkpd.selling.presenter;

import android.content.Context;

import com.tokopedia.tkpd.session.baseFragment.BaseImpl;


/**
 * Created by Toped10 on 7/18/2016.
 */
public abstract class NewOrder extends BaseImpl<NewOrderView> {
    public NewOrder(NewOrderView view) {
        super(view);
    }

    public abstract void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context);

    public abstract void getOrderList(boolean isVisibleToUser);

    public abstract void onQuerySubmit(String query);

    public abstract void onQueryChange(String newText);

    public abstract void onDeadlineSelected();

    public abstract void onRefreshView();

    public abstract void onScrollList(boolean isLastItemVisible);

    public abstract void moveToDetail(int position);

}
