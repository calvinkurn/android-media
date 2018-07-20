package com.tokopedia.contactus.orderquery.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.view.presenter.ContactUsHomeContract;
import com.tokopedia.contactus.orderquery.data.QueryTicket;
import com.tokopedia.contactus.orderquery.domain.QueryTicketUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class OrderQueryTicketPresenter extends BaseDaggerPresenter<OrderQueryTicketContract.View> implements OrderQueryTicketContract.Presenter {

    private final Context context;
    QueryTicketUseCase queryTicketUseCase;

    @Inject
    OrderQueryTicketPresenter(QueryTicketUseCase queryTicketUseCase, @ApplicationContext Context context) {
        this.queryTicketUseCase = queryTicketUseCase;
        this.context = context;
    }


    private RequestParams createRequestParam(BuyerPurchaseList purchaseList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("order", String.valueOf(buyerPurchaseList.getDetail().getId()));
        requestParams.putString("inv", String.valueOf(buyerPurchaseList.getDetail().getCode()));
        requestParams.putString("order_type", String.valueOf(buyerPurchaseList.getDetail().getTypeId()));
        requestParams.putString("product_type_id", String.valueOf(buyerPurchaseList.getDetail().getProductTypeId()));

        requestParams.putString("selected_ord_status", String.valueOf(purchaseList.getDetail().getStatusId()));
        requestParams.putString("selected_pym_method", String.valueOf(purchaseList.getPayment().getMethod()));
        requestParams.putString("selected_pg_id", String.valueOf(purchaseList.getPayment().getGatewayId()));
        return requestParams;
    }

    private BuyerPurchaseList buyerPurchaseList;

    @Override
    public void setBuyerPurchaseList(BuyerPurchaseList buyerPurchaseList) {
        this.buyerPurchaseList = buyerPurchaseList;
        getView().setInvoiceNumber(buyerPurchaseList.getDetail().getCode());
        getView().setPurchaseTitle(buyerPurchaseList.getProducts().get(0).getName());
        getView().setOrderImage(buyerPurchaseList.getProducts().get(0).getImage());
        queryTicketUseCase.execute(createRequestParam(buyerPurchaseList), new Subscriber<List<QueryTicket>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(ContactUsHomeContract.ContactUsName, " BuyerPurchase OnError " + e);
            }

            @Override
            public void onNext(List<QueryTicket> queryTickets) {
                for (QueryTicket response : queryTickets) {
                    getView().addTicket(response);
                }

            }
        });
    }

}
