package com.tokopedia.tkpd.inboxreputation.model.param;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 21/01/16.
 */
public class InboxReputationPass {

    private static final String PARAM_ACT = "act";
    private static final String PARAM_NAV = "nav";
    private static final String PARAM_FILTER = "filter";
    private static final String PARAM_KEYWORD = "keyword";
    private static final String PARAM_PAGE = "page";

    private String act;
    private String nav;
    private String filter;
    private String keyword;
    private int page;

    public InboxReputationPass(){}

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Map<String, String> getInboxReputationParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ACT, getAct());
        params.put(PARAM_NAV, getNav());
        params.put(PARAM_FILTER, getFilter());
        params.put(PARAM_KEYWORD, getKeyword());
        params.put(PARAM_PAGE, String.valueOf(getPage()));
        return params;
    }

    public void reset() {
        setAct("");
        setNav("");
        setFilter("");
        setKeyword("");
        setPage(0);
    }
}
