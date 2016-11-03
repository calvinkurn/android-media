
package com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InboxReputationDetail {


    @SerializedName("list")
    @Expose
    java.util.List<InboxReputationDetailItem> inboxReputationDetailItem = new ArrayList<InboxReputationDetailItem>();
    @SerializedName("token")
    @Expose
    String token;

    /**
     *
     * @return
     *     The list
     */
    public java.util.List<InboxReputationDetailItem> getInboxReputationDetailItemList() {
        return inboxReputationDetailItem;
    }

    /**
     *
     * @param inboxReputationDetailItem
     *     The list
     */
    public void setInboxReputationDetailItem(java.util.List<InboxReputationDetailItem> inboxReputationDetailItem) {
        this.inboxReputationDetailItem = inboxReputationDetailItem;
    }

    /**
     *
     * @return
     *     The token
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     *     The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    public InboxReputationDetailItem getProduct(String productId) {
        for(InboxReputationDetailItem product : getInboxReputationDetailItemList()){
            if(product.getProductId().equals(productId)){
                return product;
            }
        }
        return null;
    }
}
