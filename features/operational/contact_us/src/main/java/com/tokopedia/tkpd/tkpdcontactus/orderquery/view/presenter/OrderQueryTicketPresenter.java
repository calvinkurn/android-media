package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsArticleUseCase;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsPurchaseListUseCase;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.QueryTicketUseCase;
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
    public OrderQueryTicketPresenter(QueryTicketUseCase queryTicketUseCase,  @ApplicationContext Context context) {
        this.queryTicketUseCase = queryTicketUseCase;
        this.context = context;
    }



    public RequestParams createRequestParam(BuyerPurchaseList purchaseList ) {
        RequestParams requestParams = RequestParams.create();
       /* requestParams.putString("order", 12517137+"");
        requestParams.putString("order_type", 1+"");
        requestParams.putString("product_type_id", 0+"");

        requestParams.putString("selected_ord_status",10+"");
        requestParams.putString("selected_pym_method",7+"");
        requestParams.putString("selected_pg_id",8+"");
*/


        requestParams.putString("order", buyerPurchaseList.getDetail().getId()+"");
        requestParams.putString("order_type", buyerPurchaseList.getDetail().getTypeId()+"");
        requestParams.putString("product_type_id", buyerPurchaseList.getDetail().getProductTypeId()+"");

        requestParams.putString("selected_ord_status",purchaseList.getDetail().getStatusId()+"");
        requestParams.putString("selected_pym_method",purchaseList.getPayment().getMethod()+"");
        requestParams.putString("selected_pg_id",purchaseList.getPayment().getGatewayId()+"");
        return requestParams;
    }

    BuyerPurchaseList buyerPurchaseList;
    @Override
    public void setBuyerPurchaseList(BuyerPurchaseList buyerPurchaseList) {
        this.buyerPurchaseList = buyerPurchaseList;
        getView().setInvoiceNumber(buyerPurchaseList.getDetail().getCode());
        getView().setPurchaseTitle(buyerPurchaseList.getProducts().get(0).getName());
        getView().setOrderImage(buyerPurchaseList.getProducts().get(0).getImage());
        queryTicketUseCase.execute(createRequestParam(buyerPurchaseList),new Subscriber<List<QueryTicket>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("contacus ", " onerror "+e);
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
