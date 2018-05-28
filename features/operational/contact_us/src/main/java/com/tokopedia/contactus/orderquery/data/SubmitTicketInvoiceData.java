package com.tokopedia.contactus.orderquery.data;

import com.tokopedia.contactus.common.data.BuyerPurchaseList;

import java.io.Serializable;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketInvoiceData implements Serializable{
    BuyerPurchaseList buyerPurchaseList;
    QueryTicket queryTicket;

    public BuyerPurchaseList getBuyerPurchaseList() {
        return buyerPurchaseList;
    }

    public void setBuyerPurchaseList(BuyerPurchaseList buyerPurchaseList) {
        this.buyerPurchaseList = buyerPurchaseList;
    }

    public QueryTicket getQueryTicket() {
        return queryTicket;
    }

    public void setQueryTicket(QueryTicket queryTicket) {
        this.queryTicket = queryTicket;
    }
}
