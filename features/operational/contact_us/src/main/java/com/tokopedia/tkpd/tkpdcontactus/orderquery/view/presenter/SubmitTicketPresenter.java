package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.SubmitTicketUsecase;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketPresenter extends BaseDaggerPresenter<SubmitTicketContract.View> implements SubmitTicketContract.Presenter {
    Context context;
    SubmitTicketUsecase submitTicketUsecase;

    @Inject
    public SubmitTicketPresenter(@ApplicationContext Context context, SubmitTicketUsecase submitTicketUsecase) {
        this.context = context;
        this.submitTicketUsecase = submitTicketUsecase;
    }

    @Override
    public void attachView(SubmitTicketContract.View view) {
        super.attachView(view);
        BuyerPurchaseList buyerPurchaseList = getView().getSubmitTicketInvoiceData().getBuyerPurchaseList();
        QueryTicket queryTicket = getView().getSubmitTicketInvoiceData().getQueryTicket();
        getView().setInvoiceImage(buyerPurchaseList.getProducts().get(0).getImage());
        getView().setInvoiceNumber(buyerPurchaseList.getDetail().getCode());
        getView().setInvoiceTitle(buyerPurchaseList.getProducts().get(0).getName());
        getView().setQueryTitle(queryTicket.getName());
    }
}
