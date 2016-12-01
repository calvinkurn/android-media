package com.tokopedia.core.inboxreputation.model.param;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 1/27/16.
 */
public class ReputationDetailPass {

    private static final String PARAM_REPUTATION_ID = "reputation_id";
    private static final String PARAM_BUYER_SELLER = "buyer_seller";
    private static final String PARAM_REPUTATION_INBOX_ID = "reputation_inbox_id";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_N = "n";


    private String act;
    private String reputationId;
    private int buyerSeller;
    private String reputationInboxId;
    private int page;
    private int n = 0;

    public ReputationDetailPass(){}

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getReputationId() {
        return reputationId;
    }

    public void setReputationId(String reputationId) {
        this.reputationId = reputationId;
    }

    public int getBuyerSeller() {
        return buyerSeller;
    }

    public void setBuyerSeller(int buyerSeller) {
        this.buyerSeller = buyerSeller;
    }

    public String getReputationInboxId() {
        return reputationInboxId;
    }

    public void setReputationInboxId(String reputationInboxId) {
        this.reputationInboxId = reputationInboxId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Map<String, String> getInboxReputationParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_BUYER_SELLER, String.valueOf(getBuyerSeller()));
        params.put(PARAM_PAGE, String.valueOf(getPage()));
        params.put(PARAM_REPUTATION_ID, getReputationId());
        params.put(PARAM_REPUTATION_INBOX_ID, getReputationInboxId());
        params.put(PARAM_N,String.valueOf(getN()));
        return params;
    }
}
