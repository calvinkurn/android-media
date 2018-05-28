package com.tokopedia.contactus.orderquery.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.contactus.orderquery.data.QueryTicket;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 16/04/18.
 */

public class QueryTicketDetailPresenter extends BaseDaggerPresenter<QueryTicketDetailContract.View> implements QueryTicketDetailContract.Presenter {

    private final Context context;

    @Inject
    public QueryTicketDetailPresenter(@ApplicationContext Context context) {
        this.context = context;
    }
    @Override
    public void attachView(QueryTicketDetailContract.View view) {
        super.attachView(view);
        QueryTicket queryTicket = getView().getSubmitTicketInvoiceData().getQueryTicket();
        getView().setTitle(queryTicket.getName());
        getView().setDetail(queryTicket.getDescription().replace("text-align: center;",""));
    }
}
